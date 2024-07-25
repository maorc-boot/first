package com.asiainfo.biapp.pec.plan.jx.coordination.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("标签配置列表查询入参对象")
public class TagConfigReq extends McdPageQuery {

    @ApiModelProperty(value = "客户类型Id")
    private  long custTypeId;

}
