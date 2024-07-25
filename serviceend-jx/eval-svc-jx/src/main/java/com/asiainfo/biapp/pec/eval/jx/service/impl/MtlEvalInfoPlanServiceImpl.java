package com.asiainfo.biapp.pec.eval.jx.service.impl;

import com.asiainfo.biapp.pec.eval.jx.dao.MtlEvalInfoPlanMapper;
import com.asiainfo.biapp.pec.eval.jx.model.MtlEvalInfoPlan;
import com.asiainfo.biapp.pec.eval.jx.req.PlanEvalPageQuery;
import com.asiainfo.biapp.pec.eval.jx.service.MtlEvalInfoPlanService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 产品效果评估 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2022-12-18
 */
@Service
public class MtlEvalInfoPlanServiceImpl extends ServiceImpl<MtlEvalInfoPlanMapper, MtlEvalInfoPlan> implements MtlEvalInfoPlanService {


    @Resource
    private MtlEvalInfoPlanMapper planMapper;

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    @Override
    public IPage<MtlEvalInfoPlan> queryPage(PlanEvalPageQuery query) {
        IPage<MtlEvalInfoPlan> page = new Page<>();
        page.setCurrent(query.getCurrent());
        page.setSize(query.getSize());
        if("W".equalsIgnoreCase(query.getViewType())){
            // 周视图和月视图查询同一张表，只是日期区间不一样
            query.setViewType("D");
        }
        return planMapper.queryEvalInfoPlanPage(page, query);
    }

    /**
     * 导出
     *
     * @param query
     * @return
     */
    @Override
    public List<List<String>> export(PlanEvalPageQuery query) {
        IPage<MtlEvalInfoPlan> iPage = queryPage(query);

        List<String> list1;
        List<List<String>> list2 = new ArrayList<>();
        for (MtlEvalInfoPlan record : iPage.getRecords()) {
            list1 = new ArrayList<>();
            list1.add(record.getStatDate());
            list1.add(record.getCityName());
            list1.add(record.getCountyName());
            list1.add(record.getChannelName());
            list1.add(record.getPlanName());
            list1.add(record.getPlanId());
            // 营销用户
            list1.add(String.valueOf(record.getCampSuccNum()));
            // 营销成功用户
            list1.add(String.valueOf(record.getCampSuccNum()));
            // 送达成功率
            list1.add(String.valueOf(record.getCampSuccRate()));
            // 全网订购数
            list1.add(String.valueOf(record.getTotalOrderNum()));

            // 覆盖数
            list1.add(String.valueOf(record.getPvNum()));
            // 用户覆盖率
            list1.add(String.valueOf(record.getPvConvertRate()));

            list2.add(list1);
        }
        return list2;
    }
}
