package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdAlarmReportQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.IMcdAlarmEnergyService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdAlarmEfficacyVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/jx/energy")
@Api(tags = "客户通-预警能效")
@Slf4j
@DataSource("khtmanageusedb")
public class McdAlarmEnergyController {

    @Autowired
    private IMcdAlarmEnergyService alarmEnergyService;

    @PostMapping("/queryAlarmEnergyList")
    @ApiOperation("预警能效查询")
    public ActionResponse<IPage<McdAlarmEfficacyVo>> queryAlarmEnergyList(@RequestBody McdAlarmReportQuery query) {
        log.info("查询预警能效信息【queryAlarmEnergyList】===》，入参{}", query);
        return ActionResponse.getSuccessResp(alarmEnergyService.queryAlarmEnergyList(query));
    }


    /**
     * 通话明细查询 --完成  请求
     * @return map
     */
    @PostMapping("/exportEnergy")
    @ApiOperation("预警能效导出")
    public ActionResponse exportEnergy(@RequestBody McdAlarmReportQuery query , HttpServletResponse response){
        log.info("预警能效导出【exportEnergy】===》");
        return ActionResponse.getSuccessResp(alarmEnergyService.exportEnergy(query, response));
    }
}
