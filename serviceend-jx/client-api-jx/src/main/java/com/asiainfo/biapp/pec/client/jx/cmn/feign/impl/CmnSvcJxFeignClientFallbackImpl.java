package com.asiainfo.biapp.pec.client.jx.cmn.feign.impl;

import com.asiainfo.biapp.pec.client.jx.cmn.feign.CmnSvcJxFeignClient;
import com.asiainfo.biapp.pec.client.jx.cmn.feign.vo.UserJxVO;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import lombok.extern.slf4j.Slf4j;

/**
 * description: 江西：cmn模块的feign接口降级实现
 *
 * @author: lvchaochao
 * @date: 2023/12/18
 */
@Slf4j
public class CmnSvcJxFeignClientFallbackImpl implements CmnSvcJxFeignClient {

    /**
     * 根据用户id查询用户信息
     *
     * @param req 用户id
     * @return {@link ActionResponse}<{@link UserJxVO}>
     */
    @Override
    public ActionResponse<UserJxVO> getByUserID(McdIdQuery req) {
        log.error("feign远程调用根据用户id查询用户信息服务异常后的降级方法");
        return ActionResponse.getFaildResp("调用错误！");
    }
}
