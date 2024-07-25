package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author : zhouyang
 * @date : 2021-12-07 20:44:00
 *
 */
@Data
@ApiModel(value = "修改优先级入参", description = "修改优先级入参")
public class CampPriorityOrderModJx {

    @NotEmpty(message = "子策略id不能为空")
    @ApiModelProperty(value = "子策略id")
    private String campsegId;

    @NotNull(message = "优先级不能为空")
    @ApiModelProperty(value = "策略优先级0-1，1最高")
    private double orderNum;

}
