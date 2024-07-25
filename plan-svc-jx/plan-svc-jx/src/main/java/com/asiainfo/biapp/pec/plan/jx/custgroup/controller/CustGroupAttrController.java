package com.asiainfo.biapp.pec.plan.jx.custgroup.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.model.McdCustgroupAttrList;
import com.asiainfo.biapp.pec.plan.service.IMcdCustgroupAttrListService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author mamp
 * @date 2022/12/20
 */
@Api(tags = "江西:客户群属性(可替换变量)")
@RestController
@RequestMapping("/api/action/custgroup/")
@Slf4j
public class CustGroupAttrController {

    @Resource
    private IMcdCustgroupAttrListService attrListService;

    /**
     * 查询客户群属性(可替换变量)
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询客户群属性(可替换变量)", notes = "查询客户群属性(可替换变量)")
    @PostMapping("/listCustAttr")
    @ResponseBody
    public ActionResponse listCustAttr(@RequestParam(value = "custGroupId") String custGroupId) {
        log.info("查询客户群属性,custGroupId = {} ", custGroupId);
        try {
            QueryWrapper<McdCustgroupAttrList> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(McdCustgroupAttrList::getCustomGroupId, custGroupId);
            return ActionResponse.getSuccessResp(attrListService.list(queryWrapper));
        } catch (Exception e) {
            log.error("查询客户群属性异常,custGroupId = {} ", custGroupId, e);
            return ActionResponse.getFaildResp("查询客户群属性异常");
        }


    }

}
