package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.controller;


import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.controller.reqParam.CustomerPreserveDetailParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.service.ReportSmsMessageService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.service.impl.resultInfo.CustomerPreserveDetailResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 客户通客户接触维系表 前端控制器
 * </p>
 *
 * @author chenlin
 * @since 2023-06-13
 */
@RestController
@RequestMapping("/reportSmsMessage")
@Slf4j
@Api(tags = "客户通-客户接触维系情况")
@DataSource("khtmanageusedb")
public class ReportSmsMessageController {

    @Autowired
    private ReportSmsMessageService smsMessageService;

    @ApiOperation("根据条件查询客户通客户接触维系情况")
    @GetMapping
    public ActionResponse<Page<CustomerPreserveDetailResultInfo>> selectCustomerPreserveDetail(@Validated CustomerPreserveDetailParam param) {
        String userId = UserUtilJx.getUserId();
        log.info("userId为：{}的用户正在查询客户接触维系详情！", userId);
        return ActionResponse.getSuccessResp(smsMessageService.selectCustomerPreserveDetail(param));
    }

    @ApiOperation("根据条件导出客户通客户接触维系情况")
    @GetMapping("/export")
    public void exportCustomerPreserveDetail(@Validated CustomerPreserveDetailParam param) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户id：{}正在导出客户接触维系详情报表，导出日期为：{}", user.getUserId(), param.getOpDate());
        smsMessageService.exportCustomerPreserveDetail(param);
    }


}

