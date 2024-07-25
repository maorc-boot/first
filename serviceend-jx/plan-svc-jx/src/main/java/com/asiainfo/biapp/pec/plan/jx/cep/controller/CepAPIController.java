package com.asiainfo.biapp.pec.plan.jx.cep.controller;


import com.asiainfo.biapp.pec.core.common.OutInterface;
import com.asiainfo.biapp.pec.plan.jx.cep.req.EventSyncRequestBO;
import com.asiainfo.biapp.pec.plan.jx.cep.service.ICepSysEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * CEP对外接口
 */
@Slf4j
@Controller
@RequestMapping("/api/rule/")
public class CepAPIController {

    @Autowired
    private ICepSysEventService iCepSysEventService;


    /**
     * 事件定义信息同步接口
     *
     * @param requestBO
     * @return
     */
    @ResponseBody
    @OutInterface
    @RequestMapping(value = "syncEvent", method = {RequestMethod.POST})
    public Map<String, Object> syncEvent(@RequestBody EventSyncRequestBO requestBO) {
        log.info("思特奇同步事件到start-----IOP接口入参:{}", requestBO);
        Map<String, Object> result = new HashMap<>();
        //获取事件编码 --ID
        String eventCode = requestBO.getEventCode() == null ? "" : requestBO.getEventCode();
        if (eventCode == "") {
            result.put("resultCode", "0");
            result.put("resultDesc", "事件编码不能为空");
            return result;
        }
        //获取事件名称 --NAME
        String eventName = requestBO.getEventName() == null ? "" : requestBO.getEventName();
        if (eventName == "") {
            result.put("resultCode", "0");
            result.put("resultDesc", "事件名称不能为空");
            return result;
        }
        //获取事件类型 --
//        String eventType = requestBO.getEventType() =="" ? "999" :requestBO.getEventType();
        //获取创建人ID --CREATE_USER
        String createUserId = requestBO.getCreateUserId() == null ? "" : requestBO.getCreateUserId();
        if (createUserId == "") {
            result.put("resultCode", "0");
            result.put("resultDesc", "创建人ID不能为空");
            return result;
        }
        //事件描述
        String eventDesc = requestBO.getEventDesc() == null ? "" : requestBO.getEventDesc();
        if (eventDesc == "") {
            result.put("resultCode", "0");
            result.put("resultDesc", "事件描述不能为空");
            return result;
        }
        //获取创建时间 --CREATE_TIME
        String createTime = requestBO.getCreateTime() == null ? "" : requestBO.getCreateTime();
        if (createTime == "") {
            result.put("resultCode", "0");
            result.put("resultDesc", "创建时间不能为空");
            return result;
        }
        try {
            iCepSysEventService.syncEvent(requestBO);
            result.put("resultCode", "1");
            result.put("resultDesc", "成功");
            log.info("思特奇同步事件到IOP成功:{}", requestBO);
        } catch (Exception e) {
            log.error("思特奇同步事件到IOP失败:{}", requestBO, e);
            result.put("resultCode", "0");
            result.put("resultDesc", "失败");
        }
        log.info("思特奇同步事件结束返回参数", result);
        return result;
    }

}