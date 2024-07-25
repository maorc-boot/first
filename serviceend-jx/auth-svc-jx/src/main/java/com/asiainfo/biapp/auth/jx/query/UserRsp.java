package com.asiainfo.biapp.auth.jx.query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "USERRSP")
public class UserRsp {
	
	@XmlElement(name = "HEAD")
	private Head head;
	
	@XmlElement(name = "BODY")
	private UserRspBody body;
	
	public UserRsp() {
		
	}

	public UserRsp(Head head, UserRspBody body) {
		this.head = head;
		this.body = body;
	}

	@XmlTransient
	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	@XmlTransient
	public UserRspBody getBody() {
		return body;
	}

	public void setBody(UserRspBody body) {
		this.body = body;
	}
}
