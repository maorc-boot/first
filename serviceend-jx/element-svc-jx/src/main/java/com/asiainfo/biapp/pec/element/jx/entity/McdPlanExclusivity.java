package com.asiainfo.biapp.pec.element.jx.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 产品互斥关系表
 * </p>
 *
 * @author mamp
 * @since 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiOperation("江西主产品互斥产品关系")
@TableName("mcd_plan_exclusivity")
public class McdPlanExclusivity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品编码
     */
    @TableField("PLAN_ID")
    @ApiModelProperty(value = "主产品编码")
    private String planId;

    /**
     * 产品名称
     */
    @TableField("PLAN_NAME")
    @ApiModelProperty(value = "主产品名称")
    private String planName;

    /**
     * 互斥产品编码
     */
    @TableField("EX_PLAN_ID")
    @ApiModelProperty(value = "互斥产品编码,多个用逗号','隔开")
    private String exPlanId;

    /**
     * 互斥产品名称
     */
    @TableField("EX_PLAN_NAME")
    @ApiModelProperty(value = "互斥产品名称,多个用逗号','隔开")
    private String exPlanName;

    /**
     * 数据来源  0：系统，1：CRM
     */
    @TableField("SOURCE_TYPE")
    @ApiModelProperty(value = "数据来源  0：系统，1：CRM")
    private Integer sourceType;

    /**
     * 产品所属组ID
     */
    @TableField("PLAN_GROUP_ID")
    @ApiModelProperty(value = "产品所属组ID")
    private String planGroupId;

    /**
     * 产品类型：1 主产品；0 互斥产品
     */
    @TableField("TYPE")
    @ApiModelProperty(value = "产品类型：1 主产品；0 互斥产品")
    private Integer type;


}
