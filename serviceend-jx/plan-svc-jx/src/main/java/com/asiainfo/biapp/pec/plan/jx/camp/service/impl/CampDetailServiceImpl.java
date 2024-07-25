package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.client.app.element.vo.PlanDefVO;
import com.asiainfo.biapp.pec.plan.assembler.CampsegAssembler;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.dao.McdCampWavesRuleDao;
import com.asiainfo.biapp.pec.plan.dao.McdCustgroupDefDao;
import com.asiainfo.biapp.pec.plan.dao.McdDimAdivInfoDao;
import com.asiainfo.biapp.pec.plan.dao.McdDimChannelDao;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.ExclusivePlanDao;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.FusionPlanDao;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampsegJxDao;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.SeriesPlanDao;
import com.asiainfo.biapp.pec.plan.jx.camp.enums.CampStatusJx;
import com.asiainfo.biapp.pec.plan.jx.camp.model.CampExecInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.model.ExclusivePlan;
import com.asiainfo.biapp.pec.plan.jx.camp.model.FusionPlan;
import com.asiainfo.biapp.pec.plan.jx.camp.model.SeriesPlan;
import com.asiainfo.biapp.pec.plan.jx.camp.req.*;
import com.asiainfo.biapp.pec.plan.jx.camp.service.CampDetailService;
import com.asiainfo.biapp.pec.plan.jx.dna.service.IDNACustomGroupService;
import com.asiainfo.biapp.pec.plan.model.*;
import com.asiainfo.biapp.pec.plan.service.*;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import com.asiainfo.biapp.pec.plan.vo.CustgroupDetailVO;
import com.asiainfo.biapp.pec.plan.vo.req.CampChildrenScheme;
import com.asiainfo.biapp.pec.plan.vo.req.CampScheme;
import com.asiainfo.biapp.pec.plan.vo.req.ChannelInfo;
import com.asiainfo.biapp.pec.plan.vo.req.TacticsInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.THREE_NUMBER;

/**
 * @author mamp
 * @date 2022/11/11
 */
@Service
@Slf4j
public class CampDetailServiceImpl implements CampDetailService {

    @Value("${grouphalls.channel:923}")
    private String eleChannel;
    @Resource
    private IMcdCampChannelListService channelListService;

    @Resource
    private IMcdDimChannelService channelService;

    @Resource
    private IMcdCampsegService mcdCampsegService;


    @Resource
    private IMcdCampChannelExtService extService;

    @Resource
    private IMcdPlanDefService planDefService;

    @Resource
    private IMcdCampChannelListService campChannelListService;


    @Resource
    private McdCustgroupDefDao custgroupDefDao;

    @Resource
    private McdDimChannelDao channelDao;

    @Resource
    private McdDimAdivInfoDao adivInfoDao;

    @Resource
    private McdCampWavesRuleDao campWavesRuleDao;

    @Resource
    private SeriesPlanDao seriesPlanDao;

    @Resource
    private FusionPlanDao fusionPlanDao;

    @Resource
    private ExclusivePlanDao exclusivePlanDao;

    @Resource
    private McdCampsegJxDao mcdCampsegJxDao;

    @Autowired
    private IDNACustomGroupService idnaCustomGroupService;

