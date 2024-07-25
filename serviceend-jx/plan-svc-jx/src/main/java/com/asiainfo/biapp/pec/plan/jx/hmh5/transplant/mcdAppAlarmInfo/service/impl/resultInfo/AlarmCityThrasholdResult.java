package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("自定义预警-预警阈值模型")
public class AlarmCityThrasholdResult {

    @ApiModelProperty(value = "预警地市编码")
    private String alarmCityCode;

    @ApiModelProperty(value = "预警地市名称")
    private String alarmCityName;

    @ApiModelProperty(value = "预警地市阈值")
    private String alarmThrashValue;
}
