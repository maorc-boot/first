package com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.asiainfo.biapp.pec.common.jx.constant.RedisDicKey;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.CampStatus;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.plan.common.CampDefType;
import com.asiainfo.biapp.pec.plan.common.CampLogType;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.IChannelMaterialQueryDao;
import com.asiainfo.biapp.pec.plan.jx.camp.enums.CampStatusJx;
import com.asiainfo.biapp.pec.plan.jx.camp.model.JxMcdCampDef;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdFqcCycle;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdFqcRule;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.*;
import com.asiainfo.biapp.pec.plan.jx.camp.service.impl.CampsegAssemblerJx;
import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupDefJx;
import com.asiainfo.biapp.pec.plan.jx.custgroup.service.IMcdCustgroupDefJxService;
import com.asiainfo.biapp.pec.plan.jx.dna.constant.ConstantDNA;
import com.asiainfo.biapp.pec.plan.jx.dna.service.IDNACustomGroupService;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.constant.IntellPushPrismConstant;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.TemplateByTypeRespondDTO;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna.*;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity.*;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.*;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.*;
import com.asiainfo.biapp.pec.plan.model.*;
import com.asiainfo.biapp.pec.plan.service.*;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import com.asiainfo.biapp.pec.plan.vo.CampBusinessInfo;
import com.asiainfo.biapp.pec.plan.vo.CustgroupDetailVO;
import com.asiainfo.biapp.pec.plan.vo.req.CampChildrenScheme;
import com.asiainfo.biapp.pec.plan.vo.req.CampScheme;
import com.asiainfo.biapp.pec.plan.vo.req.ChannelInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.ONE_NUMBER;
import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.TWO_NUMBER;
import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.ZERO_NUMBER;

