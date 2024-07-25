package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

/**
 * 预警轨迹明细表
 */

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "预警轨迹明细表", description = "预警轨迹明细表")
@TableName("REPORT_YJ_DETAIL_RECORDING")
public class ReportYjDetailRecording {
    @ApiModelProperty("地市ID")
    @TableField("CITY_ID")
    private String cityId;

    @ApiModelProperty("区县ID")
    @TableField("COUNTY_ID")
    private String countyId;

    @ApiModelProperty("网格ID")
    @TableField("AREA_ID")
    private String areaId;

    @ApiModelProperty("网格名称")
    @TableField("GRID_NAME")
    private String gridName;

    @ApiModelProperty("渠道编码")
    @TableField("CHANNEL_ID")
    private String channelId;

    @ApiModelProperty("渠道名称")
    @TableField("CHANNEL_NAME")
    private String channelName;

    @ApiModelProperty("经理工号")
    @TableField("STAFF_ID")
    private String 	staffId;

    @ApiModelProperty("经理姓名")
    @TableField("STAFF_NAME")
    private String 	staffName;

    @ApiModelProperty("账期")
    @TableField("STAT_DATE")
    private String statDate;

    @ApiModelProperty("来源表名")
    @TableField("SOURCE_TABLE")
    private String sourceTable;

    @ApiModelProperty("省专用(预警时间)")
    @TableField("WARNING_DATE")
    private Date warningDate;

    @ApiModelProperty("是否过滤")
    @TableField("FILTER")
    private String 	filter;

    @ApiModelProperty("过滤原因")
    @TableField("FILTER_CASE")
    private String 	filterCase;

    @ApiModelProperty("近30天内预警时间")
    @TableField("CREATE_TIME")
    private Date  createTime;

    @ApiModelProperty("预警号码")
    @TableField("PRODUCT_NO")
    private String productNo;

    @ApiModelProperty("预警id")
    @TableField("ALARM_ID")
    private String 	alarmId;

    @ApiModelProperty("预警名称")
    @TableField("ALARM_NAME")
    private String 	alarmName;

    @ApiModelProperty("预警类型")
    @TableField("ALARM_TYPE")
    private String 	alarmType;

    @ApiModelProperty("任务状态")
    @TableField("DATA_STATUS")
    private String 	dataStatus;

    @ApiModelProperty("接触状态")
    @TableField("JC_STATUS")
    private String 	jcStatus;

    @ApiModelProperty("接触时间")
    @TableField("END_TIME")
    private Date 	end_time;

    @ApiModelProperty("接触时长")
    @TableField("TALK_DURATION")
    private String 	talkDuration;

    @ApiModelProperty("是否修复")
    @TableField("XF")
    private String 	xf;

    @ApiModelProperty("修复时间")
    @TableField("DONE_DATE")
    private Date doneDate;

    @ApiModelProperty("是否闭环")
    @TableField("LATEST_RETURE_VISIT_DESC")
    private String 	latestRetureVisitDesc;

    @ApiModelProperty("是否解警")
    @TableField("IS_CLEAR_WARNING")
    private String isCearWarning;

    @ApiModelProperty("解警原因")
    @TableField("CLEAR_WARNING_DATE")
    private String reaClearWarning;

    @ApiModelProperty("手动闭环时间")
    @TableField("LATEST_RETURE_VISIT_TIME")
    private Date latestRetureVisitTime;

    @ApiModelProperty("闭环失败原因")
    @TableField("BH_FALSE_CASE")
    private String bhFalseCase;
}
