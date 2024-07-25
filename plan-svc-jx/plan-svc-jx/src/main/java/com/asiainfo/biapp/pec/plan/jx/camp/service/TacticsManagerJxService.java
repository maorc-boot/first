package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.model.CampManageJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.*;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.ApprRecordJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCampCmpApproveProcessRecordJx;
import com.asiainfo.biapp.pec.plan.vo.CampManage;
import com.asiainfo.biapp.pec.plan.vo.req.SearchTacticsActionQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author mamp
 * @date 2022/11/9
 */
public interface TacticsManagerJxService {
    /**
     * 营销活动列表
     *
     * @param form
     * @return
     */
    IPage<CampManage> searchIMcdCamp(SearchTacticsActionQuery form);

    /**
     * 我的策略和全部策略列表分页查询
     *
     * @param form
     * @return
     */
    IPage<CampManage> searchIMcdCampJx(SearchTacticsActionQueryJx form);

    List<CampManage> queryMcdSubCampList(McdSubCampByIdQuery req);

    /**
     * 我的策略和全部策略导出
     *
     * @param form
     */
    void exportCampJx(SearchTacticsActionQueryJx form, HttpServletResponse response);

    /**
     * 我参与的策略
     *
     * @param form
     * @return
     */
    IPage<CampManageJx> searchIMcdMyParticipationCamp(SearchMyParticipationTacticsQuery form);

    void exportIMcdMyParticipationCamp(SearchMyParticipationTacticsQuery form, HttpServletResponse response);


    /**
     * 营销审批列表
     *
     * @param req
     * @return
     */
    IPage<ApprRecordJx> approveRecord(McdCampApproveJxQuery req);

    IPage<McdCampCmpApproveProcessRecordJx> approveRecordNew(McdCampApproveJxNewQuery req);


    /**
     * 复制活动
     *
     * @param sourceCampId
     * @param newCampName
     * @param user
     * @param flag
     * @return
     */
    String copyCamp(String sourceCampId, String newCampName, UserSimpleInfo user, String flag);


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
     * 删除活动
     *
     * @param id
     */
    void deleteCampsegInfo(String id);


    /**
     * 一键审批
     * @param campsegRootId
     */
     void quickApprove(String campsegRootId);

    /**
     * 策略延期
     *
     * @param campsegPid
     * @param endDate
     */
    void updateCampsegEndDate(String campsegPid, Date endDate);
    
    /**
     * 主题策略导出(我的策略和全部策略)
     * @param form
     * @param response
     */
    void exportThemeCamp(CampThemeQuery form, HttpServletResponse response);
}
