package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description: 江西客户通关怀短信模板详情回显对象
 *
 * @author: lvchaochao
 * @date: 2023/3/14
 */
@ApiModel(value = "江西客户通关怀短信模板详情回显对象",description = "江西客户通关怀短信模板详情回显对象")
@Data
public class McdCareSmsTemplateDetailResp {

    @ApiModelProperty(value = "模板编码")
    private String templateCode;

    @ApiModelProperty(value = "模板内容")
    private String templateContent;

    @ApiModelProperty(value = "模板状态：0-未引用 1-已引用 2-已弃用")
    private String statusStr;

    @ApiModelProperty(value = "模板状态：0-未引用 1-已引用 2-已弃用")
    private Integer status;

    @ApiModelProperty(value = "模板类型")
    private String templateType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    @ApiModelProperty(value = "创建人ID")
    private String createUserId;

    @ApiModelProperty(value = "审批状态：20-草稿 40-审批中 41-审批退回 42-审批终止 43-审批通过")
    private Integer approvalStatus;

    @ApiModelProperty(value = "审批状态：20-草稿 40-审批中 41-审批退回 42-审批终止 43-审批通过")
    private String approvalStatusStr;

    @ApiModelProperty(value = "地市ID,999-省公共模板")
    private String cityId;

    @ApiModelProperty(value = "消息类型：hmh5-客户通，daiwei-代维")
    private String msgSmsPlat;

    @ApiModelProperty(value = "有效截止日期")
    private String validDeadLine;

    @ApiModelProperty(value = "审批流实例")
    private String approveFlowId;

    @ApiModelProperty(value = "创建人名")
    private String userName;

    @ApiModelProperty(value = "审批节点名")
    private String nodeName;

    // @ApiModelProperty(value = "审批日期")
    // private String approveDate;

    // @ApiModelProperty(value = "审批人")
    // private String approvalerName;

    @ApiModelProperty(value = "审批结果")
    private String approveResult;

    @ApiModelProperty(value = "业务流程ID")
    private String businessId;

    @ApiModelProperty(value = "流程实例ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long instanceId;

    @ApiModelProperty(value = "节点ID")
    private String nodeId;

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
}
