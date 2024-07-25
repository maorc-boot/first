package com.asiainfo.biapp.pec.approve.jx.event;

import com.asiainfo.biapp.pec.approve.common.Event;
import com.asiainfo.biapp.pec.approve.common.EventInterface;
import com.asiainfo.biapp.pec.approve.jx.service.feign.CustomAlarmFeignClient;
import com.asiainfo.biapp.pec.approve.jx.service.feign.CustomLabelFeignClient;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.ModifyAlarmStatusParam;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.ModifySelfLabelStatusParam;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.approve.jx.event
 * @className: CustomLabelApproveEndJxEvent
 * @author: chenlin
 * @description: 新增自定义标签审批结束事件
 * @date: 2023/7/9
 * @version: 1.0
 */
@Component
@Event(name = "江西自定义标签审批流程结束事件", value = "customLabelApproveEndJxEvent")
@Slf4j
public class CustomLabelApproveEndJxEvent implements EventInterface {

    @Autowired
    private CustomLabelFeignClient customLabelFeignClient;

    @Override
    @Async
    public void invoke(CmpApproveProcessRecord record) {
        try {
            Thread.sleep(10 * 1000);

            ModifySelfLabelStatusParam modifySelfLabelStatusParam = new ModifySelfLabelStatusParam();

            //设置审批通过
            modifySelfLabelStatusParam.setLabelId(Integer.valueOf(record.getBusinessId()));
            modifySelfLabelStatusParam.setApproveFlowId(String.valueOf(record.getInstanceId()));
            modifySelfLabelStatusParam.setApproveStatus(52);

            ActionResponse<String> response = customLabelFeignClient.modifyLabelStatus(modifySelfLabelStatusParam);
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
