package com.asiainfo.biapp.pec.approve.jx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/12/6
 */
@Data
@ApiModel(value = "预警待办查询")
public class WarningReq {
    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;
    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "策略名称或者编码")
    private String campsegKey;
}

