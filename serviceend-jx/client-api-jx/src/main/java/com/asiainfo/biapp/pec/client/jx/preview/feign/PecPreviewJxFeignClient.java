package com.asiainfo.biapp.pec.client.jx.preview.feign;

import com.asiainfo.biapp.pec.client.jx.config.FeignClientConfig;
import com.asiainfo.biapp.pec.client.jx.preview.feign.impl.PecPreviewJxFeignClientFallbackImpl;
import com.asiainfo.biapp.pec.client.jx.preview.model.McdCampPreview;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author mamp
 * @date 2022/10/12
 */
@FeignClient(value = "preview-svc", configuration = FeignClientConfig.class, fallback = PecPreviewJxFeignClientFallbackImpl.class)
public interface PecPreviewJxFeignClient {
    /**
     * 保存预演信息
     *
     * @param mcdCampPreview
     * @return
     */
    @PostMapping("/preview-svc/jx/preview/savePreview")
    ActionResponse createPreview(@RequestBody McdCampPreview mcdCampPreview);
}
