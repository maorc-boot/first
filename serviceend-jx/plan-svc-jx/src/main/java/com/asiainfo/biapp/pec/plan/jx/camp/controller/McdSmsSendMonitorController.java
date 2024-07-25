package com.asiainfo.biapp.pec.plan.jx.camp.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.WorkbookUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdEcExecLog;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.jx.camp.model.*;
import com.asiainfo.biapp.pec.plan.jx.camp.req.*;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdEcExecLogJxService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdSmsSendHistoryService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdSmsSendMonitorService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdUserRoleRelationService;
import com.asiainfo.biapp.pec.plan.service.IMcdCampsegService;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import com.asiainfo.biapp.pec.plan.vo.req.CampPauseQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/mcd/sms/send")
@Api(tags = "江西短信发送监控管理")
public class McdSmsSendMonitorController {

    @Resource
    private McdSmsSendHistoryService mcdSmsSendHistoryService;

    @Resource
    private McdSmsSendMonitorService mcdSmsSendMonitorService;

    @Autowired
    private IMcdCampsegService campsegService;
    @Resource
    private McdUserRoleRelationService mcdUserRoleRelationService;

    @Resource
    private McdEcExecLogJxService mcdEcExecLogJxService;

    @PostMapping("/querySmsSendMonitory")
    @ApiOperation("短信发送情况查询接口")
    public ActionResponse<IPage<McdSmsSendMonitorModel>> querySmsSendMonitory(@RequestBody McdSmsSendMonitorQuery req,HttpServletRequest request){

        //UserSimpleInfo user = UserUtil.getUser(request);

        if (StringUtils.isEmpty(req.getChannelId())){
            req.setChannelId("801");
        }
        LambdaQueryWrapper<McdSmsSendMonitorModel>  monitorWrapper = new LambdaQueryWrapper<>();
        monitorWrapper.eq(McdSmsSendMonitorModel::getChannelId,req.getChannelId());
                     // .eq(!"999".equals(user.getCityId()),McdSmsSendMonitorModel::getCityId,user.getCityId());

        Page<McdSmsSendMonitorModel> page = new Page<>();
        page.setCurrent(req.getPageNum());
        page.setSize(req.getPageSize());

        Page<McdSmsSendMonitorModel> monitorModelPage = mcdSmsSendMonitorService.page(page, monitorWrapper);
        return ActionResponse.getSuccessResp(monitorModelPage );

    }


    @PostMapping("/querySmsSendHistory")
    @ApiOperation("短信发送历史查询接口")
    public ActionResponse<IPage<McdSmsSendHistoryModel>> querySmsSendMonitory(@RequestBody McdSmsSendHistoryQuery req, HttpServletRequest request){

        if (StringUtils.isEmpty(req.getChannelId())){
            req.setChannelId("801");
        }
        UserSimpleInfo user = UserUtil.getUser(request);
        LambdaQueryWrapper<McdSmsSendHistoryModel>  promptWrapper = new LambdaQueryWrapper<>();
        promptWrapper.eq(StringUtils.isNotEmpty(req.getChannelId()),McdSmsSendHistoryModel::getChannelId,req.getChannelId())
                     .ge(StringUtils.isNotEmpty(req.getStartDate()),McdSmsSendHistoryModel::getStartDate,req.getStartDate())
                     .le(StringUtils.isNotEmpty(req.getEndDate()),McdSmsSendHistoryModel::getEndDate,req.getEndDate())
                     .eq(!"999".equals(user.getCityId()),McdSmsSendHistoryModel::getCityId,user.getCityId());

        Page<McdSmsSendHistoryModel> page = new Page<>();
        page.setCurrent(req.getPageNum());
        page.setSize(req.getPageSize());
        Page<McdSmsSendHistoryModel> historyModelPage = mcdSmsSendHistoryService.page(page, promptWrapper);

        return ActionResponse.getSuccessResp(historyModelPage);

    }


