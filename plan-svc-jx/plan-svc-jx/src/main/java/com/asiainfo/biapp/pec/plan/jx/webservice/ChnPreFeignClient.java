package com.asiainfo.biapp.pec.plan.jx.webservice;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mamp
 * @date 2022/6/28
 */
@FeignClient(value = "preview-svc")
public interface ChnPreFeignClient {

    /**
     * 查询实时类活动列表-江西
     *
     * @return
     */
    @PostMapping(value = "/preview-svc/jx/preview/custChnPreCal")
    ActionResponse custChnPreCal(@RequestParam("custGroupId") String custGroupId, @RequestParam("dataDate") String dataDate, @RequestParam("custFileName") String custFileName);




}
