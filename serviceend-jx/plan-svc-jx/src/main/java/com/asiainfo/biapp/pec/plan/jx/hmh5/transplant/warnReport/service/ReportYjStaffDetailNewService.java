package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjStaffDetailNew;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjStaffResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户通预警报表（个人） 服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
public interface ReportYjStaffDetailNewService extends IService<ReportYjStaffDetailNew> {

    Page<ReportYjStaffResultInfo> selectStaffWarnReport(Integer pageNum, Integer pageSize, String statDate);

    void exportStaffWarnReport(String statDate);

}
