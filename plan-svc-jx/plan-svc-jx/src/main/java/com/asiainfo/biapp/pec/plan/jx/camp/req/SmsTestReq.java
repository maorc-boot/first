package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author mamp
 * @date 2022/12/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "江西:测试短信请求参数", description = "江西:测试短信请求参数")
@ToString
public class SmsTestReq {
    @ApiModelProperty("测试号码多个用英文逗号分隔")
    private String productNo;
    @ApiModelProperty("短信内容")
    private String content;
    @ApiModelProperty("执行端口")
    private String spCode;


}
