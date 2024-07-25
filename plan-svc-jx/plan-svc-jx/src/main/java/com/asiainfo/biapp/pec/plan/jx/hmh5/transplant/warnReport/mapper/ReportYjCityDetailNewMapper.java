package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.mapper;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjCityDetailNew;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjCityDetailResultInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户通预警报表（城市） Mapper 接口
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
public interface ReportYjCityDetailNewMapper extends BaseMapper<ReportYjCityDetailNew> {

    Page<ReportYjCityDetailResultInfo> selectCityWarnReport(Page page, @Param("statDate") String statDate);

    List<Map<String,Object>> exportCityWarnReport(@Param("statDate") String statDate);

}
