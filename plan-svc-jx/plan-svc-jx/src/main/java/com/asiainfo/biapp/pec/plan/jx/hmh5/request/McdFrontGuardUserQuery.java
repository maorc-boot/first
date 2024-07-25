package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 * @description: 存放看护关系查询条件
 * @date 2023/2/18  18:21
 */
@Data
@ApiModel("客户通看护看管查询入参")
public class McdFrontGuardUserQuery  {

    @ApiModelProperty("手机号码")
    private String productNo;
    @ApiModelProperty("看护工号")
    private String managerId;
    @ApiModelProperty("看护渠道Id")
    private String channelId;

    @ApiModelProperty("地市编码")
    private String cityCode;
    @ApiModelProperty("区县")
    private String countyCode;
    @ApiModelProperty("是否重点客户 1是,2否")
    private String vipCustomerFlag;
    @ApiModelProperty("看护类型 0 渠道共享, 1 工号")
    private String guardType;

    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;

}
