package com.asiainfo.biapp.pec.approve.jx.dto;

import com.asiainfo.biapp.pec.approve.jx.model.SysUser;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessNode;
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
    private List<SysUser> approverUser;

    @ApiModelProperty(value = "流程版本号")
    private Integer processVersionNum;
}
