package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 流程配置表
 * </p>
 *
 * @author feify
 * @since 2022-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("CMP_APPROVAL_PROCESS_CONF")
@ApiModel(value="ApproveConfig对象", description="流程配置表")
public class ApproveConfig extends Model<ApproveConfig> {

    private static final long serialVersionUID = -66341662298392184L;

    @ApiModelProperty(value = "ID")
    @TableId("ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "部门ID")
    @TableField("DEPT_ID")
    private String deptId;

    @ApiModelProperty(value = "地市ID")
    @TableField("CITY_ID")
    private String cityId;

    @ApiModelProperty(value = "区县ID")
    @TableField("COUNTY_ID")
    private String countyId;

    @ApiModelProperty(value = "网格ID")
    @TableField("GRID_ID")
    private String gridId;

    @ApiModelProperty(value = "系统ID")
    @TableField("SYSTEM_ID")
    private String systemId;

    @ApiModelProperty(value = "流程ID")
    @TableField("PROCESS_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long processId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
