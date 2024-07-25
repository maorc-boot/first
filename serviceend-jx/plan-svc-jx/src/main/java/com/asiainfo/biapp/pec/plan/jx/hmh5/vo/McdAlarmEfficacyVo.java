package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 预警能效
 */
@Data
@ApiModel("预警能效信息")
public class McdAlarmEfficacyVo {

    @ApiModelProperty("地市ID")
    private String cityId;

    @ApiModelProperty("区县ID")
        @JsonProperty("countryId")
    private String countyId;

    @ApiModelProperty("区县名称")
    @JsonProperty("countryName")
    private String countyName;

    @ApiModelProperty("网格编号")
    private String gridId;

    @ApiModelProperty("网格名称")
    private String gridName;

    @ApiModelProperty("经理工号")
    private String staffId;

    @ApiModelProperty("经理名称")
    private String staffName;

    @ApiModelProperty("地市名称")
    private String cityName;

    @ApiModelProperty("预警名称")
    private String alarmId;

    @ApiModelProperty("预警名称")
    private String alarmName;

    @ApiModelProperty("预警类型")
    private String alarmType;

    @ApiModelProperty("任务下发：任务数量")
    private String issuedTaskNum;

    @ApiModelProperty("任务下发：用户数量")
    private String issuedUserNum;

    @ApiModelProperty("任务状态全为空：过滤总数量")
    private String taskFilterNum;

    @ApiModelProperty("任务状态全为空：top1过滤原因")
    private String taskFilterReason1;

    @ApiModelProperty("任务状态全为空：top1过滤数量")
    private String taskFilterNum1;

    @ApiModelProperty("任务状态全为空：top2过滤原因")
    private String taskFilterReason2;

    @ApiModelProperty("任务状态全为空：top2过滤数量")
    private String taskFilterNum2;

    @ApiModelProperty("任务状态全为空：top3过滤原因")
    private String taskFilterReason3;

    @ApiModelProperty("任务状态全为空：top3过滤数量")
    private String taskFilterNum3;

    @ApiModelProperty("闭环情况：闭环总数量")
    private String closedNum;

    @ApiModelProperty("闭环情况：人工闭环")
    private String labourClosed;

    @ApiModelProperty("闭环情况：自动解警")
    private String autoClosed;

    @ApiModelProperty("任务状态正常:任务数量")
    private String normalTaskNum;

    @ApiModelProperty("任务状态正常:用户数量")
    private String normalUserNum;

    @ApiModelProperty("任务状态为空：过滤总数量")
    private String unNormalTaskNum;

    @ApiModelProperty("任务状态为空：已接触")
    private String unNormalReached;

    @ApiModelProperty("任务状态为空：已修复")
    @JsonProperty("unNormalFixed")
    private String unNormalFix;

    @ApiModelProperty("任务状态为终止：闭环总数量")
    private String closedTermNum;

    @ApiModelProperty("任务状态为终止：已接触(闭环)")
    private String closedTermReached;

    @ApiModelProperty("任务状态为终止：已修复(闭环)")
    private String closedTermFixed;

    @ApiModelProperty("任务状态为终止：人工闭环")
    @JsonProperty("closedLabour")
    private String closeLabour;

    @ApiModelProperty("任务状态为终止：已接触(人工闭环)")
    private String closedLabourReached;

    @ApiModelProperty("任务状态为终止：已修复(人工闭环)")
    private String closedLabourFiexd;

    @ApiModelProperty("任务状态为终止：自动解警")
    private String closedAuto;

    @ApiModelProperty("任务状态为终止：已接触(自动解警)")
    private String closedAutoReached;

    @ApiModelProperty("任务状态为终止：已修复(自动解警)")
    private String closedAutoFiexd;

    @ApiModelProperty("接触状态：未接触")
    private String unTouch;

    @ApiModelProperty("接触状态：未接通")
    @JsonProperty("noGetTouch")
    private String unGetTouch;

    @ApiModelProperty("接触状态：已接触未达时长")
    private String noReachGetTouch;

    @ApiModelProperty("接触状态：已接触")
    private String reachGetTouch;

    @ApiModelProperty("修复状态：未修复")
    @JsonProperty("fixedRepairState")
    private String fiexdRepairState;

    @ApiModelProperty("修复状态： 已修复")
    private String closedRepairState;

    @ApiModelProperty("正常闭环状态：未闭环")
    private String closeLoopStatus;

    @ApiModelProperty("正常正常闭环状态：人工闭环")
    private String closeLabourDealarm;

    @ApiModelProperty("正常闭环状态：自动解警")
    private String closeAutoDealarm;

    @ApiModelProperty("数据统计日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private String createTime;

}
