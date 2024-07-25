package com.asiainfo.biapp.pec.plan.jx.camp.service;


import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.common.jx.model.CampsegSyncInfo;

import java.util.List;
import java.util.Map;

public interface JxImcdCampSysncListService {

	List<CampsegSyncInfo> syncReadyCampList();

	/**
	 * 通过子活动ID,查询活动详情
	 * @param campId 活动（子）ID
	 * @return
	 */
	CampsegSyncInfo selectCampInfoById( String campId);

	/**
	 * 查询不限定客户群活动
	 * @return
	 */
	Map<String,String> selectNoCustCamp();
}
