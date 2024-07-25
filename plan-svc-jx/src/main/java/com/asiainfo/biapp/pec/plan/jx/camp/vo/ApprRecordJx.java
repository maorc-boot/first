package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author : ranpf
 * @date : 2023-4-18 20:45:54
 *
 */
@ApiModel(value = "江西审批列表实体", description = "江西审批列表实体")
@Data
public class ApprRecordJx extends CmpApproveProcessRecordJx {

    @ApiModelProperty(value = "策略ID")
    private String campsegId;

    @ApiModelProperty(value = "策略名称")
    private String campsegName;

    @ApiModelProperty(value = "策略创建人ID")
    private String createUserId;

    @ApiModelProperty(value = "策略策略创建人姓名")
    private String userName;

    @ApiModelProperty(value = "策略创建时间")
    private Date createTime;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "营销场景")
    private String campScene;

}
