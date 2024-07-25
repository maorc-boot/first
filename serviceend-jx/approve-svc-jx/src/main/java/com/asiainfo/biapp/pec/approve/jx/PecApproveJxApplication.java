package com.asiainfo.biapp.pec.approve.jx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.asiainfo.biapp")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.asiainfo.biapp.pec")
@MapperScan(basePackages = {"com.asiainfo.biapp.pec.approve.**.dao"})
@EnableAsync
public class PecApproveJxApplication {
    public static void main(String[] args) {
        SpringApplication.run(PecApproveJxApplication.class, args);
    }
}
