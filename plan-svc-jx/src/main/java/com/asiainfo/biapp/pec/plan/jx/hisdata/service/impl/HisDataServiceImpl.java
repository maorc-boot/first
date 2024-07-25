package com.asiainfo.biapp.pec.plan.jx.hisdata.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.model.*;
import com.asiainfo.biapp.pec.plan.jx.camp.service.*;
import com.asiainfo.biapp.pec.plan.jx.hisdata.dao.McdCampExtMapMapper;
import com.asiainfo.biapp.pec.plan.jx.hisdata.model.CmpApproveProcessRecordHis;
import com.asiainfo.biapp.pec.plan.jx.hisdata.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.jx.hisdata.model.McdCampExtMap;
import com.asiainfo.biapp.pec.plan.jx.hisdata.service.CmpApproveProcessRecordHisService;
import com.asiainfo.biapp.pec.plan.jx.hisdata.service.HisDataService;
import com.asiainfo.biapp.pec.plan.jx.hisdata.service.McdCampExtMapService;
import com.asiainfo.biapp.pec.plan.jx.hisdata.service.feign.McdHisDataFeignClient;
import com.asiainfo.biapp.pec.plan.model.McdCampChannelExt;
import com.asiainfo.biapp.pec.plan.model.McdCampTask;
import com.asiainfo.biapp.pec.plan.model.McdCampTaskDate;
import com.asiainfo.biapp.pec.plan.service.IMcdCampChannelExtService;
import com.asiainfo.biapp.pec.plan.service.IMcdCampTaskDateService;
import com.asiainfo.biapp.pec.plan.service.IMcdCampTaskService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mamp
 * @date 2023/5/20
 */
@Service
@Slf4j
public class HisDataServiceImpl implements HisDataService {


    @Resource
    private McdHisDataFeignClient mcdHisDataFeignClient;

    @Resource
    private IMcdCampDefJxService mcdCampDefJxService;

    @Resource
    private IMcdCampChannelListJxService mcdCampChannelListJxService;

    @Resource
    private IMcdCampChannelExtService mcdCampChannelExtService;

    @Resource
    private IMcdCampTaskService mcdCampTaskService;

    @Resource
    private IMcdCampTaskDateService campTaskDateService;

    @Resource
    private McdCampExtMapService extMapService;

    @Resource
    private CmpApproveProcessRecordHisService recordHisService;

    @Autowired
    private ExclusivePlanService exclusivePlanService;

    @Autowired
    private FusionPlanService fusionPlanService;

    @Autowired
    private SeriesPlanService seriesPlanService;

    @Resource
    private McdFqcCycleService fqcCycleService;

    @Resource
    private McdFqcRuleService fqcRuleService;

    @Resource
    private McdCampExtMapMapper mcdCampExtMapMapper;

    @Override
    public boolean handleCamp(String stat, String channelId, String campsegId) {

        List<Map<String, Object>> channelList = mcdHisDataFeignClient.getChannelList(stat, channelId);
        if (StrUtil.isNotEmpty(campsegId)) {
            channelList = channelList.stream().filter(c -> campsegId.equals(c.get("CAMPSEG_ID"))).collect(Collectors.toList());
        }
        log.info("渠道:{},stat:{},将要移动的活动有{}个", stat, channelId, channelList.size());
        for (Map<String, Object> map : channelList) {
            String campId = map.get("CAMPSEG_ID").toString();
            try {
                createCamp(channelId, map);
            } catch (Exception e) {
                log.error("活动:{},channelId:{}迁移异常", campId, channelId, e);
            }
        }
        return false;
    }

    @Override
    public boolean handleApproveRecord(String approveFlowId) {
        try {
            List<Map<String, Object>> list = mcdHisDataFeignClient.selectApproveRecored(approveFlowId);
            if (CollectionUtil.isNotEmpty(list)) {
                UpdateWrapper<CmpApproveProcessRecordHis> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda().eq(CmpApproveProcessRecordHis::getInstanceId, approveFlowId);
                // 防止数据重复先删除旧数据
                recordHisService.remove(updateWrapper);
            }
            List<CmpApproveProcessRecordHis> hisList = new ArrayList<>();
            for (Map<String, Object> objectMap : list) {
                CmpApproveProcessRecordHis recordHis = BeanUtil.mapToBean(objectMap, CmpApproveProcessRecordHis.class, true, null);
                recordHis.setApprover(getStr("APPROVER", objectMap));
                hisList.add(recordHis);
            }
            if (CollectionUtil.isNotEmpty(hisList)) {
                recordHisService.saveBatch(hisList);
                log.error("approveFlowId:{}审批流程迁移成功,共{}条", approveFlowId, hisList.size());
            }
        } catch (Exception e) {
            log.error("approveFlowId:{}审批流程迁移失败", approveFlowId, e);
            return false;
        }
        return true;
    }


