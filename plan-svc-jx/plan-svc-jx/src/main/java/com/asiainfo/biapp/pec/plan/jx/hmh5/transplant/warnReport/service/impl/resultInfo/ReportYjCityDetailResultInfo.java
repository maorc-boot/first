package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 客户通预警报表（城市）
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@Data
@ApiModel("地市客户通预警报表查询结果模型")
public class ReportYjCityDetailResultInfo implements Serializable {

    /**
     * 城市id（最多4个字符）
     */
    @ApiModelProperty(value = "地市id")
    private String cityId;

    /**
     * 城市名（最多16个字符）
     */
    @ApiModelProperty(value = "地市名")
    private String cityName;

    /**
     * 预警类型（最多32个字符）
     */
    @ApiModelProperty(value = "预警类型")
    private String alarmName;

    /**
     * 近30天任务总量
     */
    @ApiModelProperty(value = "近30天任务量")
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

    /**
     * 业务办理量
     */
    @ApiModelProperty(value = "业务办理量")
    private Integer ybl;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyyMMdd")
    private String statDate;
}
