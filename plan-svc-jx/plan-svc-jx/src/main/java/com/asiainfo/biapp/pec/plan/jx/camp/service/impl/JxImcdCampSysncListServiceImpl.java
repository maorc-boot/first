package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.client.app.element.api.AppElementFeignClient;
import com.asiainfo.biapp.client.app.element.model.ProductPackage;
import com.asiainfo.biapp.client.app.element.vo.PlanDefVO;
import com.asiainfo.biapp.pec.common.jx.model.CampsegSyncInfo;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.PlanDetail;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampListDao;
import com.asiainfo.biapp.pec.plan.jx.camp.service.JxImcdCampSysncListService;
import com.asiainfo.biapp.pec.plan.service.IMcdPlanDefService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.ONE_NUMBER;
import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.ZERO_NUMBER;

/**
 * 获取同步的活动信息
 *
 * @author admin
 * @version 1.0
 * @date 2022/6/15 15:18
 */
@Service
@Slf4j
public class JxImcdCampSysncListServiceImpl implements JxImcdCampSysncListService {

    @Resource
    private McdCampListDao mcdCampListDao;

    @Resource
    private AppElementFeignClient elementFeignClient;

    @Resource
    private IMcdPlanDefService planDefService;

    @Override
    public List<CampsegSyncInfo> syncReadyCampList() {
        List<CampsegSyncInfo> campsegInfos = mcdCampListDao.syncReadyCampList();

        campsegInfos.forEach(commonCampsegInfo -> {
            List<PlanDetail> planList = new ArrayList<>();
            commonCampsegInfo.setPlanList(planList);
            if (StrUtil.isBlank(commonCampsegInfo.getPlanName())) {
                //售前是否产品包
                final String planId = commonCampsegInfo.getPlanId();

                McdIdQuery mcdIdQuery = new McdIdQuery();
                mcdIdQuery.setId(planId);
                final ProductPackage productPackage = elementFeignClient.getById(mcdIdQuery).getData();
                if (null != productPackage) {
                    planList.addAll(planDefService.detailPackage(productPackage.getProductIds(), Constant.PRODUCT_ATTR[ZERO_NUMBER]));
                }
            }

            final String crmPlanId = commonCampsegInfo.getCrmPlanId();
            if (StrUtil.isNotBlank(crmPlanId)) {
                // 售前售中售后产品
                final String[] split = crmPlanId.split(Constant.SpecialCharacter.COMMA);
                for (int i = 0; i < split.length; i++) {
                    final String itemPlanId = split[i];
                    if (StrUtil.isNotBlank(itemPlanId)) {
                        final PlanDefVO planDefVO = planDefService.detailPlan(itemPlanId, Constant.PRODUCT_ATTR[i + ONE_NUMBER]);
                        if (StrUtil.isNotBlank(planDefVO.getId())) {
                            //说明有产品包
                            planList.addAll(planDefService.detailPackage(planDefVO.getId(), Constant.PRODUCT_ATTR[i + ONE_NUMBER]));
                        } else {
                            planList.add(planDefService.convert(planDefVO));
                        }
                    }
                }
            }
        });
        return campsegInfos;
    }

    /**
     * 通过子活动ID,查询活动详情
     *
     * @param campId 活动（子）ID
     * @return
     */
    @Override
    public CampsegSyncInfo selectCampInfoById(String campId) {
        return mcdCampListDao.selectCampInfoById(campId);
    }

    @Override
    public Map<String, String> selectNoCustCamp() {
        List<Map<String,String>>list = mcdCampListDao.selectNoCustCamp();
        Map<String, String> map = new HashMap<>();
        if (CollectionUtil.isEmpty(list)) {
            return map;
        }
        for (Map<String,String> m : list) {
            String eventId = m.get("STREAM_EVENT_ID");
            String campsegId = m.get("CAMPSEG_ID");
            map.put(eventId, campsegId);
        }
        return map;
    }
}
