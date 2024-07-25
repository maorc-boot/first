package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl;

import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils.ExportUtil;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjCountyResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.mapper.ReportYjCountyDetailNewMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjCountyDetailNew;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.ReportYjCountyDetailNewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户通预警报表（区县） 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@Service
public class ReportYjCountyDetailNewServiceImpl extends ServiceImpl<ReportYjCountyDetailNewMapper, ReportYjCountyDetailNew> implements ReportYjCountyDetailNewService {

    @Override
    public Page<ReportYjCountyResultInfo> selectCountyWarnReport(Integer pageNum, Integer pageSize, String statDate) {
        return baseMapper.selectCountyWarnReport(new Page<>(pageNum, pageSize), statDate);
    }

    @Override
    public void exportCountyWarnReport(String statDate) {
        List<Map<String, Object>> countyWarnReports = baseMapper.exportCountyWarnReport(statDate);
        if (countyWarnReports.size() == 0)
            throw new BaseException("没有符合日期：" + String.format("%tF", statDate) + "的预警数据，无法导出！");
        ExportUtil.mapToXlsx("客户通区县预警报表", true, countyWarnReports);
    }
}
