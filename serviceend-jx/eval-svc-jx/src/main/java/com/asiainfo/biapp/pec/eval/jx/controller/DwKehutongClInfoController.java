package com.asiainfo.biapp.pec.eval.jx.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.eval.jx.model.DwKehutongClInfo;
import com.asiainfo.biapp.pec.eval.jx.req.CityCampPageQuery;
import com.asiainfo.biapp.pec.eval.jx.service.DwKehutongClInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 * 分策略分地市分渠道报表 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2022-12-19
 */
@RestController
@RequestMapping("/report")
@Slf4j
@Api(tags = "江西:分策略分地市分渠道报表")
public class DwKehutongClInfoController {
    @Resource
    private HttpServletResponse response;
    @Resource
    private DwKehutongClInfoService clInfoService;

    @PostMapping("/queryPage")
    @ApiOperation("分页查询分地市分策略分渠道报表")
    public ActionResponse<IPage<DwKehutongClInfo>> queryPage(@RequestBody CityCampPageQuery query) {
        log.info("queryPage param: {} ", query);
        try {
            return ActionResponse.getSuccessResp(clInfoService.queryPage(query));
        } catch (Exception e) {
            return ActionResponse.getFaildResp("分地市分策略分渠道报表数据异常");
        }
    }


    @GetMapping("/export")
    @ApiOperation(value = "导出分地市分策略分渠道报表", notes = "导出分地市分策略分渠道报表")
    public void moreExportCampInfoList(@Param("statDate") String statDate) {
        CityCampPageQuery pageQuery = new CityCampPageQuery(statDate);
        pageQuery.setCurrent(1);
        pageQuery.setSize(999999);
        try {
            log.info("开始导出分地市分策略分渠道报表");
            //获取列表
            List<List<String>> lists = clInfoService.export(pageQuery);
            //获取表头
            List<String> header = Lists.newArrayList("策略类型", "策略名称", "产品类型", "产品名称", "目标客户群名称", "客户群数量", "地市", "渠道", "营销目标用户数", "接触用户数(分策略剃重)", "接触率", "响应用户数(分策略剃重)", "响应率", "办理用户数(分策略剃重)", "转化率");
            //获取文件名
            String fileName = StrUtil.format("分地市分策略分渠道报表_{}.xlsx", DateUtil.now());

            log.info("分地市分策略分渠道报表查询成功，正在导出...");
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
            log.info("分地市分策略分渠道报表数据导出成功");
        } catch (Exception e) {
            log.error("分地市分策略分渠道报表数据失败", e);
        }
    }
}

