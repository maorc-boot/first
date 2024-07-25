package com.asiainfo.biapp.pec.plan.jx.smstemplates.vo;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("营销用语模板查询接口入参")
public class McdSmsContentTemplateQuery extends McdPageQuery {

    @ApiModelProperty(value = "渠道ID")
    private String channelId;

    @ApiModelProperty(value = "产品编码")
    private String planId;


}
