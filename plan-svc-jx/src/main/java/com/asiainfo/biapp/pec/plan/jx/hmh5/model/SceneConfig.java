package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

/**
 * 场景渠道配置表
 *
 * @author admin
 * @version 1.0
 * @date 2022-09-01 14:59
 */
public class SceneConfig {
	//组合sceneCode+rulesType
	private String sceneRule;
	//场景编码
	private String sceneCode;
	//场景类型
	private String rulesType;
	//渠道编码
	private String channelCode;

	public SceneConfig() {
	}

	public SceneConfig(String sceneRule, String sceneCode, String rulesType, String channelCode) {
		this.sceneRule = sceneRule;
		this.sceneCode = sceneCode;
		this.rulesType = rulesType;
		this.channelCode = channelCode;
	}

	public String getSceneRule() {
		return sceneRule;
	}

	public void setSceneRule(String sceneRule) {
		this.sceneRule = sceneRule;
	}

	public String getSceneCode() {
		return sceneCode;
	}

	public void setSceneCode(String sceneCode) {
		this.sceneCode = sceneCode;
	}

	public String getRulesType() {
		return rulesType;
	}

	public void setRulesType(String rulesType) {
		this.rulesType = rulesType;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
}
