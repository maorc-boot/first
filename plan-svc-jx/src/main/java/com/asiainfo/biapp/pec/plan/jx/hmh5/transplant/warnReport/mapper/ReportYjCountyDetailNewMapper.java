package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.mapper;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjCountyDetailNew;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjCountyResultInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户通预警报表（区县） Mapper 接口
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
public interface ReportYjCountyDetailNewMapper extends BaseMapper<ReportYjCountyDetailNew> {

    Page<ReportYjCountyResultInfo> selectCountyWarnReport(Page<Object> objectPage, @Param("statDate") String statDate);


    List<Map<String,Object>> exportCountyWarnReport(@Param("statDate") String statDate);
}
