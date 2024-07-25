package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 网格维表
 * </p>
 *
 * @author feify
 * @since 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dim_pub_grid")
@ApiModel(value="Grid对象", description="")
public class Grid extends Model<Grid> {

    private static final long serialVersionUID = 1L;

    /**
     * 网格ID
     */
    @TableId("GRID_ID")
    private String gridId;

    /**
     * 网格名称
     */
    @TableField("GRID_NAME")
    private String gridName;

    /**
     * 上级区县ID
     */
    @TableField("COUNTY_ID")
    private String countyId;

    /**
     * 上级地市ID
     */
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 序号
     */
    @TableField("SORTNUM")
    private Integer sortnum;

    /**
     * 网格描述
     */
    @TableField("GRID_DESC")
    private String gridDesc;


    @Override
    protected Serializable pkVal() {
        return this.gridId;
    }

}
