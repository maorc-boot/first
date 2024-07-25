package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdSenarioRule;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.ScenarioRule;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.ScenarioRuleAdd;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.SceneConfig;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.CalloutScenarioQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.ModifyCalloutRuleStatusParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.ScenarioApproveJxQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.ScenarioRuleApprRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO
 *
 * @author admin
 * @version 1.0
 * @project backend-jx
 * @date 2024-07-12 11:03:02
 */
@Mapper
public interface ScenarioDao {
	/**
	 * 查询所有场景的单条数据详细信息
	 * @return
	 */
	Map<String,Object> queryAllScenarioData();

	List<ScenarioRule> queryScenarioList(@Param("page") Page<ScenarioRule> page, @Param("query") CalloutScenarioQuery query);

	List<Map<String, Object>> queryScenario();

	List<Map<String, Object>> queryRulesListYes();

	List<Map<String, Object>> queryRulesListNo();

	List<Map<String, Object>> queryGeneralRules();

	List<String> getRulesExist(@Param("query") ScenarioRuleQuery scenarioRule);

	void saveScenarioRule(@Param("param")ScenarioRuleAdd ruleAdd);

	void delectScenarioRulesByCityId(@Param("cityId") String cityId,@Param("scenarioId") String scenarioId);

	void delectScenarioRulesByRuleId(@Param("ruleId") String ruleId);

	void saveScenarioRuleBatch(@Param("list") List<McdSenarioRule> ruleList);

	List<SceneConfig> queryScenrioConfig();

	void deleteFqcRulesByCityId(@Param("cityId") String cityId,@Param("list") List<String> channelCodes);

	void updateScenarioRule(@Param("param") McdSenarioRule rule);

	Integer queryRulesByScenaiorId(@Param("scenarioId") String scenarioId);

	void updateScenarioRuleApproveStatus(@Param("scenarioId") String scenarioId,@Param("instanceId")String instanceId, @Param("approvalType")String approvalType,@Param("approveUserName") String approveUserName);

	/**
	 * 客户通外呼规则审批列表查询
	 *
	 * @param scenarioIds 任务编码集合
	 * @param req req
	 * @return List<BlacklistApprRecord>
	 */
	List<ScenarioRuleApprRecord> qryApprRecord(@Param("scenarioIds") Set<String> scenarioIds, @Param("query") ScenarioApproveJxQuery req);

	void udpateCalloutRuleStatus(@Param("param") ModifyCalloutRuleStatusParam calloutRuleStatusParam);

	String queryCalloutRuleApprFlowId(@Param("scenarioId") String scenarioId);
}
