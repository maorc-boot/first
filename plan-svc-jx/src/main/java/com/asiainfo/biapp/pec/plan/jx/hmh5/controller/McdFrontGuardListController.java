package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontLineChannelModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontGuardUserQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontLineChannelQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontGuardListService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontLineChannelService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontGuardUserInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 看护关系
 * @author ranpf  2023-02-15
 *
 */
@Slf4j
@RestController
@RequestMapping("/mcd/front/guardList")
@Api(tags = "客户通-看护关系管理")
@DataSource("khtmanageusedb")
public class McdFrontGuardListController   {


	@Resource
	private McdFrontGuardListService mcdFrontGuardListService;

	@Resource
	private McdFrontLineChannelService mcdFrontLineChannelService;


	/**
	 * 看护客户列表查询
	 *
	 * @return
	 */
	@PostMapping("/queryGuardUserList")
	@ApiOperation("客户通看护关系查询")
	public ActionResponse<IPage<McdFrontGuardUserInfo>> queryGuardUserList(@RequestBody McdFrontGuardUserQuery req) {

		return ActionResponse.getSuccessResp(mcdFrontGuardListService.queryGuardUserList(req));
	}

	@PostMapping("/exportGuardUserList")
	@ApiOperation("客户通看护关系导出")
	public void exportGuardUserList(@RequestBody McdFrontGuardUserQuery req ,HttpServletResponse response)  {

		try {

			//判断主要过滤条件是否全部为空
			if(StringUtils.isBlank(req.getProductNo()) && StringUtils.isBlank(req.getManagerId()) && StringUtils.isBlank(req.getChannelId())
				){
				String msg = "<h3>必须选择<span style='color:red'>“手机号码、看护工号、看护渠道”</span> 其中一个条件进行导出</h3>";
				response.setHeader("Content-Type","text/html;charset=utf-8");
				response.getWriter().println(msg);
				return;
			}
			mcdFrontGuardListService.exportGuardUserExcel(req, response);
		} catch (Exception e) {
			log.error("客户通导出看护数据异常", e);
		}

	}


	/**
	 * 根据地市、区县获取一线渠道列表
	 * @param request
	 * @return
	 */
	@ApiOperation("根据地市、区县获取一线渠道列表")
	@PostMapping("/queryChannelByCityAndCounty")
	public ActionResponse<List<McdFrontLineChannelModel>> queryChannelByCityAndCounty(@RequestBody McdFrontLineChannelQuery req,HttpServletRequest request )  {
		List<McdFrontLineChannelModel> channelList = new ArrayList<>();
		try{
			UserSimpleInfo user = UserUtil.getUser(request);
			String cityId = user.getCityId();
			String countyId = req.getCountyCode();
            cityId = StringUtils.isEmpty(req.getCityCode())?cityId:req.getCityCode();

			log.info("queryChannelByCityAndCounty cityId={},countyId={}",cityId,countyId);
			LambdaQueryWrapper<McdFrontLineChannelModel>  lineChannelWrapper = new LambdaQueryWrapper<>();
			lineChannelWrapper.eq(McdFrontLineChannelModel::getCityId,cityId)
					          .eq(StringUtils.isNotEmpty(countyId),McdFrontLineChannelModel::getCountyId,countyId);
			channelList = mcdFrontLineChannelService.list(lineChannelWrapper);
		}catch(Exception e){
			log.error("查询一线渠道列表失败！",e);
		}
		return ActionResponse.getSuccessResp(channelList);
	}

}
