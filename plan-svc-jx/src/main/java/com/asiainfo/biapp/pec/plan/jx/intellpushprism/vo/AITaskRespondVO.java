package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 推理任务返回实体对象
 *
 * @author: lvchaochao
 * @date: 2024/6/5
 */
@Data
@ApiModel("推理任务返回实体对象")
public class AITaskRespondVO {

    @ApiModelProperty("推理任务返回前端产品实体对象")
    private AITaskRespondPlanVO planVO;

    @ApiModelProperty("推理任务返回前端客群实体对象")
    private AITaskRespondCustVO custVO;

    @ApiModelProperty("推理任务返回前端渠道实体对象")
    private AITaskRespondChannelVO channelVO;

}
