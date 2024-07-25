package com.asiainfo.biapp.pec.element.jx.material.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * Description :
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-3
 * //@modified By :
 * //@since :
 */

@Data
@ApiModel(value = "江西新建或修改素材接口入参")
public class McdDimMaterialNewQuery {
    
    @ApiModelProperty(value = "素材ID")
    private String materialId;
    
    @ApiModelProperty(value = "素材名称")
    private String materialName;
    
    @ApiModelProperty(value = "素材类型0：图片1：文字2：视频")
    private Integer materialType;
    
    @ApiModelProperty(value = "渠道ID")
    private String channelId;
    
    @ApiModelProperty(value = "触点ID")
    private String contactId;
    
    @ApiModelProperty(value = "运营位ID")
    private String positionId;
    
    @ApiModelProperty(value = "文字内容(营销话术)")
    private String content;
    
    @ApiModelProperty(value = "附件名称")
    private String resourceName;
    
    @ApiModelProperty(value = "图片/视频大小")
    private String resourceSize;
    
    @ApiModelProperty(value = "图片/视频存放地址")
    private String resourceUrl;
    
    @ApiModelProperty(value = "图片/视频目标链接地址")
    private String targetUrl;
    
    @ApiModelProperty(value = "状态0：草稿1：审批中2：审批通过, 3 失效 , 6直接使用(并不用审批)")
    private Integer materialStatus;
    
    @ApiModelProperty(value = "描述")
    private String description;
    
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    
    @ApiModelProperty(value = "审批流ID")
    private String approveFlowId;
    
    @ApiModelProperty(value = "图片尺寸")
    private String materialPicSize;
    
    @ApiModelProperty(value = "访问地址")
    private String visitUrl;
    
    @ApiModelProperty(value = "引用次数")
    private Integer useTimes;
    
    @ApiModelProperty(value = "优秀模板(0:普通素材、1:优秀素材)")
    private Integer excellence;
    
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    
    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "上下线状态")
    private Integer onlineStatus;

    @ApiModelProperty(value = "图片位置类型0默认位置，1是adiv_size2")
    private String adivSizeType;

    @ApiModelProperty(value = "失效时间")
    @TableField(value = "INVALID_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date invalidDate;

    @ApiModelProperty(value = "产品ID")
    private String planId;

    @ApiModelProperty(value = "产品名称")
    private String planName;

}
