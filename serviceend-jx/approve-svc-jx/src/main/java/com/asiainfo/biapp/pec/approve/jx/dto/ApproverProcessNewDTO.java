package com.asiainfo.biapp.pec.approve.jx.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApproverProcessNewDTO {

    @ApiModelProperty(value = "流程模板ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long processId;

    @ApiModelProperty(value = "流程实例ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long instanceId;

    @ApiModelProperty(value = "业务流程ID")
    private String businessId;

    @ApiModelProperty(value = "当前节点ID")
    private String nodeId;

    @ApiModelProperty(value = "节点名称NODE_NAME")
    private String nodeName;

    @ApiModelProperty(value = "审批岗位唯一标识APPROVER")
    private String approver;

    @ApiModelProperty(value = "审批人地市")
    private String approvalUserCity;
    @ApiModelProperty(value = "审批人部门")
    private String approvalUserDept;

    @ApiModelProperty(value = "当前的审批人姓名APPROVER_NAME")
    private String approverName;

    @ApiModelProperty(value = "审批状态 1:通过 2:驳回")
    private Integer submitStatus;

    @ApiModelProperty(value = "审批意见")
    private String dealOpinion;

    @ApiModelProperty(value = "创建时间")
    private String createDate;

    @ApiModelProperty(value = "处理时间")
    private String dealTime;

    @ApiModelProperty(value = "下一节点审批")
    private String nextNodesApprover;

    @ApiModelProperty(value = "节点类型 0起始节点 1审批节点  2终止节点 3事件节点")
    private Integer nodeType;

}
