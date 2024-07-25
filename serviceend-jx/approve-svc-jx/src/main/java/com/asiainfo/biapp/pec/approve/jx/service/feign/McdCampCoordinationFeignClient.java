package com.asiainfo.biapp.pec.approve.jx.service.feign;

import com.asiainfo.biapp.pec.approve.config.FeignConfig;
import com.asiainfo.biapp.pec.approve.jx.dto.CampCoordinationStatusQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "plan-svc" ,configuration = FeignConfig.class)
public interface McdCampCoordinationFeignClient {

    @RequestMapping(value="/plan-svc/api/campCoordinate/taskApprove/updateTaskStatus",method = RequestMethod.POST)
    ActionResponse<Boolean> updateTaskStatus(@RequestBody CampCoordinationStatusQuery udto);

    @PostMapping(value = "/plan-svc/api/campCoordinate/taskApprove/chkApprovingTask")
    ActionResponse chkApprovingTask(@RequestBody CampCoordinationStatusQuery query);

    @PostMapping(value = "/plan-svc/api/campCoordinate/taskApprove/updateChildTask")
    ActionResponse updateChildTask(@RequestBody CampCoordinationStatusQuery query);

    @PostMapping(value = "/plan-svc/api/campCoordinate/taskApprove/updateMainTaskStat")
    ActionResponse updateMainTaskStat(@RequestBody CampCoordinationStatusQuery query);

}
