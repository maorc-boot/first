package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdCityCountryQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.IMcdBasiTermService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/jx/term")
@Api(tags = "客户通-筛选条件信息")
@Slf4j
@DataSource("khtmanageusedb")
public class McdBasiTermController {

    @Autowired
    private IMcdBasiTermService mcdBasiTermService;

    /**
     * 查询区县
     *
     * @param query
     * @return
     */
    @PostMapping("/getDimGrid")
    @ApiOperation("网格信息")
    public ActionResponse<McdGridVo> getDimGrid(@RequestBody McdCityCountryQuery query) {
        log.info("查询区县下网格信息【getDimGrid】===》，入参{}", query);
        return ActionResponse.getSuccessResp(mcdBasiTermService.getDimGrid(query));
    }


    /**
     * 查询轨迹预警信息
     *
     * @param query
     * @return
     */
    @PostMapping("/getReportTraceAlarm")
    @ApiOperation("轨迹报表预警数据信息")
    public ActionResponse<McdAlarmVo> getReportAlarm(@RequestBody McdCityCountryQuery query) {
        log.info("查询轨迹报表预警数据信息【getReportAlarm】===》，入参{}", query);
        return ActionResponse.getSuccessResp(mcdBasiTermService.getReportTraceAlarm(query));
    }



    /**
     * 查询区县
     *
     * @param query
     * @return
     */
    @PostMapping("/getReportEnergyAlarm")
    @ApiOperation("能效报表预警数据信息")
    public ActionResponse<McdAlarmVo> getReportEnergyAlarm(@RequestBody McdCityCountryQuery query) {
        log.info("查询能效报表预警数据信息【getReportEnergyAlarm】===》，入参{}", query);
        return ActionResponse.getSuccessResp(mcdBasiTermService.getReportEnergyAlarm(query));
    }

    /**
     * 查询区县
     *
     * @param query
     * @return
     */
    @PostMapping("/getDimCounty")
    @ApiOperation("区县信息")
    public ActionResponse<McdCountryVo> getDimCounty(@RequestBody McdCityCountryQuery query) {
        log.info("查询地市下区县信息【getDimCounty】===》，入参{}", query);
        return ActionResponse.getSuccessResp(mcdBasiTermService.getDimCounty(query.getCityId()));
    }

    /**
     * 查询地市
     *
     * @return
     */
    @PostMapping("/getDimCity")
    @ApiOperation("地市信息")
    public ActionResponse<McdCityVo> getDimCity() {
        log.info("查询地市信息【getDimCity】===》");
        return ActionResponse.getSuccessResp(mcdBasiTermService.getDimcity());
    }

    /**
     * 查询业务场景
     *
     * @return
     */
    @PostMapping("/getScenario")
    @ApiOperation("业务场景查询")
    public ActionResponse<McdScenarioVo> getScenario() {
        log.info("查询业务场景【getScenario】===》");
        return ActionResponse.getSuccessResp(mcdBasiTermService.getScenario());
    }

    /**
     * 查询场景对应的模块类型
     * 注：此接口对应的配置表，配置的是场景细分之后的模块数据，如果场景不需要细分，此处模块字段配置对应场景数据，如：
     * MODULE_TYPE	MODULE_NAME	BUSSINESS_SCENARIO_ID	BUSSINESS_SCENARIO_NAME
     * 80901	    营销模块	    80901	               营销任务
     *
     * @return
     */
    @PostMapping("/getModuleTypes")
    @ApiOperation("业务场景模块类型查询")
    public ActionResponse<McdScenarioTypeVo> getModuleTypes() {
        log.info("业务场景模块类型查询【getModuleTypes】===》");
        return ActionResponse.getSuccessResp(mcdBasiTermService.getModuleTypes());
    }

    /**
     * 查询客户权限（查询外呼日志的权限，不同权限查询出不同的数据）
     * 省级---可以查看所有地市的
     * 市级---可以查看本市及本市下所有地市的
     * 区县---可以查看本区县及本区县下的
     * 网格---可以查看本网格及本网格下的
     *
     * @param request
     * @return
     */
    @PostMapping("/getUserPermissionConfig")
    @ApiOperation("查询客户权限（查询外呼日志的权限，不同权限查询出不同的数据）")
    public ActionResponse<McdCallPermissConfVo> getUserPermissionConfig(HttpServletRequest request) {
        log.info("查询客户权限（查询外呼日志的权限，不同权限查询出不同的数据）【getUserPermissionConfig】===》");
        UserSimpleInfo user = UserUtil.getUser(request);
        return ActionResponse.getSuccessResp(mcdBasiTermService.getCallLogPermissionConf(user.getUserId()));
    }

    /**
     * 查询报表客户权限（查询预警的权限，不同权限查询出不同的数据）
     * 省级---可以查看所有地市的
     * 市级---可以查看本市及本市下所有地市的
     * 区县---可以查看本区县及本区县下的
     * 网格---可以查看本网格及本网格下的
     *
     * @param request
     * @return
     */
    @PostMapping("/getReportUserPermission")
    @ApiOperation("查询报表客户权限（查询预警的权限，不同权限查询出不同的数据）")
    public ActionResponse<McdCallPermissConfVo> getReportUserPermission(HttpServletRequest request) {
        log.info("查询客户权限（查询外呼日志的权限，不同权限查询出不同的数据）【getReportUserPermission】===》");
        UserSimpleInfo user = UserUtil.getUser(request);
        return ActionResponse.getSuccessResp(mcdBasiTermService.getReportUserPermission(user.getUserId()));
    }
}
