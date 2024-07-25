package com.asiainfo.biapp.pec.plan.jx.system.dao;

import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageCityDTO;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageDeptDTO;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageDetail;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsagePersonageDTO;
import com.asiainfo.biapp.pec.plan.jx.system.req.SystemUsageDetailReqQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface ISystemUsageDao {

    IPage<SystemUsageCityDTO> querySystemUsageCityList(Page page,@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    IPage<SystemUsageDeptDTO> querySystemUsageDeptList(Page page,@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    IPage<SystemUsagePersonageDTO> querySystemUsagePersonageList(Page page,@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    IPage<SystemUsageDetail> getUsageCityDetailList(Page page, @Param("param") SystemUsageDetailReqQuery reqQuery);

    IPage<SystemUsageDetail> getUsageDeptDetailList(Page page, @Param("param") SystemUsageDetailReqQuery reqQuery);

    IPage<SystemUsageDetail> getUsagePersonageDetailList(Page page, @Param("param") SystemUsageDetailReqQuery reqQuery);
}
