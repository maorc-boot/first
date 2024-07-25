/**
 * 
 */
package com.asiainfo.biapp.pec.plan.jx.enterprise.service.impl;


import com.asiainfo.biapp.pec.plan.jx.enterprise.service.ISmsMessageDao;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.ISmsMessageService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/** 
* @ClassName: SmsMessageService 
* @Description: 江西短信消息发送接口实现
* @CopyRight :  CopyRight (c) 2016
* @Company : 北京亚信智慧数据科技有限公司 
* @author: zhengyq3
* @date: 2016年11月30日 下午10:33:40  
*/
@Service("smsMessageService")
public class SmsMessageServiceJXImpl implements ISmsMessageService {
	protected final Log logger = LogFactory.getLog(getClass());
	@Resource(name="smsMessageDao")
	private ISmsMessageDao smsMessageDao;
	@Override
	public void sendSMSBatch(List<Map<String, Object>> msgList) throws Exception {
		try {
			if(CollectionUtils.isNotEmpty(msgList)){
				smsMessageDao.saveSmsMessage(msgList);
				logger.info("已发短信...");
			}else{
				logger.info("待发提醒短信为空");
			}
		} catch (Exception e) {
			logger.error("发送短信失败", e);
		}
	}

}
