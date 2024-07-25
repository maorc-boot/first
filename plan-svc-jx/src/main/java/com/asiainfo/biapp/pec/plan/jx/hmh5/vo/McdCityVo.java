package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("地市信息")
public class McdCityVo {

    @ApiModelProperty("地市ID")
    private String cityId;

    @ApiModelProperty("地市名称")
    private String cityName;
}
