package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.camp.req.*;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdMarketingHistoryService;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdChanPlanSuccessMarketingHistoryInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCustContactMarketingHistoryInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCustSuccessMarketingHistoryInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdPlanMarketingHistoryInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "江西:营销历史")
@Slf4j
@RestController
@RequestMapping("/mcd/marketing/history")
public class McdMarketingHistoryController {

    @Autowired
    private McdMarketingHistoryService mcdMarketingHistoryService;

    /**
     * 选产品营销历史查询
     *
     * @return
     */
    @ApiOperation(value = "江西:选产品营销历史查询", notes = "选产品营销历史查询")
    @PostMapping("/planMarketingHistory")
    public ActionResponse<List<McdPlanMarketingHistoryInfo>> queryPlanMarketingHistory(@RequestBody McdPlanMarketingHistoryQuery req) {

        return ActionResponse.getSuccessResp(mcdMarketingHistoryService.queryPlanMarketingHistory(req));
    }


    /**
     * 选渠道产品营销历史查询接口
     */
    @ApiOperation(value = "江西:选渠道产品营销历史查询接口", notes = "选渠道产品营销历史查询接口")
    @PostMapping("/chanAndPlanMarketingHistory")
    public ActionResponse<List<McdChanPlanSuccessMarketingHistoryInfo>> queryChanAndPlanMarketingHistory(@RequestBody McdChanAanPlanMarketingHistoryQuery req) {
        return ActionResponse.getSuccessResp(mcdMarketingHistoryService.queryChanAndPlanMarketingHistory(req));

    }

}
