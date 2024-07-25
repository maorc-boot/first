package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : ranpf
 * @date : Created in 2023-1-9
 */

@Data
@ApiModel(value = "江西策略审批条件查询接口入参",description = "策略审批条件查询接口入参")
public class McdCampApproveJxQuery {


    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "业务对象名称")
    private String campsegName;

    @ApiModelProperty(value = "渠道")
    private String channelId;

    @ApiModelProperty(value = "创建人")
    private String creator;


}
