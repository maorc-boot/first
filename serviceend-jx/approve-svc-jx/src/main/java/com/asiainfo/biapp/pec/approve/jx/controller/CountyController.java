package com.asiainfo.biapp.pec.approve.jx.controller;


import com.asiainfo.biapp.pec.approve.jx.model.County;
import com.asiainfo.biapp.pec.approve.jx.service.CountyService;
import com.asiainfo.biapp.pec.approve.jx.vo.AreaQueryVo;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 区县维表 前端控制器
 * </p>
 *
 * @author feify
 * @since 2022-06-02
 */
@RestController
@RequestMapping("/api/county")
@Api(value = "江西:区县管理服务", tags = {"江西:区县管理服务"})
@Slf4j
public class CountyController {

    @Autowired
    private CountyService countyService;

    @ApiOperation(value = "查询所有区县信息", notes = "目前不需要参数，查询所有区县列表，多用作下拉")
    @PostMapping("/list")
    public ActionResponse<List<County>> list(){
        List<County> list = countyService.lambdaQuery().orderByAsc(County::getCountyId).list();
        return ActionResponse.getSuccessResp(list);
    }

    @ApiOperation(value = "查询区县信息", notes = "查询某地市下所有区县")
    @PostMapping(path = "/listByCityID")
    public ActionResponse<List<County>> listByCityID(@RequestBody AreaQueryVo areaQueryVo){
        List<County> list = countyService.lambdaQuery().eq(County::getCityId, areaQueryVo.getCityId()).orderByAsc(County::getCountyId).list();
        return ActionResponse.getSuccessResp(list);
    }

}
