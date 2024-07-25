package com.asiainfo.biapp.pec.plan.jx.kpi.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.plan.jx.kpi.kpiConstant.KpiConstant;
import com.asiainfo.biapp.pec.plan.jx.kpi.service.IDimEvalIndexInfoService;
import com.asiainfo.biapp.pec.plan.jx.kpi.vo.DimEvalIndexInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 指标管理控制层
 *
 * @author: lvchaochao
 * @date: 2023/1/10
 */
@Api(tags = {"指标管理"},value = "指标管理")
@RequestMapping("/api/kpi")
@RestController
@Slf4j
public class KpiController {

    @Autowired
    private IDimEvalIndexInfoService dimEvalIndexInfoService;

    @ApiOperation(value = "查询指标口径管理列表信息", notes = "查询指标口径管理列表信息")
    @PostMapping("/getCaliberInfo")
    public IPage<DimEvalIndexInfo> getCaliberInfo (@RequestBody McdPageQuery form) {
        log.info("查询指标口径管理列表信息入参：{}", JSONUtil.toJsonStr(form));
        Page<DimEvalIndexInfo> pager = new Page<>(form.getCurrent(), form.getSize());
        final LambdaQueryWrapper<DimEvalIndexInfo> qry = Wrappers.lambdaQuery();
        qry.like(StrUtil.isNotEmpty(form.getKeyWords()), DimEvalIndexInfo::getIndexName, form.getKeyWords());
        Page<DimEvalIndexInfo> page = dimEvalIndexInfoService.page(pager, qry);
        page.getRecords().forEach(vo -> {
            if (KpiConstant.KPI_INDEX_TYPE_ZERO.equals(vo.getIndexType())) {
                vo.setIndexType("客户接触情况");
            } else if (KpiConstant.KPI_INDEX_TYPE_ONE.equals(vo.getIndexType())) {
                vo.setIndexType("营销成功情况");
            } else if (KpiConstant.KPI_INDEX_TYPE_TWO.equals(vo.getIndexType())) {
                vo.setIndexType("营销效益情况");
            } else if (KpiConstant.KPI_INDEX_TYPE_THREE.equals(vo.getIndexType())) {
                vo.setIndexType("客户质量情况");
            } else {
                vo.setIndexType("资源使用情况");
            }
        });
        return page;
    }

    @ApiOperation(value = "修改指标口径管理列表信息", notes = "修改指标口径管理列表信息")
    @PostMapping("/updateCaliberInfo")
    public ActionResponse updateCaliberInfo (@RequestBody DimEvalIndexInfo request) {
        log.info("修改指标口径管理列表信息入参：{}", JSONUtil.toJsonStr(request));
        final LambdaUpdateWrapper<DimEvalIndexInfo> qry = Wrappers.lambdaUpdate();
        qry.set(DimEvalIndexInfo::getIndexDesc, request.getIndexDesc()).eq(DimEvalIndexInfo::getIndexId, request.getIndexId());
        return ActionResponse.getSuccessResp(dimEvalIndexInfoService.update(qry));
    }
}
