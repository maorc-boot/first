package com.asiainfo.biapp.pec.element.jx.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.element.jx.model.McdDimSubChannel;
import com.asiainfo.biapp.pec.element.jx.service.McdDimSubChannelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 子渠道定义 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2022-12-20
 */
@RestController
@RequestMapping("/api/jx/subChannel")
@Api(value = "江西:子渠道（短信发送端口）", tags = {"江西:子渠道（短信发送端口）"})
@Slf4j
public class McdDimSubChannelController {

    @Resource
    private McdDimSubChannelService subChannelService;

    @ApiOperation("根据父渠道ID查询子渠道（短信发送端口）")
    @PostMapping(path = "/listSubChannel")
    public ActionResponse<McdDimSubChannel> listSubChannel(@RequestParam("parentId") String parentId) {
        log.info("根据父渠道ID查询子渠道（短信发送端口）,parentId = {}", parentId);
        try {
            QueryWrapper<McdDimSubChannel> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(McdDimSubChannel::getParentId, parentId);
            return ActionResponse.getSuccessResp(subChannelService.list(wrapper));
        } catch (Exception e) {
            log.error("根据父渠道ID查询子渠道（短信发送端口）异常, parentId = {} ", parentId,e);
            return ActionResponse.getFaildResp("根据父渠道ID查询子渠道（短信发送端口）异常");
        }
    }
}