    @PostMapping("/smsSendTaskPause")
    @ApiOperation("短信发送(运行/暂停)操作控制")
    public ActionResponse smsSendTaskSuspend(@RequestBody McdSmsSendSuspendQuery req, HttpServletRequest request){

        if (StringUtils.isEmpty(req.getCampsegId())){
            return ActionResponse.getFaildResp("短信发送监控操作策略ID为空了!");
        }

        if (StringUtils.isEmpty(req.getCtrlStatus()+"")){
            return ActionResponse.getFaildResp("短信发送监控操作控制状态为空了!");
        }

        UserSimpleInfo user = UserUtil.getUser(request);

        LambdaQueryWrapper<McdUserRoleRelationModel> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(McdUserRoleRelationModel::getUserId,user.getUserId());
        final List<McdUserRoleRelationModel> relationModels = mcdUserRoleRelationService.list();

        boolean updateFlag = false;
        for (McdUserRoleRelationModel relationModel : relationModels) {
            if ("0,1".contains(relationModel.getRoleId())){
                //管理员可以修改活动
                updateFlag = true;
            }
        }

        if (!updateFlag){
            CampBaseInfoVO campsegBaseInfo = campsegService.getCampsegBaseInfo(req.getCampsegId());
            if (Objects.nonNull(campsegBaseInfo) && user.getUserId().equals(campsegBaseInfo.getCreateUserId())){
                //活动创建人可以修改活动
                updateFlag = true;
            }
        }

        if (!updateFlag){
            return ActionResponse.getFaildResp("无权限修改此活动!");
        }

        McdSmsSendMonitorModel sendMonitorModel = mcdSmsSendMonitorService.getById(req.getCampsegId());
        if (Objects.nonNull(sendMonitorModel)){
            if (sendMonitorModel.getCtrlStatus() == req.getCtrlStatus()){
                return ActionResponse.getFaildResp("重复操作!");
            }
        }
        if (req.getCtrlStatus() > 0){
            CampPauseQuery pauseQuery = new CampPauseQuery();
            pauseQuery.setPauseCommen("短信发送监控!");
            pauseQuery.setId(req.getCampsegId());
            //暂停
            campsegService.campPause(pauseQuery, UserUtil.getUser(request));
        }else {
            //重启
            campsegService.campRestart(req.getCampsegId());
        }

        LambdaUpdateWrapper<McdSmsSendMonitorModel>  monitorWrapper = new LambdaUpdateWrapper<>();
        monitorWrapper.set(McdSmsSendMonitorModel::getCtrlStatus,req.getCtrlStatus())
                .eq(McdSmsSendMonitorModel::getCampsegId,req.getCampsegId())
                .eq(McdSmsSendMonitorModel::getChannelId,req.getChannelId());

        return ActionResponse.getSuccessResp();

    }



    @PostMapping("/exportSmsSendHistory")
    @ApiOperation("导出短信发送历史数据")
    public void exportSmsSendHistory(@RequestBody McdSmsSendHistoryQuery req, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isEmpty(req.getChannelId())){
            req.setChannelId("801");
        }
        UserSimpleInfo user = UserUtil.getUser(request);
        LambdaQueryWrapper<McdSmsSendHistoryModel>  promptWrapper = new LambdaQueryWrapper<>();
        promptWrapper.eq(StringUtils.isNotEmpty(req.getChannelId()),McdSmsSendHistoryModel::getChannelId,req.getChannelId())
                .ge(StringUtils.isNotEmpty(req.getStartDate()),McdSmsSendHistoryModel::getStartDate,req.getStartDate())
                .le(StringUtils.isNotEmpty(req.getEndDate()),McdSmsSendHistoryModel::getEndDate,req.getEndDate())
                .eq(!"999".equals(user.getCityId()),McdSmsSendHistoryModel::getCityId,user.getCityId());

        List<McdSmsSendHistoryModel> historyModelList = mcdSmsSendHistoryService.list( promptWrapper);

