package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller;


import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.McdAppAlarmInfoService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmDetailResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmTypeResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 预警信息表 前端控制器
 * </p>
 *
 * @author chenlin
 * @since 2023-06-28
 */
@RestController
@RequestMapping("/mcdAppAlarmInfo")
@Api(tags = "客户通-自定义预警")
@Slf4j
@Validated
public class McdAppAlarmInfoController {

    @Autowired
    private McdAppAlarmInfoService alarmInfoService;

    @ApiOperation("自定义预警页面查询")
    @GetMapping
    @DataSource("khtmanageusedb")
    public ActionResponse<Page<SelectAlarmResultInfo>> selectAlarm(@Validated SelectAlarmParam alarmParam) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在查询客户通自定义预警，关键字：{}！", user.getUserName(), alarmParam.getKeywords());
        Page<SelectAlarmResultInfo> selfAlarms = alarmInfoService.selectAlarm(alarmParam);
        return ActionResponse.getSuccessResp(selfAlarms);
    }

    @ApiOperation("根据alarmId查询预警详情")
    @ApiImplicitParam(name = "alarmId", value = "根据alarmId查询预警详情", example = "1")
    @GetMapping("/selectAlarmDetailById")
    @DataSource("khtmanageusedb")
    public ActionResponse<SelectAlarmDetailResultInfo> selectAlarmDetailById(
            @Min(value = 1, message = "无效的alarmId值！") @RequestParam Integer alarmId
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在查询客户通自定义预警详情，alarmId值：{}！", user.getUserName(), alarmId);
        SelectAlarmDetailResultInfo alarmDetail = alarmInfoService.selectAlarmDetailById(alarmId);
        return ActionResponse.getSuccessResp(alarmDetail);
    }


    @ApiOperation("查询客户通自定义预警类别（新建预警页面时，用作下拉展示）")
    @GetMapping("/selectAlarmType")
    @DataSource("khtmanageusedb")
    public ActionResponse<List<SelectAlarmTypeResult>> selectAlarmType() {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在查询江西客户通自定义预警类别（新建预警页面时）！", user.getUserName());
        List<SelectAlarmTypeResult> alarmTypes = alarmInfoService.selectAlarmType();
        return ActionResponse.getSuccessResp(alarmTypes);
    }

    @ApiOperation("新增客户通自定义预警（新建预警保存时）")
    @PostMapping
    @DataSource("khtmanageusedb")
    public ActionResponse<String> addAlarm(@RequestBody @Validated @ApiParam("新增客户通自定义预警参数") AddAlarmParam alarmParam) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在新增客户通自定义预警（新建预警保存时），请求参数为：{}！", user.getUserName(), alarmParam);
        if (user.getUserId() == null) {
            user.setUserId("admin01");
            user.setCityId("999");
        }
        Integer alarmId = alarmInfoService.addAlarm(user, alarmParam);
        return ActionResponse.getSuccessResp("新增自定义预警成功！alarmId = " + alarmId);
    }

    @ApiOperation("修改客户通自定义预警（修改预警保存时）")
    @PutMapping
    @DataSource("khtmanageusedb")
    public ActionResponse<String> modifyAlarm(@RequestBody @Validated @ApiParam("修改客户通自定义预警参数") ModifyAlarmParam alarmParam) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在修改客户通自定义预警（新建预警保存时），请求参数为：{}！", user.getUserName(), alarmParam);
        if (user.getUserId() == null) {
            user.setUserId("admin01");
            user.setCityId("999");
        }
        alarmInfoService.modifyAlarm(user, alarmParam);
        return ActionResponse.getSuccessResp("修改自定义预警成功！alarmId = " + alarmParam.getAlarmId());
    }

    @ApiOperation("根据alarmId值终止自定义预警")
    @PutMapping("/terminateAlarm")
    @ApiImplicitParam(name = "alarmId", value = "要终止的预警alarmId值", example = "1")
    @DataSource("khtmanageusedb")
    public ActionResponse<String> terminateAlarm(
            @NotNull(message = "预警ID值不可为空！") @Min(value = 1, message = "无效的预警ID值！") Integer alarmId
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在终止客户通自定义预警，删除的预警alarmId值为：{}！", user.getUserName(), alarmId);
        if (user.getUserId() == null) {
            user.setUserId("admin01");
            user.setCityId("999");
        }
        alarmInfoService.terminateAlarm(user, alarmId);
        return ActionResponse.getSuccessResp("终止自定义预警成功！");
    }

    /*
     * @param alarmStatusParam:
     * @return ActionResponse<String>:
     * @author chenlin
     * @description feign调用的接口，用于修改预警状态
     * @date 2023/7/9 21:45
     */
    @ApiIgnore
    @PutMapping("/modifyAlarmStatus")
    @DataSource("khtmanageusedb")
    public ActionResponse<String> modifyAlarmStatus(@RequestBody @Validated ModifyAlarmStatusParam alarmStatusParam) {
        UserSimpleInfo user = UserUtilJx.getUser();
        Integer approveStatus = alarmStatusParam.getApproveStatus();
        log.info("用户：{}正在审核预警，预警alarmId值为：{}，审核结果为：{}！", user.getUserName(), alarmStatusParam.getAlarmId(),
                approveStatus == 1 ? "审批完成" : "审批驳回");
        if (user.getUserId() == null) {
            log.error("没有获取到登录用户，正在创建虚拟用户！--------------------------------------------------------------");
            user.setUserId("admin01");
            user.setCityId("999");
        }
        alarmInfoService.modifyAlarmStatus(alarmStatusParam);
        return ActionResponse.getSuccessResp("修改自定义预警状态成功！");
    }

    /*
     * @param alarmStatusParam:
     * @return ActionResponse<String>:
     * @author chenlin
     * @description feign调用的接口，用于查询审批流程id
     * @date 2023/7/10 11:43
     */
    @ApiIgnore
    @GetMapping("/selectAlarmApproveFlowId")
    @DataSource("khtmanageusedb")
    public ActionResponse<String> selectAlarmApproveFlowId(
            @NotNull(message = "预警ID值不可为空！") @Min(value = 1, message = "无效的预警ID值！") Integer alarmId
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在查询预警审批流程ID，alarmId值为：{}！", user.getUserName(), alarmId);
        if (user.getUserId() == null) {
            log.error("没有获取到登录用户，正在创建虚拟用户！--------------------------------------------------------------");
            user.setUserId("admin01");
            user.setCityId("999");
        }
        String approveFlowId = alarmInfoService.selectAlarmApproveFlowId(alarmId);
        ActionResponse successResp = ActionResponse.getSuccessResp();
        successResp.setData(approveFlowId);
        return successResp;
    }


    @ApiOperation("根据alarmId值删除自定义预警")
    @DeleteMapping
    @ApiImplicitParam(name = "alarmId", value = "要删除的预警alarmId值", example = "1")
    @DataSource("khtmanageusedb")
    public ActionResponse<String> deleteAlarm(
            @RequestParam("alarmId") @Min(value = 1, message = "无效的预警ID值！") Integer alarmId
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在删除客户通自定义预警，删除的预警alarmId值为：{}！", user.getUserName(), alarmId);
        if (user.getUserId() == null) {
            user.setUserId("admin01");
            user.setCityId("999");
        }
        alarmInfoService.deleteAlarm(user, alarmId);
        return ActionResponse.getSuccessResp("删除自定义预警成功！");
    }


    @ApiOperation("获取流程实例节点下级审批人，点击提交审批申请时")
    @GetMapping("/getNodeApprover")
    public ActionResponse<SubmitProcessJxDTO> getNodeApprover() {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在获取获取流程实例节点下级审批人！", user.getUserName());
        return alarmInfoService.getNodeApprover();
    }

    @ApiOperation("提交审批申请")
    @PostMapping("/commitProcess")
    public ActionResponse<String> commitProcess(
            @RequestBody @Validated @ApiParam("自定义预警提交审批申请参数") AlarmCommitProcessParam processParam
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在提交自定义预警审批申请，alarmId：{}！", user.getUserName(), processParam.getAlarmId());
        if (user.getUserId() == null) user.setUserId("admin01");    //TODO 2023-07-11 18:19:46 创建临时用户
        alarmInfoService.commitProcess(processParam, user);
        return ActionResponse.getSuccessResp("提交审批申请成功！");
    }


    @ApiOperation("根据数据源名称和表名称查询表结构")
    @GetMapping("/queryStructure") // 暂时不用
    public ActionResponse<Map<String, Object>> queryStructure(
            @NotBlank(message = "数据源名称不可为空！") String dataSourceName,
            @NotBlank(message = "表名称不可为空！") @Pattern(regexp = "\\S+", message = "表名称不能含有空字符！") String tableName
    ) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在验证预警表字段，数据源：{}，表名：{}！", user.getUserName(), dataSourceName, tableName);

        Map<String, Object> queryResult = alarmInfoService.queryStructure(dataSourceName, tableName);

        return ActionResponse.getSuccessResp(queryResult);
    }

    @ApiOperation("获取数据源列表")
    @GetMapping("/queryDataSourceList")
    public ActionResponse<Map<String, String>> queryDataSourceList() {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在获取数据源列表！", user.getUserName());

        Map<String, String> dataSourceList = alarmInfoService.queryDataSourceList();

        return ActionResponse.getSuccessResp(dataSourceList);
    }

    @ApiOperation(value = "根据数据源名称获取数据库中的所有表", notes = "建议：下拉选项时显示表名称，鼠标停留时显示批注")
    @GetMapping("/queryDataTableList") // 暂时不用
    public ActionResponse<Map<String, String>> queryDataTableList(@NotBlank(message = "数据源名称不可为空！") String dataSourceName) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户：{}正在查询所有表名称，数据库为：{}！", user.getUserName(), dataSourceName);

        Map<String, String> tableMap = alarmInfoService.queryDataTableList(dataSourceName);

        return ActionResponse.getSuccessResp(tableMap);
    }

    @GetMapping(value = "/querySourceTable")
    @ApiOperation(value = "根据数据源名称和表名称验证表名", notes = "根据数据源名称和表名称验证表名以及获取表的变量信息")
    public ActionResponse<List<Map<String, Object>>> querySourceTable(@NotBlank(message = "数据源名称不可为空！") @DataSource String dataSourceName,
                                                                      @NotBlank(message = "表名称不可为空！") @Pattern(regexp = "\\S+", message = "表名称不能含有空字符！") String tableName){
        log.info("根据数据源名称和表名称验证表名，数据源：{}，表名：{}！", dataSourceName, tableName);
        return alarmInfoService.querySourceTable(dataSourceName, tableName);
    }

    @PostMapping("/batchImportAutoClearAlarm")
    @ApiOperation(value = "解警产品批量导入", notes = "解警产品批量导入(全量数据覆盖)")
    @DataSource("khtmanageusedb")
    public ActionResponse batchImportAutoClearAlarm(@RequestBody MultipartFile multipartFile) {
        UserSimpleInfo user = UserUtilJx.getUser();
        if (user.getUserId() == null) user.setUserId("admin01"); user.setCityId("999");
        try {
            alarmInfoService.batchImportAutoClearAlarm(multipartFile, user);
        } catch (Exception e) {
            log.error("导入自动解警数据文件异常", e);
            return ActionResponse.getFaildResp("导入自动解警数据文件异常");
        }
        return ActionResponse.getSuccessResp("导入自动解警数据文件成功");
    }

    @GetMapping("/queryHasRole")
    @ApiOperation(value = "根据用户id查询角色id", notes = "根据用户id查询角色id-校验是否有解警产品上传权限")
    @DataSource("khtmanageusedb")
    public ActionResponse queryHasRole() {
        UserSimpleInfo user = UserUtilJx.getUser();
        if (user.getUserId() == null) user.setUserId("admin01");
        List<String> userRoles = alarmInfoService.getUserRoleList(user.getUserId());
        // 网格短信模板配置角色
        String autoClearAlarmRoleId = RedisUtils.getDicValue("auto_clear_alarm_role_id");
        log.info("用户={}正在查询是否有解警产品上传权限userRoles={},autoClearAlarmRoleId={}", user.getUserId(), JSONUtil.toJsonStr(userRoles), autoClearAlarmRoleId);
        if(StringUtils.isBlank(autoClearAlarmRoleId) || !userRoles.contains(autoClearAlarmRoleId)){
            return ActionResponse.getSuccessResp("没有解警产品上传权限").setData(false);
        } else {
            return ActionResponse.getSuccessResp("具有解警产品上传权限").setData(true);
        }
    }

    @PostMapping("/queryApprovingAlarm")
    @ApiOperation(value = "查询审批中的预警信息", notes = "查询审批中的预警信息(预警待审批列表接口使用)")
    @DataSource("khtmanageusedb")
    public ActionResponse<Page<SelectAlarmResultInfo>> queryApprovingAlarm(@RequestBody McdCampApproveJxNewQuery query) {
        log.info("查询审批中的预警信息入参={}", JSONUtil.toJsonStr(query));
        Page<SelectAlarmResultInfo> selfAlarms = alarmInfoService.queryApprovingAlarm(query);
        return ActionResponse.getSuccessResp(selfAlarms);
    }

    @PostMapping("/queryAlarmNameAndCreator")
    @ApiOperation(value = "查询预警名称以及创建人信息", notes = "查询预警名称以及创建人信息(审批驳回发送emis阅知待办使用)")
    @DataSource("khtmanageusedb")
    public ActionResponse<Map<String, Object>> queryAlarmNameAndCreator(@RequestBody McdIdQuery query) {
        log.info("查询预警名称以及创建人信息入参={}", JSONUtil.toJsonStr(query));
        Map<String, Object> alarmNameAndCreator = alarmInfoService.queryAlarmNameAndCreator(query.getId());
        return ActionResponse.getSuccessResp(alarmNameAndCreator);
    }

    @PostMapping("/updateAlarmThreshold")
    @ApiOperation(value = "更新预警阈值信息", notes = "更新预警阈值信息)")
    @DataSource("khtmanageusedb")
    public ActionResponse updateAlarmThreshold(@RequestBody McdCityThreshParam param) {
        log.info("更新预警阈值信息入参={}", JSONUtil.toJsonStr(param));
        return ActionResponse.getSuccessResp(alarmInfoService.updateThreshold(param));
    }

    /**
     * 创建活动页面：初始化产品类型模块
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "所有地市查询", notes = "所有地市查询")
    @PostMapping(value = "/queryAllCitys")
    @DataSource("khtmanageusedb")
    public ActionResponse queryAllCitys() {
        log.info("获取自定义预警地市信息【mcd_dim_city】");
        return ActionResponse.getSuccessResp(alarmInfoService.queryAllCitys());
    }
}

