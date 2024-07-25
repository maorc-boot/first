package com.asiainfo.biapp.pec.plan.jx.link.service.impl;

import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.link.dao.IMtlStcPlanDao;
import com.asiainfo.biapp.pec.plan.jx.link.service.IMcdPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("mcdPlanService")
public class McdPlanServiceImpl implements IMcdPlanService {

    @Autowired
    protected IMtlStcPlanDao mtlStcPlanDao;

    @Override
    public List<Map<String, Object>> getPlanByCondition(String typeId, String statusId, String keyWords, Pager pager) {
        return mtlStcPlanDao.queryListBySql(typeId, statusId, keyWords,pager);
    }

}
