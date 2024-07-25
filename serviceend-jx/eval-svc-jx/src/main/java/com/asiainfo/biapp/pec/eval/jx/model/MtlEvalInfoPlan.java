package com.asiainfo.biapp.pec.eval.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 产品效果评估
 * </p>
 *
 * @author mamp
 * @since 2022-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("产品评估")
public class MtlEvalInfoPlan implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 时间
     */
    @ApiModelProperty("数据日期")
    @TableField("STAT_DATE")
    private String statDate;

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    @TableField("PLAN_ID")
    private String planId;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    @TableField("PLAN_NAME")
    private String planName;

    /**
     * 地市ID
     */
    @ApiModelProperty("地市ID")
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 地市ID
     */
    @ApiModelProperty("地市名称")
    @TableField("CITY_NAME")
    private String cityName;
    /**
     * 区县ID
     */
    @ApiModelProperty("区县ID")
    @TableField("COUNTY_ID")
    private String countyId;

    /**
     * 区县名称
     */
    @ApiModelProperty("区县名称")
    @TableField("COUNTY_NAME")
    private String countyName;

    /**
     * 渠道ID
     */
    @ApiModelProperty("渠道ID")
    @TableField("CHANNEL_ID")
    private String channelId;

    /**
     * 渠道名称
     */
    @ApiModelProperty("渠道名称")
    @TableField("CHANNEL_ID")
    private String channelName;

    /**
     * 目标用户数
     */
    @ApiModelProperty("目标用户数")
    @TableField("TARGET_USER_NUM")
    private Integer targetUserNum;

    /**
     * 营销用户数
     */
    @ApiModelProperty("营销用户数")
    @TableField("CAMP_USER_NUM")
    private Integer campUserNum;

    /**
     * 营销成功用户数
     */
    @ApiModelProperty("营销成功用户数")
    @TableField("CAMP_SUCC_NUM")
    private Integer campSuccNum;

    /**
     * 营销成功率
     */
    @ApiModelProperty("营销成功率")
    @TableField("CAMP_SUCC_RATE")
    private BigDecimal campSuccRate;

    /**
     * 订购用户数
     */
    @ApiModelProperty("订购用户数")
    @TableField("TOTAL_ORDER_NUM")
    private Integer totalOrderNum;

    /**
     * 点击次数
     */
    @ApiModelProperty("点击次数")
    @TableField("CK_NUM")
    private Integer ckNum;

    /**
     * 曝光次数
     */
    @ApiModelProperty("曝光次数")
    @TableField("PV_NUM")
    private Integer pvNum;

    /**
     * 转化率
     */
    @ApiModelProperty("转化率")
    @TableField("PV_CONVERT_RATE")
    private BigDecimal pvConvertRate;

    /**
     * 用户覆盖率
     */
    @ApiModelProperty("用户覆盖率")
    @TableField("USER_COVERAGE_RATE")
    private BigDecimal userCoverageRate;

    @ApiModelProperty("营销收益")
    @TableField("MARKETING_PROFIT")
    private BigDecimal marketingProfit;

    /**
     * 推荐次数
     */
    @ApiModelProperty("推荐次数")
    @TableField("MKT_FB_CNT")
    private BigDecimal mktFbCnt;

    /**
     * 推荐用户数
     */
    @ApiModelProperty("推荐用户数")
    @TableField("MKT_FB_USERS")
    private BigDecimal mktFbUsers;

    /**
     * 推荐成功率
     */
    @ApiModelProperty("推荐成功率    ")
    @TableField("MKT_FB_RATE")
    private BigDecimal mktFbRate;


}
