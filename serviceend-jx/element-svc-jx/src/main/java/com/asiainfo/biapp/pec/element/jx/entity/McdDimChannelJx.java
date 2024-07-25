package com.asiainfo.biapp.pec.element.jx.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 渠道表
 * </p>
 *
 * @author ranpf
 * @since 2022-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_dim_channel")
@ApiModel(value="McdDimChannel渠道信息对象", description="渠道表数据")
public class McdDimChannelJx extends Model<McdDimChannelJx> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "渠道ID")
    @TableId("CHANNEL_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private String channelId;

    @ApiModelProperty(value = "父渠道ID")
    @TableField("PARENT_ID")
    private String parentId;

    @ApiModelProperty(value = "渠道名称")
    @TableField("CHANNEL_NAME")
    private String channelName;

    @ApiModelProperty(value = "渠道编码")
    @TableField("CHANNEL_CODE")
    private String channelCode;

    @ApiModelProperty(value = "渠道类型,1 线上, 0 线下")
    @TableField("CHANNEL_TYPE")
    private Integer channelType;

    @ApiModelProperty(value = "创建人")
    @TableField( "CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField( "CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "显示序号")
    @TableField("DISPLAY_ORDER")
    private Integer displayOrder;

    @ApiModelProperty(value = "状态 1：上线 0：未上线")
    @TableField("ONLINE_STATUS")
    private String onlineStatus;

    @ApiModelProperty(value = "集团统一编码（IOP要求）")
    @TableField("IOP_PREFIX_CODE")
    private String iopPrefixCode;

    @ApiModelProperty(value = "IOP渠道名称")
    @TableField("IOP_CHANNEL_NAME")
    private String iopChannelName;

    @ApiModelProperty(value = "IOP所需要的渠道类型")
    @TableField("IOP_CHANNEL_TYPE")
    private String iopChannelType;

    @ApiModelProperty(value = "渠道描述（IOP要求）")
    @TableField("IOP_DESCRIPTION")
    private String iopDescription;

    @ApiModelProperty(value = "操作员（IOP要求）")
    @TableField("IOP_OPERATOR")
    private String iopOperator;

    @ApiModelProperty(value = "IOP渠道编码")
    @TableField("IOP_CHANNEL_CODE")
    private String iopChannelCode;

    @ApiModelProperty(value = "渠道偏好名称")
    @TableField("CHANNEL_PREDILECTION_NAME")
    private String channelPredilectionName;

    @ApiModelProperty(value = "营销形式,如短信类，营业厅类等")
    @TableField("MARKETING_FORM")
    private String marketingForm;

    @ApiModelProperty(value = "运营位类型,如文件，实时，文件+实时等")
    @TableField("ADIV_TYPE")
    private String adivType;

    @ApiModelProperty(value = "一客一策引擎开关，打开/关闭：1/0")
    @TableField("CHANNEL_TACTICS_SWITCH")
    @JsonProperty("isYkyce")
    private Integer channelTacticsSwitch;

    @ApiModelProperty(value = "提示语")
    @TableField("PROMPT_WORD")
    private String promptWord;

    @ApiModelProperty(value = "附件名称")
    @TableField("FILE_NAME")
    private String fileName;

    @ApiModelProperty(value = "附件路径地址")
    @TableField("FILE_UPLOAD_URL")
    private String fileUploadUrl;

    @ApiModelProperty(value = "预演是否支持 1：支持预演 2：不支持预演 3:强制预演")
    @TableField("PREVIEW_STATUS")
    private Integer previewStatus;

    @ApiModelProperty(value = "渠道归属(政企规范), 1-内部系统, 2-外部合作方")
    @TableField("CHANNEL_AFFILIATION")
    private Integer channelAffiliation;

    @ApiModelProperty(value = "触点类型(政企规范)1-政企, 2-大市场, 0-其它")
    @TableField("CHANNEL_TYPE_ZQ")
    private Integer channelTypeZq;

    @ApiModelProperty(value = "知能话术开关 1-开启,  0-关闭")
    @TableField("SMART_SCRIPT")
    private Integer smartScript;
    
    @Override
    protected Serializable pkVal() {
        return this.channelId;
    }

}
