package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;


import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.McdFrontUsageDao;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontUsageService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageDetailInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageOppoHandleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageSummaryInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class McdFrontUsageServiceImpl implements McdFrontUsageService {

    @Resource
    private McdFrontUsageDao mcdFrontUsageDao;


    @Override
    public IPage<McdFrontUsageInfo> queryCityUsageInfo(McdFrontUsageCityQuery req) {
        Page page = new Page();
        page.setSize(req.getPageSize());
        page.setCurrent(req.getCurrentPage());
        return mcdFrontUsageDao.queryCityUsageInfo(page,req);
    }

    @Override
    public IPage<McdFrontUsageInfo> queryChannelUsageInfo(McdFrontUsageChannelQuery req) {
        Page page = new Page();
        page.setSize(req.getPageSize());
        page.setCurrent(req.getCurrentPage());
        return mcdFrontUsageDao.queryChannelUsageInfo(page,req);
    }

    @Override
    public IPage<McdFrontUsageInfo> queryGridUsageInfo(McdFrontUsageGridQuery req) {
        Page page = new Page();
        page.setSize(req.getPageSize());
        page.setCurrent(req.getCurrentPage());
        return mcdFrontUsageDao.queryGridUsageInfo(page,req);
    }

    @Override
    public IPage<McdFrontUsageInfo> queryCountyUsageInfo(McdFrontUsageCountyQuery req) {
        Page page = new Page();
        page.setSize(req.getPageSize());
        page.setCurrent(req.getCurrentPage());
        return mcdFrontUsageDao.queryCountyUsageInfo(page,req);
    }


    @Override
    public IPage<McdFrontUsageSummaryInfo> queryCampsegHandleSummary(McdFrontUsageQuery req) {
        Page page = new Page();
        page.setSize(req.getPageSize());
        page.setCurrent(req.getCurrentPage());
        return mcdFrontUsageDao.queryCampsegHandleSummary(page,req);
    }


    @Override
    public IPage<McdFrontUsageDetailInfo> queryCampsegHandleDetail(McdFrontUsageQuery req) {
        Page page = new Page();
        page.setSize(req.getPageSize());
        page.setCurrent(req.getCurrentPage());
        return mcdFrontUsageDao.queryCampsegHandleDetail(page,req);
    }

    @Override
    public IPage<McdFrontUsageOppoHandleInfo> queryOppoCampCityHandle(McdFrontUsageQuery req) {
        Page page = new Page();
        page.setSize(req.getPageSize());
        page.setCurrent(req.getCurrentPage());
        return mcdFrontUsageDao.queryOppoCampCityHandle(page,req);
    }
}
