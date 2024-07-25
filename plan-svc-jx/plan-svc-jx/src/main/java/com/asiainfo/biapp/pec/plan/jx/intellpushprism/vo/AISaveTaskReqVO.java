package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: AI任务推理保存请求入参对象
 *
 * @author: lvchaochao
 * @date: 2024/5/31
 */
@Data
@ApiModel("AI任务推理保存请求入参对象")
public class AISaveTaskReqVO {

    @ApiModelProperty("AI执行推理任务入参对象")
    private InferenceResByAIReqVO resByAIReqVO;

    @ApiModelProperty("是否手动完善 0：否 1：是 ")
    private Integer isManualPerfection;
}
