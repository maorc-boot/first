package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam;

import com.asiainfo.biapp.pec.plan.jx.api.dto.NodesApproverJx;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam
 * @className: CommitProcessParam
 * @author: chenlin
 * @description: 提交自定义标签审批流程的请求参数模型
 * @date: 2023/7/5 15:45
 * @version: 1.0
 */
@Data
@ApiModel("提交自定义标签审批流程的请求参数模型")
public class SelfLabelCommitProcessParam {

    @ApiModelProperty(value = "流程模板ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long processId;

/*    @ApiModelProperty(value = "流程实例ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long instanceId;*/

    @ApiModelProperty(value = "提交审批标签的labelId",example = "1")
    @Min(value = 1,message = "无效的labelId值！")
    @NotNull(message = "labelId值不可为空！")
    private Integer labelId;

    @ApiModelProperty(value = "当前节点ID")
    private String nodeId;

/*    @ApiModelProperty(value = "当前的审批人")
    private User user;*/

    @ApiModelProperty(value = "流程版本号")
    private Integer berv;

/*    @ApiModelProperty(value = "审批状态 1:通过 2:驳回")
    private Integer submitStatus;*/

/*    @ApiModelProperty(value = "审批意见")
    private String dealOpinion;*/

/*    @ApiModelProperty(value = "触发条件")
    private JSONObject triggerParm;*/

    @ApiModelProperty(value = "下一节点审批")
    List<NodesApproverJx> nextNodesApprover;

/*    @ApiModelProperty(value = "审批类型")
    private String approvalType;*/
}
