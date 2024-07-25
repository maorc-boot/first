package com.asiainfo.biapp.pec.plan.jx.cep.service;


import com.asiainfo.biapp.pec.plan.jx.cep.req.EventSyncRequestBO;

public interface ICepSysEventService {
    /**
     * 事件定义信息同步接口
     * @param requestBO
     */
    public void syncEvent(EventSyncRequestBO requestBO);
}
