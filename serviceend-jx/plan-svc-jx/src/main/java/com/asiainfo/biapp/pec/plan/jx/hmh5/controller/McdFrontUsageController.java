
package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontUsageService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageDetailInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageOppoHandleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageSummaryInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@Api(tags = "客户通-客户通使用情况")
@RequestMapping("/action/jx/onlinelChannel")
@DataSource("khtmanageusedb")
public class McdFrontUsageController   {

    @Autowired
    private McdFrontUsageService mcdFrontUsageService;


    @PostMapping("/queryCityUsageList")
    @ApiOperation("客户通地市使用情况查询")
    public ActionResponse<IPage<McdFrontUsageInfo>> queryCityUsageList(@RequestBody McdFrontUsageCityQuery req){
        if (StringUtils.isEmpty(req.getStartTime()) ){
            req.setStartTime(getYesterday("yyyy-MM-dd"));
        }
        if (StringUtils.isEmpty(req.getEndTime())){
            req.setEndTime(getYesterday("yyyy-MM-dd"));
        }
        return ActionResponse.getSuccessResp(mcdFrontUsageService.queryCityUsageInfo(req));
    }

    @PostMapping("/queryCountyUsageList")
    @ApiOperation("客户通区县使用情况查询")
    public ActionResponse<IPage<McdFrontUsageInfo>> queryCountyUsageList(@RequestBody McdFrontUsageCountyQuery req){
        if (StringUtils.isEmpty(req.getStartTime()) ){
            req.setStartTime(getYesterday("yyyy-MM-dd"));
        }
        if (StringUtils.isEmpty(req.getEndTime())){
            req.setEndTime(getYesterday("yyyy-MM-dd"));
        }
        return ActionResponse.getSuccessResp(mcdFrontUsageService.queryCountyUsageInfo(req));
    }

    @PostMapping("/queryGridUsageList")
    @ApiOperation("客户通网格使用情况查询")
    public ActionResponse<IPage<McdFrontUsageInfo>> queryGridUsageList(@RequestBody McdFrontUsageGridQuery req){
        if (StringUtils.isEmpty(req.getStartTime()) ){
            req.setStartTime(getYesterday("yyyy-MM-dd"));
        }
        if (StringUtils.isEmpty(req.getEndTime())){
            req.setEndTime(getYesterday("yyyy-MM-dd"));
        }
        return ActionResponse.getSuccessResp(mcdFrontUsageService.queryGridUsageInfo(req));
    }

    @PostMapping("/queryChannelUsageList")
    @ApiOperation("客户通渠道(厅店)使用情况查询")
    public ActionResponse<IPage<McdFrontUsageInfo>> queryChannelUsageList(@RequestBody McdFrontUsageChannelQuery req){
        if (StringUtils.isEmpty(req.getStartTime()) ){
            req.setStartTime(getYesterday("yyyy-MM-dd"));
        }
        if (StringUtils.isEmpty(req.getEndTime())){
            req.setEndTime(getYesterday("yyyy-MM-dd"));
        }
        return ActionResponse.getSuccessResp(mcdFrontUsageService.queryChannelUsageInfo(req));
    }

    @PostMapping("/queryCampsegHandleSummary")
    @ApiOperation("客户通使用情况明细数据")
    public ActionResponse<IPage<McdFrontUsageSummaryInfo>> queryCampsegHandleSummary(@RequestBody McdFrontUsageQuery req){
        if (StringUtils.isEmpty(req.getDataDate()) ){
            req.setDataDate(getYesterday("yyyy-MM-dd"));
        }

        return ActionResponse.getSuccessResp(mcdFrontUsageService.queryCampsegHandleSummary(req));
    }


    @PostMapping("/queryCampsegHandleDetail")
    @ApiOperation("客户通活动处理情况")
    public ActionResponse<IPage<McdFrontUsageDetailInfo>> queryCampsegHandleDetail(@RequestBody McdFrontUsageQuery req){
        if (StringUtils.isEmpty(req.getDataDate()) ){
            req.setDataDate(getYesterday("yyyy-MM-dd"));
        }

        return ActionResponse.getSuccessResp(mcdFrontUsageService.queryCampsegHandleDetail(req));
    }


