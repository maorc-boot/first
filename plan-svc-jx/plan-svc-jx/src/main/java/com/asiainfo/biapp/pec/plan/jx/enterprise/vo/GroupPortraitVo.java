package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import com.asiainfo.biapp.pec.plan.jx.enterprise.enums.GroupPortraitEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 集团画像
 *
 * @author admin
 * @version 1.0
 * @date 2023/7/17 17:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupPortraitVo {
	//集团信息
	private GroupBaseInfo groupInfo;
	//集团成员信息
	private List<BaseInfo> memberInfo = new ArrayList<>();
	//集团信息化业务情况
	private List<BaseInfo> msgInfo = new ArrayList<>();
	//预警
	private List<BaseInfo> prewarningInfo = new ArrayList<>();
	//商机
	private List<BaseInfo> opportunityInfo = new ArrayList<>();
	//集团客户政策信息
	private List<BaseInfo> policyInfo = new ArrayList<>();
	//集团成员业务渗透信息
	private List<ChartInfos> chartInfo = new ArrayList<>();
	// 刷新标志
	String refresh;
	/**
	 *  场景编码Map
	 */
	private Map<String,String > sceneCodeMap;

	/**
	 * 初始化场景编码
	 */
	public void initSceneCodeMap() {
		sceneCodeMap = new HashMap<>();
		sceneCodeMap.put(GroupPortraitEnum.GROUP_PORTRAIT_GROUPINFO.getSceneName(), GroupPortraitEnum.GROUP_PORTRAIT_GROUPINFO.getSceneCode());
		sceneCodeMap.put(GroupPortraitEnum.GROUP_PORTRAIT_MEMBERINFO.getSceneName(), GroupPortraitEnum.GROUP_PORTRAIT_MEMBERINFO.getSceneCode());
		sceneCodeMap.put(GroupPortraitEnum.GROUP_PORTRAIT_MSGINFO.getSceneName(), GroupPortraitEnum.GROUP_PORTRAIT_MSGINFO.getSceneCode());
		sceneCodeMap.put(GroupPortraitEnum.GROUP_PORTRAIT_PREWARNINGINFO.getSceneName(), GroupPortraitEnum.GROUP_PORTRAIT_PREWARNINGINFO.getSceneCode());
		sceneCodeMap.put(GroupPortraitEnum.GROUP_PORTRAIT_OPPORTUNITYINFO.getSceneName(), GroupPortraitEnum.GROUP_PORTRAIT_OPPORTUNITYINFO.getSceneCode());
		sceneCodeMap.put(GroupPortraitEnum.GROUP_PORTRAIT_POLICYINFO.getSceneName(), GroupPortraitEnum.GROUP_PORTRAIT_POLICYINFO.getSceneCode());
		sceneCodeMap.put(GroupPortraitEnum.GROUP_PORTRAIT_CHARTINFO.getSceneName(), GroupPortraitEnum.GROUP_PORTRAIT_CHARTINFO.getSceneCode());
	}

}
