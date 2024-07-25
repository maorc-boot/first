package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdSmsSendMonitorDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdSmsSendMonitorModel;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdSmsSendMonitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class McdSmsSendMonitorServiceImpl extends ServiceImpl<McdSmsSendMonitorDao, McdSmsSendMonitorModel> implements McdSmsSendMonitorService {

    @Resource
    private McdSmsSendMonitorDao mcdSmsSendMonitorDao;
    @Override
    public List<Map<String, Object>> querySmsSendBasicInfo() {
        return mcdSmsSendMonitorDao.querySmsSendBasicInfo();
    }

    @Override
    public List<Map<String, Object>> querySmsSendHistoryBasicInfo(String endDate) {
        return mcdSmsSendMonitorDao.querySmsSendHistoryBasicInfo(endDate);
    }
}
