package com.asiainfo.biapp.pec.plan.jx.custgroup.dao;

import com.asiainfo.biapp.pec.core.enums.KeyWordsEnum;
import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupDefJx;
import com.asiainfo.biapp.pec.plan.jx.custgroup.vo.CustgroupDetailJxVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


public interface McdCustgroupDefJxDao extends BaseMapper<McdCustgroupDefJx> {

    IPage<CustgroupDetailJxVO> moreMyCustom(Page<?> page,
                                            @Param("userId") String userId,
                                            @Param("custType") Integer[] custType,
                                            @Param("keyWord") @KeyWordsEnum(cols = {"cust.CUSTOM_GROUP_ID","cust.CUSTOM_GROUP_NAME"}) String keyWord,
                                            @Param("isQryNotLimitCust") Integer isQryNotLimitCust,
                                            @Param("aIIntellRec") Integer aIIntellRec);

    CustgroupDetailJxVO detailCustgroup(@Param("custgroupId") String custgroupId);

}
