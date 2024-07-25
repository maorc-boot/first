package com.asiainfo.biapp.pec.element.jx.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author : zhouyang
 * @date : 2021-11-25 11:24:46
 */
@Data
@ApiModel(value = "产品id实体", description = "产品id实体")
public class PlanIdQuery {

    @NotEmpty(message = "产品id不能为空")
    @ApiModelProperty(value = "产品id", required = true)
    private String planId;

}
