package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.approve.jx.service.Chan968MaterialAuditService;
import com.asiainfo.biapp.pec.approve.jx.po.MaterialAuditFallBackPO;
import com.asiainfo.biapp.pec.approve.jx.service.IMaterialAuditFallBackService;
import com.asiainfo.biapp.pec.approve.jx.vo.MaterialAuditFallBackVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * description: 广点通968渠道素材审核接口实现
 *
 * @author: lvchaochao
 * @date: 2023/8/29
 */
@Service
@Slf4j
public class Chan968MaterialAuditServiceImpl implements Chan968MaterialAuditService {

    @Value("${chan968.materialAudit.url:http://120.197.235.102/rzocp-api/ocp-scene/api/iop/materialSync}")
    private String materialAuditUrl;

    /**
     * 请求来源编码
     */
    @Value("${chan968.materialAudit.sourceId:JX_IOP}")
    private String sourceId;

    /**
     * 请求头--账号信息
     */
    @Value("${chan968.materialAudit.username:JX_IOP}")
    private String username;

    /**
     * 请求头--密码信息
     */
    @Value("${chan968.materialAudit.password:J23dw2X}")
    private String password;

    @Autowired
    private IMaterialAuditFallBackService materialAuditFallBackService;

    /**
     * 请求素材审核接口
     *
     * @param jsonObject 入参对象
     */
    public void postMaterialAudit(JSONObject jsonObject) {
        jsonObject.putOpt("Source_ID", sourceId);
        log.info("活动={}请求素材审核接口入参={}", jsonObject.getStr("ACTIVITY_ID"), JSONUtil.toJsonStr(jsonObject));
        try {
            String result = HttpRequest.post(materialAuditUrl)
                    .header("username", username)
                    .header("password", password)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(JSONUtil.toJsonStr(jsonObject)).execute().body();
            log.info("活动={}请求素材审核接口返回={}", jsonObject.getStr("ACTIVITY_ID"), JSONUtil.toJsonStr(result));
        } catch (HttpException e) {
            log.error("活动={}请求素材审核接口异常：", jsonObject.getStr("ACTIVITY_ID"), e);
        }
    }

    /**
     * 素材审核通知接口
     *
     * @param vo 审核结果入参
     * @return {@link Map}<{@link String}, {@link String}>
     */
    @Override
    public Map<String, String> materialAuditFallback(MaterialAuditFallBackVo vo) {
        log.info("materialAuditFallback-->素材审核通知接口入参={}", JSONUtil.toJsonStr(vo));
        try {
            MaterialAuditFallBackPO po = new MaterialAuditFallBackPO();
            BeanUtil.copyProperties(vo, po);
            materialAuditFallBackService.save(po);
        } catch (Exception e) {
            log.error("materialAuditFallback-->素材审核通知保存异常：", e);
        }
        Map<String, String> resMap = new HashMap<>();
        resMap.put("code", "0");
        resMap.put("Description", "成功");
        return resMap;
    }
}