    /**
     * 查询活动详情
     *
     * @param id 活动RootID
     * @return
     */
    @Override
    public TacticsInfoJx getCampsegInfo(String id) {
        TacticsInfoJx result = new TacticsInfoJx();
        CampBaseInfoJxVO campBaseInfoJxVO = mcdCampsegJxDao.getCampsegBaseInfo(id);
        // 有分号表示场景信息不为空 需要截取原有的描述信息
        if (campBaseInfoJxVO.getCampsegDesc().contains(";")) {
            campBaseInfoJxVO.setCampsegDesc(campBaseInfoJxVO.getCampsegDesc().substring(0, campBaseInfoJxVO.getCampsegDesc().indexOf(";")));
        }
        result.setBaseCampInfo(campBaseInfoJxVO);
        //返回前端策划关系
        List<CampBaseInfoJxVO> mcdCampDefs = new ArrayList<>();
        if (Constant.SpecialNumber.ZERO_STRING.equals(campBaseInfoJxVO.getCampsegRootId())) {
            mcdCampDefs = mcdCampsegJxDao.getCampsegBaseInfoByRootId(id);
        } else {
            mcdCampDefs.add(campBaseInfoJxVO);
        }
        List<CampChildrenScheme> childrenSchemes = new ArrayList<>();
        List<CampScheme> campSchemes = new ArrayList<>();
        int campGroup = 0;
        for (CampBaseInfoVO campDef : mcdCampDefs) {
            String campsegPid = campDef.getCampsegId();

            //子活动的子策略和波次策略，需要拆分成多个CampScheme
            final List<McdCampChannelList> mcdCampChannelLists = campChannelListService.listMcdCampChannelListByCampsegPid(campsegPid);
            final List<McdCampChannelExt> campChannelExt = extService.qryByCampsegPid(campsegPid);

            //批量获取客户群信息
            final List<CustgroupDetailVO> custGroupInfoByCampsegPid = custgroupDefDao.getCustGroupInfoByCampsegPid(campsegPid);
            //批量获取渠道信息
            final List<McdDimChannel> listByCampsegPid = channelDao.getListByCampsegPid(campsegPid);
            //批量获取运营位信息
            final List<McdDimAdivInfo> listByCampsegPid1 = eleChannel.equals(CollectionUtils.isEmpty(listByCampsegPid) ? "" : listByCampsegPid.get(0).getChannelId()) ? adivInfoDao.getEleListByCampsegPid(campsegPid) : adivInfoDao.getListByCampsegPid(campsegPid);

            //批量获取产品信息
            List<PlanDefVO> planInfoByCampsegPid = Lists.newArrayList();//planDefDao.getPlanInfoByBatchIds(productIds);

            CustgroupDetailVO custgroupDetailVOTar = new CustgroupDetailVO();
            boolean isWaveCamp = false;
            for (McdCampChannelList mcdCampChannelList : mcdCampChannelLists) {
                //解析产品id，适应售前售中售后
                planInfoByCampsegPid.addAll(planDefService.getDetailByPlanId(mcdCampChannelList.getPlanId(), null));
                if (!isWaveCamp && Integer.valueOf(THREE_NUMBER).equals(mcdCampChannelList.getCampClass())) {
                    isWaveCamp = true;
                }
            }
            planInfoByCampsegPid = mapPlan(planInfoByCampsegPid);

            if (isWaveCamp) {
                //波次规则信息
                final List<McdCampWavesRule> mcdCampWavesRules = campWavesRuleDao.listByCampsegPid(campsegPid);
                final Map<String, List<McdCampWavesRule>> collect5 = mcdCampWavesRules.stream().collect(Collectors.groupingBy(McdCampWavesRule::getChannelCode));
                final Map<String, List<McdCampChannelExt>> collect = campChannelExt.stream().collect(Collectors.groupingBy(McdCampChannelExt::getCampsegId));
                final Map<String, List<CustgroupDetailVO>> collect1 = custGroupInfoByCampsegPid.stream().collect(Collectors.groupingBy(CustgroupDetailVO::getCustomGroupId));
                final Map<String, List<PlanDefVO>> collect2 = planInfoByCampsegPid.stream().collect(Collectors.groupingBy(PlanDefVO::getPlanId));
                final Map<String, List<McdDimChannel>> collect3 = listByCampsegPid.stream().collect(Collectors.groupingBy(McdDimChannel::getChannelId));
                final Map<String, List<McdDimAdivInfo>> collect4 = listByCampsegPid1.stream().collect(Collectors.groupingBy(McdDimAdivInfo::getAdivId));

                //子策略一个客群一个产品一个渠道，波次策略一个客群一个产品一个渠道
                for (McdCampChannelList mcdCampChannelList : mcdCampChannelLists) {
                    CampScheme campScheme = new CampScheme();
                    campSchemes.add(campScheme);
                    campScheme.setCampGroup(Strings.EMPTY + campGroup);
                    if (Integer.valueOf(Constant.SpecialNumber.ONE_STRING).equals(mcdCampChannelList.getCampClass())) {
                        campScheme.setBaseCampInfo(campDef);
                    }
                    final List<McdCampChannelList> curList = Lists.newArrayList(mcdCampChannelList);
                    final List<ChannelInfo> channelInfos = CampsegAssembler.convertToChannelInfos(collect3.get(mcdCampChannelList.getChannelId()),
                            collect4.get(mcdCampChannelList.getChannelAdivId()),
                            curList, collect.get(mcdCampChannelList.getCampsegId()), collect5.get(mcdCampChannelList.getChannelId()));
                    campScheme.setChannels(channelInfos);

                    campScheme.setProduct(collect2.get(mcdCampChannelList.getPlanId()));
                    // dna的客群信息在保存活动的时候已经保存到了客群定义表MCD_CUSTGROUP_DEF 直接查表就行
                    campScheme.setCustomer(collect1.get(mcdCampChannelList.getPCustgroupId()));
                    // if (Constant.SpecialNumber.ZERO_NUMBER == mcdCampChannelList.getCustgroupSource()) {
                        // coc客群信息获取
                    // } else {
                    //     // 构造DNA客户群详情对象
                    //     buildCustDetailVo(mcdCampChannelLists.get(Constant.SpecialNumber.ZERO_NUMBER), custgroupDetailVOTar);
                    //     campScheme.setCustomer(Collections.singletonList(custgroupDetailVOTar));
                    // }
                    childrenSchemes.addAll(CampsegAssemblerJx.convertToChildrenScheme(curList, campScheme.getCampGroup()));

                }
            } else {
                CampScheme campScheme = new CampScheme();
                campScheme.setBaseCampInfo(campDef);
                campSchemes.add(campScheme);
                campScheme.setCampGroup(Strings.EMPTY + campGroup);
                final List<ChannelInfo> channelInfos = CampsegAssembler.convertToChannelInfos(listByCampsegPid, listByCampsegPid1,
                        mcdCampChannelLists, campChannelExt, null);
                campScheme.setChannels(channelInfos);
                campScheme.setProduct(planInfoByCampsegPid);
                campScheme.setCustomer(custGroupInfoByCampsegPid);
                // for (McdCampChannelList mcdCampChannelList : mcdCampChannelLists) {
                //     // coc客群信息获取
                //     if (Constant.SpecialNumber.ZERO_NUMBER == mcdCampChannelList.getCustgroupSource()) {
                //     } else {
                //         // 构造DNA客户群详情对象
                //         buildCustDetailVo(mcdCampChannelLists.get(Constant.SpecialNumber.ZERO_NUMBER), custgroupDetailVOTar);
                //         campScheme.setCustomer(Collections.singletonList(custgroupDetailVOTar));
                //     }
                // }
                childrenSchemes.addAll(CampsegAssemblerJx.convertToChildrenScheme(mcdCampChannelLists, campScheme.getCampGroup()));
            }
            campGroup++;
        }
        result.setType(campBaseInfoJxVO.getCampCreateType());
        result.setChildrenSchemes(childrenSchemes);
        result.setCampSchemes(campSchemes);
        // 产品扩展信息
        setPlanExtInfos(result);
        return result;
    }

