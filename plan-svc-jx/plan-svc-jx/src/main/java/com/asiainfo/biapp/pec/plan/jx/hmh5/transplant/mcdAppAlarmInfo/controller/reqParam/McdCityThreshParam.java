package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.AlarmCityThrasholdResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel("预警阈值入参")
public class McdCityThreshParam {

    /**
     * 预警id
     */
    @ApiModelProperty(value = "预警ID",example = "预警ID")
    @NotBlank(message = "预警ID不可为空！")
    private Integer alarmId;

    @ApiModelProperty(value = "预警登录人ID",example = "登录人ID")
    private String userId;

    @ApiModelProperty(value = "预警登录人地市ID",example = "地市ID")
    private String cityId;

    /**
     * 预警阈值信息
     */
    @ApiModelProperty(value = "预警阈值原始值信息",example = "预警阈值原始值")
    private List<AlarmCityThrasholdResult> oldAlarmThreshold;

    /**
     * 预警阈值信息
     */
    @ApiModelProperty(value = "预警阈值新值信息",example = "预警阈值新值")
    private List<AlarmCityThrasholdResult> newAlarmThreshold;

}
