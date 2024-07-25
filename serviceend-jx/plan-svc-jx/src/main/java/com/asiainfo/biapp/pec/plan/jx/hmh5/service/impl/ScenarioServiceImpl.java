package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.pec.approve.model.CmpApprovalProcess;
import com.asiainfo.biapp.client.pec.approve.model.RecordPageDTO;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.api.PecApproveFeignClient;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CmpApproveProcessRecordJx;
import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.ScenarioDao;
import com.asiainfo.biapp.pec.plan.jx.hmh5.enums.Hmh5BlacklistEnum;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.CalloutScenarioQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleDel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.ScenarioRuleSaveModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.ScenarioService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.mapper.McdAppAlarmInfoMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdDimCity;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.*;
import com.asiainfo.biapp.pec.plan.util.MpmUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : yantao
 * @version : v1.0
 * @className : ScenarioServiceImpl
 * @description : [描述说明该类的功能]
 * @createTime : [2024-07-12 上午 10:58:44]
 */
@Slf4j
@Service
public class ScenarioServiceImpl implements ScenarioService {

	@Autowired
	private ScenarioDao scenarioDao;

	@Autowired
	private McdAppAlarmInfoMapper mcdAppAlarmInfoMapper;

	@Autowired
	private PecApproveFeignClient approveFeignClient;


	@Override
	public Page<ScenarioRule> getScenarioList(CalloutScenarioQuery query) {
		Page<ScenarioRule> page = new Page<>(query.getCurrent(), query.getSize());
		List<ScenarioRule> list = scenarioDao.queryScenarioList(page, query);
		page.setRecords(list);
		return page;
	}

	@Override
	public List<Map<String, Object>> getScenarioAllList(Pager pager, ScenarioAllVo scenarioAllVo) throws ParseException {
		return null;
	}

	@Override
	public List<Map<String, Object>> selectScenarioList() {
		return scenarioDao.queryScenario();
	}

	@Override
	public List<Map<String, Object>> selectRulesListYes() {
		return scenarioDao.queryRulesListYes();
	}

	@Override
	public List<Map<String, Object>> selectRulesListNo() {
		return scenarioDao.queryRulesListNo();
	}

	@Override
	public List<Map<String, Object>> selectMyscenarioById(String rulesIds) {
		return null;
	}

	@Override
	public List<Map<String, Object>> getGeneralRules() {
		return scenarioDao.queryGeneralRules();
	}

	@Override
	public void saveScenairoRule(ScenarioRuleQuery scenarioRule, String userId) {
		String unit = scenarioRule.getUnit();
		if("0".equals(unit)){
			scenarioRule.setUnit("日");
		} else {
			scenarioRule.setUnit("月");
		}
		//自动生成id
		String rulesId =  MpmUtil.generateCampsegAndTaskNo();
		String rulesName = scenarioRule.getFrequency() +"次/1"+scenarioRule.getUnit();
		ScenarioRuleAdd ruleAdd = new ScenarioRuleAdd();
		ruleAdd.setUserId(userId);
		ruleAdd.setRuleName(rulesName);
		ruleAdd.setFrequency(scenarioRule.getFrequency());
		ruleAdd.setUnit(scenarioRule.getUnit());
		ruleAdd.setScenarioType(scenarioRule.getScenarioType());
		ruleAdd.setRuleId(rulesId);
		scenarioDao.saveScenarioRule(ruleAdd);

	}

	@Override
	public boolean hasRulesExist(ScenarioRuleQuery scenarioRule) {
		String unit = scenarioRule.getUnit();
		if("0".equals(unit)){
			scenarioRule.setUnit("日");
		} else {
			scenarioRule.setUnit("月");
		}
		List<String> list = scenarioDao.getRulesExist(scenarioRule);
		if(CollectionUtil.isNotEmpty(list)){
			return true;
		}
		return false;
	}

