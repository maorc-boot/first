package com.asiainfo.biapp.pec.approve.jx.event;

import com.asiainfo.biapp.pec.approve.common.Event;
import com.asiainfo.biapp.pec.approve.common.EventInterface;
import com.asiainfo.biapp.pec.approve.jx.service.feign.KhtBlacklistFeignClient;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.ModifyBlacklistApprStatusParam;
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
 * 客户通黑名单审批流程结束事件
 *
 * @author lvcc
 * @date 2024/02/23
 */
@Component("hmh5BlacklistAppEndEvent")
@Event(name = "客户通黑名单审批流程结束事件", value = "hmh5BlacklistAppEndEvent")
@Slf4j
public class Hmh5BlacklistAppEndEvent implements EventInterface {

    @Autowired
    private ICmpApproveEventLogService logService;

    @Autowired
    private KhtBlacklistFeignClient blacklistFeignClient;

    @Override
    @Async
    public void invoke(CmpApproveProcessRecord record) {
        log.info("客户通导入审批事件触发开始！===");
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
            // 1. 根据任务id查询下载清单文件并保存到清单表
            ActionResponse<String> saveImportList2DB = blacklistFeignClient.saveImportList2DB(record.getBusinessId());
            // 2. 修改任务状态为审批完成
            ModifyBlacklistApprStatusParam blacklistApprStatusParam = new ModifyBlacklistApprStatusParam();
            blacklistApprStatusParam.setTaskId(record.getBusinessId());
            blacklistApprStatusParam.setApprovalStatus(5);
            log.info("客户通导入审批事件修改黑名单状态！");
            ActionResponse<String> actionResponse = blacklistFeignClient.modifyBlacklistApprStatus(blacklistApprStatusParam);
            if (actionResponse != null && saveImportList2DB != null) {
                int code = actionResponse.getStatus().getCode();
                int code2 = saveImportList2DB.getStatus().getCode();
                if (code == ResponseStatus.SUCCESS.getCode() && code2 == ResponseStatus.SUCCESS.getCode()) {
                    eventLog.setEventStatus(0);
                } else {
                    eventLog.setEventStatus(1);
                }
                eventLog.setEventMessage(actionResponse.getMessage());
            } else {
                eventLog.setEventStatus(1);
                eventLog.setEventMessage("调用客户通黑名单审批状态修改接口无返回");
            }
            log.info("客户通导入审批事件触发结束！===");
        } catch (Exception e) {
            log.error("Hmh5BlacklistAppEndEvent error: ", e);
            eventLog.setEventStatus(1);
            eventLog.setEventMessage(e.getMessage());
        }
        logService.saveEventLog(eventLog);
    }
}
