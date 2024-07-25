package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 单用户在当前活动某一渠道的操作详情
 */
@Data
@ApiModel(value = "单用户在当前活动某一渠道的操作详情", description = "单用户在当前活动某一渠道的操作详情")
public class SingleUserMarketResultDetail  {

    @ApiModelProperty(value = "渠道id")
    private String channelId;      
    @ApiModelProperty(value = "渠道名称")
    private String channelName;    
    @ApiModelProperty(value = "当前渠道操作详细列表")
    private List<MarketResultDetail> marketResultChannelDetailList;    


}
