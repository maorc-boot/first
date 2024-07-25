package com.asiainfo.biapp.pec.plan.jx.enterprise.service;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.biapp.client.pec.approve.model.User;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.GrouPortraitVo;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.GroupMemberVo;

import java.util.List;
import java.util.Map;

public interface EnterpriseGroupService {

	/**
	 * 获取集团画像信息
	 * @param grouPortraitVo
	 * @return
	 */
	JSONObject getGroupPortraitInfo(GrouPortraitVo grouPortraitVo, User user);

	/**
	 * 获取集团成员画像信息
	 * @param groupMemberVo
	 * @return
	 */
	JSONObject groupMemberDetail(GroupMemberVo groupMemberVo, User user);

	/**
	 * 根据集团ID,指标ID/策略ID，查看阈值指标数据
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<Map<String,String>> queryTargetThreshold(Map<String,Object> param) throws Exception;
}
