package com.asiainfo.biapp.pec.approve.jx.service.feign;

import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.approve.config.FeignConfig;
import com.asiainfo.biapp.pec.approve.model.MaterialStatusQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;


/**
 * @author ranpf
 */
@FeignClient(value = "element-svc",configuration = FeignConfig.class)
public interface ElementMatermialJxService {

    @RequestMapping(value="/element-svc/api/mcdDimMaterialApprove/jx/updateMaterialStatus",method = RequestMethod.POST)
    ActionResponse<Boolean> updateMaterialStatus(@RequestBody @Valid MaterialStatusQuery req);

    /**
     * 更新素材为审批驳回状态
     *
     * @param req 入参对象
     * @return {@link ActionResponse}
     */
    @RequestMapping(value="/element-svc/api/mcdDimMaterial/jx/updateMaterialApproveRejectStatus", method = RequestMethod.POST)
    ActionResponse updateMaterialApproveRejectStatus(@RequestBody @Valid MaterialStatusQuery req);

}
