package com.asiainfo.biapp.pec.element.jx.material.controller;


import cn.hutool.extra.validation.BeanValidationResult;
import cn.hutool.extra.validation.ValidationUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.app.element.model.MaterialStatusQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.element.dto.MaterialStatusDTO;
import com.asiainfo.biapp.pec.element.dto.request.*;
import com.asiainfo.biapp.pec.element.dto.response.*;
import com.asiainfo.biapp.pec.element.jx.material.model.McdDimMaterialJxModel;
import com.asiainfo.biapp.pec.element.jx.material.request.*;
import com.asiainfo.biapp.pec.element.jx.material.response.DimMaterialJxResponse;
import com.asiainfo.biapp.pec.element.jx.material.service.IMcdDimMaterialJxService;
import com.asiainfo.biapp.pec.element.query.DimMaterialListQuery;
import com.asiainfo.biapp.pec.element.util.DataUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 营销素材 前端控制器
 * </p>
 *
 * @author ranpf
 * @since 2023-1-4
 */
@RestController
@RequestMapping("/api/mcdDimMaterial/jx")
@Api(value = "江西:素材管理相关服务接口", tags = {"江西:素材管理相关服务接口"})
@Slf4j
public class McdDimMaterialJxController {

    private static final String IS_APPROVE = "IS_NEED_APPROVE";


    @Resource
    private IMcdDimMaterialJxService mcdDimMaterialJxService;

    @PostMapping(path = "/queryMaterialPageList")
    @ApiOperation(value = "江西:素材管理列表查询", notes = "素材管理列表查询")
    public ActionResponse<IPage<DimMaterialJxResponse>> queryMaterialPageList(@RequestBody DimMaterialPageListQuery req) {
        log.info("start queryMaterialPageListJx para:{}", new JSONObject(req));
        return ActionResponse.getSuccessResp(mcdDimMaterialJxService.queryMaterialPageList(req));
    }

    /**
     * 查询渠道 下拉框
     *
     * @return
     */
    @ApiOperation(value = "江西:查询渠道下拉框", notes = "查询渠道 下拉框")
    @PostMapping(value = "/queryChannel")
    public ActionResponse<ChannelNameAndIdListResponse> queryChannelInfoList() {
        log.info("start queryChannelInfoListJx");
        return ActionResponse.getSuccessResp(mcdDimMaterialJxService.listChannelInfo());
    }

    /**
     * 触点 下拉框
     *
     * @param dimChannelRequest
     * @return
     */
    @ApiOperation(value = "江西:触点下拉框", notes = "触点 下拉框")
    @PostMapping(value = "/queryContact")
    public ActionResponse<ConctactListResponse> queryContactInfoList(@RequestBody DimChannelRequest dimChannelRequest) {
        log.info("start queryContactInfoListJx param {}", new JSONObject(dimChannelRequest));
        if (StringUtils.isNotBlank(dimChannelRequest.getChannelId())) {
            try {
                return ActionResponse.getSuccessResp(mcdDimMaterialJxService.listContactInfo(dimChannelRequest.getChannelId()));
            } catch (Exception e) {
                log.info("查询触点下拉框失败", e);
            }
        }
        log.error("查询触点下拉框失败 param {}", new JSONObject(dimChannelRequest));
        return ActionResponse.getFaildResp("查询触点 下拉框失败");

    }

    /**
     * 运营位 下拉框
     *
     * @param jxQuery
     * @return
     */
    @ApiOperation(value = "江西:运营位下拉框", notes = "运营位 下拉框")
    @PostMapping(value = "/queryPosition")
    public ActionResponse<AdivInfoListResponse> queryPositionInfoList(@RequestBody DimAdivInfoJxQuery jxQuery) {
        log.info("start queryPositionInfoListJx param {}", new JSONObject(jxQuery));

        if (StringUtils.isNotBlank(jxQuery.getChannelId())) {
            try {
                return ActionResponse.getSuccessResp(mcdDimMaterialJxService.listPositionInfo(jxQuery));
            } catch (Exception e) {
                log.info("查询运营位下拉框 失败", e);
            }
        }
        log.error("运营位下拉框查询失败! param {}", new JSONObject(jxQuery));
        return ActionResponse.getFaildResp("运营位下拉框查询失败!");
    }


