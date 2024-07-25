package com.asiainfo.biapp.pec.plan.jx.channelconfig.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("渠道文字提示及附件下载入参")
public class McdDownloadChanPromptFileReq {

    @ApiModelProperty("下载文件全路径+名称")
    private String fileUrlAndName;


}
