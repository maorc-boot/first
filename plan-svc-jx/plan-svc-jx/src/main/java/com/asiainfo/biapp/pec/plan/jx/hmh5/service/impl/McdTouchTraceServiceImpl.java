package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.WorkbookUtil;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.jx.hmh5.contant.McdCallExeclContant;
import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.IMcdTouchTraceMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.ReportYjDetailRecording;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdAlarmReportQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.IMcdTouchTraceService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdTouchTraceDetailsVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;

@Service
@Slf4j
public class McdTouchTraceServiceImpl extends ServiceImpl<IMcdTouchTraceMapper, ReportYjDetailRecording> implements IMcdTouchTraceService {

    @Autowired
    private IMcdTouchTraceMapper mcdTouchTraceMapper;

    @Override
    public IPage<McdTouchTraceDetailsVo> queryTouchTraceList(McdAlarmReportQuery query) {
        IPage<McdTouchTraceDetailsVo> page = new Page(query.getPageNum(),query.getPageSize());
        return mcdTouchTraceMapper.queryTouchTraceList(page,query);
    }

    @Override
    public String exportTraceDetails(McdAlarmReportQuery query, HttpServletResponse response){
        final Workbook book = WorkbookUtil.createBook(true);
        String fileName = "预警轨迹明细.xls";
        IPage<McdTouchTraceDetailsVo> page = new Page(query.getPageNum(),query.getPageSize());
        IPage<McdTouchTraceDetailsVo> resultList = mcdTouchTraceMapper.queryTouchTraceList(page,query);

        Sheet sheet = book.createSheet("Sheet");
        Row row = sheet.createRow(Constant.SpecialNumber.ZERO_NUMBER);
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        String[] excelHeader = {"地市名称","区县名称","网格编码","网格名称","渠道编码","渠道名称","经理工号","经理姓名","帐期","来源表名","省专用（预警时间）","是否过滤","过滤原因", "近30天内预警时间","预警号码","预警id", "预警名称", "预警类型", "任务状态", "接触状态", "接触时间",
                "接触时长", "是否修复", "修复时间", "是否手动闭环", "手动闭环时间", "闭环失败原因", "是否解警", "解警原因", "解警时间"};

        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
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

        if(CollectionUtil.isNotEmpty(resultList.getRecords())) {
            int count = 0;
            for (McdTouchTraceDetailsVo record : resultList.getRecords()) {
                count++;
                row = sheet.createRow(count);
                // 地市名称
                row.createCell(Constant.SpecialNumber.ZERO_NUMBER).setCellValue(record.getCityName());

                // 区县名称
                row.createCell(Constant.SpecialNumber.ONE_NUMBER).setCellValue(record.getCountyName());

                // 网格编码
                row.createCell(Constant.SpecialNumber.TWO_NUMBER).setCellValue(record.getAreaId());

                // 网格名称
                row.createCell(Constant.SpecialNumber.THREE_NUMBER).setCellValue(record.getGridName());

                // 渠道编码
                row.createCell(Constant.SpecialNumber.FOUR_NUMBER).setCellValue(record.getChannelId());

                // 渠道名称
                row.createCell(Constant.SpecialNumber.FIVE_NUMBER).setCellValue(record.getChannelName());

                // 经理工号
                row.createCell(Constant.SpecialNumber.SIX_NUMBER).setCellValue(record.getStaffId());

                //  经理姓名
                row.createCell(McdCallExeclContant.SEVEN_NUMBER).setCellValue(record.getStaffName());

                // 帐期
                row.createCell(McdCallExeclContant.ENIGHT_NUMBER).setCellValue(record.getStatDate());

                // 来源表名
                row.createCell(McdCallExeclContant.NINE_NUMBER).setCellValue(record.getSourceTable());

                //  省专用（预警时间）
                row.createCell(McdCallExeclContant.TEN_NUMBER).setCellValue(record.getWarningDate());

                // 是否过滤
                row.createCell(McdCallExeclContant.ELEVEN_NUMBER).setCellValue(record.getFilter());

                // 过滤原因
                row.createCell(McdCallExeclContant.TWELVE_NUMBER).setCellValue(record.getFilterCase());

                // 近30天内预警时间
                row.createCell(McdCallExeclContant.THIRTEEN_NUMBER).setCellValue(record.getEndTime());

                // 预警号码
                row.createCell(McdCallExeclContant.FOURTEEN_NUMBER).setCellValue(record.getProductNo());

                // 预警id
                row.createCell(McdCallExeclContant.FIFITEEN_NUMBER).setCellValue(record.getAlarmId());

                // 预警名称
                row.createCell(McdCallExeclContant.SIXTEEN_NUMBER).setCellValue(record.getAlarmName());

                // 预警类型
                row.createCell(McdCallExeclContant.SEVENTEEN_NUMBER).setCellValue(record.getAlarmType());

                // 任务状态
                row.createCell(McdCallExeclContant.EIGHTEEN_NUMBER).setCellValue(record.getDataStatus());

                // 接触状态
                row.createCell(McdCallExeclContant.NINETEEN_NUMBER).setCellValue(record.getJcStatus());

                // 接触时间
                row.createCell(McdCallExeclContant.TWENTY_NUMBER).setCellValue(record.getEndTime());

                // 接触时长
                row.createCell(McdCallExeclContant.TWENTY_ONE_NUMBER).setCellValue(record.getTalkDuration());

                // 是否修复
                row.createCell(McdCallExeclContant.TWENTY_TWO_NUMBER).setCellValue(record.getXf());

                // 修复时间
                row.createCell(McdCallExeclContant.TWENTY_THREE_NUMBER).setCellValue(record.getDoneDate());

                // 是否手动闭环
                row.createCell(McdCallExeclContant.TWENTY_FOUR_NUMBER).setCellValue(record.getLatestRetureVisitDesc());

                // 手动闭环时间
                row.createCell(McdCallExeclContant.TWENTY_FIVE_NUMBER).setCellValue(record.getLatestRetureVisitTime());

                // 闭环失败原因
                row.createCell(McdCallExeclContant.TWENTY_SIX_NUMBER).setCellValue(record.getBhFalseCase());

                // 是否解警
                row.createCell(McdCallExeclContant.TWENTY_SEVEN_NUMBER).setCellValue(record.getIsClearWarning());

                // 解警原因
                row.createCell(McdCallExeclContant.TWENTY_EIGHT_NUMBER).setCellValue(record.getReaClearWarning());

                // 解警时间
                row.createCell(McdCallExeclContant.TWENTY_NINE_NUMBER).setCellValue(record.getClearWarningDate());

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
            log.error("策略列表导出失败", e);
            return "策略列表导出失败！";
        }
        return "策略列表导出成功！";
    }
}
