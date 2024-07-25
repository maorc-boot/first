package com.asiainfo.biapp.pec.approve.jx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : ranpf
 * @date : Created in 2023-5-22
 */

@Data
@ApiModel(value = "江西H5策略审批查询接口入参",description = "江西H5策略审批查询接口入参")
public class McdCampApproveH5Query {


    @ApiModelProperty(value = "每页大小")
    private String pageSize = "10";

    @ApiModelProperty(value = "当前页码")
    private String pageNum = "1";

    @ApiModelProperty(value = "渠道")
    private String channelId;

    @ApiModelProperty(value = "//系统类型策略：IMCD")
    private String systemId="IMCD";

    @ApiModelProperty(value = "查询关键字")
    private String keyword;

    @ApiModelProperty(value = "审批状态")
    private String approveStatus;

    @ApiModelProperty(value = "当前登录人")
    private String userId;

}
