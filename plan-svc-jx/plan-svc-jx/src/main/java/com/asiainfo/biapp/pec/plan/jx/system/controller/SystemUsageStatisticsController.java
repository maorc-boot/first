package com.asiainfo.biapp.pec.plan.jx.system.controller;

import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageCityDTO;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageDeptDTO;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsageDetail;
import com.asiainfo.biapp.pec.plan.jx.system.model.SystemUsagePersonageDTO;
import com.asiainfo.biapp.pec.plan.jx.system.req.SystemUsageDetailReqQuery;
import com.asiainfo.biapp.pec.plan.jx.system.req.SystemUsageReqQuery;
import com.asiainfo.biapp.pec.plan.jx.system.service.ISystemUsageService;
import com.asiainfo.biapp.pec.plan.jx.utils.DateTool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags ="江西:系统使用情况相关接口")
@RestController
@RequestMapping("/api/action/systemUsage")
public class SystemUsageStatisticsController {

     @Autowired
    ISystemUsageService systemUsageService;


    @ApiOperation(value = "江西:系统使用情况区域查询",notes = "系统使用情况区域查询")
    @PostMapping(value = "/querySystemUsageCityList")
    @ResponseBody
    public IPage<SystemUsageCityDTO> querySystemUsageCityList(@RequestBody SystemUsageReqQuery reqQuery )  {

        if (StringUtils.isEmpty(reqQuery.getBeginTime()) || StringUtils.isEmpty(reqQuery.getEndTime())){
            reqQuery.setBeginTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",-2));
            reqQuery.setEndTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",0));
        }

        return systemUsageService.querySystemUsageCityList(reqQuery);

    }



    @ApiOperation(value = "江西:系统使用情况个人查询",notes = "系统使用情况个人查询")
    @PostMapping(value = "/querySystemUsagePersonageList")
    @ResponseBody
    public IPage<SystemUsagePersonageDTO> querySystemUsagePersonageList(@RequestBody SystemUsageReqQuery reqQuery )  {

        if (StringUtils.isEmpty(reqQuery.getBeginTime()) || StringUtils.isEmpty(reqQuery.getEndTime())){
            reqQuery.setBeginTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",-2));
            reqQuery.setEndTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",0));
        }

        return systemUsageService.querySystemUsagePersonageList(reqQuery);

    }



    @ApiOperation(value = "江西:系统使用情况部门查询",notes = "系统使用情况部门查询")
    @PostMapping(value = "/querySystemUsageDeptList")
    @ResponseBody
    public IPage<SystemUsageDeptDTO> querySystemUsageDeptList(@RequestBody SystemUsageReqQuery reqQuery )  {

        if (StringUtils.isEmpty(reqQuery.getBeginTime()) || StringUtils.isEmpty(reqQuery.getEndTime())){
            reqQuery.setBeginTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",-2));
            reqQuery.setEndTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",0));
        }

        return systemUsageService.querySystemUsageDeptList(reqQuery);

    }



    @ApiOperation(value = "江西:系统使用情况区域指标详情查询",notes = "系统使用情况区域指标详情查询")
    @PostMapping(value = "/getUsageCityDetailList")
    @ResponseBody
    public IPage<SystemUsageDetail> getUsageCityDetailList(@RequestBody SystemUsageDetailReqQuery reqQuery )  {

        if (StringUtils.isEmpty(reqQuery.getBeginTime()) || StringUtils.isEmpty(reqQuery.getEndTime())){
            reqQuery.setBeginTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",-2));
            reqQuery.setEndTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",0));
        }

        return systemUsageService.getUsageCityDetailList(reqQuery);

    }


    @ApiOperation(value = "江西:系统使用情况部门指标详情查询",notes = "系统使用情况部门指标详情查询")
    @PostMapping(value = "/getUsageDeptDetailList")
    @ResponseBody
    public IPage<SystemUsageDetail> getUsageDeptDetailList(@RequestBody SystemUsageDetailReqQuery reqQuery )  {

        if (StringUtils.isEmpty(reqQuery.getBeginTime()) || StringUtils.isEmpty(reqQuery.getEndTime())){
            reqQuery.setBeginTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",-2));
            reqQuery.setEndTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",0));
        }

        return systemUsageService.getUsageDeptDetailList(reqQuery);

    }


    @ApiOperation(value = "江西:系统使用情况个人指标详情查询",notes = "系统使用情况个人指标详情查询")
    @PostMapping(value = "/getUsagePersonageDetailList")
    @ResponseBody
    public IPage<SystemUsageDetail> getUsagePersonageDetailList(@RequestBody SystemUsageDetailReqQuery reqQuery )  {

        if (StringUtils.isEmpty(reqQuery.getBeginTime()) || StringUtils.isEmpty(reqQuery.getEndTime())){
            reqQuery.setBeginTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",-2));
            reqQuery.setEndTime(DateTool.getTargetYearMonthDay("yyyy-MM-dd",0));
        }

        return systemUsageService.getUsagePersonageDetailList(reqQuery);

    }

}
