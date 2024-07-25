package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.model.CampManageJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdCampApproveJxQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdSubCampByIdQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SearchMyParticipationTacticsQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SearchTacticsActionQueryJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.ApprRecordJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CampExportVO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.MyMCDJxVO;
import com.asiainfo.biapp.pec.plan.jx.hisdata.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.vo.CampManage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 策略定义表 服务类
 * </p>
 *
 * @author imcd
 * @since 2021-11-11
 */
public interface IMcdCampDefJxService extends IService<McdCampDef> {


    /**
     * 我的营销案
     * @return
     */
    List<MyMCDJxVO> listMyMCD(String userId);


    /**
     * 获取审批记录
     * @param campsegIds
     * @param req
     * @return
     */
    List<ApprRecordJx> qryApprRecord(Set<String> campsegIds, McdCampApproveJxQuery  req);


    /**
     * 我参与的策略
     * @param form
     * @return
     */
    IPage<CampManageJx> searchIMcdMyParticipationCamp(SearchMyParticipationTacticsQuery form);

    /**
     * 我参与的策略 导出
     * @param form
     * @return
     */
    List<CampManageJx> exportIMcdMyParticipationCamp(SearchMyParticipationTacticsQuery form);

    /**
     * 我的策略和全部策略列表查询
     * @param query
     * @return
     */
    IPage<CampManage> jxPageCampDef(SearchTacticsActionQueryJx query);

    List<CampManage> queryMcdSubCampList(McdSubCampByIdQuery query);

    /**
     * 策略导出
     * @param param
     * @return
     */
    List<CampExportVO > exportJxPageCampDef(SearchTacticsActionQueryJx param);

}
