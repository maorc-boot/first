package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 关怀短信模板审批列表分页(或根据条件)查询
 *
 * @author lvcc
 * @date 2023/03/14
 */
@Data
@ApiModel(value = "关怀短信模板审批列表分页(或根据条件)查询入参",description = "关怀短信模板审批列表分页(或根据条件)查询入参")
public class CareSmsTemplateApproveJxQuery {

    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "业务对象名称")
    private String templateCode;

    // @ApiModelProperty(value = "渠道")
    // private String channelId;

    @ApiModelProperty(value = "创建人")
    private String userName;

    @ApiModelProperty("审批类型")
    private String approvalType;


}
