package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author ranpf
 * @since 2023-04-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mcd_sys_city")
@ApiModel(value = "McdSysCity对象", description = "")
public class McdSysCity extends Model<McdSysCity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("城市ID")
    @TableId("CITY_ID")
    private String cityId;

    @ApiModelProperty("城市名字")
    @TableField("CITY_NAME")
    private String cityName;

    @ApiModelProperty("所属父城市ID")
    @TableField("PARENT_ID")
    private String parentId;

    @ApiModelProperty("排序编号")
    @TableField("SORT_NUM")
    private Integer sortNum;

    @ApiModelProperty("空闲ID")
    @TableField("UNKNOWN_FLAG")
    private Integer unknownFlag;

    @ApiModelProperty("运营位城市ID")
    @TableField("DM_CITY_ID")
    private String dmCityId;

    @ApiModelProperty("运营位地市ID")
    @TableField("DM_COUNTY_ID")
    private String dmCountyId;

    @ApiModelProperty("运营位部门ID")
    @TableField("DM_DEPT_ID")
    private String dmDeptId;

    @ApiModelProperty("资源类型")
    @TableField("RESOURCE_TYPE")
    private Integer resourceType;

    @ApiModelProperty("类型编码")
    @TableField("DM_TYPE_CODE")
    private String dmTypeCode;

    @ApiModelProperty("类型ID")
    @TableField("DM_TYPE_ID")
    private String dmTypeId;


    @Override
    public Serializable pkVal() {
        return this.cityId;
    }

}
