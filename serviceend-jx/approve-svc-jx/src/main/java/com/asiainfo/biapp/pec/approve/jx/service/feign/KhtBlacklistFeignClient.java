package com.asiainfo.biapp.pec.approve.jx.service.feign;

import com.asiainfo.biapp.pec.approve.config.FeignConfig;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.ModifyBlacklistApprStatusParam;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * 客户通黑名单远程调用feign接口
 *
 * @author lvcc
 * @date 2024/05/29
 */
@FeignClient(name = "plan-svc",path = "plan-svc",contextId = "KhtBlacklistFeignClient",configuration = FeignConfig.class)
public interface KhtBlacklistFeignClient {

    /**
     * 审批流关怀短信状态修改
     *
     * @param blacklistApprStatusParam 修改客户通黑名单审批状态请求参数
     * @return {@link ActionResponse}<{@link String}>
     */
    @PostMapping("/mcd-hmh5-blacklist-task/modifyBlacklistApprStatus")
    ActionResponse<String> modifyBlacklistApprStatus(@RequestBody @Valid ModifyBlacklistApprStatusParam blacklistApprStatusParam);

    /**
     * 审批流流程id获取
     *
     * @param taskId 任务编码
     * @return {@link ActionResponse}<{@link String}>
     */
    @GetMapping("/mcd-hmh5-blacklist-task/selectBlacklistApprFlowId")
    ActionResponse<String> selectBlacklistApprFlowId(@RequestParam("taskId") String taskId);

    /**
     * 保存任务导入的清单文件号码
     *
     * @param taskId 任务编码
     * @return {@link ActionResponse}<{@link String}>
     */
    @GetMapping("/mcd-hmh5-blacklist-task/saveImportList2DB")
    ActionResponse<String> saveImportList2DB(@RequestParam("taskId") String taskId);

}
