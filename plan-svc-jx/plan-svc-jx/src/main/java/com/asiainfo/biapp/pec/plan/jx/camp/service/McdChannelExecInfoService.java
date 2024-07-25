package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdChannelExecInfoModel;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdChannelExecQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdChannelExecInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface McdChannelExecInfoService extends IService<McdChannelExecInfoModel> {

    McdChannelExecInfo queryChannelExecInfo(McdChannelExecQuery req);
    List<McdChannelExecInfo> queryChannelExecList(McdChannelExecQuery req);

    List<Map<String,Object>> queryCustNumByChannel();

}
