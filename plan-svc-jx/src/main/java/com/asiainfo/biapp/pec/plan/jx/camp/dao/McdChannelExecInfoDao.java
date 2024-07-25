package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdChannelExecInfoModel;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdChannelExecInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface McdChannelExecInfoDao extends BaseMapper<McdChannelExecInfoModel> {
    McdChannelExecInfo queryChannelExecInfo(@Param("channelId") String channelId);
    List<McdChannelExecInfo> queryChannelExecList(@Param("campsegId") String campsegId);
    List<Map<String,Object>> queryCustNumByChannel();
}
