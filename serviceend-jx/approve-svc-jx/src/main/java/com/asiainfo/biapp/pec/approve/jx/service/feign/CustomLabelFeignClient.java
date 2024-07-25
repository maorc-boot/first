package com.asiainfo.biapp.pec.approve.jx.service.feign;

import com.asiainfo.biapp.pec.approve.config.FeignConfig;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.ModifySelfLabelStatusParam;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.approve.jx.service.feign
 * @className: CustomLabelFeignClient
 * @author: chenlin
 * @description: 客户通自定义标签客户端
 * @date: 2023/7/9 20:32
 * @version: 1.0
 */
@FeignClient(name = "plan-svc",path = "plan-svc",contextId = "customLabelFeignClient",configuration = FeignConfig.class)
//@FeignClient(name = "plan-svc-tmp",path = "plan-svc",contextId = "customLabelFeignClient",configuration = FeignConfig.class)
public interface CustomLabelFeignClient {

    // 2023-07-05 17:54:38 针对自定义标签和自定义预警设置的ENUM_VALUE值
    String CUSTOM_LABEL = "CUSTOM_LABEL";

    @PutMapping("/mcdCustomLabelDef/modifyLabelStatus")
    ActionResponse<String> modifyLabelStatus(@RequestBody @Valid ModifySelfLabelStatusParam modifySelfLabelStatusParam);


    @GetMapping("/mcdCustomLabelDef/selectLabelApproveFlowId")
    ActionResponse<String> selectLabelApproveFlowId(@RequestParam("labelId") Integer labelId);
}
