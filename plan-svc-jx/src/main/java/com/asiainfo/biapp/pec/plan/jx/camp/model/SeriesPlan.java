package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 策略同系列产品
 * @author liujw18
 * @date 2022/10/18
 */
@Data
@TableName("mcd_camp_series_plan")
@ApiModel(value = "策略同系列产品", description = "策略同系列产品")
public class SeriesPlan {
    /**
     * id
     */
    private String campsegId;

    private String planId;

    private String seriesPlanId;
    @ApiModelProperty("同系列产品名称")
    private String seriesPlanName;



}
