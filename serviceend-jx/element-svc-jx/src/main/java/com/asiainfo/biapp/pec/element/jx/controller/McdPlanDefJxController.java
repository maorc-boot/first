package com.asiainfo.biapp.pec.element.jx.controller;

import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.element.jx.model.McdPlanDef;
import com.asiainfo.biapp.pec.element.jx.query.PlanIdQuery;
import com.asiainfo.biapp.pec.element.jx.query.PlanManageJxQuery;
import com.asiainfo.biapp.pec.element.jx.service.McdPlanDefService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mamp
 * @date 2022/10/18
 */
@RestController
@RequestMapping("/api/jx/mcdPlanDef")
@Api(value = "江西:产品管理服务", tags = {"江西:产品管理服务"})
@Slf4j
public class McdPlanDefJxController {

    @Autowired
    private McdPlanDefService mcdPlanDefService;

    @PostMapping(path = "/queryPlanDefPageList")
    @ApiOperation(value = "产品分页列表查询", notes = "产品分页列表查询")
    public ActionResponse<IPage<McdPlanDef>> queryPlanDefPageList(@RequestBody PlanManageJxQuery planQuery) {
        log.info("start queryPlanDefPageList para:{}", new JSONObject(planQuery));
        return ActionResponse.getSuccessResp(mcdPlanDefService.queryPlanDefPageList(planQuery));
    }

    @PostMapping(path = "/getPlanDetailById")
    @ApiOperation(value = "根据产品ID查询产品ID", notes = "根据产品ID查询产品详情,planId-产品ID")
    public ActionResponse<McdPlanDef> getPlanDetailById(@RequestBody PlanIdQuery planIdQuery) {
        log.info("start getPlanDetailById para:{}", new JSONObject(planIdQuery));
        return ActionResponse.getSuccessResp(mcdPlanDefService.getPlanDetailById(planIdQuery.getPlanId()));
    }

}
