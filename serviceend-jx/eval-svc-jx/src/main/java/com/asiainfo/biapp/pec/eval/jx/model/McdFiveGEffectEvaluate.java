package com.asiainfo.biapp.pec.eval.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 5G效果评估表
 * </p>
 *
 * @author mamp
 * @since 2022-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "5G效果评估", description = "开始时间")
public class McdFiveGEffectEvaluate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开始时间
     */

    @TableField("START_DATE")
    @ApiModelProperty("开始时间")
    private String startDate;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @TableField("END_DATE")
    private String endDate;

    /**
     * 策略类型
     */
    @ApiModelProperty("策略类型")
    @TableField("CAMPSEG_TYPE")
    private String campsegType;

    /**
     * 活动ID
     */
    @ApiModelProperty("活动ID")
    @TableField("CAMPSEG_ROOT_ID")
    private String campsegRootId;

    /**
     * 策略名称
     */
    @ApiModelProperty("策略名称")
    @TableField("CAMPSEG_NAME")
    private String campsegName;

    /**
     * 产品类型
     */
    @ApiModelProperty("产品类型")
    @TableField("PLAN_TYPE")
    private String planType;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    @TableField("PLAN_NAME")
    private String planName;

    /**
     * 创建城市NAME
     */
    @ApiModelProperty("创建城市NAME")
    @TableField("CREATECITY_NAME")
    private String createcityName;

    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    @TableField("CREATE_USERID")
    private String createUserid;

    /**
     * 地市
     */
    @ApiModelProperty("地市")
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 地市名称
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
    @TableField("CHANNEL_NAME")
    private String channelName;

    /**
     * 5G消息送达用户数
     */
    @ApiModelProperty("5G消息送达用户数")
    @TableField("CONTACT_NUM")
    private Long contactNum;

    /**
     * 点击用户数
     */
    @ApiModelProperty("点击用户数")
    @TableField("SUCCESS_NUM")
    private Long successNum;

    /**
     * 营销用户数
     */
    @ApiModelProperty("群发总人数")
    @TableField("SGMTNUM")
    private Long sgmtnum;

    /**
     * 5G消息点击率
     */
    @ApiModelProperty("5G消息点击率")
    @TableField("SUCCESS_RATE")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "#.####")
    private Double successRate;

    /**
     * 接触率
     */
    @ApiModelProperty("送达成功率")
    @TableField("CONTACT_RATE")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "#.####")
    private Double contactRate;

    /**
     * 来源类型
     */
    @ApiModelProperty("有无回落")
    @TableField("SOURCETYPE")
    private String sourcetype;

    /**
     * 应用号
     */
    @ApiModelProperty("应用号")
    @TableField("APPLICATION_NUM")
    private String applicationNum;

    /**
     * 成功订购数量
     */
    @ApiModelProperty("总成功订购数")
    @TableField("SUCCESS_ORDER_NUM")
    private String successOrderNum;

    @ApiModelProperty("总成功订购率")
    @TableField("SUCCESS_ORDER_RATE")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "#.####")
    private Double successOrderRate;

    /**
     * 回落类型
     */
    @ApiModelProperty("回落类型")
    @TableField("FALLBACK_TYPE")
    private String fallbackType;

    /**
     * 回落成功数量
     */
    @ApiModelProperty("回落成功数量")
    @TableField("FALLBACK_SUCC_NUM")
    private String fallbackSuccNum;

    /**
     * 回落成功率
     */
    @ApiModelProperty("回落成功率")
    @TableField("FALLBACK_SUCC_RATE")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "#.####")
    private Double fallbackSuccRate;

    @ApiModelProperty("回落成功率")
    @TableField("FALLBACK_SUCC_PEOPLE_NUM")
    private String fallbackSuccPeopleNum;

    /**
     * 非5G消息数量
     */
    @ApiModelProperty("非5G消息数量")
    @TableField("NON_FIVE_G_MESSAGE_NUM")
    private Long nonFiveGMessageNum;


}
