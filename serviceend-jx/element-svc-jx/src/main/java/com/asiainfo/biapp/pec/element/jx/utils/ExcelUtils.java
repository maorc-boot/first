package com.asiainfo.biapp.pec.element.jx.utils;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * excel工具类
 * add by ranpf 20221213
 */
public class ExcelUtils {


    protected static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * excel数据转换
     * @param fileInputSteam
     * @return
     */
    public static List<Object[]> readeExcelData(InputStream fileInputSteam,
                                                           int sheetNumber,
                                                           int rowStart) throws Exception{
        log.info("--ExcelUtils--readeExcelData--file="+fileInputSteam.available());
        List<Object[]> result = new ArrayList();
        Workbook workbook = WorkbookFactory.create(fileInputSteam);
        Sheet sheet = workbook.getSheetAt(sheetNumber);
        int rowEnd = sheet.getLastRowNum();
        log.info("--ExcelUtils--readeExcelData--sheet="+sheet.getSheetName()+"--last Rownum="+sheet.getLastRowNum());
        //获取文档内容
        for (int i = rowStart; i <= rowEnd; i++) {
            Row currentRow = sheet.getRow(i);
            if (null==currentRow) {
                continue;
            }
            log.info("--ExcelUtils--readeExcelData--sheet="+sheet.getSheetName()+"--Rownum="+i+"--cell num="+currentRow.getLastCellNum());
            Object[] rowData = new Object[currentRow.getLastCellNum()];
            for (int j = 0; j < currentRow.getLastCellNum(); j++) {
                //将null转化为Blank
                Cell data = currentRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (null!=data) {
                    rowData[j] = doRevert(data);
                }
            }
            result.add(rowData);
        }
        log.info("--ExcelUtils--readeExcelData--sheet="+sheet.getSheetName()+"--result="+result.size());
        return result;
    }

    /**
     * excel数据转换
     * @param data
     * @return
     */
    public static Object doRevert(Cell data){
        Object result;
        switch (data.getCellType()) {
            case  STRING:
                result = data.getRichStringCellValue().getString().trim();
                break;
            case  NUMERIC:
                if (DateUtil.isCellDateFormatted(data)) {
                    result = data.getDateCellValue();
                } else {
                    DecimalFormat df = new DecimalFormat("0");
                    result = df.format(data.getNumericCellValue());
                }
                break;
            case FORMULA:
                result = data.getCellFormula();
                break;
            case BOOLEAN:
                result = data.getBooleanCellValue();
                break;
            default:
                result = "";
        }
        return result;
    }


    public static boolean isMatch(String matchStr,String reg){
        return Pattern.matches(reg, matchStr);
    }

    public static void main(String[] args) {

       System.out.println("Hello World!");
       String reg="[0-9]+\\d+";
       System.out.println(ExcelUtils.isMatch("",reg));
    }
}
