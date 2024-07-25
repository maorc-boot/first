package com.asiainfo.biapp.pec.approve.jx.controller;


import com.asiainfo.biapp.pec.approve.jx.model.Grid;
import com.asiainfo.biapp.pec.approve.jx.service.GridService;
import com.asiainfo.biapp.pec.approve.jx.vo.AreaQueryVo;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 网格维表 前端控制器
 * </p>
 *
 * @author feify
 * @since 2022-06-02
 */
@RestController
@RequestMapping("/api/grid")
@Api(value = "江西:网格管理服务", tags = {"江西:网格管理服务"})
public class GridController {

    @Autowired
    private GridService gridService;

    @ApiOperation(value = "查询所有网格信息", notes = "目前不需要参数，查询所有网格列表，多用作下拉")
    @PostMapping("/list")
    public ActionResponse<List<Grid>> list(){
        List<Grid> list = gridService.lambdaQuery().orderByAsc(Grid::getSortnum).list();
        return ActionResponse.getSuccessResp(list);
    }

    @ApiOperation(value = "查询网格信息", notes = "查询某地市区县下网格数据")
    @PostMapping(path = "/listByCountyID")
    public ActionResponse<List<Grid>> listByCityID(@RequestBody AreaQueryVo areaQueryVo){
        List<Grid> list = gridService.lambdaQuery()
                .eq(Grid::getCityId, areaQueryVo.getCityId())
                .eq(Grid::getCountyId, areaQueryVo.getCountyId())
                .orderByAsc(Grid::getGridId).list();
        return ActionResponse.getSuccessResp(list);
    }

}