    @PostMapping(path = "/saveOrUpdateMaterial")
    @ApiOperation(value = "江西:新增或修改素材接口", notes = "新增或修改素材")
    public ActionResponse saveOrUpdateMaterial(@RequestBody McdDimMaterialNewQuery mcdDimMaterial) {
        log.info("start saveOrUpdateMaterialjx para:{}", new JSONObject(mcdDimMaterial));
        if (StringUtils.isBlank(mcdDimMaterial.getMaterialId())) {
            //新建素材ID
            return saveMaterial(mcdDimMaterial, "新建成功", "新建失败");
        } else {
            //修改素材
            //修改素材校验素材名称是否存在
            // boolean flag = mcdDimMaterialJxService.validateMaterialNameByUpdate(mcdDimMaterial);
            // if (!flag) {
            //     return ActionResponse.getFaildResp("修改的素材名称已存在");
            // }
            //更新修改素材信息
            //上线状态 (江西目前无上线状态 先注释掉)
           /* if (mcdDimMaterial.getOnlineStatus() == 1) {
                return ActionResponse.getFaildResp("上线状态素材无法修改");
            }*/

            // final String isApprove = RedisUtils.getDicValue(IS_APPROVE);
            // if (StringUtils.isNotBlank(isApprove) && mcdDimMaterial.getMaterialStatus() == 2) {
            //     return ActionResponse.getFaildResp("审批通过状态素材无法修改");
            // }
            //修改信息
            // boolean re = mcdDimMaterialJxService.updateMaterial(mcdDimMaterial);
            // if (re) {
            //     return ActionResponse.getSuccessResp("修改成功");
            // } else {
            //     return ActionResponse.getFaildResp("修改失败");
            // }

            // 广点通渠道特殊性  修改后的素材id必须与之前不一致  所以修改素材需要先删后增 目前草稿
            boolean removeById = mcdDimMaterialJxService.removeById(mcdDimMaterial.getMaterialId());
            if (removeById) {
                return saveMaterial(mcdDimMaterial, "修改成功", "修改失败");
            } else {
                return ActionResponse.getFaildResp("修改失败");
            }
        }
    }

    /**
     * save素材
     *
     * @param mcdDimMaterial mcddim素材
     * @param succMsg
     * @param errorMsg
     * @return {@link ActionResponse}
     */
    private ActionResponse saveMaterial(McdDimMaterialNewQuery mcdDimMaterial, String succMsg, String errorMsg) {
        // 新建素材ID
        mcdDimMaterial.setMaterialId(DataUtil.generateDateStringId());
        // 校验新建素材的名称
        boolean flag = mcdDimMaterialJxService.validateMaterialName(mcdDimMaterial.getMaterialName());
        if (!flag) {
            return ActionResponse.getFaildResp("素材名称已存在");
        }
        boolean re = mcdDimMaterialJxService.saveOrUpdateMaterial(mcdDimMaterial);
        if (re) {
            return ActionResponse.getSuccessResp(succMsg);
        } else {
            return ActionResponse.getFaildResp(errorMsg);
        }
    }


    /**
     * 素材详细查询
     *
     * @param dimMaterialRequest
     * @return
     */
    @PostMapping(path = "/getMaterial")
    @ApiOperation(value = "江西:素材详细查询", notes = "素材详情查询")
    private ActionResponse<DimMaterialResponse> getMaterialInfo(@RequestBody DimMaterialRequest dimMaterialRequest) {
        log.info("start getMaterialInfo param {} ", new JSONObject(dimMaterialRequest));
        if (StringUtils.isBlank(dimMaterialRequest.getMaterialId())) {
            log.error("查询素材详情参数====> {}", new JSONObject(dimMaterialRequest));
            return ActionResponse.getFaildResp("查询素材详情失败");
        }
        return ActionResponse.getSuccessResp(mcdDimMaterialJxService.getMaterialInfo(dimMaterialRequest.getMaterialId()));
    }


