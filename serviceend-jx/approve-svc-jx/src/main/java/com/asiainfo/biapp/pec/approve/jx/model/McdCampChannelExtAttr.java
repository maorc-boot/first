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
 * 渠道运营位配置表
 * </p>
 *
 * @author imcd
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_camp_channel_ext_attr")
@ApiModel(value="McdCampChannelExtAttr对象", description="渠道运营位配置表")
public class McdCampChannelExtAttr extends Model<McdCampChannelExtAttr> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "渠道ID")
    @TableId("CHANNEL_ID")
    private String channelId;

    @ApiModelProperty(value = "运营位ID")
    @TableField("CHANNEL_ADIV_ID")
    private String channelAdivId;

    @ApiModelProperty(value = "字段名")
    @TableField("ATTR_NAME")
    private String attrName;

    @ApiModelProperty(value = "字段类型")
    @TableField("ATTR_TYPE")
    private String attrType;

    @ApiModelProperty(value = "是否需要转义 0：不需要 1：需要")
    @TableField("IS_ENUM")
    private Boolean isEnum;

    @ApiModelProperty(value = "字段显示名")
    @TableField("ATTR_DISPLAY_NAME")
    private String attrDisplayName;

    @ApiModelProperty(value = "字段描述")
    @TableField("ATTR_DESC")
    private String attrDesc;

    @ApiModelProperty(value = "字段排序号")
    @TableField("ATTR_ORDER")
    private Integer attrOrder;

    @ApiModelProperty(value = "是否显示 0：不显示 1：显示")
    @TableField("IS_DISPLAY")
    private Boolean isDisplay;

    @ApiModelProperty(value = "控件类型 1：单选框 2：复选框 3：日期 4：文本框 5：日期(带时间)")
    @TableField("CONTROL_TYPE")
    private Integer controlType;


    @Override
    protected Serializable pkVal() {
        return this.channelId;
    }

}
