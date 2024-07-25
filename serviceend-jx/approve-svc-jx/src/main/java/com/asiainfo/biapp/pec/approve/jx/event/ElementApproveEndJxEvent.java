package com.asiainfo.biapp.pec.approve.jx.event;

import com.asiainfo.biapp.pec.approve.common.Event;
import com.asiainfo.biapp.pec.approve.common.EventInterface;
import com.asiainfo.biapp.pec.approve.event.ElementApproveEndEvent;
import com.asiainfo.biapp.pec.approve.jx.service.feign.ElementMatermialJxService;
import com.asiainfo.biapp.pec.approve.model.CmpApproveEventLog;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.asiainfo.biapp.pec.approve.model.MaterialStatusQuery;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveEventLogService;
import com.asiainfo.biapp.pec.approve.service.feign.ElementService;
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
@Component("elementApproveEndJxEvent")
@Event(name = "江西素材审批流程结束事件",value = "elementApproveEndEvent")
@Slf4j
public class ElementApproveEndJxEvent extends ElementApproveEndEvent {

    @Autowired
    private ICmpApproveEventLogService logService;
    @Autowired
    private ElementMatermialJxService elementMatermialJxService;

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
            MaterialStatusQuery iopDimMaterial = new MaterialStatusQuery();
            iopDimMaterial.setFlowId(record.getInstanceId().toString());
            iopDimMaterial.setMaterialStat(1);
            ActionResponse<Boolean> actionResponse = elementMatermialJxService.updateMaterialStatus(iopDimMaterial);
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
                eventLog.setEventMessage("调用素材状态修改接口无返回");
            }
        } catch (Exception e) {
            log.error("elementApproveEndEvent error:{}",e);
            eventLog.setEventStatus(1);
            eventLog.setEventMessage(e.getMessage());
        }
        logService.saveEventLog(eventLog);
    }
}
