package com.asiainfo.biapp.pec.plan.jx.api.dto;

import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.client.pec.approve.model.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.api
 * @className: SubmitProcessJxDTO
 * @author: chenlin
 * @description: 提交审批需要的实体类，同com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO
 * @date: 2023/7/5 14:18
 * @version: 1.0
 */
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

    @ApiModelProperty(value = "当前节点ID")
    private String nodeId;

    @ApiModelProperty(value = "当前的审批人")
    private User user;

    @ApiModelProperty(value = "流程版本号")
    private Integer berv;

    @ApiModelProperty(value = "审批状态 1:通过 2:驳回")
    private Integer submitStatus;

    @ApiModelProperty(value = "审批意见")
    private String dealOpinion;

    @ApiModelProperty(value = "触发条件")
    private JSONObject triggerParm;

    @ApiModelProperty(value = "下一节点审批")
    List<NodesApproverJx> nextNodesApprover;

    @ApiModelProperty(value = "审批类型")
    private String approvalType;

}
