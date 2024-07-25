package com.asiainfo.biapp.pec.plan.jx.camp.service;

/**
 * 江西客群管理相关
 *
 * @author admin
 * @version 1.0
 * @date 2023-03-31 下午 15:55:26
 */
public interface CustomerManageService {

	//未使用
	void dealCustUnused(String cocToken);

	String getCocToken(String userId, String pwd);

}