/**
 * description: 智推棱镜service层实现
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
@Service
@Slf4j
@RefreshScope
public class IntellPushPrismServiceImpl implements IIntellPushPrismService {

    @Autowired
    private IMcdPrismCampThemeService mcdPrismCampThemeService;

    @Autowired
    private IMcdPrismDimSceneService mcdPrismDimSceneService;

    @Autowired
    private IMcdPrismDimTemplateSceneService mcdPrismDimTemplateSceneService;

    @Autowired
    private IMcdPrismSceneCampService mcdPrismSceneCampService;

    @Autowired
    private IMcdPrismTemplateSceneCampService mcdPrismTemplateSceneCampService;

    @Autowired
    private IMcdCampDefService campDefService;

    @Resource
    private IMcdCampOperateLogService logService;

    @Resource
    private IMcdCustgroupDefService custgroupDefService;

    @Resource
    private IMcdCustgroupDefJxService custgroupDefJxService;

    @Resource
    private IMcdCampChannelExtService extService;

    @Resource
    private IMcdPlanDefService planDefService;

    @Resource
    private IMcdCampChannelListService campChannelListService;


    @Resource
    private IApproveServiceJx approveServiceJx;


    @Resource
    private IMcdCampsegServiceJx mcdCampsegServiceJx;

    @Resource
    ICampPreveiwJxService campPreveiwJxService;

    @Resource
    private IPlanExtInfoService planExtInfoService;

    @Resource
    private McdFqcCycleService fqcCycleService;

    @Resource
    private McdFqcRuleService fqcRuleService;

    @Resource
    private JxMcdCampDefService jxMcdCampDefService;

    @Autowired
    private IChannelMaterialQueryDao materialQueryDao;

    @Autowired
    private DnaColumnService dnaColumnService;

    @Resource
    private IMcdCampsegService mcdCampsegService;

    @Resource
    private TacticsManagerJxService tacticsManagerJxService;

    @Resource
    private IMcdPrismCampTemplateService mcdPrismCampTemplateService;

    @Autowired
    private IDNACustomGroupService idnaCustomGroupService;

    @Resource
    private IMcdCustgroupPushLogService mcdCustgroupPushLogService;

    @Autowired
    private IMcdPrismTemplateCampDefService mcdPrismTemplateCampDefService;

    @Autowired
    private IMcdPrismTemplateCampChannelListService mcdPrismTemplateCampChannelListService;

    @Autowired
    private IMcdPrismTemplateCampChannelExtService mcdPrismTemplateCampChannelExtService;

    private static final String[] PRODUCT_TEMPLATE_TITLE = {"产品编码"};
    private static final String[] IMPORT_PRODUCT_TEMPLATE = {"310095500538"};

    private static final String[] THEME_TEMPLATE_TITLE = {"主题名称", "创建时间", "创建人", "使用场景说明", "引用次数", "注：导入时先删除下面的示例数据"};
    private static final String[] IMPORT_THEME_TEMPLATE = {"看是否融合", "2022-05-06 09:23:13", "admin01", "可以应用与套餐融合类营销场", "1"};

    private static final String[] CUST_TEMPLATE_TITLE = {"客户群编码"};
    private static final String[] IMPORT_CUST_TEMPLATE = {"KHQ1w"};

    private static final String[] LABEL_TEMPLATE_TITLE = {"标签名称", "标签编码"};
    private static final String[] IMPORT_LABEL_TEMPLATE = {"网格地州", "3520"};

    /**
     * 手动执行AI推理任务路径
     */
    @Value("${intellpushprism.handleExec.url}")
    private String handleExecUrl;

    /**
     * 根据主题id、名称校验主题是否存在
     *
     * @param themeId   主题id
     * @param themeName 主题名称
     * @return boolean true-存在
     */
    @Override
    public boolean checkThemes(String themeId, String themeName) {
        final McdPrismCampTheme theme = new McdPrismCampTheme().setThemeName(themeName);
        LambdaQueryWrapper<McdPrismCampTheme> qry = Wrappers.lambdaQuery(theme);
        List<McdPrismCampTheme> list = mcdPrismCampThemeService.list(qry);
        if (!CollectionUtils.isEmpty(list)) {
            if (StringUtils.isNotBlank(themeId)) {
                return list.stream().anyMatch(campDef -> campDef.getThemeId().equals(themeId));
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 中间结果封装
     */
    private static class Result {
        public final String campsegRootId;
        public final List<McdCampDef> campDefs;
        public final McdCampDef campDef;
        public final CampBusinessInfo campBusinessInfo;

        public Result(String campsegRootId, List<McdCampDef> campDefs, McdCampDef campDef, CampBusinessInfo campBusinessInfo) {
            this.campsegRootId = campsegRootId;
            this.campDefs = campDefs;
            this.campDef = campDef;
            this.campBusinessInfo = campBusinessInfo;
        }
    }

    /**
     * 保存场景所有信息
     * 1. 请求dna接口获取裂变最小层级的客群id
     * 2. 主题基本信息保存
     * 3. 场景基本信息保存
     * 4. 产品、渠道等活动信息保存
     *
     * @param req  保存入参对象
     * @param user 当前登录用户对象
     * @return {@link String}
     */
    @Override
    @Transactional
    public List<String> saveScene(IntellPushPrismReqVO req, UserSimpleInfo user) {
        // 收集推理任务id
        Set<String> aiTaskIds = new HashSet<>();
        CampBusinessInfo campBusinessInfo = null;
        // 批量保存的所有活动id集合
        List<String> campsegIds = new ArrayList<>();
        // 批量保存场景与策略关系集合
        List<McdPrismSceneCamp> mcdPrismSceneCampList = new ArrayList<>();
        // 批量保存模板场景与策略关系集合
        List<McdPrismTemplateSceneCamp> mcdPrismTemplateSceneCampList = new ArrayList<>();
        // 裂变后的客群信息集合
        List<CustgroupDetailVO> fissonCustInfoList = new ArrayList<>();
        // 2. 主题基本信息保存
        McdPrismCampTheme theme = new McdPrismCampTheme();
        BeanUtil.copyProperties(req.getThemeBaseInfo(), theme, true);
        // 后端生成唯一主题id
        if (StrUtil.isEmpty(theme.getThemeId())) {
            theme.setThemeId(IdUtils.generateId());
        } else {
            log.info("删除各表旧数据开始");
            // 删除各表旧数据
            delOldData(req);
            log.info("删除各表旧数据结束");
        }
        if (ObjectUtil.isNotEmpty(user)) {
            theme.setCreateUser(user.getUserId());
        }
        theme.setCreateTime(new Date());
        mcdPrismCampThemeService.save(theme);
        // 7. 根据isCreateTemplate判断是否创建模板以及isOneClickRef判断是否一键引用
        McdPrismCampTemplate templateBaseInfo = new McdPrismCampTemplate();
        saveOrUpdatePrismTpl(req, theme, templateBaseInfo);
        // 3. 场景基本信息保存
        List<McdPrismDimScene> mcdPrismDimSceneList = new ArrayList<>();
        // 模板场景集合
        List<McdPrismDimTemplateScene> mcdPrismDimTemplateSceneList = new ArrayList<>();
        // 场景逻辑处理
        dealSceneLogic(req, user, mcdPrismSceneCampList, mcdPrismTemplateSceneCampList, fissonCustInfoList, theme, templateBaseInfo, mcdPrismDimSceneList, mcdPrismDimTemplateSceneList);
        log.info("saveScene-->最终客群id集合fissonCustInfoList={}", JSONUtil.toJsonStr(fissonCustInfoList));
        // 主题场景基本信息保存
        mcdPrismDimSceneService.saveBatch(mcdPrismDimSceneList);
        // templateBaseInfo对象的模板id不为空，说明需要保存模板信息
        if (StrUtil.isNotEmpty(templateBaseInfo.getTemplateId())) {
            // 模板场景基本信息保存
            mcdPrismDimTemplateSceneService.saveBatch(mcdPrismDimTemplateSceneList);
        }
        // 4. 产品、渠道等活动信息批量保存  正常情况req.getCampInfo().size()==customerList.size() 否则异常
        for (int i = 0; i < req.getCampInfo().size(); i++) {
            TacticsInfoJx tacticsInfoJx = req.getCampInfo().get(i);
            IntellPushPrismServiceImpl.Result result = saveCampseg(tacticsInfoJx, user, fissonCustInfoList.get(i), templateBaseInfo, req.getIsAI());
            // 5. 判断是否提交审批
            commitApprove(tacticsInfoJx, user, result.campsegRootId, result.campDefs);
            campsegIds.add(result.campDef.getCampsegId());
            aiTaskIds.add(tacticsInfoJx.getBaseCampInfo().getTaskIdByAI());
            campBusinessInfo = result.campBusinessInfo;
        }
        // 6. 批量保存场景与策略关系  遍历两个集合中较短的那个，以避免数组越界异常
        for (int i = 0; i < Math.min(campsegIds.size(), mcdPrismSceneCampList.size()); i++) {
            McdPrismSceneCamp camp = mcdPrismSceneCampList.get(i);
            String campsegId = campsegIds.get(i);
            // 主题场景与策略关系-活动rootId数据填充
            camp.setCampsegRootId(campsegId);
        }
        mcdPrismSceneCampService.saveBatch(mcdPrismSceneCampList);
        // 7. 批量保存模板场景与策略关系  遍历两个集合中较短的那个，以避免数组越界异常
        if (StrUtil.isNotEmpty(templateBaseInfo.getTemplateId())) {
            for (int i = 0; i < Math.min(campsegIds.size(), mcdPrismTemplateSceneCampList.size()); i++) {
                McdPrismTemplateSceneCamp templateSceneCamp = mcdPrismTemplateSceneCampList.get(i);
                String campsegId = campsegIds.get(i);
                // 模板场景与策略关系-活动rootId数据填充
                templateSceneCamp.setCampsegRootId(campsegId);
            }
            mcdPrismTemplateSceneCampService.saveBatch(mcdPrismTemplateSceneCampList);
        }
        // 8. 异步请求智慧大脑接口，手动执行推理任务以及更新推理任务结束时间(使用活动结束时间)以及执行周期字段值
        // 需要区分下是否AI流程
        if (IntellPushPrismConstant.IS_AI.equals(req.getIsAI())) {
            IntellPushPrismServiceImpl intellPushPrismService = SpringUtil.getBean(IntellPushPrismServiceImpl.class);
            intellPushPrismService.asyncDealAITaskLogic(aiTaskIds, req.getCampInfo().get(ZERO_NUMBER), campBusinessInfo);
        }
        return campsegIds;
    }

    /**
     * 场景逻辑处理
     *
     * @param req 场景蓝图信息保存对象
     * @param user 用户信息
     * @param mcdPrismSceneCampList  批量保存模板场景与策略关系集合
     * @param mcdPrismTemplateSceneCampList 批量保存模板场景与策略关系集合
     * @param fissonCustInfoList 裂变后的客群信息集合
     * @param theme 主题信息
     * @param templateBaseInfo 模板信息
     * @param mcdPrismDimSceneList 场景基本信息保存
     * @param mcdPrismDimTemplateSceneList 模板场景信息集合
     */
    private void dealSceneLogic(IntellPushPrismReqVO req, UserSimpleInfo user, List<McdPrismSceneCamp> mcdPrismSceneCampList, List<McdPrismTemplateSceneCamp> mcdPrismTemplateSceneCampList,
                                List<CustgroupDetailVO> fissonCustInfoList, McdPrismCampTheme theme, McdPrismCampTemplate templateBaseInfo, List<McdPrismDimScene> mcdPrismDimSceneList,
                                List<McdPrismDimTemplateScene> mcdPrismDimTemplateSceneList) {
        flag: for (List<SceneBaseInfo> sceneBaseInfos : req.getSceneBaseInfo()) {
                  for (SceneBaseInfo sceneBaseInfo : sceneBaseInfos) {
                      // 场景裂变类型：0-标签 1-客户群 2-客群&标签
                      Integer sceneFissionType = sceneBaseInfo.getSceneFissionType();
                      // log.info("saveScene-->sceneFissionType={}", sceneFissionType);
                      // 是否最后一级场景,1-是，0-否  决定是否有通过客群裂变
                      Integer leafSceneFlag = sceneBaseInfo.getLeafSceneFlag();
                      // log.info("saveScene-->leafSceneFlag={}", leafSceneFlag);
                      // 3.1 主题场景基本信息对象组装
                      McdPrismDimScene mcdPrismDimScene = new McdPrismDimScene();
                      BeanUtil.copyProperties(sceneBaseInfo, mcdPrismDimScene, true);
                      mcdPrismDimScene.setThemeId(theme.getThemeId());
                      mcdPrismDimScene.setCreateUser(user.getUserId());
                      mcdPrismDimSceneList.add(mcdPrismDimScene);
                      // templateBaseInfo对象的模板id不为空，说明需要保存模板信息
                      if (StrUtil.isNotEmpty(templateBaseInfo.getTemplateId())) {
                          // 3.2 模板场景基本信息对象组装
                          McdPrismDimTemplateScene mcdPrismDimTemplateScene = new McdPrismDimTemplateScene();
                          BeanUtil.copyProperties(sceneBaseInfo, mcdPrismDimTemplateScene, true);
                          mcdPrismDimTemplateScene.setTemplateId(templateBaseInfo.getTemplateId());
                          mcdPrismDimTemplateScene.setCreateUser(user.getUserId());
                          mcdPrismDimTemplateSceneList.add(mcdPrismDimTemplateScene);
                      }
                      if (IntellPushPrismConstant.LEAF_SCENE_FLAG.equals(leafSceneFlag)) {
                          // 主题场景与策略关系-最后一层级场景id数据填充
                          McdPrismSceneCamp mcdPrismSceneCamp = new McdPrismSceneCamp();
                          mcdPrismSceneCamp.setSceneId(sceneBaseInfo.getSceneId());
                          mcdPrismSceneCampList.add(mcdPrismSceneCamp);
                          // templateBaseInfo对象的模板id不为空，说明需要保存模板信息
                          if (StrUtil.isNotEmpty(templateBaseInfo.getTemplateId())) {
                              // 模板场景与策略关系-最后一层级场景id数据填充
                              McdPrismTemplateSceneCamp mcdPrismTemplateSceneCamp = new McdPrismTemplateSceneCamp();
                              mcdPrismTemplateSceneCamp.setSceneId(sceneBaseInfo.getSceneId());
                              mcdPrismTemplateSceneCampList.add(mcdPrismTemplateSceneCamp);
                          }

                          // 下面逻辑是获取最终的客群id集合
                          // a. 标签类型裂变
                          if (IntellPushPrismConstant.SCENE_FISSION_LABEL_TYPE.equals(sceneFissionType)) {
                              log.info("标签类型裂变");
                              // 标签类型裂变==>请求dna接口获取裂变客群id
                              custInfoTransfer(req, fissonCustInfoList);
                              // b. 客群类型裂变逻辑 || 客群&标签裂变逻辑（本质走的还是标签裂变，仅接口传参treeDetails对象的根节点columnNum字段传客群id==>此客群id是coc客群更新时同步调dna接口获取的客群id）
                          } else {
                              log.info("仅选择客群批量保存，没有选择标签进行裂变");
                              CustgroupDetailVO customerInfo = sceneBaseInfo.getCustomerInfo();
                              fissonCustInfoList.add(customerInfo);
                          }
                          // 只要走到这里，说明一条完整场景的数据已经处理完，即跳到锚点处，执行下一条场景数据
                          continue flag;
                      }
                  }
              }
    }

    /**
     * 1. 手动执行任务
     * 2. 更新任务结束时间
     * 3. 更新执行周期
     *
     * @param req req
     */
    @Async
    public void asyncDealAITaskLogic(Set<String> aiTaskIds, TacticsInfoJx req, CampBusinessInfo campBusinessInfo) {
        log.info("手动执行AI推理任务开始,aiTaskIds={}", JSONUtil.toJsonStr(aiTaskIds));
        try {
            String aiTaskIdStr = StringUtils.join(aiTaskIds.toArray(), StrUtil.COMMA);
            HandleAITaskExecReqVO reqVO = new HandleAITaskExecReqVO();
            // 活动结束时间
            Date endDate = req.getBaseCampInfo().getEndDate();
            // 此时McdCampChannelList对象中的updateCycle值已经根据客群源赋好值了
            Integer updateCycle = campBusinessInfo.getMcdCampChannelLists().get(ZERO_NUMBER).getUpdateCycle();
            reqVO.setId(aiTaskIdStr);
            if (ConstantDNA.CUSTGROUP_DAY_CYCLE.equals(updateCycle)) {
                reqVO.setDateType(ZERO_NUMBER);
            }
            if (ConstantDNA.CUSTGROUP_MONTH_CYCLE.equals(updateCycle)) {
                reqVO.setDateType(TWO_NUMBER);
            }
            reqVO.setTaskFailTime(DateUtil.format(endDate, "yyyy-MM-dd"));
            reqVO.setExecuteType(ONE_NUMBER);
            // 月周期  每月1号执行
            reqVO.setExecWeek("1");
            log.info("手动执行AI推理任务接口入参={}", JSONUtil.toJsonStr(reqVO));
            String resp = HttpRequest.post(handleExecUrl)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(JSONUtil.toJsonStr(reqVO)).execute().body();
            log.info("手动执行AI推理任务接口响应={}", JSONUtil.toJsonStr(resp));
        } catch (HttpException e) {
            log.error("手动执行AI推理任务异常：", e);
        }
        log.info("手动执行AI推理任务结束");
    }

    /**
     * dna接口请求获取的客群信息转换客群定义表对象使用
     *
     * @param req                请求入参
     * @param fissonCustInfoList dna接口请求返回
     */
    private void custInfoTransfer(IntellPushPrismReqVO req, List<CustgroupDetailVO> fissonCustInfoList) {
        // 1. 请求dna接口获取裂变最小层级的客群id(此处调用暂时同步操作，后续看接口相应时间，若接口相应较慢则考虑其他优化方案，如创建任务异步请求)
        List<LabelFissionGetCustIdRespDTO> customerList = getLabelFissionCustId(req);
        // 客群对象信息转换
        customerList.forEach(custInfo -> {
            CustgroupDetailVO vo = new CustgroupDetailVO();
            vo.setCustomGroupId(String.valueOf(custInfo.getCustomerId()));
            vo.setCustomGroupName(custInfo.getCustomerName());
            vo.setCustomNum(custInfo.getUserCount());
            fissonCustInfoList.add(vo);
        });
    }

    /**
     * 删除各表旧数据
     *
     * @param req 主题信息
     */
    private void delOldData(IntellPushPrismReqVO req) {
        ThemeBaseInfo theme = req.getThemeBaseInfo();
        // 1. 删除主题信息
        boolean remove = mcdPrismCampThemeService.remove(Wrappers.<McdPrismCampTheme>update().lambda().eq(McdPrismCampTheme::getThemeId, theme.getThemeId()));
        log.warn("删除主题表={}, themeId={}", remove, theme.getThemeId());
        // 2. 删除场景信息
        boolean remove3 = mcdPrismDimSceneService.remove(Wrappers.<McdPrismDimScene>update().lambda().eq(McdPrismDimScene::getThemeId, theme.getThemeId()));
        log.warn("删除场景表={}, themeId={}", remove3, theme.getThemeId());
        // 3. 删除场景、活动关系数据
        // 3.1 获取所有场景id
        List<String> scendIdsFinall = new ArrayList<>();
        for (List<SceneBaseInfo> sceneBaseInfos : req.getSceneBaseInfo()) {
            List<String> scendIds = sceneBaseInfos.stream().map(SceneBaseInfo::getSceneId).collect(Collectors.toList());
            scendIdsFinall.addAll(scendIds);
        }
        boolean remove4 = mcdPrismSceneCampService.remove(Wrappers.<McdPrismSceneCamp>update().lambda().in(McdPrismSceneCamp::getSceneId, scendIdsFinall));
        log.warn("删除场景、活动关系表={}, scendIdsFinall={}", remove4, JSONUtil.toJsonStr(scendIdsFinall));
    }

    /**
     * 请求dna接口获取裂变客群id
     *
     * @param req
     * @return {@link List}<{@link LabelFissionGetCustIdRespDTO}>
     */
    private List<LabelFissionGetCustIdRespDTO> getLabelFissionCustId(IntellPushPrismReqVO req) {
        List<LabelFissionGetCustIdRespDTO> customerList;
        try {
            // 1.1 组装请求入参
            LabelFissionGetCustIdReqDTO reqDTO = getLabelFissionGetCustIdReqParam(req);
            DNACustomActionResponse dnaActionResponse = dnaColumnService.getLabelFissionCustId(reqDTO);
            log.info("请求dna获取裂变最小层级的客群id接口返回={}", JSONUtil.toJsonStr(dnaActionResponse));
            if (DNAResponseStatus.SUCCESS.getCode().equals(dnaActionResponse.getCode())) {
                JSONArray jsonArray = JSONUtil.parseArray(JSONUtil.toJsonStr(dnaActionResponse.getData()));
                customerList = JSONUtil.toList(jsonArray, LabelFissionGetCustIdRespDTO.class);
            } else {
                log.warn("请求dna接口获取裂变客群id失败,未获取到裂变后客群id");
                throw new BaseException("未获取到裂变后客群编号");
            }
        } catch (Exception e) {
            log.error("请求dna接口获取裂变最小层级的客群id异常：", e);
            throw new BaseException("请求dna接口获取裂变客群编号异常");
        }
        return customerList;
    }

    /**
     * 根据isCreateTemplate判断是否创建模板以及isOneClickRef判断是否一键引用
     *
     * @param req              保存入参对象
     * @param theme            主题信息
     * @param templateBaseInfo 模板信息
     */
    private void saveOrUpdatePrismTpl(IntellPushPrismReqVO req, McdPrismCampTheme theme, McdPrismCampTemplate templateBaseInfo) {
        // 只需要保存模板
        if (IntellPushPrismConstant.IS_CREATE_TEMPLATE.equals(req.getIsCreateTemplate()) && !IntellPushPrismConstant.IS_ONE_CLICK_REF.equals(req.getIsOneClickRef())) {
            log.warn("新建或修改主题并保存模板...");
            BeanUtil.copyProperties(theme, templateBaseInfo);
            templateBaseInfo.setTemplateId(IdUtils.generateId());
            templateBaseInfo.setTemplateName(theme.getThemeName());
            templateBaseInfo.setTemplateContent(theme.getThemeContent());
            mcdPrismCampTemplateService.save(templateBaseInfo);
            // 判断是否一键引用模板过来创建的 0-不是 1-是  仅一键引用过来,不保存模板
        } else if (IntellPushPrismConstant.IS_ONE_CLICK_REF.equals(req.getIsOneClickRef()) && !IntellPushPrismConstant.IS_CREATE_TEMPLATE.equals(req.getIsCreateTemplate())) {
            log.warn("一键引用过来...");
            // 根据主题id即模板id查询当前引用次数REF_COUNT
            McdPrismCampTemplate mcdPrismCampTemplate = mcdPrismCampTemplateService.getOne(Wrappers.<McdPrismCampTemplate>query().lambda()
                    .eq(McdPrismCampTemplate::getTemplateId, req.getTemplateId()).eq(McdPrismCampTemplate::getStatus, 1));
            // 引用次数累加1
            mcdPrismCampTemplateService.update(null, Wrappers.<McdPrismCampTemplate>update().lambda().set(McdPrismCampTemplate::getRefCount, mcdPrismCampTemplate.getRefCount() + 1)
                    .eq(McdPrismCampTemplate::getTemplateId, req.getTemplateId()));
        } else if (IntellPushPrismConstant.IS_ONE_CLICK_REF.equals(req.getIsOneClickRef()) && IntellPushPrismConstant.IS_CREATE_TEMPLATE.equals(req.getIsCreateTemplate())) {
            log.warn("一键引用过来,也要保存模板...");
            // 一键引用过来 也要保存模板
            McdPrismCampTemplate mcdPrismCampTemplate = mcdPrismCampTemplateService.getOne(Wrappers.<McdPrismCampTemplate>query().lambda()
                    .eq(McdPrismCampTemplate::getTemplateId, req.getTemplateId()).eq(McdPrismCampTemplate::getStatus, 1));
            if (ObjectUtil.isNotEmpty(mcdPrismCampTemplate)) {
                log.warn("模板id={}不为空，修改引用的模板次数累加1...", theme.getThemeId());
                mcdPrismCampTemplateService.update(null, Wrappers.<McdPrismCampTemplate>update().lambda()
                        .set(McdPrismCampTemplate::getRefCount, mcdPrismCampTemplate.getRefCount() + 1)
                        .eq(McdPrismCampTemplate::getTemplateId, req.getTemplateId()));
            }
            log.warn("模板为空，一键引用保存为新的模板...");
            BeanUtil.copyProperties(theme, templateBaseInfo);
            templateBaseInfo.setTemplateId(IdUtils.generateId());
            templateBaseInfo.setTemplateName(theme.getThemeName());
            templateBaseInfo.setTemplateContent(theme.getThemeContent());
            mcdPrismCampTemplateService.save(templateBaseInfo);
        }
    }

    /**
     * 组装请求入参
     *
     * @param req
     * @return {@link LabelFissionGetCustIdReqDTO}
     */
    private LabelFissionGetCustIdReqDTO getLabelFissionGetCustIdReqParam(IntellPushPrismReqVO req) {
        LabelFissionGetCustIdReqDTO reqDTO = new LabelFissionGetCustIdReqDTO();
        List<LabelFissionGetCustIdReqDTO.CustomerCreateVos> customerCreateVos = new ArrayList<>();
        // 取出场景数据
        List<List<SceneBaseInfo>> sceneBaseInfoList = req.getSceneBaseInfo();
        try {
            for (List<SceneBaseInfo> sceneVoList : sceneBaseInfoList) {
                LabelFissionGetCustIdReqDTO.CustomerCreateVos createVo = new LabelFissionGetCustIdReqDTO.CustomerCreateVos();
                // (最后一层级)最小层级的场景集合
                List<SceneBaseInfo> collect2 = sceneVoList.stream()
                        .filter(a -> IntellPushPrismConstant.LEAF_SCENE_FLAG.equals(a.getLeafSceneFlag()) && IntellPushPrismConstant.SCENE_FISSION_LABEL_TYPE.equals(a.getSceneFissionType()))
                        .collect(Collectors.toList());
                // collect2为空，说明有一条分支场景是coc客群 不需要裂变直接保存即可，所以不需要请求dna接口
                if (CollectionUtil.isNotEmpty(collect2)) {
                    // 循环最后一层级集合  为了确保基本信息的填充
                    for (SceneBaseInfo sceneBaseInfo : collect2) {
                        List<LabelFissionGetCustIdReqDTO.CustomerCreateVos.TreeDetails> treeDetails = new ArrayList<>();
                        // 填充基本信息
                        createVo.setMergeOrRejectType(null);
                        createVo.setMergeOrRejectCustomerId(null);
                        // 最后一层级场景名称 拼接时间戳保证唯一
                        createVo.setTreeName(sceneBaseInfo.getSceneName() + StrUtil.UNDERLINE + IdUtils.generateId());
                        createVo.setBusinessType("1");
                        createVo.setRemark(sceneBaseInfo.getSceneName() + StrUtil.UNDERLINE + IdUtils.generateId());
                        createVo.setStartTime(DateUtil.format(req.getCampInfo().get(0).getBaseCampInfo().getStartDate(), "yyyy-MM-dd"));
                        createVo.setEndTime(DateUtil.format(req.getCampInfo().get(0).getBaseCampInfo().getEndDate(), "yyyy-MM-dd"));
                        createVo.setExecuteType("1");
                        createVo.setIsPushIop("1");
                        List<LabelFissionGetCustIdReqDTO.CustomerCreateVos.TreeDetails> treeDetails2 = JSONUtil.toList(sceneBaseInfo.getSceneCondition(), LabelFissionGetCustIdReqDTO.CustomerCreateVos.TreeDetails.class);
                        treeDetails.addAll(treeDetails2);
                        createVo.setTreeDetails(treeDetails);
                    }
                    customerCreateVos.add(createVo);
                    reqDTO.setCustomerCreateVos(customerCreateVos);
                }
            }
        } catch (Exception e) {
            log.error("请求dna获取裂变最小层级的客群id接口入参异常：", e);
        }
        return reqDTO;
    }

    /**
     * 保存活动基本信息
     *
     * @param req
     * @param user
     * @return
     */
    @Transactional
    public IntellPushPrismServiceImpl.Result saveCampseg(TacticsInfoJx req, UserSimpleInfo user, CustgroupDetailVO labelFissionGetCustIdRespDTO, McdPrismCampTemplate templateBaseInfo, Integer isAI) {
        log.info("tacticsCreateJx SaveAndUpdate start...");
        String campsegRootId = req.getBaseCampInfo().getCampsegId();
        //组装保存实体
        final CampBusinessInfo campBusinessInfo = CampsegAssemblerJx.convertToInfo(req, user, campsegRootId);
        //1.保存基本信息(orupdate)
        List<McdCampDef> campDefs = campBusinessInfo.getMcdCampDefs();
        //这是根活动
        McdCampDef campDef = campDefs.get(ZERO_NUMBER);
        Assert.isTrue(campDef.getEndDate().after(DateUtil.date().toJdkDate()), "请检查策略结束时间");
        //2.保存渠道信息(子活动ordeleteandinsert)
        final List<McdCampChannelList> campChannelLists = campBusinessInfo.getMcdCampChannelLists();
        // 2.1 回填客群id
        for (McdCampChannelList mcdCampChannelList : campChannelLists) {
            if (ObjectUtil.isNotEmpty(labelFissionGetCustIdRespDTO)) {
                // 场景与策略关系-活动rootId数据填充
                mcdCampChannelList.setCustgroupId(String.valueOf(labelFissionGetCustIdRespDTO.getCustomGroupId()));
                mcdCampChannelList.setPCustgroupId(String.valueOf(labelFissionGetCustIdRespDTO.getCustomGroupId()));
            } else {
                log.warn("customerList集合为空,排查请求dna接口逻辑！");
                break;
            }
        }
        for (CampScheme campScheme : req.getCampSchemes()) {
            List<CustgroupDetailVO> customer = campScheme.getCustomer();
            for (CustgroupDetailVO custgroupDetailVO : customer) {
                if (ObjectUtil.isNotEmpty(labelFissionGetCustIdRespDTO)) {
                    custgroupDetailVO.setCustomGroupId(String.valueOf(labelFissionGetCustIdRespDTO.getCustomGroupId()))
                            .setCustomGroupName(labelFissionGetCustIdRespDTO.getCustomGroupName())
                            .setCustomNum(labelFissionGetCustIdRespDTO.getCustomNum())
                            .setActualCustomNum(labelFissionGetCustIdRespDTO.getCustomNum())
                            .setCreateUserId(user.getUserId());
                } else {
                    log.warn("customerList集合为空,排查请求dna接口逻辑！");
                    break;
                }
            }
        }
        //3.保存渠道扩展信息(子活动ordeleteandinsert)
        final List<McdCampChannelExt> campChannelExts = campBusinessInfo.getCampChannelExts();

        boolean isNew = true;
        if (StringUtils.isBlank(campsegRootId)) {
            campsegRootId = campDef.getCampsegId();
        } else {
            isNew = false;
            for (McdCampDef def : campDefs) {
                def.setCampsegStatId(CampStatus.DRAFT.getId());
            }
            //channelList老数据
            final List<McdCampChannelList> oldCampChannelLists = campChannelListService.listMcdCampChannelListByCampsegRootId(campsegRootId);
            //删除之前的关系
            Set<String> defIds = new HashSet<>();
            oldCampChannelLists.forEach(campChannelList -> {
                defIds.add(campChannelList.getCampsegRootId());
                defIds.add(campChannelList.getCampsegPid());
                defIds.add(campChannelList.getCampsegId());
            });
            final boolean campFlag = campDefService.removeByIds(defIds);
            log.info("清理策略定义，{}", campFlag);
            final boolean listFlag = campChannelListService.removeByIds(defIds);
            log.info("清理策略客户群产品渠道运营位关联关系，{}", listFlag);
            final boolean extFlag = extService.removeByIds(defIds);
            log.info("清理运营位扩展属性，{}", extFlag);
        }

        //验证是否选内容；选了需要添加到产品库
        mcdCampsegServiceJx.saveDigitalContent2Plan(campChannelLists.stream().map(McdCampChannelList::getPlanId).collect(Collectors.toSet()));
        //分箱和波次客户群加入客群表
        final List<McdCustgroupDef> extCustGroup = campBusinessInfo.getExtCustGroup();
        if (!CollectionUtils.isEmpty(extCustGroup)) {
            custgroupDefService.saveOrUpdateBatch(extCustGroup);
        }
        // 设置普通流程创建还是智推棱镜创建COMMON_OR_PRISM_CREATE字段值 0-普通 1-智推棱镜创建
        // todo 这里后续可优化 将普通活动保存和智推棱镜保存逻辑分开或者加字段区分 CampsegAssemblerJx里面转换保存McdCampDef对象信息时即可保存CommonOrPrismCreate字段值
        campDefs.forEach(camp -> {
            if (IntellPushPrismConstant.IS_AI.equals(isAI)) {
                // 智推棱镜-AI场景创建 = 1
                camp.setCommonOrPrismCreate(ONE_NUMBER);
            } else {
                // 智推棱镜-裂变场景创建 = 2
                camp.setCommonOrPrismCreate(TWO_NUMBER);
            }
        });
        campDefService.saveBatch(campDefs);
        jxExtProperty(req, campDefs);
        campChannelListService.saveBatch(campChannelLists);
        extService.saveBatch(campChannelExts);

        // 我的模板相关活动信息保存--如果慢了  可以异步
        if (StrUtil.isNotEmpty(templateBaseInfo.getTemplateId())) {
            long start = System.currentTimeMillis();
            log.info("我的模板相关活动信息保存开始");
            List<McdPrismTemplateCampDef> mcdPrismTemplateCampDefs = BeanUtil.copyToList(campDefs, McdPrismTemplateCampDef.class);
            List<McdPrismTemplateCampChannelList> mcdPrismTemplateCampChannelLists = BeanUtil.copyToList(campChannelLists, McdPrismTemplateCampChannelList.class);
            List<McdPrismTemplateCampChannelExt> mcdPrismTemplateCampChannelExts = BeanUtil.copyToList(campChannelExts, McdPrismTemplateCampChannelExt.class);
            mcdPrismTemplateCampDefService.saveBatch(mcdPrismTemplateCampDefs);
            mcdPrismTemplateCampChannelListService.saveBatch(mcdPrismTemplateCampChannelLists);
            mcdPrismTemplateCampChannelExtService.saveBatch(mcdPrismTemplateCampChannelExts);
            log.info("我的模板相关活动信息保存结束，耗时={}秒", (System.currentTimeMillis() - start) / 1000);
        }

        // 保存活动级别的频次信息到频次规则表 mcd_fqc_rule
        saveFqc(campChannelLists);
        // 保存产品扩展信息：融合产品，同系列产品，互斥产品
        planExtInfoService.savePlanExtInfo(req.getPlanExtInfoList(), campDef);
        // 生成预演数据
        campPreveiwJxService.savePreview(campDef, campChannelLists);
        // 异步处理选择的客群为dna源时的逻辑    智推棱镜AI流程的话 不需要保存客群信息 智慧大脑侧会调iop webservice接口保存客群信息 所以AI流程全部按照coc源处理（AI流程不需要走下面逻辑）
        if (!IntellPushPrismConstant.IS_AI.equals(isAI) && ConstantDNA.CUSTGROUP_SOURCE.equals(req.getBaseCampInfo().getCustgroupSource())) {
            // 1. @Async注解是通过aop代理实现的，调用自己类的异步方法时，就会绕过aop代理从而导致其异步失效
            // 2. 所以需要从spring上下文中取得代理对象，继而调用其异步方法(或者单独起个类去写异步逻辑然后把异步类注入进来调用即可)
            IntellPushPrismServiceImpl intellPushPrismService = SpringUtil.getBean(IntellPushPrismServiceImpl.class);
            intellPushPrismService.asyncDelDnaCustgroupLogic(req);
        }

        if (isNew) {
            logService.markSuccLog(campsegRootId, CampLogType.CAMP_CREATE, null, user);
        } else {
            logService.markSuccLog(campsegRootId, CampLogType.CAMP_MOD, null, user);
        }
        log.info("SaveAndUpdate campseg success campsegId={}", campsegRootId);
        mcdCampsegServiceJx.updateCEPEventId(campsegRootId, true, CampStatus.DRAFT);
        return new Result(campsegRootId, campDefs, campDef, campBusinessInfo);
    }

    /**
     * 异步处理选择dna标签裂变时：
     * 1. 判断客群是否存在mcd_custgroup_def 存在说明文件已转换，此客群不是第一次使用，不需要走后续逻辑
     * 2. 调用dna接口1.4 生成客群清单文件
     * 3. 下载清单文件到本地(plan-svc服务本地)
     * 4. 将获取到的清单文件按照coc文件格式命名
     * 5. 按照coc格式生成校验文件
     * 6. 上传清单文件&校验文件到coc清单文件所在目录
     * 7. mcd_custgroup_def保存dna客群信息
     * 8. mcd_custgroup_push_log保存日志信息
     *
     * @param req 保存入参对象
     */
    @Async
    public void asyncDelDnaCustgroupLogic(TacticsInfoJx req) {
        Integer dnaUpdateCycle = req.getBaseCampInfo().getDnaUpdateCycle();
        long starttime = System.currentTimeMillis();
        log.info("thread-name={},异步处理选择的客群为dna源开始，周期={}，starttime={}", Thread.currentThread().getName(), dnaUpdateCycle, starttime);
        try {
            List<CampScheme> campSchemes = req.getCampSchemes();
            for (CampScheme campScheme : campSchemes) {
                List<com.asiainfo.biapp.pec.plan.vo.CustgroupDetailVO> customer = campScheme.getCustomer();
                for (com.asiainfo.biapp.pec.plan.vo.CustgroupDetailVO custgroupDetailVO : customer) {
                    // 1. 判断客群是否存在mcd_custgroup_def且数据日期是最新(当天时间) 存在说明文件已转换，此客群不是第一次使用 不需要走后续2.3...等流程
                    LambdaQueryWrapper<McdCustgroupDef> queryWrapper = new LambdaQueryWrapper<>();
                    if (ConstantDNA.CUSTGROUP_DAY_CYCLE.equals(dnaUpdateCycle)) { // 日周期
                        queryWrapper.eq(McdCustgroupDef::getCustomGroupId, custgroupDetailVO.getCustomGroupId())
                                .eq(McdCustgroupDef::getDataDate, Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd")));
                    } else { // 月周期
                        queryWrapper.eq(McdCustgroupDef::getCustomGroupId, custgroupDetailVO.getCustomGroupId())
                                .eq(McdCustgroupDef::getDataDate, Integer.valueOf(DateUtil.format(new Date(), "yyyyMM")));
                    }
                    McdCustgroupDef byId = custgroupDefService.getOne(queryWrapper);
                    if (ObjectUtil.isNotEmpty(byId)) {
                        log.info("客群定义表已存在该客群={}", custgroupDetailVO.getCustomGroupId());
                        // 1.1 使用该客群的活动修改时可能会修改执行周期 所以客群存在时，需要实时更新该值
                        if (!byId.getUpdateCycle().equals(dnaUpdateCycle)) {
                            log.warn("客群={}的执行周期值={}与表中={}不一致，需更新！", custgroupDetailVO.getCustomGroupId(), dnaUpdateCycle, byId.getUpdateCycle());
                            custgroupDetailVO.setUpdateCycle(dnaUpdateCycle);
                            custgroupDefService.updateById(custgroupDetailVO);
                        }
                        continue;
                    }
                    // 2. 调用dna接口1.4 生成客群清单文件
                    // 3.下载清单到本地
                    Map<String, String> map = idnaCustomGroupService.dowloadCustFile(custgroupDetailVO.getCustomGroupId());
                    if (ObjectUtil.isEmpty(map)) {
                        log.warn("客群={}调用dna计算接口获取清单文件为空！", custgroupDetailVO.getCustomGroupId());
                        // 获取清单文件为空时，保存客群基本信息入定义表
                        McdCustgroupDef mcdCustgroupDef = new McdCustgroupDef();
                        BeanUtil.copyProperties(custgroupDetailVO, mcdCustgroupDef);
                        mcdCustgroupDef.setUpdateCycle(dnaUpdateCycle);
                        if (ConstantDNA.CUSTGROUP_DAY_CYCLE.equals(dnaUpdateCycle)) {
                            mcdCustgroupDef.setDataDate(Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd"))); // 和coc客群(T-1)区别，数据日期设置为T当天时间
                        } else {
                            mcdCustgroupDef.setDataDate(Integer.valueOf(DateUtil.format(new Date(), "yyyyMM"))); // 月周期
                        }
                        mcdCustgroupDef.setCustomSourceId(ConstantDNA.CUSTOM_SOURCE_DNA); // 数据来源1:coc 2:dna 3:多波次
                        // 客户群状态0：无效；1：有效；2：删除；3：提取处理中；4：提取失败；9：客户群导入失败；10：入库时异常
                        mcdCustgroupDef.setCustomStatusId(1);
                        custgroupDefService.saveOrUpdate(mcdCustgroupDef);
                        continue;
                    }
                    // 4. 将获取到的清单文件按照coc文件格式命名  MCD_GROUP_客群id_数据日期.txt  MCD_GROUP_客群id_数据日期.CHK
                    // 4.1 获取清单文件名
                    String formatFileName = String.format(ConstantDNA.DNA_2_COC_CUSTOMGROUP_FILENAME, custgroupDetailVO.getCustomGroupId(), DateUtil.format(new Date(), "yyyyMMdd"));
                    String prefix = FileUtil.getPrefix(formatFileName);
                    // 5. 按照coc格式生成校验文件
                    File chkFile = FileUtil.newFile(map.get("localPath") + "/" + prefix + ".CHK");
                    File dnaCustFile = FileUtil.newFile(map.get("localPath") + "/" + map.get("fileName"));
                    File newCustFile = FileUtil.rename(dnaCustFile, formatFileName, true);
                    if (!chkFile.exists()) {
                        chkFile.createNewFile();
                    }
                    // 5.1 写校验文件 文件名,文件大小,文件行数
                    FileUtil.writeString(formatFileName + StrUtil.COMMA + newCustFile.length() + StrUtil.COMMA + map.get("count"), chkFile, map.get("encoding"));
                    // 6. 上传清单文件&校验文件到coc清单文件所在目录
                    uploadCustFile(chkFile, dnaCustFile, newCustFile, map);
                    // 7. mcd_custgroup_def保存dna客群信息
                    McdCustgroupDef mcdCustgroupDef = new McdCustgroupDef();
                    BeanUtil.copyProperties(custgroupDetailVO, mcdCustgroupDef);
                    mcdCustgroupDef.setUpdateCycle(dnaUpdateCycle);
                    mcdCustgroupDef.setFileName(formatFileName);
                    if (ConstantDNA.CUSTGROUP_DAY_CYCLE.equals(dnaUpdateCycle)) {
                        mcdCustgroupDef.setDataDate(Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd"))); // 和coc客群(T-1)区别，数据日期设置为T当天时间
                    } else {
                        mcdCustgroupDef.setDataDate(Integer.valueOf(DateUtil.format(new Date(), "yyyyMM"))); // 月周期
                    }
                    mcdCustgroupDef.setCustomSourceId(ConstantDNA.CUSTOM_SOURCE_DNA); // 数据来源1:coc 2:dna 3:多波次
                    // 客户群状态0：无效；1：有效；2：删除；3：提取处理中；4：提取失败；9：客户群导入失败；10：入库时异常
                    mcdCustgroupDef.setCustomStatusId(1);
                    custgroupDefService.saveOrUpdate(mcdCustgroupDef);
                    // 8. mcd_custgroup_push_log保存日志信息
                    McdCustgroupPushLog log = BeanUtil.toBean(mcdCustgroupDef, McdCustgroupPushLog.class);
                    mcdCustgroupPushLogService.save(log);
                }
            }
            log.info("thread-name={},异步处理选择的客群为dna源结束，endtime={},耗时={}秒", Thread.currentThread().getName(), System.currentTimeMillis(), (System.currentTimeMillis() - starttime) / 1000);
        } catch (Exception e) {
            log.error("asyncDelDnaCustgroupLogic-->异步处理选择的客群为dna源时异常：", e);
        }
    }

    /**
     * 上传清单文件&校验文件到coc清单文件所在目录
     *
     * @param chkFile     校验文件
     * @param dnaCustFile 更名后的客群文件
     * @param newCustFile dna接口获取到的客群文件
     * @param map         调用dna接口返回
     */
    private void uploadCustFile(File chkFile, File dnaCustFile, File newCustFile, Map<String, String> map) {
        SftpUtils sftpUtils = new SftpUtils();
        // 1. coc客群清单存放sftp配置
        String custFileFtpUsername = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_USER);
        String custFileFtpPassword = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_PASSWD);
        String custFileHost = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_HOST);
        String custFileServerPort = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_PORT);
        String custFileServerPath = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_SERVER_PATH);
        // 2. 连接sftp
        ChannelSftp channelSftp = sftpUtils.connect(custFileHost, Integer.parseInt(custFileServerPort), custFileFtpUsername, custFileFtpPassword);
        if (FileUtil.isNotEmpty(chkFile)) {
            // 3. 先上传校验文件，校验文件上传成功后，再上传清单文件
            boolean uploadCustChkFile = sftpUtils.upload(custFileServerPath, chkFile.getName(), map.get("localPath"), channelSftp);
            if (uploadCustChkFile) {
                log.info("校验文件={}上传sftp成功", chkFile.getName());
                if (FileUtil.isNotEmpty(newCustFile)) {
                    // 4. 上传清单文件
                    boolean uploadCustFile = sftpUtils.upload(custFileServerPath, newCustFile.getName(), map.get("localPath"), channelSftp);
                    if (uploadCustFile) {
                        log.info("源文件={}，更名后文件={}上传sftp成功", dnaCustFile.getName(), newCustFile.getName());
                    }
                }
            }
        }
    }

    /**
     * 提交审批
     *
     * @param req
     * @param user
     * @param campsegRootId
     * @param campDefs
     */
    private void commitApprove(TacticsInfoJx req, UserSimpleInfo user, String campsegRootId, List<McdCampDef> campDefs) {
        if (Constant.SpecialNumber.ONE_STRING.equals(String.valueOf(req.getIsSubmit()))) {
            // 广点通968渠道审批前校验--需校验此需要保存的活动引用的素材是否有正在被引用且活动状态为草稿状态
            chn968ApprovePreChk(req);
            req.getApproveUserId().setBusinessId(campsegRootId);
            final ActionResponse<Object> submit = approveServiceJx.submit(req.getApproveUserId());
            log.info("提交审批结果->{}", new JSONObject(submit));
            if (ResponseStatus.SUCCESS.equals(submit.getStatus())) {
                log.info("submit approval success, flowId->{}", submit.getData());
                for (McdCampDef def : campDefs) {
                    def.setApproveFlowId(submit.getData().toString());
                    def.setCampsegStatId(CampStatus.APPROVING.getId());
                }
                campDefService.updateBatchById(campDefs);
                final LambdaUpdateWrapper<McdCampChannelList> update = Wrappers.lambdaUpdate();
                update.set(McdCampChannelList::getStatus, CampStatus.APPROVING.getId());
                update.eq(McdCampChannelList::getCampsegRootId, campsegRootId)
                        .eq(McdCampChannelList::getCampClass, ONE_NUMBER);
                campChannelListService.update(update);

                logService.markSuccLog(campsegRootId, CampLogType.CAMP_APPR, null, user);
            } else {
                throw new BaseException("策略提交审批失败");
            }
        }
    }

    /**
     * 广点通968渠道审批前校验--需校验此需要保存的活动引用的素材是否有正在被引用且活动状态为草稿状态
     *
     * @param req req
     */
    private void chn968ApprovePreChk(TacticsInfoJx req) {
        List<CampScheme> campSchemes = req.getCampSchemes();
        for (CampScheme campScheme : campSchemes) {
            List<ChannelInfo> channels = campScheme.getChannels();
            for (ChannelInfo channelInfo : channels) {
                String channelId = channelInfo.getChannelId();
                if (StrUtil.equals("968", channelId)) {
                    String materialId = channelInfo.getChannelConf().getChannelExtConf().getColumnExt2(); // 素材id
                    // 968渠道判断该素材是否有非草稿、预演完成状态的活动使用
                    Map<String, Object> map = materialQueryDao.chkHasCampUsedMaterial(materialId);
                    // 若是审批退回状态的话  需要 特殊 判断是iop的审批流驳回还是广点通素材审核驳回
                    // 1.iop审批流驳回 素材可以重复使用
                    // 2.广点通素材审核驳回 素材不能重复使用
                    if (ObjectUtil.isNotNull(map.get("status"))) {
                        // 判断MATERIAL_AUDIT_FALLBACK 广点通素材审核反馈表数据是否有且状态是驳回
                        Map<String, Object> fallbackStat = materialQueryDao.chkMaterialFallbackStat(materialId);
                        // 1. 素材审核结果还未保存此表MATERIAL_AUDIT_FALLBACK
                        if (ObjectUtil.isNull(fallbackStat.get("status")) && Integer.parseInt(String.valueOf(fallbackStat.get("count"))) == 0) {
                            log.warn("素材={}还未提交到广点通审核", map.get("materialId"));
                            continue;
                        } else {
                            // 只要素材结果反馈，通过或这个驳回，素材均不能重复使用
                            log.warn("素材={}已被广点通审核", map.get("materialId"));
                            throw new BaseException("当前素材ID已使用，请更换素材！");
                        }
                    }
                    if (Integer.parseInt(String.valueOf(map.get("count"))) >= 1) {
                        log.warn("已有活动={}使用了该素材={}", map.get("campsegId"), map.get("materialId"));
                        throw new BaseException("当前素材ID已使用，请更换素材！");
                    }
                }
            }
        }
    }

    /**
     * 江西camp_camp_def 表一些个性化字段保存
     *
     * @param req
     * @param campDefs
     */
    private void jxExtProperty(TacticsInfoJx req, List<McdCampDef> campDefs) {
        String sensitiveCustIds = req.getBaseCampInfo().getSensitiveCustIds();
        // 渠道级敏感客户群ID
        if (StrUtil.isEmpty(sensitiveCustIds)) {
            return;
        }
        List<String> list = campDefs.stream().map(McdCampDef::getCampsegId).collect(Collectors.toList());
        LambdaUpdateWrapper<JxMcdCampDef> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(JxMcdCampDef::getSensitiveCustIds, sensitiveCustIds)
                .in(JxMcdCampDef::getCampsegId, list);
        jxMcdCampDefService.update(wrapper);
    }

    /**
     * 保存活动级频次信息
     *
     * @param campChannelLists
     */
    private void saveFqc(List<McdCampChannelList> campChannelLists) {
        if (CollectionUtil.isEmpty(campChannelLists)) {
            log.warn("渠道列表为空");
            return;
        }
        log.info("保存频次开始....");
        List<McdFqcRule> fqcRules = new ArrayList<>();
        try {
            for (McdCampChannelList campChannelList : campChannelLists) {
                String freQuency = campChannelList.getFrequency();
                if (StrUtil.isEmpty(freQuency)) {
                    continue;
                }
                // 频次格式为: 周期_频次
                String[] fqcItems = freQuency.split("_");
                if (fqcItems.length != 2) {
                    log.error("频次规则格式异常:{},campsegId:{}", freQuency, campChannelList.getCampsegId());
                    continue;
                }
                QueryWrapper<McdFqcCycle> cycleQueryWrapper = new QueryWrapper<>();
                cycleQueryWrapper.lambda().eq(McdFqcCycle::getCycle, fqcItems[0]);
                List<McdFqcCycle> fqcCycles = fqcCycleService.list(cycleQueryWrapper);
                if (CollectionUtil.isEmpty(fqcCycles)) {
                    log.error("频次周期不存在:{},campsegId:{}", fqcItems[0], campChannelList.getCampsegId());
                    continue;
                }
                String cycleId = fqcCycles.get(0).getCycleId();
                McdFqcRule mcdFqcRule = new McdFqcRule();
                mcdFqcRule.setCampsegId(campChannelList.getCampsegId());
                mcdFqcRule.setChannelId(campChannelList.getChannelId());
                mcdFqcRule.setFrequency(Integer.valueOf(fqcItems[1]));
                mcdFqcRule.setCycleId(cycleId);
                mcdFqcRule.setRuleType("3");
                fqcRules.add(mcdFqcRule);
            }
            if (CollectionUtil.isEmpty(fqcRules)) {
                return;
            }
            // 保存新数据
            fqcRuleService.saveBatch(fqcRules);
            log.info("保存频次完成....");
        } catch (Exception e) {
            log.error("保存频次数据异常:{}", campChannelLists.get(0).getCampsegRootId(), e);
        }
    }

    /**
     * 验证活动结束时间
     *
     * @param req 保存入参对象
     */
    @Override
    public void checkCampEndDate(TacticsInfoJx req) {
        final CampBaseInfoVO baseCampInfo = req.getBaseCampInfo();
        if (null != baseCampInfo.getEndDate()) {
            final LocalDateTime delayLocalDate = baseCampInfo.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            Assert.isTrue(LocalDateTime.now().isBefore(delayLocalDate), "执行结束时间应大于当前时间！");
            // 活动管理页面对执行中的活动进行延期操作时，如果延期时间大于客户群失效时间，则提示调用coc客户群延期接口
            final List<CampChildrenScheme> childrenSchemes = req.getChildrenSchemes();
            for (CampChildrenScheme campChildrenScheme : childrenSchemes) {
                final List<String> data = campChildrenScheme.getData();
                String planId = data.get(ZERO_NUMBER);
                // String custId = data.get(Constant.SpecialNumber.ONE_NUMBER);
                // 判断产品失效日期
                McdPlanDef plan = planDefService.getById(planId);
                Assert.notNull(plan, "策略:{},配置的产品:{}不正确", baseCampInfo.getCampsegName(), planId);
                final LocalDateTime planEndDate = plan.getPlanEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                Assert.isTrue(planEndDate.isAfter(delayLocalDate) || planEndDate.equals(delayLocalDate), "活动结束日期大于产品失效日期({})，", planEndDate);
            }
        } else {
            //判断子活动的结束时间
            final List<CampChildrenScheme> childrenSchemes = req.getChildrenSchemes();
            //分组
            final Map<String, List<CampChildrenScheme>> groupChildrenSchemes
                    = childrenSchemes.stream().collect(Collectors.groupingBy(CampChildrenScheme::getCampGroup));
            for (CampScheme campScheme : req.getCampSchemes()) {
                if (null != campScheme.getBaseCampInfo()) {
                    final CampBaseInfoVO childBaseCampInfo = campScheme.getBaseCampInfo();
                    final LocalDateTime delayLocalDate = childBaseCampInfo.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    String campName = childBaseCampInfo.getCampsegName();
                    Assert.isTrue(LocalDateTime.now().isBefore(delayLocalDate), "子活动:{},执行结束时间应大于当前时间！", campName);
                    // 活动管理页面对执行中的活动进行延期操作时，如果延期时间大于客户群失效时间，则提示调用coc客户群延期接口
                    final List<CampChildrenScheme> childrenSchemesItem = groupChildrenSchemes.get(campScheme.getCampGroup());
                    for (CampChildrenScheme campChildrenScheme : childrenSchemesItem) {
                        final List<String> data = campChildrenScheme.getData();
                        String planId = data.get(ZERO_NUMBER);
                        // String custId = data.get(Constant.SpecialNumber.ONE_NUMBER);
                        // 判断产品失效日期
                        McdPlanDef plan = planDefService.getById(planId);
                        Assert.notNull(plan, "子活动:{},配置的产品:{}不正确", campName, planId);
                        final LocalDateTime planEndDate = plan.getPlanEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        Assert.isTrue(planEndDate.isAfter(delayLocalDate) || planEndDate.equals(delayLocalDate), "子活动:{},活动结束日期大于产品失效日期({})，", campName, planEndDate);
                    }
                }
            }

        }
    }

    /**
     * 批量导入产品模板下载
     *
     * @param response
     */
    @Override
    public void batchImpPlanTmpDownload(HttpServletResponse response) {
        HSSFWorkbook wb = getSheets(PRODUCT_TEMPLATE_TITLE, IMPORT_PRODUCT_TEMPLATE);
        OutputStream os = null;
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            String fileName = "批量导入产品模板.xls";
            fileName = URLEncoder.encode(fileName, "utf-8");
            fileName = fileName.replace("+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
            response.flushBuffer();
            os = response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            log.error("批量导入产品模板下载异常 {}", e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    log.error("批量导入产品模板下载关流异常 : {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 批量导入客群模板下载
     *
     * @param response 响应流
     */
    @Override
    public void batchImpCustTmpDownload(HttpServletResponse response) {
        HSSFWorkbook wb = getSheets(CUST_TEMPLATE_TITLE, IMPORT_CUST_TEMPLATE);
        OutputStream os = null;
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            String fileName = "批量导入客群模板.xls";
            fileName = URLEncoder.encode(fileName, "utf-8");
            fileName = fileName.replace("+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
            response.flushBuffer();
            os = response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            log.error("批量导入客群模板下载异常 {}", e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    log.error("批量导入客群模板下载关流异常 : {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 批量导入标签模板下载
     *
     * @param response 响应流
     */
    @Override
    public void batchImpLabelTmpDownload(HttpServletResponse response) {
        HSSFWorkbook wb = getSheets(LABEL_TEMPLATE_TITLE, IMPORT_LABEL_TEMPLATE);
        OutputStream os = null;
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            String fileName = "批量导入标签模板.xls";
            fileName = URLEncoder.encode(fileName, "utf-8");
            fileName = fileName.replace("+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
            response.flushBuffer();
            os = response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            log.error("批量导入标签模板下载异常 {}", e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    log.error("批量导入标签模板下载关流异常 : {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 批量导入产品
     *
     * @param multipartFile 文件
     * @return List<McdPlanDef>
     * @throws Exception 异常
     */
    @Override
    public List<McdPlanDef> batchImportProduct(MultipartFile multipartFile) throws Exception {
        // 字节输入流
        InputStream inputStream = multipartFile.getInputStream();
        // 通过输入流创建ExcelReader 对象
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 获取模板表头 判断模板是否正确
        if (!"产品编码".equals(reader.getOrCreateRow(ZERO_NUMBER).getCell(ZERO_NUMBER).toString())) {
            throw new Exception("请按正确的模板导入!");
        }
        // 导入模板的列名要跟这个一致 不然字段是映射不上
        reader.addHeaderAlias("产品编码", "planId");
        List<McdPlanDef> planDefs = reader.readAll(McdPlanDef.class);
        // 获取所有的产品编码
        List<String> planIds = planDefs.stream().map(McdPlanDef::getPlanId).collect(Collectors.toList());
        // 根据产品编码批量查询产品信息
        List<McdPlanDef> list = planDefService.list(Wrappers.<McdPlanDef>query().lambda().in(McdPlanDef::getPlanId, planIds));
        log.info("batchImportProduct-->批量导入产品共{}条", planDefs.size());
        return list;
    }

    /**
     * 批量导入客群
     *
     * @param multipartFile 文件
     * @return {@link List}<{@link McdCustgroupDef}>
     * @throws Exception 异常
     */
    @Override
    public List<BatchImportCustRespVO> batchImportCust(MultipartFile multipartFile) throws Exception {
        List<BatchImportCustRespVO> batchImportCustRespVOList = new ArrayList<>();
        // 字节输入流
        InputStream inputStream = multipartFile.getInputStream();
        // 通过输入流创建ExcelReader 对象
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 获取模板表头 判断模板是否正确
        if (!"客户群编码".equals(reader.getOrCreateRow(ZERO_NUMBER).getCell(ZERO_NUMBER).toString())) {
            throw new Exception("请按正确的模板导入!");
        }
        // 导入模板的列名要跟这个一致 不然字段是映射不上
        reader.addHeaderAlias("客户群编码", "customGroupId");
        List<McdCustgroupDefJx> custgroupDefs = reader.readAll(McdCustgroupDefJx.class);
        // 获取所有的客户群编码
        // 对导入的客群进行以下过滤   保留状态!=0 and columnNum !=null and finalExpression !=null
        List<String> customGroupIds = custgroupDefs.stream().map(McdCustgroupDefJx::getCustomGroupId).collect(Collectors.toList());
        // 根据产品编码批量查询客户群信息 过滤掉未同步dna的客群(COLUMN_NUM == null即未完成同步)
        List<McdCustgroupDefJx> list = custgroupDefJxService.list(Wrappers.<McdCustgroupDefJx>query().lambda()
                .ne(McdCustgroupDefJx::getUpdateStatus, 0)
                .isNotNull(McdCustgroupDefJx::getColumnNum)
                .in(McdCustgroupDefJx::getCustomGroupId, customGroupIds));
        for (McdCustgroupDefJx mcdCustgroupDef : list) {
            // 循环请求客群数量接口  将获取的表达式返回前端(前端直接拿取表达式即可 不需要再请求dna接口)
            BatchImportCustRespVO vo = new BatchImportCustRespVO();
            BeanUtil.copyProperties(mcdCustgroupDef, vo);
            TargetUserCountReqDTO query = new TargetUserCountReqDTO();
            TargetUserCountReqDTO.CustomerVo customerVo = new TargetUserCountReqDTO.CustomerVo();
            List<TargetUserCountReqDTO.CustomerVo.TreeDetails> treeDetails = new ArrayList<>();
            TargetUserCountReqDTO.CustomerVo.TreeDetails  treeDetail = new TargetUserCountReqDTO.CustomerVo.TreeDetails();
            treeDetail.setId(1);
            treeDetail.setParentId(0);
            treeDetail.setType("root");
            treeDetail.setCalType("=");
            treeDetail.setColumnName(null);
            treeDetail.setColumnNum(Integer.parseInt(mcdCustgroupDef.getColumnNum()));
            treeDetail.setSelectedValues(Collections.singletonList("Y"));
            treeDetail.setUpdateCycle(null);
            treeDetails.add(treeDetail);
            customerVo.setTreeDetails(treeDetails);
            query.setCustomerVo(customerVo);
            DNACustomActionResponse dnaActionResponse = dnaColumnService.getTargetUserCount(query);
            log.info("批量导入客群==>客群={}实时获取客群数量响应={}", mcdCustgroupDef.getColumnNum(), JSONUtil.toJsonStr(dnaActionResponse));
            if (DNAResponseStatus.SUCCESS.getCode().equals(dnaActionResponse.getCode())) {
                JSONObject jsonObject = JSONUtil.parseObj(dnaActionResponse.getData());
                vo.setFinalExpression(jsonObject.getStr("finalExpression"));
                batchImportCustRespVOList.add(vo);
            } else {
                log.warn("客群={}DNA表达式为空，不返回前端", mcdCustgroupDef.getCustomGroupId());
            }
        }
        log.info("batchImportCust-->excel文件客群数量={}条，过滤后成功批量导入客户群共{}条", custgroupDefs.size(), batchImportCustRespVOList.size());
        return batchImportCustRespVOList;
    }

    /**
     * 批量导入标签
     *
     * @param multipartFile 文件
     * @return {@link List}<{@link ColumnSearchRespondDTO.ColumnList}>
     * @throws Exception 异常
     */
    @Override
    public List<ColumnSearchRespondDTO.ColumnList> batchImportLabel(MultipartFile multipartFile) throws Exception {
        // 字节输入流
        InputStream inputStream = multipartFile.getInputStream();
        // 通过输入流创建ExcelReader 对象
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 获取模板表头 判断模板是否正确
        if (!"标签名称".equals(reader.getOrCreateRow(ZERO_NUMBER).getCell(ZERO_NUMBER).toString())) {
            throw new Exception("请按正确的模板导入!");
        }
        // 导入模板的列名要跟这个一致 不然字段是映射不上
        reader.addHeaderAlias("标签名称", "columnName");
        reader.addHeaderAlias("标签编码", "columnNum");
        List<ColumnSearchRespondDTO.ColumnList> labels = reader.readAll(ColumnSearchRespondDTO.ColumnList.class);
        log.info("batchImportCust-->批量导入标签共{}条", labels.size());
        return labels;
    }

    /**
     * 主题模板下载
     *
     * @param response 响应流
     */
    @Override
    public void themeTemplateDownload(HttpServletResponse response) {
        HSSFWorkbook wb = getSheets(THEME_TEMPLATE_TITLE, IMPORT_THEME_TEMPLATE);
        OutputStream os = null;
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            String fileName = "主题导入模板.xls";
            fileName = URLEncoder.encode(fileName, "utf-8");
            fileName = fileName.replace("+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
            response.flushBuffer();
            os = response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            log.error("主题导入模板下载异常 {}", e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    log.error("主题导入模板下载关流异常 : {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 设置模板表格样式
     *
     * @param themeTemplateTitle  标题
     * @param importThemeTemplate 内容
     * @return {@link HSSFWorkbook}
     */
    private HSSFWorkbook getSheets(String[] themeTemplateTitle, String[] importThemeTemplate) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        //sheet.createFreezePane(8,100);// 冻结
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFDataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置列的样式 和 自动列宽
        for (int i = 0; i < themeTemplateTitle.length; i++) {
            sheet.setDefaultColumnStyle(i, style);
            sheet.autoSizeColumn(i);
            int maxWith = 255 * 256;
            int newWidth = sheet.getColumnWidth(i) * 17 / 10;
            if (newWidth < maxWith) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, maxWith / 2);
            }
        }
        // 字体样式
        HSSFFont columnHeadFont = wb.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeightInPoints((short) 12);
        //columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(columnHeadFont);
        HSSFCell cell = row.createCell((short) 0);
        for (int i = 0; i < themeTemplateTitle.length; i++) {
            HSSFCellStyle style_0 = wb.createCellStyle();
            style_0.setAlignment(HorizontalAlignment.CENTER);
            HSSFFont columnHeadFont_0 = wb.createFont();
            columnHeadFont_0.setFontName("宋体");
            columnHeadFont_0.setFontHeightInPoints((short) 12);
            columnHeadFont_0.setBold(true);
            // 表头最后一列才设置
            if (i == themeTemplateTitle.length - 1) {
                columnHeadFont_0.setColor(IndexedColors.RED.getIndex());
            }
            style_0.setFont(columnHeadFont_0);
            cell.setCellType(CellType.STRING);
            cell = row.createCell(i);
            cell.setCellValue(themeTemplateTitle[i]);
            cell.setCellStyle(style_0);
        }
        HSSFRow row1 = sheet.createRow((short) 1);
        HSSFCell cell1 = row1.createCell((short) 0);
        for (int i = 0; i < importThemeTemplate.length; i++) {
            HSSFCellStyle style_0 = wb.createCellStyle();
            style_0.setAlignment(HorizontalAlignment.CENTER);
            HSSFFont columnHeadFont_0 = wb.createFont();
            columnHeadFont_0.setFontName("宋体");
            columnHeadFont_0.setFontHeightInPoints((short) 12);
            columnHeadFont_0.setBold(false);
            style_0.setFont(columnHeadFont_0);
            cell1.setCellType(CellType.STRING);
            cell1 = row1.createCell(i);
            cell1.setCellValue(importThemeTemplate[i]);
            cell1.setCellStyle(style_0);
        }
        return wb;
    }

    /**
     * 查询各类型下的模板
     *
     * @param pageQuery 入参对象
     * @return {@link List}<{@link TemplateByTypeRespondDTO}>
     */
    @Override
    public List<TemplateByTypeRespondDTO> queryTemplateByType(McdPageQuery pageQuery) {
        List<TemplateByTypeRespondDTO> dtoList = new ArrayList<>();
        List<TemplateInfoRespondVO> templateInfoRespondVOList = new ArrayList<>();
        // List<Map<String, Object>> map = null;
        try {
            // 1. 查询状态可用的所有模板
            List<McdPrismCampTemplate> list = mcdPrismCampTemplateService.list(Wrappers.<McdPrismCampTemplate>query().lambda().eq(McdPrismCampTemplate::getStatus, 1)
                    .eq(StrUtil.isNotEmpty(pageQuery.getKeyWords()), McdPrismCampTemplate::getTemplateName, pageQuery.getKeyWords()));
            if (CollectionUtil.isEmpty(list)) {
                log.warn("未查询到各类型下的模板信息");
                return new ArrayList<>();
            }
            // 1.1 获取所有的模板id
            // List<String> themeIdList = list.stream().map(McdPrismCampTemplate::getTemplateId).collect(Collectors.toList());
            // if (CollectionUtil.isNotEmpty(themeIdList)) {
            //     // 1.2 根据模板id批量查询是否有创建活动
            //     map = mcdPrismSceneCampService.queryTemplateRefCount(themeIdList);
            // }
            // 2. 按照模板场景分类进行分组
            Map<Integer, List<McdPrismCampTemplate>> listMap = list.stream().collect(Collectors.groupingBy(McdPrismCampTemplate::getSceneClass));
            // 3. 组装数据返回
            // List<Map<String, Object>> finalMap = map;
            listMap.forEach((sceneClass, templateList) -> {
                TemplateByTypeRespondDTO dto = new TemplateByTypeRespondDTO();
                dto.setSceneType(sceneClass);
                templateList.forEach(template -> {
                    TemplateInfoRespondVO vo = new TemplateInfoRespondVO();
                    BeanUtil.copyProperties(template, vo, true);
                    templateInfoRespondVOList.add(vo);
                });
                dto.setTemplateInfoRespondVOList(templateInfoRespondVOList);
                dtoList.add(dto);
            });
        } catch (Exception e) {
            log.error("查询各主题场景类型下的模板异常：", e);
            return dtoList;
        }
        return dtoList;
    }

    /**
     * 分页查询模板列表数据
     *
     * @param paramVO 分页查询入参对象
     * @return {@link IPage}<{@link McdPrismCampTemplate}>
     */
    @Override
    public IPage<McdPrismCampTemplate> queryTemplateList(TemplateListParamVO paramVO, UserSimpleInfo user) {
        try {
            // 1. 分页查询状态可用的所有模板
            IPage<McdPrismCampTemplate> pager = new Page<>(paramVO.getCurrent(), paramVO.getSize());
            IPage<McdPrismCampTemplate> page = mcdPrismCampTemplateService.page(pager, Wrappers.<McdPrismCampTemplate>query().lambda().eq(McdPrismCampTemplate::getStatus, 1)
                    .eq(ObjectUtil.isNotEmpty(paramVO.getSceneClass()), McdPrismCampTemplate::getSceneClass, paramVO.getSceneClass())
                    .eq(1 == paramVO.getIsSelectMy(), McdPrismCampTemplate::getCreateUser, user.getUserId())
                    .and(StrUtil.isNotEmpty(paramVO.getKeyWords()), c -> c.like(McdPrismCampTemplate::getTemplateId, paramVO.getKeyWords())
                            .or()
                            .like(StrUtil.isNotEmpty(paramVO.getKeyWords()), McdPrismCampTemplate::getTemplateName, paramVO.getKeyWords()))
                    .orderByDesc(McdPrismCampTemplate::getCreateTime));
            List<McdPrismCampTemplate> list = page.getRecords();
            if (CollectionUtil.isEmpty(list)) {
                log.warn("未查询到模板信息");
                return new Page<>();
            }
            return page;
        } catch (Exception e) {
            log.error("查询模板列表信息异常：", e);
            return new Page<>();
        }
    }

    /**
     * 批量删除主题下策略信息
     *
     * @param themeId 主题id
     */
    @Override
    @Transactional
    public boolean batchDelCampseg(String themeId) {
        try {
            // 1. 根据主题id查询下面的所有活动
            List<Map<String, Object>> allCampByThemeId = mcdPrismSceneCampService.queryAllCampByThemeId(themeId);
            for (Map<String, Object> map : allCampByThemeId) {
                String id = String.valueOf(map.get("CAMPSEG_ROOT_ID"));
                final McdCampDef campDef = campDefService.getById(id);
                Assert.notNull(campDef, "传入的id不正确，请重试！");
                Assert.isTrue(campDef.getCampsegStatId().equals(CampStatus.DRAFT.getId()) ||
                        campDef.getCampsegStatId().equals(CampStatusJx.PREVIEWED.getId()) ||
                        campDef.getCampsegStatId().equals(CampStatusJx.PREVIEW_ERROR.getId()), "活动当前状态不可删除");
                final String campsegRootId = campDef.getCampsegRootId();
                boolean isRoot = Constant.SpecialNumber.ZERO_STRING.equals(campsegRootId);
                final LambdaQueryWrapper<McdCampDef> qry = Wrappers.lambdaQuery();
                //mcd_camp_def
                campDefService.removeById(id);
                if (isRoot) {
                    //mcd_camp_def,子活动
                    qry.eq(McdCampDef::getCampsegRootId, id);
                    campDefService.remove(qry);
                    //mcd_camp_channel_ext
                    extService.deleteByCampsegRootId(id);
                    //mcd_camp_channel_list
                    campChannelListService.deleteByCampsegRootId(id);
                } else {
                    //如果是子活动，验证是否还有其余子活动
                    qry.eq(McdCampDef::getCampsegRootId, campsegRootId);
                    final List<McdCampDef> list = campDefService.list(qry);
                    if (!CollectionUtils.isEmpty(list)) {
                        //还有子活动
                        //mcd_camp_channel_ext
                        extService.deleteByCampsegPid(id);
                        //mcd_camp_channel_list
                        campChannelListService.deleteByCampsegPid(id);
                    } else {
                        //没有子活动，需要删除父活动
                        campDefService.removeById(campsegRootId);
                        //mcd_camp_channel_ext
                        extService.deleteByCampsegRootId(campsegRootId);
                        //mcd_camp_channel_list
                        campChannelListService.deleteByCampsegRootId(campsegRootId);
                    }
                }
                // 更新cep事件状态
                mcdCampsegService.updateCEPEventId(id, isRoot, CampStatus.SUSPEND);
                // 记录删除日志
                logService.markSuccLog(id, CampLogType.CAMP_DELETE, "删除", null);
            }
            // 根据主题查询场景
            List<McdPrismDimScene> sceneList = mcdPrismDimSceneService.list(Wrappers.<McdPrismDimScene>query().lambda().eq(McdPrismDimScene::getThemeId, themeId).eq(McdPrismDimScene::getStatus, 1));
            // 获取所有场景id
            List<String> sceneIdList = sceneList.stream().map(McdPrismDimScene::getSceneId).collect(Collectors.toList());
            // 2. 删除场景下活动
            mcdPrismSceneCampService.remove(Wrappers.<McdPrismSceneCamp>query().lambda().in(McdPrismSceneCamp::getSceneId, sceneIdList));
            // 3. 删除主题下场景
            mcdPrismDimSceneService.remove(Wrappers.<McdPrismDimScene>query().lambda().eq(McdPrismDimScene::getThemeId, themeId));
            // 4. 删除主题
            mcdPrismCampThemeService.remove(Wrappers.<McdPrismCampTheme>query().lambda().eq(McdPrismCampTheme::getThemeId, themeId));
        } catch (Exception e) {
            log.error("批量删除主题下策略信息异常：", e);
            return false;
        }
        return true;
    }

    /**
     * 根据主题id查询画布流程详细信息
     *
     * @param reqVO 入参
     * @return {@link McdPrismCampTheme}
     */
    @Override
    public McdPrismThemeDetailRespVO getThemeDetailByThemeId(QueryCampByThemeIdOrTepIdReqVO reqVO) {
        McdPrismThemeDetailRespVO respVO = new McdPrismThemeDetailRespVO();
        // 根据主题id查询所有的活动信息以及渠道名称
        List<Map<String, Object>> allCampByThemeId = mcdPrismSceneCampService.queryAllCampInfoByThemeId(reqVO);
        McdPrismCampTheme one = mcdPrismCampThemeService.getOne(Wrappers.<McdPrismCampTheme>query().lambda()
                .select(McdPrismCampTheme::getThemeContent)
                .eq(McdPrismCampTheme::getThemeId, reqVO.getThemeId()));
        respVO.setThemeOrTemplateContent(one.getThemeContent());
        respVO.setAllCampByThemeIdOrTemplateId(allCampByThemeId);
        respVO.setIsCopy(Integer.valueOf(String.valueOf(allCampByThemeId.get(ZERO_NUMBER).get("IS_COPY"))));
        return respVO;
    }

    /**
     * 根据模板id查询画布流程详细信息
     *
     * @param reqVO 入参
     * @return {@link McdPrismCampTemplate}
     */
    @Override
    public McdPrismThemeDetailRespVO getTemplateDetailByTemplateId(QueryCampByThemeIdOrTepIdReqVO reqVO) {
        McdPrismThemeDetailRespVO respVO = new McdPrismThemeDetailRespVO();
        // 根据模板id查询所有的活动信息以及渠道名称
        List<Map<String, Object>> allCampByThemeId = mcdPrismSceneCampService.queryAllCampInfoByThemeId(reqVO);
        McdPrismCampTemplate one = mcdPrismCampTemplateService.getOne(Wrappers.<McdPrismCampTemplate>query().lambda()
                .select(McdPrismCampTemplate::getTemplateContent)
                .eq(McdPrismCampTemplate::getTemplateId, reqVO.getTemplateId()));
        respVO.setThemeOrTemplateContent(one.getTemplateContent());
        respVO.setAllCampByThemeIdOrTemplateId(allCampByThemeId);
        return respVO;
    }

    /**
     * 复制主题
     *
     * @param sourceThemeId 源主题id
     * @param newThemeName  新的主题名称
     * @param user          用户信息
     * @param flag          flag
     * @return 复制后的根活动id
     */
    @Override
    @Transactional
    public String copyTheme(String sourceThemeId, String newThemeName, UserSimpleInfo user, String flag) {
        log.info("copyTheme start...");
        // 场景和活动关系复制后数据集合
        List<McdPrismSceneCamp> sceneCampList = new ArrayList<>();
        // 批量保存的所有活动id集合
        List<String> campsegIds = new ArrayList<>();
        // 新的场景名称集合
        List<String> newSceneNameList = new ArrayList<>();
        final String newThemeId = IdUtils.generateId();
        // 1. 根据主题id查询主题信息
        final McdPrismCampTheme theme = new McdPrismCampTheme().setThemeId(sourceThemeId);
        LambdaQueryWrapper<McdPrismCampTheme> qry = Wrappers.lambdaQuery(theme);
        McdPrismCampTheme campTheme = mcdPrismCampThemeService.getOne(qry);
        String newName = newThemeName;
        if (StringUtils.isBlank(newName)) {
            newName = campTheme.getThemeName() + "_复制";
            if (!checkThemes(null, newName)) {
                // 验证主题名称重复问题
                newName += DateUtil.now();
            }
        }
        campTheme.setThemeId(newThemeId).setThemeName(newName).setCreateTime(new Date()).setCreateUser(user.getUserId());
        // 2. 根据主题查询场景信息
        List<McdPrismDimScene> sourceSceneList = mcdPrismDimSceneService.list(Wrappers.<McdPrismDimScene>query().lambda()
                .eq(McdPrismDimScene::getThemeId, sourceThemeId).eq(McdPrismDimScene::getStatus, 1));
        for (McdPrismDimScene sourceScene : sourceSceneList) {
            sourceScene.setSceneId(IdUtils.generateId());
            String newSceneName = sourceScene.getSceneName() + "_复制";
            // 验证场景名称重复问题
            McdPrismDimScene one = mcdPrismDimSceneService.getOne(Wrappers.<McdPrismDimScene>query().lambda()
                    .eq(McdPrismDimScene::getSceneName, newSceneName).eq(McdPrismDimScene::getStatus, 1));
            if (ObjectUtil.isNotEmpty(one)) {
                newSceneName += DateUtil.now();
            }
            newSceneNameList.add(newSceneName);
            sourceScene.setSceneName(newSceneName);
            sourceScene.setCreateTime(new Date());
            sourceScene.setCreateUser(user.getUserId());
            sourceScene.setThemeId(newThemeId);
            sourceScene.setIsCopy(1);
            McdPrismSceneCamp sceneCamp = null;
            // 场景与策略关系-最后一层级场景id数据填充
            if (IntellPushPrismConstant.LEAF_SCENE_FLAG.equals(sourceScene.getLeafSceneFlag())) {
                sceneCamp = new McdPrismSceneCamp();
                sceneCamp.setSceneId(sourceScene.getSceneId());
                sceneCampList.add(sceneCamp);
            }
        }
        // 主题内容字段--复制新的主题名称回填
        if (0 == campTheme.getSceneType()) {
            dealThemeContent(campTheme, newName, newSceneNameList);
        } else {
            // 辅助营销、智能营销
            dealThemeContentOnAI(campTheme, newName, newSceneNameList);
        }
        mcdPrismCampThemeService.save(campTheme);
        // 2.1 保存主题下的场景信息
        mcdPrismDimSceneService.saveBatch(sourceSceneList);
        // 3. 根据主题id查询下面的所有活动
        List<Map<String, Object>> allCampByThemeId = mcdPrismSceneCampService.queryAllCampByThemeId(sourceThemeId);
        for (Map<String, Object> map : allCampByThemeId) {
            String sourceCampId = String.valueOf(map.get("CAMPSEG_ROOT_ID"));
            final List<McdCampDef> sourceCampDefs = campDefService.listByCampsegRootId(sourceCampId);
            Assert.notEmpty(sourceCampDefs, "传入的id不正确，请重试！");
            final List<McdCampChannelList> allChannelList = new ArrayList<>();
            final Map<String, String> oldNewRelation = new HashMap<>();
            final String newCampRootId = IdUtils.generateId();
            campsegIds.add(newCampRootId);
            for (McdCampDef sourceCampDef : sourceCampDefs) {
                if (null != user) {
                    sourceCampDef.setCreateUserId(user.getUserId()).setCityId(user.getCityId());
                }
                if (Constant.SpecialNumber.ONE_STRING.equals(flag)) {
                    sourceCampDef.setCampDefType(CampDefType.IOP.getType());
                }
                String newCampName = newThemeName;
                if (StringUtils.isBlank(newCampName)) {
                    newCampName = sourceCampDef.getCampsegName() + "_复制";
                    if (!tacticsManagerJxService.checkTactics(newCampName, null)) {
                        //验证活动名称重复问题
                        newCampName += DateUtil.now();
                    }
                }
                //复制活动基本信息
                sourceCampDef.setCampsegName(newCampName).setCreateTime(new Date())
                        .setCampsegStatId(CampStatus.DRAFT.getId()).setApproveFlowId(null);
                if (Constant.SpecialNumber.ZERO_STRING.equals(sourceCampDef.getCampsegRootId())) {
                    sourceCampDef.setCampsegId(newCampRootId);
                } else {
                    //旧wave信息和新wave信息关联
                    final String newCampPid = IdUtils.generateId();
                    final List<McdCampChannelList> mcdCampChannelLists
                            = campChannelListService.listMcdCampChannelListByCampsegPid(sourceCampDef.getCampsegId());
                    mcdCampChannelLists.forEach(campChannelList -> {
                        final String newCampId = IdUtils.generateId();
                        oldNewRelation.put(campChannelList.getCampsegId(), newCampId);
                        campChannelList.setCampsegRootId(newCampRootId);
                        campChannelList.setCampsegPid(newCampPid);
                        campChannelList.setCampsegId(newCampId);
                        campChannelList.setStatus(CampStatus.DRAFT.getId());
                        campChannelList.setReason(Strings.EMPTY);
                        campChannelList.setWavesCampPid(oldNewRelation.get(campChannelList.getWavesCampPid()));
                        allChannelList.add(campChannelList);
                    });
                    sourceCampDef.setCampsegRootId(newCampRootId).setCampsegId(newCampPid);
                }
            }
            campDefService.saveBatch(sourceCampDefs);
            final Collection<McdCampChannelExt> campChannelExts = extService.listByIds(oldNewRelation.keySet());
            campChannelExts.forEach(campChannelExt -> campChannelExt.setCampsegId(oldNewRelation.get(campChannelExt.getCampsegId())));
            //复制渠道运营位基础属性
            campChannelListService.saveBatch(allChannelList);
            //复制渠道运营位扩展属性
            extService.saveBatch(campChannelExts);
            // 保存活动级别的频次信息到频次规则表 mcd_fqc_rule
            saveFqc(allChannelList);
            for (McdCampDef campDef : sourceCampDefs) {
                if (Constant.SpecialNumber.ZERO_STRING.equals(campDef.getCampsegRootId())) {
                    // 生成预演数据
                    campPreveiwJxService.savePreview(campDef, allChannelList);
                    break;
                }
            }
            logService.markSuccLog(newThemeId, CampLogType.CAMP_CREATE, "复制", null);
        }
        // 4. 批量保存场景与策略关系  遍历两个集合中较短的那个，以避免数组越界异常
        for (int i = 0; i < Math.min(campsegIds.size(), sceneCampList.size()); i++) {
            McdPrismSceneCamp camp = sceneCampList.get(i);
            String campsegId = campsegIds.get(i);
            // 场景与策略关系-活动rootId数据填充
            camp.setCampsegRootId(campsegId);
        }
        mcdPrismSceneCampService.saveBatch(sceneCampList);
        return newThemeId;
    }

    /**
     * 主题内容字段--复制新的主题名称回填
     *
     * @param campTheme 主题信息
     * @param newName 新的主题名
     * @param newSceneNameList 新的场景名称集合
     */
    private void dealThemeContentOnAI (McdPrismCampTheme campTheme, String newName, List<String> newSceneNameList) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(campTheme.getThemeContent());
            // 修改themeName
            modifyThemeName(rootNode, newName);
            // 递归修改customGroupName
            recursivelyModifyCustomGroupName(rootNode, newSceneNameList);
            // 将修改后的JSON转换回字符串
            String modifiedJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            campTheme.setThemeContent(JSONUtil.toJsonStr(modifiedJsonString));
        } catch (Exception e) {
            log.error("dealThemeContentOnAI-->异常：", e);
        }
    }

    /**
     * 主题内容字段--复制新的主题名称回填
     *
     * @param campTheme 主题信息
     * @param newName 新的主题名
     * @param newSceneNameList 新的场景名称集合
     */
    private void dealThemeContent(McdPrismCampTheme campTheme, String newName, List<String> newSceneNameList) {
        Map<String, Object> themeMap = new HashMap<>();
        String themeContent = campTheme.getThemeContent();
        JSONObject jsonObject = JSONUtil.parseObj(themeContent);
        String businessData1 = jsonObject.getStr("BUSINESS_DATA");
        Object businessData2 = jsonObject.get("businessData");
        Object depth = jsonObject.get("depth");
        Object extendData = jsonObject.get("extendData");
        Object id = jsonObject.get("id");
        Object nodeLines = jsonObject.get("nodeLines");
        Object pId = jsonObject.get("pId");
        Object size = jsonObject.get("size");
        Object style = jsonObject.get("style");
        Object title = jsonObject.get("title");
        Object type = jsonObject.get("type");
        Object x = jsonObject.get("x");
        Object y = jsonObject.get("y");
        JSONObject businessData = JSONUtil.parseObj(businessData1);
        businessData.set("themeName", newName);
        // jsonObject.set("BUSINESS_DATA", businessData);
        themeMap.put("BUSINESS_DATA", businessData);
        themeMap.put("businessData", businessData2);
        // themeMap.put("children", children);
        themeMap.put("depth", depth);
        themeMap.put("extendData", extendData);
        themeMap.put("id", id);
        themeMap.put("nodeLines", nodeLines);
        themeMap.put("pId", pId);
        themeMap.put("size", size);
        themeMap.put("style", style);
        themeMap.put("title", title);
        themeMap.put("type", type);
        themeMap.put("x", x);
        themeMap.put("y", y);
        // 主题内容字段--复制新的场景名称回填
        // 获取所有的场景节点信息
        JSONArray sceneChildrenList = JSONUtil.parseArray(jsonObject.getStr("children"));
        // 递归处理场景名称回填
        recursiveDealSceneName(sceneChildrenList, themeMap, newSceneNameList);
        // 1.1 保存主题数据
        campTheme.setThemeContent(JSONUtil.toJsonStr(themeMap));
    }

    /**
     * 递归处理场景名称回填
     *
     * @param sceneChildrenList 场景集合对象
     * @param themeMap 主题内容信息
     */
    private void recursiveDealSceneName (JSONArray sceneChildrenList, Map<String, Object> themeMap, List<String> newSceneNameList) {
        List<Object> childrenList = new ArrayList<>();
        for (int i = 0; i < sceneChildrenList.size(); i++) {
            JSONObject children = JSONUtil.parseObj(sceneChildrenList.get(i));
            if ("Scene".equals(children.getStr("type"))) {
                JSONObject childrenBusinessData = children.getJSONObject("businessData");
                childrenBusinessData.set("sceneName", newSceneNameList.get(i));
                // 回塞赋值后的属性值
                children.set("businessData", childrenBusinessData);
                childrenList.add(children);
                themeMap.put("children", childrenList);
                JSONArray childrenJsonArray = JSONUtil.parseArray(children.getStr("children"));
                if (childrenJsonArray.size() > 0) {
                    // 递归判断
                    recursiveDealSceneName(childrenJsonArray, themeMap, newSceneNameList);
                }
            }
            // 如果type != Scene 则跳出循环进行下一次判断
        }
    }

    /**
     * 我的模板、首页模板信息一键引用
     *
     * @param templateId 模板id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> oneClickRefMyTemplate(String templateId) {
        // 1. 据模板id批量查询下面归属的活动
        List<Map<String, Object>> mapList = null;
        try {
            mapList = mcdPrismSceneCampService.queryCampInfoByThemeIdOrTemplateId(new QueryCampByThemeIdOrTepIdReqVO().setTemplateId(templateId).setThemeId(null));
        } catch (Exception e) {
            log.error("我的模板、首页模板信息一键引用查询异常：", e);
        }
        return mapList;
    }

    /**
     * 根据主题或者模版查询下面的活动
     *
     * @param reqVO 入参
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> queryCampInfoByThemeIdOrTemplateId(QueryCampByThemeIdOrTepIdReqVO reqVO) {
        // 1. 根据主题或者模版查询下面的活动
        List<Map<String, Object>> mapList = null;
        try {
            mapList = mcdPrismSceneCampService.queryCampInfoByThemeIdOrTemplateId(reqVO);
        } catch (Exception e) {
            log.error("根据主题或者模版查询下面的活动查询异常：", e);
        }
        return mapList;
    }

    /**
     * 修改themeName
     *
     * @param node 获取BUSINESS_DATA节点信息
     * @param newName 新的主题名
     */
    private static void modifyThemeName(JsonNode node, String newName) {
        JsonNode businessDataNode = node.get("BUSINESS_DATA");
        if (businessDataNode != null && businessDataNode.isObject()) {
            ObjectNode businessDataObject = (ObjectNode) businessDataNode;
            businessDataObject.put("themeName", newName);
        }
    }

    /**
     * 递归修改customGroupName
     *
     * @param node 获取children节点信息
     * @param newSceneNameList 新的场景名集合
     */
    private static void recursivelyModifyCustomGroupName(JsonNode node, List<String> newSceneNameList) {
        if (node.has("children") && node.get("children").isArray()) {
            for (int i = 0; i < node.get("children").size(); i++) {
                JsonNode child = node.get("children").get(i);
                if (child.has("type") && "AiCustomer".equals(child.get("type").asText())) {
                    JsonNode businessDataNode = child.get("businessData");
                    if (businessDataNode != null && businessDataNode.isObject()) {
                        ObjectNode businessDataObject = (ObjectNode) businessDataNode;
                        JsonNode customerInfoNode = businessDataObject.get("customerInfo");
                        if (customerInfoNode != null && customerInfoNode.isObject()) {
                            ObjectNode customerInfoObject = (ObjectNode) customerInfoNode;
                            for (String newSceneName : newSceneNameList) {
                                if (newSceneName.contains(customerInfoObject.get("customGroupName").asText())) {
                                    customerInfoObject.put("customGroupName", newSceneName);
                                }
                            }
                        }
                    }
                }
                recursivelyModifyCustomGroupName(child, newSceneNameList);
            }
        }
    }

}
