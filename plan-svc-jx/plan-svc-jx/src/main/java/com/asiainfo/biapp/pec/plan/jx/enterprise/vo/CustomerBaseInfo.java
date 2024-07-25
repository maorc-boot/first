package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

/**
 * 成员画像-基础信息
 *
 * @author admin
 * @version 1.0
 * @date 2022/8/3 15:00
 */
public class CustomerBaseInfo {
	//集团成员id
	private String memberId = "-";
	//成员名称
	private String memberName = "-";
	//是否为关键人
	private String isKey = "-";
	//是否为联系人
	private String isContact = "-";
	//联系方式
	private String telNo = "-";
	//订购套餐名称
	private String packageName = "-";
	//合约到期时间
	private String expire = "-";
	//进三个月arpu
	private String arpu = "-";
	//是否5G终端
	private String is5G = "-";
	//终端入网年龄
	private String netAge = "-";
	//是否5G套餐
	private String is5GPackage = "-";

	public CustomerBaseInfo() {
	}

	public CustomerBaseInfo(String memberId, String memberName, String isKey, String isContact, String telNo, String packageName, String expire, String arpu, String is5G, String netAge, String is5GPackage) {
		this.memberId = memberId;
		this.memberName = memberName;
		this.isKey = isKey;
		this.isContact = isContact;
		this.telNo = telNo;
		this.packageName = packageName;
		this.expire = expire;
		this.arpu = arpu;
		this.is5G = is5G;
		this.netAge = netAge;
		this.is5GPackage = is5GPackage;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getIsKey() {
		return isKey;
	}

	public void setIsKey(String isKey) {
		this.isKey = isKey;
	}

	public String getIsContact() {
		return isContact;
	}

	public void setIsContact(String isContact) {
		this.isContact = isContact;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public String getArpu() {
		return arpu;
	}

	public void setArpu(String arpu) {
		this.arpu = arpu;
	}

	public String getIs5G() {
		return is5G;
	}

	public void setIs5G(String is5G) {
		this.is5G = is5G;
	}

	public String getNetAge() {
		return netAge;
	}

	public void setNetAge(String netAge) {
		this.netAge = netAge;
	}

	public String getIs5GPackage() {
		return is5GPackage;
	}

	public void setIs5GPackage(String is5GPackage) {
		this.is5GPackage = is5GPackage;
	}
}
