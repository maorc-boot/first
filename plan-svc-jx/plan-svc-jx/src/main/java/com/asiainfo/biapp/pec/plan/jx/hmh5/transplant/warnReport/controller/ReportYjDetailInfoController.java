package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.controller.reqParam.SelectWarnDetailParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.ReportYjDetailInfoService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjDetailResultInfo;
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
 * 客户通预警报表（明细） 前端控制器
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@RestController
@RequestMapping("/reportYjDetailInfo")
@Validated
@Api(tags = "客户通-客户通预警情况-客户通预警报表（明细）")
@Slf4j
@DataSource("khtmanageusedb")
public class ReportYjDetailInfoController {

    @Autowired
    private ReportYjDetailInfoService yjDetailInfoService;

    @ApiOperation("根据条件查询{明细}客户通预警报表")
    @GetMapping
    public ActionResponse<Page<ReportYjDetailResultInfo>> selectDetailWarnReport(@Validated SelectWarnDetailParam param) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户id：{}正在查看客户通预警明细报表，查询日期为：{}", user.getUserId(), param.getStatDate());
        if (ObjectUtil.isNotEmpty(param.getStatDate())) {
            param.setStatDateStr(DateUtil.format(param.getStatDate(), "yyyyMMdd"));
        }
        Page<ReportYjDetailResultInfo> detailWarnReports = yjDetailInfoService.selectDetailWarnReport(param);
        return ActionResponse.getSuccessResp(detailWarnReports);
    }


    @ApiOperation("根据条件导出{明细}客户通预警报表")
    @GetMapping("/export")
    public void exportDetailWarnReport(@Validated SelectWarnDetailParam param) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户id：{}正在导出客户通预警明细报表，导出日期为：{}", user.getUserId(), param.getStatDate());
        if (ObjectUtil.isNotEmpty(param.getStatDate())) {
            param.setStatDateStr(DateUtil.format(param.getStatDate(), "yyyyMMdd"));
        }
        yjDetailInfoService.exportDetailWarnReport(param);
    }

}

