package com.asiainfo.biapp.pec.plan.jx.channelconfig.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("渠道文字提示查询入参")
public class McdChanPromptQuery {

    @ApiModelProperty("查询渠道文字提示及附件配置渠道id")
    private String channelId;

    @ApiModelProperty("查询附件名称关键字")
    private String keyWords;

    @ApiModelProperty("每页大小")
    private int pageSize = 10;
    @ApiModelProperty("当前页")
    private int  pageNum = 1;
}
