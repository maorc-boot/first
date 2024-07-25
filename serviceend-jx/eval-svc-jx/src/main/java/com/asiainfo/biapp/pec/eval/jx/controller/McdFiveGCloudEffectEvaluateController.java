package com.asiainfo.biapp.pec.eval.jx.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.eval.jx.model.McdFiveGCloudEffectEvaluate;
import com.asiainfo.biapp.pec.eval.jx.req.G5CloudEvalPageQuery;
import com.asiainfo.biapp.pec.eval.jx.service.McdFiveGCloudEffectEvaluateService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 5G云卡效果评估控制层
 *
 * @author lvcc
 * @date 2023/10/12
 */
@RestController
@RequestMapping("/eval/5gcloud")
@Api(tags = "江西:5G云卡效果评估")
@Slf4j
public class McdFiveGCloudEffectEvaluateController {

    @Autowired
    private McdFiveGCloudEffectEvaluateService effectEvaluateService;

    @ApiOperation(value = "5G云卡效果评估列表", notes = "5G云卡效果评估列表")
    @PostMapping("/queryPage")
    public ActionResponse<IPage<McdFiveGCloudEffectEvaluate>> queryPage(@RequestBody G5CloudEvalPageQuery query) {
        log.info("5G云卡效果评估查询入参={}", JSONUtil.toJsonStr(query));
        try {
            return ActionResponse.getSuccessResp(effectEvaluateService.queryPage(query));
        } catch (Exception e) {
            log.error("查询5G云卡效果评估列表数据异常: ", e);
            return ActionResponse.getFaildResp("查询5G云卡效果评估列表数据异常");
        }
    }

    @ApiOperation(value = "导出5G云卡效果评估列表数据", notes = "导出5G云卡效果评估列表数据")
    @PostMapping("/export5gCloudEvalListData")
    public void export5gCloudEvalListData(@RequestBody G5CloudEvalPageQuery query, HttpServletResponse response) {
        log.info("导出5G云卡效果评估数据请求入参：{}", JSONUtil.toJsonStr(query));
        try {
            // 1.获取导出数据
            List<List<String>> retrunList = effectEvaluateService.getExport5gCloudEvalList(query);
            // 2.获取表头
            List<String> header;
            if ("-1".equals(query.getCallbackType())) {
                header = Lists.newArrayList("开始时间", "结束时间", "活动ID", "活动名称", "产品名称", "地市", "区县", "回调类型", "群发总人数", "下发总人数", "送达用户数", "送达成功率");
            } else header = Lists.newArrayList("开始时间", "结束时间", "活动ID", "活动名称", "产品名称", "地市", "区县", "群发总人数", "下发总人数", "送达用户数", "送达成功率");
            // 3.获取文件名
            String fileName = StrUtil.format("5G云卡效果评估_{}.xlsx", DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss"));
            log.info("5G云卡效果评估数据查询成功，正在导出...");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLUtil.encode(fileName));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            final ServletOutputStream outputStream = response.getOutputStream();
            // 4.写表头，写内容
            ExcelUtil.getWriter().writeHeadRow(header).write(retrunList).flush(outputStream).close();
            log.info("5G云卡效果评估数据导出成功");
        } catch (Exception e) {
            log.error("导出5G云卡效果评估列表数据异常：", e);
        }
    }

}

