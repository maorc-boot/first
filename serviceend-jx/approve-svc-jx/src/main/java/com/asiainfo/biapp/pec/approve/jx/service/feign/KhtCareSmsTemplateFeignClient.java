package com.asiainfo.biapp.pec.approve.jx.service.feign;

import com.asiainfo.biapp.pec.approve.config.FeignConfig;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.ModifyCareSmsTemplateStatusParam;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * 客户通关怀短信模板远程调用feign接口
 *
 * @author lvcc
 * @date 2024/02/23
 */
@FeignClient(name = "plan-svc",path = "plan-svc",contextId = "KhtCareSmsTemplateFeignClient",configuration = FeignConfig.class)
public interface KhtCareSmsTemplateFeignClient {

    /**
     * 审批流关怀短信状态修改
     *
     * @param careSmsTemplateStatusParam careSmsTemplateStatusParam
     * @return {@link ActionResponse}<{@link String}>
     */
    @PostMapping("/sms/template/modifyCareSmsTemplateStatus")
    ActionResponse<String> modifyCareSmsTemplateStatus(@RequestBody @Valid ModifyCareSmsTemplateStatusParam careSmsTemplateStatusParam);

    /**
     * 审批流流程id获取
     *
     * @param templateCode 模板编码
     * @return {@link ActionResponse}<{@link String}>
     */
    @GetMapping("/sms/template/selectCareSmsTmpApprFlowId")
    ActionResponse<String> selectCareSmsTmpApprFlowId(@RequestParam("templateCode") String templateCode);

}
