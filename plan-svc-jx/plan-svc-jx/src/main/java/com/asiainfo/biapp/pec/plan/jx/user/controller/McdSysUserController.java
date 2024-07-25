package com.asiainfo.biapp.pec.plan.jx.user.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.utils.DES;
import com.asiainfo.biapp.pec.plan.jx.webservice.Ws4AService;
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
 * 用户表 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2023-05-15
 */
@RestController
@RequestMapping("/mcdSysUser")
@Api(value = "江西:系统用户", tags = "江西:系统用户")
@Slf4j
public class McdSysUserController {


    @Resource
    private Ws4AService ws4AService;

    @PostMapping("/encrypt")
    @ApiOperation(value = "用户ID DES加密", notes = "用户ID DES加密")
    private ActionResponse<String> encrypt(@RequestParam("userId") String userId) {
        log.info("userId:{}", userId);
        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            resp.setData(DES.encrypt(userId));
        } catch (Exception e) {
            resp = ActionResponse.getFaildResp();
            log.error("userId:{} encrypt error!", userId, e);
        }
        return resp;
    }

    @PostMapping("/decrypt")
    @ApiOperation(value = "用户ID DES解密", notes = "用户ID DES解密")
    private ActionResponse<String> decrypt(@RequestParam("userId") String userId) {
        log.info("userId:{}", userId);
        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            resp.setData(DES.decrypt(userId));
        } catch (Exception e) {
            resp = ActionResponse.getFaildResp();
            log.error("userId:{} decrypt error!", userId, e);
        }
        return resp;
    }

    @PostMapping("/mock4a")
    @ApiOperation(value = "模拟4A调用webservice接口", notes = "模拟4A调用webservice接口")
    private ActionResponse<String> mock4a(@RequestParam("infoXml") String infoXml) {
        log.info("infoXml:{}", infoXml);
        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            resp.setData(ws4AService.UpdateAppAcctSoap(infoXml));
        } catch (Exception e) {
            resp = ActionResponse.getFaildResp();
            log.error("mock4a error,param={}", infoXml, e);
        }
        return resp;
    }


}

