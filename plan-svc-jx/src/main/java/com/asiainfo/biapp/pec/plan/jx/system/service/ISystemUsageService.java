package com.asiainfo.biapp.pec.plan.jx.system.service;

import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageCityDTO;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageDeptDTO;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageDetail;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsagePersonageDTO;
import com.asiainfo.biapp.pec.plan.jx.system.req.SystemUsageDetailReqQuery;
import com.asiainfo.biapp.pec.plan.jx.system.req.SystemUsageReqQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface ISystemUsageService {

    IPage<SystemUsageCityDTO> querySystemUsageCityList(SystemUsageReqQuery reqQuery);

    IPage<SystemUsageDeptDTO> querySystemUsageDeptList(SystemUsageReqQuery reqQuery);

    IPage<SystemUsagePersonageDTO> querySystemUsagePersonageList(SystemUsageReqQuery reqQuery);


    IPage<SystemUsageDetail> getUsageCityDetailList(SystemUsageDetailReqQuery reqQuery);

    IPage<SystemUsageDetail> getUsageDeptDetailList( SystemUsageDetailReqQuery reqQuery);

    IPage<SystemUsageDetail> getUsagePersonageDetailList(SystemUsageDetailReqQuery reqQuery);

}
