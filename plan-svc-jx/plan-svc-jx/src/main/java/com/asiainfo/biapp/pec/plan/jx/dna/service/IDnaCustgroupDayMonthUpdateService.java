package com.asiainfo.biapp.pec.plan.jx.dna.service;

import cn.hutool.json.JSONObject;

/**
 * description: DNA客群更新调度(日周期/月周期)service
 *
 * @author: lvchaochao
 * @date: 2023/12/18
 */
public interface IDnaCustgroupDayMonthUpdateService {

    /**
     * DNA客群更新调度(日周期/月周期)任务
     *
     * @param request 入参 type: day-日周期 month-月周期
     */
    void dnaCustgroupDayMonthUpdateTask(JSONObject request);

    /**
     * DNA客群更新通知接口--dna侧调用
     *
     * @param request 入参 dataDate--客群数据日期
     */
    void dnaCustgroupUpdateNotice(JSONObject request) throws Exception;
}
