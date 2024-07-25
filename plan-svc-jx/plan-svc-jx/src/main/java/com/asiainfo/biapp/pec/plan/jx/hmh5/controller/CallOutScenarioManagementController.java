package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontBlacklistCust;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.ScenarioRule;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.CalloutScenarioQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleDel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleSaveModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.ScenarioService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.ModifyCalloutRuleStatusParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.ScenarioApproveJxQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.ScenarioRuleApprRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : yantao
 * @version : v1.0
 * @className : CallOutScenarioManagementController
 * @description : [外呼管理]
 * @createTime : [2024-07-12 上午 11:17:16]
 */
@Slf4j
@Api(value = "客户通-外呼管理相关",tags = "客户通-外呼管理相关")
@RestController
@RequestMapping("/callout/scenario")
@DataSource("khtmanageusedb")
public class CallOutScenarioManagementController {

	@Autowired
	private ScenarioService scenarioService;

	/**
	 * 获取所有场景单条详情信息
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "获取所有场景单条详情信息接口")
	@GetMapping(value = "/getAllScenarioAndCityData")
	public ActionResponse<Map<String, Object>> getAllScenarioAndCityData(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = scenarioService.queryAllScenarioAndCityData();
		}catch (Exception e){
			log.error("获取所有场景单条详情信息失败", e);
		}
		return ActionResponse.getSuccessResp(map);
	}

	/**
	 * 外呼场景管理列表查询
	 * @param request
	 * @param query
	 * @return
	 */
	@ApiOperation(value = "外呼场景管理列表查询")
	@PostMapping(value = "/getScenarioList")
	public ActionResponse<Page<McdFrontBlacklistCust>> getCustomizeTitleList(HttpServletRequest request, @RequestBody CalloutScenarioQuery query) {
		UserSimpleInfo user = UserUtil.getUser(request);
		//具体的分页数据
		Page<ScenarioRule> list = scenarioService.getScenarioList(query);
		return ActionResponse.getSuccessResp(list);
	}

