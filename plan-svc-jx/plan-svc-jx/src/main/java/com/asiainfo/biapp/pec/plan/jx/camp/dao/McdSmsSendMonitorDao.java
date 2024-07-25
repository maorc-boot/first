package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdSmsSendMonitorModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface McdSmsSendMonitorDao extends BaseMapper<McdSmsSendMonitorModel> {

    List<Map<String,Object>> querySmsSendBasicInfo();

    List<Map<String ,Object>> querySmsSendHistoryBasicInfo(@Param("endDate") String endDate);
}
