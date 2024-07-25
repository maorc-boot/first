package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.camp.model.CampPriorityOrderJx;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampChannelListJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampPriorityOrderModJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdDimChannelQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampChannelListJxService;
import com.asiainfo.biapp.pec.plan.vo.PriorityChannel;
import com.asiainfo.biapp.pec.plan.vo.req.CampPriorityOrderQuery;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "江西:策略优先级管理")
@Slf4j
@RestController
@RequestMapping("/api/action/camppriority/jx")
public class CampPriorityJxController {

    @Autowired
    private IMcdCampChannelListJxService campChannelListJxService;

    /**
     * 渠道信息
     *
     * @return
     */
    @ApiOperation(value = "江西:线上线下渠道信息", notes = "线上线下渠道信息")
    @PostMapping("/priorityChannelinfo")
    public List<PriorityChannel> channelInfo(@RequestBody McdDimChannelQuery req) {
        return campChannelListJxService.queryPriorityChannel(req.getChannelType());
    }


    /**
     * 江西调整活动优先级
     *
     * @param mod
     * @return
     */
    @ApiOperation(value = "江西:调整活动优先级", notes = "调整活动优先级新")
    @PostMapping("/updateCampPriorityOrder")
    public ActionResponse updateCampPriorityOrder(@RequestBody CampPriorityOrderModJx mod) {
        final LambdaUpdateWrapper<McdCampChannelListJx> update = Wrappers.lambdaUpdate();
        update.set(McdCampChannelListJx::getPriorityOrder, mod.getOrderNum())
                .eq(McdCampChannelListJx::getCampsegId, mod.getCampsegId());
        campChannelListJxService.update(update);
        return ActionResponse.getSuccessResp();
    }


    /**
     * 获取优先级管理列表
     */
    @ApiOperation(value = "获取优先级管理列表", notes = "获取优先级管理列表")
    @PostMapping("/querySysCampPriorityList")
    public IPage<CampPriorityOrderJx> querySysCampPriorityList(@RequestBody CampPriorityOrderQuery req) {
        return campChannelListJxService.pagePriorityOrderJx(req);

    }

}
