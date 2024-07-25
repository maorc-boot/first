package com.asiainfo.biapp.pec.plan.jx.custgroup.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CustomJxActionQuery;
import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupDefJx;
import com.asiainfo.biapp.pec.plan.jx.custgroup.service.IMcdCustgroupDefJxService;
import com.asiainfo.biapp.pec.plan.jx.custgroup.vo.CustgroupDetailJxVO;
import com.asiainfo.biapp.pec.plan.vo.req.CustgroupIdQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;


/**
 * 江西：策略创建：客群查询
 */
@Api(tags = "江西：策略创建：客群查询")
@RequestMapping("/jx/api/action/tactics/tacticsCreate")
@RestController
@Slf4j
public class JxTacticsCreateController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IMcdCustgroupDefJxService custgroupDefJxService;


    /**
     * 创建产品界面：返回客户群列表。（选客户群模块）
     *
     * @param form
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "客户群列表", notes = "客户群列表")
    @PostMapping("getMoreMyCustom")
    public IPage<CustgroupDetailJxVO> getMoreMyCustom(@RequestBody @Valid CustomJxActionQuery form) {
        form.setUserId(UserUtil.getUserId(request));
        return custgroupDefJxService.getMoreMyCustom(form);

    }

    /**
     * 创建策略界面：查看客户群详情
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查看客户群详情", notes = "查看客户群详情")
    @PostMapping("viewCustGroupDetail")
    public CustgroupDetailJxVO viewCustGroupDetail(@RequestBody @Valid CustgroupIdQuery query) {
        return custgroupDefJxService.detailCustgroup(query.getCustgroupId());
    }


    /**
     * 通过客户群ID查询客户群详情
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过客户群ID查询客户群详情", notes = "通过客户群ID查询客户群详情")
    @PostMapping("/queryCustDetailById")
    public Map<String,Object> queryCustDetailById(@RequestParam("custgroupId") String custgroupId) {
        LambdaQueryWrapper<McdCustgroupDefJx> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdCustgroupDefJx::getCustomGroupId,custgroupId);
        return custgroupDefJxService.getMap(queryWrapper);
    }

    @ApiOperation(value = "coc客群更新同步dna", notes = "coc客群更新同步dna(手工同步旧数据接口)")
    @PostMapping("/syncSendUpdateCustInfo2Dna")
    public ActionResponse syncSendUpdateCustInfo2Dna(@RequestBody McdIdQuery query) {
        return custgroupDefJxService.syncSendUpdateCustInfo2Dna(query.getId());
    }
}
