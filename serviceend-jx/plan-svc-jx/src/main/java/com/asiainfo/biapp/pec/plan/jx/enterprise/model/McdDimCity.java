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
@ApiModel(value = "EnterPriseMapper对象",description = "地市维表")
public class McdDimCity {

  @ApiModelProperty(value = "地市ID")
  @TableId(value = "CITY_ID")
  private String cityId;

  @ApiModelProperty(value = "地市名称")
  @TableField("CITY_NAME")
  private String cityName;

  @ApiModelProperty(value = "区号")
  @TableField("CITY_CODE")
  private String cityCode;

  @ApiModelProperty(value = "地市描述")
  @TableField("CITY_DESC")
  private String cityDesc;

  @ApiModelProperty(value = "集团统一地市代码")
  @TableField("CITY_UNIFIED_CODE")
  private String cityUnifiedCode;

  @ApiModelProperty(value = "地市名缩写")
  @TableField("CITY_SHORT_NAME")
  private String cityShortName;

  @ApiModelProperty(value = "归属省份")
  @TableField("PROVINCE_ID")
  private String provinceId;




}
