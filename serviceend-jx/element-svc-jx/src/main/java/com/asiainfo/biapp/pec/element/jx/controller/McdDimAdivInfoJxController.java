package com.asiainfo.biapp.pec.element.jx.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.element.model.McdDimAdivInfo;
import com.asiainfo.biapp.pec.element.service.IMcdDimAdivInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 运营位管理 前端控制器
 * </p>
 *
 * @author wuhq6
 * @since 2021-11-23
 */

@RestController
@RequestMapping("/api/jx/mcdDimAdivInfo")
@Api(value = "江西:运营位管理服务", tags = {"江西:运营位管理服务"})
@Slf4j
public class McdDimAdivInfoJxController {

    @Resource
    private IMcdDimAdivInfoService mcdDimAdivInfoService;


    /**
     * 保存运营位前,校验运营位ID是否重复
     *
     * @param mcdDimAdivInfo 新建入参对象
     * @return true/false
     */
    @PostMapping(path = "/getValidateAdivInfoId")
    @ApiOperation(value = "江西:校验运营位Id", notes = "江西:校验运营位Id")
    public ActionResponse getValidateAdivInfoId(@RequestBody McdDimAdivInfo mcdDimAdivInfo) {
        log.info("start getValidateAdivInfoId param {}", new JSONObject(mcdDimAdivInfo));

        if (StrUtil.isEmpty(mcdDimAdivInfo.getAdivId())) {
            return ActionResponse.getFaildResp("运营位ID为空");
        }
        LambdaQueryWrapper<McdDimAdivInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdDimAdivInfo::getAdivId, mcdDimAdivInfo.getAdivId());
        try {
            int count = mcdDimAdivInfoService.count(queryWrapper);
            if (count <= 0) {
                return ActionResponse.getSuccessResp(StrUtil.format("运营位ID[{}]不存在,可以使用", mcdDimAdivInfo.getAdivId()));
            } else {
                return ActionResponse.getFaildResp(StrUtil.format("运营位ID[{}]已存在", mcdDimAdivInfo.getAdivId()));
            }
        } catch (Exception e) {
            log.error("校验运营位Id异常:", e);
            return ActionResponse.getFaildResp(StrUtil.format("校验运营位ID[{}]异常", mcdDimAdivInfo.getAdivId()));
        }
    }

}
