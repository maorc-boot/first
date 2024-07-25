package com.asiainfo.biapp.pec.plan.jx.intellpushprism.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampsegServiceJx;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.TemplateByTypeRespondDTO;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna.*;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity.McdPrismCampTemplate;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.DnaColumnService;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.IIntellPushPrismService;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.*;
import com.asiainfo.biapp.pec.plan.model.McdPlanDef;
import com.asiainfo.biapp.pec.plan.vo.req.CampsegRootIdQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * description: 智推棱镜控制层
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
@RestController
@RequestMapping("/api/intellpushprism")
@Api(value = "智推棱镜", tags = {"智推棱镜控制层"})
@Slf4j
public class IntellPushPrismController {

    @Autowired
    private IIntellPushPrismService intellPushPrismService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DnaColumnService dnaColumnService;

    @Autowired
    private IMcdCampsegServiceJx campsegService;

    @ApiOperation(value = "选择标签", notes = "选择标签支持模糊搜索")
    @PostMapping("/column/search0")
    public ActionResponse<IPage<ColumnSearchRespondDTO.ColumnList>> search(@RequestBody @Valid ColumnSearchQuery columnSearchQuery) {
        log.info("营销画布选择标签.标签名:{}", columnSearchQuery.getColumnName());

        DNAActionResponse<ColumnSearchRespondDTO> dnaActionResponse = dnaColumnService.search(columnSearchQuery.transToColumnSearchRequestDTO());
        if (DNAResponseStatus.SUCCESS.getCode().equals(dnaActionResponse.getCode())) {
            log.info("营销画布选择标签成功.标签名:{}", columnSearchQuery.getColumnName());
            return ActionResponse.getSuccessResp(dnaActionResponse.getData().wrapPage(columnSearchQuery));
        }

        log.warn("营销画布选择标签失败.标签名:{}, 异常信息:{}", columnSearchQuery.getColumnName(), dnaActionResponse.getMsg());
        return ActionResponse.getFaildResp(dnaActionResponse.getMsg());
    }

    @ApiOperation(value = "获取标签映射值", notes = "获取标签映射值")
    @PostMapping("/column/valuePage")
    public ActionResponse<IPage<ColumnValuePageRespondDTO.ValueList>> getValuePage(@RequestBody @Valid ColumnValuePageQuery columnValuePageQuery) {
        log.info("营销画布获取标签映射值.标签编码:{}", columnValuePageQuery.getColumnNum());

        DNAActionResponse<ColumnValuePageRespondDTO> dnaActionResponse = dnaColumnService.getValuePage(columnValuePageQuery.transToColumnValuePageRequestDTO());
        if (DNAResponseStatus.SUCCESS.getCode().equals(dnaActionResponse.getCode())) {
            log.info("营销画布获取标签映射值成功.标签编码:{}", columnValuePageQuery.getColumnNum());
            return ActionResponse.getSuccessResp(dnaActionResponse.getData().wrapPage(columnValuePageQuery));
        }

        log.warn("营销画布选择标签失败.标签编码:{}, 异常信息:{}", columnValuePageQuery.getColumnNum(), dnaActionResponse.getMsg());
        return ActionResponse.getFaildResp(dnaActionResponse.getMsg());
    }

    @ApiOperation(value = "实时获取客群数量", notes = "根据标签映射值实时获取客群数量")
    @PostMapping("/column/getTargetUserCount")
    public ActionResponse getTargetUserCount(@RequestBody TargetUserCountReqDTO query) {
        DNACustomActionResponse dnaActionResponse = dnaColumnService.getTargetUserCount(query);
        log.info("根据标签映射值实时获取客群数量响应={}", JSONUtil.toJsonStr(dnaActionResponse));
        if (DNAResponseStatus.SUCCESS.getCode().equals(dnaActionResponse.getCode())) {
            return ActionResponse.getSuccessResp(dnaActionResponse.getData());
        }
        log.warn("根据标签映射值实时获取客群数量失败,异常信息:{}", dnaActionResponse.getMsg());
        return ActionResponse.getFaildResp(dnaActionResponse.getMsg());
    }

    @ApiOperation(value = "创建场景蓝图", notes = "智推棱镜：创建场景蓝图")
    @PostMapping("/saveScene")
    public ActionResponse<List<String>> saveScene(@RequestBody @Valid IntellPushPrismReqVO req) {
        log.info("智推棱镜：创建主题场景蓝图入参={}", JSONUtil.toJsonStr(req));
        String themeId = req.getThemeBaseInfo().getThemeId();
        String themeName = req.getThemeBaseInfo().getThemeName();
        // 为空 表示新建  需要校验
        if (StrUtil.isEmpty(themeId)) {
            log.warn("主题校验、活动校验开始...");
            // 主题校验
            boolean checkThemes = intellPushPrismService.checkThemes(themeId, themeName);
            Assert.isTrue(checkThemes, "主题名重复，保存主题失败");
            req.getCampInfo().forEach(reqVo -> {
                String campsegId = reqVo.getBaseCampInfo().getCampsegId();
                String campsegName = reqVo.getBaseCampInfo().getCampsegName();
                // 活动校验
                Map<Object, Object> resMap = campsegService.checkTacticsDetail(campsegName, campsegId);
                Assert.isTrue(BooleanUtil.toBoolean(StrUtil.toString(resMap.get("bool"))), "策略名【"+ resMap.get("campsegName") + "】重复，保存策略失败");
                intellPushPrismService.checkCampEndDate(reqVo);
            });
            log.warn("主题校验、活动校验结束...");
        }
        return ActionResponse.getSuccessResp((Object) intellPushPrismService.saveScene(req, UserUtil.getUser(request)));
    }

