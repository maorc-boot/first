package com.asiainfo.biapp.auth.jx;

import com.asiainfo.biapp.client.cmn.api.CmnSvcFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = "com.asiainfo.biapp")
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {CmnSvcFeignClient.class})
public class AuthSvcJxApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthSvcJxApplication.class, args);
    }
}
