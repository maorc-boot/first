package com.asiainfo.biapp.pec.element.jx.custLabel.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.element.jx.custLabel.req.ChannelLabelReq;
import com.asiainfo.biapp.pec.element.jx.custLabel.req.CustLabelModel;
import com.asiainfo.biapp.pec.element.jx.custLabel.req.LabelModel;
import com.asiainfo.biapp.pec.element.jx.custLabel.service.McdCustChannelLabelService;
import com.asiainfo.biapp.pec.element.jx.custLabel.vo.ChannelLabelResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/custLabel")
@Api(value = "江西:客群标签推送配置相关", tags = {"江西:客群标签同步相关"})
@Slf4j
public class McdCustChannelLabelController {


	@Resource
	private McdCustChannelLabelService mcdCustChannelLabelService;


	/**
	 * 江西客群标签推送配置-新增推送标签配置
	 *
	 * @param request
	 * @param channelLabel
	 * @return
	 */
	@PostMapping(path = "/saveChannelLabel")
	@ApiOperation(value = "江西:新增推送标签配置", notes = "江西:新增推送标签配置")
	public ActionResponse saveChannelLabel(HttpServletRequest request, @RequestBody LabelModel channelLabel) {
		log.info("start saveChannelLabel param:{}", channelLabel.toString());
		String userId = UserUtil.getUserId(request);
		channelLabel.setCreateUserId(userId);
		ActionResponse resp = ActionResponse.getSuccessResp();
		try {
			mcdCustChannelLabelService.saveChannelLabel(channelLabel);
			resp = ActionResponse.getSuccessResp("新增推送标签配置成功");
		} catch (Exception e) {
			resp = ActionResponse.getFaildResp("新增推送标签配置失败");
			log.error("新增推送标签配置失败:", e);
		}
		return resp;
	}

	/**
	 * 江西客群标签推送配置-查询推送标签配置
	 *
	 * @param channelLabelReq 入参对象
	 * @return ActionResponse
	 */
	@PostMapping(path = "/getChannelLabelInfos")
	@ApiOperation(value = "江西:查询推送标签配置", notes = "江西:查询推送标签配置")
	public ActionResponse getChannelLabelInfos(@RequestBody ChannelLabelReq channelLabelReq) {
		log.info("start getChannelLabelInfos param:{}", channelLabelReq.toString());
		ActionResponse resp = ActionResponse.getSuccessResp();
		try {
			resp = ActionResponse.getSuccessResp(mcdCustChannelLabelService.getLabelInfos(channelLabelReq));
		} catch (Exception e) {
			resp = ActionResponse.getFaildResp("查询推送标签配置失败");
			log.error("查询标签配置失败:", e);
		}
		return resp;
	}

	/**
	 * 江西客群标签推送配置-查询新增标签时所有可选配置标签
	 *
	 * @param channelId 入参对象
	 * @return ActionResponse
	 */
	@PostMapping(path = "/getLabelConfig/{channelId}")
	@ApiOperation(value = "江西:查询该渠道所有可选标签", notes = "江西:查询该渠道所有可选标签")
	public ActionResponse getLabelConfig(@PathVariable String channelId) {
		log.info("start getLabelConfig param:{}", channelId);
		ActionResponse resp = ActionResponse.getSuccessResp();
		try {
			resp = ActionResponse.getSuccessResp(mcdCustChannelLabelService.getLabelConfig(channelId));
		} catch (Exception e) {
			resp = ActionResponse.getFaildResp("查询该渠道所有可选标签失败");
			log.error("查询该渠道所有可选标签失败:", e);
		}
		return resp;
	}

	/**
	 * 江西客群标签推送配置-新增推送标签配置
	 *
	 * @param request
	 * @param channelLabel
	 * @return
	 */
	@PostMapping(path = "/updateChannelLabel")
	@ApiOperation(value = "江西:更新推送标签配置", notes = "江西:更新推送标签配置")
	public ActionResponse updateChannelLabel(HttpServletRequest request, @RequestBody LabelModel channelLabel) {
		log.info("start updateChannelLabel param:{}", channelLabel.toString());
		String userId = UserUtil.getUserId(request);
		channelLabel.setCreateUserId(userId);
		ActionResponse resp = ActionResponse.getSuccessResp();
		try {
			mcdCustChannelLabelService.updateChannelLabel(channelLabel);
			resp = ActionResponse.getSuccessResp("新增推送标签配置成功");
		} catch (Exception e) {
			resp = ActionResponse.getFaildResp("新增推送标签配置失败");
			log.error("新增推送标签配置失败:", e);
		}
		return resp;
	}

	/**
	 * 江西客群标签推送配置-删除推送标签配置
	 *
	 * @param channelId
	 * @return
	 */
	@PostMapping(path = "/deleteChannelLabel/{channelId}")
	@ApiOperation(value = "江西:删除推送标签配置", notes = "江西:删除推送标签配置")
	public ActionResponse deleteChannelLabel(HttpServletRequest request, @PathVariable String channelId) {
		String userId = UserUtil.getUserId(request);
		log.info("start deleteChannelLabel channelId:{},删除操作人ID:{}", channelId, userId);
		ActionResponse resp = ActionResponse.getSuccessResp();
		try {
			mcdCustChannelLabelService.deleteChannelLabel(channelId);
			resp = ActionResponse.getSuccessResp("删除推送标签配置成功");
		} catch (Exception e) {
			resp = ActionResponse.getFaildResp("删除推送标签配置失败");
			log.error("删除推送标签配置失败:", e);
		}
		return resp;
	}

	/**
	 * 江西:活动创建选择标签推送触点-查询该渠道有哪些标签，及哪些标签可选，
	 *
	 * @param custLabel
	 * @return
	 */
	@PostMapping(path = "/getLabelByChannelId")
	@ApiOperation(value = "江西:活动创建选择标签推送触点-查询该渠道有哪些标签，及哪些标签可选,", notes = "江西:查询该渠道有哪些标签，及哪些标签可选")
	public ActionResponse getLabelByChannelId(HttpServletRequest request,@RequestBody CustLabelModel custLabel) {
		log.info("start getLabelByChannelId inputParam:{}", custLabel.toString());
		ActionResponse resp = ActionResponse.getSuccessResp();
		try {
			ChannelLabelResult result = mcdCustChannelLabelService.getCanSelectlabelByChannelId(custLabel);
			resp = ActionResponse.getSuccessResp(result);
		} catch (Exception e) {
			resp = ActionResponse.getFaildResp("查询该渠道有哪些标签，及哪些标签可选成功失败");
			log.error("查询该渠道有哪些标签，及哪些标签可选成功失败:", e);
		}
		return resp;
	}
}
