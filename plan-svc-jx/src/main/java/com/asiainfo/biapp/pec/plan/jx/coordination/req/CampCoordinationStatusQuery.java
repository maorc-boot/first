package com.asiainfo.biapp.pec.plan.jx.coordination.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author : ranpf
 * @date : 2023-6-16 09:14:44
 * 修改统筹任务状态实体
 */
@ApiModel(value = "修改统筹任务状态实体", description = "修改统筹任务状态实体")
@Data
public class CampCoordinationStatusQuery   {

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
