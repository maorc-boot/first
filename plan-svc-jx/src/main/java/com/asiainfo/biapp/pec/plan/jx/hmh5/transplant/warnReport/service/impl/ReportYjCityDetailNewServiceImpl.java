package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl;

import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils.ExportUtil;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.mapper.ReportYjCityDetailNewMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjCityDetailNew;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.ReportYjCityDetailNewService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjCityDetailResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户通预警报表（城市） 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@Service
public class ReportYjCityDetailNewServiceImpl extends ServiceImpl<ReportYjCityDetailNewMapper, ReportYjCityDetailNew> implements ReportYjCityDetailNewService {

    @Override
    public Page<ReportYjCityDetailResultInfo> selectCityWarnReport(Integer pageNum, Integer pageSize, String statDate) {
        return baseMapper.selectCityWarnReport(new Page(pageNum, pageSize), statDate);
    }

    @SneakyThrows
    @Override
    public void exportCityWarnReport(String statDate) {
        List<Map<String, Object>> cityWarnReports = baseMapper.exportCityWarnReport(statDate);
        if (cityWarnReports.size() == 0)
            throw new BaseException("没有符合日期：" + String.format("%tF", statDate) + "的预警数据，无法导出！");
        ExportUtil.mapToXlsx("客户通地市预警报表", true, cityWarnReports);
    }
}
