package com.asiainfo.biapp.pec.approve.jx.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ranpf
 */
@Data
public class NodesApproverH5 {


    @ApiModelProperty(value = "节点ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private String nodeId;
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    @ApiModelProperty(value = "节点审批人")
    private List<H5ApproveUser> approvers;

}
