package com.asiainfo.biapp.cmn.jx.controller;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.cmn.jx.service.IMenuJxService;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 江西菜单表 前端控制器
 * </p>
 *
 * @author ranpf
 * @since 2023-1-13
 */
@Api(tags = "江西:权限设置")
@Slf4j
@RestController
@RequestMapping("/api/menu/jx")
public class MenuJxController {

    @Resource
    private IMenuJxService menuJxService;



    @ApiOperation(value = "江西根据角色Id查询菜单和权限的", notes = "江西根据角色ID查询所有关联的权限ID列表角色下的权限回写")
    @PostMapping("/listMenuPermissionByRoleId")
    public ActionResponse<Map<String,List<String>>> listMenuPermissionByRoleId(@RequestBody McdIdQuery dto) {
        if(StrUtil.isBlank(dto.getId())){
            return ActionResponse.getFaildResp("没有接收到角色ID");
        }
        Long roleId = Long.parseLong(dto.getId());
        Map<String,List<String>> result = menuJxService.listIDByRoleId(roleId);
        return ActionResponse.getSuccessResp(result);
    }
}
