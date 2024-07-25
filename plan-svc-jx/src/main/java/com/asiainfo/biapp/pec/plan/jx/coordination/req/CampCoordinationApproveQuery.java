package com.asiainfo.biapp.pec.plan.jx.coordination.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * @author : ranpf
 * @date : 2023-6-14 09:14:44
 * 查询策略统筹审批任务实体
 */
@ApiModel(value = "查询策略统筹审批任务实体", description = "查询策略统筹审批任务实体")
@Data
public class CampCoordinationApproveQuery {

    @ApiModelProperty(value = "创建人")
    private String createUserIdOrName;

    @ApiModelProperty(value = "任务id或名称")
    private String taskIdOrName;

    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

}
