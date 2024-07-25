package com.asiainfo.biapp.pec.approve.jx.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AreaQueryVo {

    @ApiModelProperty(value = "地市ID")
    @JsonProperty(value = "cityId")
    private String cityId;

    @ApiModelProperty(value = "区县ID")
    private String countyId;

}
