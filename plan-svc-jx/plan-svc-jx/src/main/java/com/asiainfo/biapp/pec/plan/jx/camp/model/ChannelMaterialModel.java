package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Description : 素材信息列表
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-3
 * //@modified By :
 * //@since :
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("mcd_dim_material")
@ApiModel(value="江西活动策划素材McdDimMaterial对象", description="营销素材")
public class ChannelMaterialModel extends Model<ChannelMaterialModel> {

    @ApiModelProperty(value = "素材ID")
    @TableId("MATERIAL_ID")
    private String materialId;

    @ApiModelProperty(value = "素材名称")
    @TableField("MATERIAL_NAME")
    private String materialName;

    @ApiModelProperty(value = "素材类型0：图片1：文字2：视频")
    @TableField("MATERIAL_TYPE")
    private Integer materialType;

    @ApiModelProperty(value = "渠道ID")
    @TableField("CHANNEL_ID")
    private String channelId;

    @ApiModelProperty(value = "触点ID")
    @TableField("CONTACT_ID")
    private String contactId;

    @ApiModelProperty(value = "运营位ID")
    @TableField("POSITION_ID")
    private String positionId;

    @ApiModelProperty(value = "文字内容")
    @TableField("CONTENT")
    private String content;

    @ApiModelProperty(value = "附件名称")
    @TableField("ATTACHMENT_NAME")
    private String attachmentName;

    @ApiModelProperty(value = "图片/视频大小")
    @TableField("MATERIAL_SIZE")
    private Float materialSize;

    @ApiModelProperty(value = "图片/视频目标链接地址")
    @TableField("TARGET_URL")
    private String targetUrl;

    @ApiModelProperty(value = "图片/视频存放地址")
    @TableField("RESOURCE_URL")
    private String resourceUrl;

    @ApiModelProperty(value = "状态0：草稿1：审批中2：审批通过 3：驳回 6：可直接使用")
    @TableField("MATERIAL_STATUS")
    private Integer materialStatus;

    @ApiModelProperty(value = "描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


    @ApiModelProperty("创建者")
    @TableField(value = "CREATOR", fill = FieldFill.INSERT)
    private String creator;


    @ApiModelProperty(value = "产品ID")
    @TableField(value = "PLAN_ID")
    private String planId;

    @ApiModelProperty(value = "产品名称")
    @TableField(value = "PLAN_NAME")
    private String planName;

    @Override
    protected Serializable pkVal() {
        return this.materialId;
    }


}
