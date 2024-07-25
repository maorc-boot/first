package com.asiainfo.biapp.pec.element.jx.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.element.jx.entity.McdPlanExclusivity;
import com.asiainfo.biapp.pec.element.jx.model.McdPlanDef;
import com.asiainfo.biapp.pec.element.jx.model.McdPlanDefInfo;
import com.asiainfo.biapp.pec.element.jx.model.McdPlanGroup;
import com.asiainfo.biapp.pec.element.jx.query.PlanExcluQuery;
import com.asiainfo.biapp.pec.element.jx.query.PlanIdQuery;
import com.asiainfo.biapp.pec.element.jx.query.RequestPlanQuery;
import com.asiainfo.biapp.pec.element.jx.service.McdPlanDefService;
import com.asiainfo.biapp.pec.element.jx.service.McdPlanExclusivityService;
import com.asiainfo.biapp.pec.element.jx.service.McdPlanGroupService;
import com.asiainfo.biapp.pec.element.util.CommonUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 产品互斥关系表 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2022-10-18
 */
@RestController
@RequestMapping("/mcd-plan-exclusivity")
@Slf4j
@Api(value = "江西:互斥产品", tags = {"江西:互斥产品"})
public class McdPlanExclusivityController {

    @Autowired
    private McdPlanExclusivityService planExclusivityService;
    @Autowired
    private McdPlanDefService mcdPlanDefService;
    @Autowired
    private McdPlanGroupService mcdPlanGroupService;


