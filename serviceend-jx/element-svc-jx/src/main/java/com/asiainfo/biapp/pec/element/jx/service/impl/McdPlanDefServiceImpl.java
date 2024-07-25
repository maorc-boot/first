package com.asiainfo.biapp.pec.element.jx.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.asiainfo.biapp.pec.element.dao.McdCampChannelListDao;
import com.asiainfo.biapp.pec.element.jx.mapper.McdPlanDefMapper;
import com.asiainfo.biapp.pec.element.jx.mapper.McdPlanExclusivityMapper;
import com.asiainfo.biapp.pec.element.jx.model.McdPlanDef;
import com.asiainfo.biapp.pec.element.jx.model.McdPlanDefInfo;
import com.asiainfo.biapp.pec.element.jx.query.PlanManageJxQuery;
import com.asiainfo.biapp.pec.element.jx.query.RequestPlanQuery;
import com.asiainfo.biapp.pec.element.jx.service.McdPlanDefService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2022-11-08
 */
@Service("mcdPlanDefServiceImplJx")
public class McdPlanDefServiceImpl extends ServiceImpl<McdPlanDefMapper, McdPlanDef> implements McdPlanDefService {



    @Resource
    private McdPlanDefMapper mcdPlanDefMapper;

    @Resource
    private McdCampChannelListDao mcdCampChannelListDao;
    @Resource
    private McdPlanExclusivityMapper planExclusivityMapper;

    @Override
    public IPage<McdPlanDef> queryPlanDefPageList(PlanManageJxQuery planQuery) {
        if (ObjectUtil.isEmpty(planQuery.getIsSupportAi())) {
            planQuery.setIsSupportAi(0);
        }

        Page<McdPlanDef> page = new Page<>(planQuery.getCurrent(), planQuery.getSize());
        IPage<McdPlanDef> planDefPage = page(page, Wrappers.<McdPlanDef>query().lambda()
                .eq(StringUtils.isNotBlank(planQuery.getStatusId()), McdPlanDef::getStatus, planQuery.getStatusId())
                .eq(StringUtils.isNotBlank(planQuery.getPlanTypeId()), McdPlanDef::getPlanType, planQuery.getPlanTypeId())
                .eq(StringUtils.isNotBlank(planQuery.getPlanSrvType()), McdPlanDef::getPlanSrvType, planQuery.getPlanSrvType())
                .eq(StringUtils.isNotBlank(planQuery.getProdClassBusi()), McdPlanDef::getProdClassBusi, planQuery.getProdClassBusi())
                .eq(StringUtils.isNotBlank(planQuery.getItemType()), McdPlanDef::getItemType, planQuery.getItemType())
                .eq(StringUtils.isNotBlank(planQuery.getOfferType()), McdPlanDef::getOfferType, planQuery.getOfferType())
                .eq(1 == planQuery.getIsSupportAi(), McdPlanDef::getIsSupportAi, 1)
                .gt(McdPlanDef::getPlanEndDate, new Date())
                .and(
                        StringUtils.isNotBlank(planQuery.getKeyWords()), i -> i
                                .like(StringUtils.isNotBlank(planQuery.getKeyWords()), McdPlanDef::getPlanId, planQuery.getKeyWords())
                                .or().like(StringUtils.isNotBlank(planQuery.getKeyWords()), McdPlanDef::getPlanName, planQuery.getKeyWords())
                ).orderByDesc(McdPlanDef::getCreateDate)
        );
      /*  Map<String, String> map = new HashMap<>();
        final List<McdPlanLabel> list = mcdPlanLabelService.list();
        list.stream().forEach(l -> {
            map.put(l.getLabelId(), l.getLabelValue());
        });

        planDefPage.getRecords().stream().forEach(r -> {
            r.setProdClassBusiName(map.get(r.getProdClassBusi()));
            r.setOfferTypeName(map.get(r.getOfferType()));
            r.setItemTypeName(map.get(r.getItemType()));
        });*/

        return planDefPage;

    }

    @Override
    public McdPlanDef getPlanDetailById(String planId) {

        QueryWrapper<McdPlanDef> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(McdPlanDef::getPlanId, planId);
        final McdPlanDef one = getOne(queryWrapper);
       /* if (null == one) {
            return null;
        }
        QueryWrapper<McdPlanLabel> qw = new QueryWrapper();
        qw.in("LABEL_ID", one.getProdClassBusi(), one.getItemType(), one.getOfferType());
        List<McdPlanLabel> mcdPlanLabels = mcdPlanLabelService.list(qw);
        mcdPlanLabels.stream().forEach(label -> {
            if (String.valueOf(one.getProdClassBusi()).equals(label.getLabelId())) {
                one.setProdClassBusiName(label.getLabelValue());
                return;
            }
            if (String.valueOf(one.getItemType()).equals(label.getLabelId())) {
                one.setItemTypeName(label.getLabelValue());
                return;
            }
            if (String.valueOf(one.getOfferType()).equals(label.getLabelId())) {
                one.setOfferTypeName(label.getLabelValue());
                return;
            }
        });*/
        return one;
    }


    @Override
    public IPage<McdPlanDefInfo> queryPlanDefList(RequestPlanQuery planQuery) {

        Page page = new Page();
        page.setSize(planQuery.getSize());
        page.setCurrent(planQuery.getCurrent());

        IPage<McdPlanDefInfo> mcdPlanDefIPage = mcdPlanDefMapper.queryPlanDefList(page,planQuery);


        return mcdPlanDefIPage;
    }
}
