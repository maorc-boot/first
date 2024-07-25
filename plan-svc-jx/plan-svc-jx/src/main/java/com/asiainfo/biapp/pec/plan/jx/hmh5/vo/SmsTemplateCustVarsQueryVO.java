package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 短信模板-获取营销用语变量替换信息入参对象
 *
 * @author: lvchaochao
 * @date: 2024/5/28
 */
@ApiModel("短信模板-获取营销用语变量替换信息入参对象")
@Data
public class SmsTemplateCustVarsQueryVO {

    @ApiModelProperty("消息类型 普通短信-hmh5 代维短信-daiwei")
    private String smsType;

    @ApiModelProperty("模板类型 作战地图、生日祝福、佳节祝福、友情提醒、紧急通知等")
    private String templateType;
}
