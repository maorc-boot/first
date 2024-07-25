package com.asiainfo.biapp.pec.plan.jx.camp.controller;


import com.asiainfo.biapp.pec.plan.jx.camp.service.CustomerManageService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "江西:客群更新相关")
@RequestMapping("/mcd/jx/cust/")
@Slf4j
@RestController
public class McdCustomerController {
	private static final String USER_ID = "admin";
	private static final String PWD = "Jxyd!791";
	@Resource
	private CustomerManageService customerManageService;

	/**
	 * 取消一个月未使用客群不再更新，调用接口通知COC
	 */
	@PostMapping("/cancelUpdate")
	public void cancelCustCysleUpdate() {
		log.info("扫描一个月未使用的客群任务cancelCustCysleUpdate开始!");
		//调用COC获取token
		String cocToken = customerManageService.getCocToken(USER_ID, PWD);
		//处理一个月没有使用的客群
		customerManageService.dealCustUnused(cocToken);
		log.info("扫描一个月未使用的客群任务处理结束!");
	}

}
