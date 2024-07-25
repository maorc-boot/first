package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 * @description: 存放看护关系查询条件
 * @date 2023/2/18  18:21
 */
@Data
@ApiModel(value = "江西客户通看护关系对象", description = "江西客户通看护关系对象")
public class McdFrontGuardUserInfo   {

    @ApiModelProperty("手机号码")
    private String productNo;
    @ApiModelProperty("自有经理看管编号(看护工号)")
    private String managerId;
    @ApiModelProperty("看护渠道Id")
    private String channelId;
    @ApiModelProperty("看护渠道名称")
    private String channelName;
    @ApiModelProperty("地市编码")
    private String cityName;
    @ApiModelProperty("区县")
    private String countyName;
    @ApiModelProperty("是否重点客户")
    private String vipCustomerFlag;
    @ApiModelProperty("看护类型")
    private String guardType;

}
