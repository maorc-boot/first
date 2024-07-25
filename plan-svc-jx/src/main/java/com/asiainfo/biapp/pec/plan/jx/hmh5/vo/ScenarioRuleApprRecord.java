package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import com.asiainfo.biapp.client.pec.approve.model.CmpApproveProcessRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * description: 外呼场景规则审批列表实体
 *
 * @author: lvchaochao
 * @date: 2023/3/15
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "外呼场景规则审批列表实体",description = "外呼场景规则审批列表实体")
@Data
public class ScenarioRuleApprRecord extends CmpApproveProcessRecord {

    @ApiModelProperty("场景编码")
    private String scenarioId;

    @ApiModelProperty("外呼场景规则创建人ID")
    private String createUserId;

    @ApiModelProperty("外呼场景规则创建人姓名")
    private String userName;

    @ApiModelProperty("外呼场景规则创建时间")
    private Date createTime;

    @ApiModelProperty("场景名称")
    private String scenarioName;

}
