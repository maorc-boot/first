package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;


import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.McdFrontKeyMonitorDao;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontKeyMonitorModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontKeyMonitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author ranpf
 * @date 2023-2-17
 * @description
 */
@Service
public class McdFrontKeyMonitorServiceImpl extends ServiceImpl<McdFrontKeyMonitorDao, McdFrontKeyMonitorModel> implements McdFrontKeyMonitorService {

    @Resource
    private McdFrontKeyMonitorDao mcdFrontKeyMonitorDao;


}