	@Override
	public void addScenarioById(ScenarioRuleSaveModel scenarioRule) {
		List<String > rulesIdsAllList = new ArrayList<>();
		// 已接触规则集合
		List<RuleModel> rulesIdsYes = scenarioRule.getRulesIdsYes();
		// 未接触规则集合
		List<RuleModel> rulesIdsNo = scenarioRule.getRulesIdsNo();
		// 将规则id添加到集合中
		rulesIdsYes.stream().map(RuleModel::getRuleId).forEach(rulesIdsAllList::add);
		rulesIdsNo.stream().map(RuleModel::getRuleId).forEach(rulesIdsAllList::add);
		// 所有规则集合
		String rulesIds = rulesIdsAllList.stream().collect(Collectors.joining(","));
		String ruleNameYes = rulesIdsYes.stream().map(RuleModel::getRuleName).collect(Collectors.toList()).stream().collect(Collectors.joining(";"));
		String ruleNameNo = rulesIdsNo.stream().map(RuleModel::getRuleName).collect(Collectors.toList()).stream().collect(Collectors.joining(";"));

		List<String> cityAll = new ArrayList<>();
		if(!"1".equals(scenarioRule.getCityId())){
			cityAll.add(scenarioRule.getCityId());
		}else{
			// 获取所有的地市
			List<McdDimCity> cityList = mcdAppAlarmInfoMapper.queryAllCityCode();
			// 获取所有的地市ID
			List<String> cityIdList = cityList.stream().map(McdDimCity::getCityId).collect(Collectors.toList());
			cityAll.addAll(cityIdList);
		}
		// 80901/80902等
		String scenarioId = scenarioRule.getScenarioId();
		List<McdSenarioRule> ruleList = new ArrayList<>();
		for (String cityId : cityAll) {
			// 先删除已有的，然后重新保存
			scenarioDao.delectScenarioRulesByCityId(cityId,scenarioId);
			String scenarioRulesId =  MpmUtil.generateCampsegAndTaskNo();
			McdSenarioRule rule = new McdSenarioRule();
			rule.setScenarioRulesId(scenarioRulesId);
			rule.setScenarioId(scenarioId);
			rule.setCityId(cityId);
			rule.setRulesId(rulesIds);
			rule.setMyRulesNo(ruleNameNo);
			rule.setMyRulesYes(ruleNameYes);
			rule.setMyRules(scenarioRule.getMyScenarioRulesAll());
			rule.setApprovalType("3");
			rule.setCreateScenarioRulesUser(scenarioRule.getCreateUserId());
			rule.setDelectScenarioRulesType("0");
			ruleList.add(rule);
		}
		// 批量保存
		scenarioDao.saveScenarioRuleBatch(ruleList);
	}

	@Override
	public void updateScenarioById(ScenarioRuleSaveModel updateScenarioRule) {
		// 獲取地市
		String cityId = updateScenarioRule.getCityId();
		// 場景規則ID
		String scenarioRuleId = updateScenarioRule.getScenarioRuleId();
		// 获取个性化规则集合（已接通）
		List<RuleModel> rulesIdsYes = updateScenarioRule.getRulesIdsYes();
		// 获取个性化规则集合(未接通)
		List<RuleModel> rulesIdsNo = updateScenarioRule.getRulesIdsNo();
		// 场景编码
		String scenarioId = updateScenarioRule.getScenarioId();
		// 获取通用规则集合
		String myScenarioRulesAll = updateScenarioRule.getMyScenarioRulesAll();

		List<String > rulesIdsAllList = new ArrayList<>();
		// 将规则id添加到集合中
		rulesIdsYes.stream().map(RuleModel::getRuleId).forEach(rulesIdsAllList::add);
		rulesIdsNo.stream().map(RuleModel::getRuleId).forEach(rulesIdsAllList::add);
		// 所有规则集合
		String rulesIds = rulesIdsAllList.stream().collect(Collectors.joining(","));
		String ruleNameYes = rulesIdsYes.stream().map(RuleModel::getRuleName).collect(Collectors.toList()).stream().collect(Collectors.joining(";"));
		String ruleNameNo = rulesIdsNo.stream().map(RuleModel::getRuleName).collect(Collectors.toList()).stream().collect(Collectors.joining(";"));

		McdSenarioRule rule = new McdSenarioRule();
		rule.setScenarioRulesId(scenarioRuleId);
		rule.setScenarioId(scenarioId);
		rule.setCityId(cityId);
		rule.setRulesId(rulesIds);
		rule.setMyRulesNo(ruleNameNo);
		rule.setMyRulesYes(ruleNameYes);
		rule.setMyRules(myScenarioRulesAll);
		rule.setApprovalType("3");
		rule.setCreateScenarioRulesUser(updateScenarioRule.getCreateUserId());
		rule.setDelectScenarioRulesType("0");
		scenarioDao.updateScenarioRule(rule);
	}

