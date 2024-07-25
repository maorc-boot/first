package com.asiainfo.biapp.pec.eval.jx.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * description: 导出excel工具类
 *
 * @author: lvchaochao
 * @date: 2023/1/11
 */
@Component
public class ExportExcelUtil {

    /**
     * 导出excel
     *
     * @param excelHeader excel头
     * @param list        列表数据
     * @param response    响应
     * @throws Exception 异常
     */
    public void exportExcel(String[] excelHeader, List<Object> list, HttpServletResponse response, String fileName) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Sheet");
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            Object[] obj = (Object[]) list.get(i);
            for (int j = 0; j < obj.length; j++) {
                row.createCell(j).setCellValue(obj[j] == null ? "" : obj[j].toString());
            }
        }
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8") + ".xls");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
}
