package com.asiainfo.biapp.pec.plan.jx.webservice.impl;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.webservice.ChnPreFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mamp
 * @date 2022/6/28
 */
@Component
@Slf4j
public class ChnPreFeignClientImpl implements ChnPreFeignClient {
    @Override
    public ActionResponse custChnPreCal(String custGroupId, String dataDate, String custFileName) {
        log.error("feign远程调用计算渠道偏好数据服务异常后的降级方法");
        return ActionResponse.getFaildResp("调用错误！");
    }
}
