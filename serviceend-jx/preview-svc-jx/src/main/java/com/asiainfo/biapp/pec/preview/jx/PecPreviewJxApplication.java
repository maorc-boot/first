package com.asiainfo.biapp.pec.preview.jx;

import com.asiainfo.biapp.client.cmn.api.CmnSvcFeignClient;
import com.asiainfo.biapp.pec.client.jx.plan.feign.PecPlanJxFeignClient;
import com.asiainfo.biapp.pec.preview.jx.service.feign.CampPreviewFilterLogClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.asiainfo.biapp")
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {CmnSvcFeignClient.class, PecPlanJxFeignClient.class, CampPreviewFilterLogClient.class})
@MapperScan({"com.asiainfo.biapp.pec.preview.jx.**.mapper","com.asiainfo.biapp.pec.common.jx.**.dao"})
public class PecPreviewJxApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecPreviewJxApplication.class, args);
    }

}
