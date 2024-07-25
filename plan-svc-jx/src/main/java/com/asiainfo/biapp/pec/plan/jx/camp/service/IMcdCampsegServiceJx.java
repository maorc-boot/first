package com.asiainfo.biapp.pec.plan.jx.camp.service;


import com.alibaba.fastjson.JSONObject;
import com.asiainfo.biapp.pec.core.enums.CampStatus;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampExcuteReq;
import com.asiainfo.biapp.pec.plan.jx.camp.req.HisCampInfoReq;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.HisCampInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : ranpf
 * @date : 2022-10-09 14:31:05
 * 营销策略处理服务
 */
public interface IMcdCampsegServiceJx {


    /**
     * 检查策略名称是否存在
     *
     * @param campsegName 策略名称
     * @param campsegId   策略id非必填
     * @return 更新成功则返回true否则返回false
     * @throws Exception 业务异常
     */
    boolean checkTactics(String campsegName, String campsegId);

    /**
     * 检查策略名称是否存在，并返回具体重复信息
     *
     * @param campsegName 策略名称
     * @param campsegId 策略id非必填
     * @return {@link Map}<{@link Object}, {@link Object}>
     */
    Map<Object, Object> checkTacticsDetail(String campsegName, String campsegId);


    /**
     * 保存活动信息
     *
     * @param req
     * @return
     */
    String saveCamp(TacticsInfoJx req, UserSimpleInfo user);


    /**
     * 验证活动结束时间
     *
     * @param req
     */
    void checkCampEndDate(TacticsInfoJx req);


    /**
     * 内容备份到产品库
     *
     * @param planIds
     */
    void saveDigitalContent2Plan(Set<String> planIds);


    /**
     * 更新CEP状态
     *
     * @param id
     * @param isRoot
     * @param status
     */
    void updateCEPEventId(String id, boolean isRoot, CampStatus status);


    /**
     * 根据客户群ID和渠道ID查询历史活动
     * @return
     */
    IPage<List<HisCampInfo>> queryHisCamp(HisCampInfoReq req);

    JSONObject getCampExcuteInfos(CampExcuteReq req);

    /**
     * 查询模板
     * @return
     */
    List<Map<String, Object>> queryIopTemplate(@Param("userId")String userId);

    /**
     * 查询活动场景配置信息
     *
     * @return {@link Map}<{@link String}, {@link List}<{@link Map}<{@link String}, {@link Object}>>>
     */
    Map<String, List<Map<String, Object>>> queryCampScene();
}