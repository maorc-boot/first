package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import com.asiainfo.biapp.client.pec.approve.model.CmpApproveProcessRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * description: 客户通黑名单审批列表实体
 *
 * @author lvcc
 * @date 2024/05/29
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "客户通黑名单审批列表实体",description = "客户通黑名单审批列表实体")
@Data
public class BlacklistApprRecord extends CmpApproveProcessRecord {

    @ApiModelProperty("任务编码")
    private String taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务创建人ID")
    private String createUserId;

    @ApiModelProperty("任务创建人姓名")
    private String userName;

    @ApiModelProperty("任务创建时间")
    private Date createTime;

}
