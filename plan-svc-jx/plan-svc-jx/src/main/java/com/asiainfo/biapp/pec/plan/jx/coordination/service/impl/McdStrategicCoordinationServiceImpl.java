package com.asiainfo.biapp.pec.plan.jx.coordination.service.impl;


import com.asiainfo.biapp.pec.plan.jx.coordination.dao.McdStrategicCoordinationDao;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.McdStrategicCoordinationReq;
import com.asiainfo.biapp.pec.plan.jx.coordination.service.McdStrategicCoordinationService;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.StrategicCoordinationBaseInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class McdStrategicCoordinationServiceImpl implements McdStrategicCoordinationService {


    @Resource
    private McdStrategicCoordinationDao mcdStrategicCoordinationDao;

    @Override
    public Page<StrategicCoordinationBaseInfo> queryStrategicCoordinationBaseInfos(McdStrategicCoordinationReq req) {
        Page page =new Page();
        page.setSize(req.getSize());
        page.setCurrent(req.getCurrent());
        return mcdStrategicCoordinationDao.queryStrategicCoordinationBaseInfos(page,req);
    }

    @Override
    public List<StrategicCoordinationBaseInfo> selectAllStrategicCoordinationList(McdStrategicCoordinationReq req) {
        return mcdStrategicCoordinationDao.selectAllStrategicCoordinationList(req);
    }
}
