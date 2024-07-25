package com.asiainfo.biapp.pec.plan.jx.smstemplates.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@ApiOperation("短信夹带模板信息")
@Data
@ApiModel(value = "短信夹带模板信息" ,description = "短信夹带模板短信用语编辑")
public class McdSmsTemplatesInfo  {

    @ApiModelProperty("模板ID")
    private String  templateId;
    @ApiModelProperty("模板名称")
    private String  templateName;
    @ApiModelProperty("模板内容")
    private String  templateContent;
    @ApiModelProperty("模板类型ID")
    private String  templateTypeId;
    @ApiModelProperty("状态 1已上线,0待上线")
    private int  status;
    @ApiModelProperty("短信夹带用语")
    private String  defaultMarkingContent;
    @ApiModelProperty("运营位名称")
    private String  adivName;
}
