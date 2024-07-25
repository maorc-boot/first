package com.asiainfo.biapp.pec.plan.jx.user.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class UserModifyReqBody {

    @XmlElement(name = "OPERATORID")
    private String operatorId;

    @XmlElement(name = "OPERATORPWD")
    private String operatorPwd;

    @XmlElement(name = "MODIFYMODE")
    private String modifyMode;

    @XmlElement(name = "USERINFO")
    private UserInfo4A userInfo4A;

    public UserModifyReqBody() {
    }

    @XmlTransient
    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    @XmlTransient
    public String getOperatorPwd() {
        return operatorPwd;
    }

    public void setOperatorPwd(String operatorPwd) {
        this.operatorPwd = operatorPwd;
    }

    @XmlTransient
    public String getModifyMode() {
        return modifyMode;
    }

    public void setModifyMode(String modifyMode) {
        this.modifyMode = modifyMode;
    }

    @XmlTransient
    public UserInfo4A getUserInfo() {
        return userInfo4A;
    }

    public void setUserInfo(UserInfo4A userInfo4A) {
        this.userInfo4A = userInfo4A;
    }

    @Override
    public String toString() {
        return "UserModifyReqBody{" +
                "operatorId='" + operatorId + '\'' +
                ", operatorPwd='" + operatorPwd + '\'' +
                ", modifyMode='" + modifyMode + '\'' +
                ", userInfo4A=" + userInfo4A +
                '}';
    }
}