        exportSmsSendHistoryData(historyModelList,response);
    }


    private void exportSmsSendHistoryData(List<McdSmsSendHistoryModel> historyModelList, HttpServletResponse response) {

        String[] excelHeader = {"策略编号及名称", "执行周期", "地市", "活动内容", "创建人","客群数量","发送总量"};
        String fileName = "历史短信发送数据导出列表.xlsx";
        final Workbook book = WorkbookUtil.createBook(false);
        Sheet sheet = book.createSheet("Sheet");
        Row row = sheet.createRow(Constant.SpecialNumber.ZERO_NUMBER);
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
            int maxWith = 255 * 256;
            int newWidth = sheet.getColumnWidth(i) * 17 / 10;
            if (newWidth < maxWith) {
                sheet.setColumnWidth(i, newWidth);
            }else {
                sheet.setColumnWidth(i, maxWith / 2);
            }
        }
        if (!CollectionUtils.isEmpty(historyModelList)) {
            for (int i = 0; i < historyModelList.size(); i++) {
                final McdSmsSendHistoryModel campManage = historyModelList.get(i);
                row = sheet.createRow(i + 1);
                row.createCell(Constant.SpecialNumber.ZERO_NUMBER).setCellValue(campManage.getCampsegId() + "," + campManage.getCampsegName());
                row.createCell(Constant.SpecialNumber.ONE_NUMBER).setCellValue(campManage.getStartDate() + "--" + campManage.getEndDate());
                row.createCell(Constant.SpecialNumber.TWO_NUMBER).setCellValue(campManage.getCityId());
                row.createCell(Constant.SpecialNumber.THREE_NUMBER).setCellValue(campManage.getExecContent());
                row.createCell(Constant.SpecialNumber.FOUR_NUMBER).setCellValue(campManage.getCreateUserId());
                row.createCell(Constant.SpecialNumber.FIVE_NUMBER).setCellValue(campManage.getCustListCount());
                row.createCell(Constant.SpecialNumber.SIX_NUMBER).setCellValue(campManage.getSendNum());
            }
        }
        try {
            fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            OutputStream ouputStream = response.getOutputStream();
            book.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            log.error("历史短信发送数据导出失败", e);
        }

    }

    /**
     * 短信发送监控数据定时任务
     */
    @PostMapping("/smsSendMonitorTask")
    public void smsSendMonitorTask(){

        log.info("短信发送监控数据定时任务smsSendMonitorTask开始!");
        final List<Map<String, Object>> sendBasicInfoList = mcdSmsSendMonitorService.querySmsSendBasicInfo();

        //总量
        int total = 0;
        //发送量请求
        int  sendNum = 0;
        //免打扰过滤请求
        int botherNum = 0;
        //频次过滤请求
        int fqcNum = 0;

        McdSmsSendInfoQuery req = new McdSmsSendInfoQuery();
        for (Map<String, Object> map : sendBasicInfoList) {

            String campsegId = map.get("CAMPSEG_ID").toString();
            req.setCampsegId(campsegId);
            req.setChannelId(map.get("CHANNEL_ID").toString());
            LambdaQueryWrapper<McdEcExecLog>  ecExecLogWrapper =new LambdaQueryWrapper<>();
            ecExecLogWrapper.eq(McdEcExecLog::getCampsegPid,campsegId)
                            .in(McdEcExecLog::getStage,0,1,2)
                            .orderByDesc(McdEcExecLog::getCreateTime);
            List<McdEcExecLog> mcdEcExecLogList = mcdEcExecLogJxService.list(ecExecLogWrapper);
            for (McdEcExecLog mcdEcExecLog : mcdEcExecLogList) {//取第一条数据
                sendNum = mcdEcExecLog.getSendNum();
                botherNum = mcdEcExecLog.getBaFilter();
                fqcNum = mcdEcExecLog.getFqcFilter();
                total = mcdEcExecLog.getTotal();
                break;
            }

            McdSmsSendMonitorModel sendMonitorModel = mcdSmsSendMonitorService.getById(campsegId);

            if (Objects.isNull(sendMonitorModel)){
                sendMonitorModel = new McdSmsSendMonitorModel();
                sendMonitorModel.setCampsegId(campsegId);
                sendMonitorModel.setCampsegRootId(map.get("CAMPSEG_ROOT_ID").toString());
                sendMonitorModel.setCampsegName(map.get("CAMPSEG_NAME").toString());
                sendMonitorModel.setCreateUserId(map.get("CREATE_USER_ID").toString());
                sendMonitorModel.setUserName(map.get("USER_NAME").toString());
                sendMonitorModel.setStartDate(map.get("START_DATE").toString());
                sendMonitorModel.setEndDate(map.get("END_DATE").toString());
                sendMonitorModel.setExecContent(map.get("EXEC_CONTENT")== null?"":map.get("EXEC_CONTENT").toString());
                sendMonitorModel.setTaskId(map.get("TASK_ID").toString());
                sendMonitorModel.setCityId(map.get("CITY_ID").toString());
                sendMonitorModel.setCityName(map.get("CITY_NAME").toString());
                sendMonitorModel.setChannelId(map.get("CHANNEL_ID").toString());
                sendMonitorModel.setCustListCount(Integer.parseInt(map.get("CUST_LIST_COUNT").toString()));
            }

            sendMonitorModel.setCustListCount(total > 0 ? total: Integer.parseInt(map.get("CUST_LIST_COUNT").toString()));
            sendMonitorModel.setSendNum(sendNum);
            sendMonitorModel.setContactNum(botherNum+fqcNum);
            sendMonitorModel.setExecStatus(Integer.parseInt(map.get("EXEC_STATUS").toString()));
            sendMonitorModel.setCtrlStatus(Integer.parseInt(map.get("CTRL_STATUS").toString()));

            mcdSmsSendMonitorService.saveOrUpdate(sendMonitorModel);

        }
        log.info("短信发送监控数据定时任务smsSendMonitorTask完成!");

    }


    /**
     *
     * 短信发送历史数据定时任务
     */
    @PostMapping("/smsSendHistoryTask")
    public void smsSendHistoryTask(){

        log.info("短信发送历史数据定时任务smsSendHistoryTask开始!");
        String endDate = getYesterday("yyyy-MM-dd");
        final List<Map<String, Object>> sendHistoryBasicInfo = mcdSmsSendMonitorService.querySmsSendHistoryBasicInfo(endDate);

        //总量
        int total = 0;
        //发送量请求
        int  sendNum = 0;

        McdSmsSendInfoQuery req = new McdSmsSendInfoQuery();
        for (Map<String, Object> map : sendHistoryBasicInfo) {

            String campsegId = map.get("CAMPSEG_ID").toString();
            req.setCampsegId(campsegId);
            req.setChannelId(map.get("CHANNEL_ID").toString());
            LambdaQueryWrapper<McdEcExecLog>  ecExecLogWrapper =new LambdaQueryWrapper<>();
            ecExecLogWrapper.eq(McdEcExecLog::getCampsegPid,campsegId)
                    .in(McdEcExecLog::getStage,3)
                    .orderByDesc(McdEcExecLog::getCreateTime);
            List<McdEcExecLog> mcdEcExecLogList = mcdEcExecLogJxService.list(ecExecLogWrapper);
            for (McdEcExecLog mcdEcExecLog : mcdEcExecLogList) {//取第一条数据
                sendNum = mcdEcExecLog.getSendNum();
                total = mcdEcExecLog.getTotal();
                break;
            }


            McdSmsSendHistoryModel sendHistoryModel = mcdSmsSendHistoryService.getById(campsegId);

            if (Objects.isNull(sendHistoryModel)){
                sendHistoryModel = new McdSmsSendHistoryModel();
                sendHistoryModel.setCampsegId(campsegId);
                sendHistoryModel.setCampsegRootId(map.get("CAMPSEG_ROOT_ID").toString());
                sendHistoryModel.setCampsegName(map.get("CAMPSEG_NAME").toString());
                sendHistoryModel.setCreateUserId(map.get("CREATE_USER_ID").toString());
                sendHistoryModel.setUserName(map.get("USER_NAME").toString());
                sendHistoryModel.setStartDate(map.get("START_DATE").toString());
                sendHistoryModel.setEndDate(map.get("END_DATE").toString());
                sendHistoryModel.setExecContent(map.get("EXEC_CONTENT")== null?"":map.get("EXEC_CONTENT").toString());
                sendHistoryModel.setTaskId(map.get("TASK_ID").toString());
                sendHistoryModel.setCityId(map.get("CITY_ID").toString());
                sendHistoryModel.setCityName(map.get("CITY_NAME").toString());
                sendHistoryModel.setChannelId(map.get("CHANNEL_ID").toString());
                sendHistoryModel.setCustListCount(Integer.parseInt(map.get("CUST_LIST_COUNT").toString()));
            }

            sendHistoryModel.setCustListCount(total > 0 ? total: Integer.parseInt(map.get("CUST_LIST_COUNT").toString()));
            sendHistoryModel.setSendNum(sendNum);


            mcdSmsSendHistoryService.saveOrUpdate(sendHistoryModel);

        }
        log.info("短信发送历史数据定时任务smsSendHistoryTask完成!");

    }


    private String getYesterday(String dateStr){
        //获取昨天日期
        DateTime dateTime = cn.hutool.core.date.DateUtil.offsetDay(new Date(), -1);
        String dataDate  = DateUtil.format(dateTime, dateStr);

        return dataDate;
    }

}
