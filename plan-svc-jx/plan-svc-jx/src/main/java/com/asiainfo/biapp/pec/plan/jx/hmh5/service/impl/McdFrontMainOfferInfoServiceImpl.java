package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;


import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.McdFrontMainOfferInfoDao;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontMainOfferInfoModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontMainOfferInfoSaveModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontMainOfferQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontMainOfferInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;



/**
 * @author ranpf
 * @date 2023-2-17
 * @description 主套餐逻辑处理层
 */
@Service
public class McdFrontMainOfferInfoServiceImpl extends ServiceImpl<McdFrontMainOfferInfoDao, McdFrontMainOfferInfoSaveModel> implements McdFrontMainOfferInfoService {

    @Override
    public Page selectMainOffers(McdFrontMainOfferQuery mainOfferQuery) {
        return baseMapper.selectMainOffers(
                new Page<>(mainOfferQuery.getCurrent(), mainOfferQuery.getSize()),
                mainOfferQuery
        );
    }

    @Override
    public McdFrontMainOfferInfoModel selectMainOffer(String offerId) {
        return baseMapper.selectMainOffer(offerId);
    }
}
