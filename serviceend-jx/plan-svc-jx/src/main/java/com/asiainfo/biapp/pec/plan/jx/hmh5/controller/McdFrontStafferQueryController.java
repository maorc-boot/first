package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontReportCusManagerModel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontStafferQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontStafferService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/mcd/front/staffer")
@Api(value = "客户通-工号查询",tags = "客户通-工号查询")
@DataSource("khtmanageusedb")
public class McdFrontStafferQueryController {

    @Autowired
    private McdFrontStafferService mcdFrontStafferService;


    /**
     * 客户通工号查询接口
     * @return
     */
    @PostMapping("/queryFrontStafferList")
    @ApiOperation("工号查询接口")
    public ActionResponse<IPage<McdFrontReportCusManagerModel>> queryFrontStafferList(@RequestBody McdFrontStafferQuery req) {

        String keyWorks = req.getKeyWords();
        LambdaQueryWrapper<McdFrontReportCusManagerModel>  modelWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(keyWorks)){
            modelWrapper.like(McdFrontReportCusManagerModel::getStaffId,keyWorks)
            .or().like(McdFrontReportCusManagerModel::getStaffName,keyWorks)
            .or().like(McdFrontReportCusManagerModel::getChannelId,keyWorks)
            .or().like(McdFrontReportCusManagerModel::getCallingNum,keyWorks)
            .or().like(McdFrontReportCusManagerModel::getCountyId,keyWorks)
            .or().like(McdFrontReportCusManagerModel::getCityId,keyWorks) ;
        }

        Page<McdFrontReportCusManagerModel> page = new Page<>();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());

        //查询数据
        IPage<McdFrontReportCusManagerModel> mcdFrontReportCusManagerModelIPage = mcdFrontStafferService.page(page,modelWrapper);

        return ActionResponse.getSuccessResp(mcdFrontReportCusManagerModelIPage);
    }


}
