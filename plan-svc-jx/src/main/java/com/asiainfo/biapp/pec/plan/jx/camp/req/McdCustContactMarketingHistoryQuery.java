package com.asiainfo.biapp.pec.plan.jx.camp.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("选客群营销历史接触次数查询入参")
public class McdCustContactMarketingHistoryQuery {

    @ApiModelProperty("客群ID")
    private String custGroupId;

    @ApiModelProperty("开始时间")
    private String startDate;
    @ApiModelProperty("结束时间")
    private String endDate;
}
