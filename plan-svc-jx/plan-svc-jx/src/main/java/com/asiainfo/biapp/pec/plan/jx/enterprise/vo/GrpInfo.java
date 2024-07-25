package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name="grpInfo")
@Data
public class GrpInfo {
    /**
     * 客群Id
     */
    private String customGroupId;
    /**
     * 客户群名称
     */
    private String customGroupName;
    /**
     * 客户群描述
     */
    private String customGroupDesc;
    /**
     * 规则描述
     */
    private String ruleDesc;
    /**
     * 客户群周期  1：一次性， 2：月周期，3：日周期
     */
    private String updateCycle;
    /**
     * 数据日期 yyyyMMdd
     */

    private String dataDate;
    /**
     * 生效时间 yyyy-MM-dd HH:mm:ss
     */
    private String effectiveTime;
    /**
     * 失效时间 yyyy-MM-dd HH:mm:ss
     */
    private String failTime;
    /**
     * 客群创建用户id
     */
    private String createUserId;
    /**
     * 客户群类型 0:普通; 1:外呼专用客户; 3:政企客户群; 4:网;
     */
    private String custType;
    /**
     * 客群清单文件名
     */
    private String fileName;
    /**
     * 客群清单文件属性项定义
     */
    private List<FileAttrs> fileAttrs;

}
