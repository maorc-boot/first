package com.asiainfo.biapp.pec.plan.jx.hisdata.service;

import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2023/5/20
 */
public interface HisDataService {

    /**
     * 迁移活动相关信息
     *
     * @param stat
     * @param channelId
     * @param campsegId
     * @return
     */
    boolean handleCamp(String stat, String channelId, String campsegId);

    /**
     * 迁移审批记录
     *
     * @param approveFlowId
     * @return
     */
    boolean handleApproveRecord(String approveFlowId);

    /**
     * 删除活动
     *
     * @param campsegRootId
     * @return
     */
    boolean deleteCamp(String campsegRootId,String channelId);

    /**
     * 查询待同步的活动（7.0的活动数据同步到4.0）
     * @param stat
     * @return
     */
    List<Map<String, Object>> queryCampInfoList(int stat);

    Map<String,Object> queryCampTaskInfo(String campsegId,String channelId);

    Map<String,Object> queryMcdCampTaskDate(String taskId);

    Map<String,Object> queryMcdCampChannelListInfo(String campsegId,String channelId);

    List<Map<String, Object>> queryCampFusionPlanList(String campsegId);

    List<Map<String, Object>> queryCampSeriesPlanList(String campsegId);

    List<Map<String, Object>> queryCampExclusivePlanList(String campsegId);
}
