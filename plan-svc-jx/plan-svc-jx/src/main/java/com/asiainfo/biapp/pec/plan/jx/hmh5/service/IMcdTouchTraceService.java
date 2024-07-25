package com.asiainfo.biapp.pec.plan.jx.hmh5.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.model.ReportYjDetailRecording;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdAlarmReportQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

public interface IMcdTouchTraceService extends IService<ReportYjDetailRecording> {

    /**
     * 外呼轨迹查询
     *
     * @param query
     * @return
     */
    IPage queryTouchTraceList(McdAlarmReportQuery query);

    /**
     * 外呼轨迹明细导出
     *
     * @param query
     * @param response
     */
    String exportTraceDetails(McdAlarmReportQuery query, HttpServletResponse response);
}
