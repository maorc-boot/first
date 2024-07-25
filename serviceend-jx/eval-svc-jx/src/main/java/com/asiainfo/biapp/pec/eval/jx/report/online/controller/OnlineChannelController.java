
package com.asiainfo.biapp.pec.eval.jx.report.online.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.eval.jx.report.online.model.*;
import com.asiainfo.biapp.pec.eval.jx.report.online.req.OnlineReportDataQuery;
import com.asiainfo.biapp.pec.eval.jx.report.online.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping({"/mcd/onlinel/report"})
@Api(tags = "江西:在线报表")
@Slf4j
public class OnlineChannelController   {

    @Autowired
    private DWHeigh88CityService dwHeigh88CityService;

    @Autowired
    private DW88CampService  dw88CampService;

    @Autowired
    private STCamp85CityDMService stCamp85CityDMService;

    @Autowired
    private STCamp86CityDMService stCamp86CityDMService;

    @Autowired
    private STCamp85HDDMService  stCamp85HDDMService;

    @Autowired
    private STCamp86HDDMService  stCamp86HDDMService;

    @Autowired
    private ReportMaxDateService  reportMaxDateService;


    @ApiOperation("江西在线报表获取数据最新日期")
    @PostMapping("/getDateTime")
    public ActionResponse<ReportMaxDateModel> getDataNewestTime(@RequestBody OnlineReportDataQuery req) {
        if (StringUtils.isEmpty(req.getTablename())){
            log.error("江西在线报表获取数据最新日期入参为空!");
            return ActionResponse.getFaildResp("入参为空!");
        }

        LambdaQueryWrapper<ReportMaxDateModel>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReportMaxDateModel::getTableName,req.getTablename())
                    .orderByDesc(ReportMaxDateModel::getMaxDate).last(" limit 1");

