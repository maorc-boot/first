package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 江西EMIS系统代办任务
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("mcd_emis_task")
@ApiModel(value = "McdEmisTask对象", description = "")
public class McdEmisTask extends Model<McdEmisTask> {

    @TableId(value = "ID",type = IdType.AUTO)
    private Integer id;
    /**
     * 流程实例ID
     */
    @TableField("INSTANCE_ID")
    private String instanceId;
    /**
     * 流程实例名称
     */
    @TableField("INSTANCE_NAME")
    private String instanceName;
    /**
     * 流程节点ID
     */
    @TableField("NODE_ID")
    private String nodeId;
    /**
     * 流程节点名称
     */
    @TableField("NODE_NAME")
    private String nodeName;
    /**
     * 下一环节流程节点，多个节点ID间以逗号分隔
     */
    @TableField("NEXT_NODES")
    private String nextNodes;

    /**
     * 第几次审批或流程版本号
     */
    @TableField("APPROVAL_TIMES")
    private int approvalTimes;

    /**
     * 审批人ID
     */
    @TableField("APPROVAL_USER")
    private String approvalUser;
    /**
     * 策划人ID
     */
    @TableField("SUBMIT_USER")
    private String submitUser;
    /**
     * 0：审批不通过 1：审批通过 2：待审批
     */
    @TableField("RESULT")
    private int result;//
    /**
     * 审批意见
     */
    @TableField("ADVICE")
    private String advice;

    /**
     * 所有父节点ID，暂时用不上
     */
    @TableField("PREV_NODES")
    private String prevNodes;

    /**
     * 策略ID
     */
    @TableField("CAMPSEG_ID")
    private String campsegId;

    /**
     * 任务类型，0：审批，1阅知
     */
    @TableField("TASK_TYPE")
    private int taskType;

}
