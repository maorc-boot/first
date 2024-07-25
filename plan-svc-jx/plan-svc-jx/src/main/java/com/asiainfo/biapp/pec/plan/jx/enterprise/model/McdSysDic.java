package com.asiainfo.biapp.pec.plan.jx.enterprise.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "mcd_plan_def")
@ApiModel(value = "EnterPriseMapper对象",description = "数据字典表")
public class McdSysDic {

  @ApiModelProperty(value = "运行环境")
  @TableId(value = "DIC_PRO_FILE")
  private String dicProfile;

  @ApiModelProperty(value = "数据KEY")
  @TableId(value = "DIC_KEY")
  private String dicKey;

  @ApiModelProperty(value = "数据值")
  @TableField("DIC_VALUE")
  private String dicValue;

  @ApiModelProperty(value = "数据名称")
  @TableField("DIC_NAME")
  private String dicName;

  @ApiModelProperty(value = "数据值描述")
  @TableField("DIC_DESC")
  private String dicDesc;

  @ApiModelProperty(value = "显示顺序")
  @TableField("DIC_ORDER")
  private long dicOrder;

  @ApiModelProperty(value = "创建人")
  @TableField("DIC_CREATE_USER")
  private String dicCreateUser;

  @ApiModelProperty(value = "创建时间")
  @TableField("DIC_CREATE_TIME")
  private java.sql.Timestamp dicCreateTime;

  @ApiModelProperty(value = "数据值父值")
  @TableField("DIC_dATA_PVALUE")
  private String dicDataPvalue;


}
