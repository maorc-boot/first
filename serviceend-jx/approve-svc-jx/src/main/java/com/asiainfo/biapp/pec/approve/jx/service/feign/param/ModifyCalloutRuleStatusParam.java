package com.asiainfo.biapp.pec.approve.jx.service.feign.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description: 修改客户通外呼规则审批状态请求参数对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("修改客户通外呼规则审批状态请求参数")
public class ModifyCalloutRuleStatusParam {

    @ApiModelProperty(value = "场景编码")
    @NotNull(message = "场景编码值不可为空！")
    private String scenarioId;

    @ApiModelProperty(value = "审批状态 1：审批中 3：草稿 4：已驳回 5:审批完成")
    @NotNull(message = "审批状态不可为空！")
    private String approvalStatus;

    @ApiModelProperty("审批流程id")
    @NotBlank(message = "审批流程id不可为空！")
    private String approveFlowId;

}
