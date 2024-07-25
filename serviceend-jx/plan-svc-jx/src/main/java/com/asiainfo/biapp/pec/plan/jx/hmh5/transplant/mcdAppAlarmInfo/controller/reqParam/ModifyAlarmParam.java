package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.AlarmCityThrasholdResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam
 * @className: SaveOrUpdateAlarmParam
 * @author: chenlin
 * @description: 新增自定义预警请求参数
 * @date: 2023/6/28 17:21
 * @version: 1.0
 */
@Data
@ApiModel("修改自定义预警请求参数")
public class ModifyAlarmParam {

    /**
     * 预警ID
     */
    @ApiModelProperty(value = "预警ID")
    @NotNull(message = "预警ID值不可为空！")
    @Min(value = 1,message = "无效的预警ID值！")
    private Integer alarmId;

    /**
     * 预警名称
     */
    @ApiModelProperty(value = "预警名称", example = "测试预警1")
    @NotBlank(message = "预警名称不可为空！")
    private String alarmName;

    /**
     * 预警类型
     */
    @ApiModelProperty(value = "字典value值（从自定义预警类别查询到的dicValue值）",example = "yjlx1")
    @NotBlank(message = "字典value值不可为空！")
    private String alarmType;

    /**
     * 1:集市库1; 2:集市库2; 3:COC客户群;
     */
    @ApiModelProperty(value = "从/queryDataSourceList获取数据源列表得到的结果")
    @NotBlank(message = "数据源不可为空！")
    private String sourceDb;

    /**
     * 数据来源表
     */
    @ApiModelProperty(value = "数据来源表名称")
    @NotBlank(message = "数据来源表名称不可为空！")
    @Pattern(regexp = "\\S+", message = "表名称不能含有空字符！")
    private String sourceTable;

    /**
     * 预警内容
     */
    @ApiModelProperty(value = "预警内容")
    @NotBlank(message = "预警内容不可为空！")
    private String alarmInfo;

    /**
     * 关怀短信
     */
    @ApiModelProperty(value = "关怀短信")
    @NotBlank(message = "关怀短信不可为空！")
    private String alarmMessage;

    /**
     * 变量属性：变量名1@字段名1,变量名2@字段名2...
     */
    @ApiModelProperty(value = "变量属性：变量名1@字段名1,变量名2@字段名2")
    private String alarmAttr;

    /**
     * 状态：1 有效 ；0 无效；2 终止
     */
    @ApiModelProperty(value = "状态：1 有效 ；0 无效；2 终止",example = "1")
    private Integer dataStatus = 1;

    /*    *//**
     * 创建日期
     *//*
    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;*/


    /**
     * 审批状态：0 草稿；1 审批完成；2 未审批；3 终止;4 审批驳回
     */
    @ApiModelProperty(value = "审批状态：0 草稿；1 审批完成；2 未审批；3 终止;4 审批驳回", example = "0")
    private Integer approveStatus = 0;

    /*    *//**
     * 更新日期
     *//*
    @ApiModelProperty(value = "更新日期")
    private LocalDateTime updateTime;*/

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始日期")
    @NotNull(message = "开始日期不可为空！")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束日期")
    @NotNull(message = "结束日期不可为空！")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    /**
     * 是否已过期清理： 1 已清理 0 未清理
     */
    @ApiModelProperty(value = "是否已过期清理： 1 已清理 0 未清理")
    private Integer isClear = 0;

    /**
     * 数据周期：0 小时；1 每天；2 每月
     */
    @ApiModelProperty(value = "数据周期：0：小时，1：每天，2：每月", example = "1")
    @NotNull(message = "数据周期不可为空！")
    @Min(value = 0, message = "无效的alarmCycle值！")
    @Max(value = 2, message = "无效的alarmCycle值！")
    private Integer alarmCycle;

    /**
     * 周期更新时间点
     */
    @ApiModelProperty(value = "周期更新时间点", example = "1")
    @NotBlank(message = "周期更新时间点不可为空！")
    private String alarmCycleTime;

    /**
     * 是否预演过滤
     */
    @ApiModelProperty(value = "是否预演过滤 0：否，1：是", example = "0")
    @NotBlank(message = "预演过滤状态不可为空！")
    @Pattern(regexp = "(0|1)", message = "无效的预演状态值！")
    private String isPreviewFilter;

    @ApiModelProperty(value = "是否选择自动解警 0：否，1：是", example = "0")
    private String autoClearAlarmStatus;

    /**
     * 是否预演过滤
     */
    @ApiModelProperty(value = "预警下发条件字段",example = "0")
    private String executeCondition;

    /**
     * 预警下发条件属性字段
     */
    @ApiModelProperty(value = "预警下发条件属性字段")
    private String executeConditionVal;

    /**
     * 预警下发预警阈值
     */
    @ApiModelProperty(value = "预警下发预警阈值")
    private List<AlarmCityThrasholdResult> alarmThreshold;

    /**
     * 预警执行期限
     */
    @ApiModelProperty(value = "预警执行期限")
    private String alarmExecDealtime;

    /**
     * 预警执行期限单位
     */
    @ApiModelProperty(value = "预警执行期限单位")
    private String alarmExecDealtimeUnit;

    /**
     * 预警执行期限是否自选标识
     */
    @ApiModelProperty(value = "预警执行期限是否自选标识")
    private boolean isDefineTime;
}
