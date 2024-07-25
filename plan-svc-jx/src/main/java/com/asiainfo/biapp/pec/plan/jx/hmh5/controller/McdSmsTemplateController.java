package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.McdSmsTemplateDao;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCareSmsTemplate;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdSmsTemplateService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.model.McdFrontCareSmsLabelRela;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service.McdFrontCareSmsLabelRelaService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service.McdFrontCareSmsLabelService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.*;
import com.asiainfo.biapp.pec.plan.model.McdCustgroupAttrList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * description: 江西客户通短信模板（关怀短信&代维短信）控制层
 * 注：代维短信是根据用户角色权限控制的,目前设置的代维短信角色id=888  代维短信需要查询标签以及保存或修改模板与标签的关系--MCD_FRONT_CARE_SMS_LABEL_RELA
 *
 * @author: lvchaochao
 * @date: 2023/3/14
 */
@Slf4j
@Api(value = "客户通-客户通短信模板(关怀短信&代维短信)",tags = "客户通-客户通短信模板")
@RestController
@RequestMapping("/sms/template")
@DataSource("khtmanageusedb")
public class McdSmsTemplateController {

    @Autowired
    private McdSmsTemplateService mcdSmsTemplateService;

    @Autowired
    private McdFrontCareSmsLabelRelaService mcdFrontCareSmsLabelRelaService;

    @Autowired
    private McdSmsTemplateDao mcdSmsTemplateDao;

    @Autowired
    private McdFrontCareSmsLabelService smsLabelService;

    @ApiOperation(value = "获取关怀短信模板列表数据")
    @PostMapping("/getCareSmsTemplate")
    // @DataSource("khtmanageusedb")
    public IPage<McdCareSmsTemplateResp> getCareSmsTemplate(@RequestBody McdCareSmsTemplateQuery query) {
        log.info("获取关怀短信模板列表数据入参：{}", JSONUtil.toJsonStr(query));
        Page<McdCareSmsTemplateResp> page = new Page<>(query.getCurrent(), query.getSize());
        return mcdSmsTemplateService.getCareSmsTemplate(page, query);
    }

    @ApiOperation(value = "新建或修改关怀短信模板")
    @PostMapping("/saveCareSmsTemplate")
    // @DataSource("khtmanageusedb")
    public ActionResponse saveCareSmsTemplate(@RequestBody McdCareSmsTemplate vo, HttpServletRequest request) {
        log.info("新建关怀短信模板入参：{}", JSONUtil.toJsonStr(vo));
        UserSimpleInfo user = UserUtil.getUser(request);
        vo.setCreateUserId(user.getUserId());
        vo.setCityId(user.getCityId());
        if (StrUtil.isEmpty(vo.getTemplateCode())) {
            // 获取序列值
            int seq = mcdSmsTemplateDao.getSeq();
            vo.setTemplateCode(seq + "c");
            // 保存
            boolean save = mcdSmsTemplateService.save(vo);
            if (save) {
                return ActionResponse.getSuccessResp("新建成功").setData(vo.getTemplateCode());
            } else {
                return ActionResponse.getSuccessResp("新建失败").setData(null);
            }
        } else {
            // 修改
            boolean updateById = mcdSmsTemplateService.updateById(vo);
            if (updateById) {
                return ActionResponse.getSuccessResp("修改成功").setData(vo.getTemplateCode());
            } else {
                return ActionResponse.getSuccessResp("修改失败").setData(null);
            }
        }
    }

    @ApiOperation(value = "删除关怀短信模板")
    @PostMapping("/delCareSmsTemplate")
    // @DataSource("khtmanageusedb")
    public ActionResponse delCareSmsTemplate(@RequestBody McdIdQuery query) {
        log.info("删除关怀短信模板入参：{}", JSONUtil.toJsonStr(query));
        boolean removeById = mcdSmsTemplateService.removeById(query.getId());
        if (removeById) {
            return ActionResponse.getSuccessResp("删除成功");
        } else {
            return ActionResponse.getSuccessResp("删除失败");
        }
    }

