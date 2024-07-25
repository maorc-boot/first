package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.asiainfo.biapp.client.pec.approve.model.CmpApproveProcessRecord;
import lombok.Data;

/**
 * @author : yantao
 * @version : v1.0
 * @className : McdSenarioRule
 * @description : [描述说明该类的功能]
 * @createTime : [2024-07-16 上午 10:31:23]
 */
@Data
public class McdSenarioRule extends CmpApproveProcessRecord {
	// 外呼场景规则ID
	private String scenarioRulesId;
	// 业务场景ID
	private String scenarioId;
	// 地市ID
	private String cityId;
	// 规则ID
	private String rulesId;
	// 个性化规则(未接通)
	private String myRulesNo;
	// 个性化规则(已接通)
	private String myRulesYes;
	// 通用规则
	private String myRules;
	// 审批状态 1：审批中 2：执行中 3：草稿 4：已驳回 5:审批完成
	private String approvalType;
	// 创建人
	private String createScenarioRulesUser;
	// 删除状态 0：未删除 1：删除
	private String delectScenarioRulesType;
	// 创建时间
	private String createScenarioRulesData;
	// 审批实例ID
	// private String instanceId;
	// 当前审批人姓名
	private String approverName;


}
