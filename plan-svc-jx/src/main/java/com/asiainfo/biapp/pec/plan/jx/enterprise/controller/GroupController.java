package com.asiainfo.biapp.pec.plan.jx.enterprise.controller;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.biapp.client.pec.approve.model.User;
import com.asiainfo.biapp.pec.core.common.OutInterface;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.EnterpriseGroupService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.GrouPortraitVo;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.GroupMemberVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2022/7/30
 */
@Api(tags = "江西-政企:集团画像、成员画像及阈值指标")
@RestController
@RequestMapping("/api/groupInterface")
public class GroupController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private EnterpriseGroupService enterpriseGroupService;

    /**
     * 3.1 集团画像接口
     *
     * @param grouPortraitVo 入参封装Vo
     * @return
     */
    @ApiOperation(value = "集团画像接口", notes = "集团画像接口")
    @RequestMapping("/grouPortraitQuery")
    @OutInterface
    public Object grouPortraitQuery(@RequestBody GrouPortraitVo grouPortraitVo, HttpServletRequest request) {
        LOGGER.info("grouPortraitQuery grouPortraitVo : {} ", grouPortraitVo);
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(grouPortraitVo.getGroupId())) {
            grouPortraitVo.setGroupId(request.getParameter("groupId"));
        }
        User user = (User) request.getSession().getAttribute("USER");
        if (user==null){
            user = new User();
            user.setId("admin");
            user.setPwd("test1234");
        }
        JSONObject groupPortraitInfo = enterpriseGroupService.getGroupPortraitInfo(grouPortraitVo, user);
        return groupPortraitInfo;
    }

    /**
     * 3.2 成员画像查询接口
     *
     * @param groupMemberVo 入参封装Vo
     * @return
     */
    @ApiOperation(value = "成员画像查询接口", notes = "成员画像查询接口")
    @RequestMapping("/groupMemberDetail")
    @OutInterface
    public Object groupMemberDetail(@RequestBody GroupMemberVo groupMemberVo, HttpServletRequest request) {
        LOGGER.info("groupMemberDetail groupMemberVo : {} ", groupMemberVo);
        Map<String, Object> result = new HashMap<>();
        User user = (User) request.getSession().getAttribute("USER");
        if (user==null){
            user = new User();
            user.setId("admin");
            user.setPwd("test1234");
        }
        JSONObject jsonObject = enterpriseGroupService.groupMemberDetail(groupMemberVo, user);
        return jsonObject;
    }

    /**
     *  2.5 阈值指标数据查看接口
     *
     * @param resultData 请求参数
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "阈值指标数据查看接口", notes = "阈值指标数据查看接口")
    @RequestMapping(value = "/targetThresholdQuery")
    @OutInterface
    public Object queryTargetThreshold(HttpServletRequest request, @RequestBody JSONObject resultData) {
        Map<String,Object> result = new HashMap<>();
        try {
            LOGGER.info("call  targetThresholdQuery method---param <resultData>：" + resultData);
            String groupId = StringUtils.isNotEmpty(resultData.get("groupId").toString()) ? resultData.get("groupId").toString() : "";
            String targetId = StringUtils.isNotEmpty(resultData.get("targetId").toString()) ? resultData.get("targetId").toString() : "";
            LOGGER.info("江西政企阈值指标数据查看接口 请求参数集团ID={},请求参数成员ID={}", groupId, targetId);
            Map<String, Object> param = new HashMap<>();
            User user = (User) request.getSession().getAttribute("USER");
            if (user == null) {
                user = new User();
                user.setId("admin");
                user.setPwd("Jxyd!791");
            }
            param.put("groupId", groupId);
            param.put("targetId", targetId);
            param.put("user", user);
            List<Map<String, String>> data = enterpriseGroupService.queryTargetThreshold(param);
            result.put("resultCode","0");
            result.put("resultInfo","OK");
            result.put("data",data);
            LOGGER.info("江西政企调用阈值指标数据查看接口成功！");
        } catch (Exception e) {
            result.put("resultCode","1");
            result.put("resultInfo","false");
            result.put("data",new ArrayList<>());
            LOGGER.error("江西政企调用阈值指标数据查看接口异常! 异常信息为：{}", e);
        }
        return result;
    }

}
