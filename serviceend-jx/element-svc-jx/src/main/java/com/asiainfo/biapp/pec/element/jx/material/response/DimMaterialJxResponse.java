package com.asiainfo.biapp.pec.element.jx.material.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

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
@ApiModel(value = "江西响应素材信息列表")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DimMaterialJxResponse {


    @ApiModelProperty(value = "素材ID",required = true, dataType = "string")
    private String materialId;

    @ApiModelProperty(value = "素材名称",required = true, dataType = "string")
    private String materialName;

    @ApiModelProperty(value = "素材类型0：图片1：文字2：视频", required = true)
    private Integer materialType;

    @ApiModelProperty(value = "渠道ID")
    private String channelId;

    @ApiModelProperty(value = "渠道名称",required = true, dataType = "string")
    private  String channelName;

    @ApiModelProperty(value = "触点ID")
    private String contactId;

    @ApiModelProperty(value = "触点名称",required = true, dataType = "string")
    private String contactName;

    @ApiModelProperty(value = "运营位ID")
    private String positionId;

    @ApiModelProperty(value = "运营位名称",required = true, dataType = "string")
    private String positionName;

    @ApiModelProperty(value = "文字内容",required = true, dataType = "string")
    private String content;

    @ApiModelProperty(value = "附件名称")
    private String attachmentName;

    @ApiModelProperty(value = "图片/视频大小")
    private Float materialSize;

    @ApiModelProperty(value = "图片/视频存放地址")
    private String resourceUrl;

    @ApiModelProperty(value = "图片/视频目标链接地址",required = true, dataType = "string")
    private String targetUrl;

    @ApiModelProperty(value = "状态0：草稿1：审批中2：审批通过,3 失效,6直接使用")
    private Integer materialStatus;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "审批流ID")
    private String approveFlowId;

    @ApiModelProperty(value = "运营位尺寸",required = true,dataType = "string")
    private String materialPicSize;

    @ApiModelProperty(value = "访问地址",required = true,dataType = "string")
    private String visitUrl;

    @ApiModelProperty("引用次数")
    private Integer useTimes;

    @ApiModelProperty("优秀模板(0:普通素材、1:优秀素材)")
    private Integer excellence;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("创建者")
    private String creator;

    @ApiModelProperty("上下线状态")
    private Integer onlineStatus;

    @ApiModelProperty("运营位素材尺寸类型 0：尺寸1， 1：尺寸2")
    private String adivSizeType;

    @ApiModelProperty(value = "产品ID")
    private String planId;

    @ApiModelProperty(value = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invalidDate;

    @ApiModelProperty(value = "产品名称")
    private String planName;
}
