package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.mapper;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.controller.reqParam.CustomerPreserveDetailParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.model.ReportSmsMessage;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.service.impl.resultInfo.CustomerPreserveDetailResultInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户通客户接触维系表 Mapper 接口
 * </p>
 *
 * @author chenlin
 * @since 2023-06-13
 */
public interface ReportSmsMessageMapper extends BaseMapper<ReportSmsMessage> {

    Page<CustomerPreserveDetailResultInfo> selectCustomerPreserveDetail(Page page, @Param("param") CustomerPreserveDetailParam param);

    List<Map<String, Object>> exportCustomerPreserveDetail(@Param("param") CustomerPreserveDetailParam param);
}
