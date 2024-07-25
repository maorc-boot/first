package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.CampStatus;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.common.CampDefType;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampBaseInfoJxVO;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.ApprRecordJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CmpApproveProcessRecordJx;
import com.asiainfo.biapp.pec.plan.model.*;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import com.asiainfo.biapp.pec.plan.vo.CampBusinessInfo;
import com.asiainfo.biapp.pec.plan.vo.CustgroupDetailVO;
import com.asiainfo.biapp.pec.plan.vo.req.*;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialCharacter.COMMA;
import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialCharacter.SEPARATOR_LINE;
import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialCharacter.UNDER_LINE;
import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.*;

/**
 * @author : zhouyang
 * @date : 2021-11-12 16:32:32
 * 活动信息转换
 */
public class CampsegAssemblerJx {

    public final static Map<Integer, BiFunction<TacticsInfoJx, UserSimpleInfo, CampBusinessInfo>> INFO;

    static {
        INFO = new HashMap<>();
        INFO.put(ONE_NUMBER, CampsegAssemblerJx::convertNormalCamp);
        INFO.put(TWO_NUMBER, CampsegAssemblerJx::convertSeparateBoxCamp);
        INFO.put(THREE_NUMBER, CampsegAssemblerJx::convertNormalCamp);
        INFO.put(FOUR_NUMBER, CampsegAssemblerJx::convertNormalCamp);
        //政企页面创建
        INFO.put(SIX_NUMBER, CampsegAssemblerJx::convertNormalCamp);
    }

    public static McdCampDef convertToCampModel(CampBaseInfoVO baseCampInfo, UserSimpleInfo user) {
        McdCampDef campDef = new McdCampDef();
        BeanUtil.copyProperties(baseCampInfo, campDef);
        if (null != user) {
            campDef.setCreateTime(new Date()).setCreateUserId(user.getUserId()).setCityId(user.getCityId());
        }
        if (null == baseCampInfo.getCampsegStatId()) {
            baseCampInfo.setCampsegStatId(CampStatus.DRAFT.getId());
        }
        return campDef.setCampsegRootId(baseCampInfo.getCampsegRootId())
                .setCampsegId(baseCampInfo.getCampsegId())
                .setCampsegName(baseCampInfo.getCampsegName())
                .setCampsegDesc(StrUtil.isEmpty(baseCampInfo.getCampScene()) ? baseCampInfo.getCampsegDesc() : baseCampInfo.getCampsegDesc() + ";" + baseCampInfo.getCampScene())
                .setCampsegStatId(baseCampInfo.getCampsegStatId())
                .setCampsegTypeId(baseCampInfo.getCampsegTypeId())
                .setMarketingType(baseCampInfo.getMarketingType())
                .setActivityTemplateId(baseCampInfo.getActivityTemplateId())
                .setActivityType(baseCampInfo.getActivityType())
                .setActivityObjective(baseCampInfo.getActivityObjective())
                .setTopicId(baseCampInfo.getTopicId())
                .setActivityServiceType(baseCampInfo.getActivityServiceType())
                .setCampBusinessType(baseCampInfo.getCampBusinessType())
                .setUnityCampsegId(baseCampInfo.getUnityCampsegId())
                .setOptionSign(baseCampInfo.getOptionSign())
                .setCampDefType(null == baseCampInfo.getCampDefType() ? CampDefType.IOP.getType() : baseCampInfo.getCampDefType())
                .setStartDate(baseCampInfo.getStartDate())
                .setEndDate(baseCampInfo.getEndDate())
                .setPreviewCamp(baseCampInfo.getPreviewCamp())
                .setTacticsMap(baseCampInfo.getTacticsMap())
                .setCampScene(baseCampInfo.getCampScene())
                .setAITaskId(baseCampInfo.getTaskIdByAI())
                .setCampCreateType(baseCampInfo.getCampCreateType());
    }


