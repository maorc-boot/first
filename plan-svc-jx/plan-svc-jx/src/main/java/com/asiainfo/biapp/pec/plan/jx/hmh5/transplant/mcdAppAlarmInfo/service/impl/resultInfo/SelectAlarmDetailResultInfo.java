package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo
 * @className: SelectAlarmResultInfo
 * @author: chenlin
 * @description: 自定义预警查询详情结果模型
 * @date: 2023/6/28 15:30
 * @version: 1.0
 */
@Data
@ApiModel("自定义预警查询详情结果模型")
public class SelectAlarmDetailResultInfo {

    @ApiModelProperty(value = "预警alarmId，当查询预警详情时传入")
    private Integer alarmId;

    @ApiModelProperty(value = "预警名称")
    private String alarmName;

    @ApiModelProperty(value = "预警类型名称")
    private String alarmType;

    @ApiModelProperty(value = "数据源库  1:集市库1; 2:集市库2; 3:COC客户群")
    private String sourceDb;

    @ApiModelProperty(value = "来源表")
    private String sourceTable;

    @ApiModelProperty(value = "预警内容")
    private String alarmInfo;

    @ApiModelProperty(value = "预警下发条件字段")
    private String executeCondition;

    @ApiModelProperty(value = "预警下发条件属性字段")
    private String executeConditionVal;

    @ApiModelProperty(value = "预警下发条件字段")
    private List<AlarmCityThrasholdResult> alarmThreshold;

    @ApiModelProperty(value = "预警执行期限")
    private String alarmExecDealtime;

    @ApiModelProperty(value = "预警执行期限单位：小时/h 天/d")
    private String alarmExecDealtimeUnit;

    @ApiModelProperty(value = "预警执行期限是否自选标识")
    private String isDefineTime;

    @ApiModelProperty(value = "关怀短信")
    private String alarmMessage;

    @ApiModelProperty(value = "变量属性")
    private String alarmAttr;

    @ApiModelProperty(value = "开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String startTime;

    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endTime;

    @ApiModelProperty(value = "更新周期 数据周期：0 小时；1 每天；2 每月")
    private Integer alarmCycle;

    @ApiModelProperty(value = "更新周期频率")
    private String alarmCycleTime;

    @ApiModelProperty(value = "是否预演过滤 0：否，1：是")
    private String isPreviewFilter;

    @ApiModelProperty(value = "是否选择自动解警 0：否，1：是", example = "0")
    private String autoClearAlarmStatus;

}
