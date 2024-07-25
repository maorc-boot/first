package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.WorkbookUtil;
import com.asiainfo.biapp.client.pec.approve.model.RecordPageDTO;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.CampStatus;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.common.CampDefType;
import com.asiainfo.biapp.pec.plan.common.CampLogType;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.common.RedisDicKey;
import com.asiainfo.biapp.pec.plan.dao.*;
import com.asiainfo.biapp.pec.plan.jx.api.PecApproveFeignClient;
import com.asiainfo.biapp.pec.plan.jx.camp.enums.CampStatusJx;
import com.asiainfo.biapp.pec.plan.jx.camp.model.CampManageJx;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdFqcCycle;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdFqcRule;
import com.asiainfo.biapp.pec.plan.jx.camp.req.*;
import com.asiainfo.biapp.pec.plan.jx.camp.service.*;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.*;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.IMcdPrismCampThemeService;
import com.asiainfo.biapp.pec.plan.model.*;
import com.asiainfo.biapp.pec.plan.service.*;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.asiainfo.biapp.pec.plan.vo.CampManage;
import com.asiainfo.biapp.pec.plan.vo.req.SearchTacticsActionQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.asiainfo.biapp.pec.core.enums.CampStatus.EXECUTED;
import static com.asiainfo.biapp.pec.core.enums.CampStatus.PAUSE;
import static com.asiainfo.biapp.pec.core.enums.CampStatus.PENDING;
import static com.asiainfo.biapp.pec.core.enums.CampStatus.PUBLISHING;

/**
 * @author mamp
 * @date 2022/11/9
 */
@Service
@Slf4j
public class TacticsManagerJxServiceImpl implements TacticsManagerJxService {
    @Autowired
    private IMcdCampDefService campDefService;

    @Autowired
    private IMcdCampDefJxService mcdCampDefJxService;

    @Resource
    private PecApproveFeignClient pecApproveFeignClient;


    @Autowired
    private IMcdCampOperateLogService logService;


    @Autowired
    private IMcdCampChannelExtService extService;

    @Autowired
    private IMcdCampChannelListService campChannelListService;
    @Resource
    ICampPreveiwJxService campPreveiwJxService;

    @Resource
    private McdFqcCycleService fqcCycleService;

    @Resource
    private McdFqcRuleService fqcRuleService;

    @Resource
    private IMcdCampsegService mcdCampsegService;

    @Resource
    private McdCampTaskDao campTaskDao;

    @Resource
    private McdCampTaskDateDao campTaskDateDao;

    @Resource
    private McdCustgroupDefDao custgroupDefDao;

    @Resource
    private McdCampDefDao campDefDao;

    @Resource
    private McdCampChannelListDao mcdCampChannelListDao;

    @Resource
    private IMcdCustgroupDefService custgroupDefService;

    @Resource
    IMcdCampTaskService mcdCampTaskService;
    
    @Resource
    private IMcdPrismCampThemeService prismCampThemeService;


    /**
     * 营销活动列表
     *
     * @param form
     * @return
     */
    @Override
    public IPage<CampManage> searchIMcdCamp(SearchTacticsActionQuery form) {
        Page pager = new Page(form.getCurrent(), form.getSize());
        IPage<CampManage> campManagePage;
        if (StringUtils.isBlank(form.getPid())) {
            campManagePage = campDefDao.pageCampDef(pager, form);
            campManagePage.getRecords().forEach(campManage -> {
                campManage.setChannelIds(mcdCampChannelListDao.listChannelIdByCampsegId(campManage.getCampsegId()));
            });
        } else {
            final McdCampDef campDef = campDefDao.selectById(form.getPid());

            if (Constant.SpecialNumber.ZERO_STRING.equals(campDef.getCampsegRootId())) {
                //如果是主活动，查询下级列表
                campManagePage = mcdCampChannelListDao.pageCampChildRenByRootId(pager, form.getPid());
            } else {
                campManagePage = mcdCampChannelListDao.pageCampChildRen(pager, form.getPid());
            }
            final List<CampManage> childCampsegs = campManagePage.getRecords();
            // 添加活动监控数据
            for (CampManage childCampseg : childCampsegs) {
                childCampseg.setHasChild(false);
                // 修改策略状态
                childCampseg.setCampsegStatName(CampStatusJx.valueOfId(childCampseg.getCampsegStatId()));
            }
        }
        return campManagePage;
    }

