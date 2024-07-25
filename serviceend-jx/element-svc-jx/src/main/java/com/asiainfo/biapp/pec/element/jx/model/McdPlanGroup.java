package com.asiainfo.biapp.pec.element.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("mcd_plan_group")
@ApiModel(value = "产品分组表")
public class McdPlanGroup extends Model<McdPlanGroup> {

    @ApiModelProperty(value = "产品分组Id")
    @TableField("PLAN_GROUP_ID")
    private String planGroupId;
    @ApiModelProperty(value = "产品分组名称")
    @TableField("PLAN_GROUP_NAME")
    private String planGroupName;

    @ApiModelProperty(value = "产品分组类型:I组内互斥；W组间互斥")
    @TableField("PLAN_GROUP_TYPE")
    private String planGroupType;
    @ApiModelProperty(value = "W组间互斥时，互斥的产品组ID")
    @TableField("EXCLUDE_GROUP_ID")
    private int excludeGroupId;
    @ApiModelProperty(value = "来源: 0系统自定义；1CRM同步")
    @TableField("SOURCE_TYPE")
    private int sourceType;
    @ApiModelProperty(value = "描述")
    @TableField("REMARK")
    private String remark;


}
