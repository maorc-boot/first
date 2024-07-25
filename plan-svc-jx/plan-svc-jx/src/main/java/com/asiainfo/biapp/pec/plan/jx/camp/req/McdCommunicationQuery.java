package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "江西转至沟通人查询入参")
public class McdCommunicationQuery {

    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;

    @ApiModelProperty(value = "搜索关键字")
    private String keyWords;
}
