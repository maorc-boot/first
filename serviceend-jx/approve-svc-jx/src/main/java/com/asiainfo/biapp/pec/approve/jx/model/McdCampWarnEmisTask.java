package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 活动预警任务表
 * </p>
 *
 * @author mamp
 * @since 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "预警详情")
public class McdCampWarnEmisTask implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 唯一识别码
     */
    @ApiModelProperty(value = "唯一识别码")
    @TableField("UNIQUE_IDENTIFIER_ID")
    private String uniqueIdentifierId;

    /**
     * 活动编码
     */
    @ApiModelProperty(value = "活动编码")
    @TableField("CAMPSEG_ID")
    private String campsegId;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    @TableField("CAMPSEG_NAME")
    private String campsegName;

    /**
     * 下发数
     */
    @ApiModelProperty(value = "下发数")
    @TableField("ISSUED_CUSTOMER_NUM")
    private Integer issuedCustomerNum;

    /**
     * 接触率
     */
    @ApiModelProperty(value = "接触率")
    @TableField("CONTACT_RATE")
    private BigDecimal contactRate;

    /**
     * 营销成功率
     */
    @ApiModelProperty(value = "营销成功率")
    @TableField("MARKET_SUCCESS_RATE")
    private BigDecimal marketSuccessRate;

    /**
     * 预警时间
     */
    @ApiModelProperty(value = "预警时间")
    @TableField("WARNING_TIME")
    private Date warningTime;

    /**
     * 地市
     */
    @ApiModelProperty(value = "地市")
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    @TableField("DEPARTMENT_ID")
    private String departmentId;

    /**
     * 活动创建人
     */
    @ApiModelProperty(value = "活动创建人")
    @TableField("CREATER")
    private String creater;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 预警处理状态，0:待处理；1：处理待办成功；2：已发送emis待办；3：发送emis待办失败或异常
     */
    @ApiModelProperty(value = "预警处理状态，0:待处理；1：处理待办成功；2：已发送emis待办；3：发送emis待办失败或异常")
    @TableField("STATUS")
    private Integer status;

    /**
     * 预警处理时间
     */
    @ApiModelProperty(value = "预警处理时间")
    @TableField("WARN_UPDATE_TIME")
    private Date warnUpdateTime;

    /**
     * 活动编码
     */
    @ApiModelProperty(value = "活动编码")
    @TableField("CAMPSEG_PID")
    private String campsegPId;

}