    /**
     * 删除素材
     *
     * @param query
     * @return
     */
    @PostMapping(path = "/deleteMaterial")
    @ApiOperation(value = "江西:删除素材", notes = "删除素材根据素材ID")
    public ActionResponse<Boolean> deleteMaterial(@RequestBody DimMaterialRequest query) {
        log.info("start deleteMaterialJx para:{}", new JSONObject(query));
        String id = query.getMaterialId();
        boolean b = mcdDimMaterialJxService.removeById(id);
        if (b) {
            return ActionResponse.getSuccessResp("素材删除成功");
        }
        return ActionResponse.getFaildResp("素材删除失败");
    }


    /**
     * 删除素材
     *
     * @param query
     * @return
     */
    @PostMapping(path = "/batchDeleteMaterial")
    @ApiOperation(value = "江西:批量删除素材", notes = "批量删除素材根据素材ID")
    public ActionResponse<Boolean> batchDeleteMaterial(@RequestBody DimMaterialBatchDelQuery query) {
        log.info("start deleteMaterialJx para:{}", new JSONObject(query));
        List<String> ids = query.getMaterialIds();
        boolean b = mcdDimMaterialJxService.removeByIds(ids);
        if (b) {
            return ActionResponse.getSuccessResp("批量素材删除成功");
        }
        return ActionResponse.getFaildResp("批量素材删除失败");
    }

    /**
     * 移除优秀素材素材
     *
     * @param dimMaterialRequest
     * @return
     */
    @PostMapping(path = "/removeMaterialById")
    @ApiOperation(value = "删除素材", notes = "删除素材")
    public ActionResponse<Boolean> removeMaterialById(@RequestBody DimMaterialRequest dimMaterialRequest) {
        log.info("start removeMaterialById para:{}", new JSONObject(dimMaterialRequest));
        if (StringUtils.isNotBlank(dimMaterialRequest.getMaterialId())) {
            //删除的时候异步删除文件
            final boolean removeResult = mcdDimMaterialJxService.removeMaterialById(dimMaterialRequest.getMaterialId());
            return ActionResponse.getSuccessResp(removeResult);
            //return ActionResponse.getSuccessResp(mcdDimMaterialJxService.removeById(dimMaterialRequest.getMaterialId()));
        }
        log.error("素材删除失败,param {}", new JSONObject(dimMaterialRequest));
        return ActionResponse.getFaildResp("素材删除失败");
    }

    /**
     * 素材导出文件
     *
     * @param exportQuery 条件入参
     * @param response
     */
    @PostMapping(path = "/exportMaterialsFile")
    @ApiOperation(value = "江西:素材导出文件", notes = "素材导出文件")
    public void exportMaterialsFile(@RequestBody ExportMaterialInfoQuery exportQuery, HttpServletResponse response) {
        try {
            List<Object> exportMaterialsList = mcdDimMaterialJxService.exportMaterialsFile(exportQuery);
            String[] excelHeader = {"素材名称", "产品编码","产品名称", "渠道ID","渠道名称","运营位ID", "运营位名称", "失效时间", "素材类型名称", "文字内容", "链接", "描述"};
            mcdDimMaterialJxService.exportExcel("素材管理数据导出文件.xls", excelHeader, exportMaterialsList, response);
            log.info("导出素材数据成功!");
        } catch (Exception e) {
            log.error("导出素材数据失败", e);
        }
    }

