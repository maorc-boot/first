package com.asiainfo.biapp.pec.plan.jx.link.dao;


import com.asiainfo.biapp.pec.plan.jx.link.vo.McdCampDef;
import com.asiainfo.biapp.pec.plan.jx.link.vo.McdDimCampStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMpmCampSegInfoDao {

    /**
     * 根据编号取具体的某个活动波次信息
     * @param campSegId
     * @return
     * @throws Exception
     */
    McdCampDef getCampSegInfo(@Param("campSegId") String campSegId) throws Exception;

    /**
     * 根据营销状态ID获取营销状态
     * @param dimCampsegStatID
     * @return
     */
    McdDimCampStatus getDimCampsegStat(@Param("dimCampsegStatID") String dimCampsegStatID);

}