    @Override
    public TacticsBaseInfoJx getCampsegBaseInfo(String id) {
        TacticsBaseInfoJx result = new TacticsBaseInfoJx();
        CampBaseInfoJxVO campBaseInfoJxVO = mcdCampsegJxDao.getCampsegBaseInfo(id);
        result.setBaseCampInfo(campBaseInfoJxVO);
        //返回前端策划关系
        List<CampBaseInfoJxVO> mcdCampDefs = new ArrayList<>();
        if (Constant.SpecialNumber.ZERO_STRING.equals(campBaseInfoJxVO.getCampsegRootId())) {
            mcdCampDefs = mcdCampsegJxDao.getCampsegBaseInfoByRootId(id);
        } else {
            mcdCampDefs.add(campBaseInfoJxVO);
        }
        List<CampChildrenScheme> childrenSchemes = new ArrayList<>();
        List<CampScheme> campSchemes = new ArrayList<>();
        int campGroup = 0;
        for (CampBaseInfoVO campDef : mcdCampDefs) {
            String campsegPid = campDef.getCampsegId();

            //子活动的子策略和波次策略，需要拆分成多个CampScheme
            final List<McdCampChannelList> mcdCampChannelLists = campChannelListService.listMcdCampChannelListByCampsegPid(campsegPid);
            final List<McdCampChannelExt> campChannelExt = extService.qryByCampsegPid(campsegPid);

            //批量获取客户群信息
            final List<CustgroupDetailVO> custGroupInfoByCampsegPid = custgroupDefDao.getCustGroupInfoByCampsegPid(campsegPid);
            //批量获取渠道信息
            final List<McdDimChannel> listByCampsegPid = channelDao.getListByCampsegPid(campsegPid);
            //批量获取运营位信息
            final List<McdDimAdivInfo> listByCampsegPid1 = eleChannel.equals(CollectionUtils.isEmpty(listByCampsegPid) ? "" : listByCampsegPid.get(0).getChannelId()) ? adivInfoDao.getEleListByCampsegPid(campsegPid) : adivInfoDao.getListByCampsegPid(campsegPid);

            //批量获取产品信息
            List<PlanDefVO> planInfoByCampsegPid = Lists.newArrayList();//planDefDao.getPlanInfoByBatchIds(productIds);

            boolean isWaveCamp = false;
            for (McdCampChannelList mcdCampChannelList : mcdCampChannelLists) {
                //解析产品id，适应售前售中售后
                planInfoByCampsegPid.addAll(planDefService.getDetailByPlanId(mcdCampChannelList.getPlanId(), null));
                if (!isWaveCamp && Integer.valueOf(THREE_NUMBER).equals(mcdCampChannelList.getCampClass())) {
                    isWaveCamp = true;
                }
            }
            planInfoByCampsegPid = mapPlan(planInfoByCampsegPid);

            if (isWaveCamp) {
                //波次规则信息
                final List<McdCampWavesRule> mcdCampWavesRules = campWavesRuleDao.listByCampsegPid(campsegPid);
                final Map<String, List<McdCampWavesRule>> collect5 = mcdCampWavesRules.stream().collect(Collectors.groupingBy(McdCampWavesRule::getChannelCode));
                final Map<String, List<McdCampChannelExt>> collect = campChannelExt.stream().collect(Collectors.groupingBy(McdCampChannelExt::getCampsegId));
                final Map<String, List<CustgroupDetailVO>> collect1 = custGroupInfoByCampsegPid.stream().collect(Collectors.groupingBy(CustgroupDetailVO::getCustomGroupId));
                final Map<String, List<PlanDefVO>> collect2 = planInfoByCampsegPid.stream().collect(Collectors.groupingBy(PlanDefVO::getPlanId));
                final Map<String, List<McdDimChannel>> collect3 = listByCampsegPid.stream().collect(Collectors.groupingBy(McdDimChannel::getChannelId));
                final Map<String, List<McdDimAdivInfo>> collect4 = listByCampsegPid1.stream().collect(Collectors.groupingBy(McdDimAdivInfo::getAdivId));

                //子策略一个客群一个产品一个渠道，波次策略一个客群一个产品一个渠道
                for (McdCampChannelList mcdCampChannelList : mcdCampChannelLists) {
                    CampScheme campScheme = new CampScheme();
                    campSchemes.add(campScheme);
                    campScheme.setCampGroup(Strings.EMPTY + campGroup);
                    if (Integer.valueOf(Constant.SpecialNumber.ONE_STRING).equals(mcdCampChannelList.getCampClass())) {
                        campScheme.setBaseCampInfo(campDef);
                    }
                    final List<McdCampChannelList> curList = Lists.newArrayList(mcdCampChannelList);
                    final List<ChannelInfo> channelInfos = CampsegAssembler.convertToChannelInfos(collect3.get(mcdCampChannelList.getChannelId()),
                            collect4.get(mcdCampChannelList.getChannelAdivId()),
                            curList, collect.get(mcdCampChannelList.getCampsegId()), collect5.get(mcdCampChannelList.getChannelId()));
                    campScheme.setChannels(channelInfos);

                    campScheme.setProduct(collect2.get(mcdCampChannelList.getPlanId()));
                    campScheme.setCustomer(collect1.get(mcdCampChannelList.getPCustgroupId()));
                    childrenSchemes.addAll(CampsegAssembler.convertToChildrenScheme(curList, campScheme.getCampGroup()));

                }
            } else {
                CampScheme campScheme = new CampScheme();
                campScheme.setBaseCampInfo(campDef);
                campSchemes.add(campScheme);
                campScheme.setCampGroup(Strings.EMPTY + campGroup);
                final List<ChannelInfo> channelInfos = CampsegAssembler.convertToChannelInfos(listByCampsegPid, listByCampsegPid1,
                        mcdCampChannelLists, campChannelExt, null);
                campScheme.setChannels(channelInfos);
                campScheme.setProduct(planInfoByCampsegPid);
                campScheme.setCustomer(custGroupInfoByCampsegPid);
                childrenSchemes.addAll(CampsegAssembler.convertToChildrenScheme(mcdCampChannelLists, campScheme.getCampGroup()));
            }
            campGroup++;
        }
        result.setChildrenSchemes(childrenSchemes);
        result.setCampSchemes(campSchemes);
        // 产品扩展信息
        setPlanExtOutInfos(result);
        return result;
    }

