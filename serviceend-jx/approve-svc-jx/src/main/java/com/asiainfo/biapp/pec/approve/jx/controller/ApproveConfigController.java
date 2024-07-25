package com.asiainfo.biapp.pec.approve.jx.controller;

import com.asiainfo.biapp.pec.approve.common.McdException;
import com.asiainfo.biapp.pec.approve.jx.dto.ApproveConfigDTO;
import com.asiainfo.biapp.pec.approve.jx.model.ApproveConfig;
import com.asiainfo.biapp.pec.approve.jx.service.ApproveConfigService;
import com.asiainfo.biapp.pec.approve.jx.service.ApproveService;
import com.asiainfo.biapp.pec.approve.jx.vo.McdAppTypeVo;
import com.asiainfo.biapp.pec.approve.model.CmpApprovalProcess;
import com.asiainfo.biapp.pec.approve.service.ICmpApprovalProcessService;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 流程配置表 前端控制器
 * </p>
 *
 * @author feify
 * @since 2022-06-07
 */
@RestController
@RequestMapping("/jx/cmpApprovalProcessConf")
@Api(value = "江西:审批流程配置关系服务", tags = {"江西:审批流程配置关系服务"})
@Slf4j
public class ApproveConfigController {

    @Setter(onMethod_ = {@Autowired})
    private ApproveConfigService approveConfigService;

    @Setter(onMethod_ = {@Autowired})
    private ICmpApprovalProcessService processService;

    @Autowired
    private ApproveService approveService;

    @PostMapping(path = "/queryApproveConfigPageList")
    @ApiOperation(value = "获取审批流程配置关系分页列表", notes = "获取审批流程配置关系分页列表")
    public ActionResponse<IPage<ApproveConfigDTO>> queryApproveConfigPageList(@RequestBody ApproveConfigDTO approveConfigDTO) {
        return ActionResponse.getSuccessResp(approveConfigService.queryApproveConfigPageList(approveConfigDTO));
    }

    @PostMapping(path = "/saveOrUpdateApproveConfig")
    @ApiOperation(value = "新增或修改审批流程配置关系", notes = "新增或修改审批流程配置关系")
    public ActionResponse<ApproveConfig> saveOrUpdateApproveConfig(@RequestBody ApproveConfig approveConfig) {
        return ActionResponse.getSuccessResp(approveConfigService.saveOrUpdateApproveConfig(approveConfig));
    }

    @PostMapping(path = "/getApproveConfig")
    @ApiOperation(value = "根据当前用户获取审批模板", notes = "根据当前用户获取审批模板")
    public ActionResponse<CmpApprovalProcess> getApproveConfig(@RequestParam(value = "systemId", required = false, defaultValue = "IMCD") String systemId) {

        ApproveConfig approveConfig = approveConfigService.getApproveConfig(systemId);
        if (approveConfig == null) {
            McdException.throwMcdException(McdException.McdExceptionEnum.NO_PROCESS_TEMPLATE_FOR_CURRENT_USER);
        }

        return ActionResponse.getSuccessResp(processService.getById(approveConfig.getProcessId()));
    }

    @PostMapping(path = "/copyCmpApprovalProcess")
    @ApiOperation(value = "复制审批流程", notes = "复制审批流程,processId:模板流程ID")
    public CmpApprovalProcess copyCmpApprovalProcess(@RequestParam("processId") Long processId) {
        log.info("开始复制审批流程:{}", processId);
        return approveService.copyCmpApprovalProcess(processId);

    }


    @PostMapping(path = "/queryAppTypeList")
    @ApiOperation(value = "查询审批类型列表", notes = "查询审批类型列表")
    public ActionResponse<List<McdAppTypeVo>> queryAppTypeList() {
        log.info("开始查询审批类型列表");
        return ActionResponse.getSuccessResp(approveConfigService.getAppTypeList());

    }

}
