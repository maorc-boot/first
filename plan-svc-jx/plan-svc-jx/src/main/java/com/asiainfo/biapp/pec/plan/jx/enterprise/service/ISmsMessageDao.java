package com.asiainfo.biapp.pec.plan.jx.enterprise.service;

import java.util.List;
import java.util.Map;


/** 
* @ClassName: ISmsMessageDao 
* @Description: 短信消息发送Dao
* @CopyRight :  CopyRight (c) 2016
* @Company : 北京亚信智慧数据科技有限公司 
* @author: zhengyq3
* @date: 2016年11月30日 下午10:53:36  
*/
public interface ISmsMessageDao {
	/**
	 * 保存短信到短信表中
	 * @param smsMessageList
	 * @throws Exception
	 */
	public void saveSmsMessage(List<Map<String,Object>> smsMessageList) throws Exception;
}
