package com.asiainfo.biapp.pec.plan.jx.coordination.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author : ranpf
 * @date : 2023-6-9 19:54:10
 */
@Data
@ApiModel("策略统筹融合计算任务表")
@TableName("mcd_camp_coordinate_task")
public class McdCampCoordinationTaskModel {

    @ApiModelProperty(value = "任务ID")
    @TableId("TASK_ID")
    private String taskId;

    @ApiModelProperty(value = "任务父ID")
    @TableField("TASK_PID")
    private String taskPId;

    @ApiModelProperty(value = "任务名称")
    @TableField("TASK_NAME")
    private String taskName;

    @ApiModelProperty("执行状态 10待执行,20计算中,25计算失败, 30待审批,40审批中,41审批驳回,54审批通过,90已推送")
    @TableField("EXEC_STATUS")
    private int execStatus;

    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    private String createTime;

    @ApiModelProperty(value = "产品总数")
    @TableField("PLAN_TOTAL_NUM")
    private int planTotalNum;

    @ApiModelProperty(value = "客户总数")
    @TableField("CUSTOM_TOTAL_NUM")
    private int customTotalNum;

    @ApiModelProperty(value = "原始客户群总数")
    @TableField("ORIGINAL_CUSTOM_NUM")
    private int originalCustomNum;

    @ApiModelProperty(value = "策划人ID")
    @TableField("CREATE_USER_ID")
    private String createUserId;

    @ApiModelProperty(value = "策划人")
    @TableField("CREATE_USER_NAME")
    private String createUserName;

    @ApiModelProperty(value = "任务推送文件名称前缀")
    @TableField("PUSH_FILE_NAME")
    private String pushFileName;

    @ApiModelProperty(value = "任务导出样例文件名称前缀")
    @TableField("EXPORT_FILE_NAME")
    private String exportFileName;

    @ApiModelProperty(value = "审批流ID")
    @TableField("APPROVE_FLOW_ID")
    private String approveFlowId;

    @ApiModelProperty(value = "计算时间")
    @TableField("calc_time")
    private long calcTime;
}