    @PostMapping(path = "/batchImpPlanTmpDownload")
    @ApiOperation(value = "批量导入产品模板下载", notes = "智推棱镜：批量导入产品模板下载")
    public void batchImpPlanTmpDownload(HttpServletResponse response) {
        intellPushPrismService.batchImpPlanTmpDownload(response);
    }

    @ApiOperation(value = "批量导入产品", notes = "智推棱镜：批量导入产品")
    @PostMapping("/batchImportProduct")
    public ActionResponse<List<McdPlanDef>> batchImportProduct(@RequestParam("multipartFile") MultipartFile multipartFile) {
        if (ObjectUtil.isEmpty(multipartFile)) {
            return ActionResponse.getFaildResp("批量导入产品异常,文件为null");
        }
        try {
            String fileName = multipartFile.getOriginalFilename();
            // 校验文件格式
            if (!fileName.toLowerCase().endsWith(".xls") && !fileName.toLowerCase().endsWith(".xlsx")) {
                return ActionResponse.getFaildResp("选择文件的类型不正确，请重新选择xlsx、xls类型的文件!");
            }
            List<McdPlanDef> planDefs = intellPushPrismService.batchImportProduct(multipartFile);
            if (CollectionUtil.isNotEmpty(planDefs)) {
                return ActionResponse.getSuccessResp(planDefs);
            } else {
                return ActionResponse.getFaildResp("批量导入产品失败!");
            }
        } catch (Exception e) {
            log.error("批量导入产品内容异常：", e);
            return ActionResponse.getFaildResp(e.getMessage());
        }
    }

    @PostMapping(path = "/batchImpCustTmpDownload")
    @ApiOperation(value = "批量导入客群模板下载", notes = "智推棱镜：批量导入客群模板下载")
    public void batchImpCustTmpDownload(HttpServletResponse response) {
        intellPushPrismService.batchImpCustTmpDownload(response);
    }

    @ApiOperation(value = "批量导入客群", notes = "智推棱镜：批量导入客群")
    @PostMapping("/batchImportCust")
    public ActionResponse<List<BatchImportCustRespVO>> batchImportCust(@RequestParam("multipartFile") MultipartFile multipartFile) {
        if (ObjectUtil.isEmpty(multipartFile)) {
            return ActionResponse.getFaildResp("批量导入客群异常,文件为null");
        }
        try {
            String fileName = multipartFile.getOriginalFilename();
            // 校验文件格式
            if (!fileName.toLowerCase().endsWith(".xls") && !fileName.toLowerCase().endsWith(".xlsx")) {
                return ActionResponse.getFaildResp("选择文件的类型不正确，请重新选择xlsx、xls类型的文件!");
            }
            List<BatchImportCustRespVO> custgroupDefs = intellPushPrismService.batchImportCust(multipartFile);
            if (CollectionUtil.isNotEmpty(custgroupDefs)) {
                return ActionResponse.getSuccessResp(custgroupDefs);
            } else {
                return ActionResponse.getFaildResp("批量导入客群失败!");
            }
        } catch (Exception e) {
            log.error("批量导入客群内容异常：", e);
            return ActionResponse.getFaildResp(e.getMessage());
        }
    }

    @PostMapping(path = "/batchImpLabelTmpDownload")
    @ApiOperation(value = "批量导入标签模板下载", notes = "智推棱镜：批量导入标签模板下载")
    public void batchImpLabelTmpDownload(HttpServletResponse response) {
        intellPushPrismService.batchImpLabelTmpDownload(response);
    }

    @ApiOperation(value = "批量导入标签", notes = "智推棱镜：批量导入标签")
    @PostMapping("/batchImportLabel")
    public ActionResponse<List<ColumnSearchRespondDTO.ColumnList>> batchImportLabel(@RequestParam("multipartFile") MultipartFile multipartFile) {
        if (ObjectUtil.isEmpty(multipartFile)) {
            return ActionResponse.getFaildResp("批量导入标签异常,文件为null");
        }
        try {
            String fileName = multipartFile.getOriginalFilename();
            // 校验文件格式
            if (!fileName.toLowerCase().endsWith(".xls") && !fileName.toLowerCase().endsWith(".xlsx")) {
                return ActionResponse.getFaildResp("选择文件的类型不正确，请重新选择xlsx、xls类型的文件!");
            }
            List<ColumnSearchRespondDTO.ColumnList> labelList = intellPushPrismService.batchImportLabel(multipartFile);
            if (CollectionUtil.isNotEmpty(labelList)) {
                return ActionResponse.getSuccessResp(labelList);
            } else {
                return ActionResponse.getFaildResp("批量导入标签失败!");
            }
        } catch (Exception e) {
            log.error("批量导入标签内容异常：", e);
            return ActionResponse.getFaildResp(e.getMessage());
        }
    }

