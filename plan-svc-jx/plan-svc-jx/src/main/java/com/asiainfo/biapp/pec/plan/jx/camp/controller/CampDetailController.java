package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.jx.camp.model.CampExecInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampBaseInfoJxVO;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampExecReq;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsBaseInfoJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.CampDetailService;
import com.asiainfo.biapp.pec.plan.service.IMcdCampDefService;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author mamp
 * @date 2022/10/28
 */
@Slf4j
@RestController
@RequestMapping("/jx/campDetail")
@Api(tags = "江西:策略详情")
public class CampDetailController {

    @Autowired
    private CampDetailService campDetailService;


    @Resource
    private IMcdCampDefService mcdCampDefService;

    /**
     * 查询策略详细信息
     *
     * @param mcdIdQuery
     * @return
     */
    @ApiOperation(value = "查询策略详细信息", notes = "查询策略详细信息")
    @PostMapping("/getCampInfo")
    public TacticsInfoJx getCampInfo(@RequestBody @Valid McdIdQuery mcdIdQuery) {
        return campDetailService.getCampsegInfo(mcdIdQuery.getId());
    }

    /**
     * 查询策略详细信息
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "查询单个子策略详细信息", notes = "查询单个子策略详细信息")
    @PostMapping("/getChildCampInfo")
    public TacticsInfoJx getChildCampInfo(@RequestBody @Valid McdIdQuery req) {
        return campDetailService.getChildCamp(req.getId());
    }

    @ApiOperation(value = "执行信息查询", notes = "执行信息查询")
    @PostMapping("/queryPreviewExecLog")
    public ActionResponse<List<CampExecInfo>> queryPreviewExecLog(@RequestBody CampExecReq param) {
        log.info("执行信息查询,param :{}", param);
        ActionResponse<List<CampExecInfo>> response = ActionResponse.getSuccessResp("执行信息查询成功");
        try {
            response.setData(campDetailService.queryPreviewExecLog(param));
        } catch (Exception e) {
            log.error("执行信息查询异常:", e);
            response = ActionResponse.getFaildResp(e.getMessage());
        }
        return response;
    }

    /**
     * 查询策略基础详细信息
     *
     * @param mcdIdQuery
     * @return
     */
    @ApiOperation(value = "江西对外查询策略详细信息", notes = "江西对外查询策略详细信息")
    @PostMapping("/getCampBaseInfo")
    public ActionResponse<TacticsBaseInfoJx> getCampBaseInfo(@RequestBody @Valid McdIdQuery mcdIdQuery) {

        log.info("江西第三方查询活动详情接口,活动ID为: "+ mcdIdQuery.getId());

        CampBaseInfoVO campsegBaseInfo = mcdCampDefService.getCampsegBaseInfo(mcdIdQuery.getId());
        if (Objects.isNull(campsegBaseInfo)){

            CampBaseInfoVO campBaseInfoVO = mcdCampDefService.qryByChildId(mcdIdQuery.getId());
            if (Objects.isNull(campBaseInfoVO)){
                return ActionResponse.getFaildResp("无此活动信息!");
            }
            mcdIdQuery.setId(campBaseInfoVO.getCampsegRootId());
        }

        return ActionResponse.getSuccessResp(campDetailService.getCampsegBaseInfo(mcdIdQuery.getId()));

    }


