package com.asiainfo.biapp.pec.plan.jx.enterprise.enums;

/**
 * 集团画像枚举
 */
public enum GroupPortraitEnum {
	GROUP_PORTRAIT_GROUPINFO("ECC9D2A5AFDB5FDA8E636BD3468BFF36","groupInfo","集团画像-集团信息"),
	GROUP_PORTRAIT_MEMBERINFO("6DB4BA02F187089D816F69E10CE81654","memberInfo","集团画像-集团成员信息"),
	GROUP_PORTRAIT_MSGINFO("ECC9D2A5AFDB5FDACC27D799A652F216","msgInfo","集团画像-集团信息化业务情况"),
	GROUP_PORTRAIT_PREWARNINGINFO("A8B71765633A6AD15A711F89E6DCC3E0","prewarningInfo","集团画像-预警信息"),
	GROUP_PORTRAIT_OPPORTUNITYINFO("6DB4BA02F187089D4E97A6973C19755C","opportunityInfo","集团画像-商机信息"),
	GROUP_PORTRAIT_POLICYINFO("6DB4BA02F187089DA840857FD6F6B762","policyInfo","集团画像-集团客户政策信息"),
	GROUP_PORTRAIT_CHARTINFO("6DB4BA02F187089DAF05989D33C2725F","chartInfo","集团画像-集团成员业务渗透信息"),

	//集团基础信息
	GROUP_PORTRAIT_GROUPINFO_GROUPCODE("LJX0018808","groupCode","集团编码"),
	GROUP_PORTRAIT_GROUPINFO_GROUPNAME("LJX0014285","groupName","集团名称"),
	GROUP_PORTRAIT_GROUPINFO_MANAGERNAME("LJX0014295","managerName","客户经理名称"),
	GROUP_PORTRAIT_GROUPINFO_KEYPERSON("LJX0022687","keyPerson","关键人"),
	GROUP_PORTRAIT_GROUPINFO_VALUEGRADE("LJX0014284","valueGrade","集团价值等级"),
	GROUP_PORTRAIT_GROUPINFO_ADDRESS("LJX0014287","address","地址"),
	GROUP_PORTRAIT_GROUPINFO_INDUSTRYATTR("LJX0014283","industryAttr","行业属性");

	private String sceneCode;
	private String sceneName;
	private String sceneDesc;

	GroupPortraitEnum(String sceneCode, String sceneName, String sceneDesc) {
		this.sceneCode = sceneCode;
		this.sceneName = sceneName;
		this.sceneDesc = sceneDesc;
	}

	public String getSceneCode() {
		return sceneCode;
	}

	public void setSceneCode(String sceneCode) {
		this.sceneCode = sceneCode;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public String getSceneDesc() {
		return sceneDesc;
	}

	public void setSceneDesc(String sceneDesc) {
		this.sceneDesc = sceneDesc;
	}
}