    /**
     * 获取子活动详情
     *
     * @param campsegId
     * @return
     */
    @Override
    public TacticsInfoJx getChildCamp(String campsegId) {
        TacticsInfo childCamp = mcdCampsegService.getChildCamp(campsegId);
        TacticsInfoJx childCampJx = new TacticsInfoJx();
        BeanUtil.copyProperties(childCamp, childCampJx);
        CampBaseInfoVO baseCampInfo = childCamp.getBaseCampInfo();
        // 转换为江西特有的策略状态
        baseCampInfo.setCampsegStatName(CampStatusJx.valueOfId(baseCampInfo.getCampsegStatId()));
        CampBaseInfoJxVO jxVO = new CampBaseInfoJxVO();
        BeanUtil.copyProperties(baseCampInfo, jxVO);
        childCampJx.setBaseCampInfo(jxVO);
        setPlanExtInfos(childCampJx);
        return childCampJx;
    }

    /**
     * 生成主产品扩展信息
     *
     * @param tacticsInfoJx
     */
    private void setPlanExtInfos(TacticsInfoJx tacticsInfoJx) {

        CampBaseInfoJxVO campBaseInfoJxVO = tacticsInfoJx.getBaseCampInfo();
        List<CampScheme> campSchemes = tacticsInfoJx.getCampSchemes();
        // 主产品ID
        Map<String, PlanDefVO> mainPlanMap = new HashMap();
        campSchemes.stream().forEach(schema -> {
            schema.getProduct().stream().forEach(plan -> {
                mainPlanMap.put(plan.getPlanId(), plan);
            });
        });
        String campRootId = campBaseInfoJxVO.getCampsegRootId();
        if ("0".equals(campRootId)) {
            campRootId = campBaseInfoJxVO.getCampsegId();
        }
        List<PlanExtInfo> planExtInfoList = new ArrayList<>();
        // result.setPlanExtInfoList();
        String finalCampRootId = campRootId;
        mainPlanMap.keySet().stream().forEach(planId -> {
            PlanExtInfo planExtInfo = new PlanExtInfo();
            // 主产品
            planExtInfo.setPlanId(planId);
            planExtInfo.setPlanName(mainPlanMap.get(planId).getPlanName());

            // 同系列产品
            QueryWrapper<SeriesPlan> wrapper = new QueryWrapper();
            wrapper.lambda().eq(SeriesPlan::getCampsegId, finalCampRootId)
                    .eq(SeriesPlan::getPlanId, planId);
            List<SeriesPlan> seriesPlans = seriesPlanDao.selectList(wrapper);
            List<PlanBaseInfo> seriesBaseInfos = new ArrayList<>();
            seriesPlans.stream().forEach(sp -> {
                PlanBaseInfo baseInfo = new PlanBaseInfo();
                baseInfo.setPlanId(sp.getSeriesPlanId());
                baseInfo.setPlanName(sp.getSeriesPlanName());
                seriesBaseInfos.add(baseInfo);
            });
            planExtInfo.setCampSeriesPlan(seriesBaseInfos);

            // 融合产品
            QueryWrapper<FusionPlan> wrapperFusion = new QueryWrapper();
            wrapperFusion.lambda().eq(FusionPlan::getCampsegId, finalCampRootId)
                    .eq(FusionPlan::getPlanId, planId);
            List<FusionPlan> fusionPlans = fusionPlanDao.selectList(wrapperFusion);
            List<PlanBaseInfo> fusionBaseInfos = new ArrayList<>();
            fusionPlans.stream().forEach(fp -> {
                PlanBaseInfo baseInfo = new PlanBaseInfo();
                baseInfo.setPlanId(fp.getFusionPlanId());
                baseInfo.setPlanName(fp.getFusionPlanName());
                fusionBaseInfos.add(baseInfo);
            });
            planExtInfo.setCampFusionPlans(fusionBaseInfos);

            // 互斥产品
            QueryWrapper<ExclusivePlan> wrapperExclue = new QueryWrapper();
            wrapperExclue.lambda().eq(ExclusivePlan::getCampsegId, finalCampRootId)
                    .eq(ExclusivePlan::getPlanId, planId);
            List<ExclusivePlan> exclusivePlans = exclusivePlanDao.selectList(wrapperExclue);
            List<PlanBaseInfo> exclusiveBaseInfos = new ArrayList<>();
            exclusivePlans.stream().forEach(ep -> {
                PlanBaseInfo baseInfo = new PlanBaseInfo();
                baseInfo.setPlanId(ep.getExPlanId());
                baseInfo.setPlanName(ep.getExPlanName());
                exclusiveBaseInfos.add(baseInfo);
            });
            planExtInfo.setCampExclusivePlan(exclusiveBaseInfos);
            planExtInfoList.add(planExtInfo);
        });
        tacticsInfoJx.setPlanExtInfoList(planExtInfoList);
    }


