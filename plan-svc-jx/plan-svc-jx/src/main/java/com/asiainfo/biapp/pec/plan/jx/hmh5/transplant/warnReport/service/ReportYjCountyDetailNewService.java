package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjCountyDetailNew;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjCountyResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户通预警报表（区县） 服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
public interface ReportYjCountyDetailNewService extends IService<ReportYjCountyDetailNew> {

    Page<ReportYjCountyResultInfo> selectCountyWarnReport(Integer pageNum, Integer pageSize, String statDate);

    void exportCountyWarnReport(String statDate);

}
