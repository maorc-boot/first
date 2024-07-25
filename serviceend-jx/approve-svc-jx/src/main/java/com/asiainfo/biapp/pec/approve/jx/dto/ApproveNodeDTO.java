package com.asiainfo.biapp.pec.approve.jx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/9/21
 */
@Data
@ApiModel
public class ApproveNodeDTO {
    @ApiModelProperty(value = "审批节点ID")
    private String nodeId;
    @ApiModelProperty(value = "审批节点名称")
    private String nodeName;
    @ApiModelProperty(value = "审批流程ID")
    private String processId;
}
