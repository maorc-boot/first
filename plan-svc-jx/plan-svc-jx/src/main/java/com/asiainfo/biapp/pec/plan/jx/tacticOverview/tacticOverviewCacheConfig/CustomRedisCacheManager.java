package com.asiainfo.biapp.pec.plan.jx.tacticOverview.tacticOverviewCacheConfig;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;


import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx
 * @className: www
 * @author: chenlin
 * @description: 自定义缓存管理器，根据@Cacheable(value = "tacticOverviewCache@15 @后的数字就是有效时间，单位：秒
 * @date: 2023/6/12 16:55
 * @version: 1.0
 */
@Slf4j
public class CustomRedisCacheManager extends RedisCacheManager {

    /**
     * 缓存参数的分隔符
     * 数组元素0=缓存的名称
     * 数组元素1=缓存过期时间TTL
     */
    private static final String DEFAULT_PATH = "@";

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        Duration ttl = getTtlByName(name);
        if (ttl != null) {
            //证明在cacheName上使用了过期时间，需要修改配置中的ttl
            cacheConfig = cacheConfig.entryTtl(ttl);
        }
        //修改缓存key和value值的序列化方式
        return super.createRedisCache(name, cacheConfig);
    }

    /**
     * 通过name获取过期时间
     *
     * @param name
     * @return
     */
    private Duration getTtlByName(String name) {
        if (name == null) {
            return null;
        }
        //根据分隔符拆分字符串，并进行过期时间ttl的解析
        String[] cacheParams = name.split(DEFAULT_PATH);
        if (cacheParams.length > 1) {
            String ttl = cacheParams[cacheParams.length - 1];
            if (!StringUtils.isEmpty(ttl)) {
                try {
                    //将乘法表达式计算出来，为缓存设置有效期
                    AtomicReference<Long> second = new AtomicReference<>(1L);
                    String[] seconds = ttl.split("\\*");
                    Stream.of(seconds).forEach(o-> second.updateAndGet(v -> v * Long.parseLong(o)));
                    return Duration.ofSeconds(second.get());
                } catch (Exception e) {
                    log.error("设置缓存过期时间出错：" + e.getMessage());
                }
            }
        }
        return Duration.ofSeconds(30);
    }
}

