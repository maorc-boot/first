package com.asiainfo.biapp.pec.plan.jx.camp.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("选客渠道产品销历史客户营销情况对象")
public class McdChanPlanSuccessMarketingHistoryInfo {


    @ApiModelProperty("营销成功率等级 0 0%,1 0%-20%,2 20%-40%,3 40%-60%,4 60%-80%,5 80%-100%")
    private String successLevel;
    @ApiModelProperty("转化率")
    private String successRate;
    @ApiModelProperty("活动数")
    private int campsegNum;

}
