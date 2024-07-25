package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class McdDimCity {

    @ApiModelProperty("地市编码")
    private String cityId;

    @ApiModelProperty("地市名称 ")
    private String cityName;
}
