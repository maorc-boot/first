package com.asiainfo.biapp.pec.element.jx.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Description : 渠道(或根据条件)分页查询入参
 * </p>
 *
 * @author : wuhq6
 * @date : Created in 2021/11/25  15:39
 * //@modified By :
 * //@since :
 */

@Data
@ApiModel(value = "渠道(或根据条件)分页查询入参",description = "渠道(或根据条件)分页查询入参")
public class DimChannelPageListRequestJx {
    
    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;
    
    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;
    
    /**
     * 关键字
     */
    @ApiModelProperty(value = "关键字")
    private String keywords;
    
    
    @ApiModelProperty(value = "ONLINE_STATUS 上线状态 状态1：上线 0：未上线")
    private Integer onlineStatus;

    @ApiModelProperty(value = "渠道归属(政企规范), 1-内部系统, 2-外部合作方")
    private Integer channelAffiliation;

    @ApiModelProperty(value = "触点类型(政企/大市场), 1-政企, 2-大市场, 0-其它")
    private Integer channelTypeZq;
    
}
