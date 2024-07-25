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
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : ranpf
 * @date : 2022-10-9 09:55:26
 * 审批组件
 */
@FeignClient("approve-svc/approve-svc")
//@FeignClient("approve-svc-tmp/approve-svc")
public interface PecApproveFeignClient {

    //2023-07-05 17:54:38 针对自定义标签和自定义预警设置的ENUM_VALUE值
    String CUSTOM_ALARM = "APP_ALARM";

    String CUSTOM_LABEL = "CUSTOM_LABEL";

    /**
     * 提交审批
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/jx/cmpApproveProcessInstance/commitProcess")
    ActionResponse<Object> submit(@RequestBody SubmitProcessQuery req);

    /**
     * 当前用户待审记录
     *
     * @return
     */
    @PostMapping("/jx/cmpApproveProcessInstance/queryRecordList")
    ActionResponse<Page<CmpApproveProcessRecordJx>> getUserRecordNew(@RequestBody RecordPageDTO record);

    /**
     * 当前用户待审记录
     *
     * @return
     */
    @PostMapping("/jx/cmpApproveProcessInstance/queryRecordListNew")
    ActionResponse<Page<McdCampCmpApproveProcessRecordJx>> queryRecordListNew(@RequestBody McdCampApproveJxNewQuery record);

    // campsegApproveSync
    @PostMapping("/jx/enterprise/approve/campsegApproveSync")
    Object mcdExternalCampApproveFeedback(@RequestBody McdCampDef mcdCampDef);

    // 2023-07-05 12:36:16
    @PostMapping("/jx/cmpApprovalProcessConf/getApproveConfig")
    ActionResponse<CmpApprovalProcess> getApproveConfig(@RequestParam("systemId") String systemId);

    @PostMapping( "/jx/cmpApproveProcessInstance/getNodeApprover")
    ActionResponse<SubmitProcessJxDTO> getNodeApprover(@RequestBody SubmitProcessJxDTO submitProcessDTO);

}
