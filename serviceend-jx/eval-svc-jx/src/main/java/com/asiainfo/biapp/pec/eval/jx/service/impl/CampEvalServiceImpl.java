package com.asiainfo.biapp.pec.eval.jx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.core.utils.DateUtil;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.eval.jx.dao.CampEvalMapper;
import com.asiainfo.biapp.pec.eval.jx.req.CampEvalReq;
import com.asiainfo.biapp.pec.eval.jx.req.ChannelRadarMapReq;
import com.asiainfo.biapp.pec.eval.jx.service.CampEvalService;
import com.asiainfo.biapp.pec.eval.jx.vo.CampEvalVo;
import com.asiainfo.biapp.pec.eval.jx.vo.UseEffectVo;
import com.asiainfo.biapp.pec.eval.util.CommonUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.record.PageBreakRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mamp
 * @date 2023/1/9
 */
@Service
@Slf4j
public class CampEvalServiceImpl implements CampEvalService {

    @Resource
    private CampEvalMapper campEvalMapper;

    /**
     * 活动评估
     *
     * @param req
     * @return
     */
    @Override
    public IPage<CampEvalVo> campEval(CampEvalReq req) throws Exception {
        convertActivityType(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Page<CampEvalVo> page = new Page<>();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());
        // 日视图: 前一天数据
        if ("D".equals(req.getVeiwType())) {
            Date date = DateUtil.addDay(new Date(), -1);
            req.setStartDate(sdf.format(date) + "000000");
            req.setEndDate(sdf.format(date) + "235959");
            return campEvalMapper.queryCampEffectDay(page, req);
        }
        // 月视图： 当月数据
        if ("M".equals(req.getVeiwType())) {
            sdf = new SimpleDateFormat("yyyyMM");
            // 当前月第一天
            req.setStartDate(DateUtil.getFirstDayByMonth(sdf.format(new Date()), "yyyyMM", "yyyyMMdd") + "000000");
            // 当前月最后一天
            req.setEndDate(DateUtil.getLastDayByMonth(sdf.format(new Date()), "yyyyMM", "yyyyMMdd") + "235959");
            return campEvalMapper.queryCampEffectMonth(page, req);
        }
        // 周视图: 上一周数据
        if ("W".equals(req.getVeiwType())) {
            Date lastWeekFirstDay = sdf.parse(DateUtil.getLastWeekdayU("yyyyMMdd"));
            // 上周第一天
            req.setStartDate(DateUtil.getLastWeekdayU("yyyyMMdd") + "000000");
            // 上周最后一天
            req.setEndDate(DateUtil.getLastWeek() + "235959");
            return campEvalMapper.queryCampEffectDay(page, req);
        }
        return campEvalMapper.queryCampEffect(page, req);
    }

    /**
     * 转换活动类型枚举值 ,前台传的是枚举key,表里存的是枚举value（汉字）
     *
     * @param req
     */
    private void convertActivityType(CampEvalReq req) {
        Map<Object, Object> objectMap = RedisUtils.getMap("activity_type");
        if (CollectionUtil.isEmpty(objectMap)) {
            log.warn("redis中没有查到活动类型枚举值(activity_type)");
            return;
        }
        String activityType = req.getCampsegType();
        if (StrUtil.isEmpty(activityType) || "0".equals(activityType) || "-1".equals(activityType)) {
            return;
        }
        activityType = objectMap.get(activityType) == null ? null : String.valueOf(objectMap.get(activityType));
        if (StrUtil.isEmpty(activityType)) {
            log.warn("枚举值{},在activity_type中不存在", req.getCampsegType());
            return;
        }
        req.setCampsegType(activityType);
    }

    @Override
    public List<List<String>> campEvalExport(CampEvalReq req) throws Exception {

        IPage<CampEvalVo> iPage = campEval(req);
        List<String> list1;
        List<List<String>> list2 = new ArrayList<>();
        for (CampEvalVo record : iPage.getRecords()) {
            list1 = new ArrayList<>();
            list1.add(record.getStartDate());
            list1.add(record.getEndDate());
            list1.add(record.getCampsegTypeName());
            list1.add(record.getCampsegRootId());
            list1.add(record.getCampsegName());
            list1.add(record.getCampsegTypeName());
            list1.add(record.getPlanName());
            list1.add(record.getCityName());
            list1.add(record.getCountyName());
            list1.add(record.getChannelName());
            list1.add(String.valueOf(record.getTotalNum()));
            list1.add(String.valueOf(record.getContactNum()));
            list1.add(String.valueOf(record.getContactRate()));
            list1.add(String.valueOf(record.getSuccessNum()));
            list1.add(String.valueOf(record.getSuccessRate()));
            list2.add(list1);
        }
        return list2;
    }

    /**
     * 查询日使用效果
     *
     * @param dataDate
     * @return
     */
    @Override
    public List<UseEffectVo> queryDayUseEffect(String dataDate) {
        return campEvalMapper.queryDayUseEffect(dataDate);
    }

    /**
     * 查询月使用效果
     *
     * @param dataDate
     * @return
     */
    @Override
    public List<UseEffectVo> queryMonthUseEffect(String dataDate) {
        return campEvalMapper.queryMonthUseEffect(dataDate);
    }

    /**
     * 查询近6个月使用效果
     *
     * @param dataDate
     * @return
     */
    @Override
    public List<UseEffectVo> queryMonth6UseEffect(String dataDate) {
        return null;
    }

    /**
     * 渠道雷达图数据查询
     *
     * @param req req
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> queryChannelRadarMap(ChannelRadarMapReq req) {
        // 1. 查询执行中、已完成所属各渠道活动总数
        List<Map<String, String>> queryChannelCampCount = campEvalMapper.queryChannelCampCount();
        // 2. 查询执行中、已完成活动总数
        int campCount = campEvalMapper.queryCampCount();
        List<Map<String, Object>> channelList = new ArrayList<>();
        for (Map<String, String> map : queryChannelCampCount) {
            Map<String, Object> objectMap = new HashMap<>();
            // 3. 计算渠道覆盖率
            double div = NumberUtil.div(Integer.parseInt(String.valueOf(map.get("fenzi"))), campCount, 4, RoundingMode.DOWN);
            String percent = NumberUtil.decimalFormat("0.00%", div);
            objectMap.put("CHANNEL_ID", map.get("CHANNEL_ID"));
            objectMap.put("fenzi", percent);
            channelList.add(objectMap);
        }
        // 4. 计算触达率、转化率
        List<Map<String, Object>> mapList = campEvalMapper.queryChannelRadarMap(req);
        List<String> numFileds = Arrays.asList("CHANNEL_CHUDA_RADIO", "CHANNEL_ZHUANHUA_RADIO");
        CommonUtil.convertNumFiled(mapList, numFileds);
        // 5. 合并结果返回前端
        mapList.forEach(map -> {
            channelList.forEach(objectMap -> {
                if (map.get("CHANNEL").equals(objectMap.get("CHANNEL_ID"))) {
                    map.put("CHANNEL_FUGAI_RADIO", objectMap.get("fenzi"));
                }
            });
        });
        return mapList;
    }

}
