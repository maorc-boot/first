package com.asiainfo.biapp.pec.approve.jx.event;

import com.asiainfo.biapp.pec.approve.common.Event;
import com.asiainfo.biapp.pec.approve.common.EventInterface;
import com.asiainfo.biapp.pec.approve.event.ElementApproveEndEvent;
import com.asiainfo.biapp.pec.approve.jx.service.feign.CustomAlarmFeignClient;
import com.asiainfo.biapp.pec.approve.jx.service.feign.ElementMatermialJxService;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.ModifyAlarmStatusParam;
import com.asiainfo.biapp.pec.approve.model.CmpApproveEventLog;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.asiainfo.biapp.pec.approve.model.MaterialStatusQuery;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveEventLogService;
import com.asiainfo.biapp.pec.approve.util.DataUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author chenlin16
 */
//2023-07-09 10:42:11 新增自定义预警审批结束事件
@Component
@Event(name = "江西自定义预警审批流程结束事件", value = "customAlarmApproveEndJxEvent")
@Slf4j
public class CustomAlarmApproveEndJxEvent implements EventInterface {

    //TODO 2023-07-09 10:44:20 暂不考虑记录日志
/*    @Autowired
    private ICmpApproveEventLogService logService;*/
    @Autowired
    private CustomAlarmFeignClient customAlarmFeignClient;

/*    @Override
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
    }*/

    @Override
    @Async
    public void invoke(CmpApproveProcessRecord record) {
        try {
            Thread.sleep(10 * 1000);

            ModifyAlarmStatusParam alarmStatusParam = new ModifyAlarmStatusParam();
            //设置审批完成
            alarmStatusParam.setAlarmId(Integer.valueOf(record.getBusinessId()));
            alarmStatusParam.setApproveFlowId(String.valueOf(record.getInstanceId()));
            alarmStatusParam.setApproveStatus(1);

            ActionResponse<String> response = customAlarmFeignClient.modifyAlarmStatus(alarmStatusParam);
            if (response != null) {
                if (response.getStatus().getCode() == ResponseStatus.SUCCESS.getCode()) {
                    log.info("自定义预警审批流程结束事件调用修改状态服务成功！");
                } else {
                    log.error("自定义预警审批流程结束事件调用修改状态服务失败，原因：" + response.getData());
                }
            } else {
                log.error("自定义预警审批流程结束事件调用修改状态服务失败");
            }
        } catch (Exception e) {
            log.error("自定义预警审批流程结束事件执行异常:{}", e);
        }
    }
}
