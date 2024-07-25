package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/10/18
 */
@Data
public class PlanBaseInfo {
    @ApiModelProperty(value = "产品ID")
    private String planId;
    @ApiModelProperty(value = "产品名称")
    private String planName;
}
