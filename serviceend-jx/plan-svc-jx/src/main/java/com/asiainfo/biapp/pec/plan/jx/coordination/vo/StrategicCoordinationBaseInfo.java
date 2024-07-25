package com.asiainfo.biapp.pec.plan.jx.coordination.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author : ranpf
 * @date : 2023-5-8 19:54:10
 */
@Data
public class StrategicCoordinationBaseInfo  {

    @ApiModelProperty(value = "子策略ID")
    private String campsegId;

    @ApiModelProperty(value = "根策略ID")
    private String campsegRootId;

    @ApiModelProperty(value = "父策略ID")
    private String campsegPId;

    @ApiModelProperty(value = "策略名称")
    private String campsegName;

    @ApiModelProperty(value = "产品ID")
    private String planId;

    @ApiModelProperty(value = "产品名称")
    private String planName;

    @ApiModelProperty(value = "客群类型  1 一次性,2周期性")
    private int updateCycle;

    @ApiModelProperty("客群类型名称")
    private String updateCycleName;

    @ApiModelProperty(value = "执行周期")
    private String execPeriod;

    @ApiModelProperty(value = "客户数")
    private int customNum;

    @ApiModelProperty(value = "策划人ID")
    private String createUserId;

    @ApiModelProperty(value = "策划人")
    private String createUserName;

    @ApiModelProperty("客群ID")
    private String customGroupId;

    @ApiModelProperty("客群清单文件名称")
    private String fileName;

    @ApiModelProperty("渠道ID")
    private String channelId;

    @ApiModelProperty("运营位ID")
    private String adivId;

    @ApiModelProperty("推送方式 0偏好, 1全量")
    private String pushType;

    @ApiModelProperty("预演策略,  1预演,0不预演")
    private String previewCamp;

}
