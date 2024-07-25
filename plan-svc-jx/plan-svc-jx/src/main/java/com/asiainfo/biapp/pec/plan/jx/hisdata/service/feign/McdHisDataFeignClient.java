package com.asiainfo.biapp.pec.plan.jx.hisdata.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * @author ranpf
 * @date 2023/3/1
 */
@FeignClient(value = "pec-his-data")
public interface McdHisDataFeignClient {


    @PostMapping("/pec-his-data/camp/getChannelList")
    List<Map<String, Object>> getChannelList(@RequestParam(value = "stat", required = false) String stat, @RequestParam("channelId") String channelId);

    @PostMapping("/pec-his-data/camp/getDef")
    List<Map<String, Object>> getDef(@RequestParam("campsegId") String campsegId);

    @PostMapping("/pec-his-data/camp/getExt")
    Map<String, Object> getExt(@RequestParam("campsegId") String campsegId);

    @PostMapping("/pec-his-data/camp/getCampTask")
    Map<String, Object> getCampTask(@RequestParam("campsegId") String campsegId, @RequestParam("channelId") String channelId);

    @PostMapping("/pec-his-data/camp/approveRecored")
    List<Map<String, Object>> selectApproveRecored(@RequestParam("approveFlowId") String approveFlowId);

    @PostMapping("/pec-his-data/camp/getCampTaskDate")
    Map<String, Object> getCampTaskDate(@RequestParam("taskId") String taskId);

    @PostMapping("/pec-his-data/camp/getCampFixPlan")
    Map<String, Object> getCampFixPlan(@RequestParam("campsegId") String campsegId);

    @PostMapping("/pec-his-data/camp/getCampSeriesPlan")
    Map<String, Object> getCampSeriesPlan(@RequestParam("campsegId") String campsegId);

    @PostMapping("/pec-his-data/camp/getCampExclusivePlan")
    Map<String, Object> getCampExclusivePlan(@RequestParam("campsegId") String campsegId);

}
