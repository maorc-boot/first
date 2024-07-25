package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;


import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontMainOfferInfoModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontMainOfferInfoSaveModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontMainOfferQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author ranpf
 * @date 2023-2-17
 * @description 主套餐持久层
 */
@Mapper
public interface McdFrontMainOfferInfoDao extends BaseMapper<McdFrontMainOfferInfoSaveModel> {

    Page selectMainOffers(Page page, @Param("mainOfferQuery") McdFrontMainOfferQuery mainOfferQuery);

    McdFrontMainOfferInfoModel selectMainOffer(String offerId);
}
