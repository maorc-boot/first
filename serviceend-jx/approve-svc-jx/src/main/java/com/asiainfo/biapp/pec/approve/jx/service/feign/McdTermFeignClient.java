package com.asiainfo.biapp.pec.approve.jx.service.feign;

import com.asiainfo.biapp.pec.approve.config.FeignConfig;
import com.asiainfo.biapp.pec.approve.jx.dto.TermApproveStatus;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "mkt-term-svc" ,configuration = FeignConfig.class)
public interface McdTermFeignClient {

    @RequestMapping(value="/mkt-term-svc/term/updateTermStatus",method = RequestMethod.POST)
    ActionResponse<Boolean> updateTaskStatus(@RequestBody TermApproveStatus approve);


}
