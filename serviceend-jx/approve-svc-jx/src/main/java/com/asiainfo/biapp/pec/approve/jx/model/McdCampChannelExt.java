package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 策略渠道运营位扩展属性表
 * </p>
 *
 * @author imcd
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_camp_channel_ext")
@ApiModel(value="McdCampChannelExt对象", description="策略渠道运营位扩展属性表")
public class McdCampChannelExt extends Model<McdCampChannelExt> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "策略ID")
    @TableId("CAMPSEG_ID")
    private String campsegId;

    @ApiModelProperty(value = "字段1")
    @TableField("COLUMN_EXT1")
    private String columnExt1;

    @ApiModelProperty(value = "字段2")
    @TableField("COLUMN_EXT2")
    private String columnExt2;

    @ApiModelProperty(value = "字段3")
    @TableField("COLUMN_EXT3")
    private String columnExt3;

    @ApiModelProperty(value = "字段4")
    @TableField("COLUMN_EXT4")
    private String columnExt4;

    @ApiModelProperty(value = "字段5")
    @TableField("COLUMN_EXT5")
    private String columnExt5;

    @ApiModelProperty(value = "字段6")
    @TableField("COLUMN_EXT6")
    private String columnExt6;

    @ApiModelProperty(value = "字段7")
    @TableField("COLUMN_EXT7")
    private String columnExt7;

    @ApiModelProperty(value = "字段8")
    @TableField("COLUMN_EXT8")
    private String columnExt8;

    @ApiModelProperty(value = "字段9")
    @TableField("COLUMN_EXT9")
    private String columnExt9;

    @ApiModelProperty(value = "字段10")
    @TableField("COLUMN_EXT10")
    private String columnExt10;

    @ApiModelProperty(value = "字段11")
    @TableField("COLUMN_EXT11")
    private String columnExt11;

    @ApiModelProperty(value = "字段12")
    @TableField("COLUMN_EXT12")
    private String columnExt12;

    @ApiModelProperty(value = "字段13")
    @TableField("COLUMN_EXT13")
    private String columnExt13;

    @ApiModelProperty(value = "字段14")
    @TableField("COLUMN_EXT14")
    private String columnExt14;

    @ApiModelProperty(value = "字段15")
    @TableField("COLUMN_EXT15")
    private String columnExt15;

    @ApiModelProperty(value = "字段16")
    @TableField("COLUMN_EXT16")
    private String columnExt16;

    @ApiModelProperty(value = "字段17")
    @TableField("COLUMN_EXT17")
    private String columnExt17;

    @ApiModelProperty(value = "字段18")
    @TableField("COLUMN_EXT18")
    private String columnExt18;

    @ApiModelProperty(value = "字段19")
    @TableField("COLUMN_EXT19")
    private String columnExt19;

    @ApiModelProperty(value = "字段20")
    @TableField("COLUMN_EXT20")
    private String columnExt20;


    @Override
    protected Serializable pkVal() {
        return this.campsegId;
    }

}