    @PostMapping("/queryOppoCampCityHandle")
    @ApiOperation("客户通抢单执行情况")
    public ActionResponse<IPage<McdFrontUsageOppoHandleInfo>> queryOppoCampCityHandle(@RequestBody McdFrontUsageQuery req){
        if (StringUtils.isEmpty(req.getDataDate()) ){
            req.setDataDate(getYesterday("yyyy-MM-dd"));
        }
        return ActionResponse.getSuccessResp(mcdFrontUsageService.queryOppoCampCityHandle(req));
    }



    @PostMapping("/exportCityUsageList")
    @ApiOperation("客户通地市使用情况导出")
    public void exportCityUsageList(@RequestBody McdFrontUsageCityQuery req,HttpServletResponse response) throws Exception{
        if (StringUtils.isEmpty(req.getStartTime()) ){
            req.setStartTime(getYesterday("yyyy-MM-dd"));
        }
        if (StringUtils.isEmpty(req.getEndTime())){
            req.setEndTime(getYesterday("yyyy-MM-dd"));
        }

        req.setCurrentPage(1);
        req.setPageSize(9999999);
        IPage<McdFrontUsageInfo> frontUsageCityInfo = mcdFrontUsageService.queryCityUsageInfo(req);
        String[] excelHeader = {"地市","地市编码","组织数","开通外呼数","开通外呼占比","组织登录数","组织参与率","工号登录数","使用总次数","人均使用次数","营销任务总量","当月销任务处理量","累计营销任务处理量","有效活动业务办理量","当月业务办理量","累计业务办理量","业务转化率","外呼量","外呼成功量","关怀短信发送量"};
       String fileName = "hmh5Usage_city_"+req.getStartTime().replaceAll("-","")+"_"+req.getEndTime().replaceAll("-","")+".xls";
        List<Object> rs = new ArrayList<>();

        List<McdFrontUsageInfo> list = frontUsageCityInfo.getRecords();
        for (McdFrontUsageInfo usageInfo : list) {
            Object[] o = new Object[21];


            o[0] = usageInfo.getLevelName();
            o[1] = usageInfo.getLevelId();
            o[2] = usageInfo.getOrgNum();
            o[3] = usageInfo.getCanCallNum();
            o[4] = usageInfo.getCanCallRate();
            o[5] = usageInfo.getOrgLoginNum();
            o[6] = usageInfo.getOrgJoinRate();
            o[7] = usageInfo.getStaffLoginMCount() ;
            o[8] = usageInfo.getStaffLoginMNum();
            o[9] = usageInfo.getStaffLoginAvg();
            o[10] = usageInfo.getTaskAll();
            o[11] = usageInfo.getTaskMonthDeal();
            o[12] = usageInfo.getTaskTotalDeal();
            o[13] = usageInfo.getEfCampNumM();
            o[14] = usageInfo.getBprSuccM();
            o[15] = usageInfo.getBprSuccT();
            o[16] = usageInfo.getOrgJoinRate();
            o[17] = usageInfo.getCallNumM();
            o[18] = usageInfo.getCallSuccNum() ;
            o[19] = usageInfo.getSmsSendNum()  ;

            rs.add(o);

        }

        exportExcel(fileName, excelHeader, rs, response);

    }

