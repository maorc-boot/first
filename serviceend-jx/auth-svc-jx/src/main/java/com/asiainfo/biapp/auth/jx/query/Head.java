
package com.asiainfo.biapp.auth.jx.query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author zhangyk7
 * @date 2020/9/8-8:13 下午
 */
public class Head {

    @XmlElement(name = "CODE")
    private String code;

    @XmlElement(name = "SID")
    private String sid;

    @XmlElement(name = "TIMESTAMP")
    private String timestamp;

    @XmlElement(name = "SERVICEID")
    private String serviceId;

    public Head() {
    }

    public Head(String code, String sid, String timestamp, String serviceId) {
        this.code = code;
        this.sid = sid;
        this.timestamp = timestamp;
        this.serviceId = serviceId;
    }

    @XmlTransient
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlTransient
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @XmlTransient
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @XmlTransient
    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
