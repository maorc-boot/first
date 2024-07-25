package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ranpf
 * @date 2023/5/18 20:43
 */
@Data
@ApiModel(value = "江西优秀策略分页查询入参")
public class McdExcellentCampPageQuery {

    @ApiModelProperty(value = "每页大小，默认值5")
    private Integer size = 5;

    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;

}
