package com.asiainfo.biapp.pec.approve.jx.controller;


import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.approve.dto.CmpApprovalProcessSaveDTO;
import com.asiainfo.biapp.pec.approve.jx.service.ApproveService;
import com.asiainfo.biapp.pec.approve.model.CmpApprovalProcess;
import com.asiainfo.biapp.pec.approve.service.ICmpApprovalProcessService;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审批流程模板主表 前端控制器
 * </p>
 *
 * @author wanghao
 * @since 2021-11-02
 */
@RestController
@RequestMapping("/cmpApprovalProcess/jx")
@Api(value = "江西:审批流程模板管理", tags = {"江西:审批流程模板管理"})
@Slf4j
public class CmpApprovalProcessJxController {

    @Autowired
    private ICmpApprovalProcessService cmpApprovalProcessService;

    @Autowired
    private ApproveService approveService;


    @PostMapping(path = "/saveApproveTemplate")
    @ApiOperation(value = "保存审批流程模板", notes = "保存审批流程模板")
    public ActionResponse<Long> saveApproveTemplate(@RequestBody CmpApprovalProcessSaveDTO approvalProcess) {
        log.info("start saveApproveTemplate para:{}", new JSONObject(approvalProcess).toString());
        if (approvalProcess.getProcessId() != null && approveService.isProcessUsed(approvalProcess.getProcessId())) {
            log.warn("审批流程已使用不能修改,processId={}", approvalProcess.getProcessId());
            return ActionResponse.getFaildResp("审批流程已使用不能修改");
        }
        Long processId = cmpApprovalProcessService.saveApproveTemplate(approvalProcess);
        return ActionResponse.getSuccessResp(processId);
    }

    @PostMapping(path = "/removeApproveTemplate")
    @ApiOperation(value = "删除审批流程模板", notes = "删除审批流程模板")
    public ActionResponse<Long> removeApproveTemplate(@RequestBody CmpApprovalProcess approvalProcess) {
        log.info("start removeApproveTemplate para:{}", new JSONObject(approvalProcess).toString());
        if (approveService.isProcessUsed(approvalProcess.getProcessId())) {
            log.warn("审批流程已使用不能删除,processId={}", approvalProcess.getProcessId());
            return ActionResponse.getFaildResp("审批流程已使用不能删除");
        }
        boolean flag = cmpApprovalProcessService.removeById(approvalProcess.getProcessId());
        return ActionResponse.getSuccessResp(flag);
    }

    @PostMapping(path = "/isProcessUsed")
    @ApiOperation(value = "审批流程模板是否已使用", notes = "审批流程模板是否已使用,processId:模板流程ID")
    public ActionResponse isProcessUsed(@RequestParam("processId") Long processId) {
        log.info("审批流程模板是否已使用:{}", processId);
        return ActionResponse.getSuccessResp().setData(approveService.isProcessUsed(processId));

    }


}
