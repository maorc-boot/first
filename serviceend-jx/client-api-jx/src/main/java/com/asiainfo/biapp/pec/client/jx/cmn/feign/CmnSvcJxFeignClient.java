package com.asiainfo.biapp.pec.client.jx.cmn.feign;

import com.asiainfo.biapp.pec.client.jx.cmn.feign.impl.CmnSvcJxFeignClientFallbackImpl;
import com.asiainfo.biapp.pec.client.jx.cmn.feign.vo.UserJxVO;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * description: 江西：cmn模块的feign接口
 *
 * @author: lvchaochao
 * @date: 2023/12/18
 */
@FeignClient(value = "cmn-svc", fallback = CmnSvcJxFeignClientFallbackImpl.class)
public interface CmnSvcJxFeignClient {

    /**
     * 根据用户id查询用户信息
     *
     * @param req 用户id
     * @return {@link ActionResponse}<{@link UserJxVO}>
     */
    @PostMapping("/cmn-svc/api/jx/user/selectByUserIDForAuth")
    ActionResponse<UserJxVO> getByUserID(@RequestBody McdIdQuery req);

}
