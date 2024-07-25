package com.asiainfo.biapp.pec.approve.jx.event;

import com.asiainfo.biapp.pec.approve.common.Event;
import com.asiainfo.biapp.pec.approve.common.EventInterface;
import com.asiainfo.biapp.pec.approve.jx.dto.TermApproveStatus;
import com.asiainfo.biapp.pec.approve.jx.service.feign.McdTermFeignClient;
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
 * @author wanghao
 */
@Component("termApproveEndEvent")
@Event(name = "江西话术审批流程结束事件",value = "termApproveEndEvent")
@Slf4j
public class TermApproveEndEvent implements EventInterface {

    @Autowired
    private ICmpApproveEventLogService logService;
    @Autowired
    private McdTermFeignClient termFeignClient;

    @Override
    @Async
    public void invoke(CmpApproveProcessRecord record){
        CmpApproveEventLog eventLog = CmpApproveEventLog.builder()
                .id(DataUtil.generateId())
                .businessId(record.getBusinessId())
                .eventId(record.getEventId())
                .nodeId(record.getNodeId())
                .nodeName(record.getNodeName())
                .instanceId(record.getInstanceId())
                .build();
        try {
            Thread.sleep(10*1000);
            TermApproveStatus termApproveStatus = new TermApproveStatus();
            termApproveStatus.setTermId(record.getBusinessId());
            termApproveStatus.setStatus(1);
            ActionResponse<Boolean> actionResponse = termFeignClient.updateTaskStatus(termApproveStatus);
            if(actionResponse != null){
                int code = actionResponse.getStatus().getCode();
                if(code == ResponseStatus.SUCCESS.getCode()){
                    eventLog.setEventStatus(0);
                }else {
                    eventLog.setEventStatus(1);
                }
                eventLog.setEventMessage(actionResponse.getMessage());
            }else{
                eventLog.setEventStatus(1);
                eventLog.setEventMessage("调用话术状态修改接口无返回");
            }
        } catch (Exception e) {
            log.error("TermApproveEndEvent error:{}",e);
            eventLog.setEventStatus(1);
            eventLog.setEventMessage(e.getMessage());
        }
        logService.saveEventLog(eventLog);
    }
}