    /**
     * 批量导入素材，excel文件导入
     *
     * @return
     */
    @PostMapping(path = "/uploadMaterialsFile")
    @ApiOperation(value = "江西:批量导入素材接口", notes = "批量导入素材接口")
    public ActionResponse<Boolean> uploadMaterialsFile(@RequestParam(value = "uploadMaterialsFile", required = true) MultipartFile uploadMaterialsFile,HttpServletRequest request) {
        if (uploadMaterialsFile != null) {
             UserSimpleInfo user = UserUtil.getUser(request);
            try {
                final boolean flag = mcdDimMaterialJxService.uploadMaterialsFile(uploadMaterialsFile,user.getUserId());
                return ActionResponse.getSuccessResp(flag);
            } catch (Exception e) {
                log.error("导入内容异常：{}", e.getMessage());
            }
        }
        return ActionResponse.getFaildResp("导入异常,文件为null");
    }

    /**
     * 素材导入模板下载
     *
     * @param response
     */
    @PostMapping(path = "/materialImportTemplateDownload")
    @ApiOperation(value = "江西:素材导入模板下载", notes = "素材导入模板下载")
    public void importMaterialsTemplate(HttpServletResponse response) {
        mcdDimMaterialJxService.importMaterialsTemplate(response);
    }

    /**
     * 上传素材 （图片）
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "江西:上传素材（图片）", notes = "上传素材（图片）")
    @PostMapping(path = "/uploadMaterialPicture")
    public ActionResponse uploadMaterialPicture(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {

        if (getComparison(file)) return ActionResponse.getFaildResp("图片上传支持格式(jpg,png,jpeg,gif)");
        Map<String, String> map = mcdDimMaterialJxService.uploadMaterialPicture(file);
        if ("0".equals(map.get("status"))) {
            return ActionResponse.getFaildResp(map.get("error"));
        } else if ("1".equals(map.get("status"))) {
            map.remove("status");
            return ActionResponse.getSuccessResp(map);
        } else {
            return ActionResponse.getFaildResp("上传异常");
        }
    }

    /**
     * 判断是否是允许上传的文件类型
     *
     * @param fileName
     * @return
     */
    protected static boolean isPermissionFile(String fileName) {

        if (StringUtils.isEmpty(fileName) || fileName.indexOf(".") == -1) {
            return false;
        }
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        return permitFileExtension().contains(fileType);
    }

    /**
     * 上传允许的文件类型
     *
     * @return
     */
    protected static List<String> permitFileExtension() {
        return Arrays.asList("txt", "doc", "docx", "jpg", "png", "gif", "xls", "xlsx", "jpeg",
                "zip", "tag", "mp3", "mp4", "avi");
    }

    /**
     * 上传素材 (视频)
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "江西:上传素材 (视频)", notes = "上传素材 (视频)")
    @PostMapping(path = "/uploadMaterialVideo")
    public ActionResponse uploadMaterialVideo(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {

        if (getComparison(file)) return ActionResponse.getFaildResp("上传的文件类型不符合要求");

        Map<String, String> map = mcdDimMaterialJxService.uploadMaterialVideo(file);
        if ("0".equals(map.get("status"))) {
            return ActionResponse.getFaildResp(map.get("error"));
        } else if ("1".equals(map.get("status"))) {
            map.remove("status");
            return ActionResponse.getSuccessResp(map);
        } else {
            return ActionResponse.getFaildResp("上传异常");
        }
    }

    /**
     * 上传素材 (sftp远程服务器)
     *
     * @param request
     * @param file
     * @return
     * @throws Exception
     */
    @ApiIgnore
    @ApiOperation(value = "上传素材 : sftp", notes = "上传素材 : sftp")
    @PostMapping(path = "/uploadMaterialPictureBySftp")
    public ActionResponse uploadMaterialPictureBySftp(HttpServletRequest request,
                                                      @RequestParam(value = "file", required = true) MultipartFile file) {

        if (getComparison(file)) return ActionResponse.getFaildResp("上传的文件类型不符合要求");

        Map<String, String> map = mcdDimMaterialJxService.uploadMaterialPictureBySftp(file, request);
        if ("0".equals(map.get("STATUS"))) {
            return ActionResponse.getFaildResp(map.get("error"));
        } else if ("1".equals(map.get("status"))) {
            map.remove("status");
            return ActionResponse.getSuccessResp(map);
        } else {
            return ActionResponse.getFaildResp("上传异常");
        }
    }

