package com.asiainfo.biapp.pec.eval.jx.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.eval.jx.constants.SpecialNumberJx;
import com.asiainfo.biapp.pec.eval.jx.model.ReportChannelExprDt;
import com.asiainfo.biapp.pec.eval.jx.model.ReportProductExprDt;
import com.asiainfo.biapp.pec.eval.jx.req.ReportFormReq;
import com.asiainfo.biapp.pec.eval.jx.service.ReportChannelService;
import com.asiainfo.biapp.pec.eval.jx.service.ReportFormService;
import com.asiainfo.biapp.pec.eval.jx.utils.ExportExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * description: 报表下载控制层
 *
 * @author: lvchaochao
 * @date: 2023/1/16
 */
@Api(tags = {"江西：报表下载"}, value = "江西：报表下载")
@RequestMapping("/reportform/download")
@RestController
@Slf4j
public class ReportFormController {

    @Autowired
    private ReportFormService reportFormService;

    @Autowired
    private ReportChannelService reportChannelService;

    @Autowired
    private ExportExcelUtil exportExcelUtil;

    @ApiOperation(value = "获取产品分类订购报表列表数据", notes = "获取产品分类订购报表列表数据")
    @PostMapping("/getPlanOrderReportList")
    public IPage<ReportProductExprDt> getPlanOrderReportList (@RequestBody ReportFormReq req) {
        log.info("获取产品分类订购报表列表数据入参：{}", JSONUtil.toJsonStr(req));
        Page<ReportProductExprDt> pager = new Page<>(req.getCurrent(), req.getSize());
        return reportFormService.page(pager, Wrappers.<ReportProductExprDt>lambdaQuery()
                .eq(StrUtil.isNotEmpty(req.getOpTime()), ReportProductExprDt::getOpTime, req.getOpTime())
                .orderByAsc(ReportProductExprDt::getCityId));
    }

    @ApiOperation(value = "获取产品分渠道订购报表列表数据", notes = "获取产品分渠道订购报表列表数据")
    @PostMapping("/getPlanChannelReportList")
    public IPage<ReportChannelExprDt> getPlanChannelReportList (@RequestBody ReportFormReq req) {
        log.info("获取产品分渠道订购报表列表数据入参：{}", JSONUtil.toJsonStr(req));
        Page<ReportChannelExprDt> pager = new Page<>(req.getCurrent(), req.getSize());
        return reportChannelService.getOrderProductDMDataByPage(pager, req);
    }

    @ApiOperation(value = "导出产品分类订购报表列表数据", notes = "导出产品分类订购报表列表数据")
    @PostMapping("/exportOrderProductDMData")
    public void exportOrderProductDMData(@RequestBody ReportFormReq req, HttpServletResponse response) {
        log.info("导出产品分类订购报表列表数据请求入参：{}", JSONUtil.toJsonStr(req));
        try {
            List<ReportProductExprDt> list = reportFormService.list(Wrappers.<ReportProductExprDt>lambdaQuery()
                    .eq(StrUtil.isNotEmpty(req.getOpTime()), ReportProductExprDt::getOpTime, req.getOpTime())
                    .orderByAsc(ReportProductExprDt::getCityId));
            List<Object> rs = new ArrayList<>();
            list.forEach(reportChannelExprDt -> {
                Object[] obj = new Object[SpecialNumberJx.TWELVE_NUMBER];
                obj[SpecialNumberJx.ZERO_NUMBER] = reportChannelExprDt.getCityName();
                obj[SpecialNumberJx.ONE_NUMBER] = reportChannelExprDt.getPlanType();
                obj[SpecialNumberJx.TWO_NUMBER] = reportChannelExprDt.getTuijianDs();
                obj[SpecialNumberJx.THREE_NUMBER] = reportChannelExprDt.getLjblDs();
                obj[SpecialNumberJx.FOUR_NUMBER] = reportChannelExprDt.getXyklDs();
                obj[SpecialNumberJx.FIVE_NUMBER] = reportChannelExprDt.getZsjjDs();
                obj[SpecialNumberJx.SIX_NUMBER] = reportChannelExprDt.getSuccDs();
                obj[SpecialNumberJx.SEVEN_NUMBER] = reportChannelExprDt.getTuijianDt();
                obj[SpecialNumberJx.EIGHT_NUMBER] = reportChannelExprDt.getLjblDt();
                obj[SpecialNumberJx.NINE_NUMBER] = reportChannelExprDt.getXyklDt();
                obj[SpecialNumberJx.TEN_NUMBER] = reportChannelExprDt.getZsjjDt();
                obj[SpecialNumberJx.ELEVEN_NUMBER] = reportChannelExprDt.getSuccDt();
                rs.add(obj);
            });
            String[] excelHeader = {"地市名称", "产品类型", "当天推荐", "当天立即办理", "当天需要考虑", "当天暂时拒绝", "当天是否办理",
                    "当月推荐", "当月立即办理", "当月需要考虑", "当月暂时拒绝", "当月是否办理"};
            exportExcelUtil.exportExcel(excelHeader, rs, response, "产品分类订购报表");
        } catch (Exception e) {
            log.error("导出产品分类订购报表列表数据异常：", e);
        }
    }

    @ApiOperation(value = "导出产品分渠道订购报表列表数据", notes = "导出产品分渠道订购报表列表数据")
    @PostMapping("/exportOrderChannelDMData")
    public void exportOrderChannelDMData(@RequestBody ReportFormReq req, HttpServletResponse response) {
        log.info("导出产品分渠道订购报表列表数据请求入参：{}", JSONUtil.toJsonStr(req));
        try {
            List<ReportChannelExprDt> orderProductDMData = reportChannelService.getExportOrderProductDMDataByPage(req);
            List<Object> rs = new ArrayList<>();
            orderProductDMData.forEach(reportChannelExprDt -> {
                Object[] obj = new Object[SpecialNumberJx.ELEVEN_NUMBER];
                obj[SpecialNumberJx.ZERO_NUMBER] = reportChannelExprDt.getCityName();
                obj[SpecialNumberJx.ONE_NUMBER] = reportChannelExprDt.getCountyName();
                obj[SpecialNumberJx.TWO_NUMBER] = reportChannelExprDt.getChannel();
                obj[SpecialNumberJx.THREE_NUMBER] = reportChannelExprDt.getOperId();
                obj[SpecialNumberJx.FOUR_NUMBER] = reportChannelExprDt.getDytj();
                obj[SpecialNumberJx.FIVE_NUMBER] = reportChannelExprDt.getLjbl();
                obj[SpecialNumberJx.SIX_NUMBER] = reportChannelExprDt.getXykl();
                obj[SpecialNumberJx.SEVEN_NUMBER] = reportChannelExprDt.getZsjj();
                obj[SpecialNumberJx.EIGHT_NUMBER] = reportChannelExprDt.getIsCz();
                obj[SpecialNumberJx.NINE_NUMBER] = reportChannelExprDt.getIsBanLi();
                obj[SpecialNumberJx.TEN_NUMBER] = reportChannelExprDt.getIsSelfBanLi();
                rs.add(obj);
            });
            String[] excelHeader = {"地市名称", "区县名称", "渠道ID", "操作ID", "当月推荐", "立即办理", "需要考虑",
                    "暂时拒绝", "是否操作", "是否办理", "是否该营业员操作"};
            exportExcelUtil.exportExcel(excelHeader, rs, response, "产品分渠道订购报表");
        } catch (Exception e) {
            log.error("导出产品分渠道订购报表列表数据异常：", e);
        }
    }

}
