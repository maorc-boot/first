package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.dao.SeriesPlanDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.SeriesPlan;
import com.asiainfo.biapp.pec.plan.jx.camp.service.SeriesPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;



@Service
@Slf4j
@RefreshScope
public class SeriesPlanServiceImpl extends ServiceImpl<SeriesPlanDao,SeriesPlan> implements SeriesPlanService {
    @Autowired
    private SeriesPlanDao seriesPlanDao;


}
