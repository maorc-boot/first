package com.asiainfo.biapp.pec.plan.jx.hmh5.service;

import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.ScenarioAllVo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.ScenarioRule;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.CalloutScenarioQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleDel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleSaveModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.ModifyCalloutRuleStatusParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.ScenarioApproveJxQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.ScenarioRuleApprRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author admin
 * @version 1.0
 * @project backend-jx
 * @date 2024-07-12 10:55:27
 */
public interface ScenarioService {
	/**
	 * 根据关键字获取外呼场景table列表数据
	 * @param query
	 * @return
	 */
	Page<ScenarioRule> getScenarioList(CalloutScenarioQuery query);


	/**
	 * 根据关键字获取外呼权限table列表数据
	 * @param pager
	 * @param scenarioAllVo
	 * @return
	 */
	public List<Map<String, Object>> getScenarioAllList(Pager pager, ScenarioAllVo scenarioAllVo) throws ParseException;

	/**
	 * 获取业务场景列表数据
	 * @return
	 */
	public  List<Map<String, Object>> selectScenarioList();

	/**
	 * 获取个性化规则库已接通数据
	 * @return
	 */
	public  List<Map<String, Object>> selectRulesListYes();

	/**
	 * 获取个性化规则库未接通数据
	 * @return
	 */
	public  List<Map<String, Object>> selectRulesListNo();

	/**
	 * 根据id获取个性化规则库数据
	 * @return
	 */
	public  List<Map<String, Object>> selectMyscenarioById(String rulesIds);

	/**
	 * 获取通用规则库数据
	 * @return
	 */
	public  List<Map<String, Object>> getGeneralRules();

	/**
	 * 添加规则
	 */
	public void saveScenairoRule(ScenarioRuleQuery scenarioRule,String userId);

	/**
	 * 查询新建的规则是否存在
	 */
	public boolean hasRulesExist(ScenarioRuleQuery scenarioRule);

	/**
	 * 新建，保存
	 * @return
	 */
	public void addScenarioById(ScenarioRuleSaveModel scenarioRule);

	/**
	 * 修改，保存
	 * @return
	 */
	public  void updateScenarioById(ScenarioRuleSaveModel updateScenarioRule);

	/**
	 * 根据id删除外呼场景table列表数据
	 * @param ruleDel
	 */
	public void delectScenarioById(ScenarioRuleDel ruleDel);

	/**
	 * 获取场景下规则个数
	 * @param scenarioId
	 * @return
	 */
	Integer getRulesByScenaiorId(String scenarioId);

	/**
	 * 删除已经同步的规则数据
	 * @param cityId
	 * @param channelCodes
	 */
	void delectFqcRulesByCityId(String cityId,String channelCodes);


	/**
	 * 获取区县列表数据
	 * @return
	 */
	public  List<Map<String, Object>> selectCouniyNameListById(String cityId);
	/**
	 * 获取区县列表数据
	 * @return
	 */
	public  List<Map<String, Object>> selectCouniyNameList();

	/**
	 * 存入场景审批关系表
	 * @return
	 */
	public  void insertScenarioProcess(String scenarioProcessId,List<Map<String,Object>> list,String approverUser, String scenarioId);

	/**
	 * 获取根据场景下所有的草稿状态
	 * @return
	 */
	public  List<Map<String,Object>>  selectByIdType(String scenarioId);

	/**
	 * 修改状态
	 * @return
	 */
	public void updateScenarioProcess(List<Map<String,Object>> list);

	/**
	 * 审批详情数据
	 * @return
	 */
	public Map<String,Object> selectScenarioRules(String instancdId);

	/**
	 * 审批详情表格数据
	 * @return
	 */
	public  List<Map<String,Object>> tableLoudeData(Pager pager, String instancdId);

	/**
	 * 获取审批记录
	 * @return
	 */
	public List<Map<String,Object>> getScenarioById(String instancdId);

	Map<String,Object> queryScenarioInfoById(String scenarioRulesId);

	/**
	 * 查询所有场景所有地市对应的总的一条数据，并拆分组合
	 * @return
	 */
	Map<String,Object> queryAllScenarioAndCityData();

	/**
	 * 获取客户通外呼规则审批流程实例节点下级审批人
	 *
	 * @param submitProcessDTO submitProcessDTO
	 * @return SubmitProcessDTO
	 */
	ActionResponse<SubmitProcessJxDTO> getCalloutRuleApprover(SubmitProcessJxDTO submitProcessDTO);

	/**
	 * 客户通外呼规则提交审批
	 *
	 * @param req  要求事情
	 * @param user 用户
	 */
	void submitCalloutRule(SubmitProcessQuery req, UserSimpleInfo user);

	IPage<ScenarioRuleApprRecord> getApproveCalloutRuleRecord(ScenarioApproveJxQuery req);

	void udpateCalloutRuleStatus(ModifyCalloutRuleStatusParam calloutRuleStatusParam);

	/**
	 * 查询该场景对应的审批实例ID---审批中的状态
	 * @param scenarioId
	 * @return
	 */
	String queryCalloutRuleApprFlowId(String scenarioId);

}
