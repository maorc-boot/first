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
@TableName("mcd_sys_user")
@ApiModel(value = "EnterPriseMapper对象", description = "角色表")
public class McdSysRole {
  @ApiModelProperty(value = "运行环境")
  @TableId(value = "ROLE_ID")
  private long roleId;

  @ApiModelProperty(value = "角色名称")
  @TableField("ROLE_NAME")
  private String roleName;

  @ApiModelProperty(value = "角色英文名称")
  @TableField("EN_NAME")
  private String enName;

  @ApiModelProperty(value = "角色描述")
  @TableField("ROLE_DESC")
  private String roleDesc;

  @ApiModelProperty(value = "部门ID")
  @TableField("DEPARTMENT_ID")
  private String departmentId;

  @ApiModelProperty(value = "角色类型")
  @TableField("ROLE_TYPE")
  private String roleType;

  @ApiModelProperty(value = "数据范围")
  @TableField("DATA_SCOPE")
  private long dataScope;

  @ApiModelProperty(value = "是否系统数据")
  @TableField("IS_SYS")
  private String isSys;

  @ApiModelProperty(value = "是否可用")
  @TableField("USE_ABLE")
  private String useAble;

  @ApiModelProperty(value = "创建人")
  @TableField("CREATE_BY")
  private String createBy;

  @ApiModelProperty(value = "创建时间")
  @TableField("CREATE_DATE")
  private java.sql.Timestamp createDate;

  @ApiModelProperty(value = "更新者")
  @TableField("UPDATE_BY")
  private String updateBy;

  @ApiModelProperty(value = "更新时间")
  @TableField("UPDATE_DATE")
  private java.sql.Timestamp updateDate;

  @ApiModelProperty(value = "描述")
  @TableField("REMARKS")
  private String remarks;

  @ApiModelProperty(value = "删除标记")
  @TableField("DEL_FLAG")
  private long delFlag;




}
