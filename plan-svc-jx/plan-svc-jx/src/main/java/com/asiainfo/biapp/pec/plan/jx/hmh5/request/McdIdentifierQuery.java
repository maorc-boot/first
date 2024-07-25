package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("通话明细查询")
public class McdIdentifierQuery {

    @ApiModelProperty("通话唯一标识")
    private String callQueryId;
}
