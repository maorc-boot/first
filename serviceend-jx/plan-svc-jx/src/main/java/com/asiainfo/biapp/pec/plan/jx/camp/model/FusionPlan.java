package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 策略融合产品
 *
 * @author liujw18
 * @date 2022/10/18
 */
@Data
@TableName("mcd_camp_fusion_plan")
@ApiModel(value = "策略融合产品", description = "策略融合产品")
public class FusionPlan {

    //策略Id
    private String campsegId;
    //主产品Id
    private String planId;
    //融合产品Id
    private String fusionPlanId;

    @ApiModelProperty("融合产品名称")
    private String fusionPlanName;
}
