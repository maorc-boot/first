package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.req.QueryActivityJxQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ITemplateJxService;
import com.asiainfo.biapp.pec.plan.service.IUserRoleRelationService;
import com.asiainfo.biapp.pec.plan.vo.ActivityVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**

 *
 * @author [Li.wang]
 * @CreateDate 2022/12/17 0017
 * @see com.asiainfo.biapp.pec.plan.controller.template
 */
@Api(tags = "江西:模板管理--集团模板")
@RequestMapping("/action/iop/approve/template/jx")
@RestController
@Slf4j
public class TemplateJxController {

    @Autowired
    private ITemplateJxService templateJxService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IUserRoleRelationService userRoleRelationService;
    /**
     * 营销活动信息查询

     */
    @ApiOperation(value = "江西:集团活动信息查询", notes = "活动信息查询")
    @ResponseBody
    @PostMapping("/queryActivityInfo")
    public ActionResponse queryActivityInfo(@RequestBody QueryActivityJxQuery form) {
        log.info("queryActivityInfo 入参:"+ JSONObject.toJSONString(form));
        ActionResponse response = ActionResponse.getSuccessResp();
        try {
            String userId= UserUtil.getUserId(request);
            boolean flag = userRoleRelationService.checkIsAdmin(userId);
            form.setUserId(userId);
            if (flag){
                form.setUserId(null);
            }
            IPage<ActivityVO> page = templateJxService.queryActivityInfo(form);
            response.setData(page);
        } catch (Exception e) {
            log.error("营销活动信息查询失败！",e);
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("营销活动信息查询失败!");
        }
        return response;
    }

}
