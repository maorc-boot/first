package com.asiainfo.biapp.pec.plan.jx.cep.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.asiainfo.biapp.pec.plan.jx.cep.model.CepRuleShare;
import com.asiainfo.biapp.pec.plan.jx.cep.service.CepRuleShareService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 事件共享表 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2023-02-20
 */
@RestController
@RequestMapping("/cepRuleShare")
@Slf4j
@Api(tags = "江西:事件共享")
public class CepRuleShareController {

    @Resource
    private CepRuleShareService shareService;

    @ApiOperation(value = "根据事件ID查询共享信息", notes = "根据事件ID查询共享信息")
    @PostMapping("/queryShareById")
    private List<CepRuleShare> queryShareInfoByEventId(@RequestBody CepRuleShare query) {
        log.info("queryShareInfoByEventId param:{}", query);
        QueryWrapper<CepRuleShare> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CepRuleShare::getEventId, query.getEventId());
        return shareService.list(queryWrapper);
    }

    @PostMapping("/saveOrUpdate")
    private boolean saveOrUpdate(@RequestBody List<CepRuleShare> shareInfos) {
        log.info("saveOrUpdate param:{}", shareInfos);
        if (CollectionUtil.isEmpty(shareInfos)) {
            return false;
        }
        return shareService.saveOrUpdate(shareInfos);
    }
}

