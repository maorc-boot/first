package com.asiainfo.biapp.pec.plan.jx.intellpushprism.service;

import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna.LabelFissionGetCustIdReqDTO;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.*;

/**
 * description: 智推棱镜-AI流程service
 *
 * @author: lvchaochao
 * @date: 2024/5/25
 */
public interface IIntellPushPrismAIService {

    /**
     * AI推理-获取根据标签裂变的客群信息
     *
     * @param reqVO 标签裂变获取客群编号接口请求参数对象
     * @return {@link ActionResponse}
     */
    ActionResponse getFissionCustInfo(LabelFissionGetCustIdReqDTO reqVO);

    /**
     * 智慧大脑产品同步IOP
     *
     * @param reqVO 同步的产品信息
     */
    void syncAIProduct(SyncAIProductReqVO reqVO);

    /**
     * 客群打分区间数据查询
     *
     * @param reqVO 客群打分区间数据查询入参对象
     * @return {@link ActionResponse}
     */
    ActionResponse queryScoreRange(QueryScoreRangeReqVO reqVO);

    /**
     * 客群打分区间数据批量查询
     *
     * @param reqVO 客群打分区间数据批量查询入参对象
     * @return {@link ActionResponse}
     */
    ActionResponse queryScoreRangeBatch(QueryScoreRangeReqBatchVO reqVO);

    /**
     * 智慧大脑推荐客群回调
     *
     * @param reqVO 智慧大脑推荐客群回调入参对象
     * @return {@link ActionResponse}
     */
    ActionResponse callbackTask(CallbackTaskReqVO reqVO);

    /**
     * AI执行推理任务
     *
     * @param reqVO AI执行推理任务入参对象
     * @return {@link ActionResponse}
     */
    ActionResponse getInferenceResByAI(AISaveTaskReqVO reqVO);


    /**
     * 试接口-手动执行推理任务
     *
     * @param aiTaskIds 任务id
     * @param endDate 任务结束时间
     * @return {@link ActionResponse}
     */
    ActionResponse testHandelExec(String aiTaskIds, String endDate);

    /**
     * 获取该产品最近5个活动的成功用户数和转化率
     *
     * @param query 产品id
     * @return {@link ActionResponse}
     */
    ActionResponse getCampEvalTop5ByPlan(McdIdQuery query);

    /**
     * 获取该产品AI画像标签TOP10
     *
     * @param query 产品id
     * @return {@link ActionResponse}
     */
    ActionResponse getAITagTop10ByPlan(McdIdQuery query);

    /**
     * 处理智推棱镜AI推理场景产品标签关联度数据
     *
     * @param request 入参信息
     */
    void dealPlanTagRelDegree(JSONObject request);

    /**
     * 查询AI客群瘦身分析结果
     *
     * @param query 产品id
     * @return {@link ActionResponse}
     */
    ActionResponse queryAICustLoseWeightRes(AICustLoseWeightReqVO query);

    /**
     * 客群清单文件生成通知接口
     *
     * @param request 客群id
     */
    void dnaCustFileGeneratedNotice(JSONObject request);
}
