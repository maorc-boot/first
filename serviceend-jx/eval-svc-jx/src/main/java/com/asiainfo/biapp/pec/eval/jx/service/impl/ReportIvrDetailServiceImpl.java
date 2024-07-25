package com.asiainfo.biapp.pec.eval.jx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.eval.jx.dao.ReportIvrDetailMapper;
import com.asiainfo.biapp.pec.eval.jx.model.ReportIvrDetail;
import com.asiainfo.biapp.pec.eval.jx.req.IvrPageQuery;
import com.asiainfo.biapp.pec.eval.jx.service.ReportIvrDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2022-12-16
 */
@Service
public class ReportIvrDetailServiceImpl extends ServiceImpl<ReportIvrDetailMapper, ReportIvrDetail> implements ReportIvrDetailService {

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    @Override
    public IPage<ReportIvrDetail> queryPage(IvrPageQuery query) {

        QueryWrapper<ReportIvrDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StrUtil.isNotEmpty(query.getStatDate()), ReportIvrDetail::getStatDate, query.getStatDate());
        IPage<ReportIvrDetail> iPage = new Page<>(query.getCurrent(), query.getSize());
        return page(iPage, queryWrapper);
    }

    @Override
    public List<List<String>> export(IvrPageQuery query) {
        IPage<ReportIvrDetail> iPage = queryPage(query);

        List<String> list1;
        List<List<String>> list2 = new ArrayList<>();
        for (ReportIvrDetail record : iPage.getRecords()) {
            list1 = new ArrayList<>();
            list1.add(record.getCampsegId());
            list1.add(record.getCampsegName());
            list1.add(record.getCustomGroupName());
            // 当日IVR呼出
            list1.add(String.valueOf(record.getDHc()));
            // 当日IVR接通
            list1.add(String.valueOf(record.getDJt()));
            // 当日IVR接通
            list1.add(String.valueOf(record.getDJt()));
            // 当日IVR请求人工
            list1.add(String.valueOf(record.getDRg()));
            // 当日IVR接入人工
            list1.add(String.valueOf(record.getDJr()));
            // 当日IVR营销成功
            list1.add(String.valueOf(record.getDYxsuc()));
            // 当月IVR呼出
            list1.add(String.valueOf(record.getMHc()));
            // 当月IVR接通
            list1.add(String.valueOf(record.getMJt()));
            // 当月IVR请求人工
            list1.add(String.valueOf(record.getMRg()));
            // 当月IVR接入人工
            list1.add(String.valueOf(record.getMJr()));
            // 当月IVR营销成功
            list1.add(String.valueOf(record.getMYxcg()));

            list2.add(list1);
        }
        return list2;
    }
}
