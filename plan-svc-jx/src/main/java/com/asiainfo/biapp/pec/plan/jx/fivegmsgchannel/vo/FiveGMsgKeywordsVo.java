package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 5G消息关键词实体对象
 *
 * @author lvcc
 * @date 2023/02/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("FIVEG_MSG_KEYWORDS")
@ApiModel(value="5G消息关键词实体对象", description="江西：5g消息关键词配置表")
public class FiveGMsgKeywordsVo implements Serializable {

    private static final long serialVersionUID =  1L;

    @ApiModelProperty(value = "关键词id")
    @TableId(value = "KEYWORDS_ID", type = IdType.ASSIGN_UUID)
    private String keywordsId;

    @ApiModelProperty("关键词name")
    @TableField("KEYWORDS_NAME")
    private String keywordsName;

    @ApiModelProperty("模板id")
    @TableField("TEMPLATE_ID")
    private String templateId;

    @ApiModelProperty("模板name")
    @TableField("TEMPLATE_NAME")
    private String templateName;

    @ApiModelProperty("模板类型")
    @TableField("TEMPLATE_TYPE")
    private String templateType;

    @ApiModelProperty("创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("应用号")
    @TableField("APPLICATION_NUM")
    private String applicationNum;

    @ApiModelProperty("回落类型")
    @TableField("FALLBACK_TYPE")
    private String fallBackType;

}
