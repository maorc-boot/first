package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;


import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.McdFrontGuardListDao;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontGuardUserQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontGuardListService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontGuardUserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 看护管理
 * @author ranpf   2023-02-15
 *
 */
@Service
@Slf4j
public class McdFrontGuardListServiceImpl implements McdFrontGuardListService {

    private static Pattern NUMBER_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
    private static  final int  PAGE_SIEZ = 30000;
    @Resource
    private McdFrontGuardListDao mcdFrontGuardListDao;

    @Override
    public IPage<McdFrontGuardUserInfo> queryGuardUserList(McdFrontGuardUserQuery req) {
        Page page = new Page();
        page.setSize(req.getSize());
        page.setCurrent(req.getCurrent());

        return mcdFrontGuardListDao.queryGuardUserList(page, req);
    }

    @Override
    public int getGuardUserCount(McdFrontGuardUserQuery req) {
        return mcdFrontGuardListDao.getGuardUserCount(req);
    }

    @Override
    public void exportGuardUserExcel(McdFrontGuardUserQuery req, HttpServletResponse response)  {
        List<Object> rs = new ArrayList<>();
        int total = mcdFrontGuardListDao.getGuardUserCount(req);
        int pageSize = PAGE_SIEZ;
        int totalPage = (total + pageSize -1)/pageSize;
        Page page = new Page();
        page.setSize(pageSize);
        for (int i=1;i<= totalPage;i++){
            page.setCurrent(i);
            IPage<McdFrontGuardUserInfo> list = mcdFrontGuardListDao.queryGuardUserList(page,req);
            for (McdFrontGuardUserInfo guardUserInfo : list.getRecords()) {
                Object[] o = new Object[8];
                if(guardUserInfo.getProductNo() != null && guardUserInfo.getProductNo().length() >7){
                    String productNo = guardUserInfo.getProductNo();
                    o[0] = productNo.substring(0,3)+"****"+productNo.substring(7);
                }else{
                    o[0] = "";
                }
                o[1] = guardUserInfo.getManagerId();
                o[2] = guardUserInfo.getChannelId();
                o[3] = guardUserInfo.getChannelName();
                o[4] = guardUserInfo.getCityName();
                o[5] = guardUserInfo.getCountyName();
                o[6] = guardUserInfo.getVipCustomerFlag();
                o[7] = guardUserInfo.getGuardType();
                rs.add(o);
            }
        }
        String[] excelHeader = {"手机号码", "看护工号", "看护渠道", "看护渠道名称","地市名称","区县名称","是否重点客户", "看护类型"};
        exportExcel("客户通看护关系导出.xls", excelHeader, rs, response);
    }


    public void exportExcel(String fileName, String[] excelHeader, List<Object> list, HttpServletResponse response)   {
        HSSFWorkbook wb = new HSSFWorkbook();
        try {
            HSSFSheet sheet = wb.createSheet("Sheet");
            HSSFRow row = sheet.createRow((int) 0);
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            sheet.setDefaultColumnWidth(15);
            HSSFCell cell;
            for (int i = 0; i < excelHeader.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(excelHeader[i]);
                row.setHeight((short) 0x150);
                cell.setCellStyle(style);
            }
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    row = sheet.createRow(i + 1);
                    row.setHeight((short) 0x150);
                    Object[] obj = (Object[])list.get(i);
                    String objValue;
                    for (int j = 0; j < obj.length; j++) {
                        cell = row.createCell(j);
                        objValue = obj[j] == null ? "" : obj[j].toString();
                        if (checkStrIsNum(objValue) && checkIsDecimal(objValue)) {
                            cell.setCellValue(new BigDecimal(objValue).doubleValue());
                        } else {
                            cell.setCellValue(objValue);
                        }
                        cell.setCellStyle(style);

                    }
                }
            }
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            log.error("导出失败", e);
        } finally {
            try {
                wb.close();
            }catch (Exception e){
                log.error("关闭流异常",e);
            }

        }
    }

    /**
     * 利用正则表达式来判断字符串是否为数字
     */
    public static boolean checkStrIsNum(String str) {
        try {
            // 先将str转成BigDecimal，然后在转成String
            new BigDecimal(str).toString();
        } catch (Exception e) {
            // 如果转换数字失败，说明该str并非全部为数字
            return false;
        }
        Matcher isNum = NUMBER_PATTERN.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是小数
     * @param obj
     * @return
     */
    public static boolean checkIsDecimal(String obj){
        if (obj.contains(".")){
            return true;
        }
        return false;
    }
}
