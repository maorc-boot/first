package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdCityCountryQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMcdBasiTermMapper {


    /**
     * 区县下网格信息
     *
     * @param query
     * @return
     */
    List<McdGridVo> getDimGrid(@Param("query") McdCityCountryQuery query);

    /**
     * 地市下区县信息
     *
     * @param cityId
     * @return
     */
    List<McdCountryVo> getDimCounty(@Param("cityId") String cityId);

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
     * 业务场景信息
     *
     * @return
     */
    List<McdScenarioTypeVo> queryScenarioModuleType();

    /**
     * 查询客户权限（查询外呼日志的权限，不同权限查询出不同的数据）
     *
     * @param userId
     * @return
     */
    List<McdCallPermissConfVo> getCallLogPermissionConf(@Param("userId") String userId);

    /**
     * 查询报表客户权限（查询预警的权限，不同权限查询出不同的数据）
     *
     * @param userId
     * @return
     */
    List<McdCallPermissConfVo> getReportUserPermission(@Param("userId") String userId);

    /**
     * 能效预警数据信息
     *
     * @param query
     * @return
     */
    List<McdAlarmVo> getReportEnergyAlarm(@Param("query") McdCityCountryQuery query);

    /**
     * 轨迹预警数据信息
     *
     * @param query
     * @return
     */
    List<McdAlarmVo> getReportTraceAlarm(@Param("query") McdCityCountryQuery query);
}
