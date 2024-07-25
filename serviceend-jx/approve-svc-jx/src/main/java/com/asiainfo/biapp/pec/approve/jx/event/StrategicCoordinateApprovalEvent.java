package com.asiainfo.biapp.pec.approve.jx.event;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.approve.common.Event;
import com.asiainfo.biapp.pec.approve.common.EventInterface;
import com.asiainfo.biapp.pec.approve.jx.dto.CampCoordinationStatusQuery;
import com.asiainfo.biapp.pec.approve.jx.dto.CmpApproveProcessJxRecord;
import com.asiainfo.biapp.pec.approve.jx.service.feign.McdCampCoordinationFeignClient;
import com.asiainfo.biapp.pec.approve.model.CmpApproveEventLog;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveEventLogService;
import com.asiainfo.biapp.pec.approve.util.DataUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @author wanghao
 */
@Component("strategicCoordinateApprovalEvent")
@Event(name = "策略统筹流程结束事件",value = "strategicCoordinateApprovalEvent")
@Slf4j
public class StrategicCoordinateApprovalEvent implements EventInterfaceJx {

    @Resource
    private McdCampCoordinationFeignClient mcdCampCoordinationFeignClient;

    @Autowired
    private ICmpApproveEventLogService logService;


    @Override
    @Async
    public void invoke(CmpApproveProcessJxRecord record) {
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
            log.info("开始统筹任务节点结束事件");
            log.info("CmpApproveProcessRecord:{}", new JSONObject(record).toString());
            CampCoordinationStatusQuery dto = new CampCoordinationStatusQuery();
            dto.setTaskId(record.getBusinessId());
            dto.setChildTaskIds(record.getChildBusinessId());
            dto.setFlowId(record.getInstanceId().toString());
            dto.setExecStatus(54);
            log.info("updateTaskStatus para:{}",new JSONObject(dto));
            ActionResponse<Boolean> actionResponse = mcdCampCoordinationFeignClient.updateTaskStatus(dto);
            log.info("updateTaskStatus return:{}",new JSONObject(actionResponse));
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
                eventLog.setEventMessage("调用策划状态修改接口无返回");
            }
        } catch (InterruptedException e) {
            log.error("strategicCoordinateApprovalEvent error:{}",e.toString());
            eventLog.setEventStatus(1);
            eventLog.setEventMessage(e.getMessage());
        }
        CmpApproveEventLog one = logService.getOne(Wrappers.<CmpApproveEventLog>query().lambda().eq(CmpApproveEventLog::getBusinessId, record.getBusinessId()));
        if (ObjectUtil.isEmpty(one)) {
            logService.saveEventLog(eventLog);
        } else {
            logService.update(Wrappers.<CmpApproveEventLog>update().lambda().set(CmpApproveEventLog::getModifyDate, new Date()).eq(CmpApproveEventLog::getBusinessId, record.getBusinessId()));
        }
    }
}
