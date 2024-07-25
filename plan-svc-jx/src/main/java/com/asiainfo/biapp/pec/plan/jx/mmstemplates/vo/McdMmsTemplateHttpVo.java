package com.asiainfo.biapp.pec.plan.jx.mmstemplates.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("彩信模板")
public class McdMmsTemplateHttpVo {

    @ApiModelProperty("模板编号,表数据,新增时和mmsTemplateNum是一样的")
    private String id;

    @ApiModelProperty("模板编号,修改时用")
    private String mmsTemplateNum;

    @ApiModelProperty("模板主题")
    private String mmsTemplateSubject;

    @ApiModelProperty("彩信地址")
    private String mmsAddress;

    @ApiModelProperty("添加传 add 修改不传")
    private String flag;

    @ApiModelProperty("每页大小")
    private String pageSize;

    @ApiModelProperty("当前页")
    private String pageNum;

    @ApiModelProperty("查询关键字")
    private String keywords;
}
