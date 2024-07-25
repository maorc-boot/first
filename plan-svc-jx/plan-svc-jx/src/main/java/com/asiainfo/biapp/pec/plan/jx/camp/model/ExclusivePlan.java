package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 策略互斥产品
 *
 * @author liujw18
 * @Date 2022/10/18
 */
@Data
@TableName("mcd_camp_exclusive_plan")
@ApiModel(value = "策略互斥产品", description = "策略互斥产品")
public class ExclusivePlan {

    private String campsegId;

    private String planId;

    private String exPlanId;

    @ApiModelProperty("互斥产品名称")
    private String exPlanName;

    private String planGroupId;

}
