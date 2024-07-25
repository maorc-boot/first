package com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna.*;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.DnaColumnService;
import com.asiainfo.biapp.pec.plan.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * dna标签获取service实现
 *
 * @author lvcc
 * @date 2024/04/15
 */
@Service
@Slf4j
public class DnaColumnServiceImpl implements DnaColumnService {

    @Value("${dna.column.url:}")
    private String dnaColumnUrl;

    @Autowired
    private HttpServletRequest request;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public DNAActionResponse<ColumnSearchRespondDTO> search(ColumnSearchRequestDTO columnSearchRequestDTO) {
        ParameterizedTypeReference<DNAActionResponse<ColumnSearchRespondDTO>> typeRef = new ParameterizedTypeReference<DNAActionResponse<ColumnSearchRespondDTO>>() {
        };
        return restTemplate.exchange(dnaColumnUrl + "/column/search", HttpMethod.POST, buildHttpEntity(columnSearchRequestDTO), typeRef).getBody();
    }

    @Override
    public DNAActionResponse<ColumnValuePageRespondDTO> getValuePage(ColumnValuePageRequestDTO columnValuePageRequestDTO) {
        ParameterizedTypeReference<DNAActionResponse<ColumnValuePageRespondDTO>> typeRef = new ParameterizedTypeReference<DNAActionResponse<ColumnValuePageRespondDTO>>() {
        };
        return restTemplate.exchange(dnaColumnUrl + "/column/value-page", HttpMethod.POST, buildHttpEntity(columnValuePageRequestDTO), typeRef).getBody();
    }

    /**
     * 标签裂变获取客群编号
     *
     * @param labelFissionGetCustIdReqDTO 标签裂变获取客群编号接口请求参数
     * @return {@link DNAActionResponse}<{@link LabelFissionGetCustIdRespDTO}>
     */
    @Override
    public DNACustomActionResponse getLabelFissionCustId(LabelFissionGetCustIdReqDTO labelFissionGetCustIdReqDTO) {
        labelFissionGetCustIdReqDTO.setRequestNo(RandomUtil.randomString(32));
        labelFissionGetCustIdReqDTO.setVersion("1.0");
        labelFissionGetCustIdReqDTO.setPageIndex(0);
        labelFissionGetCustIdReqDTO.setPageSize(0);
        JSONObject jsonObject = JSONUtil.parseObj(JSONUtil.toJsonStr(labelFissionGetCustIdReqDTO));
        jsonObject.remove("sign");
        String jsonStr = JSONUtil.toJsonStr(jsonObject);
        try {
            String sign = MD5Util.encode(jsonStr);
            labelFissionGetCustIdReqDTO.setSign(sign);
        } catch (Exception e) {
            log.error("根据标签映射值实时获取客群数量参数加密异常：", e);
        }
        log.info("请求dna获取裂变最小层级的客群id接口入参={}", JSONUtil.toJsonStr(labelFissionGetCustIdReqDTO, new JSONConfig().setIgnoreNullValue(false)));
        ParameterizedTypeReference<DNACustomActionResponse> typeRef = new ParameterizedTypeReference<DNACustomActionResponse>() {};
        return restTemplate.exchange(dnaColumnUrl + "/customer/batch-create-by-rule", HttpMethod.POST, buildHttpEntity(labelFissionGetCustIdReqDTO), typeRef).getBody();
    }

    /**
     * 根据标签映射值实时获取客群数量
     *
     * @param query 入参对象
     * @return {@link DNAActionResponse}
     */
    @Override
    public DNACustomActionResponse getTargetUserCount(TargetUserCountReqDTO query) {
        query.setRequestNo(RandomUtil.randomString(32));
        query.setVersion("1.0");
        query.setPageIndex(0);
        query.setPageSize(0);
        JSONObject jsonObject = JSONUtil.parseObj(JSONUtil.toJsonStr(query));
        jsonObject.remove("sign");
        String jsonStr = JSONUtil.toJsonStr(jsonObject);
        try {
            String sign = MD5Util.encode(jsonStr);
            query.setSign(sign);
        } catch (Exception e) {
            log.error("根据标签映射值实时获取客群数量参数加密异常：", e);
        }
        log.info("根据标签映射值实时获取客群数量入参={}", JSONUtil.toJsonStr(query));
        ParameterizedTypeReference<DNACustomActionResponse> typeRef = new ParameterizedTypeReference<DNACustomActionResponse>() {};
        return restTemplate.exchange(dnaColumnUrl + "/customer/conditions-calc", HttpMethod.POST, buildHttpEntity(query), typeRef).getBody();
    }

    private <T> HttpEntity<T> buildHttpEntity(T columnSearchRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
        return new HttpEntity<>(columnSearchRequestDTO, headers);
    }
}
