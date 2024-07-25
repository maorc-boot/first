package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "江西:企业微信统一物料查询接口实体类", description = "企业微信统一物料查询接口实体类")
public class GroupHallsAllDataJxQuery {

    @ApiModelProperty(value = "当前页")
    private String pageNum;

    @ApiModelProperty(value = "关键词")
    private String keyWords;
}
