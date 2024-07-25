package com.asiainfo.biapp.pec.approve.jx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 * @date 2023/05/26
 */
@Data
public class McdChannelRoleListVo {

    @ApiModelProperty(value = "策略ID")
    private String campsegId;
    @ApiModelProperty(value = "策略名称")
    private String campsegName;
    @ApiModelProperty(value = "渠道ID")
    private String channelId;
    @ApiModelProperty(value = "角色ID")
    private long roleId;


}
