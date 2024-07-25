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
 * 区县维表
 * </p>
 *
 * @author feify
 * @since 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dim_pub_county")
@ApiModel(value="County对象", description="")
public class County extends Model<County> {

    private static final long serialVersionUID = 1L;

    /**
     * 区县ID
     */
    @TableId("COUNTY_ID")
    private String countyId;

    /**
     * 区县名称
     */
    @TableField("COUNTY_NAME")
    private String countyName;

    /**
     * 归属地市ID
     */
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 区县描述
     */
    @TableField("DESC_TXT")
    private String descTxt;


    @Override
    protected Serializable pkVal() {
        return this.countyId;
    }

}
