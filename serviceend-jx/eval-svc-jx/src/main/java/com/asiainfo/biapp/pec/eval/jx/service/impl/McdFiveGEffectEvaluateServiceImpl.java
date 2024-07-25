package com.asiainfo.biapp.pec.eval.jx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.eval.jx.dao.McdFiveGEffectEvaluateMapper;
import com.asiainfo.biapp.pec.eval.jx.model.McdFiveGEffectEvaluate;
import com.asiainfo.biapp.pec.eval.jx.req.G5EvalPageQuery;
import com.asiainfo.biapp.pec.eval.jx.service.McdFiveGEffectEvaluateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 5G效果评估表 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2022-12-15
 */
@Service
public class McdFiveGEffectEvaluateServiceImpl extends ServiceImpl<McdFiveGEffectEvaluateMapper, McdFiveGEffectEvaluate> implements McdFiveGEffectEvaluateService {

    @Resource
    private McdFiveGEffectEvaluateMapper mapper;

    @Override
    public IPage<McdFiveGEffectEvaluate> queryPage(G5EvalPageQuery query) {
        IPage<McdFiveGEffectEvaluate> pager = new Page<>();
        pager.setCurrent(query.getCurrent());
        pager.setSize(query.getSize());
        // 非汇总数据
        if (!"0".equals(query.getCityId()) && !"0".equals(query.getCountyId()) && !"0".equals(query.getFallbackConfig())) {
            QueryWrapper<McdFiveGEffectEvaluate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(StrUtil.isNotEmpty(query.getCityId()) && !"-1".equals(query.getCityId()), McdFiveGEffectEvaluate::getCityId, query.getCityId())
                    .eq(StrUtil.isNotEmpty(query.getCountyId()) && !"-1".equals(query.getCountyId()), McdFiveGEffectEvaluate::getCountyId, query.getCountyId())
                    .eq(StrUtil.isNotEmpty(query.getFallbackType()) && !"-1".equals(query.getFallbackType()), McdFiveGEffectEvaluate::getFallbackType, query.getFallbackType())
                    .eq(StrUtil.isNotEmpty(query.getFallbackConfig()) &&  !"-1".equals(query.getFallbackConfig()),McdFiveGEffectEvaluate::getSourcetype,query.getFallbackConfig())
                    .and(StrUtil.isNotEmpty(query.getKeyWords()),r -> {
                        r.like(StrUtil.isNotEmpty(query.getKeyWords()), McdFiveGEffectEvaluate::getCampsegRootId, query.getKeyWords()).or().like(StrUtil.isNotEmpty(query.getKeyWords()), McdFiveGEffectEvaluate::getCampsegName, query.getKeyWords());
                    });
            return this.page(pager, queryWrapper);
        }
        IPage<McdFiveGEffectEvaluate> iPage = null;
        try {
            iPage = mapper.queryPage(pager, query);
        } catch (Exception e) {
            log.error("", e);
        }
        return iPage;
    }

    @Override
    public List<List<String>> export(G5EvalPageQuery query) {
        IPage<McdFiveGEffectEvaluate> iPage = queryPage(query);

        List<String> list1;
        List<List<String>> list2 = new ArrayList<>();
        for (McdFiveGEffectEvaluate record : iPage.getRecords()) {
            list1 = new ArrayList<>();
            list1.add(record.getStartDate());
            list1.add(record.getEndDate());
            list1.add(record.getCampsegRootId());
            list1.add(record.getCampsegName());
            list1.add(record.getApplicationNum());
            list1.add(record.getPlanName());
            list1.add(record.getCityName());
            list1.add(record.getCountyName());
            // 群发总人数
            list1.add(String.valueOf(record.getSgmtnum()));
            // 5G消息送达用户数
            list1.add(String.valueOf(record.getContactNum()));
            // 送达成功率
            list1.add(String.valueOf(record.getContactRate()));
            // 点击用户数
            list1.add(String.valueOf(record.getSuccessNum()));
            // 5G消息点击率
            list1.add(String.valueOf(record.getSuccessRate()));
            // 总成功订购数
            list1.add(String.valueOf(record.getSuccessOrderNum()));
            // 总成功订购率
            list1.add(String.valueOf(record.getSuccessOrderRate()));
            // 有无回落
            list1.add(String.valueOf(record.getSourcetype()));
            // 回落类型
            list1.add(String.valueOf(record.getFallbackType()));
            // 回落消息送达次数
            list1.add(String.valueOf(record.getFallbackSuccNum()));
            // 回落消息送达成功率
            list1.add(String.valueOf(record.getSuccessOrderRate()));
            // 回落消息送达人数
            list1.add(String.valueOf(record.getFallbackSuccPeopleNum()));
            list2.add(list1);
        }
        return list2;
    }
}
