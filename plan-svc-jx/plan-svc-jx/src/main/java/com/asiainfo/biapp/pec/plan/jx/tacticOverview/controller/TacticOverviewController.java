package com.asiainfo.biapp.pec.plan.jx.tacticOverview.controller;

import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.TacticOverviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.tacticOverview.controller
 * @className: TacticOverviewController
 * @author: chenlin
 * @description: 策略总览（全局业务概览）
 * @date: 2023/6/8 11:42
 * @version: 1.0
 */
@RestController
@RequestMapping("/tacticOverview")
@Slf4j
@Validated
@Api(tags = "全局业务概览")
public class TacticOverviewController {
    @Autowired
    private TacticOverviewService tacticService;

    @ApiOperation("根据策略状态campsegStatId查询相关策略")
    @ApiImplicitParam(name = "campsegStatId",
            value = "策略的状态：40（审批中），41（审批驳回），50（待执行），54（执行中），59（暂停），91（终止）",
            required = true, example = "40"
    )
    @GetMapping
    public ActionResponse tacticOverview(
            @Pattern(regexp = "(40|41|50|54|59|91)", message = "状态编号只能为：40，41，50，54，59，91中的一个！")
            @NotNull(message = "策略状态：campsegStatId不能为空！")
            String campsegStatId
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        if (user.getCityId() == null) user.setCityId("999");
        log.info("userId为：{}的用户正在查询全局业务概览！", user.getUserId());
        return ActionResponse.getSuccessResp(tacticService.selectAllTactics(campsegStatId, user.getCityId()));
    }
}
