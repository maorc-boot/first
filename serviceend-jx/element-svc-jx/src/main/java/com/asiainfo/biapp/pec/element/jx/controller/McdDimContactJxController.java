package com.asiainfo.biapp.pec.element.jx.controller;

import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.element.dto.request.DimContactDetailRequest;
import com.asiainfo.biapp.pec.element.jx.entity.McdDimContactJx;
import com.asiainfo.biapp.pec.element.jx.service.IMcdDimContactJxService;
import com.asiainfo.biapp.pec.element.jx.vo.DimContactDetailResponseJx;
import com.asiainfo.biapp.pec.element.jx.vo.DimContactPageListRequestJx;
import com.asiainfo.biapp.pec.element.service.IMcdDimContactService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 江西触点管理(政企)
 *
 * @author lvcc
 * @date 2024/04/12
 */
@RestController
@RequestMapping("/api/jx/mcdDimContact")
@Api(value = "触点信息管理服务", tags = {"触点管理服务"})
@Slf4j
public class McdDimContactJxController {
    
    @Resource
    private IMcdDimContactService mcdDimContactService;

    @Resource
    private IMcdDimContactJxService mcdDimContactJxService;
    
    /**
     * 触点信息列表展示(或根据条件)
     *
     * @return 触点信息列表
     */
    @PostMapping(path = "/pagelistMcdDimContact")
    @ApiOperation(value = "触点信息列表查询",notes = "触点信息列表查询")
    public ActionResponse<IPage<DimContactDetailResponseJx>> pagelistMcdDimContact(@RequestBody DimContactPageListRequestJx dimContactPageListRequest){
        log.info("start queryMcdDimContactPageList param:{}",new JSONObject(dimContactPageListRequest));
        return ActionResponse.getSuccessResp(mcdDimContactJxService.pagelistMcdDimContact(dimContactPageListRequest));
    }

    /**
     * 查询触点信息详情
     */
    @PostMapping(value = "/getDimContactDetail")
    @ApiOperation(value = "查询触点详情",notes = "查询触点详情")
    public ActionResponse<DimContactDetailResponseJx> getDimContactDetail(@RequestBody DimContactDetailRequest dimContactDetailRequest) {
        log.info("start getContactDetailById param:{}",new JSONObject(dimContactDetailRequest));
        return ActionResponse.getSuccessResp(mcdDimContactJxService.getContactDetailById(dimContactDetailRequest.getContactId()));
    }
    
    /**
     * 保存或更新触点信息
     *
     * @param mcdDimContact 入参
     * @return
     */
    @PostMapping(path = "/saveOrUpdateContact")
    @ApiOperation(value = "保存或更新触点",notes = "保存或更新触点")
    public ActionResponse<Boolean> saveOrUpdateContact(@RequestBody McdDimContactJx mcdDimContact) {
        log.info("start saveOrUpdateContact param:{}",new JSONObject(mcdDimContact));
        return ActionResponse.getSuccessResp(mcdDimContactJxService.saveOrUpdateContact(mcdDimContact));
    }
}