    @Override
    public IPage<CampManage> searchIMcdCampJx(SearchTacticsActionQueryJx form) {
        // // 修改策略状态
        // page.getRecords().forEach(record -> {
        //     record.setCampsegStatName(CampStatusJx.valueOfId(record.getCampsegStatId()));
        // });
        return mcdCampDefJxService.jxPageCampDef(form);
    }

    @Override
    public List<CampManage> queryMcdSubCampList(McdSubCampByIdQuery req) {
        List<CampManage> campManageList = mcdCampDefJxService.queryMcdSubCampList(req);
        // 修改策略状态
        campManageList.forEach(record -> {
            record.setCampsegStatName(CampStatusJx.valueOfId(record.getCampsegStatId()));
        });
        return campManageList;
    }

    /**
     * 我的策略和全部策略导出
     *
     * @param form
     */
    @Override
    public void exportCampJx(SearchTacticsActionQueryJx form, HttpServletResponse response) {
        List<CampExportVO> list = mcdCampDefJxService.exportJxPageCampDef(form);
        // 修改策略状态
        list.forEach(record -> {
            record.setCampsegStatName(CampStatusJx.valueOfId(record.getCampsegStatId()));
        });

        String[] excelHeader = {"策略编号及名称", "子活动编码","执行周期", "策略状态","事件名称", "地市", "创建人"};
        String fileName = "我的策略活动列表.xlsx";
        String sheetName = "我的策略";
        if (0 == form.getIsSelectMy()) {
            fileName = "全部策略活动列表.xlsx";
            sheetName = "全部策略";
        }
        final Workbook book = WorkbookUtil.createBook(true);
        Sheet sheet = book.createSheet(sheetName);
        Row row = sheet.createRow(Constant.SpecialNumber.ZERO_NUMBER);
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
            int maxWith = 255 * 256;
            int newWidth = sheet.getColumnWidth(i) * 3;
            if (newWidth < maxWith) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, maxWith / 2);
            }
        }
        int i = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (CampExportVO camp : list) {
            i++;
            row = sheet.createRow(i);
            // 策略编号及名称
            String campIdName = StrUtil.format("{} {}", camp.getCampsegId(), camp.getCampsegName());
            row.createCell(Constant.SpecialNumber.ZERO_NUMBER).setCellValue(campIdName);
            // 子活动编码
            row.createCell(Constant.SpecialNumber.ONE_NUMBER).setCellValue(camp.getSubCampsegIds());

            // 执行周期
            String campDate = StrUtil.format("{}到{}", sdf.format(camp.getStartDate()), sdf.format(camp.getEndDate()));
            row.createCell(Constant.SpecialNumber.TWO_NUMBER).setCellValue(campDate);
            // 策略状态
            row.createCell(Constant.SpecialNumber.THREE_NUMBER).setCellValue(camp.getCampsegStatName());

            // 事件名称
            String eventNames = camp.getEventNames();
            if(StrUtil.isEmpty(eventNames) || StrUtil.isEmpty(eventNames.replaceAll(",",""))){
                eventNames = "--";
            }
            row.createCell(Constant.SpecialNumber.FOUR_NUMBER).setCellValue(eventNames);
            // 地市
            row.createCell(Constant.SpecialNumber.FIVE_NUMBER).setCellValue(camp.getCityName());
            // 创建人
            row.createCell(Constant.SpecialNumber.SIX_NUMBER).setCellValue(camp.getCreateUsername());
        }
        try {
            // fileName = new String(fileName.getBytes("UTF-8"), "iso8859-1");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            OutputStream ouputStream = response.getOutputStream();
            book.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            log.error("策略列表导出失败", e);
        }
    }

    @Override
    public IPage<CampManageJx> searchIMcdMyParticipationCamp(SearchMyParticipationTacticsQuery form) {
        IPage<CampManageJx> campManageIPage = mcdCampDefJxService.searchIMcdMyParticipationCamp(form);
        // 修改策略状态
        campManageIPage.getRecords().forEach(record -> {
            record.setCampsegStatName(CampStatusJx.valueOfId(record.getCampsegStatId()));
        });

        return campManageIPage;
    }

    @Override
    public void exportIMcdMyParticipationCamp(SearchMyParticipationTacticsQuery form, HttpServletResponse response) {
        List<CampManageJx> campManageList = mcdCampDefJxService.exportIMcdMyParticipationCamp(form);
        // 修改策略状态
        campManageList.forEach(record -> {
            record.setCampsegStatName(CampStatusJx.valueOfId(record.getCampsegStatId()));
        });

        String[] excelHeader = {"策略编号及名称", "执行周期", "地市", "状态", "创建人", "参与类型"};
        String fileName = "我参与的策略营销活动列表.xlsx";
        final Workbook book = WorkbookUtil.createBook(true);
        Sheet sheet = book.createSheet("Sheet");
        Row row = sheet.createRow(Constant.SpecialNumber.ZERO_NUMBER);
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
            int maxWith = 255 * 256;
            int newWidth = sheet.getColumnWidth(i) * 5;
            if (newWidth < maxWith) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, maxWith / 2);
            }
        }
        if (!CollectionUtils.isEmpty(campManageList)) {
            for (int i = 0; i < campManageList.size(); i++) {
                final CampManageJx campManage = campManageList.get(i);
                row = sheet.createRow(i + 1);
                row.createCell(Constant.SpecialNumber.ZERO_NUMBER).setCellValue(campManage.getCampsegId() + "," + campManage.getCampsegName());
                row.createCell(Constant.SpecialNumber.ONE_NUMBER).setCellValue(campManage.getStartDate() + "--" + campManage.getEndDate());
                row.createCell(Constant.SpecialNumber.TWO_NUMBER).setCellValue(campManage.getCityId());
                row.createCell(Constant.SpecialNumber.THREE_NUMBER).setCellValue(campManage.getCampsegStatName());
                row.createCell(Constant.SpecialNumber.FOUR_NUMBER).setCellValue(campManage.getCreateUsername());
                row.createCell(Constant.SpecialNumber.FIVE_NUMBER).setCellValue(campManage.getTaskType());
            }
        }
        try {
            // fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            OutputStream ouputStream = response.getOutputStream();
            book.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            log.error("我参与的策略导出失败", e);
        }

    }

    @Override
    public IPage<ApprRecordJx> approveRecord(McdCampApproveJxQuery req) {
        RecordPageDTO dto = new RecordPageDTO();
        dto.setCurrent(req.getCurrent());
        dto.setSize(req.getSize());

        List<ApprRecordJx> mcdCampDefs = mcdCampDefJxService.qryApprRecord(Collections.EMPTY_SET, req);
        if (CollectionUtils.isEmpty(mcdCampDefs)) {
            return new Page<>();
        }
        Map<String, ApprRecordJx> campMap = mcdCampDefs.stream().collect(Collectors.toMap(ApprRecordJx::getCampsegId, Function.identity()));
        dto.setList(Lists.newArrayList(campMap.keySet()));
        final ActionResponse<Page<CmpApproveProcessRecordJx>> userRecord = pecApproveFeignClient.getUserRecordNew(dto);
        log.info("当前用户待审记录->{}", new JSONObject(userRecord));
        Assert.isTrue(ResponseStatus.SUCCESS.equals(userRecord.getStatus()), userRecord.getMessage());
        final IPage<CmpApproveProcessRecordJx> data = userRecord.getData();

        if (StringUtils.isBlank(req.getCampsegName())) {
            final Set<String> campsegIds = data.getRecords().stream().map(CmpApproveProcessRecordJx::getBusinessId).collect(Collectors.toSet());
            mcdCampDefs = mcdCampDefJxService.qryApprRecord(campsegIds, req);
            campMap = mcdCampDefs.stream().collect(Collectors.toMap(ApprRecordJx::getCampsegId, Function.identity()));
        }

        IPage<ApprRecordJx> apprRecordIPage = new Page<>(data.getCurrent(), data.getSize(), data.getTotal());
        List<ApprRecordJx> listRecord = new ArrayList<>();
        apprRecordIPage.setRecords(listRecord);
        for (CmpApproveProcessRecordJx record : data.getRecords()) {
            if (null != campMap.get(record.getBusinessId())) {
                listRecord.add(CampsegAssemblerJx.convertToAppr(record, campMap.get(record.getBusinessId())));
            }
        }
        return apprRecordIPage;
    }


    @Override
    @Transactional
    public String copyCamp(String sourceCampId, String newCampName, UserSimpleInfo user, String flag) {
        log.info("tacticsCopyJx start...");
        final String newCampRootId = IdUtils.generateId();
        final List<McdCampDef> sourceCampDefs = campDefService.listByCampsegRootId(sourceCampId);
        Assert.notEmpty(sourceCampDefs, "传入的id不正确，请重试！");
        final List<McdCampChannelList> allChannelList = new ArrayList<>();
        final Map<String, String> oldNewRelation = new HashMap<>();
        for (McdCampDef sourceCampDef : sourceCampDefs) {
            if (null != user) {
                sourceCampDef.setCreateUserId(user.getUserId()).setCityId(user.getCityId());
            }
            if (Constant.SpecialNumber.ONE_STRING.equals(flag)) {
                sourceCampDef.setCampDefType(CampDefType.IOP.getType());
            }
            String newName = newCampName;
            if (StringUtils.isBlank(newName)) {
                newName = sourceCampDef.getCampsegName() + "_复制";
                if (!checkTactics(newName, null)) {
                    //验证活动名称重复问题
                    newName += DateUtil.now();
                }
            }
            //复制活动基本信息
            sourceCampDef.setCampsegName(newName).setCreateTime(new Date())
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

        logService.markSuccLog(newCampRootId, CampLogType.CAMP_CREATE, "复制", null);
        return newCampRootId;
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


    @Override
    public boolean checkTactics(String campsegName, String campsegId) {
        final McdCampDef mcdCampDef = new McdCampDef().setCampsegName(campsegName);
        LambdaQueryWrapper<McdCampDef> qry = Wrappers.lambdaQuery(mcdCampDef);
        List<McdCampDef> list = campDefService.list(qry);
        if (!CollectionUtils.isEmpty(list)) {
            if (StringUtils.isNotBlank(campsegId)) {
                return list.stream().anyMatch(campDef -> campDef.getCampsegId().equals(campsegId));
            } else {
                return false;
            }
        }
        return true;
    }


    @Override
    public IPage<McdCampCmpApproveProcessRecordJx> approveRecordNew(McdCampApproveJxNewQuery req) {
        final ActionResponse<Page<McdCampCmpApproveProcessRecordJx>> recordListNew = pecApproveFeignClient.queryRecordListNew(req);

        return recordListNew.getData();
    }

    @Transactional
    @Override
    public void deleteCampsegInfo(String id) {
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
        //更新cep事件状态
        mcdCampsegService.updateCEPEventId(id, isRoot, CampStatus.SUSPEND);

        //记录删除日志
        logService.markSuccLog(id, CampLogType.CAMP_DELETE, "删除", null);
    }

    @Override
    public void quickApprove(String campsegRootId) {
        log.info("开始一键审批,campsegRootId={}", campsegRootId);
        final List<McdCampDef> campDefs = campDefService.listByCampsegRootId(campsegRootId);
        Assert.notEmpty(campDefs, "传入的id不正确，请重试！");
        final McdCampDef rootCampDef = campDefs.get(Constant.SpecialNumber.ZERO_NUMBER);
        final boolean isRoot = rootCampDef.getCampsegRootId().equals(Constant.SpecialNumber.ZERO_STRING);
        Assert.isTrue(isRoot, "传入的id不正确，请重试！");
        Integer campsegStatId = rootCampDef.getCampsegStatId();
        // 是否支持一键审批
        boolean isCanQuickApprove =
                // 审批中,草稿,预演中,预演失败,预演完成
                CampStatus.APPROVING.getId().equals(campsegStatId) ||
                        CampStatus.DRAFT.getId().equals(campsegStatId) ||
                        CampStatusJx.PREVIEWING.getId().equals(campsegStatId) ||
                        CampStatusJx.PREVIEW_ERROR.getId().equals(campsegStatId) ||
                        CampStatusJx.PREVIEWED.getId().equals(campsegStatId);
        Assert.isTrue(isCanQuickApprove, "当前状态不支持一键审批");
        for (McdCampDef campDef : campDefs) {
            final Date startDate = campDef.getStartDate();
            final Date endDate = campDef.getEndDate();
            final Date newDate = new Date();
            if (!Constant.SpecialNumber.ZERO_STRING.equals(campDef.getCampsegRootId())) {
                Assert.isTrue(endDate.after(newDate), "该营销策略{}已超期", campsegRootId);
            }
            campDef.setCampsegStatId(PUBLISHING.getId());
            campDefService.updateById(campDef);
            if (Constant.SpecialNumber.ZERO_STRING.equals(campDef.getCampsegRootId())) {
                //根策略跳过
                continue;
            }
            //子策略第一波次执行信息
            final List<McdCampChannelList> mcdCampChannelLists = campChannelListService.listByCampsegPidWithoutWave(campDef.getCampsegId());
            for (McdCampChannelList campChannelList : mcdCampChannelLists) {
                campChannelList.setReason(Strings.EMPTY).setStatus(PUBLISHING.getId());
                String custgroupId = campChannelList.getPCustgroupId();
                String campsegId = campChannelList.getCampsegId();
                if (Integer.valueOf(Constant.SpecialNumber.ONE_NUMBER).equals(campChannelList.getCampClass())) {
                    // 一波次生成task，task_data查找活动下的所有渠道
                    McdCampTask task = new McdCampTask();
                    task.setTaskId(IdUtils.generateId());
                    task.setCampsegId(campChannelList.getCampsegId());
                    // 生成清单表（任务的基础清客户单表和派单客户清单表
                    //任务开始时间就是任务生成时间
                    task.setTaskStartTime(new Date());
                    task.setTaskEndTime(endDate);
                    task.setExecStatus(Constant.TASK_STATUS_UNDO);
                    task.setChannelId(campChannelList.getChannelId());
                    task.setChannelAdivId(campChannelList.getChannelAdivId());

                    String taskId = task.getTaskId();
                    int tableNum = 0;
                    try {
                        Integer dataDate = null;
                        if (custgroupId != null) {
                            final McdCustgroupDef mcdCustgroupDef = custgroupDefDao.selectById(custgroupId);
                            if (mcdCustgroupDef != null) {
                                dataDate = mcdCustgroupDef.getDataDate();
                                tableNum = mcdCustgroupDef.getCustomNum() == null ? Constant.SpecialNumber.ZERO_NUMBER : mcdCustgroupDef.getCustomNum();
                            }
                        }
                        // if (campChannelList.getUpdateCycle() || newDate.after(startDate)) {
                        // 一键审批直接给task_date插数据即可
                            McdCampTaskDate date = new McdCampTaskDate();
                            date.setTaskId(taskId);
                            date.setDataDate(dataDate);
                            date.setExecStatus(Constant.TASK_STATUS_UNDO);
                            date.setCustListCount(tableNum);
                            date.setPlanExecTime(newDate);
                            date.setCampsegId(campChannelList.getCampsegId());
                            campTaskDateDao.insert(date);
                        // }
                        campTaskDao.insert(task);
                    } catch (Exception e) {
                        log.error("异常信息如下:", e);
                    }
                }
                campChannelListService.updateById(campChannelList);
            }
        }
        log.info("一键审批完成,campsegRootId={}", campsegRootId);
    }

    /**
     * 策略延期
     *
     * @param campsegPid
     * @param endDate
     */
    @Override
    public void updateCampsegEndDate(String campsegPid, Date endDate) {
        //更新策略定义表延期时间
        McdCampDef camp = campDefService.getById(campsegPid);
        final String invokeCoc = RedisUtils.getDicValue(RedisDicKey.COC_IS_INVOKE_CUSTGROUP_DELAY);
        final List<McdCampChannelList> channelLists = campChannelListService.listMcdCampChannelListByCampsegRootId(campsegPid);
        Set<String> calledCustgrouPids = new HashSet<String>();
        List<McdCampChannelList> waveList = new ArrayList<>();
        // 活动的所有子ID
        List<String> campsegIds = new ArrayList<>();
        for (McdCampChannelList list : channelLists) {
            campsegIds.add(list.getCampsegId());
            if(list.getCampClass()==Constant.SpecialNumber.THREE_NUMBER){
                final Date waveStartDate = list.getStartDate();
                Assert.isTrue(endDate.after(waveStartDate), "子活动结束时间调整失败，波次无法执行");
                list.setEndDate(endDate);
                waveList.add(list);
            }
            // 只对活动状态是暂停、待执行、执行中的活动调用coc接口
            if (Lists.newArrayList(PENDING.getCode(), EXECUTED.getCode(), PAUSE.getCode())
                    .contains(list.getStatus().toString()) && Constant.SpecialNumber.ONE_STRING.equals(invokeCoc)) {

                // 过滤重复的客户群id
                if (!calledCustgrouPids.add(list.getPCustgroupId())) {
                    continue;
                }

                // 查询客户群结束时间
                McdCustgroupDef custgroup = custgroupDefService.getById(list.getPCustgroupId());
                try {

                    // 为空应该是coc推客户群是出现错误数据
                    if (custgroup.getFailTime() == null) {
                        continue;
                    }
                } catch (Exception ex) {
                    log.error("调用coc 客户群延迟接口发生异常，异常信息：", ex);
                    Assert.error("调用coc 客户群延迟接口发生异常，请重试！！！");
                }
            }
        }
        // 更新def表的结束日期
        UpdateWrapper<McdCampDef> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(McdCampDef::getEndDate,endDate).eq(McdCampDef::getCampsegId,campsegPid).or().eq(McdCampDef::getCampsegRootId,campsegPid);
        campDefService.update(updateWrapper);
        // 更新  channelList表的结束日期
        campChannelListService.updateBatchById(waveList);
        // 更新 task 表的结束日期
        UpdateWrapper<McdCampTask> uw = new UpdateWrapper<>();
        uw.lambda().set(McdCampTask::getTaskEndTime,endDate).in(McdCampTask::getCampsegId,campsegIds);
        mcdCampTaskService.update(uw);
        //添加策略延期日志
        logService.markSuccLog(campsegPid, CampLogType.CAMP_DELAY, DateUtil.format(endDate, DatePattern.NORM_DATETIME_PATTERN), null);
    }
    
    /**
     * 主题策略导出(我的策略和全部策略)
     *
     * @param form
     * @param response
     */
    @Override
    public void exportThemeCamp(CampThemeQuery form, HttpServletResponse response) {
        List<McdPrismCampThemeVO> list = prismCampThemeService.searchThemeCampList(form).getRecords();
        
        String[] excelHeader = {"主题编号及名称", "父活动编码","执行周期", "所属场景", "策略状态", "创建人"};
        String fileName = "我的主题策略活动列表.xlsx";
        String sheetName = "我的主题策略";
        if (0 == form.getIsSelectMy()) {
            fileName = "全部主题策略活动列表.xlsx";
            sheetName = "全部主题策略";
        }
        final Workbook book = WorkbookUtil.createBook(true);
        Sheet sheet = book.createSheet(sheetName);
        Row row = sheet.createRow(Constant.SpecialNumber.ZERO_NUMBER);
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
            int maxWith = 255 * 256;
            int newWidth = sheet.getColumnWidth(i) * 3;
            if (newWidth < maxWith) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, maxWith / 2);
            }
        }
        int i = 0;
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (McdPrismCampThemeVO theme : list) {
            List<SceneCampVO> campList = theme.getCampList();
            StringJoiner campsegIds = new StringJoiner(StrUtil.COMMA);
            StringJoiner execCycles = new StringJoiner(StrUtil.COMMA);
            StringJoiner sceneNames = new StringJoiner(StrUtil.COMMA);
            StringJoiner campStats = new StringJoiner(StrUtil.COMMA);
            for (SceneCampVO camp : campList) {
                campsegIds.add(camp.getCampsegId());
                execCycles.add(StrUtil.format("{}至{}",sdf.format(campList.get(0).getStartDate()), sdf.format(campList.get(0).getEndDate())));
                sceneNames.add(camp.getSceneName());
                campStats.add(camp.getCampsegStatName());
            }
            i++;
            row = sheet.createRow(i);
            // 主题编号及名称
            String themeIdName = StrUtil.format("{} {}", theme.getThemeId(), theme.getThemeName());
            row.createCell(Constant.SpecialNumber.ZERO_NUMBER).setCellValue(themeIdName);
            // 父活动编码
            row.createCell(Constant.SpecialNumber.ONE_NUMBER).setCellValue(campsegIds.toString());
            // 执行周期
            row.createCell(Constant.SpecialNumber.TWO_NUMBER).setCellValue(execCycles.toString());
//          // 所属场景
            row.createCell(Constant.SpecialNumber.THREE_NUMBER).setCellValue(sceneNames.toString());
            // 策略状态
            row.createCell(Constant.SpecialNumber.FOUR_NUMBER).setCellValue(campStats.toString());
            // 创建人
            row.createCell(Constant.SpecialNumber.FIVE_NUMBER).setCellValue(theme.getCreateUserName());
        }
        try {
            // fileName = new String(fileName.getBytes("UTF-8"), "iso8859-1");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            OutputStream ouputStream = response.getOutputStream();
            book.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            log.error("策略列表导出失败", e);
        }
    }
}
