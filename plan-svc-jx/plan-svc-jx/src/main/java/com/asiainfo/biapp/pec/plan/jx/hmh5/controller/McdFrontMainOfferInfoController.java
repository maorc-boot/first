package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontMainOfferInfoModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontMainOfferInfoSaveModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontOfferOperationQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontMainOfferQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontMainOfferInfoService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @author ranpf
 * @date 2023-2-17
 * @description
 */
@RequestMapping("/mcd/front/mainOfferInfo")
@RestController
@Api(tags = "江西客户通主套餐配置")
@DataSource("khtmanageusedb")
public class McdFrontMainOfferInfoController   {



    @Autowired
    private McdFrontMainOfferInfoService mcdFrontMainOfferInfoService;


    /**
     *
     * @param req
     * @return 主套餐配置分页搜索功能
     */
    @PostMapping("/queryFrontMainOffers")
    @ApiOperation("查询主套餐数据")
    public ActionResponse<IPage<McdFrontMainOfferInfoModel>> queryFrontMainOffers(@RequestBody McdFrontMainOfferQuery req) {
        IPage<McdFrontMainOfferInfoModel> offerInfoModelPage = mcdFrontMainOfferInfoService.selectMainOffers(req);
        return ActionResponse.getSuccessResp(offerInfoModelPage);
    }


    /**
     *
     * @param request
     * @param model
     * @return 编辑主套餐配置
     */
    @ApiOperation("编辑或者保存套餐配置")
    @PostMapping("/saveOrUpdateMainOffer")
    public ActionResponse modifyMainOfferInfo(@RequestBody McdFrontMainOfferInfoSaveModel model, HttpServletRequest request ){

        UserSimpleInfo user = UserUtil.getUser(request);

        if (Objects.nonNull(user)){
            model.setCreateUserId(user.getUserId());
        }

        model.setUpdateTime(new Date());
        mcdFrontMainOfferInfoService.saveOrUpdate(model);


        return ActionResponse.getSuccessResp();
    }



    /**
     * 删除主套餐配置 标识选择套餐为删除
     * @param req
     * @return
     */
    @ApiOperation("删除主套餐配置")
    @PostMapping("/deleteMainOfferInfo")
    public ActionResponse deleteMainOfferInfo(@RequestBody McdFrontOfferOperationQuery req){

        LambdaUpdateWrapper<McdFrontMainOfferInfoSaveModel>  offerInfoWrapper = new LambdaUpdateWrapper<>();
        offerInfoWrapper.eq(McdFrontMainOfferInfoSaveModel::getOfferId,req.getOfferId());
        boolean flag  = mcdFrontMainOfferInfoService.remove(offerInfoWrapper);
        return ActionResponse.getSuccessResp(flag);
    }


    /**
     *  上、下线操作
     * @param request
     * @param req
     * @return
     */
    @PostMapping("/updateMainOfferInfoStatus")
    @ApiOperation("上线,下线操作")
    public ActionResponse updateMainOfferInfoStatus(@RequestBody McdFrontOfferOperationQuery req, HttpServletRequest request){

        LambdaUpdateWrapper<McdFrontMainOfferInfoSaveModel>  offerInfoWrapper = Wrappers.lambdaUpdate();

        offerInfoWrapper.set(McdFrontMainOfferInfoSaveModel::getState,req.getState()).set(McdFrontMainOfferInfoSaveModel::getUpdateTime,new Date())
                  .eq(McdFrontMainOfferInfoSaveModel::getOfferId,req.getOfferId());

        boolean flag = mcdFrontMainOfferInfoService.update(offerInfoWrapper);

        return ActionResponse.getSuccessResp(flag);
    }


    @ApiOperation("查看详情")
    @PostMapping("/getMainOfferInfoDetail")
    public ActionResponse getMainOfferInfo(@RequestBody McdFrontOfferOperationQuery req){
        if (StringUtils.isEmpty(req.getOfferId())){
            return ActionResponse.getFaildResp("主套餐编码为空");
        }
        return ActionResponse.getSuccessResp(mcdFrontMainOfferInfoService.selectMainOffer(req.getOfferId()));
    }
}