    public static McdCampChannelList convertToChannelModel(String campsegId, String campsegPid, String campsegRootId,
                                                           String custId, String childCustId, Integer status,
                                                           String planId, String channelId, String adivId, Integer updateCycle,
                                                           CampChannelConfQuery channelInfo, int campClass, Integer custgroupSource) {
        return new McdCampChannelList().setCampsegId(campsegId)
                .setCustgroupSource(custgroupSource)
                .setCampsegPid(campsegPid)
                .setCampsegRootId(campsegRootId)
                .setPCustgroupId(custId)
                .setCustgroupId(childCustId)
                .setPlanId(planId)
                .setChannelId(channelId)
                .setChannelAdivId(adivId)
                .setCampClass(campClass)
                .setWavesCampPid(StringUtils.isBlank(channelInfo.getWavesCampPid()) ? null : channelInfo.getWavesCampPid())
                .setWavesChannelPid(StringUtils.isBlank(channelInfo.getWavesChannelPid()) ? null : channelInfo.getWavesChannelPid())
                .setWavesRuleId(StringUtils.isBlank(channelInfo.getWavesRuleId()) ? null : channelInfo.getWavesRuleId())
                .setWavesNo(StringUtils.isBlank(channelInfo.getWavesNo()) ? null : channelInfo.getWavesNo())
                .setBoxNo(channelInfo.getBoxNo())
                .setBoxRuleExp(channelInfo.getBoxRuleExp())
                .setCustgroupExp(channelInfo.getCustgroupExp())
                .setIsChannelPredilection(channelInfo.getIsChannelPredilection())
                .setStatus(status)
                .setReason(Strings.EMPTY)
                .setIfHaveVar(channelInfo.getIfHaveVar())
                .setExecContent(channelInfo.getExecContent())
                .setHandleUrl(channelInfo.getHandleUrl())
                .setCepEventId(channelInfo.getCepEventId())
                .setEventParamJson(channelInfo.getEventParamJson())
                .setCepSceneName(channelInfo.getCepSceneName())
                .setMarketingTerm(channelInfo.getMarkingTerm())
                .setPccId(channelInfo.getPccId())
                .setTimeId(channelInfo.getTimeId())
                .setTimeDistindes(channelInfo.getTimeDistindes())
                .setFrequency(channelInfo.getFrequency())
                .setUpdateCycle(updateCycle)
                .setSmsContent(channelInfo.getSmsContent())
                .setStreamEventId(channelInfo.getStreamEventId())
                .setMaterialDisplayUrl(channelInfo.getMaterialDisplayUrl())
                .setMaterialClickUrl(channelInfo.getMaterialClickUrl())
                .setPriorityOrder(channelInfo.getPriorityOrder())
                .setExecuteTimeMonth(channelInfo.getExecuteTimeMonth())
                .setExecuteTimeWeek(channelInfo.getExecuteTimeWeek())
                .setExecuteTimeDay(channelInfo.getExecuteTimeDay())
                .setStartDate(channelInfo.getStartDate())
                .setEndDate(channelInfo.getEndDate());
    }

    @SneakyThrows
    public static McdCampChannelExt convertToChannelExtModel(String campsegId, CampChannelExtQuery channelExtReq) {
        McdCampChannelExt mcdCampChannelExt = new McdCampChannelExt();
        mcdCampChannelExt.setCampsegId(campsegId);

        Field[] sourceDeclaredFields = channelExtReq.getClass().getDeclaredFields();
        Field[] targetDeclaredFields = mcdCampChannelExt.getClass().getDeclaredFields();
        for (int i = ZERO_NUMBER; i < sourceDeclaredFields.length; i++) {
            Field sourceDeclaredField = sourceDeclaredFields[i];
            Field targetDeclaredField = targetDeclaredFields[i+TWO_NUMBER];
            sourceDeclaredField.setAccessible(true);
            targetDeclaredField.setAccessible(true);
            Object obj = sourceDeclaredField.get(channelExtReq);
//            if(null != obj){
//                Assert.isTrue(obj.toString().length()<=1024, "字段：{}，输入入字符长度超长", sourceDeclaredField.getName());
//            }
            targetDeclaredField.set(mcdCampChannelExt, obj);
        }
        return mcdCampChannelExt;
    }

