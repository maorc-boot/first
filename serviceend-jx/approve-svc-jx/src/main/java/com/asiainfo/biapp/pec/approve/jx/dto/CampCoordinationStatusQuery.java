package com.asiainfo.biapp.pec.approve.jx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author : ranpf
 * @date : 2023-6-12 09:14:44
 * 修改策略状态实体
 */
@ApiModel(value = "修改任务状态实体", description = "修改任务状态实体")
@Data
public class CampCoordinationStatusQuery {

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态")
    private int execStatus;

    @ApiModelProperty(value = "流程id")
    private String flowId;

    @NotNull(message = "任务id不能为空")
    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "子任务id 多个逗号隔开")
    private String childTaskIds;
}
