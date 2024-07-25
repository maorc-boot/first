package com.asiainfo.biapp.pec.eval.jx.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.eval.jx.model.McdFiveGEffectEvaluate;
import com.asiainfo.biapp.pec.eval.jx.req.G5EvalPageQuery;
import com.asiainfo.biapp.pec.eval.jx.service.McdFiveGEffectEvaluateService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 * 5G效果评估表 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2022-12-15
 */
@RestController
@RequestMapping("/eval5g")
@Api(tags = "江西:5G效果评估")
@Slf4j
public class McdFiveGEffectEvaluateController {
    @Autowired
    private McdFiveGEffectEvaluateService effectEvaluateServicel;


    @Resource
    private HttpServletResponse response;

    @PostMapping("/queryPage")
    public ActionResponse<IPage<McdFiveGEffectEvaluate>> queryPage(@RequestBody G5EvalPageQuery query) {
        try {
            return ActionResponse.getSuccessResp(effectEvaluateServicel.queryPage(query));
        } catch (Exception e) {
            return ActionResponse.getFaildResp("查询5G消息评估数据异常");
        }
    }


    @GetMapping("/export")
    @ApiOperation(value = "导出5G效果评估数据", notes = "导出5G效果评估数据")
    public void moreExportCampInfoList(@Param("cityId") String cityId,
                                       @Param("countyId") String countyId,
                                       @Param("fallbackConfig") String fallbackConfig,
                                       @Param("fallbackType") String fallbackType,
                                       @Param("keyWords") String keyWords) {
        G5EvalPageQuery pageQuery = new G5EvalPageQuery(countyId,countyId,fallbackConfig,fallbackType);
        pageQuery.setCurrent(1);
        pageQuery.setSize(999999);
        pageQuery.setKeyWords(keyWords);
        try {
            log.info("开始导出5G效果评估数据");
            //获取列表
            List<List<String>> lists = effectEvaluateServicel.export(pageQuery);
            //获取表头
            List<String> header = Lists.newArrayList("开始时间", "结束时间", "活动ID", "活动名称", "应用号", "产品名称", "地市", "区县", "群发总人数", "5G消息送达用户数", "送达成功率", "点击用户数", "5G消息点击率", "总成功订购数", "总成功订购率", "有无回落", "回落类型", "回落消息送达次数", "回落消息送达成功率", "回落消息送达人数");
            //获取文件名
            String fileName = StrUtil.format("5G效果评估_{}.xlsx", DateUtil.now());

            log.info("活5G效果评估数据查询成功，正在导出...");
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
            log.info("5G效果评估数据导出成功");
        } catch (Exception e) {
            log.error("5G效果评估数据失败", e);
        }
    }
}

