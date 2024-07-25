package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;

import cn.hutool.core.date.DateUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontKeyMonitorModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontKeyMonitorQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontKeyMonitorService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * @author ranpf
 * @date 2023-2-17
 * @description 客户通重点监控模块
 */
@RequestMapping("/mcd/front/keyMonitor")
@RestController
@Api(tags = "客户通-重点指标监控")
@DataSource("khtmanageusedb")
public class McdFrontKeyMonitorController  {

    @Autowired
    private McdFrontKeyMonitorService mcdFrontKeyMonitorService;

    @PostMapping("/queryFrontKeyMonitors")
    @ApiOperation("重点指标查询接口")
    public ActionResponse<IPage<McdFrontKeyMonitorModel>> queryFrontKeyMonitors(@RequestBody McdFrontKeyMonitorQuery req ){
        Page  page = new Page();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());

        LambdaQueryWrapper<McdFrontKeyMonitorModel>  keyMonitorWrapper = new LambdaQueryWrapper<>();
        keyMonitorWrapper.ge(StringUtils.isNotEmpty(req.getStartTime()),McdFrontKeyMonitorModel::getMonitorTime,StringUtils.isNotEmpty(req.getStartTime()) ?  DateUtil.parse(req.getStartTime() + " 00:00:00","yyyy-MM-dd HH:mm:ss") : req.getStartTime())
                         .le(StringUtils.isNotEmpty(req.getEndTime()),McdFrontKeyMonitorModel::getMonitorTime,StringUtils.isNotEmpty(req.getEndTime()) ? DateUtil.parse(req.getEndTime() + " 23:59:59","yyyy-MM-dd HH:mm:ss") :req.getEndTime())
                         .like(StringUtils.isNotEmpty(req.getKeyWords()),McdFrontKeyMonitorModel::getMonitorName,req.getKeyWords())
                         .orderByDesc(McdFrontKeyMonitorModel::getMonitorTime);

        IPage<McdFrontKeyMonitorModel> keyMonitorModelIPage = mcdFrontKeyMonitorService.page(page,keyMonitorWrapper);

        return ActionResponse.getSuccessResp(keyMonitorModelIPage);
    }

}
