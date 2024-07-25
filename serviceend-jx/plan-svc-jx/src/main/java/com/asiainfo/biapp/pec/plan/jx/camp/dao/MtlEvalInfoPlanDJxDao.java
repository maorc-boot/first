package com.asiainfo.biapp.pec.plan.jx.camp.dao;


import com.asiainfo.biapp.pec.plan.model.MtlEvalInfoPlanD;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 产品效果评估（日） Mapper 接口
 * </p>
 * @author ranpf
 * @since 2023-5-16
 */
@Mapper
public interface MtlEvalInfoPlanDJxDao extends BaseMapper<MtlEvalInfoPlanD> {




    /**
     * 查询历史触发,办理
     * @param userCity
     * @return
     */
    Map<String, Object> querySaleNumAndSaleSuccessNumPast(@Param("userCity") String userCity);


    /**
     * 查询本月触发,办理
     * @param userCity
     * @return
     */
    Map<String,Object> querySaleNumMonAndSaleSuccessNumMon(@Param("userCity") String userCity);



    /**
     * 查询日触发,办理
     * @param userCity
     * @return
     */
    Map<String,Object> querySaleNumDayAndSaleSuccessNumDay(@Param("userCity") String userCity);



}
