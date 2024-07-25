package com.asiainfo.biapp.pec.element.jx.material.controller;

import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.client.pec.approve.model.SubmApproveQuery;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessDTO;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.element.dto.request.MaterialTemplateRequest;
import com.asiainfo.biapp.pec.element.jx.material.request.DimMaterialApproveJxQuery;
import com.asiainfo.biapp.pec.element.jx.material.response.DimMaterialJxResponse;
import com.asiainfo.biapp.pec.element.jx.material.service.IMcdDimMaterialApproveJxService;
import com.asiainfo.biapp.pec.element.jx.material.service.IMcdDimMaterialJxService;
import com.asiainfo.biapp.pec.element.query.appr.ApprRecord;
import com.asiainfo.biapp.pec.element.query.appr.MaterialStatusQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * Description : 素材审批服务前端控制器
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-3
 */
@RestController
@RequestMapping("/api/mcdDimMaterialApprove/jx")
@Api(value = "江西:素材审批管理服务", tags = {"江西:素材审批管理服务"})
@Slf4j
public class McdDimMaterialApproveJxController {
    
    @Resource
    private IMcdDimMaterialJxService materialJxService;
    
    @Resource
    private IMcdDimMaterialApproveJxService mcdDimMaterialApproveJxService;
    
    @Autowired
    private HttpServletRequest request;

  /**
   * //素材审批管理 //素材审批管理-审批素材分页(审批中的素材)
   *
   * @param request
   * @return
   */
  @ApiOperation(value = "江西:审批素材分页", notes = "审批素材分页")
  @PostMapping(path = "/pagelistApproveMaterial")
  public ActionResponse<IPage<DimMaterialJxResponse>> pagelistApproveMaterial(
      @RequestBody MaterialTemplateRequest request) {
        log.info("start pagelistApproveMaterialJx params:{}",new JSONObject(request).toString());
        IPage<DimMaterialJxResponse> approveMaterialPageList = materialJxService.pagelistApproveMaterial(request);
        return ActionResponse.getSuccessResp(approveMaterialPageList);
    }
    
    
    /**
     * 素材提交审批
     */
    @ApiOperation(value = "江西:素材提交审批", notes = "素材提交审批")
    @PostMapping("/subMaterialApprove")
    public ActionResponse subApprove(@RequestBody @Valid SubmitProcessQuery req) {
        final UserSimpleInfo user = UserUtil.getUser(request);
        mcdDimMaterialApproveJxService.submitMaterialAppr(req, user);
        return ActionResponse.getSuccessResp();
    }
    
    
    ///**
    // * 获取素材审批人
    // *
    // * @return
    // */
    //@ApiOperation(value = "获取节点审批人信息", notes = "获取节点审批人信息")
    //@PostMapping("/getMaterialApprovers")
    //public ActionResponse<List<ApproveUserQuery>> getMaterialApprover() {
    //    List<ApproveUserQuery> approver = mcdDimMaterialApproveJxService.getMaterialApprover();
    //    return ActionResponse.getSuccessResp(approver);
    //}
    
    @PostMapping("/getMaterialApprover")
    @ApiOperation(value = "江西:获取流程实例节点下级审批人",notes = "获取流程实例节点下级审批人")
    public ActionResponse<SubmitProcessDTO> getNodeApprover(@RequestBody SubmitProcessDTO submitProcessDTO){
        log.info("start getNodeApprover para:{}",new JSONObject(submitProcessDTO).toString());
        SubmitProcessDTO processDTO = mcdDimMaterialApproveJxService.getMaterialApprover(submitProcessDTO);
        return ActionResponse.getSuccessResp(processDTO);
    }
    
    /**
     * 审批流修改状态
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "江西:审批流转修改状态")
    @PostMapping("/updateMaterialStatus")
    public ActionResponse updateMaterialStatus(@RequestBody @Valid MaterialStatusQuery req) {
        log.info("start updateMaterialStatus param {}",new JSONObject(req).toString());
        mcdDimMaterialApproveJxService.modifyMaterialStatusFromAppr(req);
        return ActionResponse.getSuccessResp();
    }
    
    
    /**
     * 获取提交审批对象
     * @param submApproveQuery
     * @return
     */
    @ApiOperation(value = "江西:获取提交审批对象信息", notes = "获取提交审批对象信息")
    @PostMapping("/getCmpApprovalProcess")
    public ActionResponse<SubmitProcessQuery> getCmpApprovalProcess(@RequestBody SubmApproveQuery submApproveQuery) {
        return ActionResponse.getSuccessResp(mcdDimMaterialApproveJxService.getCmpApprovalProcess(submApproveQuery));
    }
    
    /**
     * 素材审批列表
     *
     * @param req 关键字搜索
     * @return 素材审批列表
     */
    @ApiOperation(value = "江西:素材审批列表", notes = "素材审批列表")
    @PostMapping("/approveRecord")
    public IPage<ApprRecord> approveRecord(@RequestBody DimMaterialApproveJxQuery req) {
        return mcdDimMaterialApproveJxService.approveRecord(req);
    }
}
