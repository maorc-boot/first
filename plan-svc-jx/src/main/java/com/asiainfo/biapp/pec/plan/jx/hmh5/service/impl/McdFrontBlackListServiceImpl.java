package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;

import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.McdFrontBlacklistCustDao;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontBlacklistCust;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontBlackListQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontBlackListService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class McdFrontBlackListServiceImpl extends ServiceImpl<McdFrontBlacklistCustDao, McdFrontBlacklistCust> implements McdFrontBlackListService {


    private static final String[] TITLE = {"手机号码", "地市编码","备注", "导入类型(1导入,2删除)"};
    private static final String[] IMPORT_DATA_TEMPLATE = {"18823456789", "791","样例数据", "1" };
    private static final String IMPORT_DATA = "2099-01-01";

    @Resource
    private McdFrontBlacklistCustDao mcdFrontBlacklistCustDao;
    @Override
    public Page<McdFrontBlacklistCust> queryMcdFrontBlackList(McdFrontBlackListQuery req) {
        return null;
    }


    @Override
    public void downloadImportOrDelBlackListTemplate(HttpServletResponse response) {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        //sheet.createFreezePane(8,100);// 冻结
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFDataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置列的样式 和 自动列宽
        for(int i = 0; i < TITLE.length ; i++) {
            sheet.setDefaultColumnStyle(i,style);
            sheet.autoSizeColumn(i);
            int maxWith = 255 * 256;
            int newWidth = sheet.getColumnWidth(i) * 17 / 10;
            if (newWidth < maxWith) {
                sheet.setColumnWidth(i, newWidth);
            }else {
                sheet.setColumnWidth(i, maxWith / 2);
            }
        }

        //字体样式
        HSSFFont columnHeadFont = wb.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeightInPoints((short) 12);
        //columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(columnHeadFont);


        HSSFCell cell = row.createCell((short)0);
        for (int i = 0; i < TITLE.length; i++) {
            HSSFCellStyle style_0 = wb.createCellStyle();
            style_0.setAlignment(HorizontalAlignment.CENTER);
            HSSFFont columnHeadFont_0 = wb.createFont();
            columnHeadFont_0.setFontName("宋体");
            columnHeadFont_0.setFontHeightInPoints((short) 12);
            columnHeadFont_0.setBold(true);
            style_0.setFont(columnHeadFont_0);
            cell.setCellType(CellType.STRING);
            cell = row.createCell(i);
            cell.setCellValue(TITLE[i]);
            cell.setCellStyle(style_0);
        }

        HSSFRow row1 = sheet.createRow((short) 1);
        HSSFCell cell1 = row1.createCell((short)0);
        for (int i = 0; i < IMPORT_DATA_TEMPLATE.length; i++) {
            HSSFCellStyle style_0 = wb.createCellStyle();
            style_0.setAlignment(HorizontalAlignment.CENTER);
            HSSFFont columnHeadFont_0 = wb.createFont();
            columnHeadFont_0.setFontName("宋体");
            columnHeadFont_0.setFontHeightInPoints((short) 12);
            columnHeadFont_0.setBold(true);
            style_0.setFont(columnHeadFont_0);
            cell1.setCellType(CellType.STRING);
            cell1 = row1.createCell(i);
            cell1.setCellValue(IMPORT_DATA_TEMPLATE[i]);
            cell1.setCellStyle(style_0);
        }

        OutputStream os = null;
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            String fileName = "McdCustomerBlackListTemplate.xls";
            fileName = URLEncoder.encode(fileName, "utf-8");
            fileName = fileName.replace("+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
            response.flushBuffer();
            os = response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            log.error("客户通黑名单模板下载异常 ",e);
        }finally{
            if ( os != null){
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    log.error("客户通黑名单模板下载关流异常 : ", e );
                }
            }
        }
    }


    @Override
    public boolean impOrDelBlacklistFile(MultipartFile impOrDelBlacklistFile, String userId) throws Exception {
        InputStream inputStream = null;
        Workbook book = null;
        try {
            inputStream = impOrDelBlacklistFile.getInputStream();
            String name = impOrDelBlacklistFile.getOriginalFilename();
            if (name.endsWith("xls") || name.endsWith("XLS")) {
                book = new HSSFWorkbook(inputStream);
            } else if (name.endsWith("xlsx") || name.endsWith("XLSX")) {
                book = new XSSFWorkbook(inputStream);
            } else {
                throw new Exception("文件类型错误");
            }
            Sheet sheet = book.getSheetAt(0);
            //get blacklistCust list info
            Map<String,Object> resultMap = getContentWithSheet(sheet,userId);

            List<McdFrontBlacklistCust> importList =(List<McdFrontBlacklistCust>) resultMap.get("import");
            List<McdFrontBlacklistCust> deleteList =(List<McdFrontBlacklistCust>) resultMap.get("delete");

            int imoNum = 0;
            int delNum = 0;

            if (!importList.isEmpty() && importList.size() > 0){
                imoNum = mcdFrontBlacklistCustDao.batchSaveBlacklist(importList);
            }

            if (!deleteList.isEmpty() && deleteList.size() > 0){
                for (McdFrontBlacklistCust blacklistCust : deleteList) {
                     mcdFrontBlacklistCustDao.deleteById(blacklistCust.getProcuctNo());
                    delNum++;
                }
            }



            log.info("impOrDelBlacklistFile-batchImport num is :{}", delNum);
            log.info("impOrDelBlacklistFile-batchDelete num is :{}", imoNum);
            return (imoNum > 0) || (delNum > 0);
        } catch (Exception e) {
            log.error("客户通黑名单excel导入异常", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭流失败", e);
                }
            }
        }
        return false;
    }


    @Override
    public boolean impOrDelBlacklistFile(File file, String userId) throws Exception {
        InputStream inputStream = null;
        Workbook book = null;
        try {
            inputStream = new FileInputStream(file);
            String name = file.getName();
            if (name.endsWith("xls") || name.endsWith("XLS")) {
                book = new HSSFWorkbook(inputStream);
            } else if (name.endsWith("xlsx") || name.endsWith("XLSX")) {
                book = new XSSFWorkbook(inputStream);
            } else {
                throw new Exception("文件类型错误");
            }
            Sheet sheet = book.getSheetAt(0);
            //get blacklistCust list info
            Map<String,Object> resultMap = getContentWithSheet(sheet,userId);

            List<McdFrontBlacklistCust> importList =(List<McdFrontBlacklistCust>) resultMap.get("import");
            List<McdFrontBlacklistCust> deleteList =(List<McdFrontBlacklistCust>) resultMap.get("delete");

            int imoNum = 0;
            int delNum = 0;

            if (!importList.isEmpty() && importList.size() > 0){
                this.saveOrUpdateBatch(importList ,1000);
            }

            if (!deleteList.isEmpty() && deleteList.size() > 0){
                for (McdFrontBlacklistCust blacklistCust : deleteList) {
                    mcdFrontBlacklistCustDao.deleteById(blacklistCust.getProcuctNo());
                    delNum++;
                }
            }

            log.info("impOrDelBlacklistFile-batchImport num is :{}", delNum);
            log.info("impOrDelBlacklistFile-batchDelete num is :{}", imoNum);
            return (imoNum > 0) || (delNum > 0);
        } catch (Exception e) {
            log.error("客户通黑名单excel导入异常", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭流失败", e);
                }
            }
        }
        return false;
    }



    /**
     * phase excel 解析导入excel;
     * @param sheet sheet
     * @return
     */
    private Map<String,Object> getContentWithSheet(Sheet sheet, String userId) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        if (sheet != null && sheet.getLastRowNum() > 0) {
            Row row = sheet.getRow(0);
            short lastCellNum = row.getLastCellNum();
            if (lastCellNum != 4) {
                throw new Exception("未使用黑名单导入模板导入数据，请重新选择内容文件!");
            }
        }
        List<McdFrontBlacklistCust> importList = new ArrayList<>();
        List<McdFrontBlacklistCust> deleteList = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (null == row) {
                continue;
            }
            //not allow some important information to be null
            if (StringUtils.isEmpty(this.getCellString(row.getCell(0))) ||
                    StringUtils.isEmpty(this.getCellString(row.getCell(1))) ||
                    StringUtils.isEmpty(this.getCellString(row.getCell(3)))) {
                log.info("导入文件行：" + i  + "手机号码/地市编码/导入类型 有一项或多项内容为空,跳过！");
                continue;
            }

            McdFrontBlacklistCust blacklistCust = new McdFrontBlacklistCust();

            String phoneNo = this.getCellString(row.getCell(0));
            if (phoneNo.length() != 11){
                log.error("黑名单导入第"+i+"行数据手机号码长度不是11位!");
                continue;
            }

            //检查类型是否是 1,2
            String blackListType = this.getCellString(row.getCell(3));
            if ("2".equals(blackListType)){
                McdFrontBlacklistCust cust = new McdFrontBlacklistCust();
                cust.setProcuctNo(phoneNo);
                deleteList.add(cust);
                continue;
            }

            String content = this.getCellString(row.getCell(2));
            if (content.length() > 255){
                log.error("黑名单导入第"+i+"行数据备注内容长度超过255!");
                continue;
            }

            blacklistCust.setProcuctNo(phoneNo);
            blacklistCust.setCityId(this.getCellString(row.getCell(1)));
            blacklistCust.setRemarks(content);
            blacklistCust.setCreateBy(userId);
            blacklistCust.setJoinType(1);//1黑名单,2退订
            blacklistCust.setEffectEndTime(getInvalidDate(IMPORT_DATA));
            blacklistCust.setDataState(1);//生效状态


            importList.add(blacklistCust);
        }

        resultMap.put("import",importList);
        resultMap.put("delete",deleteList);
        return resultMap;
    }

    /**
     * 获取Excel中的数值为字符串
     *
     * @param cell 单元格
     * @return
     */
    private String getCellString(Cell cell) {
        if (cell == null) {
            return "";
        }
        String cellString = "";

        switch (cell.getCellType()) {
            case STRING: // 字符串
                cellString = cell.getStringCellValue();
                break;
            case NUMERIC: // 数字
                double cellDouble = cell.getNumericCellValue();
                if (this.isEqualDouble(Math.round(cellDouble), cellDouble)) {
                    cellString = String.valueOf((long) cellDouble);
                } else {
                    cellString = String.valueOf(cellDouble);
                }
                break;
            case BOOLEAN: // Boolean
                cellString = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: // 公式
                cellString = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: // 空值
                cellString = "";
                break;
            case ERROR: // 故障
                cellString = "";
                break;
            default:
                cellString = "";
                break;
        }
        return cellString;
    }


    private Date getInvalidDate(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date = sdf.parse(dateStr);
            return date;
        }catch (Exception e){
            log.error("导入时间转换异常",e);
        }
        return null;
    }

    /**
     * 判断浮点型是否相等
     * @param a 参数
     * @param b 参数
     * @return true/false
     */
    private boolean isEqualDouble(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isInfinite(a) || Double.isInfinite(b)) {
            return false;
        }
        return (a - b) < 0.001d;
    }
}
