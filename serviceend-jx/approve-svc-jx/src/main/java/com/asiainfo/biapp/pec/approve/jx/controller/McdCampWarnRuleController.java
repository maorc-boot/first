package com.asiainfo.biapp.pec.approve.jx.controller;


import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.approve.common.CommonPageQueryWrap;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTask;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnRule;
import com.asiainfo.biapp.pec.approve.jx.service.IMcdCampDefService;
import com.asiainfo.biapp.pec.approve.jx.service.McdCampWarnEmisTaskService;
import com.asiainfo.biapp.pec.approve.jx.service.McdCampWarnRuleService;
import com.asiainfo.biapp.pec.approve.jx.utils.EmisUtils;
import com.asiainfo.biapp.pec.approve.jx.utils.IdUtils;
import com.asiainfo.biapp.pec.approve.model.User;
import com.asiainfo.biapp.pec.approve.service.IUserService;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.DateTool;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 策略预警规则 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2023-04-26
 */
@Api(value = "江西:预警规则", tags = "江西:预警规则")
@RestController
@RequestMapping("/mcd-camp-warn-rule")
@Slf4j
public class McdCampWarnRuleController {
    @Resource
    private HttpServletRequest request;
    @Resource
    private McdCampWarnRuleService mcdCampWarnRuleService;
    @Resource
    private IMcdCampDefService mcdCampDefService;
    @Resource
    private McdCampWarnEmisTaskService mcdCampWarnEmisTaskService;
    @Resource
    private EmisUtils emisUtils;
    @Autowired
    private IUserService userService;

    @ApiOperation(value = "新增预警规则", notes = "新增预警规则")
    @PostMapping("/addRule")
    public ActionResponse<String> addRule(@RequestBody McdCampWarnRule rule) {
        log.info("addRule: {}", rule);
        String id = IdUtils.generateId();
        rule.setRuleId(id);
        UserSimpleInfo user = UserUtil.getUser(request);
        if(null != user){
            rule.setCreateUserId(user.getUserId())  ;
            rule.setCreateUserName(user.getUserName());
        }
        try {
            if (mcdCampWarnRuleService.save(rule)) {
                return ActionResponse.getSuccessResp(id);
            }
        } catch (Exception e) {
            log.error("新增预警规则失败:", e);
        }
        return ActionResponse.getFaildResp("新增预警规则失败");
    }

