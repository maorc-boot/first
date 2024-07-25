package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontBlacklistCust;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontBlackListPhone;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontBlackListQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontBlackListService;
import com.asiainfo.biapp.pec.plan.model.McdSysCity;
import com.asiainfo.biapp.pec.plan.service.IMcdSysCityService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(value = "客户通-客户通黑名单",tags = "客户通-客户通黑名单")
@RestController
@RequestMapping("/customer/black/list")
public class CustCommunicationBlackListController {

    @Resource
    private McdFrontBlackListService mcdFrontBlackListService;

    @Autowired
    private IMcdSysCityService sysCityService;

    @ApiOperation(value = "江西客户通黑名单查询接口")
    @PostMapping(value = "/queryBlackList")
    @DataSource("khtmanageusedb")
    public ActionResponse<Page<McdFrontBlacklistCust>> queryBlackList(@RequestBody McdFrontBlackListQuery req){
        Date start = null;
        Date end = null;
        if (StringUtils.isNotEmpty(req.getStartDate())) {
            start = DateUtil.parse(req.getStartDate(), "yyyy-MM-dd");
        }
        if (StringUtils.isNotEmpty(req.getEndDate())) {
            end = DateUtil.parse(req.getEndDate(), "yyyy-MM-dd");
        }

        LambdaQueryWrapper<McdFrontBlacklistCust>  custWrapper = new LambdaQueryWrapper<>();
        custWrapper.eq(StringUtils.isNotEmpty(req.getProductNo()),McdFrontBlacklistCust::getProcuctNo,req.getProductNo())
                   .eq(StringUtils.isNotEmpty(req.getCityId()) && !"-1".equals(req.getCityId()), McdFrontBlacklistCust::getCityId, req.getCityId())
                   .ge(ObjectUtil.isNotEmpty(start),McdFrontBlacklistCust::getCreateTime, start)
                   .le(ObjectUtil.isNotEmpty(end),McdFrontBlacklistCust::getCreateTime, end)
                   .orderByDesc(McdFrontBlacklistCust::getCreateTime);
        Page<McdFrontBlacklistCust> page = new Page<>();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());

        Page<McdFrontBlacklistCust>  mcdFrontBlacklistCustPage = mcdFrontBlackListService.page(page,custWrapper);

        return ActionResponse.getSuccessResp(mcdFrontBlacklistCustPage);
    }


    @ApiOperation(value = "")
    @PostMapping(value = "/deleBlackList")
    @DataSource("khtmanageusedb")
    public ActionResponse  deleteBlackList(@RequestBody McdFrontBlackListPhone req){

        if (StringUtils.isEmpty(req.getProductNo())){
            log.info("江西客户通根据手机号码删除黑名单入参为空!");
            return ActionResponse.getFaildResp("手机号码为空!");
        }
        mcdFrontBlackListService.removeById(req.getProductNo());

        return ActionResponse.getSuccessResp();

    }


    /**
     * 导入/删除模板下载
     *
     * @param response
     */
    @PostMapping(path = "/downloadImpOrDelBlacklistTemplate")
    @ApiOperation(value = "江西:客户通黑名单模板下载", notes = "客户通黑名单模板下载")
    public void downloadImpOrDelBlacklistTemplate(HttpServletResponse response) {
        mcdFrontBlackListService.downloadImportOrDelBlackListTemplate(response);
    }

    /**
     * 批量导入或删除黑名单接口，excel文件导入
     *
     * @return
     */
    @PostMapping(path = "/impOrDelBlacklistFile")
    @ApiOperation(value = "江西:批量导入或删除黑名单接口", notes = "批量导入或删除黑名单接口")
    @DataSource("khtmanageusedb")
    public ActionResponse<Boolean> impOrDelBlacklistFile(@RequestParam(value = "impOrDelBlacklistFile", required = true) MultipartFile impOrDelBlacklistFile, HttpServletRequest request) {
        if (impOrDelBlacklistFile != null) {
            UserSimpleInfo user = UserUtil.getUser(request);
            try {
                final boolean flag = mcdFrontBlackListService.impOrDelBlacklistFile(impOrDelBlacklistFile,user.getUserId());
                return ActionResponse.getSuccessResp(flag);
            } catch (Exception e) {
                log.error("导入内容异常：{}", e.getMessage());
            }
        }
        return ActionResponse.getFaildResp("导入异常,文件为null");
    }


    /**
     * 创建活动页面：初始化产品类型模块
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "所有地市查询", notes = "所有地市查询")
    @PostMapping(value = "/queryAllCitys")
    public ActionResponse queryAllCitys() {

        List<McdSysCity> citys = sysCityService.queryAllCitysDescCityCode();

        return ActionResponse.getSuccessResp(citys);
    }

}

