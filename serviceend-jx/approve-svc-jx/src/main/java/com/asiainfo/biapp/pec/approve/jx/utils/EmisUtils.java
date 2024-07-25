package com.asiainfo.biapp.pec.approve.jx.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.approve.jx.model.ApprovalResult;
import com.asiainfo.biapp.pec.approve.jx.model.McdApprovalAdviceModel;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampDef;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTask;
import com.asiainfo.biapp.pec.approve.jx.service.IMcdCampDefService;
import com.asiainfo.biapp.pec.approve.jx.service.McdApprovalAdviceService;
import com.asiainfo.biapp.pec.approve.model.User;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("emisUtil")
@Slf4j
public class EmisUtils {

    //营销待办请求流程header
    static String header = "{\"appId\":\"imcd\",\r\n" + "\"appName\":\"簇群平台\",\r\n"
            + "\"processId\":\"yxdbprocess\",\r\n" + "\"processName\":\"营销待办请求流程\",\r\n"
            + "\"appProcessId\":\"imcdyxdbprocess\"\r\n" + "}";
    //营销阅知待办流程header
    static String readHeader = "{\"appId\":\"imcd\",\r\n" + "\"appName\":\"簇群平台\",\r\n"
            + "\"processId\":\"yxdbprocess2\",\r\n" + "\"processName\":\"营销阅知流程\",\r\n"
            + "\"appProcessId\":\"imcdyxdbprocess2\"\r\n" + "}";
    //营销预警待办流程header
    static String warnHeader = "{\"appId\":\"imcd\",\r\n" + "\"appName\":\"簇群平台\",\r\n"
            + "\"processId\":\"yxdbprocess3\",\r\n" + "\"processName\":\"营销预警流程\",\r\n"
            + "\"appProcessId\":\"imcdyxdbprocess3\"\r\n" + "}";

    @Resource
    private McdApprovalAdviceService mcdApprovalAdviceService;
    @Resource
    private IMcdCampDefService mcdCampDefService;

    /**
     * 删除待办方法
     *
     * @param approvalResult
     * @return
     */
    public static boolean deleteMessage(ApprovalResult approvalResult) {
        Map<String, String> params = new HashMap<String, String>();
        String unid = approvalResult.getInstanceId() + approvalResult.getNodeId() + approvalResult.getApprovalUserId();
        params.put("unid", unid);
        params.put("appId", "imcd");
        params.put("additionalUnid", "");
        params.put("feature", approvalResult.getInstanceId());
        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
             resp = RestTemplateUtils.post("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/deleteMessage", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emis操作deleteMessage");
            return  true;
        }else {
            resp = RestTemplateUtils.post("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/deleteMessage", params);
        }
        return checkResp(resp,unid);
    }

    /**
     * 删除待办方法---审批待办
     *
     * @param
     * @return
     */
    public static void deleteMessage(String unid,String feature) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("unid", unid);
        params.put("appId", "imcd");
        params.put("additionalUnid", "");
        params.put("feature", feature);
        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
            resp = RestTemplateUtils.post("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/deleteMessage", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emis操作deleteMessage!");
        }else {
            resp = RestTemplateUtils.post("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/deleteMessage", params);
        }

