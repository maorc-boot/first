package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author mamp
 * @date 2022/10/18
 */
@Data
public class PlanExtInfo {
    @ApiModelProperty(value = "主产品ID")
    private String planId;
    @ApiModelProperty(value = "主产品名称")
    private String planName;

    @ApiModelProperty(value = "策略融合产品")
    private List<PlanBaseInfo> campFusionPlans;
    @ApiModelProperty(value = "策略同系列产品")
    private List<PlanBaseInfo> campSeriesPlan;
    @ApiModelProperty(value = "策略互斥产品")
    private List<PlanBaseInfo> campExclusivePlan;
}
