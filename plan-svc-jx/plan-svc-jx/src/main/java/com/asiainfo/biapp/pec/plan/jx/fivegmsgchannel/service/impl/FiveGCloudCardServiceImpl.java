package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.constant.Constants;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.dto.QryTmpListReqDto;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service.IFiveGCloudCardService;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.util.FiveGCloudCardReqUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import com.alibaba.nacos.common.codec.Base64;

import java.nio.charset.StandardCharsets;

/**
 * description: 5G云卡渠道service实现
 *
 * @author: lvchaochao
 * @date: 2023/9/12
 */
@Service
@RefreshScope
@Slf4j
public class FiveGCloudCardServiceImpl implements IFiveGCloudCardService {

    @Value("${spring.profiles.active}")
    private String profiles;

    @Value("${cloudcard5g.reqcommon.apId:aaaa}")
    private String apId;

    @Value("${cloudcard5g.reqcommon.appId:bbbb}")
    private String appId;

    @Value("${cloudcard5g.reqcommon.salt:cccccccc}")
    private String salt;

    @Value("${cloudcard5g.reqcommon.requrl:https://dev.10086.cn/superSIM/unifiedPlatform/sim/capability}")
    private String requrl;

    @Value("${cloudcard5g.tmplist.apicode:card-template-query}")
    private String apicode;

    @Value("${cloudcard5g.tmplist.apiversion:v3}")
    private String apiversion;

    @Autowired
    private FiveGCloudCardReqUtil fiveGCloudCardReqUtil;

    /**
     * 查询模板列表
     *
     * @param reqDto 请求入参对象
     * @return {@link String}
     */
    @Override
    public String getTemplateList(QryTmpListReqDto reqDto) {
        String templateList = null;
        String result;
        JSONObject reqParam = new JSONObject();
        reqParam.putOpt("page", reqDto.getPage()).putOpt("pageSize", reqDto.getPageSize());
        if (CollectionUtil.isNotEmpty(reqDto.getTemplateIds())) {
            reqParam.putOpt("templateIds", reqDto.getTemplateIds());
        }
        log.info("获取超级SIM统一平台模板列表调用入参={}", JSONUtil.toJsonStr(reqParam));
        try {
            if (StrUtil.equals("dev", profiles)) {
                result = IoUtil.readUtf8(getClass().getClassLoader().getResourceAsStream("fivegCloudCardTmpListResp.properties"));;
            } else {
                result = HttpRequest.post(requrl)
                        .header("Signature", getSignature(reqParam)) // 请求参数的签名
                        .header("Unified-Platform-Timestamp", String.valueOf(System.currentTimeMillis())) // 时间戳（毫秒级），不能超过5分钟内，否则该请求被视为无效
                        .header("Unified-Platform-Apid", apId) // 调用方apId，统一平台分配的apId
                        .header("Unified-Platform-Appid", appId) // 调用方appId，统一平台分配的appId
                        .header("Md5-Secret", getMd5Secret())
                        .header("Sim-Api-Code", apicode)
                        .header("Sim-Api-Version", apiversion)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                        .body(JSONUtil.toJsonStr(reqParam)).execute().body();
            }
            log.info("获取超级SIM统一平台模板列表调用返回={}", JSONUtil.toJsonStr(result));
            JSONObject jsonObject = JSONUtil.parseObj(result);
            JSONObject status = JSONUtil.parseObj(jsonObject.get(Constants.RESP_PROP_STATUS));
            if (1051 == status.getInt(Constants.RESP_PROP_CODE)) {
                templateList = jsonObject.getStr(Constants.RESP_PROP_CONTENT);
            } else {
                log.warn("获取超级SIM统一平台模板列表调用返回失败={}", JSONUtil.toJsonStr(status));
            }
        } catch (Exception e) {
            log.error("超级SIM统一平台模板列表调用异常：", e);
        }
        return templateList;
    }

    /**
     * 获取签名
     *
     * @return {@link String}
     * @throws Exception 异常
     */
    private String getSignature(JSONObject reqParam) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("page", reqParam.getStr("page")).putOpt("pageSize", reqParam.getStr("pageSize"));
        String parameter = JSONUtil.toJsonStr(jsonObject);
        log.info("getSignature-->参数排序前={}", JSONUtil.toJsonStr(parameter));
        // 把 paramsMap 进行自然排序，得到自然排序后的结果
        parameter = fiveGCloudCardReqUtil.sort(parameter);
        log.info("getSignature-->参数排序后={}", JSONUtil.toJsonStr(parameter));
        // 盐值，每个appid都有相应的盐值，下面为举例
        // 得到签名结果
        String bytesToHexString = fiveGCloudCardReqUtil.bytesToHexString(fiveGCloudCardReqUtil.getHmacMd5Bytes(salt.getBytes(), parameter.getBytes()));
        log.info("getSignature-->统一平台签名={}", bytesToHexString);
        return bytesToHexString;
    }

    /**
     * 获取MD5加密
     *
     * @return {@link String}
     */
    private String getMd5Secret() {
        String md5CalculateParameter = salt + appId;
        // org.springframework.util.DigestUtils
        String md5CalculateBySystem = DigestUtils.md5DigestAsHex(md5CalculateParameter.getBytes(StandardCharsets.UTF_8));
        String md5SecretCalculateBySystem = new String(Base64.encodeBase64(md5CalculateBySystem.getBytes(StandardCharsets.UTF_8)));
        log.info("getMd5Secret-->Md5-Secret={}", md5SecretCalculateBySystem);
        return md5SecretCalculateBySystem;
    }
}
