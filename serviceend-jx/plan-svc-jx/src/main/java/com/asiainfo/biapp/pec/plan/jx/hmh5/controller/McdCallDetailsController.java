package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdOutCallQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdIdentifierQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.IMcdCallDetailsService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdOutDetailsVo;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 客户通外呼详情 前端控制器
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@RestController
@RequestMapping("/api/jx/callDetails")
@Api(tags = "客户通-外呼通话")
@Slf4j
@DataSource("khtmanageusedb")
public class McdCallDetailsController {

    @Autowired
    private IMcdCallDetailsService callDetailsService;

    @PostMapping("/queryCallDetailsList")
    @ApiOperation("外呼明细查询")
    public ActionResponse<IPage<McdOutDetailsVo>> queryCallDetailsList(@RequestBody McdOutCallQuery query, HttpServletRequest request) {
        log.info("查询外呼明细信息【queryCallDetailsList】===》，入参{}", query);
        UserSimpleInfo user = UserUtil.getUser(request);
        return ActionResponse.getSuccessResp(callDetailsService.queryCallDetailsList(query,user));
    }


    /**
     * 通话明细查询 --完成  请求
     * @return map
     */
    @PostMapping("/getCallHttp")
    @ApiOperation("通话明细查询")
    public ActionResponse<McdOutDetailsVo> getCallHttp(@RequestBody McdIdentifierQuery query){
        log.info("通话明细查询【getCallHttp】===》");
        if (StringUtils.isEmpty(query.getCallQueryId())) {
            log.info("通话明细查询,通话唯一标识不能为空");
            return ActionResponse.getSuccessResp("通话唯一标识不能为空");
        }
        return ActionResponse.getSuccessResp(callDetailsService.callHttpData(query));
    }

    /**
     * 文件导出功能
     *
     * @param query
     * @param response
     */
    @PostMapping(value = "getCallExportDataList")
    @ApiOperation("外呼通话导出")
    public ActionResponse getCallExportDataList(@RequestBody McdOutCallQuery query, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("外呼通话明细导出【getCallExportDataList】===》");
        UserSimpleInfo user = UserUtil.getUser(request);
        return ActionResponse.getSuccessResp(callDetailsService.exportExcel(query,user,response));
    }
}