        return ActionResponse.getSuccessResp( reportMaxDateService.getOne(queryWrapper));
    }

    @ApiOperation("江西在线报表呼入营销情况 ")
    @PostMapping("/stCamp86CityDm")
    public ActionResponse<Page<STCamp10086CityDMModel>> queryStCamp86CityDmList(@RequestBody OnlineReportDataQuery req) {
        if (StringUtils.isEmpty(req.getStatDate())){
            log.error("江西在线报表呼入营销情况入参为空!");
            return ActionResponse.getFaildResp("入参为空!");
        }

        LambdaQueryWrapper<STCamp10086CityDMModel>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(STCamp10086CityDMModel::getStatDate,req.getStatDate());
        Page page =new Page();
        page.setCurrent(req.getPageNum());
        page.setSize(req.getPageSize());

        return ActionResponse.getSuccessResp(stCamp86CityDMService.page(page,queryWrapper));

    }


    @ApiOperation("江西在线报表呼入营销情况-分策略 ")
    @PostMapping("/stCamp86HdDm")
    public ActionResponse<Page<STCamp10086HDDMModel>> queryStCamp86HdDmList(@RequestBody OnlineReportDataQuery req) {
        if (StringUtils.isEmpty(req.getStatDate())){
            log.error("江西在线报表呼入营销情况-分策略入参为空!");
            return ActionResponse.getFaildResp("入参为空!");
        }

        LambdaQueryWrapper<STCamp10086HDDMModel>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(STCamp10086HDDMModel::getStatDate,req.getStatDate());
        Page page =new Page();
        page.setCurrent(req.getPageNum());
        page.setSize(req.getPageSize());

        return ActionResponse.getSuccessResp(stCamp86HDDMService.page(page,queryWrapper));

    }



    @ApiOperation("江西在线报表呼出营销情况-分地市 ")
    @PostMapping("/stCamp85CityDm")
    public ActionResponse<Page<STCamp10085CityDMModel>> queryStCamp85CityDmList(@RequestBody OnlineReportDataQuery req) {
        if (StringUtils.isEmpty(req.getStatDate())){
            log.error("江西在线报表呼出营销情况-分地市入参为空!");
            return ActionResponse.getFaildResp("入参为空!");
        }

        LambdaQueryWrapper<STCamp10085CityDMModel>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(STCamp10085CityDMModel::getStatDate,req.getStatDate());
        Page page =new Page();
        page.setCurrent(req.getPageNum());
        page.setSize(req.getPageSize());

        return ActionResponse.getSuccessResp(stCamp85CityDMService.page(page,queryWrapper));

    }


    @ApiOperation("江西在线报表呼出营销情况-分策略 ")
    @PostMapping("/stCamp85HdDm")
    public ActionResponse<Page<STCamp10085HDDMModel>> queryStCamp85HdDmList(@RequestBody OnlineReportDataQuery req) {
        if (StringUtils.isEmpty(req.getStatDate())){
            log.error("江西在线报表呼出营销情况-分策略入参为空!");
            return ActionResponse.getFaildResp("入参为空!");
        }

        LambdaQueryWrapper<STCamp10085HDDMModel>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(STCamp10085HDDMModel::getStatDate,req.getStatDate());
        Page page =new Page();
        page.setCurrent(req.getPageNum());
        page.setSize(req.getPageSize());

        return ActionResponse.getSuccessResp(stCamp85HDDMService.page(page,queryWrapper));

    }


    @ApiOperation("江西在线报表中高端客户专席 ")
    @PostMapping("/dwHeigh88City")
    public ActionResponse<Page<DWHeigh10088CityModel>> queryDwHeigh88City(@RequestBody OnlineReportDataQuery req) {
        if (StringUtils.isEmpty(req.getStatDate())){
            log.error("江西在线报表中高端客户专席入参为空!");
            return ActionResponse.getFaildResp("入参为空!");
        }

        LambdaQueryWrapper<DWHeigh10088CityModel>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DWHeigh10088CityModel::getStatDate,req.getStatDate());
        Page page =new Page();
        page.setCurrent(req.getPageNum());
        page.setSize(req.getPageSize());

        return ActionResponse.getSuccessResp(dwHeigh88CityService.page(page,queryWrapper));

    }


    @ApiOperation("江西在线报表中高端客户专席明细 ")
    @PostMapping("/dw88Camp")
    public ActionResponse<Page<DW10088CampModel>> queryDw88Camp(@RequestBody OnlineReportDataQuery req) {
        if (StringUtils.isEmpty(req.getStatDate())){
            log.error("江西在线报表中高端客户专席明细入参为空!");
            return ActionResponse.getFaildResp("入参为空!");
        }

        LambdaQueryWrapper<DW10088CampModel>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DW10088CampModel::getStatDate,req.getStatDate());
        Page page =new Page();
        page.setCurrent(req.getPageNum());
        page.setSize(req.getPageSize());

        return ActionResponse.getSuccessResp(dw88CampService.page(page,queryWrapper));

    }


    @ApiOperation("在线报表数据导出接口")
    @PostMapping("/onlineReportDataExport")
    public void onlineReportDataExportCommon(@RequestBody OnlineReportDataQuery req , HttpServletResponse response) throws Exception {

        if (StringUtils.isEmpty(req.getTablename()) || StringUtils.isEmpty(req.getStatDate())){
            log.error("在线报表数据导出接口入参为空!");
            return;
        }

        String tablename = req.getTablename();
        log.info("在线报表数据导出接口传入参数为：statdate :"+ req.getStatDate() +" tablename :" + tablename);
        List<Object> rs = new ArrayList();
        List<Map<String, Object>> list = null;
        Iterator i$;
        Map map;
        Object[] o;
        String[] excelHeader;

        if ("dw_10088_camp".equals(tablename)) {
            LambdaQueryWrapper<DW10088CampModel>  queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DW10088CampModel::getStatDate,req.getStatDate());
            list = dw88CampService.listMaps(queryWrapper);
            i$ = list.iterator();

            while(i$.hasNext()) {
                map = (Map)i$.next();
                o = new Object[]{map.get("waring_type") == null ? "" : map.get("waring_type"), map.get("warn_type_nm") == null ? "" : map.get("warn_type_nm"), map.get("rec_goods_code") == null ? "" : map.get("rec_goods_code"), map.get("rec_goods_name") == null ? "" : map.get("rec_goods_name"), map.get("d_jc_count") == null ? "" : map.get("d_jc_count"), map.get("d_jt_num") == null ? "" : map.get("d_jt_num"), map.get("d_jt_rate") == null ? "" : map.get("d_jt_rate"), map.get("d_blsucc_num") == null ? "" : map.get("d_blsucc_num"), map.get("d_succ_reate") == null ? "" : map.get("d_succ_reate"), map.get("m_jc_count") == null ? "" : map.get("m_jc_count"), map.get("m_jt_num") == null ? "" : map.get("m_jt_num"), map.get("m_blsucc_num") == null ? "" : map.get("m_blsucc_num"), map.get("m_blsucc_num") == null ? "" : map.get("m_blsucc_num"), map.get("m_succ_reate") == null ? "" : map.get("m_succ_reate")};
                rs.add(o);
            }

            excelHeader = new String[]{"子任务编码", "子任务名称", "推荐商品编码", "推荐商品名称", "接触数", "接通数", "接通率", "办理成功数", "转化率", "接触数", "接通数", "接通率", "办理成功数", "转化率", "当日", "当月"};
            exportExcel(tablename, "中高端专席明细.xlsx", excelHeader, rs, response);
        }

        if ("dw_heigh_10088_city".equals(tablename)) {
            LambdaQueryWrapper<DWHeigh10088CityModel>  queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DWHeigh10088CityModel::getStatDate,req.getStatDate());
            list = dwHeigh88CityService.listMaps(queryWrapper);
            i$ = list.iterator();

            while(i$.hasNext()) {
                map = (Map)i$.next();
                o = new Object[]{map.get("city_name") == null ? "" : map.get("city_name"), map.get("d_tx_count") == null ? "" : map.get("d_tx_count"), map.get("d_hr_count") == null ? "" : map.get("d_hr_count"), map.get("d_jt_count") == null ? "" : map.get("d_jt_count"), map.get("jt_count") == null ? "" : map.get("jt_count"), map.get("d_30_dur") == null ? "" : map.get("d_30_dur"), map.get("d_jt_rate") == null ? "" : map.get("d_jt_rate"), map.get("d_tx_count") == null ? "" : map.get("d_tx_count"), map.get("d_hr_count") == null ? "" : map.get("d_hr_count"), map.get("d_jt_count") == null ? "" : map.get("d_jt_count"), map.get("jt_count") == null ? "" : map.get("jt_count"), map.get("d_30_dur") == null ? "" : map.get("d_30_dur"), map.get("d_jt_rate") == null ? "" : map.get("d_jt_rate")};
                rs.add(o);
            }

            excelHeader = new String[]{"地市", "台席数", "呼入量", "呼出量", "接通量", "接通超过30S以上", "接通率", "台席数", "呼入量", "呼出量", "接通量", "接通超过30S以上", "接通率", "当日", "当月"};
            exportExcel(tablename, "中高端专席.xlsx", excelHeader, rs, response);
        }

        if ("st_campseg_10086_city_dm".equals(tablename)) {
            LambdaQueryWrapper<STCamp10086CityDMModel>  queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(STCamp10086CityDMModel::getStatDate,req.getStatDate());
            list = stCamp86CityDMService.listMaps(queryWrapper);
            i$ = list.iterator();

            while(i$.hasNext()) {
                map = (Map)i$.next();
                o = new Object[]{map.get("city_name") == null ? "" : map.get("city_name"), map.get("d_call_num") == null ? "" : map.get("d_call_num"), map.get("d_tc_cnts") == null ? "" : map.get("d_tc_cnts").toString(), map.get("d_tc_num") == null ? "" : map.get("d_tc_num").toString(), map.get("d_tc_rate") == null ? "" : map.get("d_tc_rate").toString(), map.get("d_success_num") == null ? "" : map.get("d_success_num").toString(), map.get("d_success_cnts") == null ? "" : map.get("d_success_cnts").toString(), map.get("d_success_rate") == null ? "" : map.get("d_success_rate").toString(), map.get("m_call_num") == null ? "" : map.get("m_call_num").toString(), map.get("m_tc_cnts") == null ? "" : map.get("m_tc_cnts").toString(), map.get("m_tc_num") == null ? "" : map.get("m_tc_num").toString(), map.get("m_tc_rate") == null ? "" : map.get("m_tc_rate").toString(), map.get("m_success_num") == null ? "" : map.get("m_success_num").toString(), map.get("m_success_cnts") == null ? "" : map.get("m_success_cnts").toString(), map.get("m_success_rate") == null ? "" : map.get("m_success_rate").toString()};
                rs.add(o);
            }

            excelHeader = new String[]{"地市", "来话用户数(人工服务)", "目标客户弹窗量(比如1个客户有3个弹窗)", "呼入弹窗用户数", "弹窗覆盖率", "办理客户数", "办理笔数", "转化率", "来话用户数", "目标客户弹窗量(比如1个客户有3个弹窗)", "弹窗用户数", "弹窗覆盖率", "办理客户数", "办理笔数", "转化率", "按日统计", "按月统计（剃重）[当月累计]"};
            exportExcel(tablename, "呼入营销情况.xlsx", excelHeader, rs, response);
        }

        if ("st_campseg_10086_hd_dm".equals(tablename)) {
            LambdaQueryWrapper<STCamp10086HDDMModel>  queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(STCamp10086HDDMModel::getStatDate,req.getStatDate());
            list = stCamp86HDDMService.listMaps(queryWrapper);
            i$ = list.iterator();

            while(i$.hasNext()) {
                map = (Map)i$.next();
                o = new Object[10];
                o[0] = map.get("campseg_type_name") == null ? "" : map.get("campseg_type_name");
                o[1] = map.get("campseg_name") == null ? "" : map.get("campseg_name");
                o[2] = map.get("d_tc_cnts") == null ? "" : map.get("d_tc_cnts");
                o[3] = map.get("d_tc_num") == null ? "" : map.get("d_tc_num");
                o[4] = map.get("d_success_num") == null ? "" : map.get("d_success_num");
                o[5] = map.get("d_success_rate") == null ? "" : map.get("d_success_rate");
                o[6] = map.get("m_tc_cnts") == null ? "" : map.get("m_tc_cnts");
                o[7] = map.get("m_tc_num") == null ? "" : map.get("m_tc_num");
                o[8] = map.get("m_success_num") == null ? "" : map.get("m_success_num");
                o[9] = map.get("m_success_rate") == null ? "" : map.get("m_success_rate");

                rs.add(o);
            }

            excelHeader = new String[]{"策略类型", "策略名称", "目标客户弹窗量(比如1个客户有3个弹窗)", "呼入弹窗用户数", "办理客户数", "转化率", "目标客户弹窗量(比如1个客户有3个弹窗)", "呼入弹窗用户数", "办理客户数", "转化率", "按日统计", "按月统计（剃重）[当月累计]"};
            exportExcel(tablename, "呼入营销情况-分策略.xlsx", excelHeader, rs, response);
        }

        if ("st_campseg_10085_city_dm".equals(tablename)) {
            LambdaQueryWrapper<STCamp10085CityDMModel>  queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(STCamp10085CityDMModel::getStatDate,req.getStatDate());
            list = stCamp85CityDMService.listMaps(queryWrapper);
            i$ = list.iterator();

            while(i$.hasNext()) {
                map = (Map)i$.next();
                o = new Object[]{map.get("city_name") == null ? "" : map.get("city_name"), map.get("d_call_num") == null ? "" : map.get("d_call_num"), map.get("d_jt_num") == null ? "" : map.get("d_jt_num"), map.get("d_jt_rate") == null ? "" : map.get("d_jt_rate"), map.get("d_30_num") == null ? "" : map.get("d_30_num"), map.get("d_success_num") == null ? "" : map.get("d_success_num"),  map.get("d_success_rate") == null ? "" : map.get("d_success_rate"), map.get("m_load_num") == null ? "" : map.get("m_load_num"), map.get("m_load_rate") == null ? "" : map.get("m_load_rate"), map.get("m_gl_num") == null ? "" : map.get("m_gl_num"), map.get("m_call_num") == null ? "" : map.get("m_call_num"), map.get("m_call_rate") == null ? "" : map.get("m_call_rate"), map.get("m_jt_num") == null ? "" : map.get("m_jt_num"), map.get("m_jt_rate") == null ? "" : map.get("m_jt_rate"), map.get("m_30_num") == null ? "" : map.get("m_30_num"), map.get("m_success_num") == null ? "" : map.get("m_success_num"), map.get("m_success_rate") == null ? "" : map.get("m_success_rate"), null};

                rs.add(o);
            }

            excelHeader = new String[]{"地市", "外呼用户数", "接通用户数", "接通率", "30s以上通话用户", "办理用户数", "转化率", "85导入用户数", "导入率", "85平台已过滤数据", "外呼用户数", "外呼率", "接通用户数", "接通率", "30s以上通话用户", "办理用户数", "转化率", "按日统计", "按月统计（剃重）[当月累计]"};
            exportExcel(tablename, "呼出营销情况-分地市.xlsx", excelHeader, rs, response);
        }

        if ("st_campseg_10085_hd_dm".equals(tablename)) {
            LambdaQueryWrapper<STCamp10085HDDMModel>  queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(STCamp10085HDDMModel::getStatDate,req.getStatDate());
            list = stCamp85HDDMService.listMaps(queryWrapper);
            i$ = list.iterator();

            while(i$.hasNext()) {
                map = (Map)i$.next();
                o = new Object[]{map.get("campseg_code") == null ? "" : map.get("campseg_code"), map.get("campseg_name") == null ? "" : map.get("campseg_name"), map.get("d_call_num") == null ? "" : map.get("d_call_num"), map.get("d_jt_num") == null ? "" : map.get("d_jt_num"), map.get("d_jt_rate") == null ? "" : map.get("d_jt_rate"), map.get("d_30_num") == null ? "" : map.get("d_30_num"), map.get("d_success_num") == null ? "" : map.get("d_success_num"), map.get("d_success_rate") == null ? "" : map.get("d_success_rate"), map.get("m_load_num") == null ? "" : map.get("m_load_num"), map.get("m_load_rate") == null ? "" : map.get("m_load_rate"), map.get("m_gl_num") == null ? "" : map.get("m_gl_num"), map.get("m_call_num") == null ? "" : map.get("m_call_num"), map.get("m_call_rate") == null ? "" : map.get("m_call_rate"), map.get("m_jt_num") == null ? "" : map.get("m_jt_num"), map.get("m_jt_rate") == null ? "" : map.get("m_jt_rate"), map.get("m_30_num") == null ? "" : map.get("m_30_num"), map.get("m_success_num") == null ? "" : map.get("m_success_num"), map.get("m_success_rate") == null ? "" : map.get("m_success_rate"), null, null, null, null, null, null};

                rs.add(o);
            }

            excelHeader = new String[]{"策略编号", "名称", "外呼用户数", "接通用户数", "接通率", "30s以上通话用户", "办理用户数", "转化率", "85导入用户数", "导入率", "85平台已过滤数据", "外呼用户数", "外呼率", "接通用户数", "接通率", "30s以上通话用户", "办理用户数", "转化率", "按日统计", "按月统计（剃重）[当月累计]"};
            exportExcel(tablename, "呼出营销情况-分策略.xlsx", excelHeader, rs, response);

        }

    }


    //数据导出
    private void exportExcel(String tablename ,String fileName, String[] excelHeader, List<Object> list, HttpServletResponse response) throws Exception {

        log.info("导出数据表名为 ：tablename: "+tablename);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Sheet");
        sheet.setDefaultRowHeight((short) 500);
        XSSFRow row = sheet.createRow((int) 0);
        XSSFCell cell=row.createCell(0);
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        if("st_campseg_10086_city_dm".equals(tablename)){
            CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 14);
            cell.setCellValue("呼入营销情况");
            CellRangeAddress cra1 = new CellRangeAddress(1, 2, 0, 0);
            CellRangeAddress cra2 = new CellRangeAddress(1, 1, 1, 7);
            CellRangeAddress cra3 = new CellRangeAddress(1, 1, 8, 14);
            sheet.addMergedRegion(cra);
            sheet.addMergedRegion(cra1);
            sheet.addMergedRegion(cra2);
            sheet.addMergedRegion(cra3);
            XSSFRow row1 = sheet.createRow((int) 1);
            XSSFCell cell_1=row1.createCell(0);
            sheet.setDefaultRowHeight((short) 200);
            cell_1.setCellValue(excelHeader[0]);
            XSSFCell cell_2=row1.createCell(1);
            cell_2.setCellValue(excelHeader[15]);
            XSSFCell cell_3=row1.createCell(8);
            cell_3.setCellValue(excelHeader[16]);
            cell.setCellStyle(style);
            cell_1.setCellStyle(style);
            cell_2.setCellStyle(style);
            cell_3.setCellStyle(style);

            XSSFRow row2 = sheet.createRow((int) 2);
            for(int i=0;i<excelHeader.length-3;i++){
                XSSFCell cell_2_1 = row2.createCell(i+1);
                cell_2_1.setCellValue(excelHeader[i+1]);
                sheet.setColumnWidth(cell_2_1.getColumnIndex(), 256 *30);
                cell_2_1.setCellStyle(style);
            }
        }
        if("st_campseg_10086_hd_dm".equals(tablename)){
            CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 9);
            cell.setCellValue("呼入营销情况_分策略");
            CellRangeAddress cra1 = new CellRangeAddress(1, 2, 0, 0);
            CellRangeAddress cra2 = new CellRangeAddress(1, 2, 1, 1);
            CellRangeAddress cra3 = new CellRangeAddress(1, 1, 2, 5);
            CellRangeAddress cra4 = new CellRangeAddress(1, 1, 6, 9);
            sheet.addMergedRegion(cra);
            sheet.addMergedRegion(cra1);
            sheet.addMergedRegion(cra2);
            sheet.addMergedRegion(cra3);
            sheet.addMergedRegion(cra4);
            XSSFRow row1 = sheet.createRow((int) 1);
            XSSFCell cell_1=row1.createCell(0);
            sheet.setDefaultRowHeight((short) 200);
            cell_1.setCellValue(excelHeader[0]);
            XSSFCell cell_2=row1.createCell(1);
            cell_2.setCellValue(excelHeader[1]);
            XSSFCell cell_3=row1.createCell(2);
            cell_3.setCellValue(excelHeader[10]);
            XSSFCell cell_4=row1.createCell(6);
            cell_4.setCellValue(excelHeader[11]);
            cell.setCellStyle(style);
            cell_1.setCellStyle(style);
            cell_2.setCellStyle(style);
            cell_3.setCellStyle(style);
            cell_4.setCellStyle(style);

            XSSFRow row2 = sheet.createRow((int) 2);
            for(int i=0;i<excelHeader.length-4;i++){
                XSSFCell cell_2_1 = row2.createCell(i+2);
                cell_2_1.setCellValue(excelHeader[i+2]);
                sheet.setColumnWidth(cell_2_1.getColumnIndex(), 256 *30);
                cell_2_1.setCellStyle(style);
            }
        }


        if("dw_10088_camp".equals(tablename)){
            CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 13);
            cell.setCellValue("中高端客户专席明细");
            CellRangeAddress cra1 = new CellRangeAddress(1, 2, 0, 0);
            CellRangeAddress cra2 = new CellRangeAddress(1, 2, 1, 1);
            CellRangeAddress cra3 = new CellRangeAddress(1, 2, 2, 2);
            CellRangeAddress cra4 = new CellRangeAddress(1, 2, 3, 3);
            CellRangeAddress cra5 = new CellRangeAddress(1, 1, 4, 8);
            CellRangeAddress cra6 = new CellRangeAddress(1, 1, 9, 13);
            sheet.addMergedRegion(cra);
            sheet.addMergedRegion(cra1);
            sheet.addMergedRegion(cra2);
            sheet.addMergedRegion(cra3);
            sheet.addMergedRegion(cra4);
            sheet.addMergedRegion(cra5);
            sheet.addMergedRegion(cra6);
            XSSFRow row1 = sheet.createRow((int) 1);
            XSSFCell cell_1=row1.createCell(0);
            sheet.setDefaultRowHeight((short) 200);
            cell_1.setCellValue(excelHeader[0]);
            XSSFCell cell_2=row1.createCell(1);
            cell_2.setCellValue(excelHeader[1]);
            XSSFCell cell_3=row1.createCell(2);
            cell_3.setCellValue(excelHeader[2]);
            XSSFCell cell_4=row1.createCell(3);
            cell_4.setCellValue(excelHeader[3]);
            XSSFCell cell_5=row1.createCell(4);
            cell_5.setCellValue(excelHeader[14]);
            XSSFCell cell_6=row1.createCell(9);
            cell_6.setCellValue(excelHeader[15]);
            cell.setCellStyle(style);
            cell_1.setCellStyle(style);
            cell_2.setCellStyle(style);
            cell_3.setCellStyle(style);

            XSSFRow row2 = sheet.createRow((int) 2);
            for(int i=0;i<excelHeader.length-3;i++){
                XSSFCell cell_2_1 = row2.createCell(i+1);
                cell_2_1.setCellValue(excelHeader[i+1]);
                sheet.setColumnWidth(cell_2_1.getColumnIndex(), 256 *30);
                cell_2_1.setCellStyle(style);
            }
        }
        if("dw_heigh_10088_city".equals(tablename)){
            CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 12);
            cell.setCellValue("中高端客户专席");
            CellRangeAddress cra1 = new CellRangeAddress(1, 2, 0, 0);
            CellRangeAddress cra2 = new CellRangeAddress(1, 1, 1, 6);
            CellRangeAddress cra3 = new CellRangeAddress(1, 1, 7, 12);
            sheet.addMergedRegion(cra);
            sheet.addMergedRegion(cra1);
            sheet.addMergedRegion(cra2);
            sheet.addMergedRegion(cra3);
            XSSFRow row1 = sheet.createRow((int) 1);
            XSSFCell cell_1=row1.createCell(0);
            sheet.setDefaultRowHeight((short) 200);
            cell_1.setCellValue(excelHeader[0]);
            XSSFCell cell_2=row1.createCell(1);
            cell_2.setCellValue(excelHeader[13]);
            XSSFCell cell_3=row1.createCell(7);
            cell_3.setCellValue(excelHeader[14]);
            cell.setCellStyle(style);
            cell_1.setCellStyle(style);
            cell_2.setCellStyle(style);
            cell_3.setCellStyle(style);

            XSSFRow row2 = sheet.createRow((int) 2);
            for(int i=0;i<excelHeader.length-3;i++){
                XSSFCell cell_2_1 = row2.createCell(i+1);
                cell_2_1.setCellValue(excelHeader[i+1]);
                sheet.setColumnWidth(cell_2_1.getColumnIndex(), 256 *30);
                cell_2_1.setCellStyle(style);
            }
        }
        if("st_campseg_10085_city_dm".equals(tablename)){
            CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 21);
            cell.setCellValue("呼出营销情况_分地市");
            CellRangeAddress cra1 = new CellRangeAddress(1, 2, 0, 0);
            CellRangeAddress cra2 = new CellRangeAddress(1, 1, 1, 6);
            CellRangeAddress cra3 = new CellRangeAddress(1, 1, 7, 16);
            sheet.addMergedRegion(cra);
            sheet.addMergedRegion(cra1);
            sheet.addMergedRegion(cra2);
            sheet.addMergedRegion(cra3);
            XSSFRow row1 = sheet.createRow((int) 1);
            XSSFCell cell_1=row1.createCell(0);
            sheet.setDefaultRowHeight((short) 200);
            cell_1.setCellValue(excelHeader[0]);
            XSSFCell cell_2=row1.createCell(1);
            cell_2.setCellValue(excelHeader[17]);
            XSSFCell cell_3=row1.createCell(6);
            cell_3.setCellValue(excelHeader[18]);
            cell.setCellStyle(style);
            cell_1.setCellStyle(style);
            cell_2.setCellStyle(style);
            cell_3.setCellStyle(style);

            XSSFRow row2 = sheet.createRow((int) 2);
            for(int i=0;i<excelHeader.length-3;i++){
                XSSFCell cell_2_1 = row2.createCell(i+1);
                cell_2_1.setCellValue(excelHeader[i+1]);
                sheet.setColumnWidth(cell_2_1.getColumnIndex(), 256 *30);
                cell_2_1.setCellStyle(style);
            }
        }
        if("st_campseg_10085_hd_dm".equals(tablename)){
            CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 21);
            cell.setCellValue("营销情况_分策略");
            CellRangeAddress cra1 = new CellRangeAddress(1, 2, 0, 0);
            CellRangeAddress cra2 = new CellRangeAddress(1, 2, 1, 1);
            CellRangeAddress cra3 = new CellRangeAddress(1, 1, 2, 7);
            CellRangeAddress cra4 = new CellRangeAddress(1, 1, 8, 21);
            sheet.addMergedRegion(cra);
            sheet.addMergedRegion(cra1);
            sheet.addMergedRegion(cra2);
            sheet.addMergedRegion(cra3);
            sheet.addMergedRegion(cra4);
            XSSFRow row1 = sheet.createRow((int) 1);
            XSSFCell cell_1=row1.createCell(0);
            sheet.setDefaultRowHeight((short) 200);
            cell_1.setCellValue(excelHeader[0]);
            XSSFCell cell_2=row1.createCell(1);
            cell_2.setCellValue(excelHeader[1]);
            XSSFCell cell_3=row1.createCell(2);
            cell_3.setCellValue(excelHeader[18]);
            XSSFCell cell_4=row1.createCell(8);
            cell_4.setCellValue(excelHeader[19]);
            cell.setCellStyle(style);
            cell_1.setCellStyle(style);
            cell_2.setCellStyle(style);
            cell_3.setCellStyle(style);
            cell_4.setCellStyle(style);

            XSSFRow row2 = sheet.createRow((int) 2);
            for(int i=0;i<excelHeader.length-4;i++){
                XSSFCell cell_2_1 = row2.createCell(i+2);
                cell_2_1.setCellValue(excelHeader[i+2]);
                sheet.setColumnWidth(cell_2_1.getColumnIndex(), 256 *20);
                cell_2_1.setCellStyle(style);
            }
        }
        if(list != null && list.size() > 0){
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 3);
                Object[] obj = (Object[])list.get(i);
                for(int j=0;j<obj.length;j++){
                    row.createCell(j).setCellValue(obj[j]==null?"":obj[j].toString());
                }
            }
        }
       // fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
        response.reset();
        fileName = URLEncoder.encode(fileName, "utf-8");
        fileName = fileName.replace("+", "%20");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename="+fileName);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setCharacterEncoding("UTF-8");
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }



}
