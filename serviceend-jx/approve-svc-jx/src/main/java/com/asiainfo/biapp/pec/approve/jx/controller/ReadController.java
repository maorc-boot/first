package com.asiainfo.biapp.pec.approve.jx.controller;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.approve.jx.dto.ReadInfoReq;
import com.asiainfo.biapp.pec.approve.jx.model.McdEmisReadTask;
import com.asiainfo.biapp.pec.approve.jx.service.IReadService;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 阅知待办
 *
 * @author mamp
 * @date 2022/12/6
 */
@RestController
@RequestMapping("/api/read")
@Api(value = "江西:阅知待办", tags = {"江西:阅知待办"})
@Slf4j
public class ReadController {

    @Resource
    private IReadService readService;

    @PostMapping(path = "/queryRead")
    @ApiOperation(value = "查询阅知待办列表", notes = "查询阅知待办列表")
    public ActionResponse<IPage<McdEmisReadTask>> queryRead(@RequestBody ReadInfoReq req) {
        log.info("queryRead param: {} ", req);
        return ActionResponse.getSuccessResp(readService.queryReadList(req));
    }

    @PostMapping(path = "/dealRead")
    @ApiOperation(value = "处理阅知待办", notes = "处理阅知待办")
    public ActionResponse dealRead(@RequestBody McdEmisReadTask task) {
        log.info("dealRead param: {}", task);
        if (null == task || StrUtil.isEmpty(task.getId())) {
            return ActionResponse.getFaildResp("阅知ID不能为空");
        }
        Boolean dealRead = readService.dealRead(task);
        if (dealRead) {
            return ActionResponse.getSuccessResp(dealRead);
        } else {
            return ActionResponse.getFaildResp("处理阅知待办失败");
        }
    }
}
