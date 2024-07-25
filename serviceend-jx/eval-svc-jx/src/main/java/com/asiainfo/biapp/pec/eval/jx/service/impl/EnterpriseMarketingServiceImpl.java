package com.asiainfo.biapp.pec.eval.jx.service.impl;

import com.asiainfo.biapp.pec.eval.jx.dao.IEnterpriseMarketingDao;
import com.asiainfo.biapp.pec.eval.jx.req.EnterpriseEvalReq;
import com.asiainfo.biapp.pec.eval.jx.service.IEnterpriseMarketingService;
import com.asiainfo.biapp.pec.eval.jx.vo.Marketing;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnterpriseMarketingServiceImpl  implements IEnterpriseMarketingService {

    @Autowired
    IEnterpriseMarketingDao marketingDao;

    @Override
    public List<Map<String, Object>> getDimManager(String cityId, String countyId) throws Exception {
        return marketingDao.getDimManager(cityId, countyId);
    }

    @Override
    public List<Map<String, Object>> getDimGrid(String cityId, String countyId, String managerId) throws Exception {
        return marketingDao.getDimGrid(cityId, countyId, managerId);
    }

    @Override
    public List<Map<String, Object>> getDimGroups(String cityId, String countyId, String managerId, String gridId) throws Exception {
        return marketingDao.getDimGroups(cityId, countyId, managerId, gridId);
    }

    @Override
    public IPage<Marketing> queryMarketingList(EnterpriseEvalReq enterpriseEvalReq) {
        String cityId = enterpriseEvalReq.getCityId();
        String countyId = enterpriseEvalReq.getCountyId();
        String managerId = enterpriseEvalReq.getManagerId();
        String gridId = enterpriseEvalReq.getGridId();
        String groupsId = enterpriseEvalReq.getGroupsId();

        final Page pager = new Page(enterpriseEvalReq.getPageNum(), enterpriseEvalReq.getPageSize());
        IPage<Marketing> list = new Page();
        if ("0".equals(cityId) || "0".equals(countyId) || "0".equals(managerId) || "0".equals(gridId) || "0".equals(groupsId)) {
            list = marketingDao.queryMarketingNumList(pager, enterpriseEvalReq);
        } else {
            list = marketingDao.queryMarketingList(pager, enterpriseEvalReq);
        }
        return list;
    }

    @Override
    public List<Object> exportMarketingList(EnterpriseEvalReq enterpriseEvalReq) {
        String cityId = enterpriseEvalReq.getCityId();
        String countyId = enterpriseEvalReq.getCountyId();
        String managerId = enterpriseEvalReq.getManagerId();
        String gridId = enterpriseEvalReq.getGridId();
        String groupsId = enterpriseEvalReq.getGroupsId();

        List<Marketing> list = new ArrayList<>();
        if ("0".equals(cityId) || "0".equals(countyId) || "0".equals(managerId) || "0".equals(gridId) || "0".equals(groupsId)) {
            list = marketingDao.queryExportMarketingNumList(enterpriseEvalReq);
        } else {
            list = marketingDao.queryExportMarketingList(enterpriseEvalReq);
        }
        List<Object> returnList = new ArrayList<Object>();
        for(int i=0;i<list.size();i++){
            Object[] obj = new Object[22];
            Marketing market = list.get(i);
            obj[0] = market.getTaskName();
            obj[1] = market.getTaskId();
            obj[2] = market.getCampName();
            obj[3] = market.getCampId();
            obj[4] = market.getCityName();
            obj[5] = market.getCountyName();
            obj[6] = market.getGridName();
            obj[7] = market.getGroupsName();
            obj[8] = market.getManagerName();
            obj[9] = market.getGroupsType();
            obj[10] = market.getStartTime();
            obj[11] = market.getEndTime();
            obj[12] = market.getTargetUserNum();
            obj[13] = market.getWarmUpUserNum();//预热
            obj[14] = market.getIntimeCampCount();//驻场
            obj[15] = market.getOfflineCampCount();//执行及时数
            obj[16] = market.getTimelinessRatio();//执行及时率
            obj[17] = market.getIssueUserNum();//下发数
            obj[18] = market.getCountactUserNum();//接触数
            obj[19] = market.getContactRate();//接触成功率
            obj[20] = market.getSucessUserNum();//营销成功数
            obj[21] = market.getConversionRate();//营销成功率
            returnList.add(obj);
        }
        return returnList;
    }

    @Override
    public Map<String, Object> saveCase(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String taskId = String.valueOf(paramMap.get("taskId"));
        List<Map<String, Object>> list = marketingDao.getCaseData(taskId);
        paramMap.put("marketContent", list.get(0).get("MARKET_CONTENT"));
        paramMap.put("productContent", list.get(0).get("PRODUCT_CONTENT"));
        paramMap.put("customerContent", list.get(0).get("CUSTOMER_CONTENT"));
        paramMap.put("bizType", list.get(0).get("BIZ_TYPE"));
        long size = marketingDao.saveCase(paramMap);
        if (size < 1) {
            map.put("status",201);
            map.put("message","保存失败");
        }else {
            map.put("status",200);
            map.put("message","保存成功");
        }
        return map;
    }

    @Override
    public void exportExcel(String fileName, String[] excelHeader, List<Object> list, HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Sheet");
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        //居中对齐
        style.setAlignment(HorizontalAlignment.CENTER);
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                Object[] obj = (Object[]) list.get(i);
                for (int j = 0; j < obj.length; j++) {
                    row.createCell(j).setCellValue(obj[j] == null ? "" : obj[j].toString());
                }
            }
        }
        fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
}
