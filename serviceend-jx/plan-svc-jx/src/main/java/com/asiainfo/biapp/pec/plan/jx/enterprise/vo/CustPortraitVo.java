package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 成员画像
 *
 * @author admin
 * @version 1.0
 * @date 2022/8/3 14:49
 */
public class CustPortraitVo {
	//基础信息
	private CustomerBaseInfo baseInfo;
	//业务订购信息
	private List<BaseInfo> busiOrderInfo = new ArrayList<>();
	//家庭业务
	private List<BaseInfo> familyBusiInfo = new ArrayList<>();
	//活动信息
	private List<BaseInfo> activityInfo = new ArrayList<>();
	//账单信息
	private List<BaseInfo> accountInfo = new ArrayList<>();
	//个人信息
	private List<BaseInfo> personalInfo = new ArrayList<>();
	//业务使用
	private List<BaseInfo> busiUsed = new ArrayList<>();
	//消费情况
	private List<BaseInfo> consumption = new ArrayList<>();

	public CustPortraitVo() {
	}

	public CustPortraitVo(CustomerBaseInfo baseInfo, List<BaseInfo> busiOrderInfo, List<BaseInfo> familyBusiInfo, List<BaseInfo> activityInfo, List<BaseInfo> accountInfo, List<BaseInfo> personalInfo, List<BaseInfo> busiUsed, List<BaseInfo> consumption) {
		this.baseInfo = baseInfo;
		this.busiOrderInfo = busiOrderInfo;
		this.familyBusiInfo = familyBusiInfo;
		this.activityInfo = activityInfo;
		this.accountInfo = accountInfo;
		this.personalInfo = personalInfo;
		this.busiUsed = busiUsed;
		this.consumption = consumption;
	}

	public CustomerBaseInfo getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(CustomerBaseInfo baseInfo) {
		this.baseInfo = baseInfo;
	}

	public List<BaseInfo> getBusiOrderInfo() {
		return busiOrderInfo;
	}

	public void setBusiOrderInfo(List<BaseInfo> busiOrderInfo) {
		this.busiOrderInfo = busiOrderInfo;
	}

	public List<BaseInfo> getFamilyBusiInfo() {
		return familyBusiInfo;
	}

	public void setFamilyBusiInfo(List<BaseInfo> familyBusiInfo) {
		this.familyBusiInfo = familyBusiInfo;
	}

	public List<BaseInfo> getActivityInfo() {
		return activityInfo;
	}

	public void setActivityInfo(List<BaseInfo> activityInfo) {
		this.activityInfo = activityInfo;
	}

	public List<BaseInfo> getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(List<BaseInfo> accountInfo) {
		this.accountInfo = accountInfo;
	}

	public List<BaseInfo> getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(List<BaseInfo> personalInfo) {
		this.personalInfo = personalInfo;
	}

	public List<BaseInfo> getBusiUsed() {
		return busiUsed;
	}

	public void setBusiUsed(List<BaseInfo> busiUsed) {
		this.busiUsed = busiUsed;
	}

	public List<BaseInfo> getConsumption() {
		return consumption;
	}

	public void setConsumption(List<BaseInfo> consumption) {
		this.consumption = consumption;
	}
}
