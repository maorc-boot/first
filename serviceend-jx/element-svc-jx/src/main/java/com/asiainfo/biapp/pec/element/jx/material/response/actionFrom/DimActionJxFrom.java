package com.asiainfo.biapp.pec.element.jx.material.response.actionFrom;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Description :  素材管理excel导出po
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-4
 * //@modified By :
 * //@since :
 */


@Data
public class DimActionJxFrom implements Serializable {
    
    private static final long serialVersionUID = 222122L;
    
    @ApiModelProperty(value = "素材ID")
    private String materialId;
    @ApiModelProperty(value = "素材名称")
    private String materialName;

    @ApiModelProperty(value = "产品ID")
    private String planId;
    @ApiModelProperty(value = "产品名称")
    private String planName;

    @ApiModelProperty(value = "渠道ID")
    private String channelId;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "运营位ID")
    private String positionId;
    @ApiModelProperty(value = "运营位名称")
    private String positionName;

    @ApiModelProperty(value = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invalidDate;

    @ApiModelProperty(value = "素材类型0：图片1：文字2：视频")
    private Integer materialType;
    //素材类型名称
    @ApiModelProperty(value = "素材类型0：图片1：文字2：视频")
    private String materialTypeName;
    
    @ApiModelProperty(value = "文字内容")
    private String content;
    
    @ApiModelProperty(value = "图片/视频目标链接地址")
    private String targetUrl;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "状态0：草稿1：审批中2：审批通过")
    private Integer materialStatus;

    @ApiModelProperty(value = "创建者")
    private String creator;


}
