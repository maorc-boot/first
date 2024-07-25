package com.asiainfo.biapp.pec.preview.jx.controller;


import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import com.asiainfo.biapp.pec.common.jx.util.BitmapCacheUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.OutInterface;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChnPreUserNum;
import com.asiainfo.biapp.pec.preview.jx.service.McdChnPreUserNumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 客户群渠道偏好用户数统计表 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2022-09-30
 */
@RestController
@RequestMapping("/jx/chnPre")
@Slf4j
@Api(value = "江西:渠道偏好", tags = {"江西:渠道偏好"})
public class McdChnPreUserNumController {

    @Autowired
    private McdChnPreUserNumService chnPreUserNumService;

    @ApiOperation(value = "根据客户群ID查询渠道偏好数据 ", notes = "custgroupId:客户群ID,必填;preLevel:偏好级别,只1和2两个值,不传取默认值 1")
    @PostMapping("/queryPreData")
    public ActionResponse<McdChnPreUserNum> queryPreData(@RequestParam("custgroupId") String custgroupId, @RequestParam(value = "preLevel", required = false) Integer preLevel) {
        log.info("queryPreData parma:custgroupId={}, preLevel={}", custgroupId, preLevel);
        ActionResponse response = ActionResponse.getSuccessResp();
        try {
            if (null == preLevel) {
                //默认查询第一偏好
                preLevel = 1;
            }
            List<McdChnPreUserNum> dataList = chnPreUserNumService.queryPreData(custgroupId, preLevel);
            response.setData(dataList);
        } catch (Exception e) {
            response = ActionResponse.getFaildResp("查询渠道偏好数据异常");
            log.error("查询渠道偏好数据异常,custgroupId:{},preLevel:{}", custgroupId, preLevel, e);
        }
        return response;
    }


    @ApiOperation(value = "判断号码(phoneNo)是否偏好渠道(channelId)", notes = "phoneNo:手机号码,必填;channelId:渠道ID")
    @PostMapping("/check")
    @OutInterface
    public Boolean checkChnPre(@RequestParam("phoneNo") String phoneNo, @RequestParam(value = "channelId") String channelId) {
        IBitMap bitMap = BitmapCacheUtil.getChnPreBitmap(channelId);
        if (null == bitMap) {
            return false;
        }
        return bitMap.contains(Long.valueOf(phoneNo));
    }

}

