package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("短信发送操作入参")
public class McdSmsSendSuspendQuery {

    @ApiModelProperty("渠道ID 内容短信 801, 族群短信 901")
    private String channelId;

    @ApiModelProperty("策略ID")
    private String campsegId;

    @ApiModelProperty("控制状态 0 运行, 1 暂停")
    private int ctrlStatus;
}
