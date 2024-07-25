package com.asiainfo.biapp.pec.plan.jx.camp.service;


import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 一级精准推荐service
 *
 * @author mamp
 * @date 2021/4/23
 */
public interface IQuickTacticsService {

    /**
     * 通过集团模板快速创建活动
     *
     * @param templateId 模板ID
     * @param activityId 模板活动ID
     * @param user       当前用户
     * @return
     */
    String quickCreateTacticsByIopTemplate(String templateId, String activityId, UserSimpleInfo user) throws Exception;

    /**
     * 更改集团模板活动状态
     *
     * @param request HttpServletRequest
     * @return
     * @throws Exception
     */
    String changeActivityStatus(HttpServletRequest request) throws Exception;

    /**
     * 物料上下线通知接口
     *
     * @param campInfo   活动信息
     * @param notifyType 通知类型，1：上线 2：下线
     * @throws Exception
     */
    void materialNotify(Map<String, Object> campInfo, String notifyType) throws Exception;

    /**
     * 查询IOP_ACTIVITY_INFO表状态是执行中，但是MCD_CAMP_DEF 表是审批中的活动
     */
    void updateActivityStatus();


    void qucickApprove959Camp(String campsegRootId);

}
