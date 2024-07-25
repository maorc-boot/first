package com.asiainfo.biapp.pec.eval.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("政企营销任务效果评估")
public class EnterpriseMarkeringEffect {

  @TableField("TASK_ID")
  @ApiModelProperty("任务id")
  private String taskId;

  @TableField("TASK_NAME")
  @ApiModelProperty("任务NAME")
  private String taskName;

  @TableField("CAMP_ID")
  @ApiModelProperty("活动ID")
  private String campId;

  @TableField("CAMP_NAME")
  @ApiModelProperty("活动NAME")
  private String campName;

  @TableField("PLAN_ID")
  @ApiModelProperty("产品ID")
  private String planId;

  @TableField("PLAN_NAME")
  @ApiModelProperty("产品NAME")
  private String planName;

  @TableField("INTIME_CAMP_COUNT")
  @ApiModelProperty("及时执行活动数量")
  private double intimeCampCount;


  @TableField("TIMELINESS_RATIO")
  @ApiModelProperty("执行及时率,提交时间/需要完成时间")
  private String timelinessRatio;

  @TableField("CAMP_COUNT")
  @ApiModelProperty("营销活动开展场次")
  private double campCount;

  @TableField("TARGET_USER_NUM")
  @ApiModelProperty("目标用户数,原始客群数")
  private double targetUserNum;

  @TableField("WARM_UP_USER_NUM")
  @ApiModelProperty("预热目标用户数")
  private double warmUpUserNum;

  @TableField("OFFLINE_CAMP_COUNT")
  @ApiModelProperty("驻场营销场次")
  private double offlineCampCount;

  @TableField("ISSUE_USER_NUM")
  @ApiModelProperty("下发用户数")
  private double issueUserNum;

  @TableField("COUNTACT_USER_NUM")
  @ApiModelProperty("接触用户数")
  private double countactUserNum;

  @TableField("SUCESS_USER_NUM")
  @ApiModelProperty("营销成功用户数")
  private double sucessUserNum;

  @TableField("CONTACT_RATE")
  @ApiModelProperty("用户接触率,已下发/已接触")
  private String contactRate;

  @TableField("CONVERSION_RATE")
  @ApiModelProperty("营销转化率,已下发/营销成功")
  private String conversionRate;

  @TableField("CITY_ID")
  @ApiModelProperty("地市ID")
  private String cityId;

  @TableField("CITY_NAME")
  @ApiModelProperty("地市NAME")
  private String cityName;

  @TableField("COUNTY_ID")
  @ApiModelProperty("区县ID")
  private String countyId;

  @TableField("COUNTY_NAME")
  @ApiModelProperty("区县NAME")
  private String countyName;

  @TableField("GRID_ID")
  @ApiModelProperty("网格ID")
  private String gridId;

  @TableField("GRID_NAME")
  @ApiModelProperty("网格NAME")
  private String gridName;

  @TableField("MANAGER_NAME")
  @ApiModelProperty("客户经理NAME")
  private String managerName;

  @TableField("GROUPS_ID")
  @ApiModelProperty("集团ID")
  private String groupsId;

  @TableField("GROUPS_NAME")
  @ApiModelProperty("集团NAME")
  private String groupsName;

  @TableField("GROUPS_TYPE")
  private String groupsType;

  @TableField("MANAGER_ID")
  private String managerId;

  @TableField("XF_CAMP_COUNT")
  private double xfCampCount;

}
