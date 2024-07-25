package com.asiainfo.biapp.pec.plan.jx.camp.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("选客群营销历史接触次数对象")
public class McdCustContactMarketingHistoryInfo {

    @ApiModelProperty("接触次数档位")
    private int contactLevel;
    @ApiModelProperty("接触数")
    private String contactNum;
    @ApiModelProperty("接触客户占比")
    private String contactRate;
}
