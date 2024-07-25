package com.asiainfo.biapp.pec.plan.jx.coordination.req;

import com.asiainfo.biapp.client.pec.approve.model.ApproveUserQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * Description : 提交审批入参
 * </p>
 *
 * @author : wuhq6
 * @date : Created in 2022/1/26  10:53
 * //@modified By :
 * //@since :
 */

@Data
@ApiModel(value = "提交审批入参")
public class SubmTaskApproveQuery {

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "节点审批人")
    private List<ApproveUserQuery> approver;
}
