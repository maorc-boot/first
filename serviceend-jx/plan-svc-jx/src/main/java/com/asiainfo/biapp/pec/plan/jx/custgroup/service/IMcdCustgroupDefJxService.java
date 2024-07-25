package com.asiainfo.biapp.pec.plan.jx.custgroup.service;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CustomJxActionQuery;
import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupDefJx;
import com.asiainfo.biapp.pec.plan.jx.custgroup.vo.CustgroupDetailJxVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author imcd
 * @since 2021-11-08
 */
public interface IMcdCustgroupDefJxService extends IService<McdCustgroupDefJx> {

    IPage<CustgroupDetailJxVO> getMoreMyCustom(CustomJxActionQuery req);

    /**
     * 获取客户群详情，带标签
     *
     * @param custgroupId
     * @return
     */
    CustgroupDetailJxVO detailCustgroup(String custgroupId);

    /**
     * coc客群更新同步dna(手工同步旧数据接口)
     *
     * @param custgroupId 客群id
     * @return {@link ActionResponse}
     */
    ActionResponse syncSendUpdateCustInfo2Dna(String custgroupId);

}
