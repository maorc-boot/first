package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdSmsKafka;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SmsTestReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mamp
 * @date 2022/12/22
 */
@Slf4j
@RestController
@RequestMapping("/jx/smsTest")
@Api(tags = "江西:测试短信")
public class SmsTestController {


    @Value("${spring.kafka.topic.sendMessage}")
    private String smsTopic;

    @Setter(onMethod_ = {@Autowired})
    private KafkaTemplate<String, String> kafkaTemplate;

    @ApiOperation(value = "发送测试短信", notes = "发送测试短信")
    @PostMapping("/send")
    public ActionResponse send(@RequestBody SmsTestReq query) {
        log.info("发送测试短信: {}", query);
        McdSmsKafka smsMessage;
        if (StrUtil.isEmpty(query.getProductNo())) {
            return ActionResponse.getFaildResp("号码不能为空");
        }
        String[] productNos = query.getProductNo().split(",");
        for (String productNo : productNos) {
            smsMessage = new McdSmsKafka();
            smsMessage.setMessage(query.getContent());
            smsMessage.setProductNo(productNo);
            Map<String, Object> otherInfo = new HashMap<>();
            otherInfo.put("CAMPSEG_ID", "Test");
            otherInfo.put("CHANNEL_ID", "Test");
            otherInfo.put("SP_CODE", query.getSpCode());
            smsMessage.setOtherInfo(otherInfo);
            kafkaTemplate.send(smsTopic, JSONUtil.toJsonStr(smsMessage));
        }
        return ActionResponse.getSuccessResp("成功");
    }
}
