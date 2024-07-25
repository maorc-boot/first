package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description: 推理任务接口响应实体对象
 *
 * @author: lvchaochao
 * @date: 2024/5/31
 */
@NoArgsConstructor
@ApiModel("推理任务接口响应实体对象")
@Data
public class AISaveTaskResVO implements Serializable {

    @ApiModelProperty("产品id")
    private String planId;

    @ApiModelProperty("客群id")
    private String custId;

    @ApiModelProperty("客群名称")
    private String custName;

    @ApiModelProperty("客群数量")
    private String custNum;

    @ApiModelProperty("渠道id")
    private String channelId;

    @ApiModelProperty("渠道名称")
    private String channelName;

}
