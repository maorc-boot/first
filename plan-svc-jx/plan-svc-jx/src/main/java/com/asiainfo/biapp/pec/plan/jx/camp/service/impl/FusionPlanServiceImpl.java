package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.dao.FusionPlanDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.FusionPlan;
import com.asiainfo.biapp.pec.plan.jx.camp.service.FusionPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RefreshScope
public class FusionPlanServiceImpl extends ServiceImpl<FusionPlanDao, FusionPlan> implements FusionPlanService {

    @Autowired
    private FusionPlanDao fusionPlanDao;

}
