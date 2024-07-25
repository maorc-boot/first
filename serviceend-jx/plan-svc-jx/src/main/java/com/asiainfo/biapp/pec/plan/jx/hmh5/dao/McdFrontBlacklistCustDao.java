package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontBlacklistCust;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface McdFrontBlacklistCustDao extends BaseMapper<McdFrontBlacklistCust> {


    int batchSaveBlacklist(List<McdFrontBlacklistCust> importList);
}
