package com.asiainfo.biapp.pec.plan.jx.camp.req;


import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "客户群列表查询实体", description = "客户群列表查询实体")
@Data
public class CustomJxActionQuery extends McdPageQuery {

    @ApiModelProperty(value = "客户群类型", required = true)
    private Integer[] custType = {0};

    @ApiModelProperty(value = "用户id", hidden = true)
    private String userId;

    @ApiModelProperty(value = "是否查询不限定客群 0-不是 1-是")
    private Integer isQryNotLimitCust;

    @ApiModelProperty(value = "智推棱镜：智能营销流程 1-是")
    private Integer intellRec;
}
