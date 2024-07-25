package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "江西客户通个性化称谓删除入参")
public class McdCustomTotlePhone {

    @ApiModelProperty(value = "手机号码")
    private String  phoneNum;
}
