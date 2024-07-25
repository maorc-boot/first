package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 江西客户通公告栏表
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Data
@ApiModel("修改公告的请求参数模型")
public class ModifyBulletinParam implements Serializable {


    @ApiModelProperty(value = "要修改的公告bulletinCode")
    @NotBlank(message = "公告bulletinCode不能为空！")
    private String bulletinCode;
    /**
     * 公告标题，最多100字符
     */
    @ApiModelProperty(value = "公告标题，最多100字符")
    @Length(max = 100,message = "标题过长！")
    @NotBlank(message = "标题不能为空！")
    private String bulletinTitle;

    /**
     * 文字内容，最多2500字符
     */
    @ApiModelProperty(value = "公告内容，最长2500字符")
    @Length(max = 2500,message = "公告内容过长！")
    @NotBlank(message = "公告内容不能为空！")
    private String bulletinContent;

    /**
     * 图片内容，文件名长度累计不超过256字符
     */
    @ApiModelProperty(value = "公告的附加图片名称(以\",\"分割)，可为空，文件名长度累计不超过256字符")
    @Length(max = 256,message = "图片文件名太长！")
    private String bulletinImage;

    /**
     * 展示规则，最多32字符
     */
    @ApiModelProperty(value = "公告展示规则，最多32字符")
    @Length(max = 32,message = "展示规则字符过长！")
    @NotBlank(message = "展示规则不能为空！")
    private String bulletinRule;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "公告展示的开始时间")
    @NotNull(message = "开始时间不能为空！")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "公告展示的结束时间")
    @NotNull(message = "结束时间不能为空！")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
