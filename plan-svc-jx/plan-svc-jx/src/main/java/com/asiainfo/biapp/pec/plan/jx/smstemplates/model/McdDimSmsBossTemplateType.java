package com.asiainfo.biapp.pec.plan.jx.smstemplates.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

@ApiOperation("短信夹带渠道策划短信模板信息")
@Data
@TableName("mcd_dim_smsboss_template_type")
@ApiModel(value = "短信夹带渠道策划短信模板信息" ,description = "短信夹带渠道策划短信模板信息")
public class McdDimSmsBossTemplateType {

    @ApiModelProperty("类型ID")
    private String  typeId;
    @ApiModelProperty("类型名称")
    private String  typeName;
    @ApiModelProperty("排序号")
    private String  sortNum;
    @ApiModelProperty("模板内容")
    private List<McdSmsTemplatesInfo> templateList;
}
