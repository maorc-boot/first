package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.ReportYjCountyDetailNewService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjCountyResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * <p>
 * 客户通预警报表（区县） 前端控制器
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@RestController
@RequestMapping("/reportYjCountyDetailNew")
@Validated
@Api(tags = "客户通-客户通预警情况-客户通预警报表（区县）")
@Slf4j
@DataSource("khtmanageusedb")
public class ReportYjCountyDetailNewController {

    @Autowired
    private ReportYjCountyDetailNewService countyDetailNewService;

    @ApiOperation("根据日期查询{区县}客户通预警报表，没有选择日期则查询所有")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "查询的当前页", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "查询的页面大小", required = true, example = "10"),
            @ApiImplicitParam(name = "statDate", value = "查询日期，格式yyyy-MM-dd，没有选择日期则查询所有", example = "2022-10-10"),
    })
    @GetMapping
    public ActionResponse<Page<ReportYjCountyResultInfo>> selectCountyWarnReport(
            @RequestParam("pageNum") @Min(value = 1, message = "页码至少为1！") Integer pageNum,
            @RequestParam("pageSize") @Min(value = 1, message = "查询页大小至少为1！") Integer pageSize,
            @Past(message = "不可选择将来的日期！") @DateTimeFormat(pattern = "yyyy-MM-dd") Date statDate
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户id：{}正在查看区县客户通预警报表，查询日期为：{}", user.getUserId(), statDate);
        String statDateStr = null;
        if (ObjectUtil.isNotEmpty(statDate)) {
            statDateStr = DateUtil.format(statDate, "yyyyMMdd");
        }
        Page<ReportYjCountyResultInfo> countyWarnReports = countyDetailNewService.selectCountyWarnReport(pageNum,pageSize,statDateStr);
        return ActionResponse.getSuccessResp(countyWarnReports);
    }



    @ApiOperation("根据日期导出{区县}客户通预警报表，没有选择日期时导出所有")
    @ApiImplicitParam(name = "statDate", value = "查询日期，格式yyyy-MM-dd，没有选择日期则导出所有", example = "2022-10-10")
    @GetMapping("/export")
    public void exportCountyWarnReport(
            @Past(message = "不可选择将来的日期！") @DateTimeFormat(pattern = "yyyy-MM-dd") Date statDate
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户id：{}正在导出区县客户通预警报表，导出日期为：{}", user.getUserId(), statDate);
        String statDateStr = null;
        if (ObjectUtil.isNotEmpty(statDate)) {
            statDateStr = DateUtil.format(statDate, "yyyyMMdd");
        }
        countyDetailNewService.exportCountyWarnReport(statDateStr);
    }

}

