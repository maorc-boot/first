package com.asiainfo.biapp.pec.plan.jx.camp.dao;


import com.asiainfo.biapp.pec.plan.jx.camp.vo.CampChannel;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdExcellentRecommendCamp;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface McdCampSaleSituationDao {

	List<CampChannel> getCampChannel(@Param("campsegId") String campsegId);

	Page<McdExcellentRecommendCamp> getRecommendCamp(Page page,@Param("cityId") String cityId);

}
