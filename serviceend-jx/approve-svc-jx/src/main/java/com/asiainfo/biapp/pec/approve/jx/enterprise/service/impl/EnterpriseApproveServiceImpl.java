package com.asiainfo.biapp.pec.approve.jx.enterprise.service.impl;

import com.asiainfo.biapp.pec.approve.jx.enterprise.dao.EnterpriseApproveDao;
import com.asiainfo.biapp.pec.approve.jx.enterprise.service.EnterpriseApproveService;
import com.asiainfo.biapp.pec.approve.jx.enterprise.util.RestConstant;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.ApproveUserVo;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.McdCampDef;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.McdCampTask;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.McdCampTaskDate;
import com.asiainfo.biapp.pec.approve.jx.utils.IdUtils;
import com.asiainfo.biapp.pec.core.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : yantao
 * @version : v1.0
 * @className : EnterpriseApproveServiceImpl
 * @description : [描述说明该类的功能]
 * @createTime : [2023-06-26 上午 11:31:48]
 */
@Service
@Slf4j
public class EnterpriseApproveServiceImpl implements EnterpriseApproveService {

	//任务待执行
	public static final Integer TASK_STATUS_UNDO = 50;
	//任务执行成功
	public static final Integer TASK_STATUS_SUCCESS = 54;

	@Autowired
	private EnterpriseApproveDao enterpriseApproveDao;

	@Override
	public List<ApproveUserVo> queryApprove() throws Exception {
		//查询审批人接口，查询字典表配置的角色ID
		String roleId = enterpriseApproveDao.queryDicApproveData();
		//根据角色ID查询审批信息
		List<ApproveUserVo> approveUserVos = enterpriseApproveDao.queryApprove(roleId);
		return approveUserVos;
	}

	/*@Override
	public McdCampDef getCampSegInfo(String campSegId) throws Exception {
		return enterpriseApproveDao.queryCampSegInfo(campSegId);
	}*/

	@Override
	public Map<String, Object> updateCampInfo(McdCampDef campDef) throws Exception {
		Map<String, Object> resultUpdateMap = new HashMap<>();
		resultUpdateMap.put("subitemId",campDef.getSubitemId());
		//任务表保存信息(审批完成需要)
		this.saveCampsegTask(campDef);
		//修改策略渠道关系表
		//7.0中此表没有审批结果和审批描述字段，故不更新
		// this.updateCampChannelInfo(campDef);
		//修改策略信息
		this.updateCampsegInfo(campDef);
		return resultUpdateMap;
	}




	/**
	 * 保存任务表数据
	 * @throws Exception
	 */
	private void saveCampsegTask(McdCampDef campDef){
		McdCampTask campTask = new McdCampTask();
		campTask.setTaskId(IdUtils.generateId()); // 任务编码
		campTask.setCampsegId(campDef.getSubitemId()); // 子策略编码
		Date taskStartDate = campDef.getStartDate() ;
		campTask.setTaskStartTime(new Date()); // 任务开始时间
		Date taskEndDate = campDef.getEndDate();
		campTask.setTaskEndTime(taskEndDate); // 任务结束时间
		campTask.setExecStatus(TASK_STATUS_UNDO); // 任务状态（50）
		campTask.setChannelId(campDef.getChannelId()); // 渠道编码
		campTask.setCycleType(new BigDecimal(RestConstant.NUMBER_SHORT_1)); // 客户群周期，默认设置为一次性
		campTask.setIntGroupNum(RestConstant.NUMBER_INTEGER_0); // 客户群数量（0）
		campTask.setBotherAvoidNum(RestConstant.NUMBER_INTEGER_0); // 免打扰过滤数（0）
		campTask.setContactControlNum(RestConstant.NUMBER_INTEGER_0); // 频次过滤数（0）
		//保存mcd_camp_task信息
		enterpriseApproveDao.insert(campTask);// 保存任务表数据
		String dataDate = DateUtil.formatDateToStr(taskStartDate, "yyyyMMdd");
		Date planExecTime = new Date();

		McdCampTaskDate date = new McdCampTaskDate();
		date.setTaskId(campTask.getTaskId());
		date.setDataDate(Integer.valueOf(dataDate));
		date.setExecStatus(TASK_STATUS_UNDO);
		date.setCustListCount(campTask.getIntGroupNum());
		date.setPlanExecTime(planExecTime);
		date.setCampsegId(campTask.getCampsegId());
		//保存mcd_camp_task_date信息
		enterpriseApproveDao.insertMcdCampsegTaskDate(date);
	}

	private void updateCampsegInfo(McdCampDef campDef) throws Exception {
		campDef.setCampsegStatId(TASK_STATUS_SUCCESS); // 策略状态（54）审批中
		campDef.setApproveResult(RestConstant.NUMBER_SHORT_1); // 审批结果（1）
		enterpriseApproveDao.updateCampsegInfo(campDef); // 保存子策略信息
		enterpriseApproveDao.updateCampDefInfo(campDef);
		enterpriseApproveDao.updateChannelCampInfo(campDef);
		// campDef.setCampId(campDef.getCampsegRootId()); // 策略编码
		// campDef.setPid(RestConstant.NUMBER_STRING_0);
		// campSegInfoDao.updateCampsegInfo(campDef); // 保存父策略信息
	}
}