	/**
	 * 获取所有业务场景
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "获取所有业务场景")
	@GetMapping(value = "/selectScenarioList")
	public ActionResponse<Map<String, Object>> selectScenarioList(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> maps = scenarioService.selectScenarioList();
		return ActionResponse.getSuccessResp(maps);
	}

	/**
	 * 获取个性化规则库已接通数据
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "获取个性化规则库已接通数据")
	@GetMapping(value = "/selectRulesListYes")
	public ActionResponse<Map<String, Object>> getRulesListYes(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> maps = scenarioService.selectRulesListYes();
		return ActionResponse.getSuccessResp(maps);
	}

	/**
	 * 获取个性化规则库未接通数据
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "获取个性化规则库未接通数据")
	@GetMapping(value = "/selectRulesListNo")
	public ActionResponse<Map<String, Object>> getRulesListNo(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> maps = scenarioService.selectRulesListNo();
		return ActionResponse.getSuccessResp(maps);
	}

	/**
	 * 获取通用规则库数据
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "获取通用规则库数据")
	@GetMapping(value = "/selectRulesListAll")
	public ActionResponse<String> getGeneralRules(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = new ArrayList<>();
		String rulesName = "";
		try {
			list = scenarioService.getGeneralRules();
			for (int i = 0; i < list.size(); i++) {
				Object rules_name = list.get(i).get("RULES_NAME");
				if (i == list.size() - 1) {
					rulesName += rules_name.toString();
				} else {
					rulesName += rules_name.toString() + ";";
				}
			}
		} catch (Exception e) {
			log.error("获取通用规则库数据失败", e);
		}
		return ActionResponse.getSuccessResp(rulesName);
	}

	/**
	 * 查询新建的规则是否存在
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "查询新建的规则是否存在")
	@PostMapping(value = "/rulesListRowNumber")
	public ActionResponse<Boolean> getHasRules(HttpServletRequest request, @RequestBody ScenarioRuleQuery scenarioRule) {
		boolean hasRulesExist = scenarioService.hasRulesExist(scenarioRule);
		return ActionResponse.getSuccessResp(hasRulesExist);
	}

	/**
	 * 添加规则
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "添加规则")
	@PostMapping(value = "/scenarioAddSave")
	public ActionResponse<Boolean> saveScenairoRule(HttpServletRequest request, @RequestBody ScenarioRuleQuery scenarioRule) {
		UserSimpleInfo user = UserUtil.getUser(request);
		String userId = user.getUserId();
		scenarioService.saveScenairoRule(scenarioRule,userId);
		return ActionResponse.getSuccessResp();
	}

	/**
	 * 新建-场景列表的规则
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "新建-场景列表的规则")
	@PostMapping(value = "/addScenarioById")
	public ActionResponse<Boolean> addScenarioById(HttpServletRequest request, @RequestBody ScenarioRuleSaveModel scenarioRule) {
		UserSimpleInfo user = UserUtil.getUser(request);
		String userId = user.getUserId();
		scenarioRule.setCreateUserId(userId);
		scenarioService.addScenarioById(scenarioRule);
		return ActionResponse.getSuccessResp();
	}

	/**
	 * 修改场景列表的规则
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "修改场景列表的规则")
	@PostMapping(value = "/updateScenarioById")
	public ActionResponse<Boolean> updateScenarioById(HttpServletRequest request, @RequestBody ScenarioRuleSaveModel updateScenarioRule) {
		UserSimpleInfo user = UserUtil.getUser(request);
		String userId = user.getUserId();
		updateScenarioRule.setCreateUserId(userId);
		scenarioService.updateScenarioById(updateScenarioRule);
		return ActionResponse.getSuccessResp();
	}

	/**
	 * 根据id删除外呼场景table列表数据
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "根据id删除外呼场景table列表数据")
	@PostMapping(value = "/delectScenarioById")
	public ActionResponse<Boolean> delectScenarioById(HttpServletRequest request, @Valid @RequestBody ScenarioRuleDel ruleDel) {
		UserSimpleInfo user = UserUtil.getUser(request);
		String userId = user.getUserId();
		ruleDel.setOperator(userId);
		scenarioService.delectScenarioById(ruleDel);
		return ActionResponse.getSuccessResp();
	}

	/**
	 * 提交审批前，获取提交审批场景对应的会所有待审批的规则个数
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "提交审批前，获取提交审批场景对应的会所有待审批的规则个数")
	@GetMapping(value = "/getRulesByScenaiorId")
	public ActionResponse<Integer> getRulesByScenaiorId(HttpServletRequest request, @RequestParam(value = "scenarioId") String scenarioId) {
		Integer count = 0;
		try {
			count = scenarioService.getRulesByScenaiorId(scenarioId);
		} catch (Exception e) {
			log.error("提交审批前，获取提交审批场景对应的会所有待审批的规则个数", e);
		}
		return ActionResponse.getSuccessResp(count);
	}

	@PostMapping("/getCalloutRuleApprover")
	@ApiOperation(value = "获取客户通外呼规则审批流程实例节点下级审批人",notes = "获取客户通外呼规则审批流程实例节点下级审批人")
	public ActionResponse<SubmitProcessJxDTO> getCalloutRuleApprover(@RequestBody SubmitProcessJxDTO submitProcessDTO){
		log.info("【客户通外呼规则】getCalloutRuleApprover param:{}", JSONUtil.toJsonStr(submitProcessDTO));
		return scenarioService.getCalloutRuleApprover(submitProcessDTO);
	}

	@PostMapping("/submitCalloutRuleApprove")
	@ApiOperation(value = "客户通外呼规则提交审批", notes = "客户通外呼规则提交审批")
	public ActionResponse submitCalloutRule(@RequestBody SubmitProcessQuery req, HttpServletRequest request) {
		log.info("【客户通外呼规则】submitCalloutRule param:{}", JSONUtil.toJsonStr(req));
		final UserSimpleInfo user = UserUtil.getUser(request);
		scenarioService.submitCalloutRule(req, user);
		return ActionResponse.getSuccessResp();
	}

	@ApiOperation(value = "客户通外呼规则审批列表", notes = "客户通外呼规则审批列表")
	@PostMapping("/getApproveCalloutRuleRecord")
	public IPage<ScenarioRuleApprRecord> getApproveCalloutRuleRecord(@RequestBody ScenarioApproveJxQuery req) {
		log.info("客户通外呼规则审批列表para:{}", JSONUtil.toJsonStr(req));
		return scenarioService.getApproveCalloutRuleRecord(req);
	}

	@ApiIgnore
	@ApiOperation(value = "客户通外呼规则审批流转修改状态")
	@PostMapping("/modifyCalloutRuleStatus")
	public ActionResponse modifyCalloutRuleStatus(@RequestBody ModifyCalloutRuleStatusParam calloutRuleStatusParam) {
		log.info("客户通外呼规则审批流转修改状态param={}", JSONUtil.toJsonStr(calloutRuleStatusParam));
		scenarioService.udpateCalloutRuleStatus(calloutRuleStatusParam);
		return ActionResponse.getSuccessResp();
	}

	@ApiIgnore
	@GetMapping("/selectCalloutRuleApprFlowId")
	@ApiOperation(value = "客户通外呼规则审批流程ID查询")
	public ActionResponse<String> selectCalloutRuleApprFlowId(@NotNull(message = "外呼场景编码不可为空！") String scenarioId) {
		UserSimpleInfo user = UserUtilJx.getUser();
		if (user.getUserId() == null) {
			log.error("没有获取到登录用户，正在创建虚拟用户！--------------------------------------------------------------");
			user.setUserId("admin01");
			user.setCityId("999");
		}
		log.info("用户：{}正在查询客户通外呼规则审批流程ID，scenarioId值为：{}！", user.getUserName(), scenarioId);
		// 此处存在潜在问题，因为外呼规则提交审批，目前是场景级别的审批，提交一次，这个场景下所有规则都会提交审批，如果同一场景提交多次并且审批流不同，此处无法精确识别是哪一个审批提交；
		String approveFlowId = scenarioService.queryCalloutRuleApprFlowId(scenarioId);
		if (StrUtil.isBlank(approveFlowId)) throw new BaseException("无效的scenarioId值！");
		ActionResponse successResp = ActionResponse.getSuccessResp();
		successResp.setData(approveFlowId);
		return successResp;
	}




}
