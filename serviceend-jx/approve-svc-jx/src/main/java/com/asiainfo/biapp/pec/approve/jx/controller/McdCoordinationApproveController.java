package com.asiainfo.biapp.pec.approve.jx.controller;

import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.approve.common.MetaHandler;
import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.jx.service.IMcdCoordinationAppr2Emis;
import com.asiainfo.biapp.pec.approve.model.User;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 江西策略统筹审批&&增加emis代办
 *
 * @author: lvchaochao
 * @date: 2023/7/27
 */
@RestController
@Slf4j
@Api(tags = "江西策略统筹审批&&增加emis代办")
@RequestMapping("/coordination/approve")
public class McdCoordinationApproveController {

    @Autowired
    private IMcdCoordinationAppr2Emis mcdCoordinationAppr2Emis;

    @PostMapping(path = "/commitCoordCommitAppr2Emis")
    @ApiOperation(value = "策略统筹任务发起审批流程",notes = "发起审批流程")
    public ActionResponse<Long> commitCoordCommitAppr2Emis(@RequestBody SubmitProcessJxDTO submitProcessDTO){
        log.info("start commitCoordCommitAppr2Emis para:{}", JSONUtil.toJsonStr(submitProcessDTO));
        User user = MetaHandler.getUser();
        submitProcessDTO.setUser(user);
        Long instanceId = mcdCoordinationAppr2Emis.commitProcess(submitProcessDTO);
        return ActionResponse.getSuccessResp(instanceId);
    }
}
