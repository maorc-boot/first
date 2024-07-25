package com.asiainfo.biapp.cmn.jx.controller;

import com.asiainfo.biapp.cmn.jx.query.DepartmentJxQuery;
import com.asiainfo.biapp.cmn.jx.service.IDepartmentJxService;
import com.asiainfo.biapp.cmn.model.Department;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author ranpf
 * @since 2023-1-16
 */
@Api(tags = "江西:部门查询服务")
@RestController
@RequestMapping("/api/department/jx")
public class DepartmentJxController {


    @Resource
    private IDepartmentJxService departmentJxService;

    @ApiOperation(value = "江西:根据城市ID查看部门信息", notes = "江西以部门表中的城市ID查询出部门列表")
    @PostMapping("/listByCityID")
    public ActionResponse<List<Department>> selectByCityID(@RequestBody DepartmentJxQuery req) {

        String cityId = "-1".equals(req.getCityId()) ? "":req.getCityId();

        List<Department> departments = departmentJxService.selectByCityID(cityId);

        return ActionResponse.getSuccessResp(departments);
    }

}