    /**
     * 查询策略基础详细信息
     *
     * @param mcdIdQuery
     * @return
     */
    @ApiOperation(value = "江西对外H5查询策略详细信息", notes = "江西对外H5查询策略详细信息")
    @PostMapping("/viewPolicyDetail")
    public ActionResponse<Map<String, Object>> viewPolicyDetail(@RequestBody @Valid McdIdQuery mcdIdQuery) {

        log.info("江西第三方H5查询活动详情接口,活动ID为: "+ mcdIdQuery.getId());
        Map<String, Object> returnMap = new HashMap<>();
        ActionResponse<Map<String, Object>> actionResponse = new ActionResponse<>();
        Map<String, Object> map = new HashMap<>();

        CampBaseInfoVO campsegBaseInfo = mcdCampDefService.getCampsegBaseInfo(mcdIdQuery.getId());
        if (Objects.isNull(campsegBaseInfo)){

            CampBaseInfoVO campBaseInfoVO = mcdCampDefService.qryByChildId(mcdIdQuery.getId());
            if (Objects.isNull(campBaseInfoVO)){
                returnMap.put("status", "500");
                returnMap.put("data", map);

                actionResponse.setData(returnMap);
                actionResponse.setStatus(ResponseStatus.ERROR);
                log.error("无此活动信息!");
                return actionResponse;
            }
            mcdIdQuery.setId(campBaseInfoVO.getCampsegRootId());
        }

        TacticsBaseInfoJx tacticsBaseInfo = campDetailService.getCampsegBaseInfo(mcdIdQuery.getId());
        if (Objects.isNull(tacticsBaseInfo)){
            returnMap.put("status", "500");
            returnMap.put("data", map);

            actionResponse.setData(returnMap);
            actionResponse.setStatus(ResponseStatus.ERROR);
            log.error("未查询到此活动信息!");
            return actionResponse;
        }

        String isOpenTemplate = (RedisUtils.getDicValue("MCD_TEMPLATE") == null?"":RedisUtils.getDicValue("MCD_TEMPLATE"));
        //是否开启剔除活动
        String isOpenRemoveCampseg = (RedisUtils.getDicValue("MCD_CAMPSEG_REMOVE") == null?"":RedisUtils.getDicValue("MCD_CAMPSEG_REMOVE"));

        CampBaseInfoJxVO baseCampInfo = tacticsBaseInfo.getBaseCampInfo();

        map.put("activityTypeName", baseCampInfo.getActivityTypeName());//活动类型
        map.put("localActivityTypeName", "");//本地类型
        map.put("activityObjectiveName", baseCampInfo.getActivityObjectiveName());//活动目的
        map.put("activityType",baseCampInfo.getActivityType());
        map.put("activityObjective",baseCampInfo.getActivityObjective());
        map.put("campsegId", baseCampInfo.getCampsegId());
        map.put("campsegName", baseCampInfo.getCampsegName());
        map.put("campsegDesc", baseCampInfo.getCampsegDesc());
        map.put("statusName", baseCampInfo.getCampsegStatName());//营销活动状态名
        map.put("startDate", baseCampInfo.getStartDate());
        map.put("endDate", baseCampInfo.getEndDate());
        map.put("createUserid", baseCampInfo.getCreateUserId());
        map.put("createUserName", baseCampInfo.getCreateUserName());
        map.put("campDrvName", baseCampInfo.getActivityServiceType());//业务类型
        map.put("campsegTypeId", baseCampInfo.getCampsegTypeId());
        map.put("campsegTypeName", getCampDrvName(baseCampInfo.getCampsegTypeId()));
        map.put("createTime", baseCampInfo.getCreateTime());
        map.put("activityTemplateName", baseCampInfo.getActivityTemplateId());//营销模板
        map.put("isOpenTemplate", isOpenTemplate);//是否开启营销模板
        map.put("isOpenRemoveCampseg", isOpenRemoveCampseg);
        map.put("needUserName", "");//需求人
        map.put("childMtlCampSeginfo", tacticsBaseInfo.getCampSchemes());


        returnMap.put("status", "200");
        returnMap.put("data", map);

        actionResponse.setData(returnMap);
        actionResponse.setStatus(ResponseStatus.SUCCESS);

        return actionResponse;
    }


    private String getCampDrvName (int typeId){
        String  campDrvName = "";
        if (typeId ==1){
              campDrvName ="策略类";
        }else if (typeId ==2){
              campDrvName ="调研类";
        }
        return campDrvName;
    }

}
