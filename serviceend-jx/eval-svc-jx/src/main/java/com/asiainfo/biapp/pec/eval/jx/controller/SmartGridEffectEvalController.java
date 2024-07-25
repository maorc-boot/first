package com.asiainfo.biapp.pec.eval.jx.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.eval.jx.req.SmartGridEvalReq;
import com.asiainfo.biapp.pec.eval.jx.service.SmartGridEvalService;
import com.asiainfo.biapp.pec.eval.jx.vo.CampEvalVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 智慧网格效果评估
 */
@RestController
@RequestMapping("/smart/grid/eval")
@Slf4j
@Api(value = "江西:智慧网格效果评估", tags = "江西:智慧网格效果评估")
public class SmartGridEffectEvalController {
    @Resource
    private HttpServletResponse response;
    @Resource
    private SmartGridEvalService smartGridEvalService;


    @PostMapping("/gridEval")
    @ApiOperation(value = "智慧网格效果评估", notes = "智慧网格效果评估")
    public ActionResponse<IPage<CampEvalVo>> campEval(@RequestBody SmartGridEvalReq req) {
        log.info("智慧网格效果评估campEval SmartGridEvalReq pramam = {} ", req);

        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            resp.setData(smartGridEvalService.smartGridEval(req));
        } catch (Exception e) {
            resp = ActionResponse.getFaildResp("智慧网格效果评估查询失败");
            log.error("智慧网格效果评估查询失败:", e);
        }
        return resp;
    }


    @PostMapping("/export")
    @ApiOperation(value = "导出智慧网格效果评估", notes = "导出智慧网格效果评估")
    public void export(@RequestBody SmartGridEvalReq req) {
        log.info("导出智慧网格效果评估export SmartGridEvalReq pramam = {} ", req);
        req.setCurrent(1);
        req.setSize(999999);
        try {
            log.info("开始导出智慧网格效果评估");
            //获取列表
            List<List<String>> lists = smartGridEvalService.smartGridEvalExport(req);
            //获取表头
            List<String> header = Lists.newArrayList("活动名称", "活动编码", "产品名称", "产品编码", "开始时间", "结束时间", "地市名称",
                    "区县名称", "网格名称", "客户数", "接触数", "接触率(%)","营销成功数","营销成功率(%)","接触类型","接触方式","渠道名称");
            //获取文件名
            String fileName = StrUtil.format("智慧网格效果评估_{}.xls", DateUtil.now());

            log.info("智慧网格效果评估查询成功，正在导出...");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLUtil.encode(fileName));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            final ServletOutputStream outputStream = response.getOutputStream();
            //写表头，写内容
            ExcelUtil.getWriter().writeHeadRow(header).write(lists).flush(outputStream).close();
            log.info("智慧网格效果评估数据导出成功");
        } catch (Exception e) {
            log.error("智慧网格效果评估数据导出失败", e);
        }
    }


}
