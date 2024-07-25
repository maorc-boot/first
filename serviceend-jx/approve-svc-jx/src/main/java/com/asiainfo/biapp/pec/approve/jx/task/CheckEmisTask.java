package com.asiainfo.biapp.pec.approve.jx.task;


import com.asiainfo.biapp.pec.approve.dao.CmpApproveProcessRecordDao;
import com.asiainfo.biapp.pec.approve.jx.dao.*;
import com.asiainfo.biapp.pec.approve.jx.model.*;
import com.asiainfo.biapp.pec.approve.jx.utils.EmisUtils;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessInstance;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.asiainfo.biapp.pec.approve.model.User;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveProcessInstanceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
//@EnableScheduling
@Configuration
@PropertySource("classpath:/spy.properties")
public class CheckEmisTask {


    @Resource
    private McdCampDefDao mcdCampDefDao;

    @Resource
    private McdEmisTaskDao mcdEmisTaskDao;
    @Resource
    private McdEmisReadTaskMapper mcdEmisReadTaskMapper;
    @Resource
    private McdCampWarnEmisTaskMapper mcdCampWarnEmisTaskMapper;

    @Resource
    private SysUserDao sysUserDao;

    @Resource(name = "emisUtil")
    private EmisUtils emis;
    @Resource
    private ICmpApproveProcessInstanceService instanceService;
    @Resource
    private CmpApproveProcessRecordDao cmpApproveProcessRecordDao;

    public void prcoss() {

        log.info("............................CheckEmisTask启动......................");
        try {
            this.checkEmisTask();
        } catch (Exception e) {
            log.error("EMIS审批稽核调度异常！！！", e);
        }
    }

