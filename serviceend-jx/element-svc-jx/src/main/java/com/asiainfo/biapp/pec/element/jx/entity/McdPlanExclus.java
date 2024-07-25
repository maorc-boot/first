package com.asiainfo.biapp.pec.element.jx.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
@ApiOperation("江西查看主产品互斥产品关系")
public class McdPlanExclus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "主产品编码")
    private String planId;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "主产品名称")
    private String planName;

    /**
     * 数据来源  0：系统，1：CRM
     */
    @ApiModelProperty(value = "数据来源  0：系统，1：CRM")
    private Integer sourceType;

    /**
     * 产品所属组ID
     */
    @ApiModelProperty(value = "产品所属组ID")
    private String planGroupId;

    /**
     * 产品类型：1 主产品；0 互斥产品
     */
    @ApiModelProperty(value = "产品类型：1 主产品；0 互斥产品")
    private Integer type;

    /**
     * 互斥产品list
     */
    @ApiModelProperty(value = "产品所属组ID")
    private List<McdPlanExclusivity> planExcluList;


}