    @ApiOperation(value = "根据主产品ID和互斥来源查询互斥产品", notes = "根据主产品ID和互斥来源查询互斥产品,planId:主产品ID,sourceType: 互斥产品来源，0-自定义，1-CRM")
    @PostMapping("/queryPlanExclusivity")
    private ActionResponse<McdPlanExclusivity> queryPlanExclusivity(@RequestParam(value = "planId", required = false) String planId, @RequestParam(value = "sourcetype", required = false) Integer sourceType) {

        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            LambdaQueryWrapper<McdPlanExclusivity> wrapper = new LambdaQueryWrapper<>();
            if (StrUtil.isNotEmpty(planId)) {
                wrapper.eq(McdPlanExclusivity::getPlanId, planId);
            }
            if (sourceType != null) {
                wrapper.eq(McdPlanExclusivity::getSourceType, sourceType);
            }
            resp.setData(planExclusivityService.list(wrapper));
        } catch (Exception e) {
            log.error("根据主产品ID和互斥来源查询互斥产品失败", e);
            resp.setStatus(ResponseStatus.ERROR);
        }
        return resp;
    }

    @PostMapping(path = "/queryPlanDefList")
    @ApiOperation(value = "新建互斥产品列表查询", notes = "新建互斥产品列表查询")
    @ResponseBody
    public ActionResponse<IPage<McdPlanDefInfo>> queryPlanDefList(@RequestBody RequestPlanQuery planQuery) {
        log.info("start queryPlanDefList para:{}", new JSONObject(planQuery));
        return ActionResponse.getSuccessResp(mcdPlanDefService.queryPlanDefList(planQuery));
    }

    @ApiOperation(value = "江西互斥产品管理查询",notes = "江西互斥产品管理查询")
    @PostMapping("/getPlanExclusivityList")
    @ResponseBody
    public ActionResponse<IPage<McdPlanExclusivity>> getPlanExclusivityList (@RequestBody McdPageQuery req){

        return ActionResponse.getSuccessResp(planExclusivityService.getPlanExclusivityList(req));

    }


    @ApiOperation(value = "江西互斥产品查看根据主产品ID",notes = "江西互斥产品查看根据主产品ID")
    @PostMapping("/getPlanExclusivityInfo")
    @ResponseBody
    public ActionResponse getPlanExclusivityInfo (@RequestBody PlanExcluQuery req){

        return ActionResponse.getSuccessResp(planExclusivityService.getPlanExclusivityInfo(req));

    }


    @ApiOperation(value = "江西新建主互斥产品关系",notes = "江西新建主互斥产品关系")
    @PostMapping("/savePlanExclusivity")
    @ResponseBody
    public ActionResponse savePlanExclusivity(@RequestBody McdPlanExclusivity req){

        if (StringUtils.isNotEmpty(req.getExPlanId())){
            String pgId = CommonUtil.generateId();
            //保存产品组信息
            McdPlanGroup planGroup = new McdPlanGroup();
            planGroup.setPlanGroupId(pgId);
            planGroup.setPlanGroupName(req.getPlanId());
            planGroup.setPlanGroupType("I");
            planGroup.setSourceType(0);
            mcdPlanGroupService.save(planGroup);

            //保存主,互斥产品
            String[] exPlanIds = req.getExPlanId().split(",");
            String[] exPlanNames = req.getExPlanName().split(",");
            int t = 0;
            for (String exPlanId : exPlanIds) {
                McdPlanExclusivity planExclusivity = new McdPlanExclusivity();
                planExclusivity.setPlanId(exPlanId);
                planExclusivity.setPlanName(exPlanNames[t]);
                planExclusivity.setPlanGroupId(pgId);
                planExclusivity.setSourceType(0);//0  系统
                planExclusivity.setType(0);//0 互斥产品

                planExclusivityService.save(planExclusivity);

                t++;
            }

            McdPlanExclusivity planExclusivity = new McdPlanExclusivity();
            planExclusivity.setPlanId(req.getPlanId());
            planExclusivity.setPlanName(req.getPlanName());
            planExclusivity.setPlanGroupId(pgId);
            planExclusivity.setSourceType(0);//0  系统
            planExclusivity.setType(1);//1 主产品

            planExclusivityService.save(planExclusivity);
        }

        return ActionResponse.getSuccessResp();
    }

    @ApiOperation(value = "江西修改主互斥产品关系",notes = "江西修改主互斥产品关系")
    @PostMapping("/updatePlanExclusivity")
    @ResponseBody
    public ActionResponse updatePlanExclusivity(@RequestBody McdPlanExclusivity req){

        if (StringUtils.isNotEmpty(req.getExPlanId())){
            //根据产品ID进行查询
            LambdaQueryWrapper<McdPlanExclusivity>  queryWrapper  = new LambdaQueryWrapper<>();
            queryWrapper.eq(McdPlanExclusivity::getPlanId,req.getPlanId())
                    .eq(McdPlanExclusivity::getSourceType,0)
                    .eq(McdPlanExclusivity::getType,1);
            McdPlanExclusivity planExclu = planExclusivityService.getOne(queryWrapper);

            if (Objects.isNull(planExclu)){
                return ActionResponse.getFaildResp("修改失败,主产品未找到!");
            }

            String pgId = planExclu.getPlanGroupId();
            //修改时根据组id删除互斥产品
            LambdaQueryWrapper<McdPlanExclusivity> excluDelWrapper = new LambdaQueryWrapper<>();
            excluDelWrapper.eq(McdPlanExclusivity::getPlanGroupId,pgId)
                    .eq(McdPlanExclusivity::getSourceType,0)
                    .eq(McdPlanExclusivity::getType,0);
            planExclusivityService.remove(excluDelWrapper);


            //保存互斥产品
            String[] exPlanIds = req.getExPlanId().split(",");
            String[] exPlanNames = req.getExPlanName().split(",");
            int t = 0;
            for (String exPlanId : exPlanIds) {
                McdPlanExclusivity planExclusivity = new McdPlanExclusivity();
                planExclusivity.setPlanId(exPlanId);
                planExclusivity.setPlanName(exPlanNames[t]);
                planExclusivity.setPlanGroupId(pgId);
                planExclusivity.setSourceType(0);//0  系统
                planExclusivity.setType(0);//0 互斥产品

                planExclusivityService.save(planExclusivity);

                t++;
            }

        }

        return ActionResponse.getSuccessResp();
    }

    @ApiOperation(value = "江西删除主互斥产品关系",notes = "江西删除主互斥产品关系")
    @PostMapping("/delPlanExclusivity")
    @ResponseBody
    public ActionResponse delPlanExclusivity(@RequestBody PlanExcluQuery req){

        //根据产品ID进行删除
        LambdaQueryWrapper<McdPlanExclusivity>  queryWrapper  = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdPlanExclusivity::getPlanId,req.getPlanId())
                    .eq(McdPlanExclusivity::getSourceType,0)
                    .eq(McdPlanExclusivity::getType,1);

         McdPlanExclusivity planExclusivity = planExclusivityService.getOne(queryWrapper);

        //根据组id删除互斥产品
         LambdaQueryWrapper<McdPlanExclusivity> excluDelWrapper = new LambdaQueryWrapper<>();
         excluDelWrapper.eq(McdPlanExclusivity::getPlanGroupId,planExclusivity.getPlanGroupId())
                        .eq(McdPlanExclusivity::getSourceType,0);
         planExclusivityService.remove(excluDelWrapper);

         //根据组id删除产品组信息
         LambdaQueryWrapper<McdPlanGroup>  groupWrapper = new LambdaQueryWrapper<>();
         groupWrapper.eq(McdPlanGroup::getPlanGroupId,planExclusivity.getPlanGroupId());

          mcdPlanGroupService.remove(groupWrapper);


        return ActionResponse.getSuccessResp();

    }

    /**
     * 互斥产品模板下载
     *
     * @param response
     */
    @PostMapping(path = "/exportPlanExcluTemplate")
    @ApiOperation(value = "互斥产品模板下载", notes = "互斥产品模板下载")
    public void exportPlanExcluTemplate(HttpServletResponse response) {
        planExclusivityService.exportPlanExcluTemplate(response);
    }


    /**
     * 批量导入互斥产品列表，excel文件导入
     *
     * @return
     */
    @PostMapping(path = "/uploadPlanExcluFile")
    @ApiOperation(value = "批量导入互斥产品", notes = "批量导入互斥产品")
    public ActionResponse<Boolean> uploadPlanExcluFile(@RequestParam("uploadPlanExcluFile") MultipartFile uploadPlanExcluFile ) {

        if (uploadPlanExcluFile != null) {
            try {

                String fileName = uploadPlanExcluFile.getOriginalFilename();
                    //校验文件格式
                    if (!fileName.toLowerCase().endsWith(".xls") && !fileName.toLowerCase().endsWith(".xlsx")) {
                        return ActionResponse.getFaildResp("选择文件的类型不正确，请重新选择xlsx、xls类型的文件!");
                    }
                    boolean flag = planExclusivityService.uploadPlanExcluFile(uploadPlanExcluFile);

                if (flag) {
                    return ActionResponse.getSuccessResp(flag);
                }else {
                    return ActionResponse.getFaildResp("互斥产品导入失败!");
                }
            } catch (Exception e) {
                log.error("导入内容异常：{}", e.getMessage());
            }
        }
        return ActionResponse.getFaildResp("导入异常,文件为null");
    }

    @ApiOperation(value = "江西主产品是否可新增互斥产品校验",notes = "主产品是否可新增互斥产品校验")
    @PostMapping("/checkPlanExclusivity")
    @ResponseBody
    public ActionResponse checkPlanExclusivity (@RequestBody PlanIdQuery req){

        boolean flag = false;
        LambdaQueryWrapper<McdPlanExclusivity> planExcluWraper = new LambdaQueryWrapper<>();
        planExcluWraper.eq(McdPlanExclusivity::getPlanId,req.getPlanId())
                       .eq(McdPlanExclusivity::getSourceType,0)
                       .eq(McdPlanExclusivity::getType,1);

         List<McdPlanExclusivity> planExclusivityList = planExclusivityService.list(planExcluWraper);
         if (planExclusivityList != null && planExclusivityList.size() > 0){
             flag = false;
         }else {
             flag = true;
         }

        return ActionResponse.getSuccessResp(flag);

    }

}

