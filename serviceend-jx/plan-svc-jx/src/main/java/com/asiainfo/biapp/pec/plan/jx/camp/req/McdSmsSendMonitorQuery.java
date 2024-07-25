package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("短信发送监控查询入参")
public class McdSmsSendMonitorQuery {

    @ApiModelProperty("渠道ID 内容短信 801, 族群短信 901")
    private String channelId;

    @ApiModelProperty("每页大小")
    private int pageSize = 10;
    @ApiModelProperty("当前页")
    private int  pageNum = 1;
}
