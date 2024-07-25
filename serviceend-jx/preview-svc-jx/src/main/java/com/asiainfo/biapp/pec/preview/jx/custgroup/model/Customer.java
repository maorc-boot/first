package com.asiainfo.biapp.pec.preview.jx.custgroup.model;

import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import lombok.Data;

import java.util.Date;

/**
 * @author imcd
 */
@Data
public class Customer {
    /**
     * 客群编号
     */
    private String customerId;
    /**
     * 活动编号
     */
    private String activityId;
    /**
     * 渠道编码
     */
    private String channelId;
    /**
     * 运营位编码
     */
    private String channelAdivId;
    /**
     * 事件编码
     */
    private String eventId;
    /**
     * 系统编码
     */
    private String systemId;
    /**
     * 客群条件树表达式
     */
    private String expression;
    /**
     * bitmap序列化文件路径
     */
    private String bitMapFilePath;

    /**
     * 客户群清单文件名，不带路径
     */
    private String fileName;
    /**
     * 客群对应bitmap（客群清单）
     */
    private IBitMap bitMap;
    /**
     * bitmap数量
     */
    private Integer bitMapCount;
    /**
     * 客群名称
     */
    private String customerName;

    private Date activityStartTime;

    private Date activityEndTime;

    /**
     * 拆分下发比例
     */
    private String splitRate;
    /**
     * 拆分客群id集合
     */
    private String customerIdCollect;
    /**
     * 拆分客群原始客群
     */
    private Integer originCustomer;

    /**
     * 客群最后更新时间
     */
    private Long lastUpdateTime;

}
