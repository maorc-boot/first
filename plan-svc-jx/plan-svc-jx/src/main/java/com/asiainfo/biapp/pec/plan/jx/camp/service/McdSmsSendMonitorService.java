package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdSmsSendMonitorModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface McdSmsSendMonitorService extends IService<McdSmsSendMonitorModel> {

    List<Map<String,Object>> querySmsSendBasicInfo();

    List<Map<String ,Object>> querySmsSendHistoryBasicInfo(String endDate);
}
