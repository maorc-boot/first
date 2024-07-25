package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ranpf
 * @date 2023/2/28
 */
@Data
@ApiModel("渠道执行信息查询入参")
public class McdChannelExecQuery {


    @ApiModelProperty(value = "活动ID")
    private String campsegId;

    @ApiModelProperty(value = "渠道ID")
    private String channelId;

}
