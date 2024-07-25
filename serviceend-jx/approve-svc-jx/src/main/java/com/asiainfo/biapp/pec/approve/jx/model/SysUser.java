package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author feify
 * @since 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MCD_SYS_USER")
@ApiModel(value="User对象", description="用户表")
@JsonIgnoreProperties(value = {"pwd"})
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = -2778889183999744835L;

    @ApiModelProperty(value = "用户ID")
    @TableId("ID")
    private String id;

    @ApiModelProperty(value = "用户账号")
    @TableField("USER_ID")
    private String userId;

    @ApiModelProperty(value = "用户密码")
    @TableField("PWD")
    private String pwd;

    @ApiModelProperty(value = "状态，0正常")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "归属地市")
    @TableField("CITY_ID")
    private String cityId;

    @ApiModelProperty(value = "实际所在地市")
    @TableField("ACTUAL_CITY_ID")
    private String actualCityId;

    @ApiModelProperty(value = "部门ID")
    @TableField("DEPARTMENT_ID")
    private String departmentId;

    @ApiModelProperty(value = "归属科室")
    @TableField("OFFICE_ID")
    private String officeId;

    @ApiModelProperty(value = "用户名称")
    @TableField("USER_NAME")
    private String userName;

    @ApiModelProperty(value = "工号")
    @TableField("STAFF_NO")
    private String staffNo;

    @ApiModelProperty(value = "手机号码")
    @TableField("MOBILE_PHONE")
    private String mobilePhone;

    @ApiModelProperty(value = "邮箱")
    @TableField("EMAIL")
    private String email;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_BY")
    private String createBy;

    @ApiModelProperty(value = "更新者")
    @TableField("UPDATE_BY")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_DATE")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    @ApiModelProperty(value = "删除时间")
    @TableField("DELETE_TIME")
    private String deleteTime;

    @ApiModelProperty(value = "删除标记,1逻辑删除")
    @TableField("DEL_FLAG")
    private Integer delFlag;

    @ApiModelProperty(value = "最后登录IP")
    @TableField("LOGIN_IP")
    private String loginIp;

    @ApiModelProperty(value = "最后登录时间")
    @TableField("LOGIN_DATE")
    private Date loginDate;

    @ApiModelProperty(value = "用户类型")
    @TableField("USER_TYPE")
    private Integer userType;

    @ApiModelProperty(value = "出生日期")
    @TableField("BIRTHDAY")
    private Date birthday;

    @ApiModelProperty(value = "备注")
    @TableField("NOTES")
    private String notes;

    @ApiModelProperty(value = "用户头像")
    @TableField("PHOTO")
    private String photo;

    @ApiModelProperty(value = "OA账号")
    @TableField("OA_USER_ID")
    private String oaUserId;

    @ApiModelProperty(value = "BOSS账号")
    @TableField("BOSS_USER_ID")
    private String bossUserId;

    @ApiModelProperty(value = "所属省份")
    @TableField("PROVINCE")
    private String province;

    @ApiModelProperty(value = "区县ID")
    @TableField("COUNTY_ID")
    private String countyId;

    @ApiModelProperty(value = "网格ID")
    @TableField("GRID_ID")
    private String gridId;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
