package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjGridDetailNew;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjGridResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户通预警报表（网格） 服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
public interface ReportYjGridDetailNewService extends IService<ReportYjGridDetailNew> {

    Page<ReportYjGridResultInfo> selectGridWarnReport(Integer pageNum, Integer pageSize, String statDate);

    void exportGridWarnReport(String statDate);
}
