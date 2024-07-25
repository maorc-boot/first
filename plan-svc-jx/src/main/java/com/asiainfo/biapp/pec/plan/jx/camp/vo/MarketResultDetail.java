package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 单用户针对某一活动的操作生命周期
 */
@Data
@ApiModel(value = "单用户针对某一活动的操作生命周期", description = "单用户针对某一活动的操作生命周期")
public class MarketResultDetail implements Serializable {


    @ApiModelProperty(value = "操作时间")
    private String logTime;
    @ApiModelProperty(value = "执行状态")
    private int status;
    @ApiModelProperty(value = "执行状态描述")
    private String statusDesc;
    @ApiModelProperty(value = "当前活动在该渠道下描述如各种原因")
    private String campsegChannelDescription;

}
