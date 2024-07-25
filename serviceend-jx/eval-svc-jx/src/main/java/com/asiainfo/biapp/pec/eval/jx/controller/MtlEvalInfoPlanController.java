package com.asiainfo.biapp.pec.eval.jx.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.eval.jx.model.MtlEvalInfoPlan;
import com.asiainfo.biapp.pec.eval.jx.req.PlanEvalPageQuery;
import com.asiainfo.biapp.pec.eval.jx.service.MtlEvalInfoPlanService;
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
 * 产品效果评估 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2022-12-18
 */
@RestController
@RequestMapping("/mtl-eval-info-plan")
@Api(tags = "江西:产品评估")
@Slf4j
public class MtlEvalInfoPlanController {

    @Resource
    private HttpServletResponse response;

    @Resource
    private MtlEvalInfoPlanService planService;

    @PostMapping("/queryPage")
    @ApiOperation("分页查询产品评估")
    public ActionResponse<IPage<MtlEvalInfoPlan>> queryPage(@RequestBody PlanEvalPageQuery query) {
        log.info("queryPage param: {} ", query);
        try {
            return ActionResponse.getSuccessResp(planService.queryPage(query));
        } catch (Exception e) {
            log.error("查询产品评估数据异常",e);
            return ActionResponse.getFaildResp("查询产品评估数据异常");
        }
    }


    @GetMapping("/export")
    @ApiOperation(value = "导出产品评估", notes = "导出产品评估")
    public void moreExportCampInfoList(@Param("statStartDate") String statStartDate,
                                       @Param("statEndDate") String statEndDate,
                                       @Param("channelId") String channelId,
                                       @Param("cityId") String cityId,
                                       @Param("countyId") String countyId,
                                       @Param("viewType") String viewType
    ) {
        PlanEvalPageQuery pageQuery = new PlanEvalPageQuery(statStartDate, statEndDate, channelId, cityId, countyId, viewType);
        pageQuery.setCurrent(1);
        pageQuery.setSize(999999999);
        try {
            log.info("开始导出产品评估");
            //获取列表
            List<List<String>> lists = planService.export(pageQuery);
            //获取表头
            List<String> header = Lists.newArrayList("日期", "地市", "区县", "渠道", "产品名称", "产品编号", "营销用户", "营销成功用户", "营销成功率", "全网订购数", "覆盖数", "用户覆盖率");
            //获取文件名
            String fileName = StrUtil.format("产品评估_{}.xlsx", DateUtil.now());

            log.info("产品评估数据查询成功，正在导出...");
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
            log.info("产品评估数据导出成功");
        } catch (Exception e) {
            log.error("产品评估数据失败", e);
        }
    }
}

