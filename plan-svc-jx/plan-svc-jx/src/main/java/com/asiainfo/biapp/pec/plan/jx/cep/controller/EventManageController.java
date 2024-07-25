package com.asiainfo.biapp.pec.plan.jx.cep.controller;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.cep.model.CepEventVo;
import com.asiainfo.biapp.pec.plan.jx.cep.model.TreeNodeDTO;
import com.asiainfo.biapp.pec.plan.jx.cep.req.SearchEventActionQuery;
import com.asiainfo.biapp.pec.plan.jx.cep.service.IEventManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author mamp
 * @date 2022/4/29
 */
@Api(tags = "江西:事件管理")
@RequestMapping("/api/action/cep/event/eventManager")
@RestController
@Slf4j
public class EventManageController {


    @Autowired
    private IEventManageService eventManageService;

    @ApiOperation(value = "查询事件类型", notes = "查询事件类型")
    @PostMapping("queryCepEventTypeList")
    public List<TreeNodeDTO> queryCepEventTypeList(@RequestParam(value = "keyWords", required = false) String keyWords) {
        return eventManageService.queryCepEventTypeList(keyWords);
    }

    @ApiOperation(value = "查询可用事件", notes = "查询可用事件")
    @PostMapping("queryEvents")
    public IPage<CepEventVo> queryEvents(@RequestBody SearchEventActionQuery query, HttpServletRequest request) {
        if (StrUtil.isEmpty(query.getUserId())) {
            query.setUserId(UserUtil.getUserId(request));
        }
        return eventManageService.queryEvents(query);
    }

    @ApiOperation(value = "根据创建人ID查询事件", notes = "根据创建人ID查询事件")
    @PostMapping("queryEventsByUserId")
    public IPage<CepEventVo> queryEventsByCreatorId(@RequestBody SearchEventActionQuery query, HttpServletRequest request) {
        if (StrUtil.isEmpty(query.getUserId())) {
            query.setUserId(UserUtil.getUserId(request));
        }
        return eventManageService.queryEventsByUserId(query);
    }

}
