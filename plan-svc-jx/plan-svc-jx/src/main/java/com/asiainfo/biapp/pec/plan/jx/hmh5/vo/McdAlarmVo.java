package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("预警信息")
public class McdAlarmVo {

    @ApiModelProperty("预警编码")
    private String alarmId;

    @ApiModelProperty("预警名称")
    private String alarmName;
}
