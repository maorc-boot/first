package com.asiainfo.biapp.pec.approve.jx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/12/6
 */
@Data
public class WarningVo {
    @ApiModelProperty(value = "策略ID")
    private String campsegId;
    @ApiModelProperty(value = "策略名称")
    private String campsegName;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "提交人ID")
    private String submiterId;
    @ApiModelProperty(value = "提交人姓名")
    private String submiterName;
    @ApiModelProperty(value = "创建时间")
    private String createTime;

}
