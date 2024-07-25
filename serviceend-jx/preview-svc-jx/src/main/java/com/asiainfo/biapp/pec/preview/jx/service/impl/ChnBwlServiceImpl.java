package com.asiainfo.biapp.pec.preview.jx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.common.jx.model.McdDimChannel;
import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import com.asiainfo.biapp.pec.common.jx.service.McdDimChannelService;
import com.asiainfo.biapp.pec.common.jx.service.impl.BitMapRoaringImp;
import com.asiainfo.biapp.pec.common.jx.util.BitmapCacheUtil;
import com.asiainfo.biapp.pec.preview.jx.entity.McdSpecialUse;
import com.asiainfo.biapp.pec.preview.jx.mapper.McdSpecialUseMapper;
import com.asiainfo.biapp.pec.preview.jx.service.IChnBwlService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mamp
 * @date 2022/9/29
 */
@Service
@Slf4j
@RefreshScope
public class ChnBwlServiceImpl implements IChnBwlService {


    @Value("${pec.preview.bwl.pageSize:10000}")
    private int pageSize;

    @Resource
    private McdSpecialUseMapper mapper;


    @Resource(name = "commonMcdDimChannelServiceImpl")
    private McdDimChannelService channelService;


    /**
     * 更新免打扰 bitMap数据
     */
    @Override
    public void refreshBwlBitmap(String channelId) {

        List<McdSpecialUse> mcdSpecialUses;
        if (StrUtil.isEmpty(channelId)) {
            // 更新所有渠道
            QueryWrapper<McdSpecialUse> queryChannelIdWrapper = new QueryWrapper<>();
            queryChannelIdWrapper.lambda().select(McdSpecialUse::getChannelId).groupBy(McdSpecialUse::getChannelId);
            mcdSpecialUses = mapper.selectList(queryChannelIdWrapper);
        } else {
            // 更新指定渠道
            McdSpecialUse mcdSpecialUse = new McdSpecialUse();
            mcdSpecialUse.setChannelId(channelId);
            mcdSpecialUses = new ArrayList<>(2);
            mcdSpecialUses.add(mcdSpecialUse);
        }

        List<String> onlineChannelIds = getOnlineChannelIds();
        for (McdSpecialUse mcdSpecialUse : mcdSpecialUses) {
            log.info("开始更新渠道:{}免打扰数据...", mcdSpecialUse.getChannelId());
            onlineChannelIds.remove(mcdSpecialUse.getChannelId());
            QueryWrapper<McdSpecialUse> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .select(McdSpecialUse::getProductNo)
                    .eq(McdSpecialUse::getChannelId, mcdSpecialUse.getChannelId());
            IBitMap bitMap = new BitMapRoaringImp(true);
            IPage<McdSpecialUse> page = new Page<>();
            page.setSize(pageSize);
            page.setTotal(mapper.selectCount(queryWrapper));
            Long phoneNo;
            for (int i = 0; i < page.getPages(); i++) {
                page.setCurrent(i + 1);
                IPage<McdSpecialUse> pageList = mapper.selectPage(page, queryWrapper);
                for (McdSpecialUse record : pageList.getRecords()) {
                    phoneNo = parseLong(record.getProductNo());
                    if (null != phoneNo) {
                        bitMap.add(phoneNo);
                    }
                }
            }
            BitmapCacheUtil.pushBwlBitmap(mcdSpecialUse.getChannelId(), bitMap);
            log.info("渠道:{}免打扰数据已更新完成,免打扰用户数量: {}", mcdSpecialUse.getChannelId(), bitMap.getCount());
        }
        // 没有免打扰用户的渠道bitmap也要清空
        if (CollectionUtil.isNotEmpty(onlineChannelIds)) {
            for (String chnId : onlineChannelIds) {
                BitmapCacheUtil.pushBwlBitmap(chnId, new BitMapRoaringImp(true));
            }
        }


    }


    /**
     * 获取所有状态为”在线"渠道的ID
     *
     * @return
     */
    private List<String> getOnlineChannelIds() {
        LambdaQueryWrapper<McdDimChannel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdDimChannel::getOnlineStatus, "1").select(McdDimChannel::getChannelId);
        List<McdDimChannel> channelList = channelService.list(queryWrapper);
        List<String> channelIdList = channelList.stream().map(c -> c.getChannelId()).collect(Collectors.toList());
        if (null == channelIdList) {
            channelIdList = new ArrayList<>();
        }
        return channelIdList;
    }

    /**
     * 将str解析成long
     *
     * @param str
     * @return
     */
    private Long parseLong(String str) {
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            log.error("error:{}", e.getMessage());
        }
        return null;
    }
}
