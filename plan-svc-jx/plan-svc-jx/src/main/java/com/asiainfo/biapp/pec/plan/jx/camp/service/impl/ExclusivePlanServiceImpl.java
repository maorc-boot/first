package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.dao.ExclusivePlanDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.ExclusivePlan;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ExclusivePlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RefreshScope
public class ExclusivePlanServiceImpl extends ServiceImpl<ExclusivePlanDao,ExclusivePlan> implements ExclusivePlanService {

    @Autowired
    private ExclusivePlanDao exclusivePlanDao;


}