    @ApiOperation(value = "获取营销用语变量替换信息")
    @PostMapping("/getCustGroupVars")
    // @DataSource("khtmanageusedb")
    public ActionResponse getCustGroupVars(@RequestBody SmsTemplateCustVarsQueryVO queryVO) {
        log.info("短信模板-获取营销用语变量替换信息入参对象={}", JSONUtil.toJsonStr(queryVO));
        List<McdCustgroupAttrList> custGroupVars = mcdSmsTemplateService.getCustGroupVars(queryVO);
        log.info("短信模板-获取营销用语变量替换信息:{}", JSONUtil.toJsonStr(custGroupVars));
        return ActionResponse.getSuccessResp(custGroupVars);
    }

    @ApiOperation(value = "查询关怀短信模板详情")
    @PostMapping("/getCareSmsTemplateDetail")
    // @DataSource("khtmanageusedb")
    public ActionResponse getCareSmsTemplateDetail(@RequestBody McdIdQuery query, HttpServletRequest request) {
        log.info("查询关怀短信模板详情入参：{}", JSONUtil.toJsonStr(query));
        McdCareSmsTemplateResp careSmsTemplateDetail = mcdSmsTemplateService.getCareSmsTemplateDetail(query.getId(),request);
        log.info("根据模板编码查询关怀短信模板详情:{}", JSONUtil.toJsonStr(careSmsTemplateDetail));
        return ActionResponse.getSuccessResp(careSmsTemplateDetail);
    }

    @ApiOperation(value = "查询代维短信标签列表", notes = "查询代维短信标签列表--代维短信使用")
    @PostMapping("/getLabelListDaiwei")
    public ActionResponse getLabelListDaiwei() {
        List<McdFrontCareSmsLabel> labelListDaiwei = mcdSmsTemplateDao.getLabelListDaiwei();
        return ActionResponse.getSuccessResp(labelListDaiwei);
    }

    @ApiOperation(value = "根据模板id查询关联的标签数据", notes = "根据模板id查询关联的标签数据--代维短信修改回显使用")
    @GetMapping("/getRelaLabels")
    public ActionResponse getRelaLabels(@RequestParam("smsTemplateCode") String smsTemplateCode) {
        List<McdFrontCareSmsLabel> labelListDaiwei = mcdSmsTemplateDao.getRelaLabels(smsTemplateCode);
        return ActionResponse.getSuccessResp(labelListDaiwei);
    }

    @ApiOperation(value = "保存或修改短信模板与标签关系", notes = "保存或修改短信模板与标签关系--代维短信使用")
    @PostMapping("/saveOrUpdateRela")
    public ActionResponse saveOrUpdateRela(@RequestBody SaveOrUpdateRelaParam saveOrUpdateRelaParam) {
        log.info("保存或修改短信模板与标签关系入参={}", JSONUtil.toJsonStr(saveOrUpdateRelaParam));
        return mcdSmsTemplateService.saveOrUpdateRela(saveOrUpdateRelaParam);
    }

    @ApiOperation(value = "删除短信模板引用的标签", notes = "删除代维短信模板引用的标签--代维短信使用")
    @PostMapping("/deleteTemplateLabel")
    public ActionResponse deleteTemplateLabel(@RequestBody McdIdQuery query) {
        log.info("删除短信模板引用的标签入参：{}", JSONUtil.toJsonStr(query));
        boolean removeById = mcdFrontCareSmsLabelRelaService.removeById(Wrappers.<McdFrontCareSmsLabelRela>update().lambda().eq(McdFrontCareSmsLabelRela::getSmsTemplateCode, query.getId()));
        if (removeById) {
            return ActionResponse.getSuccessResp("删除成功");
        } else {
            return ActionResponse.getSuccessResp("删除失败");
        }
    }