	@Override
	public void delectScenarioById(ScenarioRuleDel ruleDel) {
		String ruleId = ruleDel.getRuleId();
		String approvalStatus = ruleDel.getApprovalStatus();
		String scenarioId = ruleDel.getScenarioId();
		String cityId = ruleDel.getCityId();
		// 1.删除场景规则数据
		scenarioDao.delectScenarioRulesByRuleId(ruleId);
		// 2.如果状态为已完成，需要删除已同步的规则信息,删除表中的信息，redis中的待定时任务触发
		if ("5".equals(approvalStatus)) {
			List<SceneConfig> sceneConfigs = scenarioDao.queryScenrioConfig();
			Map<String, List<String>> sceneChannelMap = sceneConfigs.stream()
					.collect(Collectors.toMap(sceneConfig -> sceneConfig.getSceneCode(),
							sceneConfig -> Lists.newArrayList(sceneConfig.getChannelCode()),
							(List<String> newValueList, List<String> oldValueList) -> {
								oldValueList.addAll(newValueList);
								return oldValueList;
							}));
			List<String> channelCodeList = sceneChannelMap.get(scenarioId);
			if (CollectionUtil.isNotEmpty(channelCodeList)) {
				scenarioDao.deleteFqcRulesByCityId(cityId, channelCodeList);
			}
		}
	}

	@Override
	public Integer getRulesByScenaiorId(String scenarioId) {
		return scenarioDao.queryRulesByScenaiorId(scenarioId);
	}

	@Override
	public void delectFqcRulesByCityId(String cityId, String channelCodes) {

	}

	@Override
	public List<Map<String, Object>> selectCouniyNameListById(String cityId) {
		return null;
	}

	@Override
	public List<Map<String, Object>> selectCouniyNameList() {
		return null;
	}

	@Override
	public void insertScenarioProcess(String scenarioProcessId, List<Map<String, Object>> list, String approverUser, String scenarioId) {

	}

	@Override
	public List<Map<String, Object>> selectByIdType(String scenarioId) {
		return null;
	}

	@Override
	public void updateScenarioProcess(List<Map<String, Object>> list) {

	}

	@Override
	public Map<String, Object> selectScenarioRules(String instancdId) {
		return null;
	}

	@Override
	public List<Map<String, Object>> tableLoudeData(Pager pager, String instancdId) {
		return null;
	}

	@Override
	public List<Map<String, Object>> getScenarioById(String instancdId) {
		return null;
	}

	@Override
	public Map<String, Object> queryScenarioInfoById(String scenarioRulesId) {
		return null;
	}

	@Override
	public Map<String, Object> queryAllScenarioAndCityData() {
		int connectedMonth = 0;
		int connectedDay = 0;
		int notConnectedMonth = 0;
		int notConnectedDay = 0;
		Map<String, Object> map = scenarioDao.queryAllScenarioData();
		String rulesNo = map.get("MY_RULES_NO").toString();
		String rulesYes = map.get("MY_RULES_YES").toString();
		//未接通
		if(StringUtils.isNotBlank(rulesNo)){
			String[] split = rulesNo.replaceAll("次","").split(";");
			for (String sinString : split) {
				if(sinString.contains("月")){
					String[] strings = sinString.split("/");
					notConnectedMonth=Integer.parseInt(strings[0]);
				}
				if(sinString.contains("日")){
					String[] strings = sinString.split("/");
					notConnectedDay=Integer.parseInt(strings[0]);
				}
			}
		}
		//已接通
		if(StringUtils.isNotBlank(rulesYes)){
			String[] split = rulesYes.replaceAll("次","").split(";");
			for (String sinString : split) {
				if(sinString.contains("月")){
					String[] strings = sinString.split("/");
					connectedMonth=Integer.parseInt(strings[0]);
				}
				if(sinString.contains("日")){
					String[] strings = sinString.split("/");
					connectedDay=Integer.parseInt(strings[0]);
				}
			}
		}
		map.put("connectedMonth",connectedMonth);
		map.put("connectedDay",connectedDay);
		map.put("notConnectedMonth",notConnectedMonth);
		map.put("notConnectedDay",notConnectedDay);
		return map;
	}

