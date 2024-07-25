package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 手动执行AI推理任务的接口入参对象
 *
 * @author: lvchaochao
 * @date: 2024/6/6
 */
@ApiModel("手动执行AI推理任务的接口入参对象")
@Data
public class HandleAITaskExecReqVO {

    @ApiModelProperty("推理任务id")
    private String id;

    // @ApiModelProperty("任务生效时间")
    // private String taskEffectTime;

    @ApiModelProperty("任务失效时间")
    private String taskFailTime;

    @ApiModelProperty("执行方式 0：手动执行 1：定时执行")
    private Integer executeType;

    @ApiModelProperty("选择执行周期类型  0：日  1：周  2：月")
    private Integer dateType;

    @ApiModelProperty("执行日期 针对定时执行 1-31 目前设置默认为每月1号")
    private String execWeek;

    // @ApiModelProperty("执行时间 时：分 目前不设置")
    // private String execTime;

}
