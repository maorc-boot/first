package com.asiainfo.biapp.pec.plan.jx.enterprise.enums;

/**
 * 成员画像枚举
 */
public enum GroupMemberPortraitEnum {
	GROUP_PORTRAIT_BASEINFO("ECC9D2A5AFDB5FDAA840857FD6F6B762","baseInfo","基础信息"),
	GROUP_PORTRAIT_SUBTOTAL("ECC9D2A5AFDB5FDA956797CA5B48ECBA","subtotal","以下7个分类汇总，公用一个场景编码"),
	GROUP_PORTRAIT_BUSIORDERINFO("100012","busiOrderInfo","业务订购信息"),
	GROUP_PORTRAIT_FAMILYBUSIINFO("100013","familyBusiInfo","家庭业务"),
	GROUP_PORTRAIT_ACTIVITYINFO("100014","activityInfo","活动信息"),
	GROUP_PORTRAIT_ACCOUNTINFO("100015","accountInfo","账单信息"),
	GROUP_PORTRAIT_PERSONALINFO("100016","personalInfo","个人信息"),
	GROUP_PORTRAIT_BUSIUSED("100017","busiUsed","业务使用"),
	GROUP_PORTRAIT_CONSUMPTION("100018","consumption","消费情况"),

	//成员画像-基本属性信息
	GROUP_PORTRAIT_MEMBERID("100019","memberId","集团成员id"),
	GROUP_PORTRAIT_MEMBERNAME("LJX0005504","memberName","成员名称"),
	GROUP_PORTRAIT_ISKEY("LJX0007612","isKey","是否为关键人"),
	GROUP_PORTRAIT_ISCONTACT("100022","isContact","是否为联系人"),
	GROUP_PORTRAIT_TELNO("100023","telNo","联系方式"),
	GROUP_PORTRAIT_PACKAGENAME("LJX0002114","packageName","订购套餐名称"),
	GROUP_PORTRAIT_EXPIRE("LJX0002538","expire","合约到期时间"),
	GROUP_PORTRAIT_ARPU("LJX0012975","arpu","进三个月arpu"),
	GROUP_PORTRAIT_IS5G("LJX0005560","is5G","是否5G终端"),
	GROUP_PORTRAIT_NETAGE("LJX0679790","netAge","终端入网年龄"),
	GROUP_PORTRAIT_IS5GPACKAGE("LJX0005557","is5GPackage","是否5G套餐");

	private String sceneCode;
	private String sceneName;
	private String sceneDesc;

	GroupMemberPortraitEnum(String sceneCode, String sceneName, String sceneDesc) {
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
