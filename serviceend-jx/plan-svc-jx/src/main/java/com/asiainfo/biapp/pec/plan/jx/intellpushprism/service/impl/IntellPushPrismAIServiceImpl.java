package com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.common.jx.constant.RedisDicKey;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.constants.CommonConstant;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.plan.jx.dna.constant.ConstantDNA;
import com.asiainfo.biapp.pec.plan.jx.dna.service.IDNACustomGroupService;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dao.IIntellPushPrismAIDao;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna.*;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.DnaColumnService;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.IIntellPushPrismAIService;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.*;
import com.asiainfo.biapp.pec.plan.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.model.McdCustgroupDef;
import com.asiainfo.biapp.pec.plan.model.McdPlanDef;
import com.asiainfo.biapp.pec.plan.service.IMcdCampDefService;
import com.asiainfo.biapp.pec.plan.service.IMcdCustgroupDefService;
import com.asiainfo.biapp.pec.plan.service.IMcdPlanDefService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.ONE_NUMBER;
import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.TWO_NUMBER;
import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.ZERO_NUMBER;

/**
 * description: 智推棱镜-AI流程service实现
 *
 * @author: lvchaochao
 * @date: 2024/5/25
 */
@Service
@Slf4j
public class IntellPushPrismAIServiceImpl implements IIntellPushPrismAIService {

    @Autowired
    private IMcdPlanDefService mcdPlanDefService;

    @Autowired
    private DnaColumnService dnaColumnService;

    @Autowired
    private IIntellPushPrismAIDao intellPushPrismAIDao;

    @Autowired
    private IMcdCampDefService mcdCampDefService;

    @Autowired
    private IDNACustomGroupService idnaCustomGroupService;

    @Resource
    private IMcdCustgroupDefService custgroupDefService;

    /**
     * 产品、客群一对一推理区间接口路径
     */
    @Value("${intellpushprism.scoreRange.url}")
    private String scoreRangeUrl;

    /**
     * 产品、客群多对多推理区间接口路径
     */
    @Value("${intellpushprism.scoreRangeBatch.url}")
    private String scoreRangeBatchUrl;

    /**
     * 推理任务保存接口路径
     */
    @Value("${intellpushprism.save.url}")
    private String saveUrl;

    /**
     * 手动执行AI推理任务路径
     */
    @Value("${intellpushprism.handleExec.url}")
    private String handleExecUrl;

    @Value("${spring.profiles.active}")
    private String profiles;

    /**
     * AI推理-获取根据标签裂变的客群信息
     *
     * @param reqVO 标签裂变获取客群编号接口请求参数对象
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse getFissionCustInfo(LabelFissionGetCustIdReqDTO reqVO) {
        List<LabelFissionGetCustIdRespDTO> customerList;
        try {
            // 1.1 组装请求入参
            DNACustomActionResponse dnaActionResponse = dnaColumnService.getLabelFissionCustId(reqVO);
            log.info("AI推理-获取根据标签裂变的客群信息接口返回={}", JSONUtil.toJsonStr(dnaActionResponse));
            if (DNAResponseStatus.SUCCESS.getCode().equals(dnaActionResponse.getCode())) {
                JSONArray jsonArray = JSONUtil.parseArray(JSONUtil.toJsonStr(dnaActionResponse.getData()));
                customerList = JSONUtil.toList(jsonArray, LabelFissionGetCustIdRespDTO.class);
            } else {
                log.warn("AI推理-未获取到根据标签裂变的客群信息");
                throw new BaseException("AI推理-未获取到裂变后客群编号");
            }
        } catch (Exception e) {
            log.error("AI推理-未获取到根据标签裂变的客群信息异常：", e);
            throw new BaseException("AI推理-请求dna接口获取裂变客群编号异常");
        }
        return ActionResponse.getSuccessResp(customerList);
    }

    /**
     * 智慧大脑产品同步IOP
     * 1. 产品表增加是否能推理的标识
     * 2. 根据同步过来增加的产品，将这些产品对应的标识改为能推理
     *    根据同步过来删除的产品，将这些产品对应的标识改为不能推理
     *
     * @param reqVO 同步的产品信息
     */
    @Override
    public void syncAIProduct(SyncAIProductReqVO reqVO) {
        try {
            log.info("智慧大脑产品同步IOP开始");
            if (CollectionUtil.isNotEmpty(reqVO.getAddProducts())) {
                boolean updateSave = mcdPlanDefService.update(Wrappers.<McdPlanDef>update().lambda().set(McdPlanDef::getIsSupportAi, 1).in(McdPlanDef::getPlanId, reqVO.getAddProducts()));
                if (updateSave){
                    log.warn("智慧大脑产品同步IOP更新支持AI成功");
                } else {
                    log.warn("智慧大脑产品同步IOP更新支持AI失败");
                }
            }
            if (CollectionUtil.isNotEmpty(reqVO.getDelProducts())) {
                boolean updateDel = mcdPlanDefService.update(Wrappers.<McdPlanDef>update().lambda().set(McdPlanDef::getIsSupportAi, 0).in(McdPlanDef::getPlanId, reqVO.getDelProducts()));
                if (updateDel){
                    log.warn("智慧大脑产品同步IOP更新不支持AI成功");
                } else {
                    log.warn("智慧大脑产品同步IOP更新不支持AI失败");
                }
            }
            log.info("智慧大脑产品同步IOP结束，新增支持AI={}条，更新不支持={}条", reqVO.getAddProducts().size(), reqVO.getDelProducts().size());
        } catch (Exception e) {
           log.error("智慧大脑产品同步IOP更新异常：", e);
        }
    }

