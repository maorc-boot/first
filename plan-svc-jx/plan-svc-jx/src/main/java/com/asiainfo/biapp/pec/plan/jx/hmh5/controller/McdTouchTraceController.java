package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdAlarmReportQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.IMcdTouchTraceService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdTouchTraceDetailsVo;
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

/**
 * <p>
 * 客户通外呼轨迹 前端控制器
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@RestController
@RequestMapping("/api/jx/trace")
@Api(tags = "客户通-预警轨迹")
@Slf4j
@DataSource("khtmanageusedb")
public class McdTouchTraceController {

    @Autowired
    private IMcdTouchTraceService touchTraceService;

    @PostMapping("/queryTouchTraceList")
    @ApiOperation("预警轨迹查询")
    public ActionResponse<IPage<McdTouchTraceDetailsVo>> queryTouchTraceList(@RequestBody McdAlarmReportQuery query) {
        log.info("查询预警轨迹信息【queryTouchTraceList】===》，入参{}", query);
        return ActionResponse.getSuccessResp(touchTraceService.queryTouchTraceList(query));
    }


    /**
     * 通话明细查询 --完成  请求
     * @return map
     */
    @PostMapping("/exportTrace")
    @ApiOperation("预警轨迹导出")
    public ActionResponse exportTrace(@RequestBody McdAlarmReportQuery query , HttpServletResponse response){
        log.info("预警轨迹导出【exportTraceDetails】===》");
        return ActionResponse.getSuccessResp(touchTraceService.exportTraceDetails(query, response));
    }

}
