package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.WorkbookUtil;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.jx.hmh5.contant.McdCallExeclContant;
import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.IMcdAlarmEnergyMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdAlarmReportQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.IMcdAlarmEnergyService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdAlarmEfficacyVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;

@Service
@Slf4j
public class McdAlarmEnergyServiceImpl implements IMcdAlarmEnergyService {

    @Autowired
    private IMcdAlarmEnergyMapper alarmEnergyMapper;

    @Override
    public IPage queryAlarmEnergyList(McdAlarmReportQuery query) {
        IPage<McdAlarmEfficacyVo> page = new Page(query.getPageNum(), query.getPageSize());
        return alarmEnergyMapper.queryAlarmEnergyList(page, query);
    }

    @Override
    public String exportEnergy(McdAlarmReportQuery query, HttpServletResponse response) {
        final Workbook book = WorkbookUtil.createBook(true);
        String fileName = "预警能效分析.xls";
        IPage<McdAlarmEfficacyVo> page = new Page(query.getPageNum(), query.getPageSize());
        IPage<McdAlarmEfficacyVo> resultList = alarmEnergyMapper.queryAlarmEnergyList(page, query);

        Sheet sheet = book.createSheet("Sheet");
        Row row = sheet.createRow(Constant.SpecialNumber.ZERO_NUMBER);
        Row row1 = sheet.createRow(Constant.SpecialNumber.ONE_NUMBER);
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        String[] topHeader = {"地市","区县","网格","经理工号","经理名称", "预警类型", "预警名称", "任务下发@7@8", "过滤情况TOP3@9@15",
                "闭环情况@16@18", "下发APP@19@20", "过滤(修复/接触)情况@21@23",
                "闭环(修复/接触)情况@24@32", "接触状态@33@36", "修复状态@37@38",
                "闭环状态@39@41"};

        String[] excelHeader = {"任务数量", "用户数量", "过滤总数量", "TOP1过滤原因", "数量", "TOP2过滤原因", "数量",
                "TOP3过滤原因", "数量", "闭环总数量", "人工闭环", "自动解警", "任务数量", "用户数量", "过滤总数量", "其中：已接触",
                "其中：已修复", "闭环总数量", "其中：已接触", "其中：已修复", "人工闭环", "其中：已接触", "其中：已修复", "自动解警", "其中：已接触",
                "其中：已修复", "未接触", "未接通", "已接触未达时长", "已接触", "未修复", "已修复", "未闭环", "人工闭环", "自动解警"};
        for (int i = 0; i < topHeader.length; i++) {
            Cell cell;
            if (i < 7) {
                sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
                style.setAlignment(HorizontalAlignment.CENTER);
                cell = row.createCell(i);
                cell.setCellValue(topHeader[i]);
            } else {
                String[] split = topHeader[i].split("@");
                sheet.addMergedRegion(new CellRangeAddress(0, 0, Integer.parseInt(split[1]), Integer.parseInt(split[2])));
                style.setAlignment(HorizontalAlignment.CENTER);
                cell = row.createCell(Integer.parseInt(split[1]));
                cell.setCellValue(split[0]);
            }
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }

        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row1.createCell(i + 7);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
            int maxWith = 255 * 256;
            int newWidth = sheet.getColumnWidth(i) * 3;
            if (newWidth < maxWith) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, maxWith / 2);
            }
        }

        if (CollectionUtil.isNotEmpty(resultList.getRecords())) {
            int count = Constant.SpecialNumber.TWO_NUMBER;
            for (McdAlarmEfficacyVo record : resultList.getRecords()) {
                Row  row2 = sheet.createRow(count);
                // 地市0
                row2.createCell(Constant.SpecialNumber.ZERO_NUMBER).setCellValue(record.getCityName());

                // 区县名称1
                row2.createCell(Constant.SpecialNumber.ONE_NUMBER).setCellValue(record.getCountyName());

                // 网格名称2
                row2.createCell(Constant.SpecialNumber.TWO_NUMBER).setCellValue(record.getGridName());

                // 经理编号3
                row2.createCell(Constant.SpecialNumber.THREE_NUMBER).setCellValue(record.getStaffId());

                // 客户经理 4
                row2.createCell(Constant.SpecialNumber.FOUR_NUMBER).setCellValue(record.getStaffName());

                // 预警名称5
                row2.createCell(Constant.SpecialNumber.FIVE_NUMBER).setCellValue(record.getAlarmName());

                // 预警类型6
                row2.createCell(Constant.SpecialNumber.SIX_NUMBER).setCellValue(record.getAlarmType());

                // 任务数量7
                row2.createCell(McdCallExeclContant.SEVEN_NUMBER).setCellValue(record.getIssuedTaskNum());

                // 用户数量8
                row2.createCell(McdCallExeclContant.ENIGHT_NUMBER).setCellValue(record.getIssuedUserNum());

                // 过滤总数量9
                row2.createCell(McdCallExeclContant.NINE_NUMBER).setCellValue(record.getTaskFilterNum());

                // TOP1过滤原因10
                row2.createCell(McdCallExeclContant.TEN_NUMBER).setCellValue(record.getTaskFilterReason1());

                // 数量11
                row2.createCell(McdCallExeclContant.ELEVEN_NUMBER).setCellValue(record.getTaskFilterNum1());

                // TOP2过滤原因12
                row2.createCell(McdCallExeclContant.TWELVE_NUMBER).setCellValue(record.getTaskFilterReason2());

                // 数量13
                row2.createCell(McdCallExeclContant.THIRTEEN_NUMBER).setCellValue(record.getTaskFilterNum2());

                // TOP3过滤原因14
                row2.createCell(McdCallExeclContant.FOURTEEN_NUMBER).setCellValue(record.getTaskFilterReason3());

                // 数量15
                row2.createCell(McdCallExeclContant.FIFITEEN_NUMBER).setCellValue(record.getTaskFilterNum3());

                // 闭环总数量16
                row2.createCell(McdCallExeclContant.SIXTEEN_NUMBER).setCellValue(record.getClosedNum());

                // 人工闭环17
                row2.createCell(McdCallExeclContant.SEVENTEEN_NUMBER).setCellValue(record.getLabourClosed());

                // 自动解警18
                row2.createCell(McdCallExeclContant.EIGHTEEN_NUMBER).setCellValue(record.getAutoClosed());

                // 任务数量19
                row2.createCell(McdCallExeclContant.NINETEEN_NUMBER).setCellValue(record.getNormalTaskNum());

                // 用户数量20
                row2.createCell(McdCallExeclContant.TWENTY_NUMBER).setCellValue(record.getNormalUserNum());

                // 过滤总数量21
                row2.createCell(McdCallExeclContant.TWENTY_ONE_NUMBER).setCellValue(record.getUnNormalTaskNum());

                //其中 已接触22
                row2.createCell(McdCallExeclContant.TWENTY_TWO_NUMBER).setCellValue(record.getUnNormalReached());

                // 其中已修复23
                row2.createCell(McdCallExeclContant.TWENTY_THREE_NUMBER).setCellValue(record.getUnNormalFix());

                // 闭环总数量24
                row2.createCell(McdCallExeclContant.TWENTY_FOUR_NUMBER).setCellValue(record.getClosedTermNum());

                // 其中：已接触25
                row2.createCell(McdCallExeclContant.TWENTY_FIVE_NUMBER).setCellValue(record.getClosedTermReached());

                // 其中：已修复26
                row2.createCell(McdCallExeclContant.TWENTY_SIX_NUMBER).setCellValue(record.getClosedTermFixed());

                // 人工闭环27
                row2.createCell(McdCallExeclContant.TWENTY_SEVEN_NUMBER).setCellValue(record.getCloseLabour());

                // 其中：已接触28
                row2.createCell(McdCallExeclContant.TWENTY_EIGHT_NUMBER).setCellValue(record.getClosedLabourReached());

                // 其中：已修复29
                row2.createCell(McdCallExeclContant.TWENTY_NINE_NUMBER).setCellValue(record.getClosedLabourFiexd());

                // 自动解警30
                row2.createCell(McdCallExeclContant.THIRTY_NUMBER).setCellValue(record.getClosedAuto());

                // 其中：已接触31
                row2.createCell(McdCallExeclContant.THIRTY_ONE_NUMBER).setCellValue(record.getClosedAutoReached());

                //  其中：已修复32
                row2.createCell(McdCallExeclContant.THIRTY_TWO_NUMBER).setCellValue(record.getClosedAutoFiexd());

                // 未接触33
                row2.createCell(McdCallExeclContant.THIRTY_THREE_NUMBER).setCellValue(record.getUnTouch());

                // 未接通34
                row2.createCell(McdCallExeclContant.THIRTY_FOUR_NUMBER).setCellValue(record.getUnGetTouch());

                // 已接触未达时长35
                row2.createCell(McdCallExeclContant.THIRTY_FIVE_NUMBER).setCellValue(record.getNoReachGetTouch());

                // 已接触36
                row2.createCell(McdCallExeclContant.THIRTY_SIX_NUMBER).setCellValue(record.getReachGetTouch());

                // 未修复37
                row2.createCell(McdCallExeclContant.THIRTY_SEVEN_NUMBER).setCellValue(record.getFiexdRepairState());

                // 已修复38
                row2.createCell(McdCallExeclContant.THIRTY_EIGHT_NUMBER).setCellValue(record.getClosedRepairState());

                // 未闭环39
                row2.createCell(McdCallExeclContant.THIRTY_NINE_NUMBER).setCellValue(record.getCloseLoopStatus());

                // 人工闭环40
                row2.createCell(McdCallExeclContant.FOURTY).setCellValue(record.getCloseLabourDealarm());

                // 自动解警41
                row2.createCell(McdCallExeclContant.FOURTY_ONE_NUMBER).setCellValue(record.getCloseAutoDealarm());

                count++;
            }
        }
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "iso8859-1");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            OutputStream ouputStream = response.getOutputStream();
            book.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            log.error("预警能效分析导出失败", e);
            return "预警能效分析导出失败！";
        }
        return "预警能效分析导出成功！";
    }
}
