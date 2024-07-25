package com.asiainfo.biapp.pec.preview.jx.thread;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import com.asiainfo.biapp.pec.common.jx.service.impl.BitMapRoaringImp;
import com.asiainfo.biapp.pec.common.jx.util.BitmapCacheUtil;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.fcc.FccUtil;
import com.asiainfo.biapp.pec.fcc.common.model.FilterResult;
import com.asiainfo.biapp.pec.fcc.constant.OrderedType;
import com.asiainfo.biapp.pec.fcc.frequency.model.FrequencyRequest;
import com.asiainfo.biapp.pec.fcc.jx.FccUtilJx;
import com.asiainfo.biapp.pec.fcc.jx.ordered.model.OrderedFilterRequestJx;
import com.asiainfo.biapp.pec.fcc.jx.ordered.model.RecommendFilterBatchRequest;
import com.asiainfo.biapp.pec.fcc.jx.ordered.model.RecommendFilterBatchResult;
import com.asiainfo.biapp.pec.preview.jx.config.PlanPreBitMapCache;
import com.asiainfo.biapp.pec.preview.jx.entity.McdCampPreviewLog;
import com.asiainfo.biapp.pec.preview.jx.entity.McdPreviewInfo;
import com.asiainfo.biapp.pec.preview.jx.model.McdPreviewLogResult;
import com.asiainfo.biapp.pec.preview.jx.service.McdCampPreviewLogService;
import com.asiainfo.biapp.pec.preview.jx.service.McdCampPreviewService;
import com.asiainfo.biapp.pec.preview.jx.util.CustgroupUtil;
import com.asiainfo.biapp.pec.preview.jx.util.PreviewConst;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author mamp
 * @date 2022/9/22
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class McdPreviewThread implements Runnable {

    private static final Integer REDIS_GET_BATCH = 1000;

    /**
     * 活动预演信息
     */
    private McdPreviewInfo previewInfo;

    /**
     * 预演服务
     */
    private McdCampPreviewService campPreviewService;

    /**
     * 预演日志服务
     */
    private McdCampPreviewLogService campPreviewLogService;

    /**
     * 客户群相关操作工具对象
     */
    private CustgroupUtil custgroupUtil;


    @Override
    public void run() {
        Thread.currentThread().setName(StrUtil.format("[预演任务线程:{}]", previewInfo.getCampsegId()));
        McdPreviewLogResult result = new McdPreviewLogResult();
        result.setChnannelId(previewInfo.getChannelId());
        result.setCampsegId(previewInfo.getCampsegId());
        McdCampPreviewLog previewLog = new McdCampPreviewLog();
        previewLog.setStartTime(new Date());
        try {
            campPreviewService.updateCampPreviewStatus(previewInfo, PreviewConst.PREVIEW_STATUS_RUNNING);
            log.info("活动:{},渠道：{}预演任务开始执行", previewInfo.getCampsegId(), previewInfo.getChannelId());
            // 是否渠道偏好推送
            boolean isChnPrePush = "0".equals(previewInfo.getExt1());
            // 原始客户群数量
            int custgroupTotal = previewInfo.getTargetUserNum();
            result.setCustgroupTotal(custgroupTotal);
            result.setCurrentCustNum(custgroupTotal);

            IBitMap custBitmap;
            // 1. 渠道偏好过滤
            if (isChnPrePush) {
                log.info("活动:{},渠道:{},偏好推送", previewInfo.getCampsegId(), previewInfo.getChannelId());
                custBitmap = custgroupUtil.getCustgroupBitmap(previewInfo.getCustomGroupId(), previewInfo.getChannelId());
                // 渠道偏好的数量
                int channelPreCustNum = custBitmap.getCount();
                // *** 渠道偏好过滤数量***
                result.setChnPreFilterCnt(custgroupTotal - channelPreCustNum);
            } else {
                custBitmap = custgroupUtil.getCustgroupBitmap(previewInfo.getCustomGroupId(), null);
                log.info("活动:{},渠道:{},全量推送", previewInfo.getCampsegId(), previewInfo.getChannelId());
            }
            IBitMap currentBitmap = new BitMapRoaringImp(true);
            currentBitmap = currentBitmap.orWithModify(custBitmap);
            result.setCurrentCustNum(currentBitmap.getCount());
            log.info("渠道偏好过滤完成,result={}", result);

            // 2. 免打扰过滤
            bwlFilter(result, currentBitmap);
            // 3. 敏感客户群过滤
            sensitiveCustFilter(result, currentBitmap);
            // 4. 已办理过滤
            orderedFilter(result, currentBitmap);
            // 5. 已办理及互斥过滤
            orderedExcludeFilter(result, currentBitmap);
            // 6. 已推荐过滤
            if (campPreviewService.getRecommendFilterChnIds().contains(previewInfo.getChannelId())) {
                recommendFilter(result, currentBitmap);
            } else {
                log.warn("渠道[{}]需要已推荐过滤,当前渠道:{},活动:{}", campPreviewService.getRecommendFilterChnIds(), previewInfo.getChannelId(), previewInfo.getCampsegId());
            }
            // 7. 频次过滤
            if (campPreviewService.getFqcFilterChnIds().contains(previewInfo.getChannelId())) {
                fqcFilter(result, currentBitmap);
            } else {
                log.info("渠道[{}]需要频次过滤,当前渠道:{},活动:{}", campPreviewService.getFqcFilterChnIds(), previewInfo.getChannelId(), previewInfo.getCampsegId());
            }
            // 8. 转换率预测
            caculateSuccessRate(currentBitmap, previewLog);

            // 生成预演日志数据
            setCampPreviewLogResult(previewLog, result);
            previewLog.setStatus(PreviewConst.PREVIEW_STATUS_DONE);
            previewLog.setEndTime(new Date());

            log.info("预演日志:{}", previewLog);
            // 保存预演日志,先删除旧数据
            QueryWrapper<McdCampPreviewLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(McdCampPreviewLog::getCampsegId, previewInfo.getCampsegId()).eq(McdCampPreviewLog::getChannelId, previewInfo.getChannelId());
            campPreviewLogService.remove(queryWrapper);
            // 再插入新数据
            campPreviewLogService.save(previewLog);
            log.info("campPreviewLog 日志保存成功，campsegId={},channelId={}", previewInfo.getCampsegId(), previewInfo.getChannelId());
            // 更新预演状态为完成
            campPreviewService.updateCampPreviewStatus(previewInfo, PreviewConst.PREVIEW_STATUS_DONE);
            log.info("更新campPreview成功，campsegId={},channelId={},status={}", previewInfo.getCampsegId(), previewInfo.getChannelId(), PreviewConst.PREVIEW_STATUS_DONE);
            // 预演完成后，更新channel_list表状态由预演中改为预演完成
            campPreviewService.updateCampChannelListStatus(previewInfo, PreviewConst.CAMPSEG_STATUS_PREVIEW_SUCCESS, PreviewConst.CAMPSEG_STATUS_PREVIEW_RUNNING);
            log.info("更新CampChannelList状态为预演完成,campsegId={},channelId={},status={}", previewInfo.getCampsegId(), previewInfo.getChannelId(), PreviewConst.CAMPSEG_STATUS_PREVIEW_SUCCESS);
            // 如果所有渠道都预演完成,更新活动状态为预演完成
            if (campPreviewService.isAllChannelPreviewFinish(previewInfo.getCampsegRootId())) {
                campPreviewService.updateCampsegStatus(previewInfo, PreviewConst.CAMPSEG_STATUS_PREVIEW_SUCCESS, PreviewConst.CAMPSEG_STATUS_PREVIEW_RUNNING);
                log.info("所有渠道都预演完成,更新活动状为预演完成,campsegId={},channelId={},status={}", previewInfo.getCampsegId(), previewInfo.getChannelId(), PreviewConst.CAMPSEG_STATUS_PREVIEW_SUCCESS);
            }
        } catch (Exception e) {
            log.error("活动:{},渠道:{},预演任务执行失败", previewInfo.getCampsegId(), previewInfo.getChannelId(), e);
            // 更新预演状态为预演失败
            campPreviewService.updateCampPreviewStatus(previewInfo, PreviewConst.PREVIEW_STATUS_ERROR);
            // 更新CampChannelList表状态为预演失败
            campPreviewService.updateCampChannelListStatus(previewInfo, PreviewConst.CAMPSEG_STATUS_PREVIEW_FAIL, PreviewConst.CAMPSEG_STATUS_PREVIEW_RUNNING);
            campPreviewService.updateCampsegStatus(previewInfo, PreviewConst.CAMPSEG_STATUS_PREVIEW_FAIL, PreviewConst.CAMPSEG_STATUS_PREVIEW_RUNNING);
        }
    }

    /**
     * 计算转化率
     *
     * @param currentBitmap
     * @return
     */
    private void caculateSuccessRate(IBitMap currentBitmap, McdCampPreviewLog previewLog) {
        log.info("开始计算转化率,活动:{}", previewInfo.getCampsegId());
        try {
            //  综合总得分
            double successScore = 0.0;
            // 综合转化率 = 综合总得分 / 预演后剩余用户总数
            double successRate = 0.0;
            // 产品转化率预测
            double planSucessRate = 0.0;
            // 渠道转化率预测
            double channelSuccessRate = 0.0;
            if (currentBitmap == null || currentBitmap.getCount() == 0) {
                log.warn("预演后客户群数量为0,活动:{}", previewInfo.getCampsegId());
                return;
            }
            // 渠道偏好
            IBitMap chnPreBitmap = BitmapCacheUtil.getChnPreBitmap(previewInfo.getChannelId());
            if (null != chnPreBitmap && chnPreBitmap.getCount() != 0) {
                // 渠道转化率预测
                channelSuccessRate = Double.valueOf(currentBitmap.andWithoutModify(chnPreBitmap).getCount()) / Double.valueOf(currentBitmap.getCount());
            } else {
                log.warn("活动:{},渠道偏好数据为空", previewInfo.getCampsegId());
            }
            previewLog.setChannelSuccessRate(channelSuccessRate);

            // 产品偏好Bitmap
            Map<String, IBitMap> bitMapMap = PlanPreBitMapCache.planPreBitMaps.get(previewInfo.getPlanId());
            // 偏好偏好平均值
            Map<String, Double> scoreMap = PlanPreBitMapCache.planPreScores.get(previewInfo.getPlanId());

            double planAvg1 = 0, planAvg2 = 0, planAvg3 = 0;
            IBitMap bitMap1 = null, bitMap2 = null, bitMap3 = null;
            // 产品偏好为空时,转换率预测值为 0
            if (CollectionUtil.isNotEmpty(bitMapMap) && CollectionUtil.isNotEmpty(scoreMap)) {

                // 第一区间用户Bitmap
                bitMap1 = bitMapMap.get("1") == null ? (new BitMapRoaringImp(true)) : bitMapMap.get("1");
                // 第一偏好区间的用户数量
                int userNum1 = currentBitmap.andWithoutModify(bitMap1).getCount();
                // 第二区间用户Bitmap
                bitMap2 = bitMapMap.get("2") == null ? (new BitMapRoaringImp(true)) : bitMapMap.get("2");
                // 第二偏好区间的用户数量
                int userNum2 = currentBitmap.andWithoutModify(bitMap2).getCount();
                // 第三区间用户Bitmap
                bitMap3 = bitMapMap.get("3") == null ? (new BitMapRoaringImp(true)) : bitMapMap.get("3");
                // 第三偏好区间的用户数量
                int userNum3 = currentBitmap.andWithoutModify(bitMap3).getCount();
                log.info("产品偏好用户数量，第一区间:{},第二区间:{},第三区间:{}", userNum1, userNum2, userNum3);
                log.info("预演后剩余用户总数:{}", currentBitmap.getCount());
                // 区间1的平均值
                planAvg1 = scoreMap.get("1") == null || bitMap1.getCount() == 0 ? 0 : scoreMap.get("1") / bitMap1.getCount();
                // 区间2的平均值
                planAvg2 = scoreMap.get("2") == null || bitMap2.getCount() == 0 ? 0 : scoreMap.get("2") / bitMap2.getCount();
                // 区间3的平均值
                planAvg3 = scoreMap.get("3") == null || bitMap3.getCount() == 0 ? 0 : scoreMap.get("3") / bitMap3.getCount();
                log.info("平均值，第一区间:{},第二区间:{},第三区间:{}", planAvg1, planAvg2, planAvg3);
                // 产品转化率预测
                planSucessRate = (userNum1 * planAvg1 + userNum2 * planAvg2 + userNum3 * planAvg3) / currentBitmap.getCount();
                log.info("产品转化率:{}", planSucessRate);
            } else {
                log.warn("活动:{},产品偏好数据为空", previewInfo.getCampsegId());
            }
            previewLog.setPlanSuccessRate(planSucessRate);

            // 渠道偏好或者产品偏好为空时,转换率预测值为 0
            if (chnPreBitmap == null || chnPreBitmap.getCount() == 0 || CollectionUtil.isEmpty(bitMapMap) || CollectionUtil.isEmpty(scoreMap)) {
                log.warn("活动:{},渠道偏好或者产品偏好数据为空,转化率为 0", previewInfo.getCampsegId());
                previewLog.setSuccessRate(successRate);
                return;
            }
            log.info("开始转换率预测计算,活动:{}", previewInfo.getCampsegId());
            long start = System.currentTimeMillis();
            Iterator<Long> iterator = currentBitmap.iterator();
            while (iterator.hasNext()) {
                Long next = iterator.next();
                if (!chnPreBitmap.contains(next)) {
                    continue;
                }
                if (bitMap1 != null && bitMap1.contains(next)) {
                    successScore += planAvg1;
                    continue;
                }
                if (bitMap2 != null && bitMap2.contains(next)) {
                    successScore += planAvg2;
                    continue;
                }
                if (bitMap3 != null && bitMap3.contains(next)) {
                    successScore += planAvg3;
                    continue;
                }
            }
            successRate = successScore / currentBitmap.getCount();
            previewLog.setSuccessRate(successRate);
            log.info("转换率预测计算完成,活动:{},结果:{},预演后客户群数量:{},耗时:{}", previewInfo.getCampsegId(), successRate, currentBitmap.getCount(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("转换率预测计算失败", e);
        }
    }


    /**
     * 免打扰过滤
     *
     * @param result
     * @param currentBitmap
     */
    private void bwlFilter(McdPreviewLogResult result, IBitMap currentBitmap) {
        IBitMap bwlBitmap = custgroupUtil.getBwlBitMapByChnId(previewInfo.getChannelId());
        currentBitmap.andNotWithModify(bwlBitmap);
        // *** 渠道偏好过滤数量***
        result.setBwlFilterCnt(result.getCurrentCustNum() - currentBitmap.getCount());
        result.setCurrentCustNum(currentBitmap.getCount());
        log.info("免打扰过滤完成,result={}", result);
    }

    /**
     * 敏感客户群过滤
     *
     * @param result
     * @param currentBitmap
     */
    private void sensitiveCustFilter(McdPreviewLogResult result, IBitMap currentBitmap) throws Exception {

        // 渠道级敏感客户群数据
        IBitMap sensitiveBitmap = BitmapCacheUtil.getSensitiveBitmap(result.getChnannelId());
        if (null != sensitiveBitmap) {
            currentBitmap.andNotWithModify(sensitiveBitmap);
        }
        // 活动级敏感客户群过滤
        if (StrUtil.isNotEmpty(previewInfo.getSensitiveCustIds())) {
            for (String custId : previewInfo.getSensitiveCustIds().split(",")) {
                IBitMap campSensitiveBitmap = BitmapCacheUtil.getCampSensitiveBitmap(custId);
                if (campSensitiveBitmap == null) {
                    continue;
                }
                currentBitmap.andNotWithModify(campSensitiveBitmap);
            }
        }
        // *** 敏感客户群过滤数量 ***
        result.setSensitiveCustFilterCnt(result.getCurrentCustNum() - currentBitmap.getCount());
        result.setCurrentCustNum(currentBitmap.getCount());
        log.info("敏感客户群过滤完成,result={}", result);
    }

    /**
     * 已办理过滤
     *
     * @param result
     * @param currentBitmap
     */
    private void orderedFilter(McdPreviewLogResult result, IBitMap currentBitmap) {
        List<String> phoneList = new ArrayList<>(REDIS_GET_BATCH * 2);
        int idx = 0;
        Iterator<Long> iterator = currentBitmap.iterator();
        Long phoneNo;
        while (iterator.hasNext()) {
            phoneNo = iterator.next();
            phoneList.add(String.valueOf(phoneNo));
            if (phoneList.size() == REDIS_GET_BATCH) {
                orderedBatchFilter(previewInfo.getPlanId(), phoneList, currentBitmap);
                phoneList.clear();
                idx += REDIS_GET_BATCH;
                log.info("已办理过滤已经进行{}条", idx);
            }
        }
        orderedBatchFilter(previewInfo.getPlanId(), phoneList, currentBitmap);
        idx += phoneList.size();
        log.info("已办理过滤完成,共{}条", idx);
        phoneList.clear();
        result.setOrderedFilterCnt(result.getCurrentCustNum() - currentBitmap.getCount());
        result.setCurrentCustNum(currentBitmap.getCount());
        log.info("已办理过滤结果,result={}", result);
    }

    /**
     * 已办理互斥过滤
     *
     * @param result
     * @param currentBitmap
     */
    private void orderedExcludeFilter(McdPreviewLogResult result, IBitMap currentBitmap) {
        List<String> phoneList = new ArrayList<>(REDIS_GET_BATCH * 2);
        int idx = 0;
        Iterator<Long> iterator = currentBitmap.iterator();
        Long phoneNo;
        while (iterator.hasNext()) {
            phoneNo = iterator.next();
            phoneList.add(String.valueOf(phoneNo));
            if (phoneList.size() == REDIS_GET_BATCH) {
                excludeBatchFilter(previewInfo.getPlanId(), phoneList, currentBitmap);
                phoneList.clear();
                idx += REDIS_GET_BATCH;
                log.info("已办理互斥过滤已经进行{}条", idx);
            }
        }
        excludeBatchFilter(previewInfo.getPlanId(), phoneList, currentBitmap);
        idx += phoneList.size();
        log.info("已办理互斥过滤完成,共{}条", idx);
        phoneList.clear();
        result.setExcludeFilterCnt(result.getCurrentCustNum() - currentBitmap.getCount());
        result.setCurrentCustNum(currentBitmap.getCount());
        log.info("已办理互斥过滤结果,result={}", result);
    }

    /**
     * 已推荐过滤
     *
     * @param result
     * @param currentBitmap
     */
    private void recommendFilter(McdPreviewLogResult result, IBitMap currentBitmap) {

        List<String> phoneList = new ArrayList<>(REDIS_GET_BATCH * 2);
        int idx = 0;
        Iterator<Long> iterator = currentBitmap.iterator();
        Long phoneNo;
        RecommendFilterBatchRequest request = new RecommendFilterBatchRequest();
        // 预演不记录日志
        request.setRecordLog(false);
        request.setChannelId(previewInfo.getChannelId());
        request.setAdivId(previewInfo.getChannelAdivId());
        request.setPlanId(previewInfo.getPlanId());
        String campsegIdsKey = StrUtil.format("MCD:IDS:{}:{}", previewInfo.getChannelId(), previewInfo.getChannelAdivId());
        List<String> list = RedisUtils.getList(campsegIdsKey);
        request.setCampsegIds(list);

        while (iterator.hasNext()) {
            phoneNo = iterator.next();
            phoneList.add(String.valueOf(phoneNo));
            if (phoneList.size() == REDIS_GET_BATCH) {
                request.setPhoneList(phoneList);
                recommendBatchFilter(request, currentBitmap);
                phoneList.clear();
                idx += REDIS_GET_BATCH;
                log.info("已经推荐过滤已经进行{}条", idx);
            }
        }
        request.setPhoneList(phoneList);
        recommendBatchFilter(request, currentBitmap);
        idx += phoneList.size();
        log.info("已推荐过滤完成,共{}条", idx);
        phoneList.clear();
        result.setRecommendFilterCnt(result.getCurrentCustNum() - currentBitmap.getCount());
        result.setCurrentCustNum(currentBitmap.getCount());
        log.info("已推荐过滤结果,result={}", result);

    }


    /**
     * 频次过滤
     *
     * @param result
     * @param currentBitmap
     */
    private void fqcFilter(McdPreviewLogResult result, IBitMap currentBitmap) {
        List<String> phoneList = new ArrayList<>(REDIS_GET_BATCH * 2);
        int idx = 0;
        Iterator<Long> iterator = currentBitmap.iterator();
        Long phoneNo;
        while (iterator.hasNext()) {
            phoneNo = iterator.next();
            phoneList.add(String.valueOf(phoneNo));
            if (phoneList.size() == REDIS_GET_BATCH) {
                fqcBatchFilter(previewInfo.getChannelId(), previewInfo.getCampsegId(), phoneList, currentBitmap);
                phoneList.clear();
                idx += REDIS_GET_BATCH;
                log.info("频次过滤已经进行{}条", idx);
            }
        }
        idx += phoneList.size();
        log.info("频次过滤全部完成,共{}条", idx);
        fqcBatchFilter(previewInfo.getChannelId(), previewInfo.getCampsegId(), phoneList, currentBitmap);
        phoneList.clear();
        result.setFqcFilterCnt(result.getCurrentCustNum() - currentBitmap.getCount());
        result.setCurrentCustNum(currentBitmap.getCount());
        log.info("频次过滤结果,result={}", result);
    }


    /**
     * 生成预演日志数据
     *
     * @param previewLog
     * @param result
     */
    private void setCampPreviewLogResult(McdCampPreviewLog previewLog, McdPreviewLogResult result) {

        previewLog.setCampsegId(previewInfo.getCampsegId());
        previewLog.setCampsegRootId(previewInfo.getCampsegRootId());
        previewLog.setChannelId(previewInfo.getChannelId());
        previewLog.setCustgroupId(previewInfo.getCustomGroupId());
        previewLog.setOriginCustNum(result.getCustgroupTotal());
        previewLog.setTargetCustomNum(result.getCustgroupTotal());
        // 预演后剩下的客户群数量
        previewLog.setPreviewCustomNum(result.getCurrentCustNum());
        // 已订购剔除
        previewLog.setFilterCount1(result.getOrderedFilterCnt());
        // 已订购互斥剔除用户数
        previewLog.setFilterCount2(result.getExcludeFilterCnt());
        // 已推荐剔除用户数
        previewLog.setFilterCount3(result.getRecommendFilterCnt());
        // 频次过滤用户数
        previewLog.setFilterCount4(result.getFqcFilterCnt());
        // 渠道偏好过滤用户数
        previewLog.setFilterCount5(result.getChnPreFilterCnt());
        // 其它特殊名单过滤
        previewLog.setOtherFilterCount(result.getBwlFilterCnt() + result.getSensitiveCustFilterCnt());
    }

    /**
     * 已订购批量执行
     *
     * @param planId
     * @param phoneList
     * @param currentBitmap
     */
    private void orderedBatchFilter(String planId, List<String> phoneList, IBitMap currentBitmap) {
        OrderedFilterRequestJx request = new OrderedFilterRequestJx();
        request.setPhones(phoneList);
        request.setPlans(Arrays.asList(planId));
        request.setOrderedType(OrderedType.ORDERED);
        request.setRecordLog(false);

        FilterResult result = FccUtilJx.filterOrderedAndSinglePlan(request);
        if (result.getFailure().size() <= 0) {
            return;
        }
        // 过滤失败的从bitMap中删除
        for (String p : result.getFailure()) {
            currentBitmap.remove(Long.valueOf(p));
        }
    }

    /**
     * 已订购互斥批量执行
     *
     * @param planId
     * @param phoneList
     * @param currentBitmap
     */
    private void excludeBatchFilter(String planId, List<String> phoneList, IBitMap currentBitmap) {
        OrderedFilterRequestJx request = new OrderedFilterRequestJx();
        request.setPhones(phoneList);
        request.setPlans(Arrays.asList(planId));
        request.setOrderedType(OrderedType.PLAN_RELATION);
        request.setRecordLog(false);
        FilterResult result = FccUtilJx.filterOrderedAndSinglePlan(request);
        if (result.getFailure().size() <= 0) {
            return;
        }
        // 过滤失败的从bitMap中删除
        for (String p : result.getFailure()) {
            currentBitmap.remove(Long.valueOf(p));
        }
    }

    /**
     * 频次过滤批量执行
     *
     * @param channelId
     * @param campsegId
     * @param phoneList
     * @param currentBitmap
     */
    private void fqcBatchFilter(String channelId, String campsegId, List<String> phoneList, IBitMap currentBitmap) {
        FrequencyRequest request = new FrequencyRequest();
        request.setPhones(phoneList);
        request.setChannelId(channelId);
        request.setCampsegId(campsegId);
        // 只校验频次,不增加
        request.setCount(0);
        FilterResult result = FccUtil.filterFqc(request);
        if (result.getFailure().size() <= 0) {
            return;
        }
        // 过滤失败的从bitMap中删除
        for (String p : result.getFailure()) {
            currentBitmap.remove(Long.valueOf(p));
        }
    }

    /**
     * 批量已推荐过滤
     *
     * @param request
     * @param currentBitmap
     */
    private void recommendBatchFilter(RecommendFilterBatchRequest request, IBitMap currentBitmap) {

        try {
            RecommendFilterBatchResult batchResult = FccUtilJx.recommendFilterBatch(request);
            if (null == batchResult || CollectionUtil.isEmpty(batchResult.getFailList())) {
                return;
            }
            for (String phone : batchResult.getFailList()) {
                currentBitmap.remove(Long.valueOf(phone));
            }
        } catch (Exception e) {
            log.error("批量已推荐过滤异常", e);
        }
    }
}
