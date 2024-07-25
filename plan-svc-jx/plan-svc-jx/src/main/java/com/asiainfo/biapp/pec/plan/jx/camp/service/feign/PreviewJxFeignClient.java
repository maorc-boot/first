package com.asiainfo.biapp.pec.plan.jx.camp.service.feign;

import com.asiainfo.biapp.pec.client.jx.preview.model.McdCampPreview;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author mamp
 * @date 2022/10/20
 */
@FeignClient(value = "preview-svc")
public interface PreviewJxFeignClient {

    /**
     * 保存预演数据
     *
     * @param mcdCampPreviews
     * @return
     */
    @PostMapping("/preview-svc/jx/preview/savePreview")
    ActionResponse createPreview(@RequestBody List<McdCampPreview> mcdCampPreviews);

    /**
     * 通过活动rootId删除预演数据
     * @param campsegRootId 活动rootId
     * @return
     */
    @PostMapping("/preview-svc/jx/preview/deletePreviewByRootId")
    ActionResponse deletePreviewByRootId(@RequestParam("campsegRootId") String campsegRootId);
}
