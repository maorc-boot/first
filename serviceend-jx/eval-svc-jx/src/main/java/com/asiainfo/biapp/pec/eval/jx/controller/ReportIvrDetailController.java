package com.asiainfo.biapp.pec.eval.jx.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.eval.jx.model.ReportIvrDetail;
import com.asiainfo.biapp.pec.eval.jx.req.IvrPageQuery;
import com.asiainfo.biapp.pec.eval.jx.service.ReportIvrDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2022-12-16
 */
@RestController
@RequestMapping("/reportIvr")
@Api(tags = "江西:IVR营销情况")
@Slf4j
public class ReportIvrDetailController {
    @Resource
    private HttpServletResponse response;
    @Resource
    private ReportIvrDetailService ivrDetailService;

    @PostMapping("/queryPage")
    @ApiOperation("分页查询IVR营销情况")
    public ActionResponse<IPage<ReportIvrDetail>> queryPage(@RequestBody IvrPageQuery query) {
        log.info("queryPage param: {} ", query);
        try {
            QueryWrapper<ReportIvrDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(StrUtil.isNotEmpty(query.getStatDate()), ReportIvrDetail::getStatDate, query.getStatDate());
            IPage<ReportIvrDetail> iPage = new Page<>(query.getCurrent(), query.getSize());
            return ActionResponse.getSuccessResp(ivrDetailService.page(iPage, queryWrapper));
        } catch (Exception e) {
            return ActionResponse.getFaildResp("IVR营销情况数据异常");
        }
    }


    @GetMapping("/export")
    @ApiOperation(value = "导出IVR营销情况", notes = "导出IVR营销情况")
    public void moreExportCampInfoList(@Param("statDate") String statDate) {
        IvrPageQuery pageQuery = new IvrPageQuery(statDate);
        pageQuery.setCurrent(1);
        pageQuery.setSize(999999);
        try {
            log.info("开始导出IVR营销情况");
            //获取列表
            List<List<String>> lists = ivrDetailService.export(pageQuery);
            //获取表头
            List<String> header = Lists.newArrayList("策略id", "策略名称", "客户群名称", "客户群数量", "当日IVR呼出", "当日IVR接通", "当日IVR请求人工", "当日IVR接入人工", "当日IVR营销成功", "当月IVR呼出", "当月IVR接通", "当月IVR请求人工", "当月IVR接入人工", "当月IVR营销成功");
            //获取文件名
            String fileName = StrUtil.format("IVR营销情况_{}.xlsx", DateUtil.now());

            log.info("IVR营销情况数据查询成功，正在导出...");
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
            log.info("IVR营销情况数据导出成功");
        } catch (Exception e) {
            log.error("IVR营销情况数据失败", e);
        }
    }
}

