package com.asiainfo.biapp.pec.plan.jx.dna.service;

import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.dnacustomgroup.CustgroupDetailVO;
import com.asiainfo.biapp.pec.plan.vo.req.CustomActionQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

/**
 * description: dna客群信息获取service
 *
 * @author: lvchaochao
 * @date: 2023/12/12
 */
public interface IDNACustomGroupService {

    /**
     * 江西获取dna客群列表
     *
     * @param req 入参信息
     * @return {@link IPage}<{@link CustgroupDetailVO}>
     */
    IPage<CustgroupDetailVO> getMoreMyCustom(CustomActionQuery req);

    /**
     * 调用DNA方提供接口获取客户群数据
     *
     * @param custgroupId 客户群id
     * @return CustgroupDetailVO
     */
    CustgroupDetailVO detailCustgroup(String custgroupId);

    /**
     * 标签变量替换信息
     *
     * @param keyWord 关键字
     * @return JSONObject
     */
    JSONObject getVariableInfo(String keyWord);

    /**
     * 下载客户群清单
     *
     * @param custId 客户群编码
     * @return 文件路径
     */
    Map<String, String> dowloadCustFile(String custId);


}
