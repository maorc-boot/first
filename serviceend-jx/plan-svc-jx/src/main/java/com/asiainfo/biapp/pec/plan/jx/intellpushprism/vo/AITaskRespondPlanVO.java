package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description: 推理任务返回前端产品实体对象
 *
 * @author: lvchaochao
 * @date: 2024/6/5
 */
@Data
@ApiModel("推理任务返回前端产品实体对象")
public class AITaskRespondPlanVO {

    @ApiModelProperty("产品id")
    private String planId;

    @ApiModelProperty(value = "产品名称")
    private String planName;

    @ApiModelProperty(value = "产品生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date planStartDate;

    @ApiModelProperty(value = "产品失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date planEndDate;

    @ApiModelProperty(value = "产品描述")
    private String planDesc;
}
