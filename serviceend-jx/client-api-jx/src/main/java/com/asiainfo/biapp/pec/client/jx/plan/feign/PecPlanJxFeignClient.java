package com.asiainfo.biapp.pec.client.jx.plan.feign;

import com.asiainfo.biapp.pec.client.jx.config.FeignClientConfig;
import com.asiainfo.biapp.pec.client.jx.plan.feign.impl.PecPlanJxFeignClientImpl;
import com.asiainfo.biapp.pec.common.jx.model.CampsegSyncInfo;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mamp
 * @date 2022/9/22
 */
@FeignClient(value = "plan-svc", configuration = FeignClientConfig.class, fallback = PecPlanJxFeignClientImpl.class)
public interface PecPlanJxFeignClient {
    /**
     * 根据活动ID查询活动详情
     *
     * @param campId 活动ID
     * @return 活动详情
     */
    @PostMapping(value = "/plan-svc/mcd-camp-sync/selectCampInfoById")
    ActionResponse<CampsegSyncInfo> selectCampInfoById(@RequestParam("campId") String campId);

}
