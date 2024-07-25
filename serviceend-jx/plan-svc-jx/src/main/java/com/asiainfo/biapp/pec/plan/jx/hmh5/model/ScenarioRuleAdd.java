package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleQuery;
import lombok.Data;

/**
 * @author : yantao
 * @version : v1.0
 * @className : ScenarioRuleAdd
 * @description : [描述说明该类的功能]
 * @createTime : [2024-07-15 下午 14:37:41]
 */
@Data
public class ScenarioRuleAdd extends ScenarioRuleQuery {
	private String userId;
	private String ruleName;
	private String ruleId;
}
