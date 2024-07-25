package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ChannelKhtService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户通和网格通渠道页面查询相关
 *
 * @author admin
 * @version 1.0
 * @date 2023-01-03 下午 15:03:19
 */
@Slf4j
@RestController
@RequestMapping("/kht/grid")
@Api(tags = "江西:客户通，网格通渠道查询相关")
public class KhtAndGridController {

	@Autowired
	private ChannelKhtService channelKhtService;


	@PostMapping("/getAlarmType")
	public ActionResponse getAlarmType(@RequestParam("pcode") String pcode) {
		try {
			log.info("根据父类型编码查询任务类型，pcode:{}", pcode);
			return ActionResponse.getSuccessResp(channelKhtService.selectChannelTaskTypeDetail(pcode));
		} catch (Exception e) {
			log.error("根据父类型编码查询任务类型，pcode:{}, exception:{}", pcode, e);
			return ActionResponse.getFaildResp();
		}
	}

}
