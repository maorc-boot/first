package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("区县信息")
public class McdCountryVo {

    @ApiModelProperty("区县ID")
    private String countyId;

    @ApiModelProperty("区县名称")
    private String countyName;
}
