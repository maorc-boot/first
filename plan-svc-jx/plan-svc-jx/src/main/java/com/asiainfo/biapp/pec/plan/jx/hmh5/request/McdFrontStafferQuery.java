package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "江西客户通工号查询入参")
public class McdFrontStafferQuery {

    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;

    @ApiModelProperty(value = "搜索关键字")
    private String keyWords;
}
