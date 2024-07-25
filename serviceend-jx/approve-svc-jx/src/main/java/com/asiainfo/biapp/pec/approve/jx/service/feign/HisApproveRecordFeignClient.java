package com.asiainfo.biapp.pec.approve.jx.service.feign;

import com.asiainfo.biapp.pec.approve.config.FeignConfig;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mamp
 * @date 2023/5/23
 */
@FeignClient(value = "plan-svc",configuration = FeignConfig.class)
public interface HisApproveRecordFeignClient {

    @GetMapping("/plan-svc/hisdata/listRecord")
    @ApiOperation(value = "通过活动ID查询审批记录", tags = "通过活动ID查询审批记录")
    ActionResponse listRecord(@RequestParam("businessId") String businessId) ;
}
