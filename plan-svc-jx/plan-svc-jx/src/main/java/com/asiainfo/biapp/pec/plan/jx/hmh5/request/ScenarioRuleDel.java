package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author : yantao
 * @version : v1.0
 * @className : ScenarioRuleDel
 * @description : [描述说明该类的功能]
 * @createTime : [2024-07-16 下午 15:17:25]
 */
@Data
public class ScenarioRuleDel {
	@ApiModelProperty(value = "地市ID")
	private String cityId;
	@ApiModelProperty(value = "审批状态")
	private String approvalStatus;
	@ApiModelProperty(value = "规则ID")
	@NotBlank(message = "规则ID不能为空！")
	private String ruleId;
	@ApiModelProperty(value = "场景ID")
	private String scenarioId;
	@ApiModelProperty(value = "操作人")
	private String operator;
}
