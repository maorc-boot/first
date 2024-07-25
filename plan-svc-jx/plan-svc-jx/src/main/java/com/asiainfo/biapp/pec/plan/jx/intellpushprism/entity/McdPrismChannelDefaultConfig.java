package com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 智推棱镜AI: 渠道信息默认配置实体对象
 *
 * @author: lvchaochao
 * @date: 2024/5/31
 */
@TableName("MCD_PRISM_CHANNEL_DEFAULT_CONFIG")
@ApiModel(value = "McdPrismChannelDefaultConfig对象",description = "智推棱镜AI: 渠道信息默认配置实体对象")
@Data
@Accessors(chain = true)
public class McdPrismChannelDefaultConfig {

    @ApiModelProperty("渠道ID")
    @TableId(value = "CHANNEL_ID")
    private String channelId;

    @ApiModelProperty(value = "运营位ID")
    @TableField("ADIV_ID")
    private String adivId;

    @ApiModelProperty(value = "运营位名称")
    @TableField("ADIV_NAME")
    private String adivName;

    @ApiModelProperty(value = "推荐用语")
    @TableField("EXEC_CONTENT")
    private String execContent;

    @ApiModelProperty(value = "频次控制参数，格式：几天_几次")
    @TableField("FREQUENCY")
    private String frequency;

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
}