    /**
     * 保存活动的入参分类组装
     *
     * @param req
     * @return
     */
    public static CampBusinessInfo convertToInfo(TacticsInfoJx req, UserSimpleInfo user, String campsegRootId) {
        final CampBaseInfoVO baseCampInfo = req.getBaseCampInfo();
        if (StringUtils.isBlank(campsegRootId)) {
            baseCampInfo.setCampsegId(IdUtils.generateId());
            baseCampInfo.setCampsegRootId(ZERO_STRING);
        }
        return INFO.get(req.getType()).apply(req, user);
    }

    /**
     * 处理普通活动信息
     */
    public static CampBusinessInfo convertNormalCamp(TacticsInfoJx req, UserSimpleInfo user) {
        List<McdCampDef> camp = new ArrayList<>();
        List<McdCampChannelList> list = new ArrayList<>();
        List<McdCampChannelExt> ext = new ArrayList<>();
        List<McdCustgroupDef> extCustGroup = new ArrayList<>();
        CampBusinessInfo businessInfo = new CampBusinessInfo();
        businessInfo.setExtCustGroup(extCustGroup);
        businessInfo.setMcdCampDefs(camp);
        businessInfo.setCampChannelExts(ext);
        businessInfo.setMcdCampChannelLists(list);

        final CampBaseInfoJxVO baseCampInfo = req.getBaseCampInfo();
        baseCampInfo.setCampCreateType(req.getType());
        camp.add(convertToCampModel(baseCampInfo, user));

        //所有子活动
        final String campsegRootId = baseCampInfo.getCampsegId();
        final List<CampScheme> campSchemes = req.getCampSchemes();
        //关系映射
        Map<String, CampChannelConfQuery> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        Map<String, String> map3 = new HashMap<>();
        //处理子活动
        handlerCampSchemes(camp, campsegRootId, campSchemes, user, map1, map2, map3);
        //退回的活动，未提交审批之前永远是退回状态
        Integer status = baseCampInfo.getCampsegStatId();
        //处理子策略
        handlerChildrenScheme(businessInfo, req.getChildrenSchemes(), campsegRootId, status, map1, map2, map3, baseCampInfo.getCustgroupSource(), baseCampInfo.getDnaUpdateCycle());

        return businessInfo;
    }

