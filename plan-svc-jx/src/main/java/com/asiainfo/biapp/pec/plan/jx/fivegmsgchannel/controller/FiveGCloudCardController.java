package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.dto.QryTmpListReqDto;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service.IFiveGCloudCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 5G云卡渠道控制层
 *
 * @author: lvchaochao
 * @date: 2023/9/12
 */
@RestController
@RequestMapping("/action/jx/fivegcloudcard")
@Api(value = "5G云卡渠道", tags = {"5G云卡渠道"})
@Slf4j
public class FiveGCloudCardController {

    @Autowired
    private IFiveGCloudCardService fiveGCloudCardService;

    @ApiOperation(value = "获取5G云卡渠道模板列表", notes = "获取5G云卡渠道模板列表")
    @PostMapping("/getTemplateList")
    public ActionResponse getTemplateList(@RequestBody QryTmpListReqDto reqDto) {
        log.info("获取5G云卡渠道模板列表入参：{}", JSONUtil.toJsonStr(reqDto));
        String templateList =  fiveGCloudCardService.getTemplateList(reqDto);
        if (StrUtil.isNotEmpty(templateList)) return ActionResponse.getSuccessResp(templateList);
        else return ActionResponse.getFaildResp("获取模板列表失败");
    }
}
