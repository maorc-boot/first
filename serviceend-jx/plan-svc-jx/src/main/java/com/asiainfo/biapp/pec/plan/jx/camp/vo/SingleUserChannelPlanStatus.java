package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前活动用户在所关联渠道活动办理情况
 */
@Data
@ApiModel(value = "当前活动用户在所关联渠道活动办理情况", description = "当前活动用户在所关联渠道活动办理情况")
@AllArgsConstructor
@NoArgsConstructor
public class SingleUserChannelPlanStatus {


    @ApiModelProperty(value = "渠道id")
    private String channelId;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "当前用户执行状态")
    private int status;
    @ApiModelProperty(value = "用户办理状态描述")
    private String statusDesc;


}
