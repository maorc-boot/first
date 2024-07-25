package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjCityDetailNew;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjCityDetailResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户通预警报表（城市） 服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
public interface ReportYjCityDetailNewService extends IService<ReportYjCityDetailNew> {

    Page<ReportYjCityDetailResultInfo> selectCityWarnReport(Integer pageNum, Integer pageSize, String statDate);


    void exportCityWarnReport(String statDate);
}
