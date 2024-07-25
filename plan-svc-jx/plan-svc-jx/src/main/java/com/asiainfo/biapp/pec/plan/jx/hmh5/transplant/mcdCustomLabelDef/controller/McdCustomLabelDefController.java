package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller;


import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.McdCustomLabelDefService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo.SelfDefinedLabelResultConfInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo.SelfDefinedLabelResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标签定义表 前端控制器
 * </p>
 *
 * @author chenlin
 * @since 2023-06-26
 */
@RestController
@RequestMapping("/mcdCustomLabelDef")
@Validated
@Api(tags = "客户通-自定义标签配置")
@Slf4j
@DataSource("khtmanageusedb")
public class McdCustomLabelDefController {

    @Autowired
    private McdCustomLabelDefService labelDefService;

    @ApiOperation("配置页面根据标签名称模糊查询标签（点击添加标签选项时）")
    @GetMapping
    // @DataSource("khtmanageusedb")
    public ActionResponse<Page<SelfDefinedLabelResultConfInfo>> selectSelfDefinedLabel(
            @Validated SelectSelfDefinedLabelConfParam selfDefinedLabelConfParam
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在查询客户通自定义标签（点击添加标签选项时），查询条件为：{}！", user.getUserName(), JSONUtil.toJsonStr(selfDefinedLabelConfParam));
        if (user.getCityId() == null) user.setCityId("999"); //TODO 虚拟cityid
        Page<SelfDefinedLabelResultInfo> selfDefinedLabelConfs = labelDefService.selectSelfDefinedLabel(user.getCityId(), selfDefinedLabelConfParam);
        return ActionResponse.getSuccessResp(selfDefinedLabelConfs);
    }

    @ApiOperation("根据labelId修改客群规则（查询页操作选项，标签描述）")
    @PutMapping
    // @DataSource("khtmanageusedb")
    public ActionResponse modifySelfDefinedLabel(
            @RequestBody @Validated @ApiParam("修改客群规则（标签描述）参数列表")
            ModifySelfDefinedLabelParam selfDefinedLabelParam
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在修改客户通自定义标签（查询页操作选项，标签描述），修改参数为：{}！", user.getUserName(), JSONUtil.toJsonStr(selfDefinedLabelParam));
        labelDefService.modifySelfDefinedLabel(user.getUserId(), selfDefinedLabelParam);
        return ActionResponse.getSuccessResp("标签客群规则修改成功！");
    }

    @ApiOperation(value = "根据条件查询客户通自定义标签配置（查询页查询选项）", notes = "自定义标签列表查询")
    @GetMapping("/selectSelfDefinedLabelConfDetail")
    // @DataSource("khtmanageusedb")
    public ActionResponse<Page<SelfDefinedLabelResultConfInfo>> selectSelfDefinedLabelConfDetail(
            @Validated SelectSelfDefinedLabelParam selfDefinedLabelParam
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在查询客户通自定义标签配置（查询页查询选项），查询条件为：{}！", user.getUserName(), selfDefinedLabelParam);
        if (user.getCityId() == null) user.setCityId("999"); //TODO 虚拟cityid
        Page<SelfDefinedLabelResultConfInfo> selfDefinedLabels = labelDefService.selectSelfDefinedLabelConfDetail(user.getCityId(), selfDefinedLabelParam);
        return ActionResponse.getSuccessResp(selfDefinedLabels);
    }

    @ApiOperation(value = "查询客户通自定义标签配置（配置选项渲染页面查询）", notes = "配置选项页面渲染查询")
    @GetMapping("/selectSelfDefinedLabelConf")
    // @DataSource("khtmanageusedb")
    public ActionResponse<Map<String, List<SelfDefinedLabelResultInfo>>> selectSelfDefinedLabel() {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在查询客户通自定义标签配置（配置选项渲染页面查询）！", user.getUserName());
        if (user.getCityId() == null) user.setCityId("999"); //TODO 虚拟cityid
        Map<String, List<SelfDefinedLabelResultInfo>> selfDefinedLabels = labelDefService.selectSelfDefinedLabelConf(user.getCityId());
        return ActionResponse.getSuccessResp(selfDefinedLabels);
    }

