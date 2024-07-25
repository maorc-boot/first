package com.asiainfo.biapp.pec.plan.jx.system.service.impl;

import com.asiainfo.biapp.pec.plan.jx.system.dao.ISystemUsageDao;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageCityDTO;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageDeptDTO;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageDetail;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsagePersonageDTO;
import com.asiainfo.biapp.pec.plan.jx.system.req.SystemUsageDetailReqQuery;
import com.asiainfo.biapp.pec.plan.jx.system.req.SystemUsageReqQuery;
import com.asiainfo.biapp.pec.plan.jx.system.service.ISystemUsageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SystemUsageServiceImpl implements ISystemUsageService {

    @Resource
    ISystemUsageDao systemUsageDao;
    @Override
    public IPage<SystemUsageCityDTO> querySystemUsageCityList(SystemUsageReqQuery reqQuery) {
        Page page = new Page(reqQuery.getCurrent(),reqQuery.getSize());
        return systemUsageDao.querySystemUsageCityList(page,reqQuery.getBeginTime(),reqQuery.getEndTime());
    }

    @Override
    public IPage<SystemUsageDeptDTO> querySystemUsageDeptList(SystemUsageReqQuery reqQuery) {
        Page page = new Page(reqQuery.getCurrent(),reqQuery.getSize());
        return systemUsageDao.querySystemUsageDeptList(page,reqQuery.getBeginTime(),reqQuery.getEndTime());
    }

    @Override
    public IPage<SystemUsagePersonageDTO> querySystemUsagePersonageList(SystemUsageReqQuery reqQuery) {
        Page page = new Page(reqQuery.getCurrent(),reqQuery.getSize());
        return systemUsageDao.querySystemUsagePersonageList(page,reqQuery.getBeginTime(),reqQuery.getEndTime());
    }


    @Override
    public IPage<SystemUsageDetail> getUsageCityDetailList(SystemUsageDetailReqQuery reqQuery) {
        Page page = new Page(reqQuery.getCurrent(),reqQuery.getSize());
        return systemUsageDao.getUsageCityDetailList(page,reqQuery);
    }

    @Override
    public IPage<SystemUsageDetail> getUsageDeptDetailList(SystemUsageDetailReqQuery reqQuery) {
        Page page = new Page(reqQuery.getCurrent(),reqQuery.getSize());
        return systemUsageDao.getUsageDeptDetailList(page,reqQuery);
    }

    @Override
    public IPage<SystemUsageDetail> getUsagePersonageDetailList(SystemUsageDetailReqQuery reqQuery) {
        Page page = new Page(reqQuery.getCurrent(),reqQuery.getSize());
        return systemUsageDao.getUsagePersonageDetailList(page,reqQuery);
    }
}
