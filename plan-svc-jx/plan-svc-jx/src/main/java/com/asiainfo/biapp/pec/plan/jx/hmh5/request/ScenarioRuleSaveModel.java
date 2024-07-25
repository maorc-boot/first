package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import com.asiainfo.biapp.pec.plan.jx.hmh5.model.RuleModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author : yantao
 * @version : v1.0
 * @className : ScenarioRuleSaveModel
 * @description : [描述说明该类的功能]
 * @createTime : [2024-07-15 下午 15:26:52]
 */
@ApiModel(value = "外呼场景频次配置，保存频次接口入参")
@Data
public class ScenarioRuleSaveModel {
	@ApiModelProperty(value = "场景")
	private String scenarioId;
	@ApiModelProperty(value = "个性化规则集合(未接通)")
	private List<RuleModel> rulesIdsNo;
	@ApiModelProperty(value = "个性化规则集合(已接通)")
	private List<RuleModel> rulesIdsYes;
	@ApiModelProperty(value = "通用规则")
	private String myScenarioRulesAll;
	@ApiModelProperty(value = "地市ID")
	private String cityId;
	@ApiModelProperty(value = "创建人")
	private String createUserId;
	@ApiModelProperty(value = "场景规则ID")
	private String scenarioRuleId;

}
