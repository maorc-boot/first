package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import com.asiainfo.biapp.pec.core.common.OutInterface;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.service.IMcdCampDefService;
import com.asiainfo.biapp.pec.plan.service.IMcdCampsegService;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import com.asiainfo.biapp.pec.plan.vo.req.CampStatusQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Api(tags = "审批回调接口")
@Slf4j
@RestController
@RequestMapping("/api/jx/approveResultSave")
public class ApproveResultSaveControllerJx {

    @Resource
    private IMcdCampsegService campsegService;

    @Resource
    private IMcdCampDefService mcdCampDefService;

    @PostMapping(value = "/saveByCampsegId")
    @OutInterface
    @ApiOperation(tags = "审批回调接口",value = "审批回调接口")

    public Map<String, Object> saveApproveResultByCampsegId(@RequestBody Map paramMap) throws Exception {
        log.info("call saveApproveResultByCampsegId method---param <resultData>：" + paramMap);
        Map<String, Object> retMap = new HashMap<String, Object>();
        String resultCode = "";
        String resultDesc = "";

        try {
           // Map paramMap = (Map) JSONUtils.parse(resultData);
            String activityId = String.valueOf(paramMap.get("activityId"));
            // 省级策划一级执行流程会调用两次审批反馈接口，第一次审批结果来源于一级，第二次审批结果来源于专业公司
            String channelSourceType = String.valueOf(paramMap.get("channelSourceType"));
            String approveStatus = String.valueOf(paramMap.get("approveStatus"));
            //String description = String.valueOf(paramMap.get("description"));

            if (StringUtils.isEmpty(activityId) || StringUtils.isEmpty(approveStatus) || StringUtils.isEmpty(channelSourceType)) {
                resultCode = "1";
                resultDesc = "Invalid parameter";
            } else {
                //转换活动编码为mcd活动id
                String proCode = RedisUtils.getDicValue("MCD_UNIFIED_PROVINCE_ID");
                proCode = StringUtils.isEmpty(proCode) ? "001" : proCode;
                activityId = activityId.substring(proCode.length());

                CampBaseInfoVO campsegBaseInfo = mcdCampDefService.getCampsegBaseInfo(activityId);

                if (campsegBaseInfo != null && !StringUtils.isEmpty(campsegBaseInfo.getApproveFlowId())) {
                    // 引用统一策略活动默认返回成功
                    if (StringUtils.isNotEmpty(campsegBaseInfo.getUnityCampsegId())) {
                        resultCode = "0";
                        resultDesc = "Success";
                        retMap.put("resultCode", resultCode);
                        retMap.put("resultDesc", resultDesc);
                        return retMap;
                    }
                    CampStatusQuery req = new CampStatusQuery();
                    req.setCampsegRootId(activityId);
                    int campsegStatId = 0;
                    if("0".equals(approveStatus)){
                        campsegStatId = 41;
                    }else if("1".equals(approveStatus)){
                        campsegStatId = 52;
                    }else {
                        resultCode = "-1";
                        resultDesc = "approveStatus error";
                        retMap.put("resultCode", resultCode);
                        retMap.put("resultDesc", resultDesc);
                        return retMap;
                    }
                    req.setCampStat(campsegStatId);
                    req.setSource(1);
                    if (!"0".equals(channelSourceType)) {
                        // 表示审核结果来源于专业公司，进行审批通过后处理
                        campsegService.modifyCampStatusFromAppr(req);
                    }
                    resultCode = "0";
                    resultDesc = "Success";
                } else {
                    resultCode = "-1";
                    resultDesc = "pCampDef is null.";
                }
            }
        } catch (Exception e) {
            log.error("call saveApproveResultByCampsegId() error:", e);
            resultCode = "-1";
            resultDesc = "Error";
        }
        retMap.put("resultCode", resultCode);
        retMap.put("resultDesc", resultDesc);
        log.info("saveApproveResultByCampsegId method end ---resultMap:{}", retMap);
        return retMap;
    }

}
