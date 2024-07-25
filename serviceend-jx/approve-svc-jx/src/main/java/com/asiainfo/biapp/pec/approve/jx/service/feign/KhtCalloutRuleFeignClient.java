package com.asiainfo.biapp.pec.approve.jx.service.feign;

import com.asiainfo.biapp.pec.approve.config.FeignConfig;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.ModifyCalloutRuleStatusParam;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * 客户通外呼规则远程调用feign接口
 */
@FeignClient(name = "plan-svc",path = "plan-svc",contextId = "KhtCalloutRuleFeignClient",configuration = FeignConfig.class)
public interface KhtCalloutRuleFeignClient {

    /**
     * 审批流外呼规则状态修改
     *
     * @param calloutRuleStatusParam calloutRuleStatusParam
     * @return {@link ActionResponse}<{@link String}>
     */
    @PostMapping("/callout/scenario/modifyCalloutRuleStatus")
    ActionResponse<String> modifyCalloutRuleStatus(@RequestBody @Valid ModifyCalloutRuleStatusParam calloutRuleStatusParam);

    /**
     * 审批流流程id获取
     *
     * @param scenarioId 模板编码
     * @return {@link ActionResponse}<{@link String}>
     */
    @GetMapping("/callout/scenario/selectCalloutRuleApprFlowId")
    ActionResponse<String> selectCalloutRuleApprFlowId(@RequestParam("scenarioId") String scenarioId);

}