    /**
     * 处理多波次活动信息
     */
    public static void handlerCampSchemes(List<McdCampDef> camp, String campsegRootId, List<CampScheme> campSchemes, UserSimpleInfo user,
                                          Map<String, CampChannelConfQuery> map1, Map<String, Integer> map2,
                                          Map<String, String> map3) {
        campSchemes.forEach(campScheme -> {
            final CampBaseInfoVO baseCampInfo = campScheme.getBaseCampInfo();
            String wavePid = Strings.EMPTY;
            //渠道配置信息关联
            final List<ChannelInfo> channels = campScheme.getChannels();
            for (ChannelInfo channel : channels) {
                final String channelId = channel.getChannelId();
                final McdDimAdivInfo adivInfo = channel.getAdivInfo();
                String key = channelId + SEPARATOR_LINE + adivInfo.getAdivId();
                final CampChannelConfQuery channelConf = channel.getChannelConf();
                wavePid = channelConf.getWavesChannelPid();
                if (StringUtils.isNotBlank(wavePid)) {
                    if (null == channelConf.getStartDate() && null == channelConf.getEndDate()) {
                        //说明是波次,没有配置开始结束时间，计算时间
                        final int startDays = Integer.parseInt(channelConf.getWavesRuleId()
                                .split(UNDER_LINE)[ONE_NUMBER]);
                        final McdCampDef campDef = camp.get(camp.size() - ONE_NUMBER);
                        final Date startDate = campDef.getStartDate();
                        final Calendar calendar = DateUtil.calendar(startDate);
                        calendar.add(Calendar.DATE, startDays);
                        final Date channelStartDate = DateUtil.date(calendar).toJdkDate();
                        final Date endDate = campDef.getEndDate();
                        channelConf.setStartDate(channelStartDate).setEndDate(endDate);
                    }
                    Assert.isTrue(channelConf.getStartDate().before(channelConf.getEndDate()),
                            "请合理配置活动投放周期，目前波次执行时间晚于活动结束时间");
                }
                if (StringUtils.isNotBlank(channelConf.getWavesRuleId())) {
                    key = key + SEPARATOR_LINE + channelConf.getWavesRuleId().split(UNDER_LINE)[ZERO_NUMBER];
                }
                map1.put(key + (StringUtils.isNotBlank(wavePid) ? (UNDER_LINE + wavePid) : Strings.EMPTY), channelConf);
            }

            if (null != baseCampInfo) {
                baseCampInfo.setCampsegRootId(campsegRootId);
                baseCampInfo.setCampCreateType(camp.get(ZERO_NUMBER).getCampCreateType());
                if (StringUtils.isBlank(baseCampInfo.getCampsegId())) {
                    baseCampInfo.setCampsegId(IdUtils.generateId());
                }
                map3.put(campScheme.getCampGroup(), baseCampInfo.getCampsegId());
                final McdCampDef campDef = convertToCampModel(baseCampInfo, user);
                camp.add(campDef);
            } else {
                if (StringUtils.isBlank(wavePid)) {
                    //兼容没有传子活动信息的情况，直接用主活动信息
                    final McdCampDef newCampDef = new McdCampDef();
                    BeanUtil.copyProperties(camp.get(ZERO_NUMBER), newCampDef);
                    //组成新的子活动信息
                    newCampDef.setCampsegRootId(campsegRootId);
                    newCampDef.setCampsegId(IdUtils.generateId());
                    map3.put(campScheme.getCampGroup(), newCampDef.getCampsegId());
                    camp.add(newCampDef);
                }
            }

            //客群周期关联
            final List<CustgroupDetailVO> customers = campScheme.getCustomer();
            if (CollectionUtils.isNotEmpty(customers)) {
                for (CustgroupDetailVO customer : customers) {
                    map2.put(customer.getCustomGroupId(), customer.getUpdateCycle());
                }
            }
        });
    }

