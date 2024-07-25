package com.asiainfo.biapp.pec.plan.jx.user.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 4A从账号变更请求req
 */
@XmlRootElement(name = "USERMODIFYREQ")
public class UserModifyReq {

    @XmlElement(name = "HEAD")
    private Head head;

    @XmlElement(name = "BODY")
    private UserModifyReqBody body;

    public UserModifyReq() {
    }

    @XmlTransient
    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    @XmlTransient
    public UserModifyReqBody getBody() {
        return body;
    }

    public void setBody(UserModifyReqBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "UserModifyReq{" +
                "head=" + head +
                ", body=" + body +
                '}';
    }
}
