package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampSaleSituationDao;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CampChannel;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdExcellentRecommendCamp;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IMcdCampSaleSituationServiceImpl implements IMcdCampSaleSituationService {

    @Resource
    private McdCampSaleSituationDao mcdCampSaleSituationDao;
    @Override
    public Page<McdExcellentRecommendCamp> getRecommendCamp(Page page, String cityId) {
        return mcdCampSaleSituationDao.getRecommendCamp(page,cityId);
    }


    @Override
    public List<CampChannel> getCampChannel(String campsegId) {
        return mcdCampSaleSituationDao.getCampChannel(campsegId);
    }
}
