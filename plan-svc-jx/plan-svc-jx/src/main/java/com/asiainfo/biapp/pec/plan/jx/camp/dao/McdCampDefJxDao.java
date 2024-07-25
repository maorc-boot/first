package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.plan.jx.camp.model.CampManageJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdCampApproveJxQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SearchMyParticipationTacticsQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SearchTacticsActionQueryJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.ApprRecordJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CampExportVO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.MyMCDJxVO;
import com.asiainfo.biapp.pec.plan.jx.hisdata.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.vo.CampManage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 策略定义表 Mapper 接口
 * </p>
 *
 * @author imcd
 * @since 2022-12-27
 */
@Mapper
public interface McdCampDefJxDao extends BaseMapper<McdCampDef> {

    /**
     * 首页获取我的营销案
     *
     * @param userId
     * @return
     */
    List<MyMCDJxVO> listMyMCD(@Param("userId") String userId);


    List<ApprRecordJx> qryApprRecord(@Param("camps")Set<String> campsegIds, @Param("param") McdCampApproveJxQuery req);

    /**
     *查询支持预演的渠道ID
     * @return
     */
    List<String> queryPreviewChannelIds();

    /**
     * 营销策划管理列表
     * @param page
     * @param param
     * @return
     */
    IPage<CampManageJx> pageCampDefJx(Page<?> page, @Param("param") SearchMyParticipationTacticsQuery param);


    List<CampManageJx> exportCampDefJxList(@Param("param") SearchMyParticipationTacticsQuery param);

    /**
     * 我的策略和全部策略列表查询
     * @param page
     * @param param
     * @return
     */
    IPage<CampManage> jxPageCampDef(Page<?> page, @Param("param") SearchTacticsActionQueryJx param);

    List<CampManage> queryMcdSubCampList(@Param("userId")String userId ,@Param("campsegRootId") String campsegRootId);

    /**
     * 我的策略和全部策略 导出
     * @param param
     * @return
     */
   List<CampExportVO > exportJxPageCampDef(@Param("param") SearchTacticsActionQueryJx param);


    /**
     * 获取活动所有配置的渠道
     *
     * @param campsegId 活动根id
     * @return 渠道集合
     */
    List<String> listChannelIdByCampsegId(@Param("campsegId") String campsegId);
}
