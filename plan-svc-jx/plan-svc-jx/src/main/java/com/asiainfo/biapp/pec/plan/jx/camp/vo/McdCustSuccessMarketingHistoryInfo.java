package com.asiainfo.biapp.pec.plan.jx.camp.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("选客群营销历史客户营销情况对象")
public class McdCustSuccessMarketingHistoryInfo {


    @ApiModelProperty("营销成功率等级")
    private String successLevel;
    @ApiModelProperty("成功数")
    private String successNum;
    @ApiModelProperty("成功客户占比")
    private String successRate;
    @ApiModelProperty("客户数")
    private String custNum;

}
