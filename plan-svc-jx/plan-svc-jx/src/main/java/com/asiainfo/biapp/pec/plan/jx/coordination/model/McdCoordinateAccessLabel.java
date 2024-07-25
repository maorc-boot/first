package com.asiainfo.biapp.pec.plan.jx.coordination.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("已接入标签表表")
@TableName("mcd_coordinate_tag")
public class McdCoordinateAccessLabel {

  @ApiModelProperty(value = "标签ID")
  @TableField("LABEL_ID")
  private String labelId;

  @ApiModelProperty(value = "标签名称")
  @TableField("LABEL_NAME")
  private String labelName;

  @ApiModelProperty(value = "客户类型")
  @TableField("CUST_TYPE")
  private String custType;

}
