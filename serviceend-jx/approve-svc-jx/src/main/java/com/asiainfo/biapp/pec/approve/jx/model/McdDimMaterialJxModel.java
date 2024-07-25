package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
@TableName("mcd_dim_material")
@ApiModel(value="江西McdDimMaterial对象", description="营销素材")
public class McdDimMaterialJxModel extends Model<McdDimMaterialJxModel> {
    
    private static final long serialVersionUID = -4359169942140596002L;

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

    @ApiModelProperty(value = "图片/视频存放地址")
    @TableField("RESOURCE_URL")
    private String resourceUrl;

    @ApiModelProperty(value = "图片/视频目标链接地址")
    @TableField("TARGET_URL")
    private String targetUrl;

    @ApiModelProperty(value = "状态0：草稿1：审批中2：审批通过 3：驳回 6：可直接使用")
    @TableField("MATERIAL_STATUS")
    private Integer materialStatus;

    @ApiModelProperty(value = "描述")
    @TableField("DESCRIPTION")
    private String description;
    
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "审批流ID")
    @TableField("APPROVE_FLOW_ID")
    private String approveFlowId;
    
    @ApiModelProperty(value = "图片尺寸")
    @TableField("MATERIAL_PIC_SIZE")
    private String materialPicSize;

    @ApiModelProperty(value = "访问地址")
    @TableField("VISIT_URL")
    private String visitUrl;
    
    @ApiModelProperty("引用次数")
    @TableField("USE_TIMES")
    private Integer useTimes;
    
    @ApiModelProperty("优秀模板(0:普通素材、1:优秀素材)")
    @TableField("EXCELLENCE")
    private Integer excellence;
    
    @ApiModelProperty("修改时间")
    @TableField(value = "UPDATE_TIME")
    private Date updateTime;
    
    @ApiModelProperty("创建者")
    @TableField(value = "CREATOR", fill = FieldFill.INSERT)
    private String creator;
    
    @ApiModelProperty("上下线状态 0: 下线 1: 上线")
    private Integer onlineStatus;

    @ApiModelProperty("运营位素材尺寸类型 0：尺寸1， 1：尺寸2")
    @TableField("adiv_size_type")
    private String adivSizeType;

    @ApiModelProperty(value = "产品ID")
    @TableField("PLAN_ID")
    private String planId;

    @ApiModelProperty(value = "失效时间")
    @TableField("INVALID_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date invalidDate;

    @ApiModelProperty(value = "产品名称")
    @TableField("PLAN_NAME")
    private String planName;

 /*   @ApiModelProperty(value = "产品类别")
    @TableField("PLAN_TYPE")
    private String planType;


    @ApiModelProperty(value = "产品类别名称")
    @TableField("PLAN_TYPE_NAME")
    private String planTypeName;*/


    @Override
    protected Serializable pkVal() {
        return this.materialId;
    }

}
