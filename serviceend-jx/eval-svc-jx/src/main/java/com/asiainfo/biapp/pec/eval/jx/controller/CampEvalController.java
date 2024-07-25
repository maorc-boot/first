package com.asiainfo.biapp.pec.eval.jx.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.eval.jx.req.CampEvalReq;
import com.asiainfo.biapp.pec.eval.jx.req.ChannelRadarMapReq;
import com.asiainfo.biapp.pec.eval.jx.service.CampEvalService;
import com.asiainfo.biapp.pec.eval.jx.vo.CampEvalVo;
import com.asiainfo.biapp.pec.eval.jx.vo.UseEffectVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 效果总览
 *
 * @author mamp
 * @date 2023/1/9
 */
@RestController
@RequestMapping("/eval")
@Slf4j
@Api(value = "江西:活动评估", tags = "江西:活动评估")
public class CampEvalController {
    @Resource
    private HttpServletResponse response;
    @Resource
    private CampEvalService campEvalService;


    @PostMapping("/campEval")
    @ApiOperation(value = "江西活动评估", notes = "江西活动评估")
    public ActionResponse<IPage<CampEvalVo>> campEval(@RequestBody CampEvalReq req) {
        log.info("campEval pramam = {} ", req);

        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            resp.setData(campEvalService.campEval(req));
        } catch (Exception e) {
            resp = ActionResponse.getFaildResp("活动评估查询失败");
            log.error("活动评估查询失败:", e);
        }
        return resp;
    }


    @PostMapping("/export")
    @ApiOperation(value = "导出活动评估数据", notes = "导出活动评估数据")
    public void export(@RequestBody CampEvalReq req) {
        req.setCurrent(1);
        req.setSize(999999);
        try {
            log.info("开始导出活动评估");
            //获取列表
            List<List<String>> lists = campEvalService.campEvalExport(req);
            //获取表头
            List<String> header = Lists.newArrayList("开始时间", "结束时间", "活动类型", "活动ID", "活动名称", "产品类型", "产品名称", "地市", "区县", "渠道", "总用户数", "接触用户数", "接触成功率(%)", "成功用户数", "成功率(%)");
            //获取文件名
            String fileName = StrUtil.format("活动评估_{}.xlsx", DateUtil.now());

            log.info("活动评估查询成功，正在导出...");
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
            log.info("活动评估数据导出成功");
        } catch (Exception e) {
            log.error("活动评估数据导出失败", e);
        }
    }


    @PostMapping("/dayUseEffect")
    @ApiOperation(value = "日使用效果", notes = "日使用效果")
    public ActionResponse<List<UseEffectVo>> dayUseEffect(@RequestParam("dataDate") String dataDate) {
        log.info("dayUseEffect pramam = {} ", dataDate);
        List<UseEffectVo> effectVoList = campEvalService.queryDayUseEffect(dataDate);
        ActionResponse resp = ActionResponse.getSuccessResp();
        resp.setData(effectVoList);
        return resp;
    }


    @PostMapping("/monthUseEffect")
    @ApiOperation(value = "月使用效果", notes = "月使用效果")
    public ActionResponse<List<UseEffectVo>> monthUseEffect(@RequestParam("dataDate") String dataDate) {
        log.info("monthUseEffect pramam = {} ", dataDate);
        List<UseEffectVo> effectVoList = campEvalService.queryMonthUseEffect(dataDate);
        ActionResponse resp = ActionResponse.getSuccessResp();
        resp.setData(effectVoList);
        return resp;
    }

    @PostMapping("/month6UseEffect")
    @ApiOperation(value = "近6个月营销效果", notes = "近6个月营销效果")
    public ActionResponse<List<UseEffectVo>> month6UseEffect(@RequestParam("dataDate") String dataDate) {
        log.info("monthUseEffect pramam = {} ", dataDate);
        List<UseEffectVo> effectVoList = campEvalService.queryMonthUseEffect(dataDate);
        ActionResponse resp = ActionResponse.getSuccessResp();
        resp.setData(effectVoList);
        return resp;
    }

    @PostMapping("/queryChannelRadarMap")
    @ApiOperation(value = "渠道雷达图", notes = "渠道雷达图数据查询")
    public ActionResponse<List<Map<String, Object>>> queryChannelRadarMap(@RequestBody ChannelRadarMapReq req) {
        log.info("queryChannelRadarMap pramam = {} ", req);
        try {
            List<Map<String, Object>> list = campEvalService.queryChannelRadarMap(req);
            return ActionResponse.getSuccessResp("渠道雷达图数据查询成功").setData(list);
        } catch (Exception e) {
           log.error("渠道雷达图数据查询异常：", e);
        }
        return ActionResponse.getFaildResp("渠道雷达图数据查询失败");
    }


}