    @PostMapping(path = "/themeTemplateDownload")
    @ApiOperation(value = "主题模板下载", notes = "智推棱镜：主题模板下载")
    public void themeTemplateDownload(HttpServletResponse response) {
        intellPushPrismService.themeTemplateDownload(response);
    }

    @ApiOperation(value = "查询各类型下的模板", notes = "智推棱镜：查询各类型下的模板")
    @PostMapping("/queryTemplateByType")
    public ActionResponse<List<TemplateByTypeRespondDTO>> queryTemplateByType(@RequestBody McdPageQuery pageQuery) {
        List<TemplateByTypeRespondDTO> templateByTypeRespondDTOS = intellPushPrismService.queryTemplateByType(pageQuery);
        return ActionResponse.getSuccessResp(templateByTypeRespondDTOS);
    }

    @ApiOperation(value = "我的模板列表查询", notes = "智推棱镜：我的模板列表查询")
    @PostMapping("/queryTemplateList")
    public ActionResponse<IPage<McdPrismCampTemplate>> queryTemplateList(@RequestBody TemplateListParamVO paramVO) {
        IPage<McdPrismCampTemplate> page = intellPushPrismService.queryTemplateList(paramVO, UserUtil.getUser(request));
        return ActionResponse.getSuccessResp(page);
    }

    @ApiOperation(value = "批量删除主题下策略信息", notes = "批量删除主题下策略信息")
    @PostMapping("/batchDelCampseg")
    public ActionResponse<Boolean> batchDelCampseg(@RequestBody @Valid McdIdQuery req) {
        log.info("开始删除活动,参数id={}", req.getId());
        boolean batchDelCampseg = intellPushPrismService.batchDelCampseg(req.getId());
        if (batchDelCampseg) {
            return ActionResponse.getSuccessResp("批量删除主题下策略信息成功");
        } else {
            return ActionResponse.getFaildResp("批量删除主题下策略信息失败");
        }
    }

    @ApiOperation(value = "查询主题详情", notes = "根据主题id查询画布流程详细信息")
    @PostMapping("/getThemeDetailByThemeId")
    public ActionResponse<McdPrismThemeDetailRespVO> getThemeDetailByThemeId(@RequestBody QueryCampByThemeIdOrTepIdReqVO reqVO) {
        log.info("查询主题详情,参数id={}", JSONUtil.toJsonStr(reqVO));
        McdPrismThemeDetailRespVO baseInfo = intellPushPrismService.getThemeDetailByThemeId(reqVO);
        return ActionResponse.getSuccessResp(baseInfo);
    }

    @ApiOperation(value = "一键引用", notes = "我的模板、首页模板信息一键引用")
    @PostMapping("/oneClickRefMyTemplate")
    public ActionResponse<List<Map<String, Object>>> oneClickRefMyTemplate(@RequestBody @Valid McdIdQuery req) {
        log.info("我的模板、首页模板信息一键引用,参数id={}", req.getId());
        List<Map<String, Object>> resMap = intellPushPrismService.oneClickRefMyTemplate(req.getId());
        return ActionResponse.getSuccessResp(resMap);
    }

    @ApiOperation(value = "根据主题或者模版查询下面的活动", notes = "根据主题或者模版查询下面的活动")
    @PostMapping("/queryCampInfoByThemeIdOrTemplateId")
    public ActionResponse<List<Map<String, Object>>> queryCampInfoByThemeIdOrTemplateId(@RequestBody QueryCampByThemeIdOrTepIdReqVO reqVO) {
        log.info("根据主题或者模版查询下面的活动,参数id={}", JSONUtil.toJsonStr(reqVO));
        List<Map<String, Object>> resMap = intellPushPrismService.queryCampInfoByThemeIdOrTemplateId(reqVO);
        return ActionResponse.getSuccessResp(resMap);
    }

    @ApiOperation(value = "查询模板详情", notes = "根据模板id查询画布流程详细信息")
    @PostMapping("/getTemplateDetailByTemplateId")
    public ActionResponse<McdPrismThemeDetailRespVO> getTemplateDetailByTemplateId(@RequestBody QueryCampByThemeIdOrTepIdReqVO reqVO) {
        log.info("查询模板详情,参数id={}", JSONUtil.toJsonStr(reqVO));
        McdPrismThemeDetailRespVO baseInfo = intellPushPrismService.getTemplateDetailByTemplateId(reqVO);
        return ActionResponse.getSuccessResp(baseInfo);
    }

    @ApiOperation(value = "主题复制", notes = "主题复制")
    @PostMapping("copyTheme")
    public ActionResponse<String> copyTheme(@RequestBody @Valid CampsegRootIdQuery campsegRootId) {
        return ActionResponse.getSuccessResp((Object) intellPushPrismService.copyTheme(campsegRootId.getCampsegRootId(), null, UserUtil.getUser(request), "1"));
    }

}


