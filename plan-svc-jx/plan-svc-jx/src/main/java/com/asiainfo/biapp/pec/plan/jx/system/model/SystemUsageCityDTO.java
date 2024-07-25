package com.asiainfo.biapp.pec.plan.jx.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("江西:系统使用情况-区域")
public class SystemUsageCityDTO  extends SystemUsage implements Serializable {


    @ApiModelProperty("区域或地市ID")
    private String cityId;

   @ApiModelProperty("区域或地市名称")
    private String cityName;





}
