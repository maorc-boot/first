package com.asiainfo.biapp.pec.plan.jx.smstemplates.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("短信夹带策划模板查询入参")
public class McdSmsTemplatesRequest   {

    @ApiModelProperty(value = "渠道ID")
    private String channelId;

    @ApiModelProperty(value = "运营位ID")
    private String adviId;

}
