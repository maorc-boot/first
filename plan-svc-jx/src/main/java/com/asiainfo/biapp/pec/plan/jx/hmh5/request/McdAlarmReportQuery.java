package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  报表查询信息
 */
@Data
@ApiModel("报表查询信息")
public class McdAlarmReportQuery {

    @ApiModelProperty("当前页，默认：1")
    private  Integer pageNum = 1;

    @ApiModelProperty("每页记录数，默认：10")
    private Integer pageSize = 10;

    @ApiModelProperty("地市ID")
    private  String city;

    @ApiModelProperty("区县ID")
    private  String county;


    @ApiModelProperty("网格")
    private  String grid;

    @ApiModelProperty("经理编码")
    private  String managerId;

    @ApiModelProperty("经理名称")
    private  String managerName;

    @ApiModelProperty("预警名称")
    private  String alarmName;

    @ApiModelProperty("预警类型")
    private  String alarmType;

    @ApiModelProperty("任务状态")
    private  String taskStatus;

    @ApiModelProperty("预警号码")
    private  String productNo;

    @ApiModelProperty("接触状态")
    private  String touchStatus;

    @ApiModelProperty("修复状态")
    private  String repairStatus;


    @ApiModelProperty("闭环状态")
    private  String closedStatus;

    @ApiModelProperty("开始时间")
    private  String startTime;

    @ApiModelProperty("结束时间")
    private  String endTime;

}
