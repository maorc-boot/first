package com.asiainfo.biapp.pec.plan.jx.custgroup.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CustomJxActionQuery;
import com.asiainfo.biapp.pec.plan.jx.custgroup.dao.McdCustgroupDefJxDao;
import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupDefJx;
import com.asiainfo.biapp.pec.plan.jx.custgroup.service.IMcdCustgroupDefJxService;
import com.asiainfo.biapp.pec.plan.jx.custgroup.vo.CustgroupDetailJxVO;
import com.asiainfo.biapp.pec.plan.vo.DNAActionResponse;
import com.asiainfo.biapp.pec.plan.vo.DNAResponseStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author imcd
 * @since 2021-11-08
 */
@Service
@Slf4j
public class McdCustgroupDefJxServiceImpl extends ServiceImpl<McdCustgroupDefJxDao, McdCustgroupDefJx> implements IMcdCustgroupDefJxService {

    @Resource
    private McdCustgroupDefJxDao custgroupDefJxDao;

    @Autowired
    private HttpServletRequest request;

    @Value("${dna.column.url:}")
    private String dnaColumnUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public IPage<CustgroupDetailJxVO> getMoreMyCustom(CustomJxActionQuery req) {
        // 是否查询不限定客群 0-不是(排除不限定客群外的其余所有客群) 1-是(包含不限定客群的所有客群)
        if (ObjectUtil.isEmpty(req.getIsQryNotLimitCust())) {
            req.setIsQryNotLimitCust(1);
        }
        Page pager = new Page(req.getCurrent(), req.getSize());
        return custgroupDefJxDao.moreMyCustom(pager, req.getUserId(), req.getCustType(), req.getKeyWords(), req.getIsQryNotLimitCust(), req.getIntellRec());
    }

    @Override
    public CustgroupDetailJxVO detailCustgroup(String custgroupId) {
        return custgroupDefJxDao.detailCustgroup(custgroupId);
    }

    /**
     * coc客群更新同步dna(手工同步旧数据接口)
     * 调dna接口，同步coc客群信息到dna并获取dna客群编号==>将dna客群编号以及同步dna次数字段更新
     *
     * @param custgroupId 客群id
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse syncSendUpdateCustInfo2Dna(String custgroupId) {
        McdCustgroupDefJx mcdCustgroupDefJx = custgroupDefJxDao.selectById(custgroupId);
        try {
            JSONConfig jsonConfig = new JSONConfig();
            jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
            log.info("异步将coc更新客群信息同步dna请求url={},入参={}", dnaColumnUrl + "/customer/coc-customer-sync", JSONUtil.toJsonStr(mcdCustgroupDefJx, jsonConfig));
            ParameterizedTypeReference<DNAActionResponse> typeRef = new ParameterizedTypeReference<DNAActionResponse>() {};
            DNAActionResponse body = restTemplate.exchange(dnaColumnUrl + "/customer/coc-customer-sync", HttpMethod.POST, buildHttpEntity(mcdCustgroupDefJx), typeRef).getBody();
            log.info("异步将coc更新客群信息同步dna请求返回={}", JSONUtil.toJsonStr(body));
            if (DNAResponseStatus.SUCCESS.getCode().equals(body.getCode())) {
                JSONObject jsonObject = JSONUtil.parseObj(body.getData());
                mcdCustgroupDefJx.setSyncDna(1);
                mcdCustgroupDefJx.setColumnNum(jsonObject.getStr("columnNum"));
                custgroupDefJxDao.updateById(mcdCustgroupDefJx);
                log.info("更新客群定义表dna客群编号成功");
            }
        } catch (Exception e) {
            log.error("异步将coc更新客群信息同步dna请求接口异常：", e);
            return ActionResponse.getFaildResp();
        }
        return ActionResponse.getSuccessResp().setData(mcdCustgroupDefJx.getColumnNum());
    }

    private <T> HttpEntity<T> buildHttpEntity(T columnSearchRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
        return new HttpEntity<>(columnSearchRequestDTO, headers);
    }

}
