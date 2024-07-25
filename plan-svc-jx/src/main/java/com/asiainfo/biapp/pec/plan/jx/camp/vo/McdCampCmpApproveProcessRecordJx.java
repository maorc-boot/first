package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(value = "CmpApproveProcessRecordJx对象", description = "江西审批流程记录表")
public class McdCampCmpApproveProcessRecordJx {

    @ApiModelProperty(value = "主键ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "业务流程ID")
    private String businessId;

    @ApiModelProperty(value = "流程实例ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long instanceId;

    @ApiModelProperty(value = "节点ID")
    private String nodeId;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "节点类型 0起始节点 1审批节点  2终止节点 3事件节点")
    private Integer nodeType;

    @ApiModelProperty(value = "业务节点名称")
    private String nodeBusinessName;

    @ApiModelProperty(value = "审批人Ids 可多选")
    private String approver;

    @ApiModelProperty(value = "审批人名称 可多选 逗号隔开")
    private String approverName;

    @ApiModelProperty(value = "处理意见")
    private String dealOpinion;

    @ApiModelProperty(value = "处理状态 0待处理 1通过 2驳回 3无需处理 4待提交")
    private Integer dealStatus;

    @ApiModelProperty(value = "处理时间")
    private Date dealTime;

    @ApiModelProperty(value = "上一节点流程记录ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long preRecordId;

    @ApiModelProperty(value = "事件ID")
    private String eventId;

    @ApiModelProperty(value = "节点创建时间")
    private Date createDate;

    @ApiModelProperty(value = "节点创建人")
    private String createBy;

    @ApiModelProperty(value = "节点修改时间")
    private Date modifyDate;

    @ApiModelProperty(value = "节点修改人")
    private String modifyBy;
    @ApiModelProperty(value = "沟通状态 0 待审批,1审批完成，江西用")
    private Integer communicationState;

    @ApiModelProperty(value = "策略ID")
    private String campsegId;

    @ApiModelProperty(value = "策略名称")
    private String campsegName;

    @ApiModelProperty(value = "策略创建人ID")
    private String createUserId;

    @ApiModelProperty(value = "策略策略创建人姓名")
    private String userName;

    @ApiModelProperty(value = "策略创建时间")
    private Date createTime;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "营销场景")
    private String campScene;
}
