package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.model.ExclusivePlan;
import com.asiainfo.biapp.pec.plan.jx.camp.model.FusionPlan;
import com.asiainfo.biapp.pec.plan.jx.camp.model.SeriesPlan;
import com.asiainfo.biapp.pec.plan.jx.camp.req.PlanBaseInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.req.PlanExtInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ExclusivePlanService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.FusionPlanService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IPlanExtInfoService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.SeriesPlanService;
import com.asiainfo.biapp.pec.plan.model.McdCampDef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mamp
 * @date 2022/10/20
 */
@Service
@Slf4j
public class PlanExtInfoServiceImpl implements IPlanExtInfoService {

    @Autowired
    private ExclusivePlanService exclusivePlanService;

    @Autowired
    private FusionPlanService fusionPlanService;

    @Autowired
    private SeriesPlanService seriesPlanService;


    /**
     * 保存产品扩展信息：融合产品，同系列产品，互斥产品
     *
     * @param planExtInfoList 产品扩展信息
     * @param campDef         根活动信息
     */
    @Override
    public void savePlanExtInfo(List<PlanExtInfo> planExtInfoList, McdCampDef campDef) {
        try {
            String campsegRootId = campDef.getCampsegRootId();
            if ("0".equals(campsegRootId)) {
                campsegRootId = campDef.getCampsegId();
            }
            String finalCampsegRootId = campsegRootId;
            if(CollectionUtil.isEmpty(planExtInfoList)){
                return ;
            }
            planExtInfoList.stream().forEach(planExtInfo -> {
                // 主产品ID
                String planId = planExtInfo.getPlanId();
                // 融合产品
                List<PlanBaseInfo> campFusionPlans = planExtInfo.getCampFusionPlans();
                campFusionPlans.stream().forEach(p -> {
                    FusionPlan fusionPlan = new FusionPlan();
                    fusionPlan.setPlanId(planId);
                    fusionPlan.setFusionPlanId(p.getPlanId());
                    fusionPlan.setCampsegId(finalCampsegRootId);
                    fusionPlan.setFusionPlanName(p.getPlanName());
                    boolean plan = fusionPlanService.save(fusionPlan);
                    if (!plan) {
                        throw new RuntimeException("融合产品保存失败");
                    }
                });

                // 同系列产品
                List<PlanBaseInfo> campSeriesPlan = planExtInfo.getCampSeriesPlan();
                campSeriesPlan.stream().forEach(p -> {
                    SeriesPlan seriesPlan = new SeriesPlan();
                    seriesPlan.setPlanId(planId);
                    seriesPlan.setSeriesPlanId(p.getPlanId());
                    seriesPlan.setCampsegId(finalCampsegRootId);
                    seriesPlan.setSeriesPlanName(p.getPlanName());
                    boolean series = seriesPlanService.save(seriesPlan);
                    if (!series) {
                        throw new RuntimeException("同系列產品保存失敗");
                    }
                });

                // 互斥产品
                List<PlanBaseInfo> campExclusivePlan = planExtInfo.getCampExclusivePlan();
                //自定义分组-不理解分组的要求，暂时排序命名分组
                List<Integer> groupId = new ArrayList<>(1);
                groupId.add(1);
                int i = 1;
                campExclusivePlan.stream().forEach(p -> {
                    ExclusivePlan exclusivePlan = new ExclusivePlan();
                    exclusivePlan.setPlanId(planId);
                    exclusivePlan.setExPlanId(p.getPlanId());
                    exclusivePlan.setPlanGroupId(groupId.get(0).toString());
                    groupId.set(0, groupId.get(0) + 1);
                    exclusivePlan.setCampsegId(finalCampsegRootId);
                    exclusivePlan.setExPlanName(p.getPlanName());
                    boolean update = exclusivePlanService.save(exclusivePlan);
                    if (!update) {
                        throw new RuntimeException("互斥產品保存失敗");
                    }
                });
            });
        } catch (Exception e) {
            log.error("保存主产品扩展产品信息失败:", e);
        }
    }
}
