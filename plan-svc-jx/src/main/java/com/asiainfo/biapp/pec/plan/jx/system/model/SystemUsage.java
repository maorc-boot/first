package com.asiainfo.biapp.pec.plan.jx.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("江西:系统使用情况信息")
public class SystemUsage implements Serializable {


   @ApiModelProperty("访问次数")
    private int accessNum;

   @ApiModelProperty("客群创建次数")
    private int createCustNum;

   @ApiModelProperty("营销活动创建次数")
    private  int createCampNum;

   @ApiModelProperty("统计时间或周期")
    private String dataDate;



}
