package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "江西客户通操作主套餐入参")
@Data
public class McdFrontOfferOperationQuery {

    @ApiModelProperty(value = "主套餐编码")
    private String offerId;

    @ApiModelProperty(value = "状态 1上线,0 下线")
    private String state;


}
