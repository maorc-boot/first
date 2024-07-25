package com.asiainfo.biapp.cmn.jx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author mamp
 * @since 2022-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "返回的User对象", description = "用户表")
public class McdSysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "ID", type = IdType.ID_WORKER)
    private String id;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    @TableField("USER_ID")
    private String userId;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    @TableField("PWD")
    private String pwd;

    /**
     * 状态，0正常
     */
    @ApiModelProperty(value = "状态，0正常")
    @TableField("STATUS")
    private Integer status;

    /**
     * 归属地市
     */
    @ApiModelProperty(value = "归属地市")
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 实际所在地市
     */
    @ApiModelProperty(value = "实际所在地市")
    @TableField("ACTUAL_CITY_ID")
    private String actualCityId;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    @TableField("DEPARTMENT_ID")
    private String departmentId;

    /**
     * 归属科室
     */
    @ApiModelProperty(value = "归属科室")
    @TableField("OFFICE_ID")
    private String officeId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    @TableField("USER_NAME")
    private String userName;

    /**
     * 工号
     */
    @ApiModelProperty(value = "工号")
    @TableField("STAFF_NO")
    private String staffNo;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    @TableField("MOBILE_PHONE")
    private String mobilePhone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @TableField("EMAIL")
    private String email;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_DATE")
    private Date updateDate;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @TableField("DELETE_TIME")
    private String deleteTime;

    /**
     * 删除标记,1逻辑删除
     */
    @ApiModelProperty(value = "删除标记,1逻辑删除")
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 最后登录IP
     */
    @ApiModelProperty(value = "最后登录IP")
    @TableField("LOGIN_IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间")
    @TableField("LOGIN_DATE")
    private Date loginDate;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    @TableField("USER_TYPE")
    private Integer userType;

    /**
     * 出生日期
     */
    @ApiModelProperty(value = "出生日期")
    @TableField("BIRTHDAY")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("NOTES")
    private String notes;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    @TableField("PHOTO")
    private String photo;

    /**
     * OA账号
     */
    @ApiModelProperty(value = "OA账号")
    @TableField("OA_USER_ID")
    private String oaUserId;

    /**
     * BOSS账号
     */
    @ApiModelProperty(value = "BOSS账号")
    @TableField("BOSS_USER_ID")
    private String bossUserId;

    /**
     * 所属省份
     */
    @ApiModelProperty(value = "所属省份")
    @TableField("PROVINCE")
    private String province;

    /**
     * 区县ID
     */
    @ApiModelProperty(value = "区县ID")
    @TableField("COUNTY_ID")
    private String countyId;

    /**
     * 网格ID
     */
    @ApiModelProperty(value = "网格ID")

    @TableField("GRID_ID")
    private String gridId;

    @ApiModelProperty(value = "角色IDs,多个用英文逗号分隔")
    @TableField(exist = false)
    private String roleIds;


}