    @PostMapping("/exportCountyUsageList")
    @ApiOperation("客户通区县使用情况导出")
    public void exportCountyUsageList(@RequestBody McdFrontUsageCountyQuery req,HttpServletResponse response) throws Exception{
        if (StringUtils.isEmpty(req.getStartTime()) ){
            req.setStartTime(getYesterday("yyyy-MM-dd"));
        }
        if (StringUtils.isEmpty(req.getEndTime())){
            req.setEndTime(getYesterday("yyyy-MM-dd"));
        }
        req.setCurrentPage(1);
        req.setPageSize(9999999);

        IPage<McdFrontUsageInfo> frontUsageInfo = mcdFrontUsageService.queryCountyUsageInfo(req);
        String[] excelHeader = {"区县","区县编码","组织数","开通外呼数","开通外呼占比","组织登录数","组织参与率","工号登录数","使用总次数","人均使用次数","营销任务总量","当月销任务处理量","累计营销任务处理量","有效活动业务办理量","当月业务办理量","累计业务办理量","业务转化率","外呼量","外呼成功量","关怀短信发送量"};
        String fileName = "hmh5Usage_county_"+req.getStartTime().replaceAll("-","")+"_"+req.getEndTime().replaceAll("-","")+".xls";
        List<Object> rs = new ArrayList<>();

        List<McdFrontUsageInfo> list = frontUsageInfo.getRecords();
        for (McdFrontUsageInfo usageInfo : list) {
            Object[] o = new Object[20];

            o[0] = usageInfo.getLevelName();
            o[1] = usageInfo.getLevelId();
            o[2] = usageInfo.getOrgNum();
            o[3] = usageInfo.getCanCallNum();
            o[4] = usageInfo.getCanCallRate();
            o[5] = usageInfo.getOrgLoginNum();
            o[6] = usageInfo.getOrgJoinRate();
            o[7] = usageInfo.getStaffLoginMCount() ;
            o[8] = usageInfo.getStaffLoginMNum();
            o[9] = usageInfo.getStaffLoginAvg();
            o[10] = usageInfo.getTaskAll();
            o[11] = usageInfo.getTaskMonthDeal();
            o[12] = usageInfo.getTaskTotalDeal();
            o[13] = usageInfo.getEfCampNumM();
            o[14] = usageInfo.getBprSuccM();
            o[15] = usageInfo.getBprSuccT();
            o[16] = usageInfo.getOrgJoinRate();
            o[17] = usageInfo.getCallNumM();
            o[18] = usageInfo.getCallSuccNum() ;
            o[19] = usageInfo.getSmsSendNum()  ;

            rs.add(o);

        }

        exportExcel(fileName, excelHeader, rs, response);
    }

    @PostMapping("/exportGridUsageList")
    @ApiOperation("客户通网格使用情况导出")
    public void exportGridUsageList(@RequestBody McdFrontUsageGridQuery req,HttpServletResponse response) throws Exception{
        if (StringUtils.isEmpty(req.getStartTime()) ){
            req.setStartTime(getYesterday("yyyy-MM-dd"));
        }
        if (StringUtils.isEmpty(req.getEndTime())){
            req.setEndTime(getYesterday("yyyy-MM-dd"));
        }
        req.setCurrentPage(1);
        req.setPageSize(9999999);

        IPage<McdFrontUsageInfo> frontUsageInfo = mcdFrontUsageService.queryGridUsageInfo(req);
        String[] excelHeader = {"网格","网格编码","组织数","开通外呼数","开通外呼占比","组织登录数","组织参与率","工号登录数","使用总次数","人均使用次数","营销任务总量","当月销任务处理量","累计营销任务处理量","有效活动业务办理量","当月业务办理量","累计业务办理量","业务转化率","外呼量","外呼成功量","关怀短信发送量"};
        String fileName = "hmh5Usage_grid_"+req.getStartTime().replaceAll("-","")+"_"+req.getEndTime().replaceAll("-","")+".xls";
        List<Object> rs = new ArrayList<>();

        List<McdFrontUsageInfo> list = frontUsageInfo.getRecords();
        for (McdFrontUsageInfo usageInfo : list) {
            Object[] o = new Object[20];

            o[0] = usageInfo.getLevelName();
            o[1] = usageInfo.getLevelId();
            o[2] = usageInfo.getOrgNum();
            o[3] = usageInfo.getCanCallNum();
            o[4] = usageInfo.getCanCallRate();
            o[5] = usageInfo.getOrgLoginNum();
            o[6] = usageInfo.getOrgJoinRate();
            o[7] = usageInfo.getStaffLoginMCount() ;
            o[8] = usageInfo.getStaffLoginMNum();
            o[9] = usageInfo.getStaffLoginAvg();
            o[10] = usageInfo.getTaskAll();
            o[11] = usageInfo.getTaskMonthDeal();
            o[12] = usageInfo.getTaskTotalDeal();
            o[13] = usageInfo.getEfCampNumM();
            o[14] = usageInfo.getBprSuccM();
            o[15] = usageInfo.getBprSuccT();
            o[16] = usageInfo.getOrgJoinRate();
            o[17] = usageInfo.getCallNumM();
            o[18] = usageInfo.getCallSuccNum() ;
            o[19] = usageInfo.getSmsSendNum()  ;

            rs.add(o);

        }

        exportExcel(fileName, excelHeader, rs, response);
    }

