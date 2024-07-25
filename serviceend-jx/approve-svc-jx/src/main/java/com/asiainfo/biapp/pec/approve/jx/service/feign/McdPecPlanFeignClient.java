package com.asiainfo.biapp.pec.approve.jx.service.feign;

import com.asiainfo.biapp.pec.approve.jx.dto.McdCommunicationQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author ranpf
 * @date 2023/5/23
 */

@FeignClient(name = "plan-svc")
public interface McdPecPlanFeignClient {

    /**
     * H5查询策略详细信息
     *
     * @param campsegId
     * @return
     */
    @PostMapping("/plan-svc/jx/campDetail/viewPolicyDetail")
    ActionResponse<Map<String, Object>> viewPolicyDetail(@RequestBody @Valid McdIdQuery campsegId);


    /**
     * H5查询z沟通人信息
     * @param req
     * @return
     */
    @PostMapping("/plan-svc/api/channelMaterial/jx/queryH5CommunicationUsers")
    ActionResponse  queryCommunicationUsers(@RequestBody McdCommunicationQuery req);

}
