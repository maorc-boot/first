package com.asiainfo.biapp.pec.plan.jx.link.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.link.dao.IMpmCampSegInfoDao;
import com.asiainfo.biapp.pec.plan.jx.link.dao.LinkDao;
import com.asiainfo.biapp.pec.plan.jx.link.service.IMcdPlanService;
import com.asiainfo.biapp.pec.plan.jx.link.service.LinkService;
import com.asiainfo.biapp.pec.plan.jx.link.vo.Link;
import com.asiainfo.biapp.pec.plan.jx.link.vo.McdCampDef;
import com.asiainfo.biapp.pec.plan.jx.link.vo.McdDeleteLinkQuery;
import com.asiainfo.biapp.pec.plan.jx.link.vo.McdDimCampStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class LinkServiceImpl implements LinkService {

    @Resource
    private LinkDao linkDao;

    @Resource
    private IMpmCampSegInfoDao campSegInfoDao;

    @Autowired
    private IMcdPlanService mcdPlanService;


    @Override
    public String saveOrUpdateLink(Link link) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(Long.parseLong(link.getCreateDate())));
        link.setCreateDate(date);

        String status = "2";
        boolean isExist;
        if ("3".equals(link.getStatus())){   //  如果link的status为3,则为修改状态
            isExist = linkDao.checkLinkIsExist(link);
            if (isExist){
                linkDao.updateLinkStatus(link);
                status = "1";
            }
        }
        if ("2".equals(link.getStatus())){ //如果link的status为2，则为删除,
            linkDao.updateLinkStatus(link);
            status = "1";
        }else if ("1".equals(link.getStatus())){ //如果link的status为1，则为修改
            isExist = linkDao.checkLinkIsExist(link);
            if (isExist){
                linkDao.updateLink(link);
                status = "3";
            }
        }else if ("0".equals(link.getStatus())){ //如果link的status为0，则为新增
            isExist = linkDao.checkLinkIsExist(link);
            if (!isExist){
                link.setStatus("1");
                linkDao.saveLink(link);
                status = "1";
            }
        }
        return status; //status=1 新增或修改状态成功，status=2 均失败，status=3 修改链接成功
    }

    @Override
    public Map<String, Object> searchAllLink(Map paramMap) throws Exception {
        List<Link> links = new ArrayList<>();
        int pageLiniks;
        Map<String, Object> result = new HashMap<>();
        String pageNum = (String) paramMap.get("pageNum");
        String pageSize = (String) paramMap.get("pageSize");
        String aSwitch = (String) paramMap.get("switch");
        String userId = (String) paramMap.get("userId");

        int begin = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize);
        int end = begin + Integer.parseInt(pageSize);
        paramMap.put("pageNum",begin);
        paramMap.put("pageSize",end);

        links =  linkDao.searchAllLink(paramMap);
        pageLiniks =  linkDao.tosizeAllLinik(paramMap);
        if ("1".equals(aSwitch)) {//1是自己创建的可以修改删除
            for (int i = 0; i < links.size(); i++) {
                if (userId != null && userId.equals(links.get(i).getCreator())) {
                    links.get(i).setAswitch("1");
                }else {
                    links.get(i).setAswitch("2");
                }
            }
        } else if ("2".equals(aSwitch)) {//2是其他界面引用的 ,不让修改
            for (int i = 0; i < links.size(); i++) {
                links.get(i).setAswitch("2");
            }
        }
        List<String> list ;
        for (int i = 0; i < links.size(); i++) {
            list = linkDao.queryCampIdsByLinkUrlNum(links.get(i).getUrl(), links.get(i).getUrl());
            int totalSize = list.size();
            links.get(i).setCampSize(String.valueOf(totalSize));
        }
        result.put("totalSize",pageLiniks);
        result.put("pageSize",pageSize);
        result.put("pageNum",pageNum);
        result.put("list",links);
        return result;
    }

    @Override
    public Map<String, Object> searchCampsByUrl(String url, String pageSize, String pageNum) throws Exception {
        String urls = url;
        Map<String,Object> result = new HashMap<>();
        List<String> campIds = linkDao.searchCampIdsByLinkUrl(url, urls, pageSize, pageNum);
        result.put("list",campIds);
        List<McdCampDef> camps = new ArrayList<>();
        McdDimCampStatus dimCampsegStat;
        for (String campId : campIds) {
            McdCampDef campSegInfo = campSegInfoDao.getCampSegInfo(campId);
            campSegInfo.setStatId((short) 20);
            camps.add(campSegInfo);
        }
        for (McdCampDef camp : camps) {
            dimCampsegStat = campSegInfoDao.getDimCampsegStat(String.valueOf(camp.getStatId()));
            camp.setStatusName(dimCampsegStat.getCampsegStatName());
        }
        result.put("camps",camps);
        return result;
    }

    @Override
    public String proxyPost(String url, String params) {
        String result = null;
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            httpPost = new HttpPost(url);
            // 设置通用的请求属性
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            // 往服务器里面发送数据
            StringEntity entity = new StringEntity(params, StandardCharsets.UTF_8);
            httpPost.setEntity(entity);
            // 建立实际的连接
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            System.out.println("请求返回状态："+status.getStatusCode());
            // 获取返回数据
            if (status.getStatusCode() == 200) {
                HttpEntity responseEntity = response.getEntity();
                result= EntityUtils.toString(responseEntity,"UTF-8");
            }
        } catch (Exception e) {
            log.error("error:"+e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("error:"+e);
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error("error:"+e);
            }
        }
        return result;
    }

    /**
     * 下载批量导入链接模板文件
     * @throws Exception
     * @param response
     */
    @Override
    public void downloadLinkTemplate(HttpServletResponse response) throws Exception {
        XSSFWorkbook book = new XSSFWorkbook();
        //新建主表
        XSSFSheet sheet = book.createSheet("链接模板");

        //创建模板表
        XSSFCellStyle style = book.createCellStyle();
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        sheet.setDefaultColumnWidth(15);
        XSSFRow sheetRow0 = sheet.createRow(0);
        sheetRow0.createCell(0).setCellValue("链接名称");
        sheetRow0.createCell(1).setCellValue("链接地址");
        sheetRow0.createCell(2).setCellValue("关联产品ID");
        sheetRow0.createCell(3).setCellValue("说明");

        //设置单元格格式
        for (int i = 0; i < 4; i++) {
            sheetRow0.getCell(i).setCellStyle(style);
        }

        //创建EXCEL文件
        String name = "templateOfLink.xlsx";
        name = new String(name.getBytes("GBK"), "iso8859-1");
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename="+name);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        OutputStream ouputStream = response.getOutputStream();
        book.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 批量添加链接
     * @param uploadBatchLinkFile
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> uploadBatchLinkFile(MultipartFile uploadBatchLinkFile, UserSimpleInfo user) throws Exception {
        InputStream is = null;
        XSSFWorkbook book;
        Map<String, String> map = new HashMap<>();
        List<Link> linkList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        Pager pager = new Pager();
        pager.setPageNum(1);
        pager.setPageSize(1000000);
        List<Map<String, Object>> plans = mcdPlanService.getPlanByCondition("", "", "", pager);

        Date date;
        List<String> urls = new ArrayList<>();

        Map<String, Object> paramMap = new HashMap();
        paramMap.put("pageNum","1");
        paramMap.put("pageSize","1000000");
        List<Link> links = linkDao.searchAllLink(paramMap);
        StringBuilder alerts = new StringBuilder();
        boolean flag = false;
        try {
            is = uploadBatchLinkFile.getInputStream();
            book = new XSSFWorkbook(is);
            XSSFSheet sheet = book.getSheetAt(book.getSheetIndex("链接模板"));
            int rowNum = sheet.getLastRowNum();
            int sucNum = 0;
            for (int i = 1; i <= rowNum; i++) {
                XSSFRow row = sheet.getRow(i);
                String alert;
                //检查必填项是否为空，存在为空，则跳过该行
                if (StringUtils.isEmpty(this.getCellString(row.getCell(0)))
                        || StringUtils.isEmpty(this.getCellString(row.getCell(1)))
                        || StringUtils.isEmpty(this.getCellString(row.getCell(2)))){
                    alert = "第"+i+"行数据存在必填项为空;";
                    log.info(alert);
                    alerts.append(alert);
                    continue;
                }
                Link link = new Link();
                link.setCreator(user.getUserId());

                String linkName = this.getCellString(row.getCell(0));
                if (linkName.length()>50){
                    alert = "第"+i+"行数据名称字数超过50;";
                    log.info(alert);
                    alerts.append(alert);
                    continue;
                }
                link.setName(linkName);

                String url = this.getCellString(row.getCell(1));
                flag = false;
                for (Link link1 : links) {
                    if (url.equals(link1.getUrl())){
                        flag = true;
                        break;
                    }
                }
                if (flag){
                    alert = "第"+i+"行数据链接在数据库中已存在;";
                    log.info(alert);
                    alerts.append(alert);
                    continue;
                }
                if (urls.contains(url)){
                    alert = "第"+i+"行数据链接在导入数据中存在;";
                    log.info(alert);
                    alerts.append(alert);
                    continue;
                }
                urls.add(url);
                link.setUrl(url);

                String planId = this.getCellString(row.getCell(2));
                String plan_id;
                String plan_name;
                for (Map<String, Object> plan : plans) {
                    plan_id = (String) plan.get("PLAN_ID");
                    if (planId.equals(plan_id)){
                        link.setPlanId(planId);
                        plan_name = (String) plan.get("PLAN_NAME");
                        link.setPlanName(plan_name);
                        flag = true;
                        break;
                    }
                }
                if (!flag){
                    alert = "第"+i+"行数据产品ID不存在;";
                    log.info(alert);
                    alerts.append(alert);
                    continue;
                }
                link.setPlanId(planId);

                String description = this.getCellString(row.getCell(3));
                if (description.length()>150){
                    alert = "第"+i+"行数据说明字数超过150;";
                    log.info(alert);
                    alerts.append(alert);
                    continue;
                }
                link.setDescription(description);

                link.setStatus("1");

                date = new Date();
                date = new Date(date.getTime()+i);
                link.setId(sdf.format(date));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                long times = System.currentTimeMillis();
                String str = format.format(times);
                link.setCreateDate(str);
                linkList.add(link);

                sucNum ++;
            }
            log.info("将批量添加的素材数据量为:"+linkList.size()+"条");
            int num = linkDao.saveBatchLink(linkList);
            log.info("uploadLinkFile-batchSaveLinks num is :{}", num);
            map.put("sucNum", Integer.toString(sucNum));
            map.put("loseNum", Integer.toString(rowNum-sucNum));
            map.put("alerts",alerts.toString());
        }catch (Exception e){
            log.error("error:"+e);
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error("error:"+e);
            }
        }
        return map;
    }

    @Override
    public String getToShortUrlLinkId() {
        return linkDao.getToShortUrlLinkId();
    }

    /**
     * 获取Excel中的数值为字符串
     *
     * @param cell
     * @return
     */
    private String getCellString(Cell cell) {
        if (cell == null) return "";
        String cellSring = "";

        switch (cell.getCellType()) {
//            case HSSFCell.CELL_TYPE_STRING: // 字符串
            case STRING: // 字符串
                cellSring = cell.getStringCellValue();
                break;
//            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
            case _NONE:
                break;
            case NUMERIC: // 数字
                double cellDouble = cell.getNumericCellValue();
                if (this.isEqualDouble(Math.round(cellDouble), cellDouble)) {
                    cellSring = String.valueOf((long) cellDouble);
                } else {
                    cellSring = String.valueOf(cellDouble);
                }
                break;
//            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
            case BOOLEAN: // Boolean
                cellSring = String.valueOf(cell.getBooleanCellValue());
                break;
//            case HSSFCell.CELL_TYPE_FORMULA: // 公式
            case FORMULA: // 公式
                cellSring = String.valueOf(cell.getCellFormula());
                break;
//            case HSSFCell.CELL_TYPE_BLANK: // 空值
            case BLANK: // 空值
                cellSring = "";
                break;
//            case HSSFCell.CELL_TYPE_ERROR: // 故障
            case ERROR: // 故障
                cellSring = "";
                break;
            default:
                cellSring = "";
                break;
        }

        return cellSring;
    }

    /**
     * 判断浮点型是否相等
     * @param a
     * @param b
     * @return
     */
    private boolean isEqualDouble(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isInfinite(a) || Double.isInfinite(b)) {
            return false;
        }
        return (a - b) < 0.001d;
    }


    @Override
    public boolean deleteLink(McdDeleteLinkQuery req) throws Exception {

        //判断链接是否有活动正在使用中
        List<String> list = linkDao.queryCampStatusByLinkUrl(req.getLinkUrl());
         Link link = new Link();
        if (Objects.nonNull(list) && list.size() > 0){
            log.info("短链ID"+ req.getLinkId() +"有正在使用的活动,不能删除!");
            return false;
        }else {
            link.setId(req.getLinkId());
            link.setUrl(req.getLinkUrl());
            link.setStatus("2");//2为删除状态
            return linkDao.deleteLink(link);

        }


    }
}