    public static void handlerChildrenScheme(CampBusinessInfo businessInfo, List<CampChildrenScheme> childrenSchemes,
                                             String campsegRootId, Integer status, Map<String, CampChannelConfQuery> map1,
                                             Map<String, Integer> map2, Map<String, String> map3, Integer custgroupSource, Integer dnaUpdateCycle) {
        Map<String, String> map4 = new HashMap<>();
        //待需要创建的子策略
        childrenSchemes.forEach(campScheme -> {
            final List<String> data = campScheme.getData();
            final String planId = data.get(ZERO_NUMBER);
            String customGroupId = data.get(ONE_NUMBER);
            final String channelId = data.get(TWO_NUMBER);
            final String adivId = data.get(THREE_NUMBER);
            String campsegId = campScheme.getCampsegId();
            if (StringUtils.isBlank(campsegId)) {
                campsegId = IdUtils.generateId();
            }
            String key = channelId + SEPARATOR_LINE + adivId;
            final String wavePid = campScheme.getWavePid();
            String wavesCampPid = null;
            Integer updateCycle = map2.get(customGroupId);
            int campClass = ONE_NUMBER;
            String childCustId = customGroupId;
            if (StringUtils.isNotBlank(wavePid)) {
                key = key + SEPARATOR_LINE + campScheme.getWavesRuleId().split(UNDER_LINE)[ZERO_NUMBER] + UNDER_LINE + wavePid;
                //属于波次策略,处理波次客户群
                final McdCustgroupDef custgroupDef = handlerCust(customGroupId, THREE_STRING);
                businessInfo.getExtCustGroup().add(custgroupDef);
                childCustId = custgroupDef.getCustomGroupId();
                campClass = THREE_NUMBER;
                wavesCampPid = map4.get(wavePid);
            }
            map4.put(key, campsegId);
            final CampChannelConfQuery confQuery = map1.get(key);
            confQuery.setWavesCampPid(wavesCampPid);
            // 转化updateCycle值
            // log.info("dnaUpdateCycle={}", dnaUpdateCycle);
            if (null != dnaUpdateCycle) { // 不为空，说明活动选择的是dna的客群   为空执行周期值则为客群的updateCycle
                updateCycle = dnaUpdateCycle; // 执行周期字段值需要保存界面选择的执行周期值
            }
            // log.info("updateCycle={}", updateCycle);
            businessInfo.getMcdCampChannelLists().add(convertToChannelModel(campsegId, map3.get(campScheme.getCampGroup()), campsegRootId, customGroupId,
                    childCustId, status, planId, channelId, adivId, updateCycle, confQuery, campClass, custgroupSource));
            if (null != confQuery.getChannelExtConf()) {
                businessInfo.getCampChannelExts().add(convertToChannelExtModel(campsegId, confQuery.getChannelExtConf()));
            }
        });
    }

