package com.asiainfo.biapp.pec.plan.jx.hmh5.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdAlarmReportQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.servlet.http.HttpServletResponse;

public interface IMcdAlarmEnergyService {

    /**
     * 外呼轨迹查询
     *
     * @param query
     * @return
     */
    IPage queryAlarmEnergyList(McdAlarmReportQuery query);

    /**
     * 外呼轨迹明细导出
     *
     * @param query
     * @param response
     */
    String exportEnergy(McdAlarmReportQuery query, HttpServletResponse response);
}
