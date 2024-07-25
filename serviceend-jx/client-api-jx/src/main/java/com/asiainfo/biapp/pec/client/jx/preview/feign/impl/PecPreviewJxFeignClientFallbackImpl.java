package com.asiainfo.biapp.pec.client.jx.preview.feign.impl;

import com.asiainfo.biapp.pec.client.jx.preview.feign.PecPreviewJxFeignClient;
import com.asiainfo.biapp.pec.client.jx.preview.model.McdCampPreview;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mamp
 * @date 2022/10/12
 */
@Component
@Slf4j
public class PecPreviewJxFeignClientFallbackImpl implements PecPreviewJxFeignClient {
    @Override
    public ActionResponse createPreview(McdCampPreview mcdCampPreview) {
        log.error("feign远程调用策略预演保存服务异常后的降级方法");
        return ActionResponse.getFaildResp("调用错误！");
    }
}
