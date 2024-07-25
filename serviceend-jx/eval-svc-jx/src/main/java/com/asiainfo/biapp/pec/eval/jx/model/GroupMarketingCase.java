package com.asiainfo.biapp.pec.eval.jx.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 分策略分地市分渠道报表
 * </p>
 *
 * @author shijl8
 * @since 2023-7-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("政企优秀案例信息表")
public class GroupMarketingCase {

  @ApiModelProperty("主键")
  @TableId(value = "ID")
  private String id;

  @ApiModelProperty("案例名称")
  @TableField("CASE_NAME")
  private String caseName;

  @ApiModelProperty("案例描述")
  @TableField("CASE_DESC")
  private String caseDesc;

  @ApiModelProperty("客群推荐")
  @TableField("CUSTOMER_CONTENT")
  private String customerContent;

  @ApiModelProperty("产品推荐")
  @TableField("PRODUCT_CONTENT")
  private String productContent;

  @ApiModelProperty("营销话术")
  @TableField("MARKET_CONTENT")
  private String marketContent;

  @ApiModelProperty("创建人id")
  @TableField("CREATOR_ID")
  private String creatorId;

  @ApiModelProperty("创建人姓名")
  @TableField("CREATOR_NAME")
  private String creatorName;

  @ApiModelProperty("创建时间")
  @TableField("CREATE_TIME")
  private Date createTime;

  @ApiModelProperty("更新时间")
  @TableField("UPDATE_TIME")
  private Date updateTime;

  @ApiModelProperty("区域id")
  @TableField("ORG_ID")
  private String orgId;

  @ApiModelProperty("业务类型")
  @TableField("BIZ_TYPE")
  private String bizType;

  @ApiModelProperty("是否删除 0：否，1：是")
  @TableField("DEL_FLAG")
  private long delFlag;

}
