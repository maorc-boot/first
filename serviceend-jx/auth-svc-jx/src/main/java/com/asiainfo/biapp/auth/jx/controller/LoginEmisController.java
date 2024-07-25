package com.asiainfo.biapp.auth.jx.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.client.cmn.api.CmnSvcFeignClient;
import com.asiainfo.biapp.client.cmn.dto.Dic;
import com.asiainfo.biapp.client.cmn.query.DicQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mamp
 * @date 2023/5/29
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginEmisController {
    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Resource
    private CmnSvcFeignClient cmnSvcFeignClient;

    @Value("${spring.profiles.active:dev}")
    private volatile String dicProfile;

    @Value("${secure.urlEmis:}")
    private String loginUrl;

    @Value("${cas.serverLoginUrl:}")
    private String serverLoginUrl;


    @RequestMapping("/emis")
    public void loginEmis(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String redirectURL = request.getParameter("redirectURL");
        String campsegId = request.getParameter("campsegId");
        log.info("EmisLogin,redirectURL={},campsegId={}", redirectURL, campsegId);
        Assertion assersion = AssertionHolder.getAssertion();
        // //获取当前登录人账号名
        String loginName = null;
        if (null != assersion && null != assersion.getPrincipal()) {
            loginName = assersion.getPrincipal().getName();
        }
        log.info("Emis用户ID:{}", loginName);
        log.info("当前profile:{}", dicProfile);
        if (StrUtil.isEmpty(loginName)) {
            if ("dev".equals(dicProfile)) {
                loginName = "admin";
            } else {
                log.error("获取登录用户失,重新登录,Url:{}", serverLoginUrl);
                response.sendRedirect(serverLoginUrl);
                return;
            }
        }
        String pwd = getPwd().getData().getDicValue();
        Map<String, String> parameters = createClientParameters(loginName, pwd, "password");
        UsernamePasswordAuthenticationToken clientToken = createClientToken("asiainfo_1", "123456");
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(clientToken, parameters).getBody();
        log.info("OAuth2AccessToken:{}", oAuth2AccessToken);
        String accessToken = oAuth2AccessToken.getValue();
        OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
        String refreshToken = oAuth2RefreshToken.getValue();
        String tokenType = oAuth2AccessToken.getTokenType();
        if ("bearer".equals(tokenType)) {
            tokenType = "Bearer";
        }
        StringBuffer redirectUrl = new StringBuffer();
        redirectUrl.append(loginUrl);
        log.info("loginUrl:{}", loginUrl);
        redirectUrl.append(StrUtil.format("?access_token={}", accessToken));
        redirectUrl.append(StrUtil.format("&refresh_token={}", refreshToken));
        redirectUrl.append(StrUtil.format("&token_type={}", tokenType));
        if (null != oAuth2AccessToken.getExpiration()) {
            redirectUrl.append(StrUtil.format("&expiration={}", oAuth2AccessToken.getExpiration().getTime()));
        }
        if (null != oAuth2AccessToken.getScope()) {
            redirectUrl.append(StrUtil.format("&scope={}", oAuth2AccessToken.getScope()));
        }
        if (CollectionUtil.isNotEmpty(oAuth2AccessToken.getAdditionalInformation())) {
            Map<String, Object> info = oAuth2AccessToken.getAdditionalInformation();
            redirectUrl.append(StrUtil.format("&userId={}", getVal(info, "userId")));
            redirectUrl.append(StrUtil.format("&userNameCn={}", getVal(info, "userNameCn")));
            redirectUrl.append(StrUtil.format("&roleIds={}", getVal(info, "roleIds")));
            redirectUrl.append(StrUtil.format("&province={}", getVal(info, "province")));
            redirectUrl.append(StrUtil.format("&cityId={}", getVal(info, "cityId")));
            redirectUrl.append(StrUtil.format("&departmentId={}", getVal(info, "departmentId")));
            redirectUrl.append(StrUtil.format("&jti={}", getVal(info, "jti")));
        }
        if (StrUtil.isNotEmpty(redirectURL)) {
            redirectUrl.append(StrUtil.format("&redirectURL={}", redirectURL));
        }
        if (StrUtil.isNotEmpty(campsegId)) {
            redirectUrl.append(StrUtil.format("&campsegId={}", campsegId));
        }
        log.info("redirectUrl:{}", redirectUrl);
        response.sendRedirect(redirectUrl.toString());
    }

    /**
     * 获取参数值并URL编码
     *
     * @param map
     * @param key
     * @return
     */
    private String getVal(Map<String, Object> map, String key) {
        if (CollectionUtil.isEmpty(map) || map.get(key) == null) {
            return null;
        }
        try {
            return URLEncoder.encode(String.valueOf(map.get(key)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("URLEncode异常,str={}", key, e);
        }
        return String.valueOf(map.get(key));
    }

    /**
     * 获取字典表配置的密码
     *
     * @return 字典对象
     */
    private ActionResponse<Dic> getPwd() {
        DicQuery dto = new DicQuery();
        dto.setDicKey("LOGIN_PWD");
        dto.setDicProfile(dicProfile);
        return cmnSvcFeignClient.getByDicKeyAndDicProfile(dto);
    }

    private Map<String, String> createClientParameters(String username, String password, String grantType) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", username);
        parameters.put("password", password);
        parameters.put("grant_type", grantType);
        return parameters;
    }

    private UsernamePasswordAuthenticationToken createClientToken(String clientId, String clientSecret) {
        User u = new User(clientId, clientSecret, new ArrayList<>());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(u, null, new ArrayList<>());
        return token;
    }

}
