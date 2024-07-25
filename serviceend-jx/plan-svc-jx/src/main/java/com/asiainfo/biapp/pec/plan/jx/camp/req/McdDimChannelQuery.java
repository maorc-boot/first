package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ranpf
 * @date 2022/12/13
 */
@Data
@ApiModel("江西:线上线下活动优先级查询")
public class McdDimChannelQuery {

    @ApiModelProperty("渠道类型: 1 线上, 0 线下" )
    private String channelType;


}
