package com.asiainfo.biapp.pec.plan.jx.dna.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.dnacustomgroup.CustgroupDetailVO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.dnacustomgroup.CustomerDetailReturnDataVO;
import com.asiainfo.biapp.pec.plan.jx.dna.service.IDNACustomGroupService;
import com.asiainfo.biapp.pec.plan.jx.dna.service.IDnaCustgroupDayMonthUpdateService;
import com.asiainfo.biapp.pec.plan.vo.req.CustgroupIdQuery;
import com.asiainfo.biapp.pec.plan.vo.req.CustomActionQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.ExecutorService;

/**
 * description: dna客群信息获取控制层
 *
 * @author: lvchaochao
 * @date: 2023/12/12
 */
@RestController
// @RequestMapping("/dna/customgroup")
@Api(tags = "江西dna客群信息获取")
@Slf4j
public class DNACustomGroupController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IDNACustomGroupService idnaCustomGroupService;

    @Autowired
    private IDnaCustgroupDayMonthUpdateService dnaCustgroupDayMonthUpdateService;


    /**
     * dna客群更新日周期/月周期线程池
     */
    private ExecutorService dnaCustgroupDayMonthUpdateExecutor = ThreadUtil.newExecutor(2, 5, 5);

    /**
     * dna客群更新通知线程池
     */
    private ExecutorService dnaCustgroupUpdateNoticeExecutor = ThreadUtil.newExecutor(1);

    @ApiOperation(value = "江西获取dna客群列表", notes = "江西获取dna客群列表")
    @PostMapping("/dna/customgroup/getMoreMyCustom")
    public IPage<CustgroupDetailVO> getMoreMyCustom(@RequestBody @Valid CustomActionQuery form) {
        log.info("江西获取dna客群列表入参={}：", JSONUtil.toJsonStr(form));
        form.setUserId(StrUtil.isEmpty(UserUtil.getUserId(request)) ? "admin01": UserUtil.getUserId(request));
        return idnaCustomGroupService.getMoreMyCustom(form);
    }

    @ApiOperation(value = "江西查看dna客户群详情", notes = "江西查看dna客户群详情")
    @PostMapping("/dna/customgroup/viewCustGroupDetail")
    public CustomerDetailReturnDataVO viewCustGroupDetail(@RequestBody @Valid CustgroupIdQuery query) {
        log.info("江西查看dna客户群详情入参={}：", JSONUtil.toJsonStr(query));
        CustomerDetailReturnDataVO dataVO = new CustomerDetailReturnDataVO();
        CustgroupDetailVO custgroupDetailVO = idnaCustomGroupService.detailCustgroup(query.getCustgroupId());
        BeanUtils.copyProperties(custgroupDetailVO, dataVO);
        dataVO.setCustomStatusId(custgroupDetailVO.getCustomStatusId());
        dataVO.setUpdateCycle(custgroupDetailVO.getUpdateCycle());
        return dataVO;
    }

    /**
     * 获取标签替换后的变量信息
     *
     * @param jsonObject json对象
     * @return ActionResponse
     */
    @ApiOperation(value = "获取标签变量信息", notes = "获取标签替换后的变量信息(可模糊查询)")
    @PostMapping("/dna/customgroup/getVariableInfo")
    public JSONObject getVariableInfo(@RequestBody JSONObject jsonObject) {
        log.info("获取标签变量信息入参={}", JSONUtil.toJsonStr(jsonObject));
        return idnaCustomGroupService.getVariableInfo(jsonObject.getStr("keyWord"));
    }

    @ApiOperation(value = "DNA客群更新通知接口", notes = "DNA客群更新通知接口")
    @PostMapping("/api/dna/customgroup/dnaCustgroupUpdateNotice")
    public ActionResponse dnaCustgroupUpdateNotice(@RequestBody JSONObject request) {
        log.info("DNA客群更新通知开始, 入参={}", JSONUtil.toJsonStr(request));
        ActionResponse response = ActionResponse.getFaildResp();
        try {
            dnaCustgroupUpdateNoticeExecutor.execute(() -> {
                try {
                    dnaCustgroupDayMonthUpdateService.dnaCustgroupUpdateNotice(request);
                } catch (Exception e) {
                    log.error("DNA客群更新通知逻辑异常", e);
                }
            });
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("DNA客群更新通知失败", e);
        }
        return response;
    }


    @ApiOperation(value = "DNA客群更新调度(日周期/月周期)", notes = "DNA客群更新调度(日周期/月周期)")
    @PostMapping("/dna/customgroup/dayMonthupdate")
    public ActionResponse dnaCustgroupDayMonthUpdate(@RequestBody JSONObject request) {
        log.info("DNA客群更新调度(日周期/月周期)任务...");
        ActionResponse response = ActionResponse.getFaildResp();
        try {
            dnaCustgroupDayMonthUpdateExecutor.execute(() -> {
                dnaCustgroupDayMonthUpdateService.dnaCustgroupDayMonthUpdateTask(request);
            });
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("DNA客群更新调度(日周期/月周期)任务失败", e);
        }
        return response;
    }
}