        checkResp(resp,unid);
    }

    /**
     * 删除待办方法---阅知待办
     *
     * @param
     * @return
     */
    public static void deleteReadMessage(String unid,String feature) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("unid", unid);
        params.put("appId", "imcd");
        params.put("additionalUnid", "");
        params.put("feature", feature);
        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
            resp = RestTemplateUtils.postEmisRead("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/deleteMessage", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emis操作deleteReadMessage!");
        }else {
            resp = RestTemplateUtils.postEmisRead("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/deleteMessage", params);
        }

        checkResp(resp,unid);
    }

    /**
     * 删除待办方法---预警待办
     *
     * @param
     * @return
     */
    public static void deleteCampWarnMessage(String unid,String feature) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("unid", unid);
        params.put("appId", "imcd");
        params.put("additionalUnid", "");
        params.put("feature", feature);
        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
            resp = RestTemplateUtils.postEmisCampWarn("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/deleteMessage", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emis操作deleteCampWarnMessage!");
        }else {
            resp = RestTemplateUtils.postEmisCampWarn("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/deleteMessage", params);
        }

        checkResp(resp,unid);
    }

    /**
     * 获取待办转已办方法
     *
     * @param approvalResult
     */
    public static boolean updateMessage(ApprovalResult approvalResult) {
        Map<String, String> params = new HashMap<String, String>();
        String unid = approvalResult.getInstanceId() + approvalResult.getNodeId() + approvalResult.getApprovalUserId();
        params.put("projectName", "imcd@jx.cmcc");
        params.put("msgType", "1");
        params.put("unid", unid);
        params.put("targets", approvalResult.getApprovalUserId());
        params.put("processor", approvalResult.getCreateUserId());
        params.put("feature", approvalResult.getInstanceId());
        // 待办转已办
        String resp = "";

        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
            resp = RestTemplateUtils.post("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/updateMessageState", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emsi操作updateMessage!");
            return true;
        }else {
            resp = RestTemplateUtils.post("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/updateMessageState", params);
        }


        return checkResp(resp,unid);
    }

    /**
     * 获取阅知待办转已阅方法
     *
     * @param jsonObject
     * @param userId
     */
    public static boolean updateReadMessage(JSONObject jsonObject, String userId,String serviceType) {
        String unid = jsonObject.getString("unid");
        if(StrUtil.isEmpty(unid)){
            log.error("unid不能为空:{}", JSONUtil.toJsonStr(jsonObject));
            return false;
        }
        Map<String, String> params = new HashMap<String, String>();
        String approvalUserId = jsonObject.getString("approvalUserId");
        String instanceIdStr = unid.split("_")[0];
        params.put("projectName", "imcd@jx.cmcc");
        params.put("msgType", "1");
        params.put("unid", unid);
        params.put("targets", approvalUserId);
        params.put("processor", userId);
        params.put("feature", instanceIdStr);
        // 待办转已办

        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
            resp = RestTemplateUtils.postEmisRead("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/updateMessageState", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emis操作updateReadMessage!");
            return true;
        }else {
            resp = RestTemplateUtils.postEmisRead("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/updateMessageState", params);
        }
        log.info("updateReadMessage resp="+resp);
        return checkResp(resp,unid);
    }

    /**
     * 预警待办转已办方法
     *
     * @param jsonObject
     * @param userId
     */
    public static boolean updateWarnMessage(JSONObject jsonObject, String userId) {
        Map<String, String> params = new HashMap<String, String>();
        String uniqueIdentifierId = jsonObject.getString("UNIQUE_IDENTIFIER_ID");
        String campsegId = jsonObject.getString("CAMPSEG_ID");
        String creater = jsonObject.getString("CREATER");

        String unid = uniqueIdentifierId +"_"+ campsegId+"_"+creater;
        params.put("projectName", "imcd@jx.cmcc");
        params.put("msgType", "1");
        params.put("unid", unid);
        params.put("targets", userId);
        params.put("processor", userId);
        params.put("feature", uniqueIdentifierId);
        // 待办转已办

        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
            resp = RestTemplateUtils.postEmisCampWarn("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/updateMessageState", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emis操作!updateWarnMessage");
            return true;
        }else {
            resp = RestTemplateUtils.postEmisCampWarn("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/updateMessageState", params);
        }
        log.info("updateWarnMessage resp="+resp);
        return checkResp(resp,unid);
    }

    /**
     * 获取新增待办方法
     *
     * @param approvalResult
     * @param comCode
     * @param deptCode
     */
    public boolean addMessage(ApprovalResult approvalResult, String campseg_name, String deptCode, String comCode)  {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(date);
        String unid = approvalResult.getInstanceId() + approvalResult.getNodeId() + approvalResult.getApprovalUserId();
        String mcd7EmisUrl = RedisUtils.getDicValue("MCD_EMIS_ADD_URL");//  mcd7/#/tactics4.0?
        Map<String, String> params = new HashMap<>();
        params.put("projectName", "imcd@jx.cmcc");
        params.put("appId", "imcd");
        params.put("targets", approvalResult.getApprovalUserId());
        params.put("targetsName", approvalResult.getApprovalUserName());
        params.put("processor", "");
        params.put("processorName", "无");
        params.put("creator", approvalResult.getCreateUserId());
        params.put("opinion", approvalResult.getAdvice());
        params.put("title", campseg_name+"_营销");
        params.put("unid",unid );
        params.put("additionalUnid", "");
        params.put("feature", approvalResult.getInstanceId());
        params.put("reserve", "");
        params.put("composeTime", now);
        params.put("process", "营销待办请求流程");
        params.put("fileType", "营销待办请求流程");
        params.put("activity", (approvalResult.getNodeName()==null||"null".equals(approvalResult.getNodeName())||"".equals(approvalResult.getNodeName()))?"管理员":approvalResult.getNodeName());
        params.put("newly", "false");
        params.put("category", "营销待办请求流程");
        params.put("urgency", "普通");
        params.put("secrecy", "一般");
        params.put("serial", "营销待办请求流程");
       // params.put("link", "http://emis3.jx.chinamobile.com/eimcd/mcd-web/login/verify_JXEMIS.jsp?redirectURL=tacticsApprove");
      //  params.put("link", "http://emis3.jx.chinamobile.com/eimcd"+mcd7EmisUrl+"redirectURL=tacticsApprove");
        params.put("link", mcd7EmisUrl+"redirectURL=tacticsApprove");
        params.put("linkView", now);
        params.put("msgType", "0");
        params.put("deptCode", deptCode);
        params.put("comCode", comCode);
        params.put("moaConfig", "1"); // 手机H5 必选参数
        addParamMoaLink(approvalResult, params);
       // params.put("moaLink", "");

        // 生成待办
        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
            resp = RestTemplateUtils.post("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/addMessage", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emis操作addCMessage!");
            return true;
        }else {
            resp = RestTemplateUtils.post("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/addMessage", params);
        }

        return checkResp(resp,unid);
    }

    /**
     * 获取新增驳回阅知待办方法--客户通活动&客户通预警
     *
     * @param approvalResult
     * @param campseg_name
     * @param user
     */
    public boolean addRejectReadMessage(ApprovalResult approvalResult, String campseg_name,  User user)  {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(date);
        //String unid = approvalResult.getInstanceId() +"_"+ approvalResult.getNodeId() +"_"+ approvalResult.getApprovalUserId()+"_"+approvalResult.getServiceType()+"_"+user.getId();
        String unid = approvalResult.getInstanceId() +"_"+ user.getUserId()+"_"+approvalResult.getServiceType()+"_"+approvalResult.getNodeId();
        String mcd7EmisUrl = RedisUtils.getDicValue("MCD_EMIS_ADD_URL");//  mcd7/#/tactics4.0?
        Map<String, String> params = new HashMap<>();
        params.put("projectName", "imcd@jx.cmcc");
        params.put("appId", "imcd");
        params.put("targets", user.getUserId());
        params.put("targetsName", user.getUserName());
        params.put("processor", "");
        params.put("processorName", "无");
        params.put("creator", approvalResult.getCreateUserId());
        params.put("opinion", approvalResult.getAdvice());
        params.put("title", campseg_name+"_阅知待办");
        params.put("unid",unid );
        params.put("additionalUnid", "");
        params.put("feature", approvalResult.getInstanceId());
        params.put("reserve", "");
        params.put("composeTime", now);
        params.put("process", "营销阅知流程");
        params.put("fileType", "营销阅知流程");
        params.put("activity", (approvalResult.getNodeName()==null||"null".equals(approvalResult.getNodeName())||"".equals(approvalResult.getNodeName()))?"管理员":approvalResult.getNodeName());
        params.put("newly", "false");
        params.put("category", "营销阅知流程");
        params.put("urgency", "普通");
        params.put("secrecy", "一般");
        params.put("serial", "营销阅知流程");
        // params.put("link", "http://emis3.jx.chinamobile.com/eimcd/mcd-web/login/verify_JXEMIS.jsp?redirectURL=tacticsRead");
        // params.put("link", "http://emis3.jx.chinamobile.com/eimcd"+ mcd7EmisUrl+"redirectURL=tacticsRead");
        params.put("link", mcd7EmisUrl+"redirectURL=tacticsRejectRead");
        params.put("linkView", now);
        params.put("moaConfig", "1");//0表示未接入手机端，1表示手机H5接入，2表示手机app接入
        params.put("msgType", "0");
        params.put("deptCode", user.getDepartmentId());
        params.put("comCode", user.getCityId());
        //拼接手机端参数信息，用于手机端跳转携带
        addParamMoaLinkCampRead(approvalResult,params);
        // 生成待办
        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
            resp = RestTemplateUtils.postEmisRead("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/addMessage", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emis操作addReadMessage!,请求参数:{}", JSONUtil.toJsonStr(params));
            return true;
        }else {
            resp = RestTemplateUtils.postEmisRead("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/addMessage", params);
        }
        return checkResp(resp,unid);
    }

    /**
     * 获取新增阅知待办方法
     *
     * @param approvalResult
     * @param campseg_name
     * @param user
     */
    public boolean addReadMessage(ApprovalResult approvalResult, String campseg_name,  User user)  {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(date);
        //String unid = approvalResult.getInstanceId() +"_"+ approvalResult.getNodeId() +"_"+ approvalResult.getApprovalUserId()+"_"+approvalResult.getServiceType()+"_"+user.getId();
        String unid = approvalResult.getInstanceId() +"_"+ user.getUserId()+"_"+approvalResult.getServiceType()+"_"+approvalResult.getNodeId();
        String mcd7EmisUrl = RedisUtils.getDicValue("MCD_EMIS_ADD_URL");//  mcd7/#/tactics4.0?
        Map<String, String> params = new HashMap<>();
        params.put("projectName", "imcd@jx.cmcc");
        params.put("appId", "imcd");
        params.put("targets", user.getUserId());
        params.put("targetsName", user.getUserName());
        params.put("processor", "");
        params.put("processorName", "无");
        params.put("creator", approvalResult.getCreateUserId());
        params.put("opinion", approvalResult.getAdvice());
        params.put("title", campseg_name+"_阅知待办");
        params.put("unid",unid );
        params.put("additionalUnid", "");
        params.put("feature", approvalResult.getInstanceId());
        params.put("reserve", "");
        params.put("composeTime", now);
        params.put("process", "营销阅知流程");
        params.put("fileType", "营销阅知流程");
        params.put("activity", (approvalResult.getNodeName()==null||"null".equals(approvalResult.getNodeName())||"".equals(approvalResult.getNodeName()))?"管理员":approvalResult.getNodeName());
        params.put("newly", "false");
        params.put("category", "营销阅知流程");
        params.put("urgency", "普通");
        params.put("secrecy", "一般");
        params.put("serial", "营销阅知流程");
       // params.put("link", "http://emis3.jx.chinamobile.com/eimcd/mcd-web/login/verify_JXEMIS.jsp?redirectURL=tacticsRead");
       // params.put("link", "http://emis3.jx.chinamobile.com/eimcd"+ mcd7EmisUrl+"redirectURL=tacticsRead");
        params.put("link", mcd7EmisUrl+"redirectURL=tacticsRead");
        params.put("linkView", now);
        params.put("moaConfig", "1");//0表示未接入手机端，1表示手机H5接入，2表示手机app接入
        params.put("msgType", "0");
        params.put("deptCode", user.getDepartmentId());
        params.put("comCode", user.getCityId());
        //拼接手机端参数信息，用于手机端跳转携带
        addParamMoaLinkCampRead(approvalResult,params);
        // 生成待办
        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
            resp = RestTemplateUtils.postEmisRead("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/addMessage", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emis操作addReadMessage!,请求参数:{}", JSONUtil.toJsonStr(params));
            return true;
        }else {
            resp = RestTemplateUtils.postEmisRead("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/addMessage", params);
        }
        return checkResp(resp,unid);
    }

    /**
     * 获取新增预警待办方法
     *
     * @param campWarnEmisTask
     * @param campseg_name
     * @param user
     */
    public boolean addCampWarnMessage(McdCampWarnEmisTask campWarnEmisTask, String campseg_name, User user)  {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(date);
        String unid = campWarnEmisTask.getUniqueIdentifierId() +"_"+ campWarnEmisTask.getCampsegId()+"_"+campWarnEmisTask.getCreater();
        String mcd7EmisUrl = RedisUtils.getDicValue("MCD_EMIS_ADD_URL");//  mcd7/#/tactics4.0?
        Map<String, String> params = new HashMap<>();
        params.put("projectName", "imcd@jx.cmcc");
        params.put("appId", "imcd");
        params.put("targets", user.getUserId());
        params.put("targetsName", user.getUserName());
        params.put("processor", "");
        params.put("processorName", "无");
        params.put("creator", campWarnEmisTask.getCreater());
        params.put("opinion", "");
        params.put("title", "【策略预警】"+campseg_name+"_预警待办");
        params.put("unid",unid );
        params.put("additionalUnid", "");
        params.put("feature", campWarnEmisTask.getUniqueIdentifierId());
        params.put("reserve", "");
        params.put("composeTime", now);
        params.put("process", "营销预警流程");
        params.put("fileType", "营销预警流程");
        params.put("activity", "管理员");
        params.put("newly", "false");
        params.put("category", "营销预警流程");
        params.put("urgency", "普通");
        params.put("secrecy", "一般");
        params.put("serial", "营销预警流程");
        params.put("link",  mcd7EmisUrl+"redirectURL=tacticsWarnTodoDeal");
        params.put("linkView", now);
        params.put("moaConfig", "1");//0表示未接入手机端，1表示手机H5接入，2表示手机app接入
        params.put("msgType", "0");
        params.put("deptCode", user.getDepartmentId());
        params.put("comCode", user.getCityId());
        //拼接手机端参数信息，用于手机端跳转携带
        addParamMoaLinkCampWarn(campWarnEmisTask,user,params);
        // params.put("moaLink", "");
        // 生成待办
        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){// 1 测试, 0生产
            resp = RestTemplateUtils.postEmisCampWarn("http://10.239.35.140:8080/backlogs/unionmessageservice/unionmessage/addMessage", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境
            log.info("开发环境执行emis操作addCampWarnMessage!");
        }else {
            resp = RestTemplateUtils.postEmisCampWarn("http://10.182.30.2:8082/backlogs/unionmessageservice/unionmessage/addMessage", params);
        }
        return checkResp(resp,unid);
    }


    /**
     * 稽核方法(阅知待办)
     * 与查询审批待办差异：header流程信息不一样
     *
     * @return
     */
    public static String getCheckReadMessage() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -365);
        Date lastDate = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime = sdf.format(lastDate);
        String endTime = sdf.format(date);
        Map<String, String> params = new HashMap<String, String>();
        params.put("currentPage", "0");
        params.put("appId", "imcd");
        params.put("pageSize", "5000");
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);

        // 查询阅知待办
        String resp = "";
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){
            resp = RestTemplateUtils.postEmisRead("http://10.239.35.140:8080/backlogs/unionmessagecheck/check/selectMessageList", params);
        }else {
           // resp = RestTemplateUtils.postEmisRead("http://10.239.37.139:8080/unionmessagecheck/check/selectMessageList", params);
            resp = RestTemplateUtils.postEmisRead("http://10.182.30.2:8082/backlogs/unionmessagecheck/check/selectMessageList", params);
        }
        return resp;
    }


    /**
     * 增加稽核日志方法---审批待办
     *
     * @param result
     * @param log
     * @param beginTime
     * @param endTime
     */
    public static void addLog(String result, String log, String beginTime, String endTime) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = sdf.format(date);
        Map<String, String> params = new HashMap<String, String>();
        params.put("checkId", now);
        params.put("appId", "imcd");
        params.put("result", result);
        params.put("log", log);
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);
        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){
            RestTemplateUtils.post("http://10.239.35.140:8080/backlogs/unionmessagecheck/log/addLog", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境

        }else {
            RestTemplateUtils.post("http://10.182.30.2:8082/backlogs/unionmessagecheck/log/addLog", params);
           // RestTemplateUtils.post("http://10.239.37.139:8080/unionmessagecheck/log/addLog", params);
        }


    }

    /**
     * 增加稽核日志方法---阅知待办
     *
     * @param result
     * @param log
     * @param beginTime
     * @param endTime
     */
    public static void addReadLog(String result, String log, String beginTime, String endTime) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = sdf.format(date);
        Map<String, String> params = new HashMap<String, String>();
        params.put("checkId", now);
        params.put("appId", "imcd");
        params.put("result", result);
        params.put("log", log);
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);

        if ("1".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){
            RestTemplateUtils.postEmisRead("http://10.239.35.140:8080/backlogs/unionmessagecheck/log/addLog", params);
        }else if ("2".equals(RedisUtils.getDicValue("MCD_EMIS_PRODUCTION_ENVIRONMENT"))){//2开发环境

        }else {
           // RestTemplateUtils.postEmisRead("http://10.239.37.139:8080/unionmessagecheck/log/addLog", params);
            RestTemplateUtils.postEmisRead("http://10.182.30.2:8082/backlogs/unionmessagecheck/log/addLog", params);
        }

    }
    /**
     * 校验返回值
     *
     * @param resp
     */
    private static boolean checkResp(String resp,String uuid) {
        net.sf.json.JSONObject respJson = net.sf.json.JSONObject.fromObject(resp);
        if (respJson.containsKey("resultCode")) {
            String resultCode = respJson.getString("resultCode");
            String remark = respJson.getString("remark");
            if ("000".equals(resultCode)) {
                log.info("调用EMIS接口成功！" + uuid);
                return true;
            } else {
                log.error(uuid +" 调用EMIS接口失败！错误为：" + remark);
                return false;
            }
        } else {
            log.error("调用EMIS接口失败！"+ uuid);
        }
        return false;
    }

    /**
     *  H5手机端接收参数使用, add param: moalink
     *
     * @param approvalResult  提交审批时,审批状态信息对象
     * @param params          将moalink参数放入params中mcd_approval_advice
     */
    private void addParamMoaLink(ApprovalResult approvalResult, Map<String, String> params) {
        String campsegId = "";
        try {
            LambdaQueryWrapper<McdApprovalAdviceModel> queryWrapper =new LambdaQueryWrapper<>();
            queryWrapper.eq(McdApprovalAdviceModel::getInstanceId,approvalResult.getInstanceId())
                        .eq(McdApprovalAdviceModel::getNodeId,approvalResult.getNodeId())
                        .orderByDesc(McdApprovalAdviceModel::getDealTime ).last(" limit 1");
             McdApprovalAdviceModel adviceModel = mcdApprovalAdviceService.getOne(queryWrapper);
            // 查询 communicationstate
            int communicationstate = -1;
             if (Objects.nonNull(adviceModel)){
               communicationstate = adviceModel.getDealStatus();
             }

            List<McdCampDef> result = mcdCampDefService.listChildCampByCampsegRootId(approvalResult.getServiceType());

            if (CollectionUtils.isNotEmpty(result)) {
                campsegId = result.get(0).getCampsegId();
            }
            Map<String, String> moaLineValue = new HashMap<>();
           JSONObject mobLinkJson = new JSONObject();
            moaLineValue.put("instanceId", approvalResult.getInstanceId()); // 实例Id (必要)
            moaLineValue.put("nodeId", approvalResult.getNodeId());         // 审批节点(必要)
            moaLineValue.put("approvalTimes", approvalResult.getApprovalTimes() + ""); // 审批次数(必要)
            moaLineValue.put("createUserId", approvalResult.getCreateUserId());        // 当前用户Id(必要)
            moaLineValue.put("nodeName", approvalResult.getNodeName());
            moaLineValue.put("advice", approvalResult.getAdvice());                    // 审批意见
            moaLineValue.put("result", approvalResult.getResult() + "");               // 审批结果
            moaLineValue.put("approveUser", approvalResult.getApprovalUser());         // 审批用户名
            moaLineValue.put("approvalUserId", approvalResult.getApprovalUserId());
            moaLineValue.put("approvalUserName", approvalResult.getApprovalUserName());
            moaLineValue.put("serviceType", approvalResult.getServiceType());         //活动id(父)
            moaLineValue.put("camsepgId", campsegId);                                 //活动id(子)
            // 判断是否显示沟通人弹窗
            if (communicationstate == -1) {
                moaLineValue.put("communicationstate", "");
            } else {
                moaLineValue.put("communicationstate", communicationstate + "");
            }
            mobLinkJson.put("moaLineValue", moaLineValue);
            String moaStr = mobLinkJson.toString();
            moaStr = URLEncoder.encode(moaStr, "utf-8");
            params.put("moaLink", "http://10.180.211.100:8088/hmh5/mobilesp7.html?params=" + moaStr);
        } catch (Exception e) {
            log.error("H5手机端接OA连接拼接参数异常", e);
        }
    }

    /**
     * H5手机端接收参数使用---活动预警待办
     * @param campWarnEmisTask
     * @param user
     * @param params
     */
    private void addParamMoaLinkCampWarn(McdCampWarnEmisTask campWarnEmisTask, User user, Map<String, String> params) {
        try {
            Map<String, String> moaLineValue = new HashMap<>();
            JSONObject mobLinkJson = new JSONObject();
            moaLineValue.put("uniqueIdentifierId", campWarnEmisTask.getUniqueIdentifierId()); // 唯一识别码
            moaLineValue.put("campsegId", campWarnEmisTask.getCampsegId()); // 活动ID
            moaLineValue.put("creater", campWarnEmisTask.getCreater()); // 活动预警创建人
            moaLineValue.put("userId", user.getId());      // 当前用户Id(必要)
            moaLineValue.put("campsegPId",campWarnEmisTask.getCampsegPId());//活动父ID

            mobLinkJson.put("moaLineValue", moaLineValue);
            String moaStr = mobLinkJson.toString();
            moaStr = URLEncoder.encode(moaStr, "utf-8");
            params.put("moaLink", "http://10.180.211.100:8088/hmh5/campWarnMobilesp.html?params=" + moaStr);
        } catch (Exception e) {
            log.error("【活动预警待办】H5手机端接OA连接拼接参数异常", e);
        }
    }

    /**
     * H5手机端接收参数使用---活动预警待办
     * @param campWarnEmisTask
     * @param user
     * @param params
     */
    private void addParamMoaLinkCampRead(ApprovalResult approvalResult, Map<String, String> params) {
        String campsegId = "";
        try {
            LambdaQueryWrapper<McdApprovalAdviceModel> queryWrapper =new LambdaQueryWrapper<>();
            queryWrapper.eq(McdApprovalAdviceModel::getInstanceId,approvalResult.getInstanceId())
                    .eq(McdApprovalAdviceModel::getNodeId,approvalResult.getNodeId())
                    .orderByDesc(McdApprovalAdviceModel::getDealTime ).last(" limit 1");
            McdApprovalAdviceModel adviceModel = mcdApprovalAdviceService.getOne(queryWrapper);

            List<McdCampDef> result = mcdCampDefService.listChildCampByCampsegRootId(approvalResult.getServiceType());

            if (CollectionUtils.isNotEmpty(result)) {
                campsegId = result.get(0).getCampsegId();
            }
            Map<String, String> moaLineValue = new HashMap<>();
            JSONObject mobLinkJson = new JSONObject();

            // 唯一识别码
            moaLineValue.put("uniqueIdentifierId", params.get("unid"));
            // 活动ID(def表中的活动ID-非rootId)
            moaLineValue.put("campsegId", campsegId);
            // 活动预警创建人
            moaLineValue.put("creater",approvalResult.getCreateUserId());
            // 待办目标用户
            moaLineValue.put("userId", params.get("targets"));
            //活动父ID（rootId）
            moaLineValue.put("campsegPId",approvalResult.getServiceType());

            mobLinkJson.put("moaLineValue", moaLineValue);
            String moaStr = mobLinkJson.toString();
            moaStr = URLEncoder.encode(moaStr, "utf-8");
            params.put("moaLink", "http://10.180.211.100:8088/hmh5/readMobilesp.html?params=" + moaStr);
        } catch (Exception e) {
            log.error("H5手机端接OA连接拼接参数异常", e);
        }
    }
}
