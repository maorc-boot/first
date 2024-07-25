package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.mapper;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.controller.reqParam.SelectWarnDetailParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjDetailResultInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjDetailInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户通预警报表（明细） Mapper 接口
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
public interface ReportYjDetailInfoMapper extends BaseMapper<ReportYjDetailInfo> {

    Page<ReportYjDetailResultInfo> selectDetailWarnReport(Page<Object> objectPage, @Param("param") SelectWarnDetailParam param);

    List<Map<String, Object>> exportDetailWarnReport(@Param("param") SelectWarnDetailParam param);

}