    /**
     * 比对 传输文件是否允许
     *
     * @param file
     * @return
     */
    private boolean getComparison(@RequestParam(value = "file", required = true) MultipartFile file) {
        final String type = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        return permitFileExtension().stream().noneMatch(t -> t.equals(type));
    }

    /**
     * 图片 视频预览功能
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "江西:素材图片视频预览 ", notes = "素材图片视频预览")
    @PostMapping(path = "/loadImage")
    public ActionResponse<MaterialLoadImageResponse> loadImage(@RequestBody DimMaterialRequest dimMaterialRequest,
                                                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("start loadImageJx param {}", new JSONObject(dimMaterialRequest));
        if (StringUtils.isBlank(dimMaterialRequest.getMaterialId())) {
            throw new RuntimeException("预览失败,无法获取素材信息");
        }
        String resourceUrl = mcdDimMaterialJxService.queryMaterialById(dimMaterialRequest.getMaterialId()).get(0).getResourceUrl();
        mcdDimMaterialJxService.loadImage(resourceUrl, dimMaterialRequest, response);
        return null;
    }

    /**
     * 素材上线, 下线;
     *
     * @param materialStatusDTO
     * @return
     */
    @PostMapping(path = "/updateMaterialStatus")
    @ApiOperation(value = "素材上下线", notes = "素材上下线")
    public ActionResponse<String> updateMaterialStatus(@RequestBody @Valid MaterialStatusDTO materialStatusDTO) {
        log.info("start updateMaterialStatusJx param:{}", new JSONObject(materialStatusDTO));
        ActionResponse<String> actionResponse = new ActionResponse<>();
        try {
            if (mcdDimMaterialJxService.saveOrUpdatePlanStatus(materialStatusDTO)) {
                actionResponse.setStatus(ResponseStatus.SUCCESS);
                actionResponse.setMessage("保存成功！");
                actionResponse.setData(materialStatusDTO.getMaterialId());
            } else {
                actionResponse.setMessage("保存失败！");
                actionResponse.setStatus(ResponseStatus.ERROR);
            }
        } catch (Exception e) {
            log.error("素材上线/下线失败：", e);
            actionResponse.setMessage("保存失败！");
            actionResponse.setStatus(ResponseStatus.ERROR);
        }
        return actionResponse;
    }

    @PostMapping(path = "/updateMaterialApproveRejectStatus")
    @ApiOperation(value = "更新素材为审批驳回状态", notes = "更新素材为审批驳回状态")
    public ActionResponse updateMaterialApproveRejectStatus(@RequestBody @Valid MaterialStatusQuery query) {
        log.info("start updateMaterialApproveRejectStatus param:{}", JSONUtil.toJsonStr(query));
        try {
            boolean update = mcdDimMaterialJxService.update(Wrappers.<McdDimMaterialJxModel>lambdaUpdate()
                    .set(McdDimMaterialJxModel::getMaterialStatus, query.getMaterialStat())
                    .eq(McdDimMaterialJxModel::getMaterialId, query.getFlowId()));
            if (update) {
                return ActionResponse.getSuccessResp("更新素材为审批驳回状态成功");
            } else {
                return ActionResponse.getFaildResp("更新素材为审批驳回状态失败");
            }
        } catch (Exception e) {
            log.error("更新素材为审批驳回状态异常：", e);
            return ActionResponse.getFaildResp("更新素材为审批驳回状态失败");
        }
    }