	@Override
	public ActionResponse<SubmitProcessJxDTO> getCalloutRuleApprover(SubmitProcessJxDTO submitProcessDTO) {
		if (submitProcessDTO.getProcessId() == null){
			final ActionResponse<CmpApprovalProcess> callRulelist = approveFeignClient.getApproveConfig(submitProcessDTO.getApprovalType());
			log.info("【客户通外呼规则】getApproveConfig返回 ====> {}", JSONUtil.toJsonStr(callRulelist));
			// feign调用失败的情况
			if (ResponseStatus.SUCCESS.getCode() != callRulelist.getStatus().getCode())
				throw new BaseException(callRulelist.getMessage());
			// 首次获取审批流程节点的必要参数
			JSONObject triggerParm = new JSONObject();
			triggerParm.putOpt("channelId", "");  //不设置此值无法查询到结果
			submitProcessDTO.setProcessId(callRulelist.getData().getProcessId());
			submitProcessDTO.setBerv(callRulelist.getData().getBerv());
			submitProcessDTO.setTriggerParm(triggerParm);
		}
		log.info("【客户通外呼规则】getNodeApprover入参 ====> {}", JSONUtil.toJsonStr(submitProcessDTO));
		final ActionResponse<SubmitProcessJxDTO> submitprocessdto = approveFeignClient.getNodeApprover(submitProcessDTO);
		log.info("【客户通外呼规则】SubmitProcessDTO -【{}】", submitprocessdto);
		return submitprocessdto;
	}

	@Override
	public void submitCalloutRule(SubmitProcessQuery req, UserSimpleInfo user) {
		// 外呼场景ID
		String scenarioId = req.getBusinessId();
		final ActionResponse<Object> submit = approveFeignClient.submit(req);
		log.info("客户通外呼规则提交审批结果->{}", JSONUtil.toJsonStr(submit));
		if (ResponseStatus.SUCCESS.equals(submit.getStatus())) {
			log.info("【客户通外呼规则】submit approval success, flowId->{}", submit.getData());
			final Long id = (Long) submit.getData();
			String instanceId = (Long.toString(id));
			// 	审批状态: 1：审批中 2：执行中 3：草稿 4：已驳回 5:审批完成
			String approvalType = Hmh5BlacklistEnum.APPROVING.getCode();
			String approveUserName = user.getUserName();
			scenarioDao.updateScenarioRuleApproveStatus(scenarioId, instanceId, approvalType,approveUserName);
		} else {
			throw new BaseException("【客户通外呼规则】提交审批失败");
		}
	}

