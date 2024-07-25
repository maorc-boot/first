package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;

import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCustTitleList;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontBlacklistCust;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdCustomTitleQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdCustomTotlePhone;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdCustomizeTitleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author : yantao
 * @version : v1.0
 * @className : CustomizeTitleController
 * @description : [个性化称谓相关]
 * @createTime : [2024-06-24 上午 11:17:16]
 */
@Slf4j
@Api(value = "客户通-个性化称谓相关",tags = "客户通-个性化称谓相关")
@RestController
@RequestMapping("/jx/customizetitle")
public class CustomizeTitleController {

	@Resource
	private McdCustomizeTitleService customizeTitleService;

	/**
	 * 获取个性化称谓列表
	 * @param request
	 * @param query
	 * @return
	 */
	@ApiOperation(value = "江西客户通个性化称谓查询接口")
	@PostMapping(value = "/getCustomizeTitleList")
	@DataSource("khtmanageusedb")
	public ActionResponse<Page<McdFrontBlacklistCust>> getCustomizeTitleList(HttpServletRequest request, @RequestBody McdCustomTitleQuery query) {
		UserSimpleInfo user = UserUtil.getUser(request);
		Page<McdFrontBlacklistCust> page = new Page<>();
		page.setCurrent(query.getCurrent());
		page.setSize(query.getSize());

		Page<McdCustTitleList> mcdFrontBlacklistCustPage = customizeTitleService.getCustomizeTitleList(query);
		return ActionResponse.getSuccessResp(mcdFrontBlacklistCustPage);
	}


	@PostMapping("/batchImportCustomizeTitleData")
	@ApiOperation(value = "客户通个性化称谓批量导入", notes = "客户通个性化称谓批量导入")
	@DataSource("khtmanageusedb")
	public ActionResponse batchImportAutoClearAlarm(MultipartFile multipartFile) {
		UserSimpleInfo user = UserUtilJx.getUser();
		try {
			log.info("个性化称谓批量导入文件信息,文件名:{}, 文件大小:{}",multipartFile.getOriginalFilename(),multipartFile.getSize());
			customizeTitleService.batchImportCustomizeTitleData(multipartFile, user);
		} catch (Exception e) {
			log.error("导入个性化称谓数据文件异常", e);
			return ActionResponse.getFaildResp("导入个性化称谓数据文件异常");
		}
		return ActionResponse.getSuccessResp("导入个性化称谓数据文件成功");
	}

	@ApiOperation(value = "客户通个性化称谓删除")
	@PostMapping(value = "/deleteCustTitleById")
	@DataSource("khtmanageusedb")
	public ActionResponse  deleteBlackList(@RequestBody McdCustomTotlePhone req){

		if (StringUtils.isEmpty(req.getPhoneNum())){
			log.info("江西客户通根据手机号码删除黑名单入参为空!");
			return ActionResponse.getFaildResp("手机号码为空!");
		}
		customizeTitleService.delCustomTitleById(req.getPhoneNum());
		return ActionResponse.getSuccessResp();

	}





}