    public static McdCustgroupDef handlerCust(String customGroupId, String type) {
        McdCustgroupDef custgroupDef = new McdCustgroupDef();
        //初始化客户群基础信息 状态为1(有效)
        custgroupDef.setCustomStatusId(ONE_NUMBER);
        custgroupDef.setCustType(ZERO_NUMBER);
        custgroupDef.setSyncTimes(ONE_NUMBER);
        final Date date = new Date();
        custgroupDef.setCreateTime(date);
        custgroupDef.setCustomNum(ZERO_NUMBER);
        custgroupDef.setUpdateCycle(ONE_NUMBER);
        custgroupDef.setEffectiveTime(date);
        custgroupDef.setActualCustomNum(ZERO_NUMBER);
        final Integer dataDate = Integer.valueOf(DateUtil.format(date, DatePattern.PURE_DATE_PATTERN));
        custgroupDef.setDataDate(dataDate);
        custgroupDef.setCustomSourceId(type);
        if (FOUR_STRING.equals(type)) {
            //分箱客户群
            custgroupDef.setCustomGroupName("分箱客户群");
            custgroupDef.setCustomGroupDesc("分箱创建客户群");
            custgroupDef.setRuleDesc("分箱客户群详情");
            return custgroupDef;
        }
        //波次客户群
        custgroupDef.setCustomGroupName("波次客户群");
        custgroupDef.setCustomGroupDesc("波次创建客户群");
        custgroupDef.setRuleDesc("波次客户群详情");
        custgroupDef.setCustomGroupId("wave_" + customGroupId + UNDER_LINE
                + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_MS_PATTERN));
        return custgroupDef;
    }

    /**
     * 处理分箱活动信息
     */
    public static CampBusinessInfo convertSeparateBoxCamp(TacticsInfoJx req, UserSimpleInfo user) {
        return new CampBusinessInfo();
    }

    public static List<ChannelInfo> convertToChannelInfos(List<McdDimChannel> channels, List<McdDimAdivInfo> adivs,
                                                          List<McdCampChannelList> mcdCampChannelLists,
                                                          List<McdCampChannelExt> campChannelExt,
                                                          List<McdCampWavesRule> campWavesRules) {
        Map<String, McdCampChannelExt> collect = new HashMap<>();
        if (null != campChannelExt) {
            collect = campChannelExt.stream().collect(Collectors.toMap(McdCampChannelExt::getCampsegId, Function.identity()));
        }
        //渠道+运营位
        Map<String, McdCampChannelList> map1 = new HashMap<>();
        //渠道+运营位
        Map<String, McdCampChannelExt> map2 = new HashMap<>();

        for (McdCampChannelList mcdCampChannelList : mcdCampChannelLists) {
            final McdCampChannelExt mcdCampChannelExt = collect.get(mcdCampChannelList.getCampsegId());
            final String channelId = mcdCampChannelList.getChannelId();
            final String channelAdivId = mcdCampChannelList.getChannelAdivId();
            final String key = channelId + SEPARATOR_LINE + channelAdivId;
            map1.put(key, mcdCampChannelList);
            map2.put(key, mcdCampChannelExt);
        }

        Map<String, String> collect1 = null;
        if (null != campWavesRules) {
            collect1 = campWavesRules.stream()
                    .collect(Collectors.toMap(t -> t.getChannelCode() + UNDER_LINE + t.getRuleId(),
                            McdCampWavesRule::getStatusName));
        }
        final Map<String, List<McdDimAdivInfo>> adivMap = adivs.stream().collect(Collectors.groupingBy(McdDimAdivInfo::getChannelId));
        List<ChannelInfo> channelInfos = new ArrayList<>();
        for (McdDimChannel channel : channels) {
            final String channelId = channel.getChannelId();
            final List<McdDimAdivInfo> mcdDimAdivInfos = adivMap.get(channelId);
            for (McdDimAdivInfo mcdDimAdivInfo : mcdDimAdivInfos) {
                final String adivId = mcdDimAdivInfo.getAdivId();
                //配置项
                String key = channelId + SEPARATOR_LINE + adivId;
                final McdCampChannelList mcdCampChannelList = map1.get(key);
                final McdCampChannelExt mcdCampChannelExt = map2.get(key);
                final String wavesNo = mcdCampChannelList.getWavesNo();
                String descStr = Strings.EMPTY;
                if (StringUtils.isNotBlank(wavesNo) && wavesNo.contains(UNDER_LINE)) {
                    final String[] split = wavesNo.split(COMMA);
                    String[] desc = new String[split.length];
                    for (int i = ZERO_NUMBER; i < split.length; i++) {
                        final String[] ruleIds = split[i].split(UNDER_LINE);
                        desc[i] = collect1.get(channelId + UNDER_LINE + ruleIds[ZERO_NUMBER]);
                    }
                    descStr = String.join(COMMA, desc);
                }
                channelInfos.add(convertToChannelInfo(channel, mcdDimAdivInfo, mcdCampChannelList, mcdCampChannelExt, descStr));
            }
        }
        return channelInfos;
    }

    public static ChannelInfo convertToChannelInfo(McdDimChannel channel, McdDimAdivInfo mcdDimAdivInfo,
                                                   McdCampChannelList mcdCampChannelList, McdCampChannelExt mcdCampChannelExt,
                                                   String waveNoDesc) {
        //运营位
        final ChannelInfo channelInfo = convertToChannelInfo(channel);
        channelInfo.setAdivInfo(mcdDimAdivInfo);
        //波次todo
        //配置项
        channelInfo.setChannelConf(convertToCampChannelConf(mcdCampChannelList, mcdCampChannelExt, waveNoDesc));
        return channelInfo;
    }

    public static ChannelInfo convertToChannelInfo(McdDimChannel channel) {
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setChannelId(channel.getChannelId());
        channelInfo.setParentId(channel.getParentId());
        channelInfo.setChannelName(channel.getChannelName());
        channelInfo.setChannelCode(channel.getChannelCode());
        channelInfo.setChannelType(channel.getChannelType());
        channelInfo.setCreateUser(channel.getCreateUser());
        channelInfo.setCreateTime(channel.getCreateTime());
        channelInfo.setDisplayOrder(channel.getDisplayOrder());
        channelInfo.setOnlineStatus(channel.getOnlineStatus());
        channelInfo.setIopPrefixCode(channel.getIopPrefixCode());
        channelInfo.setIopChannelName(channel.getIopChannelName());
        channelInfo.setIopChannelType(channel.getIopChannelType());
        channelInfo.setIopDescription(channel.getIopDescription());
        channelInfo.setIopOperator(channel.getIopOperator());
        channelInfo.setIopChannelCode(channel.getIopChannelCode());
        channelInfo.setChannelPredilectionName(channel.getChannelPredilectionName());
        return channelInfo;
    }

    public static CampChannelConfQuery convertToCampChannelConf(McdCampChannelList campChannelList, McdCampChannelExt ext, String waveNoDesc) {

        final CampChannelConfQuery confQuery = new CampChannelConfQuery();
        if (null != ext) {
            confQuery.setChannelExtConf(convertToCampChannelExt(ext));
        }
        return confQuery.setIfHaveVar(campChannelList.getIfHaveVar())
                .setExecContent(campChannelList.getExecContent())
                .setSmsContent(campChannelList.getSmsContent())
//                .setMarkingTerm(campChannelList.getMarkingTerm())
                .setHandleUrl(campChannelList.getHandleUrl())
                .setCepEventId(campChannelList.getCepEventId())
//                .setOriginCepEventId(campChannelList.getOriginCepEventId())
                .setEventParamJson(campChannelList.getEventParamJson())
                .setCepSceneName(campChannelList.getCepSceneName())
                .setUpdateCycle(campChannelList.getUpdateCycle())
                .setPccId(campChannelList.getPccId())
                .setTimeId(campChannelList.getTimeId())
//                .setTimeName(campChannelList.getTimeName())
                .setTimeDistindes(campChannelList.getTimeDistindes())
                .setFrequency(campChannelList.getFrequency())
                .setUpdateCycleName(1 == campChannelList.getUpdateCycle() ? "一次性" : "周期性")
//                .setFrequencyDays(campChannelList.getFrequencyDays())
//                .setFrequencyTimes(campChannelList.getFrequencyTimes())
                .setStreamEventId(campChannelList.getStreamEventId())
                .setMaterialDisplayUrl(campChannelList.getMaterialDisplayUrl())
                .setMaterialClickUrl(campChannelList.getMaterialClickUrl())
                .setPriorityOrder(campChannelList.getPriorityOrder())
                .setIsChannelPredilection(campChannelList.getIsChannelPredilection())
                .setExecuteTimeMonth(campChannelList.getExecuteTimeMonth())
                .setExecuteTimeWeek(campChannelList.getExecuteTimeWeek())
                .setExecuteTimeDay(campChannelList.getExecuteTimeDay())
                .setWavesCampPid(campChannelList.getWavesCampPid())
                .setWavesChannelPid(campChannelList.getWavesChannelPid())
                .setWavesRuleId(campChannelList.getWavesRuleId())
                .setWavesNo(campChannelList.getWavesNo())
                .setWavesNoDesc(waveNoDesc)
                .setBoxNo(campChannelList.getBoxNo())
                .setBoxRuleExp(campChannelList.getBoxRuleExp())
                .setCustgroupExp(campChannelList.getCustgroupExp())
                .setStartDate(campChannelList.getStartDate())
                .setEndDate(campChannelList.getEndDate());
    }

    public static CampChannelExtQuery convertToCampChannelExt(McdCampChannelExt campChannelExt) {
        final CampChannelExtQuery extQuery = new CampChannelExtQuery();
        if(null == campChannelExt){
            return extQuery;
        }
        return extQuery.setColumnExt1(campChannelExt.getColumnExt1())
                .setColumnExt2(campChannelExt.getColumnExt2())
                .setColumnExt3(campChannelExt.getColumnExt3())
                .setColumnExt4(campChannelExt.getColumnExt4())
                .setColumnExt5(campChannelExt.getColumnExt5())
                .setColumnExt6(campChannelExt.getColumnExt6())
                .setColumnExt7(campChannelExt.getColumnExt7())
                .setColumnExt8(campChannelExt.getColumnExt8())
                .setColumnExt9(campChannelExt.getColumnExt9())
                .setColumnExt10(campChannelExt.getColumnExt10())
                .setColumnExt11(campChannelExt.getColumnExt11())
                .setColumnExt12(campChannelExt.getColumnExt12())
                .setColumnExt13(campChannelExt.getColumnExt13())
                .setColumnExt14(campChannelExt.getColumnExt14())
                .setColumnExt15(campChannelExt.getColumnExt15())
                .setColumnExt16(campChannelExt.getColumnExt16())
                .setColumnExt17(campChannelExt.getColumnExt17())
                .setColumnExt18(campChannelExt.getColumnExt18())
                .setColumnExt19(campChannelExt.getColumnExt19())
                .setColumnExt20(campChannelExt.getColumnExt20());
    }


    public static List<CampChildrenScheme> convertToChildrenScheme(List<McdCampChannelList> campChannelLists, String campGroup) {
        return campChannelLists.stream().map(campChannel -> {
            final String campsegId = campChannel.getCampsegId();
            CampChildrenScheme campChildrenScheme = new CampChildrenScheme();
            campChildrenScheme.setCampsegId(campsegId);
            List<String> data = new ArrayList<>();
            data.add(campChannel.getPlanId());
            data.add(campChannel.getPCustgroupId());
            data.add(campChannel.getChannelId());
            data.add(campChannel.getChannelAdivId());
            campChildrenScheme.setData(data);
            campChildrenScheme.setReason(campChannel.getReason());
            campChildrenScheme.setPriorityOrder(campChannel.getPriorityOrder());
            campChildrenScheme.setStatus(campChannel.getReason());
            campChildrenScheme.setCampGroup(campGroup);
            campChildrenScheme.setWavePid(campChannel.getWavesChannelPid());
            campChildrenScheme.setWavesRuleId(campChannel.getWavesRuleId());
            return campChildrenScheme;
        }).collect(Collectors.toList());
    }

    public static ApprRecordJx convertToAppr(CmpApproveProcessRecordJx record, ApprRecordJx campDef) {
        final ApprRecordJx apprRecord = new ApprRecordJx();
        apprRecord.setCampsegId(campDef.getCampsegId());
        apprRecord.setCampsegName(campDef.getCampsegName());
        apprRecord.setCreateUserId(campDef.getCreateUserId());
        apprRecord.setUserName(campDef.getUserName());
        apprRecord.setCreateTime(campDef.getCreateTime());
        apprRecord.setType(campDef.getType());
        apprRecord.setId(record.getId());
        apprRecord.setBusinessId(record.getBusinessId());
        apprRecord.setInstanceId(record.getInstanceId());
        apprRecord.setNodeId(record.getNodeId());
        apprRecord.setNodeName(record.getNodeName());
        apprRecord.setNodeType(record.getNodeType());
        apprRecord.setNodeBusinessName(record.getNodeBusinessName());
        apprRecord.setApprover(record.getApprover());
        apprRecord.setApproverName(record.getApproverName());
        apprRecord.setDealOpinion(record.getDealOpinion());
        apprRecord.setDealStatus(record.getDealStatus());
        apprRecord.setDealTime(record.getDealTime());
        apprRecord.setPreRecordId(record.getPreRecordId());
        apprRecord.setEventId(record.getEventId());
        apprRecord.setCreateDate(record.getCreateDate());
        apprRecord.setCreateBy(record.getCreateBy());
        apprRecord.setModifyDate(record.getModifyDate());
        apprRecord.setModifyBy(record.getModifyBy());
        return apprRecord;
    }

}




