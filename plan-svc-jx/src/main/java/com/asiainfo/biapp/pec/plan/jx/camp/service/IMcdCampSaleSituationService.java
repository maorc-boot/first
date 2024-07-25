package com.asiainfo.biapp.pec.plan.jx.camp.service;



import com.asiainfo.biapp.pec.plan.jx.camp.vo.CampChannel;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdExcellentRecommendCamp;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


public interface IMcdCampSaleSituationService {


	Page<McdExcellentRecommendCamp> getRecommendCamp(Page page, String cityId);


	List<CampChannel> getCampChannel(String campsegId);
}
