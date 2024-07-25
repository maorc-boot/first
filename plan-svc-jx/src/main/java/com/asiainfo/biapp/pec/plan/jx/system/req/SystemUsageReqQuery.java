package com.asiainfo.biapp.pec.plan.jx.system.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("江西:系统使用情况请求入参")
public class SystemUsageReqQuery   {

    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;

   @ApiModelProperty("开始时间")
    private String beginTime;

   @ApiModelProperty("结束时间")
    private String endTime;

}