    @ApiOperation(value = "客户通关怀短信模板提交审批", notes = "客户通关怀短信模板提交审批")
    @PostMapping("/subCareSmsTemplateApprove")
    // @DataSource("khtmanageusedb")
    public ActionResponse subCareSmsTemplateApprove(@RequestBody @Valid SubmitProcessQuery req, HttpServletRequest request) {
        log.info("【客户通关怀短信模板】subCareSmsTemplateApprove param:{}", JSONUtil.toJsonStr(req));
        final UserSimpleInfo user = UserUtil.getUser(request);
        mcdSmsTemplateService.subCareSmsTemplateApprove(req, user);
        return ActionResponse.getSuccessResp();
    }

    @PostMapping("/getCareSmsTemplateApprover")
    @ApiOperation(value = "获取客户通关怀短信模板审批流程实例节点下级审批人",notes = "获取客户通关怀短信模板审批流程实例节点下级审批人")
    public ActionResponse<SubmitProcessJxDTO> getCareSmsTemplateApprover(@RequestBody SubmitProcessJxDTO submitProcessDTO){
        log.info("【客户通关怀短信模板】getCareSmsTemplateApprover param:{}", JSONUtil.toJsonStr(submitProcessDTO));
        return mcdSmsTemplateService.getCareSmsTemplateApprover(submitProcessDTO);
    }

    @ApiOperation(value = "客户通关怀短信模板审批列表", notes = "客户通关怀短信模板审批列表")
    @PostMapping("/approveSmsTemplateRecord")
    public IPage<SmsTemplateApprRecord> approveSmsTemplateRecord(@RequestBody CareSmsTemplateApproveJxQuery req) {
        log.info("客户通关怀短信模板审批列表para:{}", JSONUtil.toJsonStr(req));
        return mcdSmsTemplateService.approveRecord(req);
    }

    @ApiIgnore // 此注解作用是：可以用在类、方法上，方法参数中，用来屏蔽某些接口或参数，使其不在页面上显示
    @ApiOperation(value = "客户通关怀短信模板审批流转修改状态")
    @PostMapping("/modifyCareSmsTemplateStatus")
    public ActionResponse modifyCareSmsTemplateStatus(@RequestBody ModifyCareSmsTemplateStatusParam careSmsTemplateStatusParam) {
        log.info("客户通关怀短信模板审批流转修改状态param={}", JSONUtil.toJsonStr(careSmsTemplateStatusParam));
        mcdSmsTemplateService.update(Wrappers.<McdCareSmsTemplate>update().lambda()
                .set(McdCareSmsTemplate::getApprovalStatus, careSmsTemplateStatusParam.getApprovalStatus())
                .eq(McdCareSmsTemplate::getTemplateCode, careSmsTemplateStatusParam.getTemplateCode()));
        return ActionResponse.getSuccessResp();
    }

    @ApiIgnore
    @GetMapping("/selectCareSmsTmpApprFlowId")
    @ApiOperation(value = "客户通关怀短信模板审批流程ID查询")
    // @DataSource("khtmanageusedb")
    public ActionResponse<String> selectCareSmsTmpApprFlowId(@NotNull(message = "模板编码不可为空！") String templateCode) {
        UserSimpleInfo user = UserUtilJx.getUser();
        if (user.getUserId() == null) {
            log.error("没有获取到登录用户，正在创建虚拟用户！--------------------------------------------------------------");
            user.setUserId("admin01");
            user.setCityId("999");
        }
        log.info("用户：{}正在查询客户通关怀短信模板审批流程ID，templateCode值为：{}！", user.getUserName(), templateCode);
        McdCareSmsTemplate mcdCareSmsTemplate = mcdSmsTemplateService.getOne(Wrappers.<McdCareSmsTemplate>query().lambda().eq(McdCareSmsTemplate::getTemplateCode, templateCode));
        if (ObjectUtil.isEmpty(mcdCareSmsTemplate)) throw new BaseException("无效的templateCode值！");
        String approveFlowId = mcdCareSmsTemplate.getApproveFlowId();
        ActionResponse successResp = ActionResponse.getSuccessResp();
        successResp.setData(approveFlowId);
        return successResp;
    }

}
