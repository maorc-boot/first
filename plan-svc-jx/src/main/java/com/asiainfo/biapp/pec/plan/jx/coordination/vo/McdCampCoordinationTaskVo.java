package com.asiainfo.biapp.pec.plan.jx.coordination.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 计算结果主任务列表返回对象
 *
 * @author: lvchaochao
 * @date: 2023/8/3
 */
@Data
@ApiModel("计算结果主任务列表返回对象")
public class McdCampCoordinationTaskVo {

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "任务父ID")
    private String taskPId;

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

    @ApiModelProperty(value = "任务推送文件名称前缀")
    private String pushFileName;

    @ApiModelProperty(value = "任务导出样例文件名称前缀")
    private String exportFileName;

    @ApiModelProperty(value = "审批流ID")
    private String approveFlowId;

    @ApiModelProperty(value = "是否有子任务信息 true--有  false--没有")
    private boolean hasChildTasks;

    @ApiModelProperty(value = "计算时间")
    private String calcTime;
}