    @PostMapping("/exportChannelUsageList")
    @ApiOperation("客户通渠道(厅店)使用情况导出")
    public void exportChannelUsageList(@RequestBody McdFrontUsageChannelQuery req,HttpServletResponse response) throws Exception{
        if (StringUtils.isEmpty(req.getStartTime()) ){
            req.setStartTime(getYesterday("yyyy-MM-dd"));
        }
        if (StringUtils.isEmpty(req.getEndTime())){
            req.setEndTime(getYesterday("yyyy-MM-dd"));
        }
        req.setCurrentPage(1);
        req.setPageSize(9999999);
        IPage<McdFrontUsageInfo> frontUsageInfo = mcdFrontUsageService.queryChannelUsageInfo(req);
        String[] excelHeader = {"渠道","渠道编码","组织数","开通外呼数","开通外呼占比","组织登录数","组织参与率","工号登录数","使用总次数","人均使用次数","营销任务总量","当月销任务处理量","累计营销任务处理量","有效活动业务办理量","当月业务办理量","累计业务办理量","业务转化率","外呼量","外呼成功量","关怀短信发送量"};
        String fileName = "hmh5Usage_channel_"+req.getStartTime().replaceAll("-","")+"_"+req.getEndTime().replaceAll("-","")+".xls";
        List<Object> rs = new ArrayList<>();

        List<McdFrontUsageInfo> list = frontUsageInfo.getRecords();
        for (McdFrontUsageInfo usageInfo : list) {
            Object[] o = new Object[20];

            o[0] = usageInfo.getLevelName();
            o[1] = usageInfo.getLevelId();
            o[2] = usageInfo.getOrgNum();
            o[3] = usageInfo.getCanCallNum();
            o[4] = usageInfo.getCanCallRate();
            o[5] = usageInfo.getOrgLoginNum();
            o[6] = usageInfo.getOrgJoinRate();
            o[7] = usageInfo.getStaffLoginMCount() ;
            o[8] = usageInfo.getStaffLoginMNum();
            o[9] = usageInfo.getStaffLoginAvg();
            o[10] = usageInfo.getTaskAll();
            o[11] = usageInfo.getTaskMonthDeal();
            o[12] = usageInfo.getTaskTotalDeal();
            o[13] = usageInfo.getEfCampNumM();
            o[14] = usageInfo.getBprSuccM();
            o[15] = usageInfo.getBprSuccT();
            o[16] = usageInfo.getOrgJoinRate();
            o[17] = usageInfo.getCallNumM();
            o[18] = usageInfo.getCallSuccNum() ;
            o[19] = usageInfo.getSmsSendNum()  ;

            rs.add(o);

        }

        exportExcel(fileName, excelHeader, rs, response);

    }

