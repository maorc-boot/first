package com.asiainfo.biapp.pec.approve.jx.service.feign;

import com.asiainfo.biapp.pec.approve.config.FeignConfig;
import com.asiainfo.biapp.pec.approve.jx.dto.McdCampApproveJxNewQuery;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.ModifyAlarmStatusParam;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.SelectAlarmResultInfo;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author chenlin16
 * @date 2023-07-09 14:32:41
 */
@FeignClient(name = "plan-svc",path = "plan-svc",contextId = "CustomAlarmFeignClient",configuration = FeignConfig.class)
//@FeignClient(name = "plan-svc-tmp",path = "plan-svc",contextId = "CustomAlarmFeignClient",configuration = FeignConfig.class)
public interface CustomAlarmFeignClient {

    // 2023-07-05 17:54:38 针对自定义标签和自定义预警设置的ENUM_VALUE值
    String CUSTOM_ALARM = "APP_ALARM";

    /*
     * @param alarmStatusParam:
     * @return ActionResponse<String>:
     * @author chenlin
     * @description 修改自定义预警的审批完成或者审批驳回状态
     * @date 2023/7/9 14:26
     */
    @PutMapping("/mcdAppAlarmInfo/modifyAlarmStatus")
    ActionResponse<String> modifyAlarmStatus(@RequestBody @Valid ModifyAlarmStatusParam alarmStatusParam);


    @GetMapping("/mcdAppAlarmInfo/selectAlarmApproveFlowId")
    ActionResponse<String> selectAlarmApproveFlowId(@RequestParam("alarmId") Integer alarmId);

    /**
     * 查询审批中的预警信息
     *
     * @param param 入参
     * @return {@link ActionResponse}<{@link List}<{@link SelectAlarmResultInfo}>>
     */
    @PostMapping("/mcdAppAlarmInfo/queryApprovingAlarm")
    ActionResponse<Page<SelectAlarmResultInfo>> queryApprovingAlarm(@RequestBody McdCampApproveJxNewQuery param);

    /**
     * 查询预警名称以及创建人信息
     *
     * @param query 预警id信息
     * @return {@link ActionResponse}<{@link Map}<{@link String}, {@link Object}>>
     */
    @PostMapping("/mcdAppAlarmInfo/queryAlarmNameAndCreator")
    ActionResponse<Map<String, Object>> queryAlarmNameAndCreator(@RequestBody McdIdQuery query);
}
