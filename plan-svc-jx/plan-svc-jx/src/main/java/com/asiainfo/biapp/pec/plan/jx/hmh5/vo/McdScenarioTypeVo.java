package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("业务场景类型信息")
public class McdScenarioTypeVo {

    @ApiModelProperty("模块类型")
    private String moduleType;

    @ApiModelProperty("模块名称")
    private String moduleName;

    @ApiModelProperty("业务场景ID")
    private String businessScenarioId;

    @ApiModelProperty("业务场景名称")
    private String businessScenarioName;
}
