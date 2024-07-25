package com.asiainfo.biapp.pec.plan.jx.camp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author : zhouyang
 * @date : 2021-12-07 16:17:30
 * 优先级列表实体
 */
@Data
@ApiModel(value = "江西:优先级列表实体", description = "优先级列表实体")
public class CampPriorityOrderJx {

    @ApiModelProperty(value = "策略id")
    private String campsegId;

    @ApiModelProperty(value = "父策略id")
    private String campsegPid;

    @ApiModelProperty(value = "父策略名称")
    private String campsegName;

    @ApiModelProperty(value = "优先级")
    private double priorityOrder;

    @ApiModelProperty(value = "客户群数量")
    private Integer customNum;

    @ApiModelProperty(value = "渠道id")
    private String channelId;

    @ApiModelProperty(value = "运营位id")
    private String channelAdivId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
