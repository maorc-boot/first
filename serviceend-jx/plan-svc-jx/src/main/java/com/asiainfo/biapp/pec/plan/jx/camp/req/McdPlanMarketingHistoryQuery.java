package com.asiainfo.biapp.pec.plan.jx.camp.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("选产品营销历史查询入参")
public class McdPlanMarketingHistoryQuery {


    @ApiModelProperty("产品名称")
    private String planName;
    @ApiModelProperty("开始时间")
    private String startDate;
    @ApiModelProperty("结束时间")
    private String endDate;


}
