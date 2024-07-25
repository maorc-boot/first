package com.asiainfo.biapp.pec.plan.jx.smstemplates.vo;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("短信夹带相关接口入参")
public class McdSmsTemplatesQuery extends McdPageQuery {

    @ApiModelProperty(value = "模板编码")
    private String templateId;

    @ApiModelProperty(value = "模板编码集合")
    private List<String> templateIds;

    @ApiModelProperty(value = "模板下线")
    private int status;

}
