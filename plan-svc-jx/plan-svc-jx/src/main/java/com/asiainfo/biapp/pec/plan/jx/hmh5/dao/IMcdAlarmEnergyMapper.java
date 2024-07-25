package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdAlarmReportQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdAlarmEfficacyVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMcdAlarmEnergyMapper {

    /**
     * 外呼轨迹查询
     *
     * @param page
     * @param query
     * @return
     */
    IPage<McdAlarmEfficacyVo> queryAlarmEnergyList(@Param("page") IPage page, @Param("query") McdAlarmReportQuery query);
}
