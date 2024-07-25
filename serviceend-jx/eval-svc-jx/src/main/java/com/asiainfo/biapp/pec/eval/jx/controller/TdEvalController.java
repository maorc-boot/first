package com.asiainfo.biapp.pec.eval.jx.controller;

import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.eval.jx.constants.SpecialNumberJx;
import com.asiainfo.biapp.pec.eval.jx.req.TdEvalReq;
import com.asiainfo.biapp.pec.eval.jx.service.TdEvalService;
import com.asiainfo.biapp.pec.eval.jx.utils.ExportExcelUtil;
import com.asiainfo.biapp.pec.eval.util.Pager;
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
import java.util.Map;

/**
 * description: 厅店评估控制层
 *
 * @author: lvchaochao
 * @date: 2023/1/16
 */
@Api(tags = {"江西：厅店评估"}, value = "江西：厅店评估")
@RequestMapping("/td/eval")
@RestController
@Slf4j
public class TdEvalController {

    @Autowired
    private TdEvalService tdEvalService;

    @Autowired
    private ExportExcelUtil exportExcelUtil;

    @ApiOperation(value = "获取厅店评估列表数据", notes = "获取厅店评估列表数据")
    @PostMapping("/list")
    public Pager list(@RequestBody TdEvalReq req) {
        log.info("获取厅店评估列表数据入参：{}", JSONUtil.toJsonStr(req));
        Pager pager = null;
        try {
            pager = new Pager();
            pager.setPageSize(req.getPageSize());
            pager.setPageNum(req.getPageNum()); // 当前页
            pager.setPageFlag("G");
            List<Map<String, Object>> retrunList = tdEvalService.getTdEvalList(pager, req);
            pager.setTotalSize(tdEvalService.getTdEvalListCount(req));
            pager.setResult(retrunList);
        } catch (Exception e) {
            log.error("获取厅店评估列表数据数据异常：", e);
        }
        return pager;
    }

    @ApiOperation(value = "导出厅店评估列表数据", notes = "导出厅店评估列表数据")
    @PostMapping("/exportTdEvalListData")
    public void exportTdEvalListData(@RequestBody TdEvalReq req, HttpServletResponse response) {
        log.info("导出厅店评估列表数据请求入参：{}", JSONUtil.toJsonStr(req));
        try {
            List<Map<String, Object>> retrunList = tdEvalService.getExportTdEvalList(req);
            List<Object> rs = new ArrayList<>();
            retrunList.forEach(map -> {
                Object[] obj = new Object[SpecialNumberJx.EIGHT_NUMBER];
                obj[SpecialNumberJx.ZERO_NUMBER] = map.get("STAT_DATE");
                obj[SpecialNumberJx.ONE_NUMBER] = map.get("CITY_NAME");
                obj[SpecialNumberJx.TWO_NUMBER] = map.get("COUNTY_NAME");
                obj[SpecialNumberJx.THREE_NUMBER] = map.get("ORG_ID");
                obj[SpecialNumberJx.FOUR_NUMBER] = map.get("ORG_NAME");
                obj[SpecialNumberJx.FIVE_NUMBER] = map.get("CAMP_USER_NUM");
                obj[SpecialNumberJx.SIX_NUMBER] = map.get("CAMP_SUCC_NUM");
                obj[SpecialNumberJx.SEVEN_NUMBER] = map.get("CAMP_SUCC_RATE");
                rs.add(obj);
            });
            String[] excelHeader = {"日期", "地市", "区县", "组织编号", "组织名称", "营销用户", "成功用户", "用户成功率"};
            exportExcelUtil.exportExcel(excelHeader, rs, response, "厅店评估报表");
        } catch (Exception e) {
            log.error("导出厅店评估列表数据异常：", e);
        }
    }
}
