package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.service.impl;

import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.controller.reqParam.CustomerPreserveDetailParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.mapper.ReportSmsMessageMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.model.ReportSmsMessage;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.service.ReportSmsMessageService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.service.impl.resultInfo.CustomerPreserveDetailResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils.ExportUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户通客户接触维系表 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-13
 */
@Service
public class ReportSmsMessageServiceImpl extends ServiceImpl<ReportSmsMessageMapper, ReportSmsMessage> implements ReportSmsMessageService {

    @Override
    public Page<CustomerPreserveDetailResultInfo> selectCustomerPreserveDetail(CustomerPreserveDetailParam param) {
        return baseMapper.selectCustomerPreserveDetail(
                new Page(param.getPageNum(), param.getPageSize()),
                param
        );
    }

    @Override
    public void exportCustomerPreserveDetail(CustomerPreserveDetailParam param) {
        List<Map<String, Object>> customerPreserveDetails = baseMapper.exportCustomerPreserveDetail(param);
        if (customerPreserveDetails.size() == 0)
            throw new BaseException("没有符合条件的客户接触维系数据，无法导出！");
        ExportUtil.mapToXlsx("客户接触维系情况报表", true, customerPreserveDetails);
    }
}
