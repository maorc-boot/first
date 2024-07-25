package com.asiainfo.biapp.pec.preview.jx.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/10/28
 */
@Data
@ApiModel(value = "江西:执行中预演查询", description = "江西:执行中预演查询")
public class PreveiwResultReq {

    @ApiModelProperty(value = "根活动ID", required = true)
    private String campsegRootId;

    @ApiModelProperty(value = "子活动ID", required = false)
    private String campsegId;


    @ApiModelProperty(value = "开始时间", notes = "日期格式:yyyyMMdd")
    private String starDate;

    @ApiModelProperty(value = "结棍时间时间", notes = "日期格式:yyyyMMdd")
    private String endDate;
}
