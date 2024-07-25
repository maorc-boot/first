package com.asiainfo.biapp.pec.plan.jx.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("江西:系统使用情况指标详情展示信息")
public class SystemUsageDetail implements Serializable {


   @ApiModelProperty("账号")
    private String userId;

   @ApiModelProperty("所属区县")
    private String cityName;

   @ApiModelProperty("所属部门")
    private  String deptName;

    @ApiModelProperty("操作时间")
    private String optTime;

    @ApiModelProperty("操作行为")
    private String optBehavior;

    @ApiModelProperty("客群名称")
    private String customGroupName;

    @ApiModelProperty("活动名称")
    private String campsegName;




}
