package com.asiainfo.biapp.pec.plan.jx.camp.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("选产品营销历史对象")
public class McdPlanMarketingHistoryInfo {

    @ApiModelProperty("策略ID")
    private String campsegId;
    @ApiModelProperty("策略名称")
    private String campsegName;
    @ApiModelProperty("成功数")
    private String successNum;
    @ApiModelProperty("转化率")
    private String successRate;


}
