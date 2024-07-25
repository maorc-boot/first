package com.asiainfo.biapp.pec.plan.jx.tacticOverview.service;

import com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo.SelectTacticOverviewResultInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.concurrent.ExecutionException;

public interface TacticOverviewService {
    SelectTacticOverviewResultInfo selectAllTactics(String campsegStatId, String cityId);

}
