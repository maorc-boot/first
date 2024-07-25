package com.asiainfo.biapp.pec.preview.jx.service.feign;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2022/11/1
 */
@FeignClient(value = "pec-log")
public interface CampPreviewFilterLogClient {
    /**
     * 查询执行中预演结果
     *
     * @param campsegRootId
     * @return
     */
    @PostMapping("/pec-log/preview/queryExecResult")
    ActionResponse<List<Map<String,String>>> queryPreviewResult(@RequestParam("campsegRootId") String campsegRootId,
                                                                @RequestParam(value = "startDate", required = false) String startDate,
                                                                @RequestParam(value = "endDate", required = false) String endDate
    );
}