    @Transactional
    public void createCamp(String channelId, Map<String, Object> map) {
        // 子活动ID
        String campsegId = map.get("CAMPSEG_ID").toString();
        LambdaQueryWrapper<McdCampChannelListJx> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(McdCampChannelListJx::getCampsegId, campsegId + channelId);
        int count1 = mcdCampChannelListJxService.count(queryWrapper1);
        if (count1 > 0) {
            log.warn("活动:{}已经存在，先删除迁移", campsegId + channelId);
            deleteOldData(campsegId, channelId);
        }
        log.info("开始迁移活动:{},渠道:{}", channelId, campsegId);
        String campsegPid = campsegId;
        String campsegRootId = map.get("CAMPSEG_PID").toString();
        String custGroupId = map.get("CUSTGROUP_ID").toString();

        // 查询 4.0 mcd_camp_def 表数据
        List<Map<String, Object>> defs = mcdHisDataFeignClient.getDef(campsegId);
        // 产品ID
        String planId = defs.get(0).get("PLAN_ID").toString();

        // 7.0  mcd_camp_def 表数据如果已经存在就不再插入,说明这个活动的其它渠道已经有迁移过
        List<McdCampDef> campDefJxes = new ArrayList<>();
        LambdaQueryWrapper<McdCampDef> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdCampDef::getCampsegId, campsegRootId);
        int count = mcdCampDefJxService.count(queryWrapper);
        if (count <= 0) {
            // 构建7.0mcd_camp_def表数据
            for (Map<String, Object> def : defs) {
                McdCampDef def1 = buildDef(def, campsegPid);
                if (null != def1) {
                    campDefJxes.add(def1);
                }
            }
        }
        // 构建 7.0 channel_list表数据
        McdCampChannelListJx channelListJx = buildChannelListJx(map, campsegRootId, campsegPid, planId, custGroupId);
        // 构建 7.0 活动扩展表数据
        McdCampChannelExt channelExt = buildChannelExt(map, campsegId, channelId);

        // 保存定义表
        if (CollectionUtil.isNotEmpty(campDefJxes)) {
            for (McdCampDef defJx : campDefJxes) {
                mcdCampDefJxService.save(defJx);
            }
        }
        // 保存channe_ist表数据
        mcdCampChannelListJxService.save(channelListJx);

