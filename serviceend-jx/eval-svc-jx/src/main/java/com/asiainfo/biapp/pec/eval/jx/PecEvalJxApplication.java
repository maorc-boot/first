package com.asiainfo.biapp.pec.eval.jx;

import com.asiainfo.biapp.pec.core.config.KafkaAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.asiainfo.biapp", exclude = {KafkaAutoConfiguration.class, SpringApplicationAdminJmxAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan({"com.asiainfo.biapp.pec.eval.**.mapper", "com.asiainfo.biapp.pec.eval.**.dao"})
public class PecEvalJxApplication {
    public static void main(String[] args) {
        SpringApplication.run(PecEvalJxApplication.class, args);
    }
}
