package com.asiainfo.biapp.pec.plan.jx.webservice.impl;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.webservice.ChnPreFeignClient;
import com.asiainfo.biapp.pec.plan.jx.webservice.ICustgroupAPIServiceJx;
import com.asiainfo.biapp.pec.plan.service.IReceivingCustService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * 江西在生成客户群定义相关数据后，调用preview-svc接口生成客户群渠道偏好数据
 *
 * @author mamp
 * @date 2022/6/28
 */
@Slf4j
@Component
@WebService(endpointInterface = "com.asiainfo.biapp.pec.plan.jx.webservice.ICustgroupAPIServiceJx", targetNamespace = "http://webservice.jx.plan.pec.biapp.asiainfo.com/")
public class ReceivingCustServiceJxImpl implements ICustgroupAPIServiceJx {


    @Autowired
    private ChnPreFeignClient chnPreFeignClient;
    @Autowired
    private IReceivingCustService receivingCustService;


    @Override
    public String sendCustInfoJx(String custInfoXml) {
        log.info("Coc同步客户群请求报文:{}", custInfoXml);
        String result = receivingCustService.saveCocCustInfo(custInfoXml);
        Document dom = null;
        try {
            dom = DocumentHelper.parseText(custInfoXml);
            Element root = dom.getRootElement();
            Element data = root.element("data");
            String customGroupId = data.element("customGroupId") == null ? null : data.element("customGroupId").getText();
            String customGroupDataDate = data.element("dataDate") == null ? null : data.element("dataDate").getText();
            String uploadFileName = data.element("uploadFileName") == null ? "" : data.element("uploadFileName").getText();

            log.info("customGroupId = {}, customGroupDataDate = {}, uploadFileName = {}", customGroupId, customGroupDataDate, uploadFileName);
            ActionResponse response = chnPreFeignClient.custChnPreCal(customGroupId, customGroupDataDate, uploadFileName);
            if (response.getStatus().getCode() != 200) {
                log.error("计算渠道偏好失败,customGroupId:{},customGroupDataDate:{},uploadFileName:{}", customGroupId, customGroupDataDate, uploadFileName);
            }
        } catch (DocumentException e) {
            log.error("COC 客户群同步失败", e);
        }
        return result;
    }
}
