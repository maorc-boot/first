package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;

import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.IMcdBasiTermMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdCityCountryQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.IMcdBasiTermService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class McdBasiTermServiceImpl implements IMcdBasiTermService {

    @Autowired
    private IMcdBasiTermMapper mcdBasiTermMapper;

    @Override
    public List<McdGridVo> getDimGrid(McdCityCountryQuery query) {
        return mcdBasiTermMapper.getDimGrid(query);
    }

    @Override
    public List<McdCountryVo> getDimCounty(String cityId) {
        return mcdBasiTermMapper.getDimCounty(cityId);
    }

    @Override
    public List<McdCityVo> getDimcity() {
        return mcdBasiTermMapper.getDimcity();
    }

    @Override
    public List<McdScenarioVo> getScenario() {
        return mcdBasiTermMapper.getScenario();
    }

    @Override
    public List<McdScenarioTypeVo> getModuleTypes() {
        return mcdBasiTermMapper.queryScenarioModuleType();
    }

    /**
     * 查询客户权限（查询外呼日志的权限，不同权限查询出不同的数据）
     *
     * @param userId
     * @return
     */
    @Override
    public List<McdCallPermissConfVo> getCallLogPermissionConf(String userId) {
        return mcdBasiTermMapper.getCallLogPermissionConf(userId);
    }

    /**
     * 查询客户权限（查询外呼日志的权限，不同权限查询出不同的数据）
     *
     * @param userId
     * @return
     */
    @Override
    public List<McdCallPermissConfVo> getReportUserPermission(String userId) {
        return mcdBasiTermMapper.getReportUserPermission(userId);
    }

    /**
     * 能效预警数据信息
     *
     * @param query
     * @return
     */
    @Override
    public List<McdAlarmVo> getReportEnergyAlarm(McdCityCountryQuery query) {
        return mcdBasiTermMapper.getReportEnergyAlarm(query);
    }

    /**
     * 轨迹预警数据信息
     *
     * @param query
     * @return
     */
    @Override
    public List<McdAlarmVo> getReportTraceAlarm(McdCityCountryQuery query) {
        return mcdBasiTermMapper.getReportTraceAlarm(query);
    }
}
