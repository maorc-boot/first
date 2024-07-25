package com.asiainfo.biapp.pec.plan.jx.api;

import com.asiainfo.biapp.client.pec.approve.model.CmpApprovalProcess;
import com.asiainfo.biapp.client.pec.approve.model.RecordPageDTO;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdCampApproveJxNewQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CmpApproveProcessRecordJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCampCmpApproveProcessRecordJx;
import com.asiainfo.biapp.pec.plan.jx.enterprise.model.McdCampDef;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * plan服务openfeign接口
 * @author imcd
 */
@Component
@Slf4j
public class PecApproveFeignFallbackClientJx implements PecApproveFeignClient {

    @Override
    public ActionResponse<Object> submit(SubmitProcessQuery req) {
        log.error("feign远程调用系统审批服务异常后的降级方法");
        return ActionResponse.getFaildResp("调用错误！");
    }

    @Override
    public ActionResponse<Page<CmpApproveProcessRecordJx>> getUserRecordNew(RecordPageDTO record) {
        log.error("feign远程调用系统审批服务异常后的降级方法");
        return ActionResponse.getFaildResp("调用错误！");
    }

    @Override
    public ActionResponse<Page<McdCampCmpApproveProcessRecordJx>> queryRecordListNew(McdCampApproveJxNewQuery record) {
        log.error("feign远程调用系统审批服务异常后的降级方法");
        return ActionResponse.getFaildResp("调用错误！");
    }

    @Override
    public Object mcdExternalCampApproveFeedback(McdCampDef mcdCampDef) {
        log.error("feign远程调用系统审批服务异常后的降级方法(政企-审批回调接口)");
        return ActionResponse.getFaildResp("调用错误！");
    }

    @Override
    public ActionResponse<CmpApprovalProcess> getApproveConfig(String systemId) {
        log.error("feign远程调用系统审批服务异常后的降级方法");
        return ActionResponse.getFaildResp("调用错误！");
    }

    @Override
    public ActionResponse<SubmitProcessJxDTO> getNodeApprover(SubmitProcessJxDTO submitProcessDTO) {
        log.error("feign远程调用系统审批服务异常后的降级方法");
        return ActionResponse.getFaildResp("调用错误！");
    }
}
