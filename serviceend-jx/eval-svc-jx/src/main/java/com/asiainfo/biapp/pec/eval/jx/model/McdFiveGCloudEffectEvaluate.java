package com.asiainfo.biapp.pec.eval.jx.model;

import com.asiainfo.biapp.pec.core.enums.CampStatus;
import com.asiainfo.biapp.pec.core.enums.TransEnum;
import com.asiainfo.biapp.pec.core.enums.TransPropEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 5G云卡效果评估实体
 *
 * @author lvcc
 * @date 2023/10/12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "5G云卡效果评估实体", description = "5G云卡效果评估实体对象")
@TableName("mcd_fiveg_cloud_effect_evaluate")
public class McdFiveGCloudEffectEvaluate implements Serializable {

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
     * 回调类型
     */
    @ApiModelProperty("回调类型")
    @TableField("CALLBACK_TYPE")
    private String callbackType;

    /**
     * 客群数量(总人数)
     */
    @ApiModelProperty("客群数量(群发总人数)")
    @TableField("CUST_NUM")
    private Long custNum;

    /**
     * 客群数量(总人数)
     */
    @ApiModelProperty("下发总人数(曝光表中)经过过滤后的")
    @TableField("SEND_NUM")
    private Long sendNum;

    /**
     * 成功用户数
     */
    @ApiModelProperty("送达用户数")
    @TableField("SUCCESS_NUM")
    private Long successNum;

    /**
     * 成功率
     */
    @ApiModelProperty("送达成功率")
    @TableField("SUCCESS_RATE")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "#.####")
    private Double successRate;

}
