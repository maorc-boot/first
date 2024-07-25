package com.asiainfo.biapp.pec.plan.jx.coordination.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author : ranpf
 * @date : 2023-6-9 19:54:10
 */
@Data
@ApiModel("策略统筹融合计算任务审批任务")
public class McdCampCoordinationTaskInfo {

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty("执行状态 10待执行,20计算中,25计算失败, 30待审批,40审批中,41审批驳回,54审批通过,90已推送")
    private int execStatus;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty(value = "产品总数")
    private int planTotalNum;

    @ApiModelProperty(value = "客户总数")
    private int customTotalNum;

    @ApiModelProperty(value = "策划人ID")
    private String createUserId;

    @ApiModelProperty(value = "策划人")
    private String createUserName;

    @ApiModelProperty(value = "审批流ID")
    private String approveFlowId;
}
