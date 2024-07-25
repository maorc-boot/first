package com.asiainfo.biapp.cmn.jx;

import com.asiainfo.biapp.pec.core.config.KafkaAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;


@SpringBootApplication(scanBasePackages = "com.asiainfo.biapp",exclude = KafkaAutoConfiguration.class)
@MapperScan("com.asiainfo.biapp.cmn.**.dao")
@Configuration
@EnableScheduling
public class CmnSvcJxApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmnSvcJxApplication.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        // 创建配置的工厂类对象
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 允许上传的文件最大值
        factory.setMaxFileSize(DataSize.of(50, DataUnit.MEGABYTES));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.of(50, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

}
