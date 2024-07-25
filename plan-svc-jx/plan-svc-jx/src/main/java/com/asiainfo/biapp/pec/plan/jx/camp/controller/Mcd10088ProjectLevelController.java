package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.camp.model.Mcd10088ProjectLevel;
import com.asiainfo.biapp.pec.plan.jx.camp.req.ProjectLevelReq;
import com.asiainfo.biapp.pec.plan.jx.camp.service.Mcd10088ProjectLevelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 10088外呼项目层级表 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2023-01-03
 */
@RestController
@RequestMapping("/project-level")
@Slf4j
@Api(value = "江西:10088外呼项目层级", tags = "江西:10088外呼项目层级")
public class Mcd10088ProjectLevelController {

    @Resource
    private Mcd10088ProjectLevelService levelService;

    @PostMapping("/listByTypeAndParent")
    @ApiOperation(value = "通过层级类型和父ID查询 ", notes = "通过层级类型和父ID查询")
    private ActionResponse queryProjectLevel(@RequestBody ProjectLevelReq req) {
        log.info("queryProjectLevel param: {} ", req);
        QueryWrapper<Mcd10088ProjectLevel> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Mcd10088ProjectLevel::getLevelType, req.getLevelType())
                .eq(Mcd10088ProjectLevel::getParentId, req.getParentId());
        return ActionResponse.getSuccessResp(levelService.list(wrapper));
    }

    @PostMapping("/queryById")
    @ApiOperation(value = "通ID查询  ", notes = "通ID查询")
    private ActionResponse queryById(@RequestParam("id") String id) {
        log.info("queryById param: {} ", id);
        QueryWrapper<Mcd10088ProjectLevel> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Mcd10088ProjectLevel::getId, id);
        return ActionResponse.getSuccessResp(levelService.getOne(wrapper));
    }
}

