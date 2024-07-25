package com.asiainfo.biapp.pec.plan.jx.coordination.service.impl;


import com.asiainfo.biapp.pec.plan.jx.coordination.dao.McdCampCoordinationTaskDao;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCampCoordinationTaskModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.CampCoordinationStatusQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.service.McdCampCoordinationTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class McdCampCoordinationTaskServiceImpl extends ServiceImpl<McdCampCoordinationTaskDao, McdCampCoordinationTaskModel> implements McdCampCoordinationTaskService {


    @Override
    public void modifyCampCoordinationStatusFromAppr(CampCoordinationStatusQuery req) {


    }
}
