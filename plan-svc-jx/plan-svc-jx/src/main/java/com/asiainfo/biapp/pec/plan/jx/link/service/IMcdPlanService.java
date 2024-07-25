package com.asiainfo.biapp.pec.plan.jx.link.service;

import com.asiainfo.biapp.pec.iopws.util.Pager;

import java.util.List;
import java.util.Map;


/**
 * 产品库操作service
 *
 */
public interface IMcdPlanService {
	/**
	 * 初始化table列表数据
	 * 
	 * @param typeId
	 *            类别
	 * @param statusId
	 *            状态
	 * @param keyWords
	 *            搜索框关键字
	 * @param pager
	 *            pager对象
	 * @return
	 */
	List<Map<String, Object>> getPlanByCondition(String typeId, String statusId, String keyWords, Pager pager);

}
