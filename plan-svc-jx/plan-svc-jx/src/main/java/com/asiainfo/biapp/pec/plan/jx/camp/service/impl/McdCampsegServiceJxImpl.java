package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.common.jx.constant.RedisDicKey;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.CampStatus;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.plan.common.CampLogType;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.dao.IopDigitalProductDao;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.IChannelMaterialQueryDao;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampsegJxDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.JxMcdCampDef;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdFqcCycle;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdFqcRule;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampBaseInfoJxVO;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampExcuteReq;
import com.asiainfo.biapp.pec.plan.jx.camp.req.HisCampInfoReq;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.*;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.HisCampInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCampExcuteInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.dnacustomgroup.CustgroupDetailVO;
import com.asiainfo.biapp.pec.plan.jx.dna.constant.ConstantDNA;
import com.asiainfo.biapp.pec.plan.jx.dna.service.IDNACustomGroupService;
import com.asiainfo.biapp.pec.plan.model.*;
import com.asiainfo.biapp.pec.plan.service.*;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import com.asiainfo.biapp.pec.plan.vo.CampBusinessInfo;
import com.asiainfo.biapp.pec.plan.vo.req.CampChildrenScheme;
import com.asiainfo.biapp.pec.plan.vo.req.CampScheme;
import com.asiainfo.biapp.pec.plan.vo.req.ChannelInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.ONE_NUMBER;

/**
 * @author : ranpf
 * @date : 2022-10-08 14:32:19
 * 营销策略处理服务实现类，处理逻辑
 */
@Service
@Slf4j
@RefreshScope
public class McdCampsegServiceJxImpl implements IMcdCampsegServiceJx {

    @Autowired
    private IMcdCampDefService campDefService;

    @Resource
    private IMcdCampOperateLogService logService;

    @Resource
    private IMcdCustgroupDefService custgroupDefService;

    @Resource
    private IMcdCampChannelExtService extService;

    @Resource
    private IMcdPlanDefService planDefService;

    @Resource
    private IMcdCampChannelListService campChannelListService;


    @Resource
    private IApproveServiceJx approveServiceJx;


    @Resource
    private ICepAPIService apiService;

    @Resource
    private IopDigitalProductDao digitalProductDao;

    @Resource
    ICampPreveiwJxService campPreveiwJxService;

    @Resource
    private IPlanExtInfoService planExtInfoService;

    @Resource
    private McdFqcCycleService fqcCycleService;

    @Resource
    private McdFqcRuleService fqcRuleService;

    @Resource
    private McdCampsegJxDao mcdCampsegJxDao;

    @Resource
    private JxMcdCampDefService jxMcdCampDefService;

    @Autowired
    private IChannelMaterialQueryDao materialQueryDao;

    @Autowired
    private IDNACustomGroupService idnaCustomGroupService;

    @Resource
    private IMcdCustgroupPushLogService mcdCustgroupPushLogService;

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

    /**
     * 检查策略名称是否存在，并返回具体重复信息
     *
     * @param campsegName 策略名称
     * @param campsegId   策略id非必填
     * @return {@link Map}<{@link Object}, {@link Object}>
     */
    @Override
    public Map<Object, Object> checkTacticsDetail(String campsegName, String campsegId) {
        Map<Object, Object> resMap = new HashMap<>();
        resMap.put("campsegName", campsegName);
        final McdCampDef mcdCampDef = new McdCampDef().setCampsegName(campsegName);
        LambdaQueryWrapper<McdCampDef> qry = Wrappers.lambdaQuery(mcdCampDef);
        List<McdCampDef> list = campDefService.list(qry);
        if (!CollectionUtils.isEmpty(list)) {
            if (StringUtils.isNotBlank(campsegId)) {
                boolean anyMatch = list.stream().anyMatch(campDef -> campDef.getCampsegId().equals(campsegId));
                resMap.put("bool", anyMatch);
                return resMap;
            } else {
                resMap.put("bool", false);
                return resMap;
            }
        }
        resMap.put("bool", true);
        return resMap;
    }


    @Override
    public String saveCamp(TacticsInfoJx req, UserSimpleInfo user) {
        Result result = saveCampseg(req, user);
        commitApprove(req, user, result.campsegRootId, result.campDefs);
        return result.campDef.getCampsegId();
    }

