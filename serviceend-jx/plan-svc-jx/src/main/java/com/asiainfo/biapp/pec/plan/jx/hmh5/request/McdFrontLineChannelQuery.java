package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ranpf
 * @description:
 * @date 2023/2/18  18:21
 */
@Data
@ApiModel("客户通看护看管区域查询入参")
public class McdFrontLineChannelQuery {
    @ApiModelProperty("地市编码")
    private String cityCode;
    @ApiModelProperty("区县")
    private String countyCode;

}