    @ApiOperation(value = "删除预警规则", notes = "删除预警规则")
    @PostMapping("/delRule")
    public ActionResponse<String> delRule(@RequestBody McdCampWarnRule rule) {
        log.info("delRule: {}", rule);
        if (rule == null || StrUtil.isEmpty(rule.getRuleId())) {
            return ActionResponse.getFaildResp("规则ID不能为空");
        }
        String ruleId = rule.getRuleId();
        LambdaQueryWrapper<McdCampWarnRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdCampWarnRule::getRuleId, ruleId);
        try {
            if (mcdCampWarnRuleService.remove(wrapper)) {
                return ActionResponse.getSuccessResp(ruleId);
            }
        } catch (Exception e) {
            log.error("删除预警规则失败:", e);
        }
        return ActionResponse.getFaildResp("删除预警规则失败");
    }

    @ApiOperation(value = "修改预警规则", notes = "修改预警规则")
    @PostMapping("/editRule")
    public ActionResponse<String> editRule(@RequestBody McdCampWarnRule rule) {
        log.info("editRule: {}", rule);
        if (rule == null || StrUtil.isEmpty(rule.getRuleId())) {
            return ActionResponse.getFaildResp("规则ID不能为空");
        }
        String ruleId = rule.getRuleId();
        LambdaUpdateWrapper<McdCampWarnRule> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(McdCampWarnRule::getRuleId, ruleId);
        try {
            if (mcdCampWarnRuleService.updateById(rule)) {
                return ActionResponse.getSuccessResp(ruleId);
            }
        } catch (Exception e) {
            log.error("修改预警规则失败", e);
        }
        return ActionResponse.getFaildResp("修改预警规则失败");
    }

    @ApiOperation(value = "分页查询预警规则", notes = "分页查询预警规则")
    @PostMapping("/listRule")
    public ActionResponse<IPage<McdCampWarnRule>> listRule(@RequestBody CommonPageQueryWrap query) {
        log.info("listRule: {}", query);

        Page page = new Page(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<McdCampWarnRule> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByDesc(McdCampWarnRule::getUpdateTime);
        try {
            return ActionResponse.getSuccessResp(mcdCampWarnRuleService.page(page, queryWrapper));
        } catch (Exception e) {
            log.error("查询预警规则失败", e);
        }
        return ActionResponse.getFaildResp("查询预警规则失败");
    }

    @ApiOperation(value = "查询上线预警规则数量", notes = "查询上线预警规则数量")
    @PostMapping("/queryOnlineRuleSize")
    public ActionResponse<Integer> listRule() {
        log.info("queryOnlineRuleSize: ");

        LambdaQueryWrapper<McdCampWarnRule> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(McdCampWarnRule::getIsOnling, 1);
        try {
            return ActionResponse.getSuccessResp(mcdCampWarnRuleService.count(queryWrapper));
        } catch (Exception e) {
            log.error("查询上线预警规则数量失败", e);
        }
        return ActionResponse.getFaildResp("查询上线预警规则数量失败");
    }


    @ApiOperation(value = "预警规则生成代办任务", notes = "预警规则生成代办任务")
    @PostMapping("/campWarnRuleTask")
    public void campWarnRuleTask() {
        log.info("CampWarningRuleTask运营预警规则任务启动");

        LambdaQueryWrapper<McdCampWarnRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdCampWarnRule::getIsOnling, 1).last(" limit 1 ");
        McdCampWarnRule campWarnRule = mcdCampWarnRuleService.getOne(wrapper);

        int cycleType = campWarnRule.getCycleType();
        int cycleDays = campWarnRule.getCycleDays();
        String sign = campWarnRule.getSign();
        double rate = campWarnRule.getThreshold().doubleValue();
        String compType = "<,≤,<=".contains(sign) ? "2":"1";

        String ruleId =campWarnRule.getRuleId();

        //获取当前及周期日期
        String cycleEndDate = getTargetYearMonthDay("yyyy-MM-dd",0);
        String cycleStartDate = getTargetYearMonthDay("yyyy-MM-dd",-cycleDays);

        LambdaUpdateWrapper<McdCampWarnRule> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(McdCampWarnRule::getUpdateTime,new Date())
                      .set(McdCampWarnRule::getExecuteTime,new Date())
                      .eq(McdCampWarnRule::getRuleId,ruleId);
        //修改预警规则执行时间
        mcdCampWarnRuleService.update(updateWrapper);


        List<Map<String, Object>> campsegList =   mcdCampDefService.getCampsegInfos(cycleType,cycleStartDate,cycleEndDate,cycleDays);
        if (CollectionUtils.isEmpty(campsegList)){
            log.info("预警规则任务没有符合的执行中活动!");
            return;
        }

        List<McdCampWarnEmisTask> warnEmisList = new ArrayList<>();
        for(int i=0; i<campsegList.size(); i++){
        McdCampWarnEmisTask warnEmis = new McdCampWarnEmisTask();
        Map<String, Object> campMap  = campsegList.get(i);

        String campsegPId =  campMap.get("CAMPSEG_ROOT_ID")  == null ? "" :  campMap.get("CAMPSEG_ROOT_ID").toString();//父策略ID
        String campsegId =  campMap.get("CAMPSEG_ID")  == null ? "" :  campMap.get("CAMPSEG_ID").toString();//子策略ID
        String campsegName =  campMap.get("CAMPSEG_NAME")  == null ? "" :  campMap.get("CAMPSEG_NAME").toString();
        String createUserid =  campMap.get("CREATE_USER_ID")  == null ? "" :  campMap.get("CREATE_USER_ID").toString();
        String cityId =  campMap.get("CITY_ID")  == null ? "" :  campMap.get("CITY_ID").toString();
        String deptId =  campMap.get("DEPTID")  == null ? "" :  campMap.get("DEPTID").toString();


        List<Map<String, Object>> list = mcdCampDefService.queryWarnInfoByTarget(rate,compType,campsegPId);

        if (CollectionUtils.isEmpty(list)){
            continue;
        }

        for (Map<String,Object> effMap:list){

            int customeNum = effMap.get("SGMTNUM") == null ? 0: ((BigDecimal)effMap.get("SGMTNUM")).intValue();
            BigDecimal campRate = effMap.get("RATE") == null ? BigDecimal.ZERO :  (BigDecimal)effMap.get("RATE") ;
            double succContactRatio = effMap.get("SUCCCONTACTRATIO") == null ? 0.0:((BigDecimal)effMap.get("SUCCCONTACTRATIO")).doubleValue();
            String uuId = getWarnEmisUuid(campsegId,createUserid);
            warnEmis.setUniqueIdentifierId(uuId);
            warnEmis.setCampsegId(campsegId);
            warnEmis.setCampsegPId(campsegPId);
            warnEmis.setCampsegName(campsegName);
            warnEmis.setIssuedCustomerNum(customeNum);
            warnEmis.setContactRate(BigDecimal.valueOf(succContactRatio));
            warnEmis.setMarketSuccessRate( campRate);
            warnEmis.setWarningTime(new Date());
            warnEmis.setCreateTime(new Date());
            warnEmis.setCreater(createUserid);
            warnEmis.setCityId(cityId);
            warnEmis.setDepartmentId(deptId);
            warnEmis.setStatus(0);

            warnEmisList.add(warnEmis);
        }


    }

		try {
            if(CollectionUtils.isNotEmpty(warnEmisList)){
                mcdCampWarnEmisTaskService.saveBatch(warnEmisList);
                //调用emis待办
                addCampWarnEsisTask(warnEmisList);
                log.info("生成运营预警代办数据完成,总条数 "+ warnEmisList.size());
            }
        } catch (Exception e) {
        log.error("生成运营预警代办数据异常 ", e);
        }

    }


    private void addCampWarnEsisTask(List<McdCampWarnEmisTask> warnEmisList) throws Exception {
        for (McdCampWarnEmisTask campWarnEmisTask : warnEmisList) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUserId,campWarnEmisTask.getCreater());
            User user = userService.getOne(wrapper);

            // 新增预警待办
            emisUtils.addCampWarnMessage(campWarnEmisTask, campWarnEmisTask.getCampsegName(), user);
        }
    }

    private String  getWarnEmisUuid(String campsegId ,String creater )   {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmSSsss");
        String sgTimestamp = simpleDateFormat.format(new Date());
        String warnEmisUuid =  campsegId +"_"+ creater +"_"+ sgTimestamp ;

        return warnEmisUuid;

    }

    private String getTargetYearMonthDay(String dateStr,int days){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        return new SimpleDateFormat( dateStr ).format(cal.getTime());
    }
}