    public void checkEmisTask() {
        // 将延期导致活动结束的待办变更
        mcdEmisTaskDao.updateMcdEmisTask();

        //获取IOP待办数据审批待办
        LambdaQueryWrapper<McdEmisTask> emisWrapper = new LambdaQueryWrapper<>();
        emisWrapper.eq(McdEmisTask::getResult, 2);
        List<McdEmisTask> emisApproveTasks = mcdEmisTaskDao.selectList(emisWrapper);

        List<McdEmisTask> emisRemoveTask = new ArrayList<>();

        for (McdEmisTask mcdEmisTask : emisApproveTasks) {
            LambdaQueryWrapper<CmpApproveProcessRecord> recordWrapper = new LambdaQueryWrapper<>();
            recordWrapper.eq(CmpApproveProcessRecord::getDealStatus,0)
                    .eq(CmpApproveProcessRecord::getBusinessId,mcdEmisTask.getCampsegId())
                    .eq(CmpApproveProcessRecord::getInstanceId,mcdEmisTask.getInstanceId())
                    .eq(CmpApproveProcessRecord::getNodeId,mcdEmisTask.getNodeId());
            List<CmpApproveProcessRecord> cmpApproveProcessRecords = cmpApproveProcessRecordDao.selectList(recordWrapper);

            if (CollectionUtils.isEmpty(cmpApproveProcessRecords)){
                mcdEmisTask.setResult(1);
                mcdEmisTaskDao.updateById(mcdEmisTask);
                emisRemoveTask.add(mcdEmisTask);
            }
        }

         emisApproveTasks.removeAll(emisRemoveTask);

        //获取策略预警信息待办数据
        LambdaQueryWrapper<McdCampWarnEmisTask> warnEmisTaskWrapper = new LambdaQueryWrapper<>();
        warnEmisTaskWrapper.ne(McdCampWarnEmisTask::getStatus,1)
                           .orderByDesc(McdCampWarnEmisTask::getCreateTime);
        List<McdCampWarnEmisTask> campWarnInfoToEmisList = mcdCampWarnEmisTaskMapper.selectList(warnEmisTaskWrapper);

        //获取IOP待办数据（阅知待办）
        LambdaQueryWrapper<McdEmisReadTask> readTaskWrapper = new LambdaQueryWrapper<>();
        readTaskWrapper.eq(McdEmisReadTask::getStatus, 0);
        List<McdEmisReadTask> emisReadTasks = mcdEmisReadTaskMapper.selectList(readTaskWrapper);

        //emis待办查询（阅知待办+审批待办），因为emis接口返回的是系统级的待办，不是根据流程对应返回的
        String emisReadList = EmisUtils.getCheckReadMessage();
        JSONObject listJsonObject = JSONObject.fromObject(emisReadList);
        int checkCount = Integer.parseInt(listJsonObject.getString("listNum"));
        log.info("emis代办数据总数: " + checkCount);
        JSONArray array = listJsonObject.getJSONArray("data");
        JSONArray arrayApproves = new JSONArray();
        JSONArray arrayReads = new JSONArray();
        JSONArray arrayCampWarns = new JSONArray();
        if (checkCount != 0) {
            //	筛出审批待办的数据
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String fileType = obj.getString("fileType");
                if ("营销待办请求流程".equals(fileType)) {
                    arrayApproves.add(obj);
                }else if("营销阅知流程".equals(fileType)){
                    arrayReads.add(obj);
                }else if("营销预警流程".equals(fileType)){
                    arrayCampWarns.add(obj);
                }
            }
        }
        log.info("筛选出的审批待办数量={}, 阅知待办的数量={}, 营销预警待办的数量={}" , arrayApproves.size() ,arrayReads.size(),arrayCampWarns.size());
        try {
            dealCheckApproveEmisTask(emisApproveTasks, arrayApproves);
        } catch (Exception e) {
            log.error("dealCheckApproveEmisTask 稽查审批待办异常message=", e);
        }
        dealCheckEmisTask(emisReadTasks, arrayReads);
        dealCheckCampWarnEmisTask(campWarnInfoToEmisList,arrayCampWarns);
    }

    public void dealCheckApproveEmisTask(List<McdEmisTask> emisApproveTasks, JSONArray array) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime = sdf.format(date);
        //审批待办IOP侧
        int todoNum = emisApproveTasks.size();
        int tocheckNum = array.size();

        log.info(" iop审批代办数据个数: " + todoNum + " emis审批代办数据个数: " + tocheckNum);
        if (todoNum == tocheckNum) {
            // 调用稽核日志方法
            EmisUtils.addLog("0", "数据正常", beginTime, sdf.format(new Date()));
            log.info("EMIS稽核待办数据无误。。。");
            return;
        }

        // 待办数为0，稽核不为0
        if (CollectionUtils.isEmpty(emisApproveTasks)) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject o = array.getJSONObject(i);
                String unid = o.getString("unid");
                String feature = o.getString("feature");
                CmpApproveProcessInstance processInstance = instanceService.getById(feature);
                if (Objects.isNull(processInstance)){
                    log.error("iop7.0稽查任务删除emis代办2UNID: "+ unid +" 实例ID本系统不存在,直接返回!");
                    continue;
                }
                // 删除待办系统中待办记录
                EmisUtils.deleteMessage(unid, feature);
            }
            // 调用稽核日志方法
            EmisUtils.addLog("1", "待办系统多出待办", beginTime, sdf.format(new Date()));
            log.info("EMIS待办系统多出待办。。。");
            return;
        }

        // 待办数不为0，稽核为0
        if (tocheckNum == 0) {
            for (McdEmisTask task : emisApproveTasks) {

                McdCampDef campDef = mcdCampDefDao.selectById(task.getCampsegId());
                if (campDef == null){
                    log.error("稽核为0,定时任务新增审批待办异常campsegid: "+ task.getCampsegId());
                    continue;
                }
                String campseg_name =   campDef.getCampsegName();
                LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
                userWrapper.eq(SysUser::getUserId, task.getApprovalUser());
                List<SysUser> deptList = sysUserDao.selectList(userWrapper);
                String deptCode = "";
                String comCode = "";
                String approvalUserName = "";
                if (deptList != null && !deptList.isEmpty()) {
                    SysUser user = deptList.get(0);
                    deptCode = user.getDepartmentId();
                    comCode = user.getCityId();
                    approvalUserName = user.getUserName();
                }
                ApprovalResult approvalResult = new ApprovalResult();
                approvalResult.setApprovalUserId(task.getApprovalUser());
                approvalResult.setApprovalUserName(approvalUserName);
                approvalResult.setCreateUserId(task.getSubmitUser());
                approvalResult.setAdvice(task.getAdvice());
                approvalResult.setInstanceId(task.getInstanceId());
                approvalResult.setInstanceName(task.getInstanceName());
                approvalResult.setNodeId(task.getNodeId());
                approvalResult.setNodeName(task.getNodeName());
                try {
                    // 新增待办方法
                    emis.addMessage(approvalResult, campseg_name, deptCode, comCode);
                } catch (Exception e) {
                    log.error("定时任务新增审批待办异常message=", e);
                }
            }
            // 调用稽核日志方法
            EmisUtils.addLog("2", "待办系统缺少待办", beginTime, sdf.format(new Date()));
            log.info("EMIS待办系统缺少待办。。。");
            return;
        }


        String result = "0";
        String logInfo = "数据正常";
        // 对比待办系统待办数据
        for (int i = 0; i < array.size(); i++) {
            JSONObject o = array.getJSONObject(i);
            String unid = o.getString("unid");
            String feature = o.getString("feature");
            boolean exitFlag = false;
            for (McdEmisTask task : emisApproveTasks) {
                if (unid.equals(task.getInstanceId() + task.getNodeId() + task.getApprovalUser())) {
                    exitFlag = true;
                    break;
                }
            }
            // 稽核待办多了待办
            if (!exitFlag) {
                result = "1";
                logInfo = "待办系统多出待办";
                CmpApproveProcessInstance processInstance = instanceService.getById(feature);
                if (Objects.isNull(processInstance)){
                    log.error("iop7.0稽查任务删除emis代办2UNID: "+ unid +" 实例ID本系统不存在,直接返回!");
                    continue;
                }
                // 删除待办系统中待办记录
                EmisUtils.deleteMessage(unid, feature);
            }
        }
        if ("1".equals(result)) {
            EmisUtils.addLog(result, logInfo, beginTime, sdf.format(new Date()));
            log.info("EMIS" + logInfo + "...");
        }

        // 对比业务系统待办数据
        for (McdEmisTask task : emisApproveTasks) {
            boolean exitFlag = false;
            for (int i = 0; i < array.size(); i++) {
                JSONObject o = array.getJSONObject(i);
                String unid = o.getString("unid");
                if (unid.equals(task.getInstanceId() + task.getNodeId() + task.getApprovalUser())) {
                    exitFlag = true;
                    break;
                }
            }
            // 稽核待办少了待办
            if (!exitFlag) {
                McdCampDef campDef = mcdCampDefDao.selectById(task.getCampsegId());
                if (campDef == null){
                    log.error("稽核待办少了待办,定时任务新增审批待办异常campsegid: "+ task.getCampsegId());
                    continue;
                }
                String campseg_name =   campDef.getCampsegName();
                LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
                userWrapper.eq(SysUser::getUserId, task.getApprovalUser());
                List<SysUser> deptList = sysUserDao.selectList(userWrapper);
                String deptCode = "";
                String comCode = "";
                String approvalUserName = "";
                if (deptList != null && !deptList.isEmpty()) {
                    SysUser user = deptList.get(0);
                    deptCode = user.getDepartmentId();
                    comCode = user.getCityId();
                    approvalUserName = user.getUserName();
                }
                ApprovalResult approvalResult = new ApprovalResult();
                approvalResult.setApprovalUserId(task.getApprovalUser());
                approvalResult.setApprovalUserName(approvalUserName);
                approvalResult.setCreateUserId(task.getSubmitUser());
                approvalResult.setAdvice(task.getAdvice());
                approvalResult.setInstanceId(task.getInstanceId());
                approvalResult.setInstanceName(task.getInstanceName());
                approvalResult.setNodeId(task.getNodeId());
                approvalResult.setNodeName(task.getNodeName());

                try {
                    // 新增待办方法
                    emis.addMessage(approvalResult, campseg_name, deptCode, comCode);
                } catch (Exception e) {
                    log.error("定时任务新增审批待办异常message=", e);
                }
            }
            result = "2";
            logInfo = "待办系统缺少待办";
        }
        if ("2".equals(result)) {
            EmisUtils.addLog(result, logInfo, beginTime, sdf.format(new Date()));
            log.info("EMIS" + logInfo + "...");
        }
    }


    /**
     * 阅知待办处理---稽查
     *
     * @param emisReadTasks
     */
    public void dealCheckEmisTask(List<McdEmisReadTask> emisReadTasks, JSONArray readArrays) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime = sdf.format(date);
        //阅知待办IOP侧
        int todoReadNum = emisReadTasks.size();
        //阅知待办emis侧
        int tocheckReadNum = readArrays.size();

        log.info(" iop阅知代办数据个数: " + todoReadNum + " emis阅知代办数据个数: " + tocheckReadNum);
        if (todoReadNum == tocheckReadNum) {
            // 调用稽核日志方法
            EmisUtils.addReadLog("0", "数据正常【阅知】", beginTime, sdf.format(new Date()));
            log.info("EMIS稽核阅知待办数据无误。。。");
            return;
        }

        // IOP待办数为0，稽核不为0
        if (CollectionUtils.isEmpty(emisReadTasks)) {

            for (int i = 0; i < readArrays.size(); i++) {
                JSONObject o = readArrays.getJSONObject(i);
                String unid = o.getString("unid");
                String feature = o.getString("feature");
                // 删除待办系统中待办记录
                EmisUtils.deleteReadMessage(unid, feature);
            }
            // 调用稽核日志方法
            EmisUtils.addReadLog("1", "待办系统多出待办【阅知】", beginTime, sdf.format(new Date()));
            log.info("EMIS待办系统多出待办【阅知】");
            return;
        }

        // IOP待办数不为0，稽核为0
        if (tocheckReadNum == 0) {
            for (McdEmisReadTask task : emisReadTasks) {
                String campseg_name = task.getName();
                ApprovalResult approvalResult = new ApprovalResult();
                approvalResult.setApprovalUserId(task.getReadUser());
                approvalResult.setCreateUserId(task.getSubmitUser());
                approvalResult.setAdvice(task.getAdvice());
                String taskInstanceId = task.getId();
                String instanceId = taskInstanceId.split("_")[0];
                approvalResult.setInstanceId(instanceId);
                approvalResult.setInstanceName(task.getName());
                approvalResult.setNodeId(task.getNodeId());
                approvalResult.setServiceType(task.getBusinessId());
                try {
                    User approvalUser = getUserInfo(task.getReadUser());
                    // 1-策略 2-预警 3-客户通渠道活动
                    if (1 == task.getBusinessType()) {
                        // 新增阅知待办
                        emis.addReadMessage(approvalResult, campseg_name, approvalUser);
                    } else {
                        // 新增驳回阅知待办
                        emis.addRejectReadMessage(approvalResult, campseg_name, approvalUser);
                    }
                } catch (Exception e) {
                    log.error("新增阅知待办异常【阅知】", e);
                }
            }
            // 调用稽核日志方法
            EmisUtils.addReadLog("2", "待办系统缺少待办【阅知】", beginTime, sdf.format(new Date()));
            log.info("EMIS待办系统缺少待办【阅知】");
            return;
        }

        String result = "0";
        String logInfo = "数据正常【阅知】";
        // 对比待办系统待办数据
        for (int i = 0; i < readArrays.size(); i++) {
            JSONObject o = readArrays.getJSONObject(i);
            String unid = o.getString("unid");
            String feature = o.getString("feature");
            boolean exitFlag = false;
            for (McdEmisReadTask task : emisReadTasks) {
                String taskInstanceId = task.getId();
                String instanceId = taskInstanceId.split("_")[0];
                if (unid.equals(instanceId + "_" + task.getReadUser() + "_" + task.getBusinessId() + "_" + task.getReadUser())) {
                    exitFlag = true;
                    break;
                }
            }
            // 稽核待办比IOP待办多
            if (!exitFlag) {
                result = "1";
                logInfo = "待办系统多出待办【阅知】";
                // 删除待办系统中待办记录
                EmisUtils.deleteReadMessage(unid, feature);
            }
        }
        if ("1".equals(result)) {
            EmisUtils.addReadLog(result, logInfo, beginTime, sdf.format(new Date()));
            log.info("EMIS" + logInfo + "...");
        }

        // 对比业务系统待办数据
        for (McdEmisReadTask task : emisReadTasks) {
            boolean exitFlag = false;
            for (int i = 0; i < readArrays.size(); i++) {
                JSONObject o = readArrays.getJSONObject(i);
                String unid = o.getString("unid");
                String taskInstanceId = task.getId();
                String instanceId = taskInstanceId.split("_")[0];
                if (unid.equals(instanceId + "_" + task.getReadUser() + "_" + task.getBusinessId() + "_" + task.getReadUser())) {
                    exitFlag = true;
                    break;
                }
            }
            // 稽核待办比IOP待办少
            if (!exitFlag) {
                String campseg_name = task.getName();
                ApprovalResult approvalResult = new ApprovalResult();
                approvalResult.setApprovalUserId(task.getReadUser());
                approvalResult.setCreateUserId(task.getSubmitUser());
                approvalResult.setAdvice(task.getAdvice());
                String taskInstanceId = task.getId();
                String instanceId = taskInstanceId.split("_")[0];
                approvalResult.setInstanceId(instanceId);
                approvalResult.setInstanceName(task.getName());
                approvalResult.setNodeId(task.getNodeId());
                approvalResult.setServiceType(task.getBusinessId());
                try {
                    User approvalUser = getUserInfo(task.getReadUser());
                    // 新增阅知,不同类型，unid生成不一样
                    // 1-策略 2-预警 3-客户通渠道活动
                    if (1 == task.getBusinessType()) {
                        // 新增阅知待办
                        emis.addReadMessage(approvalResult, campseg_name, approvalUser);
                    } else {
                        // 新增驳回阅知待办
                        emis.addRejectReadMessage(approvalResult, campseg_name, approvalUser);
                    }
                } catch (Exception e) {
                    log.error("新增阅知待办异常【阅知】", e);
                }

                result = "2";
                logInfo = "待办系统缺少待办【阅知】";
            }
        }
        if ("2".equals(result)) {
            EmisUtils.addReadLog(result, logInfo, beginTime, sdf.format(new Date()));
            log.info("EMIS" + logInfo + "...");
        }

    }

    /**
     * 策略预警待办处理---稽查
     * @param campWarnEmisTasks
     */
    public void dealCheckCampWarnEmisTask(List<McdCampWarnEmisTask> campWarnEmisTasks,JSONArray readArrays) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime = sdf.format(date);
        //策略预警待办IOP侧
        int todoWarnNum = campWarnEmisTasks.size();
        //策略预警待办emis侧
        int tocheckReadNum =readArrays.size();

        log.info(" iop预警代办数据个数: " + todoWarnNum + " emis预警代办数据个数: " + tocheckReadNum);
        if (todoWarnNum == tocheckReadNum) {
            // 调用稽核日志方法
            EmisUtils.addReadLog("0", "数据正常【预警】", beginTime, sdf.format(new Date()));
            log.info("EMIS稽核预警待办数据无误。。。");
            return;
        }

        // IOP待办数为0，稽核不为0
        if (CollectionUtils.isEmpty(campWarnEmisTasks)) {
            for (int i = 0; i < readArrays.size(); i++) {
                JSONObject o = readArrays.getJSONObject(i);
                String unid = o.getString("unid");
                String feature = o.getString("feature");
                // 删除待办系统中待办记录
                EmisUtils.deleteCampWarnMessage(unid, feature);
            }
            // 调用稽核日志方法
            EmisUtils.addReadLog("1", "待办系统多出待办【预警】", beginTime, sdf.format(new Date()));
            log.info("EMIS待办系统多出待办【预警】");
            return;
        }

        // IOP待办数不为0，稽核为0
        if (tocheckReadNum == 0) {
            for (McdCampWarnEmisTask task : campWarnEmisTasks) {
                try {
                    User user = getUserInfo(task.getCreater());
                    // 新增预警待办
                    emis.addCampWarnMessage(task, task.getCampsegName(), user);
                } catch (Exception e) {
                    log.error("新增预警待办异常【预警】", e);
                }
            }
            // 调用稽核日志方法
            EmisUtils.addReadLog("2", "待办系统缺少待办【预警】", beginTime, sdf.format(new Date()));
            log.info("EMIS待办系统缺少待办【预警】");
            return;
        }

        String result = "0";
        String logInfo = "数据正常【预警】";
        // 对比待办系统待办数据
        for (int i = 0; i < readArrays.size(); i++) {
            JSONObject o = readArrays.getJSONObject(i);
            String unid = o.getString("unid");
            String feature = o.getString("feature");
            boolean exitFlag = false;
            for (McdCampWarnEmisTask task : campWarnEmisTasks) {
                if (unid.equals(task.getUniqueIdentifierId() +"_"+ task.getCampsegId() +"_"+ task.getCreater())) {
                    exitFlag = true;
                    break;
                }
            }
            // 稽核待办比IOP待办多
            if (!exitFlag) {
                result = "1";
                logInfo = "待办系统多出待办【预警】";
                // 删除待办系统中待办记录
                EmisUtils.deleteCampWarnMessage(unid, feature);
            }
        }
        if ("1".equals(result)) {
            EmisUtils.addReadLog(result, logInfo, beginTime, sdf.format(new Date()));
            log.info("EMIS" + logInfo + "...");
        }

        // 对比业务系统待办数据
        for (McdCampWarnEmisTask task : campWarnEmisTasks) {
            boolean exitFlag = false;
            for (int i = 0; i < readArrays.size(); i++) {
                JSONObject o = readArrays.getJSONObject(i);
                String unid = o.getString("unid");
                if (unid.equals(task.getUniqueIdentifierId() +"_"+ task.getCampsegId() +"_"+ task.getCreater())) {
                    exitFlag = true;
                    break;
                }
            }
            // 稽核待办比IOP待办少
            if (!exitFlag) {
                try {
                    User user = getUserInfo(task.getCreater());
                    // 新增预警待办
                    emis.addCampWarnMessage(task, task.getCampsegName(), user);
                } catch (Exception e) {
                    log.error("新增阅知待办异常【预警】", e);
                }

                result = "2";
                logInfo = "待办系统缺少待办【预警】";
            }
        }
        if ("2".equals(result)) {
            EmisUtils.addReadLog(result, logInfo, beginTime, sdf.format(new Date()));
            log.info("EMIS" + logInfo + "...");
        }
    }

    private User getUserInfo(String userId) {

        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SysUser::getUserId, userId);
        List<SysUser> deptList = sysUserDao.selectList(userWrapper);
        User user = new User();
        if (deptList != null && !deptList.isEmpty()) {
            SysUser sysUser = deptList.get(0);
            user.setId(sysUser.getId());
            user.setUserId(sysUser.getUserId());
            user.setUserName(sysUser.getUserName());
            user.setCityId(sysUser.getCityId());
            user.setDepartmentId(sysUser.getDepartmentId());
            user.setMobilePhone(sysUser.getMobilePhone());
        }

        return user;
    }


}
