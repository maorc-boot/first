package com.asiainfo.biapp.pec.preview.jx.config;

import com.asiainfo.biapp.pec.common.jx.config.InitBitMapCache;
import com.asiainfo.biapp.pec.preview.jx.service.PlanPreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mamp
 * @date 2023/4/21
 */
@Slf4j
@Component
public class PreviewInitBitMapCache extends InitBitMapCache {

    @Resource
    private PlanPreService planPreService;

    @Override
    public void run(String... args) {
        super.run(args);
        log.info("开始初始化产品偏好模型数据");
        planPreService.downLoadAndUpdateCache();
    }
}
