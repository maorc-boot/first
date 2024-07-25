/**
 * 
 */
package com.asiainfo.biapp.pec.plan.jx.enterprise.service;

import java.util.List;
import java.util.Map;


/** 
* @ClassName: ISmsMessageService 
* @Description: 消息发送service
* @CopyRight :  CopyRight (c) 2016
* @Company : 北京亚信智慧数据科技有限公司 
* @author: zhengyq3
* @date: 2016年11月30日 下午10:27:07  
*/
public interface ISmsMessageService {
	/**
	 * 短信发送接口
	 * @param msgList
	 * @throws Exception
	 */
	public void sendSMSBatch(List<Map<String,Object>> msgList) throws Exception;
	
	

}