    /**
     * 保存活动基本信息
     * @param req
     * @param user
     * @return
     */
    @Transactional
    public Result saveCampseg(TacticsInfoJx req, UserSimpleInfo user) {
        log.info("tacticsCreateJx SaveAndUpdate start...");
        String campsegRootId = req.getBaseCampInfo().getCampsegId();
        //组装保存实体
        final CampBusinessInfo campBusinessInfo = CampsegAssemblerJx.convertToInfo(req, user, campsegRootId);
        //1.保存基本信息(orupdate)
        List<McdCampDef> campDefs = campBusinessInfo.getMcdCampDefs();
        //这是根活动
        McdCampDef campDef = campDefs.get(Constant.SpecialNumber.ZERO_NUMBER);
        Assert.isTrue(campDef.getEndDate().after(DateUtil.date().toJdkDate()), "请检查策略结束时间");
        //2.保存渠道信息(子活动ordeleteandinsert)
        final List<McdCampChannelList> campChannelLists = campBusinessInfo.getMcdCampChannelLists();
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
        saveDigitalContent2Plan(campChannelLists.stream().map(McdCampChannelList::getPlanId).collect(Collectors.toSet()));
        //分箱和波次客户群加入客群表
        final List<McdCustgroupDef> extCustGroup = campBusinessInfo.getExtCustGroup();
        if (!CollectionUtils.isEmpty(extCustGroup)) {
            custgroupDefService.saveOrUpdateBatch(extCustGroup);
        }
        campDefService.saveBatch(campDefs);
        jxExtProperty(req, campDefs);
        // updateCycle字段值在CampsegAssemblerJx这个对象里面已经根据客群(coc/dna)对应的周期值转化，以下代码暂时注释
        // for (McdCampChannelList channelList : campChannelLists) {
        //     String channelId = channelList.getChannelId();
        //     String channelAdivId = channelList.getChannelAdivId();
        //     List<CampScheme> campSchemes = req.getCampSchemes();
        //     for (CampScheme scheme : campSchemes) {
        //         for (ChannelInfo channel : scheme.getChannels()) {
        //             if (channelId.equals(channel.getChannelId()) && channelAdivId.equals(channel.getAdivInfo().getAdivId())) {
        //                 // Boolean updateCycle = channel.getChannelConf().getUpdateCycle();
        //                 // updateCycle = updateCycle == null ? false : updateCycle;
        //                 channelList.setUpdateCycle(channel.getChannelConf().getUpdateCycle());
        //             }
        //         }
        //     }
        // }
        campChannelListService.saveBatch(campChannelLists);
        extService.saveBatch(campChannelExts);
        // 保存活动级别的频次信息到频次规则表 mcd_fqc_rule
        saveFqc(campChannelLists);
        // 保存产品扩展信息：融合产品，同系列产品，互斥产品
        planExtInfoService.savePlanExtInfo(req.getPlanExtInfoList(), campDef);
        // 生成预演数据
        campPreveiwJxService.savePreview(campDef, campChannelLists);

        // 异步处理选择的客群为dna源时的逻辑
        if (ConstantDNA.CUSTGROUP_SOURCE.equals(req.getBaseCampInfo().getCustgroupSource())) {
            // 1. @Async注解是通过aop代理实现的，调用自己类的异步方法时，就会绕过aop代理从而导致其异步失效
            // 2. 所以需要从spring上下文中取得代理对象，继而调用其异步方法(或者单独起个类去写异步逻辑然后把异步类注入进来调用即可)
            McdCampsegServiceJxImpl mcdCampsegServiceJx = SpringUtil.getBean(McdCampsegServiceJxImpl.class);
            mcdCampsegServiceJx.asyncDelDnaCustgroupLogic(req);
        }

        if (isNew) {
            logService.markSuccLog(campsegRootId, CampLogType.CAMP_CREATE, null, user);
        } else {
            logService.markSuccLog(campsegRootId, CampLogType.CAMP_MOD, null, user);
        }
        log.info("SaveAndUpdate campseg success campsegId={}", campsegRootId);
        updateCEPEventId(campsegRootId, true, CampStatus.DRAFT);
        Result result = new Result(campsegRootId, campDefs, campDef);
        return result;
    }

