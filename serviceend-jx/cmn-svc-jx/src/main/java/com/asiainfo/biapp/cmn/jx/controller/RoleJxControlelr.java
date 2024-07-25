package com.asiainfo.biapp.cmn.jx.controller;

import com.asiainfo.biapp.cmn.jx.query.RolePageQueryJx;
import com.asiainfo.biapp.cmn.jx.service.RoleJxService;
import com.asiainfo.biapp.cmn.vo.RoleVO;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mamp
 * @date 2022/12/13
 */
@Slf4j
@RestController
@RequestMapping("/api/jx/role")
@Api(tags = "江西:角色管理")
public class RoleJxControlelr {

    @Autowired
    private RoleJxService roleJxService;

    @ApiOperation(value = "分页查询所有角色", notes = "")
    @PostMapping("/pagelist")
    public ActionResponse<IPage<RoleVO>> pageList(@RequestBody RolePageQueryJx dto) {
        IPage<RoleVO> iPage = roleJxService.pageList(dto);
        return ActionResponse.getSuccessResp(iPage);
    }
}
