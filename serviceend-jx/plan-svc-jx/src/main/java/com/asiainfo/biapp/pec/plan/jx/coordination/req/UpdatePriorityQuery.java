package com.asiainfo.biapp.pec.plan.jx.coordination.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 产品配置修改优先级入参对象
 *
 * @author: lvchaochao
 * @date: 2023/7/24
 */
@Data
@ApiModel("产品配置修改优先级入参对象")
public class UpdatePriorityQuery {

    @ApiModelProperty("产品编码")
    private String planId;

    @ApiModelProperty("优先级")
    private Integer priority;
}
