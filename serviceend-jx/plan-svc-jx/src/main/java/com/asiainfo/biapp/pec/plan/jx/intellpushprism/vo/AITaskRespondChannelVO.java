package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 推理任务返回前端渠道实体对象
 *
 * @author: lvchaochao
 * @date: 2024/6/5
 */
@Data
@ApiModel("推理任务返回前端渠道实体对象")
public class AITaskRespondChannelVO {

    @ApiModelProperty("渠道id")
    private String channelId;

    @ApiModelProperty("渠道名称")
    private String channelName;

    @ApiModelProperty("渠道配置信息")
    private PrismChannelConfigVO channelConfig;
}
