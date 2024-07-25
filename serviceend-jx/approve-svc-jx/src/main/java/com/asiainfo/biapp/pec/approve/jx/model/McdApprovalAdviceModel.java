package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 审批流程记录表
 * </p>
 *
 * @author wanghao
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_approval_advice")
@ApiModel(value="转至沟通人审批记录表", description="转至沟通人审批记录表")
public class McdApprovalAdviceModel extends Model<McdApprovalAdviceModel> {

    private static final long serialVersionUID = -6940928665407614434L;

    @ApiModelProperty(value = "主键ID")
    @TableId("ID")
    private Long id;

    @ApiModelProperty(value = "流程实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @ApiModelProperty(value = "节点ID")
    @TableField("NODE_ID")
    private String nodeId;

    @ApiModelProperty(value = "审批人Id")
    @TableField("APPROVER")
    private String approver;

    @ApiModelProperty(value = "审批人名称")
    @TableField("APPROVER_NAME")
    private String approverName;

    @ApiModelProperty(value = "处理意见")
    @TableField("DEAL_OPINION")
    private String dealOpinion;

    @ApiModelProperty(value = "处理状态 0待处理 1通过 2驳回 3无需处理 4待提交")
    @TableField("DEAL_STATUS")
    private Integer dealStatus;

    @ApiModelProperty(value = "处理时间")
    @TableField("DEAL_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date dealTime;
    @ApiModelProperty(value = "提交人")
    @TableField("SUBMITTER")
    private String submitter;






    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
