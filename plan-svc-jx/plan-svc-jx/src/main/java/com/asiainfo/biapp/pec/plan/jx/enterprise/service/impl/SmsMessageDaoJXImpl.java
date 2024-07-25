/**
 * 
 */
package com.asiainfo.biapp.pec.plan.jx.enterprise.service.impl;

import com.asiainfo.biapp.mcd.sms.SmsCodes;
import com.asiainfo.biapp.mcd.sms.model.Message;
import com.asiainfo.biapp.mcd.sms.model.Result;
import com.asiainfo.biapp.mcd.sms.service.ISendService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.ISmsMessageDao;
import com.asiainfo.biapp.pec.plan.jx.enterprise.util.SpringContextsUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("smsMessageDao")
public class SmsMessageDaoJXImpl implements ISmsMessageDao {
	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	public void saveSmsMessage(final List<Map<String, Object>> smsMessageList) throws Exception {
		try {
			String otherInfo = "";
			for (int i = 0; i < smsMessageList.size(); i++) {
				Message mes = new Message();
				mes.setProductNo((String) smsMessageList.get(i).get("productNo"));
				mes.setContent((String) smsMessageList.get(i).get("message"));
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> otherInfoMap = new HashMap<String, Object>();
				otherInfoMap.put("CAMPSEG_ID", ""); // 策略id
				otherInfoMap.put("CHANNEL_ID", "");// 渠道id
				otherInfoMap.put("CAMPSEG_TYPE_ID", "");// 策略类型ID 备用
				otherInfoMap.put("PROTOCOL_TYPE", "smpp3");
				otherInfoMap.put("SP_CODE", "10086");// 短信协议 备用
				otherInfoMap.put("PLAN_ID", "");// 产品ID 备用
				otherInfoMap.put("CITY_ID", "");// 地市ID 备用
				otherInfo = mapper.writeValueAsString(otherInfoMap);

				log.info("开始向：" + mes.getProductNo() + "发送短信message ：" + mes.getContent());

				ISendService sendService = SpringContextsUtil.getBean("sendServiceImpl", ISendService.class);
				Result result = sendService.sendMessage(mes, otherInfo);
				if (SmsCodes.SEND_STATUS_SUCCESS == result.getResult()) {
					log.info("向" + mes.getProductNo() + "发送短信成功!");
				} else {
					log.info("向" + mes.getProductNo() + "发送短信失败，返回Result对象信息：" + result.toString());
				}
			}
		} catch (Exception e) {
			log.error("抛出异常信息：---------" + e);
		}
	
	}

}