    /**
     * 生成主产品扩展信息
     *
     * @param tacticsInfoJx
     */
    private void setPlanExtOutInfos(TacticsBaseInfoJx tacticsInfoJx) {

        CampBaseInfoJxVO campBaseInfoJxVO = tacticsInfoJx.getBaseCampInfo();
        List<CampScheme> campSchemes = tacticsInfoJx.getCampSchemes();
        // 主产品ID
        Map<String, PlanDefVO> mainPlanMap = new HashMap();
        campSchemes.stream().forEach(schema -> {
            schema.getProduct().stream().forEach(plan -> {
                mainPlanMap.put(plan.getPlanId(), plan);
            });
        });
        String campRootId = campBaseInfoJxVO.getCampsegRootId();
        if ("0".equals(campRootId)) {
            campRootId = campBaseInfoJxVO.getCampsegId();
        }
        List<PlanExtInfo> planExtInfoList = new ArrayList<>();
        // result.setPlanExtInfoList();
        String finalCampRootId = campRootId;
        mainPlanMap.keySet().stream().forEach(planId -> {
            PlanExtInfo planExtInfo = new PlanExtInfo();
            // 主产品
            planExtInfo.setPlanId(planId);
            planExtInfo.setPlanName(mainPlanMap.get(planId).getPlanName());

            // 同系列产品
            QueryWrapper<SeriesPlan> wrapper = new QueryWrapper();
            wrapper.lambda().eq(SeriesPlan::getCampsegId, finalCampRootId)
                    .eq(SeriesPlan::getPlanId, planId);
            List<SeriesPlan> seriesPlans = seriesPlanDao.selectList(wrapper);
            List<PlanBaseInfo> seriesBaseInfos = new ArrayList<>();
            seriesPlans.stream().forEach(sp -> {
                PlanBaseInfo baseInfo = new PlanBaseInfo();
                baseInfo.setPlanId(sp.getSeriesPlanId());
                baseInfo.setPlanName(sp.getSeriesPlanName());
                seriesBaseInfos.add(baseInfo);
            });
            planExtInfo.setCampSeriesPlan(seriesBaseInfos);

            // 融合产品
            QueryWrapper<FusionPlan> wrapperFusion = new QueryWrapper();
            wrapperFusion.lambda().eq(FusionPlan::getCampsegId, finalCampRootId)
                    .eq(FusionPlan::getPlanId, planId);
            List<FusionPlan> fusionPlans = fusionPlanDao.selectList(wrapperFusion);
            List<PlanBaseInfo> fusionBaseInfos = new ArrayList<>();
            fusionPlans.stream().forEach(fp -> {
                PlanBaseInfo baseInfo = new PlanBaseInfo();
                baseInfo.setPlanId(fp.getFusionPlanId());
                baseInfo.setPlanName(fp.getFusionPlanName());
                fusionBaseInfos.add(baseInfo);
            });
            planExtInfo.setCampFusionPlans(fusionBaseInfos);

            // 互斥产品
            QueryWrapper<ExclusivePlan> wrapperExclue = new QueryWrapper();
            wrapperExclue.lambda().eq(ExclusivePlan::getCampsegId, finalCampRootId)
                    .eq(ExclusivePlan::getPlanId, planId);
            List<ExclusivePlan> exclusivePlans = exclusivePlanDao.selectList(wrapperExclue);
            List<PlanBaseInfo> exclusiveBaseInfos = new ArrayList<>();
            exclusivePlans.stream().forEach(ep -> {
                PlanBaseInfo baseInfo = new PlanBaseInfo();
                baseInfo.setPlanId(ep.getExPlanId());
                baseInfo.setPlanName(ep.getExPlanName());
                exclusiveBaseInfos.add(baseInfo);
            });
            planExtInfo.setCampExclusivePlan(exclusiveBaseInfos);
            planExtInfoList.add(planExtInfo);
        });
        tacticsInfoJx.setPlanExtInfoList(planExtInfoList);
    }

