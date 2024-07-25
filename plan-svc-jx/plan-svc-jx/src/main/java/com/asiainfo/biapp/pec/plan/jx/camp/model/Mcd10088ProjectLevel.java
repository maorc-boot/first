package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 10088外呼项目层级表
 * </p>
 *
 * @author mamp
 * @since 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("10088外呼项目层级 ")
@TableName("MCD_10088_PROJECT_LEVEL")
public class Mcd10088ProjectLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableField("ID")
    @ApiModelProperty("ID")
    private String id;

    /**
     * 层级类型
     */
    @TableField("LEVEL_TYPE")
    @ApiModelProperty("层级类型:层级2-iop_projectLevel2_id, 层级3-iop_projectLevel3_id")
    private String levelType;

    /**
     * 层级key
     */
    @TableField("LEVEL_KEY")
    @ApiModelProperty("层级key")
    private String levelKey;

    /**
     * 层级名称
     */
    @TableField("LEVEL_VALUE")
    @ApiModelProperty("层级名称")
    private String levelValue;

    /**
     * 排序字段
     */
    @TableField("LEVEL_ORDER")
    @ApiModelProperty("排序字段")
    private Integer levelOrder;

    @TableField("PARENT_ID")
    @ApiModelProperty("父ID")
    private String parentId;


    @TableField("TASK_TYPE_ID")
    @ApiModelProperty("主任务类型ID")
    private String taskTypeId;

    @TableField("TASK_TYPE_NAME")
    @ApiModelProperty("主任务类型Name")
    private String taskTypeName;

}
