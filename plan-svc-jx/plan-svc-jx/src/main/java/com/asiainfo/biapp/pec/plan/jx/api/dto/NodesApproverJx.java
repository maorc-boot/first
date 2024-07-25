package com.asiainfo.biapp.pec.plan.jx.api.dto;

import com.asiainfo.biapp.client.pec.approve.model.CmpApproveProcessNode;
import com.asiainfo.biapp.client.pec.approve.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author feify
 */
@Data
public class NodesApproverJx {

    @ApiModelProperty(value = "节点")
    private CmpApproveProcessNode node;

    @ApiModelProperty(value = "节点审批人")
    private List<User> approverUser;

    @ApiModelProperty(value = "流程版本号")
    private Integer processVersionNum;
}
