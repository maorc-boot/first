package com.asiainfo.biapp.pec.plan.jx.cep.dao;


import com.asiainfo.biapp.pec.plan.jx.cep.req.EventSyncRequestBO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface ICepSysEventDao {

    void insertCepRule(EventSyncRequestBO requestBO);

    void insertCepRuleAuthority(EventSyncRequestBO requestBO);

    List<Map<String, Object>> selectCepDimEvent(EventSyncRequestBO requestBO);

    void insertCepDimEvent(EventSyncRequestBO requestBO);
}

