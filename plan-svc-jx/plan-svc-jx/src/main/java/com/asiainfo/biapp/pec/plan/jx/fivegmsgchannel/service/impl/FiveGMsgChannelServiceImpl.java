package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.constant.Constants;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.dao.IFiveGMsgChannelDao;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.enumtype.Msg5GConfigEnum;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service.IFiveGMsgChannelService;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service.IMsgAccessTokenService;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.FiveGAppIdAppSecretRelVo;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.MsgAccessToken;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.QueryTmpListRequestVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description: 5G消息渠道service实现
 *
 * @author: lvchaochao
 * @date: 2022/12/22
 */
@Service
@Slf4j
public class FiveGMsgChannelServiceImpl extends ServiceImpl<IFiveGMsgChannelDao, FiveGAppIdAppSecretRelVo> implements IFiveGMsgChannelService {

    @Autowired
    private IFiveGMsgChannelDao fiveGMsgChannelDao;

    @Autowired
    private IMsgAccessTokenService msgAccessTokenService;

    @Value("${msg5g.querytemplatelist.api_base_5g_server_root:https://rcs.10086.cn/5g-csp/csp-dev}")
    private String base5GServerRoot;

    @Value("${msg5g.token.redis_expire_time:3000}")
    private String redisExpireTime;

    /**
     * 获取5G应用号密钥映射数据
     *
     * @param channelId 渠道id
     * @return List<FiveGAppIdAppSecretRelVo>
     */
    @Override
    public List<FiveGAppIdAppSecretRelVo> queryApplicationNumInfo(String channelId) {
        LambdaQueryWrapper<FiveGAppIdAppSecretRelVo> qry = Wrappers.lambdaQuery();
        qry.eq(FiveGAppIdAppSecretRelVo::getChannelId, channelId);
        return fiveGMsgChannelDao.selectList(qry);
    }

    /**
     * 获取模板列表
     *
     * @param requestVo requestVo
     * @return {@link String}
     */
    @Override
    public String getTemplateList(QueryTmpListRequestVo requestVo) {
        // 1. 根据应用号对应的appid、appsecret获取token
        String templateList = null;
        String token;
        try {
            if (!RedisUtils.existsKey(Msg5GConfigEnum.AUTHORIZATION.getKey() + StrUtil.UNDERLINE + requestVo.getChatbotId())) {
                token = getToken(requestVo);
            } else {
                token = RedisUtils.getValue(Msg5GConfigEnum.AUTHORIZATION.getKey() + StrUtil.UNDERLINE + requestVo.getChatbotId());
            }
            if (StrUtil.isEmpty(token)) {
                log.warn("5G消息获取token={}为空！", token);
                return templateList;
            }
            // 2. 根据chatbotId和token获取模板列表数据
            String url = base5GServerRoot + Constants.API_TEMPLATE_LIST.replace(Constants.PLACEHOLDER_CHATBOT_ID, requestVo.getChatbotId());
            log.info("5g消息平台调用获取模板列表url={}", url);
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("pageNum", requestVo.getPageNum());
            bodyMap.put("pageSize", requestVo.getPageSize());
            bodyMap.put("templateType", requestVo.getTemplateType());
            bodyMap.put("contentType", requestVo.getContentType());
            bodyMap.put("result", requestVo.getResult());
            bodyMap.put("templateName", requestVo.getTemplateName());
            log.info("5g消息平台获取模板列表接口调用入参={}", JSONUtil.toJsonStr(bodyMap));
            String result = HttpRequest.post(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).header(HttpHeaders.CONTENT_TYPE, "application/json").body(JSONUtil.toJsonStr(bodyMap)).execute().body();
            log.info("5g消息平台调用获取模板列表接口响应={}", JSONUtil.toJsonStr(result));
            JSONObject jsonObject = JSONObject.fromObject(result);
            if (Constants.RESP_SUCCESS_CODE0.equals(jsonObject.getString(Constants.RESP_PROP_CODE))) {
                templateList = jsonObject.getString(Constants.RESP_PROP_DATA);
            } else {
                String msg = jsonObject.getString(Constants.RESP_PROP_MSG);
                log.warn("5g消息平台调用获取模板列表失败：{}", msg);
            }
        } catch (Exception e) {
            log.error("5g消息平台调用获取模板列表异常：", e);
        }
        return templateList;
    }

    /**
     * 获得token
     *
     * @param requestVo requestVo
     * @return {@link String}
     */
    private String getToken(QueryTmpListRequestVo requestVo) {
        // 1. 获取token
        String accessToken = null;
        try {
            String url = base5GServerRoot + Constants.API_TOKEN.replace(Constants.PLACEHOLDER_APPID, requestVo.getAppId()).replace(Constants.PLACEHOLDER_SECRET, requestVo.getAppSecret());
            log.info("5g消息平台调用获取token url:{}", url);
            String body = HttpRequest.post(url).execute().body();
            log.info("5g消息平台调用获取token接口响应：{}", JSONUtil.toJsonStr(body));
            JSONObject jsonObject = JSONObject.fromObject(body);
            if (Constants.RESP_SUCCESS_CODE0.equals(jsonObject.getString(Constants.RESP_PROP_CODE))) {
                JSONObject data = jsonObject.getJSONObject(Constants.RESP_PROP_DATA);
                accessToken = data.getString("access_token");
                log.info("5g消息平台调用获取token={}", accessToken);
                // 2. 将token更新到msg_access_token表
                updateToken(requestVo, accessToken);
                // 3. token存放redis
                RedisUtils.setKeyValueWithExpireTime(Msg5GConfigEnum.AUTHORIZATION.getKey() + StrUtil.UNDERLINE + requestVo.getChatbotId(), accessToken, Long.parseLong(redisExpireTime), TimeUnit.SECONDS);
            } else {
                String msg = jsonObject.getString(Constants.RESP_PROP_MSG);
                log.warn("5g消息平台调用获取token失败：{}", msg);
            }
        } catch (Exception e) {
            log.error("5g消息平台调用获取token异常：", e);
        }
        return accessToken;
    }

    /**
     * 更新token
     *
     * @param requestVo   requestVo
     * @param accessToken accessToken
     */
    private void updateToken(QueryTmpListRequestVo requestVo, String accessToken) {
        try {
            LambdaQueryWrapper<MsgAccessToken> qry = Wrappers.lambdaQuery();
            qry.eq(MsgAccessToken::getUserName, requestVo.getAppId());
            int count = msgAccessTokenService.count(qry);
            if (count > 0) {
                LambdaUpdateWrapper<MsgAccessToken> qry2 = Wrappers.lambdaUpdate();
                qry2.set(MsgAccessToken::getUserName, requestVo.getAppId())
                        .set(MsgAccessToken::getToken, accessToken)
                        .set(MsgAccessToken::getUpdateTime, new Date())
                        .eq(MsgAccessToken::getUserName, requestVo.getAppId());
                msgAccessTokenService.update(qry2);
            } else {
                MsgAccessToken msgAccessToken = new MsgAccessToken();
                msgAccessToken.setToken(accessToken);
                msgAccessToken.setUserName(requestVo.getAppId());
                msgAccessToken.setUpdateTime(new Date());
                msgAccessTokenService.save(msgAccessToken);
            }
        } catch (Exception e) {
            log.error("更新token异常：", e);
        }

    }
}
