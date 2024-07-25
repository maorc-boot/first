package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service.IFiveGMsgChannelService;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.ApplicationNumQuery;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.QueryTmpListRequestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 5G消息渠道
 *
 * @author: lvchaochao
 * @date: 2022/12/22
 */
@RestController
@RequestMapping("/action/jx/fivegmsg")
@Api(value = "5G消息渠道", tags = {"5G消息渠道"})
@Slf4j
@RefreshScope
public class FiveGMsgChannelController {

    @Autowired
    private IFiveGMsgChannelService fiveGMsgChannelService;

    @ApiOperation(value = "根据渠道id查询应用号配置信息", notes = "根据渠道id查询应用号配置信息")
    @PostMapping("/getApplicationNumInfo")
    public ActionResponse getApplicationNumInfo(@RequestBody ApplicationNumQuery query) {
        log.info("根据渠道id查询应用号配置信息入参：{}", JSONUtil.toJsonStr(query));
        ActionResponse successResp = ActionResponse.getSuccessResp();
        return successResp.setData(fiveGMsgChannelService.queryApplicationNumInfo(query.getChannelId()));
    }

    @ApiOperation(value = "获取5G消息渠道模板列表", notes = "获取5G消息渠道模板列表")
    @PostMapping("/getTemplateList")
    public ActionResponse getTemplateList(@RequestBody QueryTmpListRequestVo requestVo) {
        log.info("获取5G消息渠道模板列表入参：{}", JSONUtil.toJsonStr(requestVo));
        String templateList =  fiveGMsgChannelService.getTemplateList(requestVo);
        ActionResponse successResp = ActionResponse.getSuccessResp();
        return successResp.setData(templateList);
    }
}
