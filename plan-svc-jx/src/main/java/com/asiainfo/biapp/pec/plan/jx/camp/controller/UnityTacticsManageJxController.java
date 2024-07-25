package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IUnityTacticsManageJxService;
import com.asiainfo.biapp.pec.plan.vo.IopUnite;
import com.asiainfo.biapp.pec.plan.vo.req.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author [Li.wang]
 * @CreateDate 2022/1/20 0020
 * @see com.asiainfo.biapp.pec.plan.controller.tactics
 */
@Api(tags = "江西:统一策略管理")
@RequestMapping("/api/action/tactics/tacticsManager/jx")
@RestController
@Slf4j
public class UnityTacticsManageJxController {


    @Autowired
    private IUnityTacticsManageJxService unityTacticsManageJxService;


    @ApiOperation(value = "江西:统一策略列表查询", notes = "统一策略列表查询")
    @PostMapping("/searchUnityTactics")
    public ActionResponse searchUnityTactics(@RequestBody SearchUnityTacticsQuery form) {
        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            if (StringUtils.isEmpty(form.getStrategyType())){
                form.setStrategyType("0");
            }
            String strategyStartTime = form.getStrategyStartTime();
            if (StringUtils.isEmpty(strategyStartTime)){
                strategyStartTime="0";
            }
            strategyStartTime = strategyStartTime.replace("-", "") + "000000";
            form.setStrategyStartTime(strategyStartTime);

            String strategyEndTime = form.getStrategyStartTime();
            if (StringUtils.isEmpty(strategyEndTime)){
                strategyEndTime="0";
            }
            strategyEndTime = strategyEndTime.replace("-", "") + "000000";
            form.setStrategyEndTime(strategyEndTime);
            IPage<IopUnite> page = unityTacticsManageJxService.searchIopUnityTactics(form);
            resp.setData(page);
        }catch (Exception e) {
            resp.setStatus(ResponseStatus.ERROR);
            resp.setMessage("查询统一策略列表异常!");
            log.error("查询策略列表异常", e);
        }
        return resp;
    }


}
