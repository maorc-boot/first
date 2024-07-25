package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.biapp.pec.core.utils.RestFullUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.CustomerManageDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.CustomerDetail;
import com.asiainfo.biapp.pec.plan.jx.camp.service.CustomerManageService;
import com.asiainfo.biapp.pec.plan.jx.camp.utils.SimpleCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 客群管理-江西
 *
 * @author admin
 * @version 1.0
 * @date 2023-03-31 下午 16:41:01
 */
@Service
@Slf4j
public class CustomerManageServiceImpl implements CustomerManageService {

	private static final String USER_ID = "admin";
	private static final String PWD = "Jxyd!791";

	@Value("${coc.token.url}")
	private String cocTokenUrl;
	@Value("${coc.syspush.updateUrl}")
	private String custPushCycleUpdateUrl;
	@Value("${coc.syspush.sysId}")
	private String custSysId;
	@Resource
	private CustomerManageDao customerManageDao;
	@Override
	public void dealCustUnused(String cocToken) {
		List<CustomerDetail> customerDetails = customerManageDao.selectCustomerDetail();
		log.info("一个月未使用的客群待调用COC，客群数量：{}",CollectionUtils.isNotEmpty(customerDetails)?customerDetails.size():0);
		List<String> successUpdateCustList = new ArrayList<>();
		for (CustomerDetail customerDetail : customerDetails) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("labelId",customerDetail.getCustomGroupId());
			jsonObject.put("sysId",custSysId);
			jsonObject.put("pushUserId",customerDetail.getCreateUserId());
			String result = HttpRequest.post(custPushCycleUpdateUrl)
					.header("format", "http+json")
					.header("token", cocToken)
					.header("X-Authorization", cocToken)
					.header(HttpHeaders.CONTENT_TYPE, "application/json")
					.body(JSONUtil.toJsonStr(jsonObject)).execute().body();
			log.info("调用COC更新客群状态入参input={}",jsonObject.toJSONString());
			JSONObject response = JSONObject.parseObject(result);
			log.info("调用COC更新客群状态,url:{}, 结果响应:{}",custPushCycleUpdateUrl,response);
			if("200".equals(response.get("status"))){
				successUpdateCustList.add(customerDetail.getCustomGroupId());
			}
		}
		//更新客群表状态
		if(CollectionUtils.isNotEmpty(successUpdateCustList)){
			log.info("调用COC接口成功，并更新客群是否更新状态开始，客群list={}",successUpdateCustList.toString());
			customerManageDao.updateCustStatus(successUpdateCustList);
		}

	}

	public String getCocToken(String userId, String pwd) {
		String token = (String) SimpleCache.getInstance().get(userId);
		if (StringUtils.isNotEmpty(token)) {
			log.info(userId + "从缓存获取token:" + token);
			return token;
		}
		try {
			String url = cocTokenUrl + "?username=" + userId + "&password=" + pwd;
			String sendPost = RestFullUtil.getInstance().sendPost2(url, null);
			log.info("getCocToken sendPost:{}", sendPost);
			JSONObject jsonObject = JSONObject.parseObject(sendPost);
			if ("200".equals(jsonObject.get("status"))) {
				JSONObject data = jsonObject.getJSONObject("data");
				token = data.getString("token");
				log.info(userId + "获取token:" + token);
				SimpleCache.getInstance().put(userId, token, 2 * 60 * 60);
				log.info(userId + "将token存入缓存");
			}
		} catch (Exception e) {
			log.error("getCocToken 获取token异常：" + e);
		}
		return token;
	}
}
