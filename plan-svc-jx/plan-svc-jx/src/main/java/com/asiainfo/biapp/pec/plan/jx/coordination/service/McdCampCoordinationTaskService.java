package com.asiainfo.biapp.pec.plan.jx.coordination.service;

import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCampCoordinationTaskModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.CampCoordinationStatusQuery;
import com.baomidou.mybatisplus.extension.service.IService;

public interface McdCampCoordinationTaskService extends IService<McdCampCoordinationTaskModel> {

    /**
     *审批流转修改状态
     *
     * @param req
     */
    void modifyCampCoordinationStatusFromAppr(CampCoordinationStatusQuery req);
}
