package com.asiainfo.biapp.auth.jx.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.asiainfo.biapp.auth.jx.query.LoginQueryJx;
import com.asiainfo.biapp.auth.jx.query.UserRsp;
import com.asiainfo.biapp.auth.jx.util.WebServiceClientUtils;
import com.asiainfo.biapp.client.cmn.api.CmnSvcFeignClient;
import com.asiainfo.biapp.client.cmn.dto.UserDTO;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * description: 江西页面登录控制层
 */
@RestController
@RequestMapping("/login")
@Api(value = "江西权限统一认证", tags = {"江西权限统一认证"})
@Slf4j
@RefreshScope
public class LoginJxController {

    @Resource
    private CmnSvcFeignClient cmnSvcFeignClient;

    @Value("${spring.profiles.active:dev}")
    private volatile String dicProfile;

    @Value("${secure.url4A:}")
    private volatile String url4A;

    @Value("${secure.tokenSigningKey}")
    private String tokenSigningKey;

    /**
     * Emis单点登录
     *
     * @param loginQuery
     * @return
     */
    @PostMapping("/emis1")
    public ActionResponse loginEmis(@RequestBody LoginQueryJx loginQuery) {
        return ssoLogin(loginQuery);
    }

    /**
     * 4A单点登录
     *
     * @param loginQuery
     * @return
     */
    @PostMapping("/4A")
    public ActionResponse login4A(@RequestBody LoginQueryJx loginQuery) {
        return ssoLogin(loginQuery);
    }

    /**
     * 4A单点登录
     *
     * @param loginQuery
     * @return
     */
    @PostMapping("/iop4")
    public ActionResponse loginIop4(@RequestBody LoginQueryJx loginQuery) {
        return ssoLogin(loginQuery);
    }

    /**
     * 单点登录
     *
     * @param loginQuery
     * @return
     */
    private ActionResponse ssoLogin(LoginQueryJx loginQuery) {
        log.info("ssoLogin param:{}", JSONUtil.toJsonStr(loginQuery));
        try {
            if (!this.verifyParam(loginQuery)) {
                return ActionResponse.getFaildResp("参数不合法！");
            }
            // 获取Emis或者4A单点登录用户名
            String userName = getUserId(loginQuery);
            McdIdQuery query = new McdIdQuery();
            query.setId(userName);
            long start = System.currentTimeMillis();
            final ActionResponse<UserDTO> byUserID = cmnSvcFeignClient.getByUserID(query);
            log.info("cmn getByUserID 接口耗时:{}", System.currentTimeMillis() - start);
            if (null == byUserID || byUserID.getData() == null) {
                return ActionResponse.getFaildResp(StrUtil.format("用户:{}不存在!", userName));
            }
            start = System.currentTimeMillis();
            // 生成jwt需要的参数
            Map<String, Object> tokenParam = new HashMap<>();
            tokenParam.put("user_name", byUserID.getData().getUserId());
            tokenParam.put("userId", byUserID.getData().getUserId());
            tokenParam.put("roleIds", byUserID.getData().getRoleIds());
            tokenParam.put("province", byUserID.getData().getProvince());
            tokenParam.put("userNameCn", byUserID.getData().getUserName());
            tokenParam.put("cityId", byUserID.getData().getCityId());
            tokenParam.put("departmentId", byUserID.getData().getDepartmentId());
            tokenParam.put("scope", "[\"all\"]");
            tokenParam.put("client_id", byUserID.getData().getId());
            // 生成jwt
            String token = JWTUtil.createToken(tokenParam, tokenSigningKey.getBytes());
            log.info("===token::{}", token);
            log.info("postAccessToken 耗时:{}", System.currentTimeMillis() - start);

            // 返回前端的结果
            Map<String, Object> result = new HashMap<>();
            result.put("access_token", token);
            result.put("token_type", "bearer");
            result.put("scope", "all");
            result.put("roleIds", byUserID.getData().getRoleIds());
            result.put("province", byUserID.getData().getProvince());
            result.put("departmentId", byUserID.getData().getDepartmentId());
            result.put("cityId", byUserID.getData().getCityId());
            result.put("userId", byUserID.getData().getUserId());
            result.put("userNameCn", byUserID.getData().getUserName());
            if (StrUtil.isNotEmpty(loginQuery.getAppAcctId())) {
                // 将4A的用户ID返回给前端
                result.put("appAcctId", loginQuery.getAppAcctId());
            }
            log.info("===result::{}", result);
            ActionResponse successResp = ActionResponse.getSuccessResp().setData(result);
            return successResp;
        } catch (Exception e) {
            log.error("登录失败:{}", e.getMessage(), e);
        }
        return ActionResponse.getFaildResp("登录失败，请重试！");
    }

    /**
     * 通过token或者 ticket获取用户ID
     *
     * @param loginQuery
     * @return
     */
    private String getUserId(LoginQueryJx loginQuery) {
        // EMIS单点登录
        if (!StrUtil.isEmpty(loginQuery.getTicket())) {
            Assertion assersion = AssertionHolder.getAssertion();
            // //获取当前登录人账号名
            String loginName = assersion.getPrincipal().getName();
            log.info("Emis 登录用户: {}", loginName);
            return loginName;
        }
        // IOP4单点登录
        if (StrUtil.isNotEmpty(loginQuery.getUserId())) {
            return loginQuery.getUserId();
        }
        // 4A单点登录
        return get4AuserId(loginQuery);
    }

    /**
     * @param loginQuery
     * @return boolean
     * @author zhukun
     * @date 2021/12/21 16:09
     * @Description 参数合法校验，合法返回true,不合法返回false
     */
    private boolean verifyParam(LoginQueryJx loginQuery) {
        if (StrUtil.isNotEmpty(loginQuery.getUserId())) {
            return true;
        }
        String token = loginQuery.getToken();
        if (StrUtil.isEmpty(token)) {
            token = loginQuery.getTicket();
        }
        String clientId = loginQuery.getClientId();
        String clientSecret = loginQuery.getClientSecret();
        String grantType = loginQuery.getGrantType();
        return StrUtil.isAllNotBlank(token, clientId, clientSecret, grantType);
    }

    /**
     * 请求4A门户接口获取用户名
     *
     * @return 用户账号
     */
    private String get4AuserId(LoginQueryJx loginQuery) {
        if ("dev".equalsIgnoreCase(dicProfile)) {
            return "admin01";
        }

        UserRsp userRsp = WebServiceClientUtils.getAccInfo01(url4A, loginQuery.getToken(), loginQuery.getAppAcctId());
        if (userRsp != null) {
            String repCode = userRsp.getBody().getRsp();
            log.info("getAccInfo response result:" + repCode);
            if ("0".equals(repCode)) {
                // IOP用户ID
                String userId = userRsp.getBody().getAppAcctId();
                return userId;
            }
            log.info("进入4A校验结束");
        }
        log.info("4A校验，用户不存在，userRsp 为null");
        return null;
    }

}