    @PostMapping("/exportCampsegHandleSummary")
    @ApiOperation("客户通使用情况明细数据导出")
    public void exportCampsegHandleSummary(@RequestBody McdFrontUsageQuery req,HttpServletResponse response) throws Exception{
        if (StringUtils.isEmpty(req.getDataDate()) ){
            req.setDataDate(getYesterday("yyyy-MM-dd"));
        }
        req.setCurrentPage(1);
        req.setPageSize(9999999);
        IPage<McdFrontUsageSummaryInfo> summaryInfoIPage = mcdFrontUsageService.queryCampsegHandleSummary(req);
        String fileName = "hmh5HandleSummary_" + req.getDataDate().replaceAll("-","")+".xls";
        List<Object> rs = new ArrayList<>();
        List<McdFrontUsageSummaryInfo> list = summaryInfoIPage.getRecords();
        for (McdFrontUsageSummaryInfo usageSummaryInfo : list) {
            Object[] o = new Object[23];

            o[0] =usageSummaryInfo.getDataDate();
            o[1] = usageSummaryInfo.getCityName() ;
            o[2] = usageSummaryInfo.getCityId() ;
            o[3] =usageSummaryInfo.getCountyName();
            o[4] = usageSummaryInfo.getCountyId() ;
            o[5] = usageSummaryInfo.getChannelId() ;
            o[6] = usageSummaryInfo.getChannelName();
            o[7] = usageSummaryInfo.getStaffId();
            o[8] = usageSummaryInfo.getStaffName();
            o[9] = usageSummaryInfo.getIsSelfManager() ;
            o[10] = usageSummaryInfo.getCallFlag() ;
            o[11] = usageSummaryInfo.getLoginCount()  ;
            o[12] = usageSummaryInfo.getLoginCount() ;
            o[13] = usageSummaryInfo.getLoginCountM();
            o[14] =usageSummaryInfo.getLoginCountM();
            o[15] = usageSummaryInfo.getStaffTask() ;
            o[16] = usageSummaryInfo.getTaskMonthDeal();
            o[17] = usageSummaryInfo.getTaskTotalDeal() ;
            o[18] = usageSummaryInfo.getEfCampNumM() ;
            o[19] = usageSummaryInfo.getBprSuccM() ;
            o[20] = usageSummaryInfo.getCallNumM() ;
            o[21] = usageSummaryInfo.getCallSuccM() ;
            o[22] = usageSummaryInfo.getSmsMonthM() ;

            rs.add(o);

        }
        String[] excelHeader = {"日期","地市","地市编码","区县名称","区县编号","渠道编号","归属营业厅","工号","工号姓名","看护类型","是否开通外呼功能","当日是否登录","登录次数","当月是否登录","当月登录次数","营销任务总量","当月营销任务处理量","累计营销任务处理量","当月有效活动营销任务办理量","当月业务办理量","外呼量","外呼成功量","关怀短信发送量"};
        exportExcel(fileName, excelHeader, rs, response);
    }


    @PostMapping("/exportCampsegHandleDetail")
    @ApiOperation("客户通活动处理情况导出")
    public void exportCampsegHandleDetail(@RequestBody McdFrontUsageQuery req,HttpServletResponse response) throws Exception{
        if (StringUtils.isEmpty(req.getDataDate()) ){
            req.setDataDate(getYesterday("yyyy-MM-dd"));
        }
        req.setCurrentPage(1);
        req.setPageSize(9999999);
        final IPage<McdFrontUsageDetailInfo> usageDetailInfoIPage = mcdFrontUsageService.queryCampsegHandleDetail(req);
        String fileName = "hmh5HandleDetail_" +req.getDataDate().replaceAll("-","")+".xls";
        List<Object> rs = new ArrayList<>();
        List<McdFrontUsageDetailInfo> list = usageDetailInfoIPage.getRecords();
        for (McdFrontUsageDetailInfo usageDetailInfo : list) {
            Object[] o = new Object[17];

            o[0] = usageDetailInfo.getDataDate();
            o[1] = usageDetailInfo.getCampsegName();
            o[2] = usageDetailInfo.getStartDate();
            o[3] = usageDetailInfo.getEndDate();
            o[4] =usageDetailInfo.getCampsegId();
            o[5] = usageDetailInfo.getCreateUserName();
            o[6] = usageDetailInfo.getCampsegDesc();
            o[7] = usageDetailInfo.getCampCityName();
            o[8] = usageDetailInfo.getDeptName();
            o[9] = usageDetailInfo.getCityName();
            o[10] = usageDetailInfo.getTaskCount();
            o[11] = usageDetailInfo.getCampCallNum() ;
            o[12] = usageDetailInfo.getCampDealNum() ;
            o[13] = usageDetailInfo.getCampCallSucc() ;
            o[14] = usageDetailInfo.getCampCallTotal() ;
            o[15] = usageDetailInfo.getCampDealTotal() ;
            o[16] = usageDetailInfo.getCampCallSuccTotal() ;

            rs.add(o);

        }
        String[] excelHeader = {"日期","活动名称","开始日期","结束日期","活动编码","创建人","活动描述","活动创建地市","活动创建部门","处理地市","营销任务下发量","当月营销任务外呼量","当月营销任务处理量","外呼成功量","累计营销任务外呼量","累计营销任务处理量","累计外呼成功量"};
        exportExcel(fileName, excelHeader, rs, response);
    }


