package com.asiainfo.biapp.pec.eval.jx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.eval.jx.dao.McdFiveGCloudEffectEvaluateMapper;
import com.asiainfo.biapp.pec.eval.jx.enums.CallbackTypeEnum;
import com.asiainfo.biapp.pec.eval.jx.model.McdFiveGCloudEffectEvaluate;
import com.asiainfo.biapp.pec.eval.jx.req.G5CloudEvalPageQuery;
import com.asiainfo.biapp.pec.eval.jx.service.McdFiveGCloudEffectEvaluateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 5G云卡效果评估service服务实现类
 *
 * @author lvcc
 * @date 2023/10/12
 */
@Service
public class McdFiveGCloudEffectEvaluateServiceImpl extends ServiceImpl<McdFiveGCloudEffectEvaluateMapper, McdFiveGCloudEffectEvaluate> implements McdFiveGCloudEffectEvaluateService {

    @Resource
    private McdFiveGCloudEffectEvaluateMapper mapper;

    @Override
    public IPage<McdFiveGCloudEffectEvaluate> queryPage(G5CloudEvalPageQuery query) {
        IPage<McdFiveGCloudEffectEvaluate> pager = new Page<>();
        pager.setCurrent(query.getCurrent());
        pager.setSize(query.getSize());
        // 非汇总数据 明细== -1 汇总== 0
        if (!"0".equals(query.getCityId()) && !"0".equals(query.getCountyId()) && !"0".equals(query.getCallbackType())) {
            QueryWrapper<McdFiveGCloudEffectEvaluate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(StrUtil.isNotEmpty(query.getCityId()) && !"-1".equals(query.getCityId()), McdFiveGCloudEffectEvaluate::getCityId, query.getCityId())
                    .eq(StrUtil.isNotEmpty(query.getCountyId()) && !"-1".equals(query.getCountyId()), McdFiveGCloudEffectEvaluate::getCountyId, query.getCountyId())
                    .eq(StrUtil.isNotEmpty(query.getCallbackType()) && !"-1".equals(query.getCallbackType()), McdFiveGCloudEffectEvaluate::getCallbackType, query.getCallbackType())
                    .and(StrUtil.isNotEmpty(query.getKeyWords()), r -> {
                        r.like(StrUtil.isNotEmpty(query.getKeyWords()), McdFiveGCloudEffectEvaluate::getCampsegRootId, query.getKeyWords())
                         .or()
                         .like(StrUtil.isNotEmpty(query.getKeyWords()), McdFiveGCloudEffectEvaluate::getCampsegName, query.getKeyWords());
                    });
            IPage<McdFiveGCloudEffectEvaluate> page = this.page(pager, queryWrapper);
            // 转换回调类型名称返回
            pager.getRecords().forEach(record -> {
                record.setCallbackType(CallbackTypeEnum.valueOfId(Integer.parseInt(record.getCallbackType())));
            });
            return page;
        }
        return mapper.queryPage(pager, query);
    }

    /**
     * 导出
     *
     * @param query 入参对象
     * @return List<List<String>>
     */
    @Override
    public List<List<String>> getExport5gCloudEvalList(G5CloudEvalPageQuery query) {
        IPage<McdFiveGCloudEffectEvaluate> mcdFiveGCloudEffectEvaluateIPage = queryPage(query);
        List<List<String>> list2 = new ArrayList<>();
        mcdFiveGCloudEffectEvaluateIPage.getRecords().forEach(record -> {
            List<String> list1 = new ArrayList<>();
            list1.add(record.getStartDate());
            list1.add(record.getEndDate());
            list1.add(record.getCampsegRootId());
            list1.add(record.getCampsegName());
            list1.add(record.getPlanName());
            list1.add(record.getCityName());
            list1.add(record.getCountyName());
            if ("-1".equals(query.getCallbackType())) {
                list1.add(record.getCallbackType());
            }
            list1.add(String.valueOf(record.getCustNum()));
            list1.add(String.valueOf(record.getSendNum()));
            list1.add(String.valueOf(record.getSuccessNum()));
            list1.add(String.valueOf(record.getSuccessRate()));
            list2.add(list1);
        });
        return list2;
    }
}
