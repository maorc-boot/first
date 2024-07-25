package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户通外呼规则审批列表分页(或根据条件)查询入参对象
 */

@Data
@ApiModel(value = "客户通外呼规则审批列表分页(或根据条件)查询入参",description = "客户通外呼规则审批列表分页(或根据条件)查询入参")
public class ScenarioApproveJxQuery {

    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "外呼场景ID")
    private String scenarioId;

    @ApiModelProperty(value = "创建人")
    private String userName;

    @ApiModelProperty("审批类型")
    private String approvalType;


}
