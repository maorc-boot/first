package com.asiainfo.biapp.pec.approve.jx.service.feign.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description: 修改客户通关怀短信模板审批状态请求参数对象
 *
 * @author: lvchaochao
 * @date: 2024/2/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("修改客户通关怀短信模板审批状态请求参数")
public class ModifyCareSmsTemplateStatusParam {

    @ApiModelProperty(value = "模板编码")
    @NotNull(message = "模板编码值不可为空！")
    private String templateCode;

    @ApiModelProperty(value = "审批状态：20-草稿 40-审批中 41-审批退回 42-审批终止 43-审批通过")
    @NotNull(message = "审批状态不可为空！")
    private Integer approvalStatus;

    @ApiModelProperty("审批流程id")
    @NotBlank(message = "审批流程id不可为空！")
    private String approveFlowId;

}
