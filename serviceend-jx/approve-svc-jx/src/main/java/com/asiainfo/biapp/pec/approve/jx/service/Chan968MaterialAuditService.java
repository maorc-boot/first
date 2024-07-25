package com.asiainfo.biapp.pec.approve.jx.service;

import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.approve.jx.po.MaterialAuditFallBackPO;
import com.asiainfo.biapp.pec.approve.jx.vo.MaterialAuditFallBackVo;

import java.util.Map;

/**
 * description: 广点通968渠道素材审核接口
 *
 * @author: lvchaochao
 * @date: 2023/8/29
 */
public interface Chan968MaterialAuditService {

    /**
     * 请求素材审核接口
     *
     * @param jsonObject 入参对象
     */
    void postMaterialAudit(JSONObject jsonObject);

    /**
     * 素材审核通知接口
     *
     * @param vo 审核结果入参
     * @return {@link Map}<{@link String}, {@link String}>
     */
    Map<String, String> materialAuditFallback(MaterialAuditFallBackVo vo);
}
