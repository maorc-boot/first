package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 客户通预警报表（明细）
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@Data
@ApiModel("客户通预警报表查询结果模型")
public class ReportYjDetailResultInfo implements Serializable {
    
/*    *//**
     * 新增主键id
     *//*
    @ApiModelProperty(value = "新增主键id")
    private Integer warnId;*/

    /**
     * 客户隐藏手机号（最多14个字符，原字段PRODUCT_NO_1）
     */
    @ApiModelProperty(value = "客户隐藏手机号")
    private String productNo1;
    // private String phoneNoHide;

    @ApiModelProperty(value = "客户手机号")
    private String productNo;

    /**
     * 预警类型（最多32个字符）
     */
    @ApiModelProperty(value = "预警类型")
    private String alarmName;

    /**
     * 创建人id（最多32个字符，原字段ALARM_CREATE_BY）
     */
    @ApiModelProperty(value = "创建人id")
    private String alarmCreateBy;
    // private String alarmCreateUserId;

/*    *//**
     * 创建人姓名（最多32个字符，新增字段）
     *//*
    @ApiModelProperty(value = "创建人姓名")
    private String alarmCreateUsername;*/

    /**
     * 城市id（最多4个字符）
     */
    @ApiModelProperty(value = "地市编号")
    private String cityId;

    /**
     * 城市名（最多16个字符）
     */
    @ApiModelProperty(value = "地市名称")
    private String cityName;

    /**
     * 区县id（最多8个字符）
     */
    @ApiModelProperty(value = "区县编号")
    private String countyId;

    /**
     * 区县名称（最多16个字符）
     */
    @ApiModelProperty(value = "区县名称")
    private String countyName;

    /**
     * 渠道id（最多16个字符）
     */
    @ApiModelProperty(value = "渠道id")
    private String channelId;

    /**
     * 渠道名称（最多64个字符）
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    /**
     * 网格编号（最多16个字符）
     */
    @ApiModelProperty(value = "网格编号")
    private String gridCode;

    /**
     * 网格名称（最多64个字符）
     */
    @ApiModelProperty(value = "网格名称")
    private String gridName;

    /**
     * 工号id（最多16个字符）
     */
    @ApiModelProperty(value = "工号id")
    private String staffId;

    /**
     * 员工姓名（最多64个字符）
     */
    @ApiModelProperty(value = "员工姓名")
    private String staffName;

    /**
     * 接触时间
     */
    @ApiModelProperty(value = "接触时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String returnVisitTime;

    /**
     * 接触情况（最多16个字符，原字段XX）
     */
    @ApiModelProperty(value = "接触情况")
    private String jcStatu;

    /**
     * 接触结果（最多16个字符，原字段XXX）
     */
    @ApiModelProperty(value = "接触结果")
    private String jcResult;

    /**
     * 接触反馈（最多64个字符）
     */
    @ApiModelProperty(value = "接触反馈")
    private String returnVisitNotes;

    /**
     * 预警创建时间
     */
    @ApiModelProperty(value = "预警创建时间/任务下发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createTime;

    /**
     * 已办理：是，否（字符长度为3，原始数据包含空格）
     */
    @ApiModelProperty(value = "是否办理业务")
    private String ybl;

    /**
     * 活动id（最长32个字符，应当是对应于策略id)
     */
    @ApiModelProperty(value = "活动id")
    private String campsegId;

    /**
     * 活动名称（最长300个字符，应当是对应于策略名称）
     */
    @ApiModelProperty(value = "活动名称")
    private String campsegName;

    @ApiModelProperty(value = "导出日期")
    private String statDate;


}
