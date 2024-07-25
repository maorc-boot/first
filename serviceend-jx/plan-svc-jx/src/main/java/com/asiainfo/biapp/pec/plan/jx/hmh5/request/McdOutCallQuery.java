package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class McdOutCallQuery {

    @ApiModelProperty("当前页，默认：1")
    private  Integer pageNum = 1;

    @ApiModelProperty("每页记录数，默认：10")
    private Integer pageSize = 10;

    @ApiModelProperty("地市ID")
    private  String city;

    @ApiModelProperty("区县ID")
    private  String county;

    @ApiModelProperty("渠道")
    private  String channel;

    @ApiModelProperty("经理编码")
    private  String managerId;

    @ApiModelProperty("经理名称")
    private  String managerName;

    @ApiModelProperty("外呼唯一编码")
    private  String onlyId;

    @ApiModelProperty("地市ID")
    private  String btp;

    @ApiModelProperty("主叫号码")
    private  String callerPhone;

    @ApiModelProperty("被叫号码")
    private  String calledPhone;

    @ApiModelProperty("外呼结果")
    private  String result;

    @ApiModelProperty("开始时间")
    private  String startTime;

    @ApiModelProperty("结束时间")
    private  String endTime;

    @ApiModelProperty("网格")
    private  String grid;
}
