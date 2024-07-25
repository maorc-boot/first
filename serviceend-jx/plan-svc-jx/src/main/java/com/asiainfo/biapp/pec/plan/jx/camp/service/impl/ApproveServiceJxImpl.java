package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.api.PecApproveFeignClient;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IApproveServiceJx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : ranpf
 * @date : 2022-10-9 15:35:37
 * 每个省份可以有单独的审批
 */
@Service
@Slf4j
public class ApproveServiceJxImpl implements IApproveServiceJx {

    @Autowired
    private PecApproveFeignClient approveFeignClient;


    @Override
    public ActionResponse<Object> submit(Object param) {
        log.info("提交approve-svc-jx 审批，参数{}", JSONUtil.toJsonStr(param));
        return approveFeignClient.submit((SubmitProcessQuery)param);
    }


}
