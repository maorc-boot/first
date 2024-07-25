package com.asiainfo.biapp.pec.plan.jx.monitor.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.plan.jx.monitor.service.IMonitorService;
import com.asiainfo.biapp.pec.plan.jx.monitor.vo.MonitorVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description: 主机监控控制层
 *
 * @author: lvchaochao
 * @date: 2023/1/10
 */
@Api(tags = {"主机监控"},value = "主机监控")
@RequestMapping("/api/monitor")
@RestController
@Slf4j
public class MonitorController {

    @Autowired
    private IMonitorService monitorService;

    @ApiOperation(value = "查询主机监控列表信息", notes = "查询主机监控列表信息")
    @PostMapping("/queryMonitor")
    public IPage<MonitorVO> queryMonitor (@RequestBody McdPageQuery form) {
        log.info("查询主机监控列表信息入参：{}", JSONUtil.toJsonStr(form));
        Page<MonitorVO> pager = new Page<>(form.getCurrent(), form.getSize());
        final LambdaQueryWrapper<MonitorVO> qry = Wrappers.lambdaQuery();
        qry.like(StrUtil.isNotEmpty(form.getKeyWords()), MonitorVO::getIp, form.getKeyWords());
        return monitorService.page(pager, qry);
    }
}
