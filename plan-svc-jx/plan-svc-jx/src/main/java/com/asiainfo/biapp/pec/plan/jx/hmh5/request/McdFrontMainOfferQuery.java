package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "江西客户通主套餐数据查询入参")
@Data
public class McdFrontMainOfferQuery {

    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;

    @ApiModelProperty(value = "主套餐编码或名称")
    private String offerIdOrName;

    @ApiModelProperty(value = "地市ID")
    private String cityId;

    @ApiModelProperty(value = "状态 1 上线, 0 下线")
    private String state;

    @ApiModelProperty(value = "开始日期")
    private String startDate;

    @ApiModelProperty(value = "结束日期")
    private String endDate;

}