    /**
     * 客群打分区间数据查询
     *
     * @param reqVO 客群打分区间数据查询入参对象
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse queryScoreRange(QueryScoreRangeReqVO reqVO) {
        try {
            String resp = HttpRequest.post(scoreRangeUrl)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(JSONUtil.toJsonStr(reqVO)).execute().body();
            log.info("客群打分区间数据查询响应={}", JSONUtil.toJsonStr(resp));
            JSONObject jsonObject = JSONUtil.parseObj(resp);
            String status = jsonObject.getStr("status");
            if ("SUCCESS".equals(status)) {
                JSONArray jsonArray = JSONUtil.parseArray(jsonObject.get("data"));
                List<AIScoreRangeRespVO> aiScoreRangeRespVOS = JSONUtil.toList(jsonArray, AIScoreRangeRespVO.class);
                return ActionResponse.getSuccessResp().setData(aiScoreRangeRespVOS);
            } else {
                return ActionResponse.getFaildResp(jsonObject.getStr("message"));
            }
        } catch (Exception e) {
            log.info("客群打分区间数据查询返回异常：", e);
            return ActionResponse.getFaildResp("客群打分区间数据查询返回异常");
        }
    }

    /**
     * 客群打分区间数据批量查询
     *
     * @param reqVO 客群打分区间数据批量查询入参对象
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse queryScoreRangeBatch(QueryScoreRangeReqBatchVO reqVO) {
        try {
            String resp = HttpRequest.post(scoreRangeBatchUrl)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(JSONUtil.toJsonStr(reqVO)).execute().body();
            log.info("客群打分区间数据批量查询响应={}", JSONUtil.toJsonStr(resp));
            JSONObject jsonObject = JSONUtil.parseObj(resp);
            String status = jsonObject.getStr("status");
            if ("SUCCESS".equals(status)) {
                JSONArray jsonArray = JSONUtil.parseArray(jsonObject.get("data"));
                List<List<AIScoreRangeRespVO>> voList = new ArrayList<>();
                for (Object obj : jsonArray) {
                    JSONArray innerArray = (JSONArray) obj;
                    List<AIScoreRangeRespVO> innerList = CollUtil.newArrayList();
                    for (Object innerObj : innerArray) {
                        JSONObject innerJsonObject = (JSONObject) innerObj;
                        AIScoreRangeRespVO vo = new AIScoreRangeRespVO(innerJsonObject);
                        innerList.add(vo);
                    }
                    voList.add(innerList);
                }
                return ActionResponse.getSuccessResp().setData(voList);
            } else {
                return ActionResponse.getFaildResp(jsonObject.getStr("message"));
            }
        } catch (Exception e) {
            log.info("客群打分区间数据批量查询返回异常：", e);
            return ActionResponse.getFaildResp("客群打分区间数据批量查询返回异常");
        }
    }

    /**
     * 智慧大脑推荐客群回调
     * 1. 根据回调的taskId判断任务执行完毕
     *
     * @param reqVO 智慧大脑推荐客群回调入参对象
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse callbackTask(CallbackTaskReqVO reqVO) {
        String status = reqVO.getStatus();
        if ("SUCCESS".equals(status)) {
            // 获取推理任务id
            String taskId = reqVO.getInfo().getTaskId();
            // 根据任务id更新活动定义表对应任务id状态为完成推理AI_TASK_STATUS = 1
            boolean update = mcdCampDefService.update(Wrappers.<McdCampDef>update().lambda().set(McdCampDef::getAITaskStatus, 1).eq(McdCampDef::getAITaskId, taskId));
            if (update) {
                log.info("根据任务id={}更新活动定义表推理任务状态为已完成成功", taskId);
            } else {
                log.info("根据任务id={}更新活动定义表推理任务状态为已完成失败", taskId);
            }
        } else {
            log.warn("智慧大脑推荐客群回调接口入参异常");
        }
        return ActionResponse.getSuccessResp();
    }

    /**
     * AI执行推理任务
     *
     * @param reqVO AI执行推理任务入参对象
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse getInferenceResByAI(AISaveTaskReqVO reqVO) {
        AITaskRespondFinalVO finalVO = new AITaskRespondFinalVO();
        List<AITaskRespondVO> respondVOS = new ArrayList<>();
        log.info("AI执行推理任务请求接口入参={}", JSONUtil.toJsonStr(reqVO.getResByAIReqVO(), new JSONConfig().setIgnoreNullValue(true)));
        try {
            String resp = HttpRequest.post(saveUrl)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(JSONUtil.toJsonStr(reqVO.getResByAIReqVO(), new JSONConfig().setIgnoreNullValue(true))).execute().body();
            log.info("AI执行推理任务请求接口响应={}", JSONUtil.toJsonStr(resp));
            JSONObject jsonObject = JSONUtil.parseObj(resp);
            String status = jsonObject.getStr("status");
            if ("SUCCESS".equals(status)) {
                // 接口响应成功时，构建返回实体对象
                if (buildReturnVO(finalVO, respondVOS, jsonObject))
                    return ActionResponse.getFaildResp("AI执行推理任务未返回匹配数据,请重新选择规则");
                // 任务id信息会保存活动定义表  映射活动
                return ActionResponse.getSuccessResp().setData(finalVO);
            } else {
                log.warn("AI执行推理任务返回失败");
                return ActionResponse.getFaildResp(jsonObject.getStr("message"));
            }
        } catch (Exception e) {
            log.info("AI执行推理任务返回异常：", e);
            return ActionResponse.getFaildResp("AI执行推理任务返回异常");
        }
    }

    /**
     * 试接口-手动执行推理任务
     *
     * @param aiTaskIds 任务id
     * @param endDate   任务结束时间
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse testHandelExec(String aiTaskIds, String endDate) {
        try {
            HandleAITaskExecReqVO reqVO = new HandleAITaskExecReqVO();
            // 活动结束时间
            Integer updateCycle = 3;
            reqVO.setId(aiTaskIds);
            if (ConstantDNA.CUSTGROUP_DAY_CYCLE.equals(updateCycle)) {
                reqVO.setDateType(ZERO_NUMBER);
            }
            if (ConstantDNA.CUSTGROUP_MONTH_CYCLE.equals(updateCycle)) {
                reqVO.setDateType(TWO_NUMBER);
            }
            reqVO.setTaskFailTime(endDate);
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
        return ActionResponse.getSuccessResp();
    }

    /**
     * 获取该产品最近5个活动的成功用户数和转化率
     *
     * @param query 产品id
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse getCampEvalTop5ByPlan(McdIdQuery query) {
        try {
            List<Map<String, Object>> campEvalTop5ByPlan = intellPushPrismAIDao.getCampEvalTop5ByPlan(query.getId());
            return ActionResponse.getSuccessResp(campEvalTop5ByPlan);
        } catch (Exception e) {
            log.error("获取该产品最近5个活动的成功用户数和转化率异常：", e);
            return ActionResponse.getFaildResp("获取该产品最近5个活动的营销数据异常");
        }
    }

    /**
     * 获取该产品AI画像标签TOP10
     *
     * @param query 产品id
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse getAITagTop10ByPlan(McdIdQuery query) {
        try {
            List<AITagTop10ByPlanRespVO> respVOList = new ArrayList<>();
            DecimalFormat df = new DecimalFormat("0.0000");
            // 1. 根据产品id查询所有的标签id
            List<Map<String, Object>> planTagIdsByPlanId = intellPushPrismAIDao.getPlanTagIdsByPlanId(query.getId());
            for (Map<String, Object> map : planTagIdsByPlanId) {
                List<AITagTop10ByPlanRespVO.TagVO> labelVOList = new ArrayList<>();
                AITagTop10ByPlanRespVO respVO = new AITagTop10ByPlanRespVO();
                respVO.setTagName(String.valueOf(map.get("tagName")));
                // 2. 根据产品id、标签id查询关联度
                Map<String, Object> correlationByTagIdAndPlanId = intellPushPrismAIDao.getCorrelationByTagIdAndPlanId(String.valueOf(map.get("tagId")), query.getId());
                respVO.setCorrelation(Double.parseDouble(String.valueOf(correlationByTagIdAndPlanId.get("correlation"))));
                // 3. 根据产品id、标签id查询标签值
                List<Map<String, Object>> tagValues = intellPushPrismAIDao.getTagValueByTagIdAndPlanId(String.valueOf(map.get("tagId")), query.getId());
                for (Map<String, Object> tagValue : tagValues) {
                    AITagTop10ByPlanRespVO.TagVO tagVO = new AITagTop10ByPlanRespVO.TagVO();
                    tagVO.setTagValue(String.valueOf(tagValue.get("tagValue")));
                    tagVO.setTagValueCn(String.valueOf(tagValue.get("tagValueCn")));
                    tagVO.setProportion(new BigDecimal(df.format(tagValue.get("proportion"))));
                    labelVOList.add(tagVO);
                }
                respVO.setLabelVOList(labelVOList);
                respVOList.add(respVO);
            }
            // 4. 根据关联度集合降序排列且取前10条数据返回
            respVOList = respVOList.stream()
                    .sorted(Comparator.comparingDouble(AITagTop10ByPlanRespVO::getCorrelation).reversed())
                    .limit(10)
                    .collect(Collectors.toList());
            return ActionResponse.getSuccessResp(respVOList);
        } catch (Exception e) {
            log.error("获取该产品AI画像标签TOP10异常：", e);
            return ActionResponse.getFaildResp("获取该产品AI画像标签TOP10失败");
        }
    }

    /**
     * 处理智推棱镜AI推理场景产品标签关联度数据
     *
     * @param request 入参信息
     */
    @Override
    public void dealPlanTagRelDegree(JSONObject request) {
        log.info("dealPlanTagRelDegree-->开始");
        try {
            List<McdPrismPlanTagRelDegreeVO> degreeVOS = new ArrayList<>();
            // 1. 查询所有产品id、标签id
            List<Map<String, Object>> allTagIdPlanId = intellPushPrismAIDao.getAllTagIdPlanId();
            log.info("dealPlanTagRelDegree-->allTagIdPlanId={}", JSONUtil.toJsonStr(allTagIdPlanId));
            for (Map<String, Object> map : allTagIdPlanId) {
                McdPrismPlanTagRelDegreeVO degreeVO = new McdPrismPlanTagRelDegreeVO();
                // 2. 根据产品id、标签id查询并计算关联度
                Map<String, Object> calCorrelation = intellPushPrismAIDao.calCorrelation(String.valueOf(map.get("tagId")), String.valueOf(map.get("planId")));
                degreeVO.setDataDate(String.valueOf(calCorrelation.get("dataDate")));
                degreeVO.setPlanId(String.valueOf(calCorrelation.get("planId")));
                degreeVO.setTagId(String.valueOf(calCorrelation.get("tagId")));
                degreeVO.setTagName(String.valueOf(calCorrelation.get("tagName")));
                degreeVO.setCorrelation(Double.parseDouble(String.valueOf(calCorrelation.get("correlation"))));
                degreeVOS.add(degreeVO);
            }
            if (CollectionUtil.isNotEmpty(degreeVOS)) {
                // 获取所有的产品id
                Set<String> planIds = degreeVOS.stream().map(McdPrismPlanTagRelDegreeVO::getPlanId).collect(Collectors.toSet());
                // 3. 删除该产品下原有历史数据
                intellPushPrismAIDao.deleteBatchDegreeInfo(planIds);
                // 4. 保存关联度数据
                intellPushPrismAIDao.saveBatchDegreeInfo2DB(degreeVOS);
            }
        } catch (Exception e) {
            log.error("dealPlanTagRelDegree-->异常：", e);
        }
        log.info("dealPlanTagRelDegree-->结束");
    }

