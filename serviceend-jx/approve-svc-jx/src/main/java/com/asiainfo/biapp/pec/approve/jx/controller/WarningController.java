package com.asiainfo.biapp.pec.approve.jx.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.approve.jx.dto.WarningDetailReq;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTask;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTaskExt;
import com.asiainfo.biapp.pec.approve.jx.model.McdEmisReadTask;
import com.asiainfo.biapp.pec.approve.jx.service.IWarningService;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 预警代办
 *
 * @author mamp
 * @date 2022/12/6
 */
@RestController
@RequestMapping("/api/warn")
@Api(value = "江西:预警待办", tags = {"江西:预警待办"})
@Slf4j
public class WarningController {

    @Resource
    private IWarningService warningService;

    @PostMapping(path = "/queryWarnList")
    @ApiOperation(value = "查询预警待办列表", notes = "查询预警待办列表")
    public ActionResponse<IPage<McdCampWarnEmisTask>> queryWarnList(@RequestBody WarningDetailReq req, HttpServletRequest request) {
        log.info("queryWarnList param: {}", req);

        req.setCreateUser(UserUtil.getUserId(request));
        return ActionResponse.getSuccessResp(warningService.queryWarningList(req));
    }

    @PostMapping(path = "/dealWarn")
    @ApiOperation(value = "处理预警待办", notes = "处理预警待办")
    public ActionResponse dealWarn(@RequestBody McdCampWarnEmisTask task) {
        log.info("queryWarnList param: {}", task);
        if (null == task || StrUtil.isEmpty(task.getUniqueIdentifierId())) {
            ActionResponse.getFaildResp("预警唯一标识不能为空");
        }
        return ActionResponse.getSuccessResp(warningService.dealWarning(task));
    }

    @PostMapping(path = "/queryWarnDetailList")
    @ApiOperation(value = "查询预警详细列表", notes = "查询预警详细列表")
    public ActionResponse<IPage<McdCampWarnEmisTaskExt>> queryWarnDetailList(@RequestBody WarningDetailReq req) {
        log.info("queryWarnDetailList param: {}", req);
        return ActionResponse.getSuccessResp(warningService.queryWarnDetailList(req));
    }

    @PostMapping(path = "/queryWarnSumList")
    @ApiOperation(value = "查询预警汇总列表", notes = "查询预警汇总列表")
    public ActionResponse<IPage<McdCampWarnEmisTaskExt>> queryWarnSumList(@RequestBody WarningDetailReq req) {
        log.info("queryWarnSumList param: {}", req);
        return ActionResponse.getSuccessResp(warningService.queryWarnSumList(req));
    }

    @PostMapping(path = "/queryRejectList")
    @ApiOperation(value = "查询自定义预警&客户通渠道活动驳回列表", notes = "查询自定义预警&客户通渠道活动驳回列表")
    public ActionResponse<IPage<McdEmisReadTask>> queryRejectList(@RequestBody McdPageQuery req) {
        log.info("查询自定义预警&客户通渠道活动驳回列表param: {}", JSONUtil.toJsonStr(req));
        return ActionResponse.getSuccessResp(warningService.queryRejectList(req));
    }

    @PostMapping(path = "/updateRejectEmisReadTask")
    @ApiOperation(value = "更新自定义预警&客户通渠道活动驳回Emis阅知待办状态", notes = "更新自定义预警&客户通渠道活动驳回Emis阅知待办状态")
    public ActionResponse updateRejectEmisReadTask(@RequestBody McdEmisReadTask req) {
        log.info("更新自定义预警&客户通渠道活动驳回Emis阅知待办状param: {}", JSONUtil.toJsonStr(req));
        boolean updateRejectEmisReadTask = warningService.updateRejectEmisReadTask(req);
        if (updateRejectEmisReadTask) {
            return ActionResponse.getSuccessResp("修改成功");
        } else {
            return ActionResponse.getFaildResp("修改失败");
        }
    }
}
