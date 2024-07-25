package com.asiainfo.biapp.pec.plan.jx.cep.service.impl;


import com.asiainfo.biapp.pec.plan.jx.cep.dao.ICepSysEventDao;
import com.asiainfo.biapp.pec.plan.jx.cep.req.EventSyncRequestBO;
import com.asiainfo.biapp.pec.plan.jx.cep.service.ICepSysEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ICepSysEventServiceImpl implements ICepSysEventService {

    @Autowired
    private ICepSysEventDao iCepSysEventDao;

    @Override
    public void syncEvent(EventSyncRequestBO requestBO) {
        //获取事件编码 --ID
        String eventCode = requestBO.getEventCode();
        //获取事件名称 --NAME
        String eventName = requestBO.getEventName();
        //获取事件类型 --
        String eventType = requestBO.getEventTypeId() =="" ? "999":requestBO.getEventTypeId();
        //获取创建人ID --CREATE_USER
        String createUserId = requestBO.getCreateUserId();
        //获取创建时间 --CREATE_TIME
        String createTime = requestBO.getCreateTime();

        //cep_rule表添加数据
        iCepSysEventDao.insertCepRule(requestBO);
        //CEP_RULE_AUTHORITY 表添加数据
        iCepSysEventDao.insertCepRuleAuthority(requestBO);
        //CEP_DIM_EVENT_CLASS 判断id是否存在
        List<Map<String, Object>> list = iCepSysEventDao.selectCepDimEvent(requestBO);
        if(list.size() <= 0){
            //CEP_DIM_EVENT_CLASS表添加数据
            iCepSysEventDao.insertCepDimEvent(requestBO);
        }
    }
}
