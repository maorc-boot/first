package com.asiainfo.biapp.pec.element.jx.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 *
 * </p>
 *
 * @author ranpf
 * @since 2022-12-15
 */
@Data
@ApiModel(value = "江西McdPlanDefInfo对象", description = "江西McdPlanDefInfo对象,互斥新建用")
public class McdPlanDefInfo extends McdPlanDef {

    @ApiModelProperty(value = "是否匹配, 是 或 否")
    private String isUsed;

    @ApiModelProperty(value = "是否作为主产品标志, 1 是  , 0否  ")
    private String isFlag;


}
