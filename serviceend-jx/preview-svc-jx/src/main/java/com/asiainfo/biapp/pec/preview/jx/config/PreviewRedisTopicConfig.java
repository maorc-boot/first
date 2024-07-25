package com.asiainfo.biapp.pec.preview.jx.config;

import com.asiainfo.biapp.pec.common.jx.config.BitMapCacheListener;
import com.asiainfo.biapp.pec.common.jx.constant.CommonConstJx;
import com.asiainfo.biapp.pec.core.config.RedisTemplateFactory;
import com.asiainfo.biapp.pec.preview.jx.util.PreviewConst;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Arrays;

/**
 * @author mamp
 * @date 2023/4/21
 */
@Configuration
public class PreviewRedisTopicConfig {

    /**
     * @param listener
     * @return redis消息监听容器
     */
    @Bean
    public RedisMessageListenerContainer container(BitMapCacheListener listener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置连接工厂
        RedisConnectionFactory connectionFactory = RedisTemplateFactory.getJedisRedisTemplate(String.class).getConnectionFactory();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listener, Arrays.asList(new PatternTopic(CommonConstJx.BITMAP_CACHE_REDIS_TOPIC),new PatternTopic(PreviewConst.PLAN_PRE_REDIS_TOPIC),new PatternTopic(PreviewConst.PLAN_PRE_REDIS_TOPIC)));
        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        container.setTopicSerializer(seria);
        return container;

    }
}
