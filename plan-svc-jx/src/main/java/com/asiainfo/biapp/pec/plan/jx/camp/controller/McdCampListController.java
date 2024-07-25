package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.camp.service.JxImcdCampSysncListService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 活动同步使用
 * </p>
 *
 * @author imcd
 * @since 2021-11-16
 */
@Slf4j
@RestController
@RequestMapping("/mcd-camp-sync")
@Api(tags = "江西:活动同步(ChannelList)")
public class McdCampListController {
    @Autowired
    private JxImcdCampSysncListService jxImcdCampSysncListService;


    @PostMapping("syncReadyCamp")
    public ActionResponse syncReadyCamp() {
        try {
            log.info("查询所有实时类活动列表");
            return ActionResponse.getSuccessResp(JSONUtil.parseArray(jxImcdCampSysncListService.syncReadyCampList()));
        } catch (Exception e) {
            log.error("查询所有实时类活动列表异常", e);
            return ActionResponse.getFaildResp();
        }
    }

    @PostMapping("selectCampInfoById")
    public ActionResponse selectCampInfoById(@RequestParam("campId") String campId) {
        try {
            log.info("根据活动ID查询活动详情，campId:{}", campId);
            return ActionResponse.getSuccessResp(jxImcdCampSysncListService.selectCampInfoById(campId));
        } catch (Exception e) {
            log.error("根据活动ID查询活动详情异常，campId:{}", campId, e);
            return ActionResponse.getFaildResp();
        }
    }

    /**
     * 查询不限定客户群活动
     * @return
     */
    @PostMapping("noCustEventCampMap")
    public ActionResponse noCustEventCampMap() {
        try {
            log.info("查询不限定客户群活动开始");
            return ActionResponse.getSuccessResp(jxImcdCampSysncListService.selectNoCustCamp());
        } catch (Exception e) {
            log.error("查询不限定客户群活动开始异常", e);
            return ActionResponse.getFaildResp();
        }
    }

}
