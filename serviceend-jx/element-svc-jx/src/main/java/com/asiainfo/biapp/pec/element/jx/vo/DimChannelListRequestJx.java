package com.asiainfo.biapp.pec.element.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 江西：渠道列表查询入参
 *
 * @author lvcc
 * @date 2024/06/06
 */
@Data
@ApiModel(value = "江西：渠道列表查询入参",description = "江西：渠道列表查询入参")
public class DimChannelListRequestJx {
    
    @ApiModelProperty(value = "ONLINE_STATUS 上线状态 状态1：上线 0：未上线")
    private Integer onlineStatus;

    @ApiModelProperty("渠道分类")
    private Integer type;

    @ApiModelProperty("渠道id")
    private String channelId;

    @ApiModelProperty("是否支持AI 0-否 1-是")
    private Integer isSupportAI;
}
