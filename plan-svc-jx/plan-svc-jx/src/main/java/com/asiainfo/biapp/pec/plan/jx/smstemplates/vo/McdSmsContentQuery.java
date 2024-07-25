package com.asiainfo.biapp.pec.plan.jx.smstemplates.vo;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("推荐用语编辑模板相关接口入参")
public class McdSmsContentQuery extends McdPageQuery {

    @ApiModelProperty(value = "模板编码")
    private String id;

    @ApiModelProperty(value = "模板编码集合")
    private List<String> ids;
}
