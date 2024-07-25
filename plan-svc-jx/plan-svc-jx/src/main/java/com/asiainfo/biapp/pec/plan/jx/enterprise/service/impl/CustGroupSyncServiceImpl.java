package com.asiainfo.biapp.pec.plan.jx.enterprise.service.impl;


import com.asiainfo.biapp.pec.plan.jx.enterprise.mapper.ICocCustGroupSyncMapper;
import com.asiainfo.biapp.pec.plan.jx.enterprise.model.McdSysInterfaceDef;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.ICustGroupSyncService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.IMtlCallWsUrlService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.ISmsMessageService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.AppConfigService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.CustSyncBO;
import com.asiainfo.biapp.pec.plan.model.McdSysUser;
import com.asiainfo.biapp.pec.plan.service.IMcdSysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.xfire.client.Client;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustGroupSyncServiceImpl implements ICustGroupSyncService {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static String SMS_CONTENT_KEYWORDS = "营销管理平台 ";


    @Autowired
    private ICocCustGroupSyncMapper cocCustGroupSyncMapper;

    @Autowired
    private IMtlCallWsUrlService callwsUrlService;

    @Autowired
    private ISmsMessageService smsMessageService;

    @Autowired
    private IMcdSysUserService mcdSysUserService;

    @Override
    public String saveCustPushLog(String custInfoXml, int pushType) {
        String customGroupId = "";
        String customGroupDataDate = "";
        String customGroupName = "";
        CustSyncBO custSyncBO = new CustSyncBO();
        ;
        int flag = 1;
        StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<result><flag>");

        String pushSmsUserId = "";
        try {
            Document dom = DocumentHelper.parseText(custInfoXml);
            Element root = dom.getRootElement();
            Element data = root.element("data");
            //客户群ID    mtl_group_info.custom_group_id
            customGroupId = data.element("customGroupId") == null ? null : data.element("customGroupId").getText();
            //客户群名称
            customGroupName = data.element("customGroupName") == null ? null : data.element("customGroupName").getText();
            //清单数据最新日期
            customGroupDataDate = data.element("dataDate") == null ? null : data.element("dataDate").getText();
            pushSmsUserId = data.element("userId") == null ? "" : data.element("userId").getText();
            String dataCycle = data.element("dataCycle") == null ? "" : data.element("dataCycle").getText();

            String userCampType = data.element("userCampType") == null ? "" : data.element("userCampType").getText();
            String groupListType = data.element("LLN0022131") == null ? "" : data.element("LLN0022131").getText();

            custSyncBO.setCustomGroupId(customGroupId);
            custSyncBO.setCustomGroupName(customGroupName);
            custSyncBO.setDataDate(customGroupDataDate);
            custSyncBO.setCreateUserId(pushSmsUserId);
            custSyncBO.setUserCampType(userCampType);
            custSyncBO.setGroupListType(groupListType);
            custSyncBO.setUpdateCycle(dataCycle == null ? 1 : Integer.valueOf(dataCycle));
            int cnt = cocCustGroupSyncMapper.queryCustGroupHis(customGroupId, customGroupDataDate, pushType);
            if (cnt > 0) {
                //上次推送客户群数据还在处理中，不能再推送
                xml.append(2 + "</flag><groupId>");
                xml.append(customGroupId + "</groupId><msg>");
                xml.append("该客户群数据正在入库,请稍后再试</msg></result>");
                log.error("客户群ID：{},日期{}客户群数据正在入库,请稍后再试！", customGroupId, customGroupDataDate);
                custSyncBO.setErrorMsg("该客户群数据正在入库,请稍后再试！");
                this.pushCOCGroupResultSms(custSyncBO);
                log.info(xml.toString());
                return xml.toString();
            }
            String isManulPush = isManualPush(custSyncBO) ? "1" : "0";
            cocCustGroupSyncMapper.saveCustPushLog(customGroupId, customGroupDataDate, pushType, isManulPush, custInfoXml);
        } catch (Exception e) {
            log.error("xml格式错误，与预订接口字段不符:", e);
            String exceptionMessage = "xml格式错误，与预订接口字段不符，具体错误如下：" + e.getMessage();
            custSyncBO.setErrorMsg(exceptionMessage);
            flag = 2;
            xml.append(flag + "</flag><groupId>");
            xml.append(customGroupId + "</groupId><msg>");
            xml.append(exceptionMessage + "</msg></result>");
            this.notifyCustListError2Coc(customGroupId, "IMCD", exceptionMessage);
            this.pushCOCGroupResultSms(custSyncBO);
            return xml.toString();
        }
        xml.append(flag + "</flag><groupId>");
        xml.append(customGroupId + "</groupId><msg>");
        xml.append("推送客户群信息成功（不包含处理清单表入库文件,请等待）</msg></result>");
        log.info(xml.toString());
        return xml.toString();
    }




    /**
     * 客户群入库错误通知coc
     *
     * @param customGroupId 客户群ID
     * @param sysId         系统 ID
     * @param excepMsg      错误信息
     * @return
     */
    private String notifyCustListError2Coc(String customGroupId, String sysId, String excepMsg) {
        try {
            McdSysInterfaceDef url = callwsUrlService
                    .getCallwsURL("WSMCDINTERFACESERVER");

            Client client = new Client(new URL(url.getCallwsUrl()));

            Object[] resultObject = client.invoke("loadCustListError",
                    new String[]{customGroupId, sysId, excepMsg});
            log.info("调用COC错误接口返回信息：" + resultObject[0]);
        } catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }


    public void pushCOCGroupResultSms(CustSyncBO custSyncBO) {

        String pushSmsUserId = custSyncBO.getCreateUserId();
        String customGroupName = custSyncBO.getCustomGroupName();
        String customGroupDataDate = custSyncBO.getDataDate();
        String errorMessage = custSyncBO.getErrorMsg();
        try {
            // 是否推送短信 0: 不推送，1: 推送
            String isPushSms = AppConfigService.getProperty("COC_IS_PUSH_SMS_USER");
            // 不发送通知短信
            if (null == isPushSms || "0".equals(isPushSms)) {
                log.warn("因COC客户群推送完毕或失败是否发送短信开关未配置，故无法发送短信！");
                return;
            }

            if (StringUtils.isEmpty(pushSmsUserId)) {
                log.warn("客户群名称为：{},推送人ID为空，无法发送短信！", customGroupName);
                return;
            }
            LambdaQueryWrapper<McdSysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(McdSysUser::getUserId,pushSmsUserId).last(" limit 1");
            McdSysUser user = mcdSysUserService.getOne(wrapper);
            if (user == null || StringUtils.isEmpty(user.getMobilePhone())) {
                log.info("客户群名称为：{},根据推送人ID ：{},无法查询到用户或者查询到用户没有配置短话号码，无法发送短信！", customGroupName, pushSmsUserId);
                return;
            }
            // 提醒短信内容
            String message;
            String format;
            if (StringUtils.isNotEmpty(errorMessage)) {
                format = "客户群：%s,数据日期为 %s 的清单推送至 %s 失败。失败原因为：%s";
                message = String.format(format, customGroupName, customGroupDataDate, SMS_CONTENT_KEYWORDS, errorMessage);
            } else {
                format = "客户群：%s ,数据日期为 %s 的清单推送至 %s 完成。";
                message = String.format(format, customGroupName, customGroupDataDate, SMS_CONTENT_KEYWORDS);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("productNo", user.getMobilePhone());
            map.put("message", message);
            List<Map<String, Object>> list = new ArrayList<>(1);
            list.add(map);
            smsMessageService.sendSMSBatch(list);
        } catch (Exception e) {
            log.error("COC推送后，短信发送错误，请查看，错误原因：{}", e.getMessage(), e);
        }
    }

    /**
     * 是否手工推送
     *
     * @param custSyncBO 客户群推送BO
     * @return
     */
    public boolean isManualPush(CustSyncBO custSyncBO) {
        // 1. 一次客户群不会自动更新，只有手动推送
        if (custSyncBO.getUpdateCycle() == 1) {
            return true;
        }
        String table = "MCD_CUSTGROUP_PUSH_LOG";
        List<Map<String, Object>> tasks = cocCustGroupSyncMapper.selectCustPushLogById(custSyncBO.getCustomGroupId(), table);

        if (CollectionUtils.isEmpty(tasks)) {
            table = "MCD_CUSTGROUP_MANUAL_PUSH_LOG";
            tasks = cocCustGroupSyncMapper.selectCustPushLogById(custSyncBO.getCustomGroupId(), table);
        }
        // 2. 周期性客户群第一次
        if (CollectionUtils.isEmpty(tasks)) {
            return true;
        }
        // 3. 手工重复推送
      /*  Map<String, Object> task = tasks.get(0);
        String dataDate = task.get("DATA_DATE").toString();
        if (dataDate.equals(custSyncBO.getDataDate())) {
            return true;
        }*/
        return false;
    }
}
