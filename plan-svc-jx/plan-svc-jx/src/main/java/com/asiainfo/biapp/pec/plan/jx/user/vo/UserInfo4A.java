package com.asiainfo.biapp.pec.plan.jx.user.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class UserInfo4A {
    @XmlElement(name = "USERID")
    private String userId;

    @XmlElement(name = "LOGINNO")
    private String loginNo;

    @XmlElement(name = "USERNAME")
    private String userName;

    @XmlElement(name = "ORGID")
    private String orgId;

    @XmlElement(name = "EMAIL")
    private String email;

    @XmlElement(name = "MOBILE")
    private String mobile;

    @XmlElement(name = "PASSWORD")
    private String password;

    @XmlElement(name = "STATUS")
    private String status;

    @XmlElement(name = "EFFECTDATE")
    private String effectDate;

    @XmlElement(name = "EXPIREDATE")
    private String expireDate;

    @XmlElement(name = "REMARK")
    private String remark;

    @XmlElement(name = "DUTY")
    private String duty;

    @XmlElement(name = "OPERTYPE")
    private String operType;

    @XmlElement(name = "OPERLEVEL")
    private String operLevel;

    @XmlElement(name = "DUTYLEVEL")
    private String dutyLevel;

    public UserInfo4A() {
    }

    @XmlTransient
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @XmlTransient
    public String getLoginNo() {
        return loginNo;
    }

    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
    }

    @XmlTransient
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @XmlTransient
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @XmlTransient
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @XmlTransient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    @XmlTransient
    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @XmlTransient
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlTransient
    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    @XmlTransient
    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    @XmlTransient
    public String getOperLevel() {
        return operLevel;
    }

    public void setOperLevel(String operLevel) {
        this.operLevel = operLevel;
    }

    @XmlTransient
    public String getDutyLevel() {
        return dutyLevel;
    }

    public void setDutyLevel(String dutyLevel) {
        this.dutyLevel = dutyLevel;
    }

    @Override
    public String toString() {
        return "UserInfo4A{" +
                "userId='" + userId + '\'' +
                ", loginNo='" + loginNo + '\'' +
                ", userName='" + userName + '\'' +
                ", orgId='" + orgId + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", effectDate='" + effectDate + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", remark='" + remark + '\'' +
                ", duty='" + duty + '\'' +
                ", operType='" + operType + '\'' +
                ", operLevel='" + operLevel + '\'' +
                ", dutyLevel='" + dutyLevel + '\'' +
                '}';
    }
}