    /**
     * 根据运营位id 获取素材列表
     * MaterialIntelligentMatching
     *
     * @param dimMaterialListQuery
     * @return
     */
    @ApiOperation(value = "素材智能匹配(根据运营位ID获取素材列表)", notes = "素材智能匹配")
    @RequestMapping(value = "/getIntelligentMatchingMaterialList", method = { RequestMethod.POST, RequestMethod.GET })
    public ActionResponse<List<Object>> getIntelligentMatchingMaterialList(
            @RequestBody(required = false) DimMaterialListQuery dimMaterialListQuery,
            @RequestParam(value = "positionId", required = false) String positionId,
            @RequestParam(value = "type", required = false) String type) {
        boolean isGet = false;
        if (null == dimMaterialListQuery) {
            dimMaterialListQuery = new DimMaterialListQuery();
            dimMaterialListQuery.setPositionId(positionId);
            dimMaterialListQuery.setType(type);
            isGet = true;
        }
        BeanValidationResult warpValidate = ValidationUtil.warpValidate(dimMaterialListQuery);
        if (!warpValidate.isSuccess()) {
            return ActionResponse.getFaildResp("运营位ID不能为空");
        }
        log.info("start getIntelligentMatchingMaterialList param {}", new JSONObject(dimMaterialListQuery).toString());
        List<McdDimMaterialJxModel> materialList = mcdDimMaterialJxService.getIntelligentMatchingMaterialList(dimMaterialListQuery);
        if (isGet) {
            List<Map<String, Object>> result = materialList.stream().map(t -> {
                Map<String, Object> item = new HashMap<>();
                item.put("attrId", t.getMaterialId());
                item.put("attrValueId", t.getMaterialId());
                item.put("attrValue", t.getMaterialId());
                item.put("attrValueName", t.getMaterialName());
                item.put("enumOrder", "1");
                item.put("enumType", "materialList");
                item.put("parentId", "");
                item.put("resourceUrl", t.getResourceUrl());
                return item;
            }).collect(Collectors.toList());
            return ActionResponse.getSuccessResp(result);
        }
        return ActionResponse.getSuccessResp(materialList);
    }

    /**
     * 素材到期失效;
     *
     * @return
     */
    @PostMapping(path = "/mcdMaterialInvalidTask")
    @ApiOperation(value = "江西素材失效", notes = "素材失效")
    public void checkMaterialInvalidTask() {

        //获取IOP待办数据（审批待办+阅知待办）
        LambdaQueryWrapper<McdDimMaterialJxModel> emisWrapper = new LambdaQueryWrapper<>();
        emisWrapper.ne(McdDimMaterialJxModel::getMaterialStatus, 3)
                .le(McdDimMaterialJxModel::getInvalidDate,new Date());
        List<McdDimMaterialJxModel> todoTaskList = mcdDimMaterialJxService.list(emisWrapper);
        if (Objects.isNull(todoTaskList)){
            log.info("没有失效的素材可修改!");
            return;
        }

        for (McdDimMaterialJxModel model:todoTaskList){
            model.setMaterialStatus(3);
            model.setOnlineStatus(0);
            mcdDimMaterialJxService.updateById(model);
        }

    }

    //@ApiIgnore()
    //@ApiOperation(value = "预览图片 视频 ",notes = "预览图片 视频")
    //@PostMapping("/loadImages")
    //public ActionResponse<LoadUrlResponse> loadIamges(@RequestBody DimMaterialRequest dimMaterialRequest,
    //                                                  HttpServletRequest request, HttpServletResponse response) {
    //    log.info("start loadImage param {}" , new JSONObject(dimMaterialRequest));
    //    if (StringUtils.isBlank(dimMaterialRequest.getMaterialId())){
    //        throw new RuntimeException("预览失败,无法获取素材信息");
    //    }
    //    String resourceUrl = mcdDimMaterialJxService.queryMaterialById(dimMaterialRequest.getMaterialId())
    //            .get(0).getResourceUrl();
    //    request.getRemoteAddr();
    //    String url =  "http://" + request.getLocalAddr() + ":"
    //                          +request.getLocalPort()
    //                          + request.getContextPath()      //项目名称
    //                          + request.getServletPath()    //请求页面或其他地址
    //                          + resourceUrl;
    //    LoadUrlResponse loadUrlResponse = new LoadUrlResponse();
    //    loadUrlResponse.setLoadUrl(url);
    //    return ActionResponse.getSuccessResp(loadUrlResponse);
    //}


}

