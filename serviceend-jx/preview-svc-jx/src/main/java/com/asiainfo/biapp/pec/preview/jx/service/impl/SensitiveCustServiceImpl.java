package com.asiainfo.biapp.pec.preview.jx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.common.jx.constant.CommonConstJx;
import com.asiainfo.biapp.pec.common.jx.model.McdDimChannel;
import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import com.asiainfo.biapp.pec.common.jx.service.McdDimChannelService;
import com.asiainfo.biapp.pec.common.jx.service.impl.BitMapRoaringImp;
import com.asiainfo.biapp.pec.common.jx.util.BitmapCacheUtil;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustConf;
import com.asiainfo.biapp.pec.preview.jx.service.ISensitiveCustService;
import com.asiainfo.biapp.pec.preview.jx.service.McdChannelSensitiveCustConfService;
import com.asiainfo.biapp.pec.preview.jx.util.CustgroupUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mamp
 * @date 2023/4/20
 */
@Service
@Slf4j
public class SensitiveCustServiceImpl implements ISensitiveCustService {

    @Resource
    private McdChannelSensitiveCustConfService sensitiveCustConfService;

    @Resource
    private CustgroupUtil custgroupUtil;


    @Resource(name = "commonMcdDimChannelServiceImpl")
    private McdDimChannelService channelService;

    /**
     * 更新bitMap数据
     *
     * @param channelId
     */
    @Override
    public void refreshSensitiveBitmap(String channelId) throws Exception {

        LambdaQueryWrapper<McdChannelSensitiveCustConf> query = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(channelId)) {
            query.eq(McdChannelSensitiveCustConf::getChannelId, channelId);
        }
        query.select(McdChannelSensitiveCustConf::getChannelId, McdChannelSensitiveCustConf::getCustgroupId);
        List<McdChannelSensitiveCustConf> list = sensitiveCustConfService.list(query);
        Map<String, List<McdChannelSensitiveCustConf>> collect = list.stream().collect(Collectors.groupingBy(McdChannelSensitiveCustConf::getChannelId));
        // 获取所有在线渠道的ID
        List<String> channelIdList = getOnlineChannelIds();
        for (Map.Entry<String, List<McdChannelSensitiveCustConf>> entry : collect.entrySet()) {
            // 渠道ID
            String key = entry.getKey();
            channelIdList.remove(key);
            log.info("开始生成渠道:{}敏感客户群bitMap", key);
            IBitMap bitMap = new BitMapRoaringImp(true);
            for (McdChannelSensitiveCustConf conf : entry.getValue()) {
                IBitMap custgroupBitmap = custgroupUtil.getCustgroupBitmap(conf.getCustgroupId(), null);
                if (null != custgroupBitmap) {
                    // 并集
                    bitMap.orWithModify(custgroupBitmap);
                }
            }
            log.info("渠道:{}敏感客户群bitMap生成,开始同步数据到redis", key);
            BitmapCacheUtil.pushSensitiveBitmap(key, bitMap);
            log.info("渠道:{}敏感客户群bitMap同步到redis完成,客户数量:{}", key, bitMap.getCount());
        }
        // 敏感客户群被清空的渠道bitmap也要清空
        if (CollectionUtil.isNotEmpty(channelIdList)) {
            for (String chnId : channelIdList) {
                BitmapCacheUtil.pushSensitiveBitmap(chnId, new BitMapRoaringImp(true));
            }
        }

        Map<Object, Object> map = RedisUtils.getMap(CommonConstJx.CAMP_SENSITIVE_CUST_ENUMTYPE);
        if (CollectionUtil.isEmpty(map)) {
            log.info("活动级敏感客户群配置为空");
            return;
        }
        log.info("活动级敏感客户群配置:{}", JSONUtil.toJsonStr(map));
        log.info("开始生成活动级敏感客户群bitmap");
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            String custgroupId = String.valueOf(entry.getValue());
            IBitMap custgroupBitmap = custgroupUtil.getCustgroupBitmap(custgroupId, null);
            if (null != custgroupBitmap) {
                BitmapCacheUtil.pushCampSensitiveBitmap(custgroupId, custgroupBitmap);
                log.info("活动级敏感客户群:{}bitmap已经生成并同步到reids", custgroupId);
                continue;
            }
            log.error("活动级敏感客户群:{}bitmapt生成失败", custgroupId);
        }
        log.info("活动级敏感客户群bitmap生成完成");

    }


    /**
     * 客户群清单更新后，更新其所在渠道的敏感客户群bitmap缓存
     *
     * @param custgroupId
     */
    @Override
    public void refreshSensitiveBitmapByCustgroupId(String custgroupId) {
        if (StrUtil.isEmpty(custgroupId)) {
            return;
        }
        try {
            // 查询敏感客户群包含 custgroupId 的所有渠道, 因为 custgroupId 更新了,对应渠道的
            // 敏感客户群bitmap都需要更新
            LambdaQueryWrapper<McdChannelSensitiveCustConf> query = new LambdaQueryWrapper<>();
            query.eq(McdChannelSensitiveCustConf::getCustgroupId, custgroupId)
                    .select(McdChannelSensitiveCustConf::getChannelId)
                    .groupBy(McdChannelSensitiveCustConf::getChannelId);
            List<McdChannelSensitiveCustConf> list = sensitiveCustConfService.list(query);
            if (CollectionUtil.isEmpty(list)) {
                log.info("客户群:{}不在任务渠道的敏感客户群中,客户群bitmap缓存不需要更新", custgroupId);
                return;
            }
            List<String> channelIs = list.stream().map(l -> l.getChannelId()).collect(Collectors.toList());
            log.info("客户群:{}是渠道:{}的敏感客户群,需要更新这些渠道敏感客户群bitmap缓存", custgroupId, JSONUtil.toJsonStr(channelIs));
            for (McdChannelSensitiveCustConf conf : list) {
                refreshSensitiveBitmap(conf.getChannelId());
            }

            Map<Object, Object> map = RedisUtils.getMap(CommonConstJx.CAMP_SENSITIVE_CUST_ENUMTYPE);
            if (CollectionUtil.isEmpty(map)) {
                return;
            }
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                if (!custgroupId.equals(entry.getValue())) {
                    continue;
                }
                IBitMap custgroupBitmap = custgroupUtil.getCustgroupBitmap(custgroupId, null);
                if (null != custgroupBitmap) {
                    BitmapCacheUtil.pushCampSensitiveBitmap(custgroupId, custgroupBitmap);
                }
            }
        } catch (Exception e) {
            log.error("通过客户群ID更新敏感客群异常,custgroupId:{}", custgroupId);
        }
    }

    /**
     * 获取所有状态为”在线"渠道的ID
     *
     * @return
     */
    private  List<String> getOnlineChannelIds() {
        LambdaQueryWrapper<McdDimChannel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdDimChannel::getOnlineStatus, "1").select(McdDimChannel::getChannelId);
        List<McdDimChannel> channelList = channelService.list(queryWrapper);
        List<String> channelIdList = channelList.stream().map(c -> c.getChannelId()).collect(Collectors.toList());
        if (null == channelIdList) {
            channelIdList = new ArrayList<>();
        }
        return channelIdList;
    }
}
