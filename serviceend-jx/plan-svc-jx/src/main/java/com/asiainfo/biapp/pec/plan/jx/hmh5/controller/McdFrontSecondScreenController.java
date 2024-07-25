package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdSjxCustomerModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontSecondScreenService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mcd/front/secondScreen")
@Api(value = "客户通-大屏配置",tags = "客户通-大屏配置")
@DataSource("khtmanageusedb") // 加此注解表明以下方法连接的是4.0oracle数据源，若需要使用mysql数据源则去掉此注解即可
public class McdFrontSecondScreenController {

    @Autowired
    private McdFrontSecondScreenService mcdFrontSecondScreenService;


    /**
     * 客户通大屏数据查询接口
     * @return
     */
    @PostMapping("/queryFrontSecondScreens")
    @ApiOperation("江西客户通大屏查询接口")
    public IPage<McdSjxCustomerModel> selectSecondScreenAll(@RequestBody McdPageQuery query) {
        Page<McdSjxCustomerModel> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<McdSjxCustomerModel>  modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.inSql(McdSjxCustomerModel::getMonthTime,"  SELECT MAX(MONTH_TIME) FROM SJX_CUSTOMER_MODEL  ")
                    .orderByDesc(McdSjxCustomerModel::getModelId);

        //查询数据
        return mcdFrontSecondScreenService.page(page, modelWrapper);
    }

    /**
     * 第二屏修改转化率
     * 客户通大屏数据修改接口
     *
     * @param listModel
     * @return
     */
    @PostMapping("/updateFrontSecondScreens")
    @ApiOperation("江西客户通大屏修改接口")
    public ActionResponse updateSecondScreenAll(@RequestBody List<McdSjxCustomerModel> listModel ) {

        if (listModel.isEmpty()){
            log.info("客户通大屏没有可修改数据!");
            return ActionResponse.getSuccessResp();
        }
        boolean flag = true;
        for (McdSjxCustomerModel mcdSjxCustomerModel : listModel) {
            LambdaUpdateWrapper<McdSjxCustomerModel> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(McdSjxCustomerModel::getModelId,mcdSjxCustomerModel.getModelId())
                         .eq(McdSjxCustomerModel::getMonthTime,mcdSjxCustomerModel.getMonthTime());
            try {
                flag = mcdFrontSecondScreenService.saveOrUpdate(mcdSjxCustomerModel,updateWrapper);
            }catch (Exception e){
                log.info("客户通修改大屏异常modelid: "+ mcdSjxCustomerModel.getModelId());
            }
        }
        log.info("客户通大屏修改"+ listModel.size()+"条数据" );


        return ActionResponse.getSuccessResp(flag);

    }
}
