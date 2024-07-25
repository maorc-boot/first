package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 活动执行信息
 */
@Data
@ApiModel(value = "江西渠道执行信息")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class McdCampExcuteInfo {

    @ApiModelProperty(value = "渠道ID")
    private String channelId;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "当前执行中的活动所涉及的客群总数量" )
    private int useCustNum;

    @ApiModelProperty(value = "当前执行中的活动数量")
    private int campsegNum;

    @ApiModelProperty(value = "每个渠道的能力")
    private int channelCapacity;

}
