package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.controller.reqParam.SelectWarnDetailParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjDetailResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjDetailInfo;

/**
 * <p>
 * 客户通预警报表（明细） 服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
public interface ReportYjDetailInfoService extends IService<ReportYjDetailInfo> {

    Page<ReportYjDetailResultInfo> selectDetailWarnReport(SelectWarnDetailParam param);

    void exportDetailWarnReport(SelectWarnDetailParam param);
}
