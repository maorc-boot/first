package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "江西客户通黑名单删除入参")
public class McdFrontBlackListPhone {

    @ApiModelProperty(value = "手机号码")
    private String  productNo;
}
