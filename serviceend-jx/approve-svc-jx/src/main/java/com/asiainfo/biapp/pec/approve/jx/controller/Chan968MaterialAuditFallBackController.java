package com.asiainfo.biapp.pec.approve.jx.controller;

import com.asiainfo.biapp.pec.approve.jx.service.Chan968MaterialAuditService;
import com.asiainfo.biapp.pec.approve.jx.vo.MaterialAuditFallBackVo;
import com.asiainfo.biapp.pec.core.common.OutInterface;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * description: 广点通渠道素材审核回调控制层
 *
 * @author: lvchaochao
 * @date: 2023/8/29
 */
@Api(value = "广点通渠道素材审核回调控制层", tags = {"广点通渠道素材审核回调控制层"})
@RestController
@RequestMapping("/api/material/fallback")
@Slf4j
public class Chan968MaterialAuditFallBackController {

    @Autowired
    private Chan968MaterialAuditService chan968MaterialAuditService;

    @PostMapping("/actApprovalResultSync")
    @OutInterface
    @ResponseBody
    @ApiOperation(value = "活动素材审核结果回调接口", notes = "活动素材审核结果回调接口")
    public Map<String, String> actApprovalResultSync(@RequestBody MaterialAuditFallBackVo vo) {
        return chan968MaterialAuditService.materialAuditFallback(vo);
    }
}
