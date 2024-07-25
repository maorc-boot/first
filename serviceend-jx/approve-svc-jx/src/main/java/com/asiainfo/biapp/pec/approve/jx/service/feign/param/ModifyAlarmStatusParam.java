package com.asiainfo.biapp.pec.approve.jx.service.feign.param;

import com.asiainfo.biapp.pec.core.exception.BaseException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam
 * @className: SaveOrUpdateAlarmParam
 * @author: chenlin
 * @description: 新增自定义预警审批状态修改参数，与com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam一致
 * @date: 2023/6/28 17:21
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("修改自定义预警审批状态请求参数")
public class ModifyAlarmStatusParam {

    /**
     * 预警ID
     */
    @ApiModelProperty(value = "预警ID")
    @NotNull(message = "预警ID值不可为空！")
    @Min(value = 1, message = "无效的预警ID值！")
    private Integer alarmId;

    /**
     * 审批状态：0 草稿；1 审批完成；2 审批中；3 终止;4 审批驳回
     */
    //2023-07-09 11:24:36 因修改状态的接口只会远程调用，所以状态只有审批完成：1，或者审批驳回4
    @ApiModelProperty(value = "审批状态：0 草稿；1 审批完成；2 审批中；3 终止;4 审批驳回")
    @NotNull(message = "审批状态不可为空！")
    private Integer approveStatus;


    // 2023-07-09 11:21:03  考虑是否传入登录用户，远程调用可能无法获取到登录用户

    /**
     * 审批流程id
     */
    @ApiModelProperty("审批流程id")
    @NotBlank(message = "审批流程id不可为空！")
    private String approveFlowId;

    public void setApproveStatus(Integer approveStatus) {
        if (approveStatus != 1 && approveStatus != 4)
            throw new BaseException("无效的审批状态值！");
        this.approveStatus = approveStatus;
    }
}
