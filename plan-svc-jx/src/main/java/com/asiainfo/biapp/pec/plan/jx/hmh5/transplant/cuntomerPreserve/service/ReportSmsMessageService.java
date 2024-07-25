package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.controller.reqParam.CustomerPreserveDetailParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.model.ReportSmsMessage;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.service.impl.resultInfo.CustomerPreserveDetailResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户通客户接触维系表 服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-13
 */
public interface ReportSmsMessageService extends IService<ReportSmsMessage> {

    Page<CustomerPreserveDetailResultInfo> selectCustomerPreserveDetail(CustomerPreserveDetailParam param);

    void exportCustomerPreserveDetail(CustomerPreserveDetailParam param);

}