	@Override
	public IPage<ScenarioRuleApprRecord> getApproveCalloutRuleRecord(ScenarioApproveJxQuery req) {
		RecordPageDTO dto = new RecordPageDTO();
		dto.setCurrent(req.getCurrent());
		dto.setSize(req.getSize());
		List<ScenarioRuleApprRecord> smsTemplateApprRecordList  = scenarioDao.qryApprRecord(Collections.EMPTY_SET, req);
		if(CollectionUtils.isEmpty(smsTemplateApprRecordList)){
			log.info("客户通外呼规则审批列表为空！");
			return new Page<>();
		}
		Map<String, ScenarioRuleApprRecord> smsTemplateMap = smsTemplateApprRecordList.stream().collect(Collectors.toMap(ScenarioRuleApprRecord::getScenarioId, Function.identity()));
		dto.setList(Lists.newArrayList(smsTemplateMap.keySet()));
		final ActionResponse<Page<CmpApproveProcessRecordJx>> userRecord = approveFeignClient.getUserRecordNew(dto);
		log.info("【客户通外呼规则】当前用户待审记录->{}", JSONUtil.toJsonStr(userRecord));
		Assert.isTrue(ResponseStatus.SUCCESS.equals(userRecord.getStatus()), userRecord.getMessage());
		final IPage<CmpApproveProcessRecordJx> data = userRecord.getData();
		final Set<String> templateCodes = data.getRecords().stream().map(CmpApproveProcessRecordJx::getBusinessId).collect(Collectors.toSet());
		smsTemplateApprRecordList = scenarioDao.qryApprRecord(templateCodes, req);
		smsTemplateMap = smsTemplateApprRecordList.stream().collect(Collectors.toMap(ScenarioRuleApprRecord::getScenarioId, Function.identity()));
		IPage<ScenarioRuleApprRecord> apprRecordIPage = new Page<>(req.getCurrent(), req.getSize(), smsTemplateApprRecordList.size());
		List<ScenarioRuleApprRecord> listRecord = new ArrayList<>();
		apprRecordIPage.setRecords(listRecord);
		for (CmpApproveProcessRecordJx record : data.getRecords()) {
			if (null != smsTemplateMap.get(record.getBusinessId())) {
				// SmsTemplateApprRecord vo = new SmsTemplateApprRecord();
				// BeanUtils.copyProperties(record, vo);
				// listRecord.add(vo);
				// listRecord.add(smsTemplateMap.get(record.getBusinessId()));
				listRecord.add(convertToAppr(record, smsTemplateMap.get(record.getBusinessId())));
			}
		}
		return apprRecordIPage;
	}

	@Override
	public void udpateCalloutRuleStatus(ModifyCalloutRuleStatusParam calloutRuleStatusParam) {
		scenarioDao.udpateCalloutRuleStatus(calloutRuleStatusParam);
	}

	@Override
	public String queryCalloutRuleApprFlowId(String scenarioId) {
		return scenarioDao.queryCalloutRuleApprFlowId(scenarioId);
	}


	/**
	 * 信息转换
	 *
	 * @param record 审批信息
	 * @param scenarioRuleApprRecord 客户通外呼规则对象信息
	 * @return {@link SmsTemplateApprRecord}
	 */
	public ScenarioRuleApprRecord convertToAppr(CmpApproveProcessRecordJx record, ScenarioRuleApprRecord scenarioRuleApprRecord) {
		final ScenarioRuleApprRecord apprRecord = new ScenarioRuleApprRecord();
		// 模板相关信息
		apprRecord.setScenarioId(scenarioRuleApprRecord.getScenarioId());
		apprRecord.setScenarioName(scenarioRuleApprRecord.getScenarioName());
		apprRecord.setUserName(scenarioRuleApprRecord.getUserName());
		apprRecord.setCreateTime(scenarioRuleApprRecord.getCreateTime());
		apprRecord.setCreateUserId(scenarioRuleApprRecord.getCreateUserId());
		// 审批对象相关需要的信息
		apprRecord.setId(record.getId());
		apprRecord.setBusinessId(record.getBusinessId());
		apprRecord.setInstanceId(record.getInstanceId());
		apprRecord.setNodeId(record.getNodeId());
		apprRecord.setNodeName(record.getNodeName());
		apprRecord.setNodeType(record.getNodeType());
		apprRecord.setNodeBusinessName(record.getNodeBusinessName());
		apprRecord.setApprover(record.getApprover());
		apprRecord.setApproverName(record.getApproverName());
		apprRecord.setDealOpinion(record.getDealOpinion());
		apprRecord.setDealStatus(record.getDealStatus());
		apprRecord.setDealTime(record.getDealTime());
		apprRecord.setPreRecordId(record.getPreRecordId());
		apprRecord.setEventId(record.getEventId());
		apprRecord.setCreateDate(record.getCreateDate());
		apprRecord.setCreateBy(record.getCreateBy());
		apprRecord.setModifyDate(record.getModifyDate());
		apprRecord.setModifyBy(record.getModifyBy());
		return apprRecord;
	}
}
