package com.asiainfo.biapp.pec.plan.jx.hmh5.service;


import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontMainOfferInfoModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontMainOfferInfoSaveModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontMainOfferQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @author ranpf
 * @date 2023-2-17
 * @description
 */
public interface McdFrontMainOfferInfoService extends IService<McdFrontMainOfferInfoSaveModel> {

    Page selectMainOffers(McdFrontMainOfferQuery mainOfferQuery);

    McdFrontMainOfferInfoModel selectMainOffer(String offerId);
}
