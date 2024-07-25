package com.asiainfo.biapp.pec.client.jx.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mamp
 * @date 2022/10/10
 */
@Configuration
public class FeignClientConfig implements RequestInterceptor {

    @Bean
    Logger.Level feignLoggerLeave() {
        return Logger.Level.FULL;
    }

    @Override
    public void apply(RequestTemplate template) {

    }
}