        String frequency = channelListJx.getFrequency();
        if (StrUtil.isNotEmpty(frequency)) {
            String[] fqcItem = frequency.split("_");
            if (fqcItem.length >= 2) {
                McdFqcRule mcdFqcRule = new McdFqcRule();
                mcdFqcRule.setCampsegId(channelListJx.getCampsegId());
                mcdFqcRule.setChannelId(channelListJx.getChannelId());
                mcdFqcRule.setFrequency(Integer.valueOf(fqcItem[1]));
                mcdFqcRule.setCycleId(fqcItem[0]);
                mcdFqcRule.setRuleType("3");
                fqcRuleService.save(mcdFqcRule);
            } else {
                log.error("频次数据格式错误,campsegId:{},channelId:{}", channelListJx.getCampsegId(), channelListJx.getChannelId());
            }

        }
        // 保存活动扩展表数据
        mcdCampChannelExtService.save(channelExt);
        // 如果任务表有数据，保存 task 和 task_date表数据
        Map<String, Object> campTask = mcdHisDataFeignClient.getCampTask(campsegId, channelId);
        if (CollectionUtil.isNotEmpty(campTask)) {
            McdCampTask mcdCampTask = buildCampTaskExt(campTask, campsegId, channelId);
            Map<String, Object> campTaskDate = mcdHisDataFeignClient.getCampTaskDate(mcdCampTask.getTaskId());
            McdCampTaskDate taskDate = buildCampTaskDate(campTaskDate, map);
            mcdCampTaskService.save(mcdCampTask);
            campTaskDateService.save(taskDate);
        }
        //融合产品
        Map<String, Object> campFixPlan = mcdHisDataFeignClient.getCampFixPlan(campsegId);
        if (CollectionUtil.isNotEmpty(campFixPlan)) {
            FusionPlan fusionPlan = buildCampFixExt(campFixPlan, campsegId, channelId);
            fusionPlanService.save(fusionPlan);
        }
        //同系列产品
        Map<String, Object> campSeriesPlan = mcdHisDataFeignClient.getCampSeriesPlan(campsegId);
        if (CollectionUtil.isNotEmpty(campSeriesPlan)) {
            SeriesPlan seriesPlan = buildCampSeriesExt(campSeriesPlan, campsegId, channelId);
            seriesPlanService.save(seriesPlan);
        }
        //互斥产品
        Map<String, Object> campExclusivePlan = mcdHisDataFeignClient.getCampExclusivePlan(campsegId);
        if (CollectionUtil.isNotEmpty(campSeriesPlan)) {
            ExclusivePlan exclusivePlan = buildCampExclusiveExt(campExclusivePlan, campsegId, channelId);
            exclusivePlanService.save(exclusivePlan);
        }

