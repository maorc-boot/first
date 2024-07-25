package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 推理任务返回前端客群实体对象
 *
 * @author: lvchaochao
 * @date: 2024/6/5
 */
@Data
@ApiModel("推理任务返回前端客群实体对象")
public class AITaskRespondCustVO {


    @ApiModelProperty("客群id")
    private String custId;

    @ApiModelProperty("客群名称")
    private String custName;

    @ApiModelProperty("客群数量")
    private String custNum;
}
