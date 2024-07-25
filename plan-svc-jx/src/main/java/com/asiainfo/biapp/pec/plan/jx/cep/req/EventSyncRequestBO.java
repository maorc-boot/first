package com.asiainfo.biapp.pec.plan.jx.cep.req;

/**
 * @author mamp
 * @date 2021/12/2
 */
public class EventSyncRequestBO {
    /**
     * 事件编码
     */
    private String eventCode;
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 事件类型
     */
    private String eventTypeId;
    /**
     * 事件类型名称
     */
    private String eventTypeName;
    /**
     * 事件描述
     */
    private String eventDesc;
    /**
     * 创建人ID
     */
    private String createUserId;

    /**
     * 创建时间
     */
    private String createTime;

    public EventSyncRequestBO(String eventCode, String eventName, String eventTypeId, String eventTypeName, String eventDesc, String createUserId, String createTime) {
        this.eventCode = eventCode;
        this.eventName = eventName;
        this.eventTypeId = eventTypeId;
        this.eventTypeName = eventTypeName;
        this.eventDesc = eventDesc;
        this.createUserId = createUserId;
        this.createTime = createTime;
    }

    public EventSyncRequestBO() {
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "EventSyncRequestBO{" +
                "eventCode='" + eventCode + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventTypeId='" + eventTypeId + '\'' +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", eventDesc='" + eventDesc + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
