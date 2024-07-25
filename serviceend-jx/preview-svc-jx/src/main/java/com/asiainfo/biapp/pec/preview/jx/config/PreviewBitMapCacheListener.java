package com.asiainfo.biapp.pec.preview.jx.config;

import com.asiainfo.biapp.pec.common.jx.config.BitMapCacheListener;
import com.asiainfo.biapp.pec.preview.jx.service.PlanPreService;
import com.asiainfo.biapp.pec.preview.jx.util.PreviewConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mamp
 * @date 2023/4/21
 */
@Component
@Slf4j
public class PreviewBitMapCacheListener extends BitMapCacheListener {

    @Resource
    private PlanPreService planPreService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        super.onMessage(message, pattern);

        String topic = new String(pattern);
        if (!PreviewConst.PLAN_PRE_REDIS_TOPIC.equals(topic)) {
            log.warn("当前Listener,只处理topic:{},本消息来源:{}", PreviewConst.PLAN_PRE_REDIS_TOPIC, topic);
            return;
        }
        byte[] body = message.getBody();
        if (body == null || body.length <= 0) {
            log.warn("消息为空");
            return;
        }
        String data = new String(body);
        log.info("消息内容:{}", data);
        if (PlanPreBitMapCache.LOCAL_TIMESTAMP.equals(data)) {
            log.info("当前节点数据已经是最新,不需要更新");
            return;
        }
        planPreService.downLoadAndUpdateCache();
    }

}
