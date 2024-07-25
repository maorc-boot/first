package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SingleUserReq;
import com.asiainfo.biapp.pec.plan.jx.camp.service.SingleUserService;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.SingleUserCampsegInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author mamp
 * @date 2022/12/14
 */
@RestController
@Slf4j
@RequestMapping("/api/singleUser")
@Api(tags = "江西:单用户查询服务")
public class SingleUserController {

    @Resource
    private SingleUserService singleUserService;

    @ApiOperation(value = "单用户查询活动", notes = "单用户查询活动")
    @PostMapping("/pagelist")
    public ActionResponse<IPage<SingleUserCampsegInfo>> pageList(@RequestBody SingleUserReq req) {
        log.info("单用户查询入参: {}", req);
        if (StrUtil.isEmpty(req.getPhoneNo())) {
            return ActionResponse.getSuccessResp("用户号码不能为空");
        }

        IPage<SingleUserCampsegInfo> iPage= null;
        if (req.getMarketResult() == 100){ //待推荐
            if(req.getCampsegStatus() == 90){ //已完成
                return ActionResponse.getSuccessResp(new ArrayList<>());
            }
            iPage = singleUserService.queryCampsegInfo(req);
        }else if (req.getCampsegStatus() != 90 && req.getMarketResult() == -1 ){//执行中
            iPage = singleUserService.queryCampsegInfoByCondition(req);

        }else {//其他
            iPage = singleUserService.queryCampsegInfoByOtherCondition(req);
        }
        return ActionResponse.getSuccessResp(iPage);
    }

}
