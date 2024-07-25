package com.asiainfo.biapp.pec.plan.jx.coordination.service.feign;

import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 策略统筹审批emis代办feign客户端
 *
 * @author lvcc
 * @date 2023/07/27
 */
@FeignClient(value = "approve-svc")
public interface McdCoordinationApprFeignClient {

    @PostMapping("/approve-svc/coordination/approve/commitCoordCommitAppr2Emis")
    ActionResponse<Object> commitCoordCommitAppr2Emis(@RequestBody SubmitProcessQuery req);

}
