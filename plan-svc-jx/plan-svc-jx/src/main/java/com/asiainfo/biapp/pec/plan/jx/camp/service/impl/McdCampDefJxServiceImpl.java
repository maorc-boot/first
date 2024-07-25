package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;


import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.plan.dao.McdCampChannelListDao;
import com.asiainfo.biapp.pec.plan.dao.McdCampDefDao;
import com.asiainfo.biapp.pec.plan.dao.McdCampStepMonitorDao;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampDefJxDao;
import com.asiainfo.biapp.pec.plan.jx.camp.enums.CampStatusJx;
import com.asiainfo.biapp.pec.plan.jx.camp.model.CampManageJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdCampApproveJxQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdSubCampByIdQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SearchMyParticipationTacticsQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SearchTacticsActionQueryJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampDefJxService;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.ApprRecordJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CampExportVO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.MyMCDJxVO;
import com.asiainfo.biapp.pec.plan.jx.hisdata.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.vo.CampManage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 策略定义表 服务实现类
 * </p>
 *
 * @author imcd
 * @since 2022-12-27
 */
@Service
public class McdCampDefJxServiceImpl extends ServiceImpl<McdCampDefJxDao, McdCampDef> implements IMcdCampDefJxService {

    @Resource
    private McdCampDefJxDao mcdCampDefJxDao;
    @Resource
    private McdCampChannelListDao mcdCampChannelListDao;

    @Resource
    private McdCampDefDao campDefDao;

    @Resource
    private McdCampStepMonitorDao campStepMonitorDao;

    @Override
    public List<MyMCDJxVO> listMyMCD(String userId) {
        List<MyMCDJxVO> myMCDVOIPage = mcdCampDefJxDao.listMyMCD(userId);
        return myMCDVOIPage;
    }



    @Override
    public List<ApprRecordJx> qryApprRecord(Set<String> campsegIds, McdCampApproveJxQuery req) {
        return mcdCampDefJxDao.qryApprRecord(campsegIds, req);
    }


    @Override
    public IPage<CampManageJx> searchIMcdMyParticipationCamp(SearchMyParticipationTacticsQuery form){

        Page pager = new Page(form.getCurrent(), form.getSize());
        IPage<CampManageJx> campManagePage = mcdCampDefJxDao.pageCampDefJx(pager, form);
        campManagePage.getRecords().forEach(campManage -> {
            campManage.setChannelIds(mcdCampChannelListDao.listChannelIdByCampsegId(campManage.getCampsegId()));
        });

        return campManagePage;
    }

    @Override
    public List<CampManageJx> exportIMcdMyParticipationCamp(SearchMyParticipationTacticsQuery form) {
        List<CampManageJx> campManageList = mcdCampDefJxDao.exportCampDefJxList(  form);
        campManageList.forEach(campManage -> {
            campManage.setChannelIds(mcdCampChannelListDao.listChannelIdByCampsegId(campManage.getCampsegId()));
        });

        return campManageList;
    }

    /**
     * 我的策略和全部策略列表查询
     * @param form
     * @return
     */
    @Override
    public IPage<CampManage> jxPageCampDef(SearchTacticsActionQueryJx form) {
        Page pager = new Page(form.getCurrent(), form.getSize());
        // 构造起止时间的起止时间段参数
        buildDateFilterParam(form);
        IPage<CampManage> campManageJxIPage = mcdCampDefJxDao.jxPageCampDef(pager, form);
        campManageJxIPage.getRecords().forEach(campManage -> {
            // 设置活动的所有渠道信息 包括多波次的
            campManage.setChannelIds(mcdCampDefJxDao.listChannelIdByCampsegId(campManage.getCampsegId()));
            // 修改策略状态
            campManage.setCampsegStatName(CampStatusJx.valueOfId(campManage.getCampsegStatId()));
        });
        return campManageJxIPage;
    }

    @Override
    public List<CampManage> queryMcdSubCampList(McdSubCampByIdQuery query) {

        List<CampManage> campManageJxIPage = mcdCampDefJxDao.queryMcdSubCampList( query.getUserId(),query.getCampsegRootId());

        return campManageJxIPage;
    }

    @Override
    public List<CampExportVO > exportJxPageCampDef(SearchTacticsActionQueryJx form) {
        // 构造起止时间的起止时间段参数
        buildDateFilterParam(form);
        List<CampExportVO > list = mcdCampDefJxDao.exportJxPageCampDef(form);
        return list;
    }

    /**
     * 构造起止时间的起止时间段参数
     *
     * @param form 入参对象
     */
    private void buildDateFilterParam(SearchTacticsActionQueryJx form) {
        if (form.getCampStartDay().contains(StrUtil.COMMA)) {
            form.setCampStartDayStart(form.getCampStartDay().split(StrUtil.COMMA)[0]);
            form.setCampStartDayEnd(form.getCampStartDay().split(StrUtil.COMMA)[1]);
        }
        if (form.getCampEndDay().contains(StrUtil.COMMA)) {
            form.setCampEndDayStart(form.getCampEndDay().split(StrUtil.COMMA)[0]);
            form.setCampEndDayEnd(form.getCampEndDay().split(StrUtil.COMMA)[1]);
        }
    }
}
