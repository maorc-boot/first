package com.asiainfo.biapp.pec.plan.jx.hmh5.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdCityCountryQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.*;

import java.util.List;
import java.util.Map;

public interface IMcdBasiTermService {

    /**
     * 地市下区县信息
     *
     * @param cityId
     * @return
     */
    List<McdCountryVo> getDimCounty(String cityId);

    /**
     * 区县下网格信息
     *
     * @param query
     * @return
     */
    List<McdGridVo> getDimGrid(McdCityCountryQuery query);

    /**
     * 地市信息
     *
     * @return
     */
    List<McdCityVo> getDimcity();

    /**
     * 业务场景信息
     *
     * @return
     */
    List<McdScenarioVo> getScenario();

    /**
     * 业务场景模块类型信息
     *
     * @return
     */
    List<McdScenarioTypeVo> getModuleTypes();

    /**
     * 查询客户权限（查询外呼日志的权限，不同权限查询出不同的数据）
     *
     * @param userId
     * @return
     */
    List<McdCallPermissConfVo> getCallLogPermissionConf(String userId);


    /**
     * 查询报表客户权限（查询预警的权限，不同权限查询出不同的数据）
     *
     * @param userId
     * @return
     */
    List<McdCallPermissConfVo> getReportUserPermission(String userId);

    /**
     * 能效预警数据信息
     *
     * @param query
     * @return
     */
    List<McdAlarmVo> getReportEnergyAlarm(McdCityCountryQuery query);

    /**
     * 轨迹报表预警数据信息
     *
     * @param query
     * @return
     */
    List<McdAlarmVo> getReportTraceAlarm(McdCityCountryQuery query);
}
