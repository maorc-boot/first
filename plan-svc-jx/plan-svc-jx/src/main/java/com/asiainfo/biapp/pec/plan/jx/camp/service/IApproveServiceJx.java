package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.core.common.ActionResponse;

/**
 * @author : ranpf
 * @date : 2022-10-9 14:57:01
 * 活动审批, 每个省份继承自己的审批提交
 */
public interface IApproveServiceJx {


    /**
     * 提交审批,返回流程实例id
     * @param param
     * @return
     */
    ActionResponse<Object> submit(Object param);



}
