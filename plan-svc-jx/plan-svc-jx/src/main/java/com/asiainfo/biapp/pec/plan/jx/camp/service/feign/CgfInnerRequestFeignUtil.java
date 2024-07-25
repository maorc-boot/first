package com.asiainfo.biapp.pec.plan.jx.camp.service.feign;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * cgf内部服务请求
 */
@FeignClient("pec-cgf")
public interface CgfInnerRequestFeignUtil {

    /**
     * cgf客户群匹配
     * @param json
     * @return
     */
    @PostMapping("/pec-cgf/match/listCustomMatch")
    String listCustomMatch(@RequestBody JSONObject json);
}
