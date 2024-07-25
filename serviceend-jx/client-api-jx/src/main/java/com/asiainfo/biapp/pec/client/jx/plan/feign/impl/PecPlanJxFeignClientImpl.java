package com.asiainfo.biapp.pec.client.jx.plan.feign.impl;

import com.asiainfo.biapp.pec.client.jx.plan.feign.PecPlanJxFeignClient;
import com.asiainfo.biapp.pec.common.jx.model.CampsegSyncInfo;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mamp
 * @date 2022/10/10
 */
@Component
@Slf4j
public class PecPlanJxFeignClientImpl implements PecPlanJxFeignClient {
    /**
     * 根据活动ID查询活动详情
     *
     * @param campId 活动ID
     * @return 活动详情
     */
    @Override
    public ActionResponse<CampsegSyncInfo> selectCampInfoById(String campId) {
        log.error("feign远程调用活动详情查询服务异常后的降级方法");
        return ActionResponse.getFaildResp("调用错误！");
    }
}
