package com.asiainfo.biapp.pec.plan.jx.coordination.service;

import com.asiainfo.biapp.pec.plan.jx.coordination.req.McdStrategicCoordinationReq;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.StrategicCoordinationBaseInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface McdStrategicCoordinationService  {

    //策略池分页查询
    Page<StrategicCoordinationBaseInfo> queryStrategicCoordinationBaseInfos(McdStrategicCoordinationReq req);

    //融合计算全部选择添加
    List<StrategicCoordinationBaseInfo> selectAllStrategicCoordinationList(McdStrategicCoordinationReq req);

}
