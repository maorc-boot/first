package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description: 推理任务返回前端实体对象
 *
 * @author: lvchaochao
 * @date: 2024/6/5
 */
@Data
@ApiModel("推理任务返回前端实体对象")
public class AITaskRespondFinalVO {

    @ApiModelProperty("AI推理任务返回的id")
    private String aITaskId;

    @ApiModelProperty("AI推理任务返回的产品、客群、渠道集合信息")
    private List<AITaskRespondVO> respondVOList;
}
