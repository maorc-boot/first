package com.asiainfo.biapp.pec.approve.jx.dto;

import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.approve.model.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubmitProcessJxDTO {

    @ApiModelProperty(value = "流程模板ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long processId;

    @ApiModelProperty(value = "流程实例ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long instanceId;

    @ApiModelProperty(value = "业务流程ID")
    private String businessId;

    @ApiModelProperty(value = "子业务流程ID 多个逗号隔开")
    private String childBusinessId;

    @ApiModelProperty(value = "当前节点ID")
    private String nodeId;

    @ApiModelProperty(value = "当前的审批人")
    private User user;

    @ApiModelProperty(value = "流程版本号")
    private Integer berv;

    @ApiModelProperty(value = "审批状态 1:通过 0:驳回")
    private Integer submitStatus;

    @ApiModelProperty(value = "审批意见")
    private String dealOpinion;

    @ApiModelProperty(value = "触发条件")
    private JSONObject triggerParm;

    @ApiModelProperty(value = "下一节点审批")
    List<NodesApproverJx> nextNodesApprover;

    @ApiModelProperty(value = "审批类型")
    private String approvalType = "IMCD";

    @ApiModelProperty(value = "渠道id")
    private String channelId;

    @ApiModelProperty(value = "批量审批: 实例ID数组")
    private String[] instanceIds;

    @ApiModelProperty(value = "批量审批: 业务(活动)ID数组")
    private String[] businessIds;
}
