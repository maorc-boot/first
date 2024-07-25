package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/4/4
 */
@Data
@ApiModel(value = "历史活动信息", description = "历史活动信息")
public class HisCampInfo {
    @ApiModelProperty(value = "活动rootId")
    private String campsegRootId;
    @ApiModelProperty(value = "活动Id")
    private String campsegId;
    @ApiModelProperty(value = "活动名称")
    private String campsegName;
    @ApiModelProperty(value = "渠道Id")
    private String channelId;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "创建人ID")
    private String createUserId;
    @ApiModelProperty(value = "创建人名称")
    private String createUserName;
}
