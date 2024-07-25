package com.asiainfo.biapp.auth.jx.query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class UserRspBody {
	/**
	 * 返回消息
	 */
	@XmlElement(name = "RSP")
	private String rsp;
	
	/**
	 * 主帐号登录名
	 */
	@XmlElement(name = "MAINACCTID")
	private String mainAcctId;

	/**
	 * 从帐号登录名
	 */
	@XmlElement(name = "APPACCTID")
	private String appAcctId;

	public UserRspBody() {
		
	}

	public UserRspBody(String rsp, String mainAcctId, String appAcctId) {
		this.rsp = rsp;
		this.mainAcctId = mainAcctId;
		this.appAcctId = appAcctId;
	}

	@XmlTransient
	public String getRsp() {
		return rsp;
	}

	public void setRsp(String rsp) {
		this.rsp = rsp;
	}

	@XmlTransient
	public String getMainAcctId() {
		return mainAcctId;
	}

	public void setMainAcctId(String mainAcctId) {
		this.mainAcctId = mainAcctId;
	}

	@XmlTransient
	public String getAppAcctId() {
		return appAcctId;
	}

	public void setAppAcctId(String appAcctId) {
		this.appAcctId = appAcctId;
	}
}
