package com.asiainfo.biapp.pec.element.jx.material.model;

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
 * 营销素材
 * </p>
 *
 * @author ranpf
 * @since 2023-1-3
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("iop_dim_material")
@ApiModel(value="江西IopDimMaterial素材对象", description="营销素材信息")
public class IopDimMaterialJxModel extends Model<IopDimMaterialJxModel> {

    private static final long serialVersionUID = -4359169942140596002L;

    @ApiModelProperty(value = "素材ID")
    @TableId("ID")
    private String id;

    @ApiModelProperty(value = "素材名称")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "素材类型 0：图片 1：文字 2：视频")
    @TableField("TYPE")
    private Integer type;

    @ApiModelProperty(value = "渠道ID")
    @TableField("CHANNEL_ID")
    private String channelId;

    @ApiModelProperty(value = "触点ID")
    @TableField("CONTACT_ID")
    private String contactId;

    @ApiModelProperty(value = "运营位ID")
    @TableField("POSITION_ID")
    private String positionId;

    @ApiModelProperty(value = "文字内容(推荐用语)")
    @TableField("CONTENT")
    private String content;

    @ApiModelProperty(value = "附件名称")
    @TableField("ATTACHMENT_NAME")
    private String attachmentName;

    @ApiModelProperty(value = "图片/视频大小（字节）")
    @TableField("MATERIAL_SIZE")
    private Float materialSize;

    @ApiModelProperty(value = "图片尺寸")
    @TableField("MATERIAL_PIC_SIZE")
    private String materialPicSize;

    @ApiModelProperty(value = "图片/视频存放地址")
    @TableField("RESOURCE_URL")
    private String resourceUrl;

    @ApiModelProperty(value = "图片/视频目标链接地址")
    @TableField("TARGET_URL")
    private String targetUrl;

    @ApiModelProperty(value = "状态 0：草稿 1：审批中 2：审批通过 3:失效")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "审批流ID")
    @TableField("APPROVE_FLOW_ID")
    private String approveFlowId;

    @ApiModelProperty(value = "公网访问地址")
    @TableField("VISIT_URL")
    private String visitUrl;

    @ApiModelProperty(value = "产品ID")
    @TableField("PLAN_ID")
    private String planId;

    @ApiModelProperty(value = "创建人账号")
    @TableField("CREATE_ID")
    private String createId;

    @ApiModelProperty(value = "失效时间")
    @TableField("INVALID_DATE")
    private String invalidDate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