    @ApiOperation("修改客户通自定义标签配置（配置页点击确定/保存按钮时）")
    @PutMapping("/modifySelfDefinedLabelConf")
    // @DataSource("khtmanageusedb")
    public ActionResponse modifySelfDefinedLabelConf(
            @RequestBody @Validated @ApiParam("修改客户通自定义标签配置参数--{web页面可能需要QS序列化数组}")
            ModifySelfDefinedLabelConfParam selfDefinedLabelConfParam
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在修改客户通自定义标签配置（配置页点击确定/保存按钮时），修改参数为：{}！", user.getUserName(), JSONUtil.toJsonStr(selfDefinedLabelConfParam));
        if (user.getCityId() == null) user.setCityId("999"); //TODO 虚拟cityid
        labelDefService.modifySelfDefinedLabelConf(user.getCityId(), selfDefinedLabelConfParam);
        return ActionResponse.getSuccessResp("客户通自定义标签配置参数修改成功！");
    }


    /*
     * @param labelStatusParam:
     * @return ActionResponse<String>:
     * @author chenlin
     * @description feign调用的接口，用于修改标签审批状态
     * @date 2023/7/10 11:56
     */
    @ApiIgnore
    @PutMapping("/modifyLabelStatus")
    public ActionResponse<String> modifyLabelStatus(@RequestBody @Validated ModifySelfLabelStatusParam labelStatusParam) {
        UserSimpleInfo user = UserUtilJx.getUser();
        Integer approveStatus = labelStatusParam.getApproveStatus();
        log.info("用户：{}正在审核自定义标签，标签labelId值为：{}，审核结果为：{}！", user.getUserName(), labelStatusParam.getLabelId(),
                approveStatus == 52 ? "通过" : "未通过");
        if (user.getUserId() == null) {
            log.error("没有获取到登录用户，正在创建虚拟用户！--------------------------------------------------------------");
            user.setUserId("admin01");
            user.setCityId("999");
        }
        labelDefService.modifyLabelStatus(labelStatusParam);
        return ActionResponse.getSuccessResp("自定义标签状态已修改！");
    }

    /*
     * @param labelId:
     * @return ActionResponse<String>:
     * @author chenlin
     * @description feign调用的接口，用于查询审批流程id
     * @date 2023/7/10 11:56
     */
    @ApiIgnore
    @GetMapping("/selectLabelApproveFlowId")
    public ActionResponse<String> selectLabelApproveFlowId(
            @NotNull(message = "标签ID值不可为空！") @Min(value = 1, message = "无效的标签ID值！") Integer labelId
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在查询标签审批流程ID，labelId值为：{}！", user.getUserName(), labelId);
        if (user.getUserId() == null) {
            log.error("没有获取到登录用户，正在创建虚拟用户！--------------------------------------------------------------");
            user.setUserId("admin01");
            user.setCityId("999");
        }
        String approveFlowId = labelDefService.selectLabelApproveFlowId(labelId);
        ActionResponse successResp = ActionResponse.getSuccessResp();
        successResp.setData(approveFlowId);
        return successResp;
    }


    @ApiOperation("获取流程实例节点下级审批人，点击提交审批申请时")
    @GetMapping("/getNodeApprover")
    public ActionResponse<SubmitProcessJxDTO> getNodeApprover() {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在获取获取流程实例节点下级审批人！", user.getUserName());
        return labelDefService.getNodeApprover();
    }

    @ApiOperation("提交审批申请")
    @PostMapping("/commitProcess")
    public ActionResponse<String> commitProcess(
            @RequestBody @Validated @ApiParam("自定义标签提交审批申请参数") LabelCommitProcessParam processParam
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在提交自定义标签审批申请，labelId：{}！", user.getUserName(), processParam.getCustomLabelId());
        if (user.getUserId() == null) user.setUserId("admin01");    //TODO 2023-07-11 18:19:46 创建临时用户
        labelDefService.commitProcess(processParam, user);
        return ActionResponse.getSuccessResp("提交审批申请成功！");
    }

    @ApiOperation("根据labelId值删除自定义预警")
    @DeleteMapping
    @ApiImplicitParam(name = "labelId", value = "要删除的预警labelId值", example = "1")
    // @DataSource("khtmanageusedb")
    public ActionResponse<String> deleteLabel(
            @RequestParam String labelId
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在删除客户通自定义标签，删除的标签labelId值为：{}！", user.getUserName(), labelId);
        if (user.getUserId() == null) {
            user.setUserId("admin01");
            user.setCityId("999");
        }
        labelDefService.deleteLabel(user, labelId);
        return ActionResponse.getSuccessResp("删除自定义标签成功！");
    }


}

