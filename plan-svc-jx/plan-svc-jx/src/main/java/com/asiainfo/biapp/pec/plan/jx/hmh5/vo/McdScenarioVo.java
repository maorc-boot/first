package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("业务场景信息")
public class McdScenarioVo {


    @ApiModelProperty("业务场景ID")
    private String scenarioId;

    @ApiModelProperty("业务场景名称")
    private String scenarioName;
}
