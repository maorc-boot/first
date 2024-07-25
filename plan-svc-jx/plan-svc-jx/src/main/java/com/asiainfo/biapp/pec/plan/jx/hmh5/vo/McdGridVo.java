package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("网格信息")
public class McdGridVo {

    @ApiModelProperty("网格编码")
    private String gridId;

    @ApiModelProperty("网格名称")
    private String gridName;
}
