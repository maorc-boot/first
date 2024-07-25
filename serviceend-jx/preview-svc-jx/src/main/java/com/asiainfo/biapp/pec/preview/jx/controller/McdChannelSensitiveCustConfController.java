package com.asiainfo.biapp.pec.preview.jx.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustConf;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustInfo;
import com.asiainfo.biapp.pec.preview.jx.query.SensitiveCustDeleReq;
import com.asiainfo.biapp.pec.preview.jx.query.SensitiveCustQuery;
import com.asiainfo.biapp.pec.preview.jx.service.McdChannelSensitiveCustConfService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 渠道敏感客户群配置表 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2022-10-12
 */
@RestController
@RequestMapping("/mcd-channel-sensitive-cust-conf")
@Slf4j
@Api(value = "渠道敏感客户群配置Api", tags = {"江西:渠道敏感客户群配置Api"})
public class McdChannelSensitiveCustConfController {

    @Autowired
    private McdChannelSensitiveCustConfService sensitiveCustConfService;


    @PostMapping("/saveSensitiveCust")
    @ApiOperation(value = "保存渠道敏感客户群配置 ", notes = "保存渠道敏感客户群配置")
    public ActionResponse saveChannelSensitiveCustConf(@RequestBody List<McdChannelSensitiveCustConf> confList) {
        log.info("保存敏感客户群配置入参:{}", confList);
        ActionResponse resp;
        try {
            resp = ActionResponse.getSuccessResp("保存敏感客户群配置成功");
            resp.setData(sensitiveCustConfService.saveChannelSensitiveCustConf(confList));
        } catch (Exception e) {
            resp = ActionResponse.getFaildResp("保存敏感客户群配置失败");
            log.error("保存敏感客户群配置失败:", e);
        }
        return resp;
    }

    /**
     * 查询渠道敏感客户群
     *
     * @param query 查询参数
     * @return
     */
    @PostMapping("/querySensitiveCust")
    @ApiOperation(value = "查询渠道敏感客户群信息 ", notes = "查询渠道敏感客户群信息")
    public ActionResponse<IPage<McdChannelSensitiveCustInfo>> querySensitiveCustInfo(@RequestBody SensitiveCustQuery query) {
        log.info("查询敏感客户群配置,query:{}", query);
        ActionResponse resp;
        try {
            resp = ActionResponse.getSuccessResp("查询渠道敏感客户群信息成功");
            resp.setData(sensitiveCustConfService.querySensitiveCustInfo(query));
        } catch (Exception e) {
            resp = ActionResponse.getFaildResp("查询渠道敏感客户群信息失败");
            log.error("查询渠道敏感客户群信息失败:", e);
        }
        return resp;
    }

    /**
     * 删除渠道敏感客户群配置
     *
     * @param custDeleReq 参数
     * @return
     */
    @PostMapping("/deleteSensitiveCust")
    @ApiOperation(value = "删除渠道敏感客户群配置 ", notes = "删除渠道敏感客户群配置")
    public ActionResponse deleteSensitiveCustInfo(@RequestBody SensitiveCustDeleReq custDeleReq) {
        log.info("删除渠道敏感客户群,query:{}", custDeleReq);
        ActionResponse resp;
        try {
            QueryWrapper<McdChannelSensitiveCustConf> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(McdChannelSensitiveCustConf::getCustgroupId, custDeleReq.getCustgroupId());
            resp = ActionResponse.getSuccessResp("删除渠道敏感客户群成功");
            resp.setData(sensitiveCustConfService.remove(wrapper));
        } catch (Exception e) {
            resp = ActionResponse.getFaildResp("删除渠道敏感客户群失败");
            log.error("删除渠道敏感客户群失败:", e);
        }
        return resp;
    }

    /**
     * 批量删除渠道敏感客户群配置
     *
     * @param query 参数
     * @return
     */
    @PostMapping("/batchDeleteSensitiveCust")
    @ApiOperation(value = "批量删除渠道敏感客户群配置 ", notes = "批量删除渠道敏感客户群配置")
    public ActionResponse batchDeleteSensitiveCustInfo(@RequestBody List<SensitiveCustDeleReq> query) {
        log.info("批量删除渠道敏感客户群,query:{}", query);
        ActionResponse resp;
        if (CollectionUtils.isEmpty(query)){
            resp = ActionResponse.getSuccessResp("批量删除渠道敏感客户群信息为空!");
            return resp;
        }
        try {
            for (SensitiveCustDeleReq custDeleReq : query) {
                QueryWrapper<McdChannelSensitiveCustConf> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(McdChannelSensitiveCustConf::getCustgroupId, custDeleReq.getCustgroupId()) ;
                sensitiveCustConfService.remove(wrapper);
            }

            resp = ActionResponse.getSuccessResp("批量删除渠道敏感客户群成功");
        } catch (Exception e) {
            resp = ActionResponse.getFaildResp("批量删除渠道敏感客户群失败");
            log.error("批量删除渠道敏感客户群失败:", e);
        }
        return resp;
    }

}

