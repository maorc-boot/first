package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * Description : 转至沟通人员
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-2-28
 * //@modified By :
 * //@since :
 */

@Data
@ApiModel(value = "江西渠道执行下发信息")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class McdChannelExecInfo {


    @ApiModelProperty(value = "渠道ID")
    private String channelId;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "渠道可推送数量" )
    private int execCapacity;

    @ApiModelProperty(value = "当日已执行数量")
    private int execNum;

    @ApiModelProperty(value = "当日待执行数量" )
    private  int noExecNum;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;




}
