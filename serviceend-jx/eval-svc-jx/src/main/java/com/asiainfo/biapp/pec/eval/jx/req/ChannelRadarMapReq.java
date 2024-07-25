package com.asiainfo.biapp.pec.eval.jx.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 渠道雷达图请求入参
 *
 * @author: lvchaochao
 * @date: 2023/2/21
 */
@Data
@ApiModel(value = "渠道雷达图请求入参")
public class ChannelRadarMapReq {

    @ApiModelProperty(value = "地市ID")
    private String cityId;

    @ApiModelProperty("区县ID")
    private String countyId;

    @ApiModelProperty("渠道类型")
    private String channelType;

    @ApiModelProperty("日期")
    private String date;

}
