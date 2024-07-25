package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 客户通预警报表（区县）
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@Data
@ApiModel("个人客户通预警报表查询结果模型")
public class ReportYjStaffResultInfo implements Serializable {

/*    *//**
     * 新增主键id
     *//*
    @ApiModelProperty(value = "新增主键id")
    private Integer warnId;

    *//**
     * 城市id（最多4个字符）
     *//*
    @ApiModelProperty(value = "城市id")
    private String cityId;

    *//**
     * 城市名（最多16个字符）
     *//*
    @ApiModelProperty(value = "城市名")
    private String cityName;*/

/*    *//**
     * 区县id（最多8个字符）
     *//*
    @ApiModelProperty(value = "区县id")
    private String countyId;

    *//**
     * 区县名字（最多16个字符）
     *//*
    @ApiModelProperty(value = "区县名字")
    private String countyName;*/



/*    *//**
     * 网格编号（最多16个字符）
     *//*
    @ApiModelProperty(value = "网格编号")
    private String gridCode;

    *//**
     * 网格名称（最多64个字符）
     *//*
    @ApiModelProperty(value = "网格名称")
    private String gridName;*/




    /**
     * 工号id（最多16个字符）
     */
    @ApiModelProperty(value = "工号id")
    private String staffId;

    /**
     * 工号名称（最多64个字符）
     */
    @ApiModelProperty(value = "工号名称")
    private String staffName;

    /**
     * 预警类型（最多32个字符）
     */
    @ApiModelProperty(value = "预警类型")
    private String alarmName;

    /**
     * 近30天任务总量
     */
    @ApiModelProperty(value = "近30天任务总量")
    private Integer rw;

    /**
     * 当日接触量
     */
    @ApiModelProperty(value = "当日接触量")
    private Integer drJc;

    /**
     * 当日解决量
     */
    @ApiModelProperty(value = "当日解决量")
    private Integer drJj;

    /**
     * 近30天接触量
     */
    @ApiModelProperty(value = "近30天接触量")
    private Integer jcNum;

    /**
     * 近30天解决量
     */
    @ApiModelProperty(value = "近30天解决量")
    private Integer jjNum;

/*    *//**
     * 不知道实际意义，可能为导出日期
     *//*
    @ApiModelProperty(value = "导出日期")
    private LocalDate statDate;*/

    /**
     * 业务办理量
     */
    @ApiModelProperty(value = "业务办理量")
    private Integer ybl;


}
