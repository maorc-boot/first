package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统计当前活动所选短信渠道，正在执行中的活动、涉及客群大小信息
 */
@Data
@ApiModel(value = "江西:查询活动执行信息入参", description = "江西:查询活动执行信息入参")
public class CampExcuteReq {
    @ApiModelProperty(value = "活动CAMPSEG_ROOT_ID")
    private String campsegRootId;

}
