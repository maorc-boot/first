package com.asiainfo.biapp.pec.approve.jx.service.feign.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改客户通黑名单审批状态请求参数
 *
 * @author lvcc
 * @date 2024/05/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("修改客户通黑名单审批状态请求参数")
public class ModifyBlacklistApprStatusParam {

    @ApiModelProperty(value = "任务编码")
    @NotNull(message = "任务编码值不可为空！")
    private String taskId;

    @ApiModelProperty(value = "审批状态：0-草稿，1-审批中，2-审批驳回，3-导入中，4-导入完成 5-审批完成")
    @NotNull(message = "审批状态不可为空！")
    private Integer approvalStatus;

    @ApiModelProperty("审批流程id")
    @NotBlank(message = "审批流程id不可为空！")
    private String approveFlowId;

    @ApiModelProperty("当前审批人")
    private String approverName;

}