    /**
     * 产品去重
     *
     * @param old
     * @return
     */
    private List<PlanDefVO> mapPlan(List<PlanDefVO> old) {
        List<PlanDefVO> result = new ArrayList<>();
        Set<String> planIds = new HashSet<>();
        for (PlanDefVO planDefVO : old) {
            if (planIds.add(planDefVO.getPlanId())) {
                result.add(planDefVO);
            }
        }
        return result;
    }

    /**
     * 查询活动执行详情
     *
     * @param param
     * @return
     */
    @Override
    public List<CampExecInfo> queryPreviewExecLog(CampExecReq param) {
        if (StrUtil.isEmpty(param.getStartDate())) {
            param.setStartDate(DateUtil.format(DateUtil.yesterday(), "yyyyMMdd"));
            param.setEndDate(DateUtil.format(DateUtil.yesterday(), "yyyyMMdd"));
        }
        String starDate = param.getStartDate();
        String endDate = param.getEndDate();
        starDate = starDate + "000000";
        endDate = endDate + "235959";
        param.setStartDate(starDate);
        param.setEndDate(endDate);
        List<CampExecInfo> campExecInfos = mcdCampsegJxDao.queryCampExecInfo(param);
        for (CampExecInfo execInfo : campExecInfos) {
            String dataDate = execInfo.getDataDate();
            if (StrUtil.isEmpty(dataDate)) {
                dataDate = DateUtil.format(DateUtil.yesterday(), "yyyyMMdd");
                execInfo.setDataDate(dataDate);
            }
        }
        return campExecInfos;
    }

    /**
     * 构造DNA客户群详情对象
     *
     * @param campChannelList campChannelList
     * @param custgroupDetailVOTar 目标对象
     */
    private void buildCustDetailVo(McdCampChannelList campChannelList, CustgroupDetailVO custgroupDetailVOTar) {
        // 调用DNA接口获取客群详情信息
        com.asiainfo.biapp.pec.plan.jx.camp.vo.dnacustomgroup.CustgroupDetailVO custgroupDetailVOSou = idnaCustomGroupService.detailCustgroup(campChannelList.getPCustgroupId());
        // 将DNA客群详情VO转换为目标客群VO
        BeanUtil.copyProperties(custgroupDetailVOSou, custgroupDetailVOTar);
    }
}
