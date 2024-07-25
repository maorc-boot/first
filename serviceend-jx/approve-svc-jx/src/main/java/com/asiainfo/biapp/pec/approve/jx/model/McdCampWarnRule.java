package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 策略预警规则
 * </p>
 *
 * @author mamp
 * @since 2023-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "预警规则")
public class McdCampWarnRule implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 规则ID
     */
    @ApiModelProperty(value = "规则ID")
    @TableId(value = "RULE_ID", type = IdType.ID_WORKER_STR)
    private String ruleId;

    /**
     * 预警指标ID
     */
    @ApiModelProperty(value = "预警指标ID,指标在枚举表配置，枚举类型: warn_rule_indicator")
    @TableField("INDICATOR_ID")
    private String indicatorId;

    /**
     * 预警指标名称
     */
    @ApiModelProperty(value = "预警指标名称,指标在枚举表配置，枚举类型: warn_rule_indicator")
    @TableField("INDICATOR_NAME")
    private String indicatorName;

    /**
     * 指标与预警关系符号：> < = 等
     */
    @ApiModelProperty(value = "指标与预警关系符号：> < = 等")
    @TableField("SIGN")
    private String sign;

    /**
     * 预警指标阈值（百分比）
     */
    @ApiModelProperty(value = "预警指标阈值（百分比）")
    @TableField("THRESHOLD")
    private BigDecimal threshold;

    /**
     * 周期类型:1-一次性 0-周期性
     */
    @ApiModelProperty(value = "周期类型:1-一次性 0-周期性")
    @TableField("CYCLE_TYPE")
    private Integer cycleType;

    /**
     * 周期天数
     */
    @ApiModelProperty(value = "周期天数")
    @TableField("CYCLE_DAYS")
    private Integer cycleDays;

    /**
     * 创建人账号
     */
    @ApiModelProperty(value = "创建人账号")
    @TableField("CREATE_USER_ID")
    private String createUserId;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    @TableField("CREATE_USER_NAME")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 预警最后执行时间
     */
    @ApiModelProperty(value = "预警最后执行时间")
    @TableField("EXECUTE_TIME")
    private Date executeTime;

    /**
     * 是否上线：1-上线，0-下线
     */
    @ApiModelProperty(value = "是否上线：1-上线，0-下线")
    @TableField("IS_ONLING")
    private Integer isOnling;


}
