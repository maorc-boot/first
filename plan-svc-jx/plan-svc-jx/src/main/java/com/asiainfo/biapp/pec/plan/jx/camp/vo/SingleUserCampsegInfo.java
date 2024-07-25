package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 单用户活动办理情况列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "单用户针对某一活动的操作生命周期", description = "单用户针对某一活动的操作生命周期")
public class SingleUserCampsegInfo implements Serializable {

    @ApiModelProperty(value = "策略id")
    private String campsegId;

    @ApiModelProperty(value = "策略名称")
    private String campsegName;

    @ApiModelProperty(value = "策略RootId")
    private String campsegRootId;

    @ApiModelProperty(value = "策略关联产品id")
    private String planId;

    @ApiModelProperty(value = "策略关联产品名称")
    private String planName;

    @ApiModelProperty(value = "策略有效期")
    private String expireDate;

    @ApiModelProperty(value = "策略关联渠道及当前用户办理情况(操作时间最大)")
    private List<SingleUserChannelPlanStatus> userChannelPlanStatusList;

    @ApiModelProperty(value = "当前用户策略关联渠道详情所有操作")
    private List<SingleUserMarketResultDetail> marketResultDetailList;

}
