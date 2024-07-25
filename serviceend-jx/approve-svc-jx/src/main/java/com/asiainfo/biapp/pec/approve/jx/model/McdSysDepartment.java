package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 部门表
 * </p>
 *
 * @author ranpf
 * @since 2023-4-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_sys_department")
@ApiModel(value="Department对象", description="部门表")
public class McdSysDepartment extends Model<McdSysDepartment> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门ID")
    @TableId("DEPT_ID")
    private String deptId;

    @ApiModelProperty(value = "直属上级部门ID")
    @TableField("PARENT_ID")
    private String parentId;

    @ApiModelProperty(value = "所有上级部门ID")
    @TableField("PARENT_IDS")
    private String parentIds;

    @ApiModelProperty(value = "部门编码")
    @TableField("DEPT_CODE")
    private String deptCode;

    @ApiModelProperty(value = "部门名称")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "部门描述")
    @TableField("NOTES")
    private String notes;

    @ApiModelProperty(value = "序号")
    @TableField("SORT_NUM")
    private Integer sortNum;

    @ApiModelProperty(value = "状态")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "归属地市ID")
    @TableField("CITY_ID")
    private String cityId;

    @ApiModelProperty(value = "删除时间")
    @TableField("DELETE_TIME")
    private Date deleteTime;

    @ApiModelProperty(value = "归属区域ID")
    @TableField("AREA_ID")
    private Long areaId;

    @ApiModelProperty(value = "机构类型")
    @TableField("TYPE")
    private Integer type;

    @ApiModelProperty(value = "机构等级")
    @TableField("GRADE")
    private Integer grade;

    @ApiModelProperty(value = "联系地址")
    @TableField("ADDRESS")
    private String address;

    @ApiModelProperty(value = "邮政编码")
    @TableField("ZIP_CODE")
    private String zipCode;

    @ApiModelProperty(value = "负责人")
    @TableField("MASTER")
    private String master;

    @ApiModelProperty(value = "电话")
    @TableField("PHONE")
    private String phone;

    @ApiModelProperty(value = "传真")
    @TableField("FAX")
    private String fax;

    @ApiModelProperty(value = "邮箱")
    @TableField("EMAIL")
    private String email;

    @ApiModelProperty(value = "是否启用")
    @TableField("USE_ABLE")
    private String useAble;

    @ApiModelProperty(value = "主负责人")
    @TableField("PRIMARY_PERSON")
    private String primaryPerson;

    @ApiModelProperty(value = "副负责人")
    @TableField("DEPUTY_PERSON")
    private String deputyPerson;

    @ApiModelProperty(value = "创建方式")
    @TableField("CREATE_BY")
    private String createBy;

    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value = "更新方式")
    @TableField("UPDATE_BY")
    private String updateBy;

    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_DATE")
    private Date updateDate;

    @ApiModelProperty(value = "标签专区ID")
    @TableField("COC_SYS_ID")
    private String cocSysId;

    @ApiModelProperty(value = "删除标记,0正常1逻辑删除")
    @TableField("DEL_FLAG")
    private Integer delFlag;

    @ApiModelProperty(value = "部门下的角色ID多个以','分隔")
    @TableField(exist=false)
    private String roleIds;

    @Override
    protected Serializable pkVal() {
        return this.deptId;
    }

}
