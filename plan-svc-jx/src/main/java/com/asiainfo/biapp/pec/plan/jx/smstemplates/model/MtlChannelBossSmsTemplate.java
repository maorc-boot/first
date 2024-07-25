package com.asiainfo.biapp.pec.plan.jx.smstemplates.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;


@ApiOperation("短信夹带模板入参信息")
@Data
@TableName(value = "mtl_channel_boss_sms_template")
@ApiModel(value = "短信夹带模板入参" ,description = "短信夹带模板短信用语编辑信息")
public class MtlChannelBossSmsTemplate {

    @ApiModelProperty("模板ID")
    @TableId("TEMPLATE_ID")
    private String  templateId;
    @ApiModelProperty("模板名称")
    private String  templateName;
    @ApiModelProperty("模板内容")
    @TableField("TEMPLATE_CONTENT")
    private String  templateContent;
    @ApiModelProperty("模板类型ID")
    private String  templateTypeId;
    @ApiModelProperty("状态 1已上线,0待上线")
    private int  status;
    @ApiModelProperty("短信夹带用语")
    private String  defaultMarkingContent;

}
