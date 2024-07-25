package com.asiainfo.biapp.cmn.jx.controller;

import com.asiainfo.biapp.cmn.jx.query.SysEnumTypeQueryJx;
import com.asiainfo.biapp.cmn.model.SysEnum;
import com.asiainfo.biapp.cmn.service.ISysEnumService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mamp
 * @date 2023/1/3
 */
@RestController
@RequestMapping("/api/jx/sys-enum")
@Api(value = "江西:枚举值管理", tags = "江西:枚举值管理")
public class SysEnumJxController {

    @Autowired
    private ISysEnumService sysEnumService;

    @ApiOperation(value = "通过枚举类型和parentId获取枚举数据列表", notes = "通过枚举类型和parentId获取枚举数据列表")
    @RequestMapping(value = "/listByEnumType", method = {RequestMethod.POST})
    public List<SysEnum> listByEnumType(@RequestBody SysEnumTypeQueryJx request) {
        QueryWrapper<SysEnum> qw = new QueryWrapper<>();
        qw.lambda().eq(SysEnum::getEnumType, request.getEnumType());
        qw.lambda().eq(SysEnum::getParentId, request.getParentId());
        qw.lambda().orderByAsc(SysEnum::getEnumOrder);
        return sysEnumService.list(qw);
    }

}
