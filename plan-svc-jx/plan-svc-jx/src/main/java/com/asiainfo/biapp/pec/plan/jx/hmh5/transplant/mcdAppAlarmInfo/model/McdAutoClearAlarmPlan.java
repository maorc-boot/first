package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 自动解警产品对象
 *
 * @author: lvchaochao
 * @date: 2023/12/4
 */
@Data
@Accessors(chain = true)
public class McdAutoClearAlarmPlan {

    @ApiModelProperty("产品ID，即押金代码")
    private String planId;

    @ApiModelProperty("产品名称，即押金名称 ")
    private String planName;

    @ApiModelProperty("押金产品类型名称")
    private String planTypeName;

    @ApiModelProperty("创建人用户ID")
    private String createUser;

    @ApiModelProperty("创建人归属地址ID")
    private String createCity;
}
