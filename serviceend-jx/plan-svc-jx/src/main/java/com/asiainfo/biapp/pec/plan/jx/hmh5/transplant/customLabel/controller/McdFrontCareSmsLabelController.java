package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller;


import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam.AddCareSmsLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam.ModifyCareSmsLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam.SelectCareSmsLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service.McdFrontCareSmsLabelService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@RestController
@RequestMapping("/mcdFrontCareSmsLabel")
@Validated
@Slf4j
@Api(tags = "客户通-代维客群标签")
@DataSource("khtmanageusedb")
public class McdFrontCareSmsLabelController {

    @Autowired
    private McdFrontCareSmsLabelService smsLabelService;

    @ApiOperation("根据查询条件获取客群标签列表")
    @GetMapping
    public ActionResponse selectCareSmsLabels(SelectCareSmsLabelParam smsLabelParam) {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("Id为：{}的用户正在查询客群标签，查询条件：{}！", userId, smsLabelParam);
        Page careSmsLabels = smsLabelService.selectCareSmsLabels(smsLabelParam);
        return ActionResponse.getSuccessResp(careSmsLabels);
    }


    @ApiOperation("新建客群标签")
    @PostMapping
    public ActionResponse addNewCareSmsLabel(@RequestBody @Validated AddCareSmsLabelParam smsLabelParam) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("Id为：{}的用户正在新建客群标签，标签名：{}！", user.getUserId(), smsLabelParam.getLabelName());
        smsLabelService.addNewCareSmsLabel(user,smsLabelParam);
        return ActionResponse.getSuccessResp("客群标签：{" + smsLabelParam.getLabelName() + "}创建成功！");
    }

    @ApiOperation("根据序列号修改客群标签")
    @PutMapping
    public ActionResponse modifyCareSmsLabel(@RequestBody @Validated ModifyCareSmsLabelParam smsLabelParam) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("Id为：{}的用户正在修改客群标签，标签id为：{}！", user.getUserId(), smsLabelParam.getSerialNum());
        smsLabelService.modifyCareSmsLabel(user,smsLabelParam);
        return ActionResponse.getSuccessResp("客群标签Id：{" + smsLabelParam.getSerialNum() + "}修改成功！");
    }

    @ApiOperation("根据序列号删除客群标签")
    @ApiImplicitParam(name = "serialNum", value = "客群标签的序列号", required = true, example = "1")
    @DeleteMapping
    public ActionResponse deleteCareSmsLabel(
            @RequestParam("serialNum") @Min(value = 1, message = "序列号从1开始！") Integer serialNum
    ) {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("Id为：{}的用户正在删除客群标签，标签id为：{}！", userId, serialNum);
        smsLabelService.deleteCareSmsLabel(userId, serialNum);
        return ActionResponse.getSuccessResp("客群标签Id：{" + serialNum + "}删除成功！");
    }
}

