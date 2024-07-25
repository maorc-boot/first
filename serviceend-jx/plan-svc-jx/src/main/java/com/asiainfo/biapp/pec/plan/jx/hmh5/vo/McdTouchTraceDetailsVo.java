package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("外呼轨迹明细列表")
public class McdTouchTraceDetailsVo {


    @ApiModelProperty("预警id")
    private String alarmId;


    @ApiModelProperty("预警名称")
    private String alarmName;


    @ApiModelProperty("网格id")
    private String areaId;


    @ApiModelProperty("网格名称")
    private String gridName;

    @ApiModelProperty("任务状态：正常、终止、空")
    private String dataStatus;

    @ApiModelProperty("接触状态")
    private String jcStatus;

    @ApiModelProperty("接触时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JsonProperty("end_time")
    private String endTime;

    @ApiModelProperty("通话时长")
    private String talkDuration;

    @ApiModelProperty("是否手动闭环")
    @JsonProperty("bhStatus")
    private String latestRetureVisitDesc;

    @ApiModelProperty("闭环时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JsonProperty("bhTime")
    private String latestRetureVisitTime;

    @ApiModelProperty("是否修复")
    private String xf;

    @ApiModelProperty("修复时间")
    @JsonProperty("updateTime")
    private String doneDate;

    @ApiModelProperty("是否解警")
    private String isClearWarning;

    @ApiModelProperty("解警原因")
    private String reaClearWarning;

    @ApiModelProperty("解警时间")
    private String clearWarningDate;

    @ApiModelProperty("是否过滤")
    private String filter;

    @ApiModelProperty("过滤原因")
    private String filterCase;

    @ApiModelProperty("账期")
    private String statDate;

    @ApiModelProperty("省专用（预警时间）")
    private String warningDate;

    @ApiModelProperty("任务下发时间：")
    @JsonFormat(pattern = "yyyyMMdd",timezone="GMT+8")
    private String createTime;

    @ApiModelProperty("地市编码")
    private String cityId;

    @ApiModelProperty("地市名称")
    private String cityName;

    @ApiModelProperty("区县编码")
    private String countyId;

    @ApiModelProperty("区县名称")
    @JsonProperty("countryName")
    private String countyName;

    @ApiModelProperty("经理工号")
    private String staffId;

    @ApiModelProperty("经理姓名")
    private String staffName;

    @ApiModelProperty("渠道ID")
    private String channelId;

    @ApiModelProperty("渠道名称")
    private String channelName;


    @ApiModelProperty("来源表名")
    private String sourceTable;

    @ApiModelProperty("预警类型：自定义预警、协同预警")
    private String alarmType;

    @ApiModelProperty("预警号码")
    private String productNo;

    @ApiModelProperty("闭环失败原因")
    @JsonProperty("bhReason")
    private String bhFalseCase;
}