        // 审批记录
        buildApproveRecoredAndSave(map);
        log.info("迁移活动:{},渠道:{}成功", channelId, campsegId);
    }

    /**
     * 构建活动定义表数据
     *
     * @param def
     * @param campsegPid
     * @return
     */
    private McdCampDef buildDef(Map<String, Object> def, String campsegPid) {
        String campsegId = def.get("CAMPSEG_ID").toString();
        String campsegPId = def.get("CAMPSEG_PID").toString();
        McdCampDef campDefJx = new McdCampDef();
        campDefJx.setCampDefType(1);
        if ("0".equals(campsegPId)) {
            campDefJx.setCampsegId(campsegId);
            campDefJx.setCampsegRootId("0");
        } else {
            campDefJx.setCampsegRootId(campsegPId);
            campDefJx.setCampsegId(campsegPid);
        }
        // 活动名称
        campDefJx.setCampsegName(getStr("CAMPSEG_NAME", def));
        // 活动描述
        campDefJx.setCampsegDesc(getStr("CAMPSEG_DESC", def));
        // 开始时间
        campDefJx.setStartDate(getDate("START_DATE", def, " 00:00:00"));
        // 结束时间
        campDefJx.setEndDate(getDate("END_DATE", def, " 23:59:59"));
        // 活动状态
        campDefJx.setCampsegStatId(getInt("CAMPSEG_STAT_ID", def));
        // 策略类型 营销类和策略类
        campDefJx.setCampsegTypeId(getInt("CAMPSEG_TYPE_ID", def));
        //活动类型
        campDefJx.setActivityType(getStr("ACTIVITY_TYPE", def));
        // 活动目的
        campDefJx.setActivityObjective(getStr("ACTIVITY_OBJECTIVE", def));
        // 营销模板
        campDefJx.setActivityTemplateId(getStr("ACTIVITY_TEMPLATE_ID", def));
        // 创建人
        campDefJx.setCreateUserId(getStr("CREATE_USERID", def));
        // 创建时间
        campDefJx.setCreateTime(getDate("CREATE_TIME", def, ""));
        // 归属地市
        campDefJx.setCityId(getStr("CITY_ID", def));
        // 预演
        campDefJx.setPreviewCamp("preview".equals(getStr("CAMP_PREVIEW", def)) ? "1" : "0");
        // 审批流ID
        campDefJx.setApproveFlowId(getStr("APPROVE_FLOW_ID", def));
        // 策略地图
        campDefJx.setTacticsMap("[{\"group\":[\"1\",\"2\",\"3\"],\"children\":[]}]");
        // 敏感客群
        campDefJx.setSensitiveCustIds(getStr("EXCLUSION_GROUP_ID",def));


        return campDefJx;
    }

    /**
     * 构建 channel_list表数据
     *
     * @param map
     * @param rootId
     * @param pid
     * @param planId
     * @param custId
     * @return
     */
    private McdCampChannelListJx buildChannelListJx(Map<String, Object> map, String rootId, String pid, String planId, String custId) {
        McdCampChannelListJx channelListJx = new McdCampChannelListJx();
        // 活动rootID
        channelListJx.setCampsegRootId(rootId);
        // 活动Pid
        channelListJx.setCampsegPid(pid);
        // （子）活动ID
        channelListJx.setCampsegId(getStr("CAMPSEG_ID", map) + getStr("CHANNEL_ID", map));
        // 渠道ID
        channelListJx.setChannelId(getStr("CHANNEL_ID", map));
        // 运营位ID
        channelListJx.setChannelAdivId(channelListJx.getChannelId() + getStr("CHANNEL_ADIV_ID", map));
        // 产品ID
        channelListJx.setPlanId(planId);
        // 客户群ID
        channelListJx.setCustgroupId(custId);
        // 推荐用语
        channelListJx.setExecContent(getStr("EXEC_CONTENT", map));
        // 事件ID
        channelListJx.setCepEventId(getStr("EVENT_PARAM_JSON", map));
        // 事件名称
        channelListJx.setCepSceneName(getStr("CEP_SCENE_NAME", map));
        // ParamJson
        channelListJx.setEventParamJson(getStr("CEP_EVENT_ID", map));
        // PccId
        channelListJx.setPccId(getStr("PCC_ID", map));
        // 频次
        if (StrUtil.isNotEmpty(getStr("PARAM_DAYS", map)) && StrUtil.isNotEmpty(getStr("PARAM_NUM", map))) {
            channelListJx.setFrequency(getStr("PARAM_DAYS", map) + "_" + getStr("PARAM_NUM", map));
        }
        // 是否周期执行
        channelListJx.setUpdateCycle("1".equals(getStr("UPDATE_CYCLE", map)));
        // 短信用语
        channelListJx.setSmsContent(getStr("SEND_SMS", map));
        // 客户群ID
        channelListJx.setPCustgroupId(custId);
        channelListJx.setCampClass(1);
        channelListJx.setIsOutCallPlans(0);
        channelListJx.setIsSeparateBox(0);
        channelListJx.setIfHaveVar(getInt("IF_HAVE_VAR", map) != null && getInt("IF_HAVE_VAR", map) != 0);
        channelListJx.setStatus(getInt("CAMPSEG_STAT_ID", map));
        return channelListJx;

    }

    /**
     * 构建扩展表数据
     *
     * @param map
     * @param campsegId
     * @param channelId
     * @return
     */
    private McdCampChannelExt buildChannelExt(Map<String, Object> map, String campsegId, String channelId) {
        LambdaQueryWrapper<McdCampExtMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdCampExtMap::getChannelId, channelId);
        List<McdCampExtMap> list = extMapService.list(queryWrapper);
        Map<String, String> extMap = new HashMap<>();
        for (McdCampExtMap mcdCampExtMap : list) {
            extMap.put(mcdCampExtMap.getExt7(), mcdCampExtMap.getExt4());
        }
        McdCampChannelExt channelExt = new McdCampChannelExt();
        channelExt.setCampsegId(campsegId + channelId);
        if (StrUtil.isNotEmpty(extMap.get("columnExt1"))) {
            channelExt.setColumnExt1(getStr(extMap.get("columnExt1"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt2"))) {
            channelExt.setColumnExt2(getStr(extMap.get("columnExt2"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt3"))) {
            channelExt.setColumnExt3(getStr(extMap.get("columnExt3"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt4"))) {
            channelExt.setColumnExt4(getStr(extMap.get("columnExt4"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt5"))) {
            channelExt.setColumnExt5(getStr(extMap.get("columnExt5"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt6"))) {
            channelExt.setColumnExt6(getStr(extMap.get("columnExt6"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt7"))) {
            channelExt.setColumnExt7(getStr(extMap.get("columnExt7"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt8"))) {
            channelExt.setColumnExt8(getStr(extMap.get("columnExt8"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt9"))) {
            channelExt.setColumnExt9(getStr(extMap.get("columnExt9"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt10"))) {
            channelExt.setColumnExt10(getStr(extMap.get("columnExt10"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt11"))) {
            channelExt.setColumnExt11(getStr(extMap.get("columnExt11"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt12"))) {
            channelExt.setColumnExt12(getStr(extMap.get("columnExt12"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt13"))) {
            channelExt.setColumnExt13(getStr(extMap.get("columnExt13"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt14"))) {
            channelExt.setColumnExt14(getStr(extMap.get("columnExt14"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt15"))) {
            channelExt.setColumnExt15(getStr(extMap.get("columnExt15"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt16"))) {
            channelExt.setColumnExt16(getStr(extMap.get("columnExt16"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt17"))) {
            channelExt.setColumnExt17(getStr(extMap.get("columnExt17"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt18"))) {
            channelExt.setColumnExt18(getStr(extMap.get("columnExt18"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt19"))) {
            channelExt.setColumnExt19(getStr(extMap.get("columnExt19"), map));
        }
        if (StrUtil.isNotEmpty(extMap.get("columnExt20"))) {
            channelExt.setColumnExt20(getStr(extMap.get("columnExt20"), map));
        }
        return channelExt;
    }

    /**
     * 组装任务表信息
     *
     * @param map
     * @param campsegId
     * @return
     */
    private McdCampTask buildCampTaskExt(Map<String, Object> map, String campsegId, String channelId) {
        McdCampTask mcdCampTask = new McdCampTask();
        mcdCampTask.setTaskId(getStr("TASK_ID", map));
        mcdCampTask.setCampsegId(campsegId + channelId);
        mcdCampTask.setChannelId(getStr("CHANNEL_ID", map));
        mcdCampTask.setChannelAdivId(getStr("CHANNEL_ADIV_ID", map));
        mcdCampTask.setExecStatus(getInt("EXEC_STATUS", map));
        mcdCampTask.setCtrlStatus(getInt("CTRL_STATUS", map) == null ? null : new BigDecimal(getInt("CTRL_STATUS", map)));
        Integer cycleType = getInt("CYCLE_TYPE", map);
        if (null == cycleType) {
            // 默认是0,周期性
            cycleType = 0;
        }
        // 1是一次性，其它都是周期性
        cycleType = cycleType.equals(Integer.valueOf(1)) ? 1 : 0;
        mcdCampTask.setCycleType(new BigDecimal(cycleType));
        mcdCampTask.setTaskStartTime(getDate("TASK_START_TIME", map, ""));
        mcdCampTask.setTaskEndTime(getDate("TASK_END_TIME", map, ""));
        return mcdCampTask;
    }

    /**
     * 组装融合产品信息
     *
     * @param map
     * @param campsegId
     * @return
     */
    private FusionPlan buildCampFixExt(Map<String, Object> map, String campsegId, String channelId) {
        FusionPlan fusionPlan = new FusionPlan();
        fusionPlan.setCampsegId(campsegId + channelId);
        fusionPlan.setPlanId(getStr("PLAN_ID", map));
        fusionPlan.setFusionPlanId(getStr("FIX_PLAN_ID", map));
        return fusionPlan;
    }

    /**
     * 组装同系列产品信息
     *
     * @param map
     * @param campsegId
     * @return
     */
    private SeriesPlan buildCampSeriesExt(Map<String, Object> map, String campsegId, String channelId) {
        SeriesPlan seriesPlan = new SeriesPlan();
        seriesPlan.setCampsegId(campsegId + channelId);
        seriesPlan.setPlanId(getStr("PLAN_ID", map));
        seriesPlan.setSeriesPlanId(getStr("EVALUATION_PLAN_ID", map));
        return seriesPlan;
    }

    /**
     * 组装互斥产品信息
     *
     * @param map
     * @param campsegId
     * @return
     */
    private ExclusivePlan buildCampExclusiveExt(Map<String, Object> map, String campsegId, String channelId) {
        ExclusivePlan exclusivePlan = new ExclusivePlan();
        exclusivePlan.setCampsegId(campsegId + channelId);
        exclusivePlan.setPlanId(getStr("PLAN_ID", map));
        exclusivePlan.setExPlanId(getStr("EXCLUDE_PLAN_ID", map));
        exclusivePlan.setExPlanName(getStr("EXCLUDE_PLAN_NAME", map));
        exclusivePlan.setPlanGroupId(getInt("PLAN_GROUP_ID", map) + "");
        return exclusivePlan;
    }

    /**
     * 构建task_date表数据
     *
     * @param campTaskDate
     * @param map
     * @return
     */
    private McdCampTaskDate buildCampTaskDate(Map<String, Object> campTaskDate, Map<String, Object> map) {
        McdCampTaskDate taskDate = new McdCampTaskDate();
        taskDate.setTaskId(getStr("TASK_ID", campTaskDate));
        taskDate.setExecStatus(getInt("EXEC_STATUS", campTaskDate));
        taskDate.setCustListCount(getInt("CUST_LIST_COUNT", campTaskDate));
        taskDate.setPlanExecTime(getDate("PLAN_EXEC_TIME", campTaskDate, ""));
        taskDate.setCampsegId(getStr("CAMPSEG_ID", map) + getStr("CHANNEL_ID", map));
        taskDate.setDataDate(getInt("DATA_DATE", campTaskDate));
        return taskDate;
    }

    /**
     * 构建并保存审批记录
     *
     * @param map
     */
    private void buildApproveRecoredAndSave(Map<String, Object> map) {
        String approveFlowId = getStr("APPROVE_FLOW_ID", map);
        if (StrUtil.isEmpty(approveFlowId)) {
            return;
        }
        handleApproveRecord(approveFlowId);
    }

    /**
     * 防止数据重复,保存数据前先删除旧数据
     *
     * @param campsegId
     * @param channelId
     */
    private void deleteOldData(String campsegId, String channelId) {
        log.info("开始删除旧数据,campsegId:{},channelId:{}", campsegId, channelId);
        // 删除channelList表数据
        LambdaUpdateWrapper<McdCampChannelListJx> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(McdCampChannelListJx::getCampsegId, campsegId + channelId);
        mcdCampChannelListJxService.remove(updateWrapper);
        // 删除活动扩展表数据
        LambdaUpdateWrapper<McdCampChannelExt> updateWrapper1 = new LambdaUpdateWrapper<>();
        updateWrapper1.eq(McdCampChannelExt::getCampsegId, campsegId + channelId);
        mcdCampChannelExtService.remove(updateWrapper1);
        // 删除mcd_camp_task表数据
        LambdaUpdateWrapper<McdCampTask> updateWrapper2 = new LambdaUpdateWrapper<>();
        updateWrapper2.eq(McdCampTask::getCampsegId, campsegId + channelId);
        mcdCampTaskService.remove(updateWrapper2);
        // 删除mcd_camp_task_date表数据
        LambdaUpdateWrapper<McdCampTaskDate> updateWrapper3 = new LambdaUpdateWrapper<>();
        updateWrapper3.eq(McdCampTaskDate::getCampsegId, campsegId + channelId);
        campTaskDateService.remove(updateWrapper3);
        // 删除融合产品
        LambdaUpdateWrapper<FusionPlan> updateWrapper4 = new LambdaUpdateWrapper<>();
        updateWrapper4.eq(FusionPlan::getCampsegId, campsegId + channelId);
        fusionPlanService.remove(updateWrapper4);
        // 删除同系列产品
        LambdaUpdateWrapper<SeriesPlan> updateWrapper5 = new LambdaUpdateWrapper<>();
        updateWrapper5.eq(SeriesPlan::getCampsegId, campsegId + channelId);
        seriesPlanService.remove(updateWrapper5);
        // 删除互斥产品
        LambdaUpdateWrapper<ExclusivePlan> updateWrapper6 = new LambdaUpdateWrapper<>();
        updateWrapper6.eq(ExclusivePlan::getCampsegId, campsegId + channelId);
        exclusivePlanService.remove(updateWrapper6);
        // 删除频次规则表数据
        LambdaUpdateWrapper<McdFqcRule> updateWrapper7 = new LambdaUpdateWrapper<>();
        updateWrapper7.eq(McdFqcRule::getCampsegId, campsegId + channelId);
        fqcRuleService.remove(updateWrapper7);
        log.info("旧数据删除完成,campsegId:{},channelId:{}", campsegId, channelId);

    }

    /**
     * 从Map中获取字符串
     *
     * @param key
     * @param map
     * @return
     */
    private String getStr(String key, Map<String, Object> map) {
        if (map.get(key) != null) {
            return map.get(key).toString();
        }
        return null;
    }

    /**
     * 从Map中获取Int
     *
     * @param key
     * @param map
     * @return
     */
    private Integer getInt(String key, Map<String, Object> map) {
        if (map.get(key) != null) {
            return Integer.valueOf(map.get(key).toString());
        }
        return null;
    }

    /**
     * 从Map中获取Date
     *
     * @param key
     * @param map
     * @param other 对于 yyyy-MM-dd格式的数据，other后面添加 00:00:00 或者 23:59:59
     * @return
     */
    private Date getDate(String key, Map<String, Object> map, String other) {
        if (map.get(key) != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(map.get(key).toString() + other);
            } catch (ParseException e) {
                log.error("error:", e);
            }
        }
        return null;
    }


    /**
     * 删除活动
     *
     * @param campsegRootId
     * @return
     */
    @Override
    public boolean deleteCamp(String campsegRootId,String channelId) {
        // 删除 mcd_camp_def
        UpdateWrapper<McdCampDef> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(McdCampDef::getCampsegId, campsegRootId).or().eq(McdCampDef::getCampsegRootId, campsegRootId);
        mcdCampDefJxService.remove(updateWrapper);
        // 删除审批实例
        extMapService.deleteApproveInstance(campsegRootId);
        // 删除审批记录
        extMapService.deleteApproveRecord(campsegRootId);

        QueryWrapper<McdCampChannelListJx> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(McdCampChannelListJx::getCampsegId).eq(McdCampChannelListJx::getCampsegRootId, campsegRootId);
        List<McdCampChannelListJx> list = mcdCampChannelListJxService.list(queryWrapper);
        for (McdCampChannelListJx channelListJx : list) {
            delCampByCampsegId(channelListJx.getCampsegId());
        }
        return true;


    }

    @Override
    public List<Map<String, Object>> queryCampInfoList(int stat) {
        List<Map<String, Object>> mapList = mcdCampExtMapMapper.queryReadyCampList(stat);
        if(CollectionUtil.isNotEmpty(mapList)){
            return mapList;
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> queryCampTaskInfo(String campsegId, String channelId) {
        return mcdCampExtMapMapper.queryCampTaskInfo(campsegId,channelId);
    }

    @Override
    public Map<String, Object> queryMcdCampTaskDate(String taskId) {
        McdCampTaskDate mcdCampTaskDate = campTaskDateService.getById(taskId);
        return BeanUtil.beanToMap(mcdCampTaskDate);
    }

    @Override
    public Map<String, Object> queryMcdCampChannelListInfo(String campsegId, String channelId) {
        McdCampChannelListJx one = mcdCampChannelListJxService.getOne(Wrappers.<McdCampChannelListJx>lambdaQuery()
                .eq(McdCampChannelListJx::getCampsegId, campsegId)
                .eq(McdCampChannelListJx::getChannelId, "809"));
        return BeanUtil.beanToMap(one);
    }

    @Override
    public List<Map<String, Object>> queryCampFusionPlanList(String campsegId) {
        LambdaQueryWrapper<FusionPlan>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FusionPlan::getCampsegId,campsegId);
        List<Map<String, Object>> mapList = fusionPlanService.listMaps(queryWrapper);
        return mapList;
    }

    @Override
    public List<Map<String, Object>> queryCampSeriesPlanList(String campsegId) {
        LambdaQueryWrapper<SeriesPlan>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SeriesPlan::getCampsegId,campsegId);
        return seriesPlanService.listMaps(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> queryCampExclusivePlanList(String campsegId) {
        LambdaQueryWrapper<ExclusivePlan>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExclusivePlan::getCampsegId,campsegId);
        return exclusivePlanService.listMaps(queryWrapper);
    }

    /**
     * 删除 mcd_camp_def
     *
     * @param campsegRootId
     */
    private void delCampDef(String campsegRootId) {

    }

    /**
     * 删除子活动相关信息
     *
     * @param campsegId
     * @return
     */
    private boolean delCampByCampsegId(String campsegId) {
        deleteOldData(campsegId, "");
        // 删除活动的预演任务
        extMapService.deleteCampPreview(campsegId);
        // 删除预演日志
        extMapService.deleteCampPreviewLog(campsegId);
        return true;
    }

}
