package com.asiainfo.biapp.pec.approve.jx.enterprise.controller;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.biapp.pec.approve.jx.enterprise.service.EnterpriseApproveService;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.ApproveUserVo;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.MarketResultVo;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.McdCampDef;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : yantao
 * @version : v1.0
 * @className : EnterpriseApproveController
 * @description : 政企活动审批相关
 * @createTime : [2023-06-26 上午 11:13:08]
 */
@RestController
@Slf4j
@RequestMapping("/jx/enterprise/approve")
@Api(value = "江西:政企审批回调", tags = {"江西:政企审批回调"})
public class EnterpriseApproveController {
	//派单成功（执行中）
	public static final String MPM_CAMPSEG_STAT_DDCG = "54";
	@Resource
	private EnterpriseApproveService enterpriseApproveService;
	/**
	 * 1.2 查询审批人接口: 获取下级审批人
	 * @return
	 */
	@RequestMapping("/queryApprove")
	public Object queryApprove() {
		Map<String, Object> result = new HashMap<>();
		try {
			List<ApproveUserVo> approveUserVoList = enterpriseApproveService.queryApprove();
			result.put("resultCode", "0");
			result.put("resultInfo", "OK");
			result.put("data", approveUserVoList);
		} catch (Exception e) {
			result.put("resultCode", "201");
			result.put("resultInfo", "ERROR,请联系管理员");
			result.put("data", null);
			log.error("查询审批人接口: 获取下级审批人异常{}", e);
		}
		return result;
	}

	/**
	 * 审批回调接口
	 * @param campDef
	 * @return
	 */
	@RequestMapping(value = "/campsegApproveSync")
	public Object mcdExternalCampApproveFeedback(@RequestBody McdCampDef campDef) {
		log.info("mcdExternalCampApproveFeedback审批回调接口入参:{}",campDef.toString());
		MarketResultVo resultVo = new MarketResultVo();
		//如果活动ID字段为null,则此次请求所对应活动为线下活动，线下活动直接返回OK
		if(StringUtils.isBlank(campDef.getSubitemId())){
			// resultVo.setData(object);
			resultVo.setResultInfo("OK");
			resultVo.setResultCode("0");
			return resultVo;
		}
		// McdCampDef campDef = null;
		try {
			// campDef = this.initSyncParameters(request); // 初始化参数信息
			JSONObject object = new JSONObject();
			object.put("subitemId",campDef.getSubitemId());
			if ("1".equals(campDef.getApproveResult()+"")){
				log.info(" 审批通过: "+ campDef.getSubitemId());
				//审批通过
				enterpriseApproveService.updateCampInfo(campDef); // 修改策略状态及生成任务数据
			}

			resultVo.setData(object);
			resultVo.setResultInfo("OK");
			resultVo.setResultCode("0");

		} catch (Exception e) {
			log.error("接口调用失败", e);
			resultVo.setResultCode("1");
			resultVo.setResultInfo(e.getMessage());
			return resultVo;
		}
		return resultVo;
	}



}