    /**
     * 查询AI客群瘦身分析结果
     *
     * @param query 产品id
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse queryAICustLoseWeightRes(AICustLoseWeightReqVO query) {
        DecimalFormat df = new DecimalFormat("0.0000");
        List<AICustLoseWeightResVO> finallResVOS = new ArrayList<>();
        // 原始客群或标签值集合
        List<AICustLoseWeightResVO> originResVOS = new ArrayList<>();
        // 扩展的标签值集合
        List<AICustLoseWeightResVO> extendResVOS = new ArrayList<>();
        try {
            // a. 传入标签数据计算
            // 计算传入标签的关联度数据
            for (int i = 0; i < query.getTreeDetails().size(); i++) {
                AICustLoseWeightResVO resVO = new AICustLoseWeightResVO();
                AICustLoseWeightReqVO.TreeDetails treeDetail = query.getTreeDetails().get(i);
                // 所有的标签值id
                Set<String> collect = treeDetail.getSelectedConditions().stream().map(AICustLoseWeightReqVO.SelectedConditions::getStrValue).collect(Collectors.toSet());
                if ("=".equals(treeDetail.getCalType())) {
                    // 根据产品id、columnNum、标签值查询 PROPORTION, ALL_PROPORTION 计算关联度
                    Map<String, Object> correlationOnOriginTag = intellPushPrismAIDao.getCorrelationOnOriginTag(query.getPlanId(), String.valueOf(treeDetail.getColumnNum()), collect);
                    if (CollectionUtil.isEmpty(correlationOnOriginTag)) {
                        log.warn("【原始选择标签】产品={}，columnNum={}未查询出标签数据", query.getPlanId(), treeDetail.getColumnNum());
                        continue;
                    }
                    originTagDeal(df, originResVOS, resVO, treeDetail, correlationOnOriginTag);
                } else {
                    // != 情况
                    // 查询产品id、columnNum且不等于当前标签值的所有标签信息
                    Map<String, Object> correlationOnOriginTag = intellPushPrismAIDao.getCorrelationOnNotEq(query.getPlanId(), String.valueOf(treeDetail.getColumnNum()), collect);
                    if (CollectionUtil.isEmpty(correlationOnOriginTag)) {
                        log.warn("【原始选择标签】产品={}，columnNum={}未查询出标签数据", query.getPlanId(), treeDetail.getColumnNum());
                        continue;
                    }
                    originTagDeal(df, originResVOS, resVO, treeDetail, correlationOnOriginTag);
                }
            }
            finallResVOS.addAll(originResVOS);

            // b. 新增加标签逻辑处理
            // 1. 根据产品id查询表中所有的标签id（dna映射的id）
            List<Map<String, Object>> planTagIdsByPlanId = intellPushPrismAIDao.getPlanTagIdsByPlanId(query.getPlanId());
            List<String> dbColumnIds = planTagIdsByPlanId.stream().map(v -> String.valueOf(v.get("columnNum"))).collect(Collectors.toList());
            // 2. 获取传入的所有标签id
            List<Integer> inColumnIds = query.getTreeDetails().stream().map(AICustLoseWeightReqVO.TreeDetails::getColumnNum).collect(Collectors.toList());
            // Integer-->String
            List<String> collect = inColumnIds.stream().map(String::valueOf).collect(Collectors.toList());
            // 3. dbColumnIds - inColumnIds 表中的标签id剔除调界面上已选择的标签
            List<String> afterFilterColumnIds = dbColumnIds.stream().filter(item -> !collect.contains(item)).collect(Collectors.toList());
            // 4. 循环处理每个标签
            for (String columnNum : afterFilterColumnIds) { // 每个标签
                // 根据过滤后的columnNum、产品id查询标签信息
                List<Map<String, Object>> tagValues = intellPushPrismAIDao.getTagValueByColumnIdAndPlanId(columnNum, query.getPlanId());
                if (CollectionUtil.isEmpty(tagValues)) {
                    log.warn("【新增加标签逻辑处理】产品={}，columnNum={}未查询出标签数据", query.getPlanId(), columnNum);
                    continue;
                }
                for (Map<String, Object> tagValue : tagValues) { // 当前循环标签下的每个标签值
                    AICustLoseWeightResVO resVO = new AICustLoseWeightResVO();
                    // 分别计算此标签下每个标签值的关联度
                    BigDecimal proportion = new BigDecimal(df.format(tagValue.get("PROPORTION")));
                    BigDecimal allProportion = new BigDecimal(df.format(tagValue.get("ALL_PROPORTION")));
                    BigDecimal subtract;
                    if (!allProportion.toString().equals("0.0000")) {
                        BigDecimal divide = proportion.divide(allProportion, 4, BigDecimal.ROUND_HALF_UP);
                        // （关联度-100） / 100   由于计算出来的关联度没有划算成百分比 所以只需减1即可
                        subtract = divide.subtract(new BigDecimal(1));
                    } else {
                        subtract = new BigDecimal(0);
                    }
                    // 减1后与0比较大小
                    int result = subtract.compareTo(new BigDecimal(0));
                    if (result <= 0) { // （divide * 100）-100 <= 0
                        // 走到这个里面  说明 说明关联度小于100  进行下一次循环
                        log.warn("PLAN_ID={},TAG_ID={},TAG_VALUE={},COLUMN_NUM={}的标签关联度小于等于0不计入", query.getPlanId(), tagValue.get("TAG_ID"), tagValue.get("TAG_VALUE"), columnNum);
                        continue;
                    } else {
                        resVO.setId(columnNum + StrUtil.UNDERLINE + tagValue.get("TAG_NAME") + StrUtil.UNDERLINE + tagValue.get("TAG_VALUE"));
                        resVO.setColumnNum(columnNum);
                        resVO.setColumnName(String.valueOf(tagValue.get("TAG_NAME")));
                        // todo 测试时需要改为!=
                        if (StrUtil.equals("dev", profiles)) {
                            resVO.setOutCalType("!=");
                        } else {
                            resVO.setOutCalType("=");
                        }
                        // 标签值需要请求dna接口获取dna侧标签映射值
                        ColumnValuePageQuery columnValuePageQuery = new ColumnValuePageQuery();
                        columnValuePageQuery.setColumnNum(columnNum);
                        columnValuePageQuery.setCurrent(1);
                        columnValuePageQuery.setSize(100000); // 数据设置大点  模糊匹配全量数据
                        columnValuePageQuery.setKeyWord(String.valueOf(tagValue.get("TAG_VALUE_CN")));
                        DNAActionResponse<ColumnValuePageRespondDTO> dnaActionResponse = dnaColumnService.getValuePage(columnValuePageQuery.transToColumnValuePageRequestDTO());
                        if (DNAResponseStatus.SUCCESS.getCode().equals(dnaActionResponse.getCode())) {
                            List<ColumnValuePageRespondDTO.ValueList> valueList = dnaActionResponse.getData().getValueList();
                            log.info("营销画布获取标签映射值成功.标签编码={},标签值={},响应={}", columnValuePageQuery.getColumnNum(), tagValue.get("TAG_VALUE_CN"), JSONUtil.toJsonStr(valueList));
                            if (CollectionUtil.isNotEmpty(valueList)) { // 反查询dna标签映射值不为空 才算
                                for (ColumnValuePageRespondDTO.ValueList list : valueList) {
                                    List<Map<String, String>> extendTagValueList = new ArrayList<>();
                                    Map<String, String> tagMap = new HashMap<>();
                                    if (list.getStrValue().equals(String.valueOf(tagValue.get("TAG_VALUE"))) // 用dna标签值(匹配coc标签值(MCD_PRISM_PLAN_TAG表中TAG_VALUE标签值)
                                    && list.getRemark().equals(String.valueOf(tagValue.get("TAG_VALUE_CN")))) { // 用dna标签值(中文)(匹配coc标签值(MCD_PRISM_PLAN_TAG表中TAG_VALUE_CN标签值) 两者都满足情况下，才可以
                                        tagMap.put("value", list.getStrValue());
                                        tagMap.put("valueCn", list.getRemark());
                                        tagMap.put("condition", list.getCondition());
                                        extendTagValueList.add(tagMap);
                                        resVO.setTagValue(extendTagValueList);
                                    }
                                }
                                resVO.setIsForwardDire(1);
                                resVO.setCorrelation(subtract.multiply(new BigDecimal(df.format(100))));
                                resVO.setIsExtend(1);
                                if (CollectionUtil.isEmpty(resVO.getTagValue())) {
                                    log.warn("标签编码={},标签值={}未查询到dna侧标签映射值", columnNum, tagValue.get("TAG_VALUE_CN"));
                                    continue;
                                }
                                extendResVOS.add(resVO);
                            }
                        }
                    }
                }
            }
            finallResVOS.addAll(extendResVOS);
        } catch (Exception e) {
            log.error("查询AI客群瘦身分析结果异常：", e);
            return ActionResponse.getFaildResp("查询AI客群瘦身分析结果失败");
        }
        return ActionResponse.getSuccessResp(finallResVOS);
    }

    /**
     * 客群清单文件生成通知接口
     *
     * @param request 客群id
     */
    @Override
    public void dnaCustFileGeneratedNotice(JSONObject request) {
        try {
            long start = System.currentTimeMillis();
            String custGroupId = request.getStr("custGroupId");
            log.info("dnaCustFileGeneratedNotice-->客群清单文件生成通知开始");
            McdCustgroupDef byId = custgroupDefService.getById(custGroupId);
            if (ObjectUtil.isEmpty(byId)) {
                log.warn("客群={}未保存客群定义表，不需要更新清单文件", custGroupId);
                return;
            }
            // 2. 调用dna接口1.4 生成客群清单文件
            // 3.下载清单到本地
            Map<String, String> map = idnaCustomGroupService.dowloadCustFile(custGroupId);
            if (ObjectUtil.isEmpty(map)) {
                log.warn("客群={}调用dna计算接口获取清单文件为空！", custGroupId);
                return;
            }
            // 4. 将获取到的清单文件按照coc文件格式命名  MCD_GROUP_客群id_数据日期.txt  MCD_GROUP_客群id_数据日期.CHK
            // 4.1 获取清单文件名
            String formatFileName = String.format(ConstantDNA.DNA_2_COC_CUSTOMGROUP_FILENAME, custGroupId, DateUtil.format(new Date(), "yyyyMMdd"));
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
            // 7. 活动保存时，已异步去请求dna接口获取清单文件，获取不到时，已将客群的基本信息保存到定义表中(除清单文件外)
            //    此处只需更新清单文件名、客群数量到mcd_custgroup_def表中即可
            custgroupDefService.update(Wrappers.<McdCustgroupDef>update().lambda()
                    .set(McdCustgroupDef::getFileName, formatFileName)
                    .set(McdCustgroupDef::getCustomNum, Integer.parseInt(map.get("count")))
                    .set(McdCustgroupDef::getActualCustomNum, Integer.parseInt(map.get("count")))
                    .eq(McdCustgroupDef::getCustomGroupId, custGroupId));
            log.info("dnaCustFileGeneratedNotice-->客群清单文件生成通知结束，耗时={}秒", (System.currentTimeMillis() - start) / 1000);
        } catch (Exception e) {
            log.error("dnaCustFileGeneratedNotice-->客群清单文件生成通知IOP异常：", e);
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
     * 原始标签关联度处理逻辑
     *
     * @param df df
     * @param originResVOS 原始客群或标签值集合
     * @param resVO 查询AI客群瘦身分析结果响应对象
     * @param treeDetail 入参
     * @param correlationOnOriginTag 根据产品id、columnNum、标签值查询符合的标签信息
     */
    private void originTagDeal(DecimalFormat df, List<AICustLoseWeightResVO> originResVOS, AICustLoseWeightResVO resVO, AICustLoseWeightReqVO.TreeDetails treeDetail, Map<String, Object> correlationOnOriginTag) {
        // 多个标签值时  收集数据
        List<Map<String, String>> tagValueListAll = new ArrayList<>();
        BigDecimal proportion = new BigDecimal(df.format(correlationOnOriginTag.get("sumPro")));
        BigDecimal allProportion = new BigDecimal(df.format(correlationOnOriginTag.get("sumAllPro")));
        BigDecimal subtract;
        // 分母allProportion不为0时
        if (!allProportion.toString().equals("0.0000")) {
            BigDecimal divide = proportion.divide(allProportion, 4, BigDecimal.ROUND_HALF_UP);
            // （关联度-100） / 100 由于计算出来的关联度没有划算成百分比 所以只需减1即可
            subtract = divide.subtract(new BigDecimal(1));
        } else {
            subtract = new BigDecimal(0);
        }
        // 减1后与0比较大小
        int result = subtract.compareTo(new BigDecimal(0));
        resVO.setId(String.valueOf(treeDetail.getId()));
        resVO.setColumnNum(String.valueOf(treeDetail.getColumnNum()));
        resVO.setColumnName(String.valueOf(correlationOnOriginTag.get("TAG_NAME")));
        resVO.setOutCalType(treeDetail.getCalType());
        for (AICustLoseWeightReqVO.SelectedConditions selectedConditions : treeDetail.getSelectedConditions()) {
            List<Map<String, String>> tagValueList = new ArrayList<>();
            Map<String, String> tagMap = new HashMap<>();
            tagMap.put("value", selectedConditions.getStrValue());
            tagMap.put("valueCn", selectedConditions.getRemark());
            tagMap.put("condition", selectedConditions.getCondition());
            tagValueList.add(tagMap);
            tagValueListAll.addAll(tagValueList);
        }
        resVO.setTagValue(tagValueListAll);
        if (result <= 0) { // divide * 100 < 100
            // 反向
            resVO.setIsForwardDire(0);
            resVO.setCorrelation(subtract.multiply(new BigDecimal(100)));
        } else {
            // 正向
            resVO.setIsForwardDire(1);
            resVO.setCorrelation(subtract.multiply(new BigDecimal(df.format(100))));
        }
        resVO.setIsExtend(0);
        originResVOS.add(resVO);
    }

    /**
     * 接口响应成功时，构建返回实体对象
     *
     * @param finalVO 返回前端对象
     * @param respondVOS 接口响应实体
     * @param jsonObject 接口响应jsonObject
     * @return boolean
     */
    private boolean buildReturnVO(AITaskRespondFinalVO finalVO, List<AITaskRespondVO> respondVOS, JSONObject jsonObject) {
        long stat = System.currentTimeMillis();
        log.info("buildReturnVO-->构建返回实体对象开始");
        // 2. 任务id
        String taskId = jsonObject.getStr("data");
        // 3. 产品对应的推理客群、渠道以及渠道配置信息
        JSONArray jsonArray = JSONUtil.parseArray(jsonObject.get("relation"));
        // 3.1 jsonArray转集合
        List<AISaveTaskResVO> taskResVOS = JSONUtil.toList(jsonArray, AISaveTaskResVO.class);
        if (CollectionUtil.isEmpty(taskResVOS)) {
            log.warn("AI执行推理任务未返回匹配数据");
            return true;
        }
        // 3.2 获取所有的产品id
        Set<String> planIdList = taskResVOS.stream().map(AISaveTaskResVO::getPlanId).collect(Collectors.toSet());
        // 获取所有的渠道id
        // Set<String> channelIdList = taskResVOS.stream().map(AISaveTaskResVO::getChannelId).collect(Collectors.toSet());
        // 根据产品id查询近30天的活动效果数据
        List<Map<String, Object>> thirtyDaysCampEvalByPlandId = intellPushPrismAIDao.getThirtyDaysCampEvalByPlandId(planIdList);
        // 3.3 根据产品id批量查询可参与智推的产品信息
        List<McdPlanDef> mcdPlanDefList = mcdPlanDefService.list(Wrappers.<McdPlanDef>query().lambda().eq(McdPlanDef::getIsSupportAi, 1).in(McdPlanDef::getPlanId, planIdList));
        // 3.4 根据产品id分组
        Map<String, List<McdPlanDef>> mcdPlanDefListMap = mcdPlanDefList.stream().collect(Collectors.groupingBy(McdPlanDef::getPlanId));
        // 此产品对应的近30天没有活动效果数据
        if (CollectionUtil.isEmpty(thirtyDaysCampEvalByPlandId)) {
            log.warn("产品={}没有查询到近30天的效果数据", JSONUtil.toJsonStr(planIdList));
            if (CollectionUtil.isNotEmpty(taskResVOS) && CollectionUtil.isNotEmpty(mcdPlanDefListMap)) {
                for (AISaveTaskResVO taskResVO : taskResVOS) {
                    // 产品、客群、渠道响应封装
                    AITaskRespondVO respondVO = new AITaskRespondVO();
                    // 产品信息
                    AITaskRespondPlanVO planVO = new AITaskRespondPlanVO();
                    // 客群信息
                    AITaskRespondCustVO custVO = new AITaskRespondCustVO();
                    // 渠道信息
                    AITaskRespondChannelVO channelVO = new AITaskRespondChannelVO();
                    buildPlanAndCustAndChannelInfo(mcdPlanDefListMap, taskResVO, respondVO, planVO, custVO, channelVO);
                    // 当前产品+渠道近30天没有活动信息  和前端约定扩展字段1空串返回==>表示渠道信息需要用户自己编辑配置
                    channelVO.setChannelConfig(new PrismChannelConfigVO(""));
                    respondVO.setChannelVO(channelVO);
                    respondVOS.add(respondVO);
                }
            }
        } else {
            // 获取所有的活动子id
            Set<String> campsegIdList = thirtyDaysCampEvalByPlandId.stream().map(item -> item.get("CAMPSEG_ID").toString()).collect(Collectors.toSet());
            // 产品id分组 产品id对应的活动id
            // Map<String, List<Map<String, Object>>> planIdCampsegRootIdMap = thirtyDaysCampEvalByPlandId.stream().collect(Collectors.groupingBy(item -> item.get("PLAN_ID").toString()));
            // 根据活动id查询扩展表数据
            List<PrismChannelConfigVO> extByCampsegRootIdAndChannelId = intellPushPrismAIDao.getExtByCampsegRootIdAndChannelId(campsegIdList);
            // 根据产品id、渠道id对数据分组
            Map<String, List<PrismChannelConfigVO>> collectByRootIdAndChannelId = extByCampsegRootIdAndChannelId.stream().collect(
                    Collectors.groupingBy(i -> i.getPlanId() + StrUtil.UNDERLINE + i.getChannelId(), Collectors.toList()));
            if (CollectionUtil.isNotEmpty(taskResVOS) && CollectionUtil.isNotEmpty(mcdPlanDefListMap)) {
                for (AISaveTaskResVO taskResVO : taskResVOS) {
                    // 产品、客群、渠道响应封装
                    AITaskRespondVO respondVO = new AITaskRespondVO();
                    // 产品信息
                    AITaskRespondPlanVO planVO = new AITaskRespondPlanVO();
                    // 客群信息
                    AITaskRespondCustVO custVO = new AITaskRespondCustVO();
                    // 渠道信息
                    AITaskRespondChannelVO channelVO = new AITaskRespondChannelVO();
                    buildPlanAndCustAndChannelInfo(mcdPlanDefListMap, taskResVO, respondVO, planVO, custVO, channelVO);
                    // 根据渠道id获取配置信息
                    List<PrismChannelConfigVO> prismChannelConfigVOS = collectByRootIdAndChannelId.get(taskResVO.getPlanId() + StrUtil.UNDERLINE + taskResVO.getChannelId());
                    // 当前产品+渠道近30天有营销活动 所以取其对应扩展表的运营位配置信息
                    if (CollectionUtil.isNotEmpty(prismChannelConfigVOS)) {
                        channelVO.setChannelConfig(prismChannelConfigVOS.get(ZERO_NUMBER));
                    } else {
                        // 当前产品+渠道近30天没有活动信息  和前端约定扩展字段1空串返回==>表示渠道信息需要用户自己编辑配置
                        channelVO.setChannelConfig(new PrismChannelConfigVO(""));
                    }
                    respondVO.setChannelVO(channelVO);
                    respondVOS.add(respondVO);
                }
            }
        }
        finalVO.setAITaskId(taskId);
        finalVO.setRespondVOList(respondVOS);
        log.info("buildReturnVO-->构建返回实体对象结束={}，耗时={}秒", System.currentTimeMillis(), (System.currentTimeMillis() - stat) / 1000);
        return false;
    }

    /**
     * 构建产品、客群、渠道基础信息
     *
     * @param mcdPlanDefListMap 支持ai的产品信息
     * @param taskResVO 推理任务信息
     * @param respondVO 响应实体
     * @param planVO 产品信息
     * @param custVO 客群信息
     * @param channelVO 渠道信息
     */
    private void buildPlanAndCustAndChannelInfo(Map<String, List<McdPlanDef>> mcdPlanDefListMap, AISaveTaskResVO taskResVO, AITaskRespondVO respondVO, AITaskRespondPlanVO planVO,
                                                AITaskRespondCustVO custVO, AITaskRespondChannelVO channelVO) {
        planVO.setPlanId(taskResVO.getPlanId());
        planVO.setPlanName(mcdPlanDefListMap.get(taskResVO.getPlanId()).get(CommonConstant.SpecialNumber.ZERO_NUMBER).getPlanName());
        planVO.setPlanDesc(mcdPlanDefListMap.get(taskResVO.getPlanId()).get(CommonConstant.SpecialNumber.ZERO_NUMBER).getPlanDesc());
        planVO.setPlanStartDate(mcdPlanDefListMap.get(taskResVO.getPlanId()).get(CommonConstant.SpecialNumber.ZERO_NUMBER).getPlanStartDate());
        planVO.setPlanEndDate(mcdPlanDefListMap.get(taskResVO.getPlanId()).get(CommonConstant.SpecialNumber.ZERO_NUMBER).getPlanEndDate());
        respondVO.setPlanVO(planVO);
        custVO.setCustId(taskResVO.getCustId());
        custVO.setCustName(taskResVO.getCustName());
        custVO.setCustNum(taskResVO.getCustNum());
        respondVO.setCustVO(custVO);
        channelVO.setChannelId(taskResVO.getChannelId());
        channelVO.setChannelName(taskResVO.getChannelName());
    }
}