    @PostMapping("/exportOppoCampCityHandle")
    @ApiOperation("客户通抢单执行情况导出")
    public void exportOppoCampCityHandle(@RequestBody McdFrontUsageQuery req,HttpServletResponse response) throws Exception{
        if (StringUtils.isEmpty(req.getDataDate()) ){
            req.setDataDate(getYesterday("yyyy-MM-dd"));
        }

        req.setCurrentPage(1);
        req.setPageSize(9999999);
        IPage<McdFrontUsageOppoHandleInfo> oppoHandleInfoIPage = mcdFrontUsageService.queryOppoCampCityHandle(req);

        String fileName = "hmh5OppoCityHandleDetail_" +req.getDataDate().replaceAll("-","")+".xls";
        List<Object> rs = new ArrayList<>();
        List<McdFrontUsageOppoHandleInfo> list = oppoHandleInfoIPage.getRecords();
        for (McdFrontUsageOppoHandleInfo oppoHandleInfo : list) {
            Object[] o = new Object[14];

            o[0] = oppoHandleInfo.getDataDate();
            o[1] = oppoHandleInfo.getCampsegName();
            o[2] = oppoHandleInfo.getCampsegId();
            o[3] = oppoHandleInfo.getCampCityName();
            o[4] = oppoHandleInfo.getCampCityId();
            o[5] = oppoHandleInfo.getCityName();
            o[6] = oppoHandleInfo.getCityId();
            o[7] = oppoHandleInfo.getTaskCount();
            o[8] = oppoHandleInfo.getCampCallM() ;
            o[9] = oppoHandleInfo.getCampDealM() ;
            o[10] = oppoHandleInfo.getCampHandleM() ;
            o[11] = oppoHandleInfo.getCampCallT() ;
            o[12] = oppoHandleInfo.getCampDealT() ;
            o[13] = oppoHandleInfo.getCampHandleT() ;

            rs.add(o);

        }
        String[] excelHeader = {"日期","活动名称","活动编码","活动创建地市","活动创建地市编码","处理地市","处理地市编码","营销任务下发量","当月-抢单处理外呼量","当月-抢单任务处理量","当月-抢单任务成功量","累计-抢单处理外呼量","累计-抢单任务处理量","累计-抢单任务成功量"};
        exportExcel(fileName, excelHeader, rs, response);

    }

    //数据导出
    public void exportExcel(String fileName, String[] excelHeader, List<Object> list, HttpServletResponse response) throws Exception {
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

        if(list != null && list.size() > 0){
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                Object[] obj = (Object[])list.get(i);
                for(int j=0;j<obj.length;j++){
                    row.createCell(j).setCellValue(obj[j]==null?"":obj[j].toString());
                }
            }
        }
        fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename="+fileName);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }




    private String getYesterday(String dateStr){
        //获取昨天日期
        DateTime dateTime = DateUtil.offsetDay(new Date(), -1);
        String dataDate  = DateUtil.format(dateTime, dateStr);

        return dataDate;
    }
}