    /**
     * 中间结果封装
     */
    private static class Result {
        public final String campsegRootId;
        public final List<McdCampDef> campDefs;
        public final McdCampDef campDef;

        public Result(String campsegRootId, List<McdCampDef> campDefs, McdCampDef campDef) {
            this.campsegRootId = campsegRootId;
            this.campDefs = campDefs;
            this.campDef = campDef;
        }
    }

    /**
     * 提交审批
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
     *  异步处理选择的客群为dna源时：
     *  1. 判断客群是否存在mcd_custgroup_def 存在说明文件已转换，此客群不是第一次使用，不需要走后续逻辑
     *  2. 调用dna接口1.4 生成客群清单文件
     *  3. 下载清单文件到本地(plan-svc服务本地)
     *  4. 将获取到的清单文件按照coc文件格式命名
     *  5. 按照coc格式生成校验文件
     *  6. 上传清单文件&校验文件到coc清单文件所在目录
     *  7. mcd_custgroup_def保存dna客群信息
     *  8. mcd_custgroup_push_log保存日志信息
     *
     *  @param req 保存入参对象
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
     * @param chkFile 校验文件
     * @param dnaCustFile 更名后的客群文件
     * @param newCustFile dna接口获取到的客群文件
     * @param map 调用dna接口返回
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


    @Override
    public void saveDigitalContent2Plan(Set<String> planIds) {
        for (String planId : planIds) {
            if (StringUtils.isNotBlank(planId)) {
                if (planId.contains(Constant.SpecialCharacter.COMMA)) {
                    //兼容售前售中售后产品
                    saveDigitalContent2Plan(Sets.newHashSet(planId.split(Constant.SpecialCharacter.COMMA)));
                } else {
                    McdPlanDef mcdPlanDef = planDefService.getById(planId);
                    if (null == mcdPlanDef) {
                        //说明选了内容，选了内容要把内容的信息添加到产品上，srvType=4
                        final IopDigitalProduct iopDigitalProduct = digitalProductDao.selectById(planId);
                        if (null == iopDigitalProduct) {
                            //产品包不做解析
                            break;
                        }
                        mcdPlanDef = new McdPlanDef();
                        mcdPlanDef.setId(IdUtils.generateId());
                        mcdPlanDef.setPlanId(iopDigitalProduct.getProductCode());
                        mcdPlanDef.setPlanName(iopDigitalProduct.getProductName());
                        mcdPlanDef.setPlanDesc(iopDigitalProduct.getProductDesc());
                        mcdPlanDef.setPlanType("9");
                        mcdPlanDef.setPlanSrvType(Constant.SpecialNumber.FOUR_STRING);
                        mcdPlanDef.setDataSource(Constant.SpecialNumber.TWO_NUMBER);
                        log.info("将热点内容存入产品库中, 内容code:{}", planId);
                        planDefService.save(mcdPlanDef);
                    }
                }
            }
        }
    }

    @Override
    public void updateCEPEventId(String id, boolean isRoot, CampStatus status) {
        //先查询活动用的事件，没有被别的活动用，然后修改状态
        List<McdCampChannelList> mcdCampChannelLists = null;
        if (isRoot) {
            mcdCampChannelLists = campChannelListService.listMcdCampChannelListByCampsegRootId(id);
        } else {
            mcdCampChannelLists = campChannelListService.listMcdCampChannelListByCampsegPid(id);
        }
        for (McdCampChannelList channelBO : mcdCampChannelLists) {
            if (StringUtils.isNotBlank(channelBO.getCepEventId())) {
                apiService.updateEventStatus(channelBO.getCepEventId(), status.getCode());
            }
        }
    }

    /**
     * 根据客户群ID和渠道ID查询历史活动
     *
     * @param req
     * @return
     */
    @Override
    public IPage<List<HisCampInfo>> queryHisCamp(HisCampInfoReq req) {
        Page page = new Page();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());
        String[] channelIds = req.getChannelIds().split(",");
        return mcdCampsegJxDao.queryHisCamp(page, req.getCustgroupId(), Arrays.asList(channelIds));
    }

    @Override
    public com.alibaba.fastjson.JSONObject getCampExcuteInfos(CampExcuteReq req) {
        //短信渠道配置信息
        String smsChannels = RedisUtils.getDicValue("IOP_SMS_CHANNEL");
        //IOP短信发送能力配置
        String smsChannelSendCapacity = RedisUtils.getDicValue("SMS_CHANNEL_SEND_CAPACITY");
        //内容短信发送能力
        String smsChannelSendCapacity801 = RedisUtils.getDicValue("SMS_CHANNEL_SEND_CAPACITY_801");
        //10086短信发送能力
        String smsChannelSendCapacity901 = RedisUtils.getDicValue("SMS_CHANNEL_SEND_CAPACITY_901");
        //视频彩信发送能力
        String smsChannelSendCapacity932 = RedisUtils.getDicValue("SMS_CHANNEL_SEND_CAPACITY_932");
        //5G消息发送能力
        String smsChannelSendCapacity960 = RedisUtils.getDicValue("SMS_CHANNEL_SEND_CAPACITY_960");
        //超级SIM卡发送能力（5G云卡）
        String smsChannelSendCapacity964 = RedisUtils.getDicValue("SMS_CHANNEL_SEND_CAPACITY_964");
        String[] split = smsChannels.split(",");
        List<String> smsChannelList = Arrays.asList(split);
        List<String> channelIds = mcdCampsegJxDao.getChannelIdByCamprootId(req.getCampsegRootId());
        //取交集
        List<String> channelIdMixList = smsChannelList.stream().filter(item -> channelIds.contains(item)).collect(Collectors.toList());
        List<McdCampExcuteInfo> mcdCampExcuteInfos = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(channelIdMixList)) {
            mcdCampExcuteInfos = mcdCampsegJxDao.queryCampExcuteInfo(channelIdMixList);
        }

        //各短信渠道配置平均发送能力拼装
        for (McdCampExcuteInfo mcdCampExcuteInfo : mcdCampExcuteInfos) {
            String channelId = mcdCampExcuteInfo.getChannelId();
            switch (channelId) {
                case "801":
                    mcdCampExcuteInfo.setChannelCapacity(Integer.valueOf(smsChannelSendCapacity801));
                    break;
                case "901":
                    mcdCampExcuteInfo.setChannelCapacity(Integer.valueOf(smsChannelSendCapacity901));
                    break;
                case "932":
                    mcdCampExcuteInfo.setChannelCapacity(Integer.valueOf(smsChannelSendCapacity932));
                    break;
                case "960":
                    mcdCampExcuteInfo.setChannelCapacity(Integer.valueOf(smsChannelSendCapacity960));
                    break;
                case "964":
                    mcdCampExcuteInfo.setChannelCapacity(Integer.valueOf(smsChannelSendCapacity964));
                    break;
            }
        }
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("channelStatis", CollectionUtil.isEmpty(mcdCampExcuteInfos) ? new ArrayList<>() : mcdCampExcuteInfos);
        jsonObject.put("smsCapacity", smsChannelSendCapacity);
        jsonObject.put("channelList", channelIdMixList);
        return jsonObject;
    }

    /**
     * 查询模板
     *
     * @param userId
     * @return
     */
    @Override
    public List<Map<String, Object>> queryIopTemplate(String userId) {
       return  mcdCampsegJxDao.queryIopTemplate(userId);
    }

    @Override
    public Map<String, List<Map<String, Object>>> queryCampScene() {
        List<Map<String, Object>> mapList = mcdCampsegJxDao.queryCampScene();
        return mapList.stream().collect(Collectors.groupingBy(item -> item.get("BUSINESS_SCENE_TYPE").toString()));
    }

    @Override
    public void checkCampEndDate(TacticsInfoJx req) {
        final CampBaseInfoJxVO baseCampInfo = req.getBaseCampInfo();
        if (null != baseCampInfo.getEndDate()) {
            final LocalDateTime delayLocalDate = baseCampInfo.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            Assert.isTrue(LocalDateTime.now().isBefore(delayLocalDate), "执行结束时间应大于当前时间！");
            // 活动管理页面对执行中的活动进行延期操作时，如果延期时间大于客户群失效时间，则提示调用coc客户群延期接口
            final List<CampChildrenScheme> childrenSchemes = req.getChildrenSchemes();
            for (CampChildrenScheme campChildrenScheme : childrenSchemes) {
                final List<String> data = campChildrenScheme.getData();
                String planId = data.get(Constant.SpecialNumber.ZERO_NUMBER);
                String custId = data.get(Constant.SpecialNumber.ONE_NUMBER);
                // 判断产品失效日期
                McdPlanDef plan = planDefService.getById(planId);
                Assert.notNull(plan, "策略:{},配置的产品:{}不正确", baseCampInfo.getCampsegName(), planId);
                final LocalDateTime planEndDate = plan.getPlanEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                Assert.isTrue(planEndDate.isAfter(delayLocalDate) || planEndDate.equals(delayLocalDate), "活动结束日期大于产品失效日期({})，", planEndDate);

                if (Constant.SpecialNumber.ZERO_NUMBER == baseCampInfo.getCustgroupSource()) { // coc客群
                    // 判断 客户群失效日期
                    McdCustgroupDef custgroup = custgroupDefService.getById(custId);
                    Assert.notNull(custgroup, "策略:{},配置的客户群:{}不正确", baseCampInfo.getCampsegName(), custId);

                    // tipStr = "客户群失效日期为空，";
                    if (custgroup.getFailTime() == null) {
                        continue;
                    }

                    final LocalDateTime custFailTime = custgroup.getFailTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    Assert.isTrue(custFailTime.isAfter(delayLocalDate) || custFailTime.equals(delayLocalDate), "活动延期日期大于客户群失效日期({})，", custFailTime);
                } else { // dna客群
                    checkDNACustFailTime(delayLocalDate, baseCampInfo.getCampsegName(), custId);
                }
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
                        String planId = data.get(Constant.SpecialNumber.ZERO_NUMBER);
                        String custId = data.get(Constant.SpecialNumber.ONE_NUMBER);
                        // 判断产品失效日期
                        McdPlanDef plan = planDefService.getById(planId);
                        Assert.notNull(plan, "子活动:{},配置的产品:{}不正确", campName, planId);
                        final LocalDateTime planEndDate = plan.getPlanEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        Assert.isTrue(planEndDate.isAfter(delayLocalDate) || planEndDate.equals(delayLocalDate), "子活动:{},活动结束日期大于产品失效日期({})，", campName, planEndDate);

                        if (Constant.SpecialNumber.ZERO_NUMBER == baseCampInfo.getCustgroupSource()) { // coc客群
                            // 判断 客户群失效日期
                            McdCustgroupDef custgroup = custgroupDefService.getById(custId);
                            Assert.notNull(custgroup, "子活动:{},配置的客户群:{}不正确", campName, custId);

                            // tipStr = "客户群失效日期为空，";
                            if (custgroup.getFailTime() == null) {
                                continue;
                            }

                            final LocalDateTime custFailTime = custgroup.getFailTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            Assert.isTrue(custFailTime.isAfter(delayLocalDate) || custFailTime.equals(delayLocalDate), "活动延期日期大于客户群失效日期({})，", custFailTime);
                        } else { // dna客群
                            checkDNACustFailTime(delayLocalDate, baseCampInfo.getCampsegName(), custId);
                        }
                    }
                }
            }

        }
    }

    /**
     * 校验客群失效时间(根据客群id从DNA获取客群详情数据)
     *
     * @param delayLocalDate delayLocalDate
     * @param campName 活动名称
     * @param custId 客群id
     */
    private void checkDNACustFailTime(LocalDateTime delayLocalDate, String campName, String custId) {
        CustgroupDetailVO custgroupDetailVO = idnaCustomGroupService.detailCustgroup(custId);
        Assert.notNull(custgroupDetailVO, "子活动:{},配置的客户群:{}不正确", campName, custId);
        if (custgroupDetailVO.getFailTime() == null) {
            return;
        }
        final LocalDateTime custFailTime = custgroupDetailVO.getFailTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Assert.isTrue(custFailTime.isAfter(delayLocalDate) || custFailTime.equals(delayLocalDate), "活动延期日期大于客户群失效日期({})，", custFailTime);
    }


}
