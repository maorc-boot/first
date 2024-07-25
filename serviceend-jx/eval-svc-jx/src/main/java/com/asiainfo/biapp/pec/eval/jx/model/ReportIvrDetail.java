package com.asiainfo.biapp.pec.eval.jx.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author mamp
 * @since 2022-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("IVR营销")
public class ReportIvrDetail implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty("活动ID")
    private String campsegId;
    @ApiModelProperty("活动名称")
    private String campsegName;
    @ApiModelProperty("客户群名称")
    private String customGroupName;

    /**
     * 客户群数量
     */
    @ApiModelProperty("客户群数量")
    private Integer targerUserNums;

    /**
     * 日呼出量
     */
    @ApiModelProperty("日呼出量")
    private Integer dHc;

    /**
     * 日接通量
     */
    @ApiModelProperty("日接通量")
    private Integer dJt;

    /**
     * 当日请求人工量
     */
    @ApiModelProperty("当日请求人工量")
    private Integer dRg;

    /**
     * 当日接入人工量
     */
    @ApiModelProperty("当日接入人工量")
    private Integer dJr;

    /**
     * 日营销成功量
     */
    @ApiModelProperty("日营销成功量")
    private Integer dYxsuc;

    /**
     * 当月接入人工量
     */
    @ApiModelProperty("当月接入人工量")
    private Integer mJr;

    /**
     * 月接通量
     */
    @ApiModelProperty("月接通量")
    private Integer mJt;

    /**
     * 当月请求人工量
     */
    @ApiModelProperty("当月请求人工量")
    private Integer mRg;

    /**
     * 月呼出量
     */
    @ApiModelProperty("月呼出量")
    private Integer mHc;

    /**
     * 月营销成功量
     */
    @ApiModelProperty("月营销成功量")
    private Integer mYxcg;

    @ApiModelProperty("数据日期")
    private String statDate;


}
