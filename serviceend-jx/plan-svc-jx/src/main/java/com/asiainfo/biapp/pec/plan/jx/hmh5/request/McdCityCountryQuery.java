package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("地市信息")
public class McdCityCountryQuery {

    @ApiModelProperty("地市ID")
    private String cityId;

    @ApiModelProperty("区县ID")
    private String countryId;

    @ApiModelProperty("地市ID")
    private String gridId;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束日期")
    private String endTime;
}
