package com.asiainfo.biapp.pec.approve.jx.event;

import com.asiainfo.biapp.pec.approve.common.Event;
import com.asiainfo.biapp.pec.approve.common.EventInterface;
import com.asiainfo.biapp.pec.approve.jx.service.feign.KhtCalloutRuleFeignClient;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.ModifyCalloutRuleStatusParam;
import com.asiainfo.biapp.pec.approve.model.CmpApproveEventLog;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveEventLogService;
import com.asiainfo.biapp.pec.approve.util.DataUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 客户通外呼规则审批流程结束事件
 */
@Component("calloutRuleAppEndEvent")
@Event(name = "客户通外呼规则审批流程结束事件", value = "calloutRuleAppEndEvent")
@Slf4j
public class CalloutRuleAppEndEvent implements EventInterface {

    @Autowired
    private ICmpApproveEventLogService logService;

    @Autowired
    private KhtCalloutRuleFeignClient calloutRuleFeignClient;

    @Override
    @Async
    public void invoke(CmpApproveProcessRecord record) {
        CmpApproveEventLog eventLog = CmpApproveEventLog.builder()
                .id(DataUtil.generateId())
                .businessId(record.getBusinessId())
                .eventId(record.getEventId())
                .nodeId(record.getNodeId())
                .nodeName(record.getNodeName())
                .instanceId(record.getInstanceId())
                .build();
        try {
            Thread.sleep(10 * 1000);
            ModifyCalloutRuleStatusParam calloutRuleStatusParam = new ModifyCalloutRuleStatusParam();
            calloutRuleStatusParam.setScenarioId(record.getBusinessId());
            calloutRuleStatusParam.setApprovalStatus("5");
            ActionResponse<String> actionResponse = calloutRuleFeignClient.modifyCalloutRuleStatus(calloutRuleStatusParam);
            if (actionResponse != null) {
                int code = actionResponse.getStatus().getCode();
                if (code == ResponseStatus.SUCCESS.getCode()) {
                    eventLog.setEventStatus(0);
                } else {
                    eventLog.setEventStatus(1);
                }
                eventLog.setEventMessage(actionResponse.getMessage());
            } else {
                eventLog.setEventStatus(1);
                eventLog.setEventMessage("调用客户通外呼规则审批状态修改接口无返回");
            }
        } catch (Exception e) {
            log.error("CalloutRuleAppEndEvent error: ", e);
            eventLog.setEventStatus(1);
            eventLog.setEventMessage(e.getMessage());
        }
        logService.saveEventLog(eventLog);
    }
}
