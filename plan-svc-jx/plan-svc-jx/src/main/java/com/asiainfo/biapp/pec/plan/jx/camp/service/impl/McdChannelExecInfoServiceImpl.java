package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdChannelExecInfoDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdChannelExecInfoModel;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdChannelExecQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdChannelExecInfoService;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdChannelExecInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class McdChannelExecInfoServiceImpl extends ServiceImpl<McdChannelExecInfoDao, McdChannelExecInfoModel> implements McdChannelExecInfoService {
    @Resource
    private McdChannelExecInfoDao mcdChannelExecInfoDao;
    @Override
    public McdChannelExecInfo queryChannelExecInfo(McdChannelExecQuery req) {
        return mcdChannelExecInfoDao.queryChannelExecInfo(req.getChannelId());
    }

    @Override
    public List<McdChannelExecInfo> queryChannelExecList(McdChannelExecQuery req) {
        return mcdChannelExecInfoDao.queryChannelExecList(req.getCampsegId());
    }

    @Override
    public List<Map<String, Object>> queryCustNumByChannel() {
        return mcdChannelExecInfoDao.queryCustNumByChannel();
    }
}
