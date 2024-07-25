package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ranpf
 * @date 2023/2/28
 */
@Data
@ApiModel("渠道曝光数据查询入参")
public class McdLogChnExpQuery {


    @ApiModelProperty(value = "日志日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private String logTime;

    @ApiModelProperty(value = "渠道ID")
    private String channelId;

}
