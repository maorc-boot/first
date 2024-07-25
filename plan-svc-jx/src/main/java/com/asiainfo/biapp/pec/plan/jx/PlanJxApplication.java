package com.asiainfo.biapp.pec.plan.jx;


import com.asiainfo.biapp.pec.core.config.KafkaAutoConfiguration;
import com.asiainfo.biapp.pec.core.config.RedisTemplateFactory;
import com.asiainfo.biapp.pec.plan.jx.tacticOverview.tacticOverviewCacheConfig.CustomRedisCacheManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication(scanBasePackages = {"com.asiainfo.biapp", "com.cmcc.iop.axis2.server"}, exclude = KafkaAutoConfiguration.class)
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.asiainfo.biapp.pec.plan.**.dao", "com.asiainfo.biapp.pec.plan.**.mapper"})
@EnableFeignClients(basePackages = {"com.asiainfo.biapp"})
@ServletComponentScan(basePackages = "com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.filter")
@EnableCaching
@EnableAsync
public class PlanJxApplication {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(new Class<?>[]{PlanJxApplication.class});
        application.setAllowBeanDefinitionOverriding(true);
        application.run(args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 文件最大
        factory.setMaxFileSize(DataSize.parse("50MB"));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse("50MB"));
        return factory.createMultipartConfig();
    }

    //配置缓存管理器
    @Bean
    public CacheManager cacheManager() {
        RedisTemplate<Object, String> redisTemplate = RedisTemplateFactory.getLettuceRedisTemplate(String.class);
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        //返回一个自定义的CacheManager
        return new CustomRedisCacheManager(redisCacheWriter, defaultCacheConfig);
    }


}
