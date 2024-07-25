package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("短信发送过滤查询入参")
public class McdSmsSendInfoQuery {

    @ApiModelProperty("渠道ID 内容短信 801, 族群短信 901")
    private String channelId;

    @ApiModelProperty("策略ID")
    private String campsegId;

}
