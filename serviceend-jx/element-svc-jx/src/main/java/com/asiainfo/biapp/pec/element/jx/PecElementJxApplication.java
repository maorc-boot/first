package com.asiainfo.biapp.pec.element.jx;

import com.asiainfo.biapp.client.pec.approve.api.PecApproveFeignClient;
import com.asiainfo.biapp.pec.core.config.KafkaAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = "com.asiainfo.biapp", exclude = {KafkaAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {PecApproveFeignClient.class})
@MapperScan({"com.asiainfo.biapp.pec.element.**.dao", "com.asiainfo.biapp.pec.element.jx.mapper"})
public class PecElementJxApplication {
    public static void main(String[] args) {
        SpringApplication.run(PecElementJxApplication.class, args);
    }

}
