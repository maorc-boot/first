package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.client.app.element.vo.PlanDefVO;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.CampStatus;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampsegJxDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.IopProductMapping;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampBaseInfoJxVO;
import com.asiainfo.biapp.pec.plan.jx.camp.req.QueryActivityJxQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampsegServiceJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IQuickTacticsService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ITemplateJxService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IopProductMappingService;
import com.asiainfo.biapp.pec.plan.jx.camp.utils.QuickTacticsUtil;
import com.asiainfo.biapp.pec.plan.model.McdCampChannelList;
import com.asiainfo.biapp.pec.plan.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.model.McdCampTask;
import com.asiainfo.biapp.pec.plan.model.McdDimAdivInfo;
import com.asiainfo.biapp.pec.plan.service.*;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.asiainfo.biapp.pec.plan.vo.*;
import com.asiainfo.biapp.pec.plan.vo.req.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mamp
 * @date 2023/4/6
 */
@Service
@Slf4j
public class QuickTacticsServiceImpl implements IQuickTacticsService {


    @Autowired
    private IMcdCampsegServiceJx campsegService;

    @Autowired
    private ITemplateJxService templateJxService;

    @Autowired
    private ITemplateService templateService;

    @Autowired
    private IopProductMappingService productMappingService;

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private McdCampsegJxDao mcdCampsegJxDao;

    @Resource
    private IMcdCampsegService mcdCampsegService;
    @Autowired
    private IMcdCampDefService mcdCampDefService;
    @Resource
    private IMcdCampChannelListService campChannelListService;
    @Resource
    private IMcdCampTaskService mcdCampTaskService;


    /**
     * 通过集团模板快速创建活动
     *
     * @param templateId 模板ID
     * @param activityId 模板活动ID
     * @param user       当前用户
     * @return
     */
    @Override
    public String quickCreateTacticsByIopTemplate(String templateId, String activityId, UserSimpleInfo user) throws Exception {

        QueryActivityJxQuery query = new QueryActivityJxQuery();
        query.setActivityId(activityId);
        query.setActivityTemplateId(templateId);
        ActivityVO activityVO = templateService.getActivityTemplate(activityId);
        if (null == activityVO) {
            throw new Exception(StrUtil.format("模板:{}不存在", templateId));
        }
        Map<String, Object> notifyParam = new HashMap<>();
        TacticsInfoJx req = convertData(user, notifyParam, activityVO);

        // 创建活动
        String campsegRootId = campsegService.saveCamp(req, user);
        // 创建活动成功后修改模板状态
        templateJxService.updateActivityStatus(templateId, activityId, "2");
        try {
            //  通知渠道上线素材
            materialNotify(notifyParam, "1");
        } catch (Exception e) {
            log.info("通知渠道上线素材接口异常:", e);
        }
        return campsegRootId;
    }


    /**
     * 将 ActivityBO 转换成  TacticsInfoJx
     *
     * @param user
     * @param notifyParam
     * @param activityVO
     * @return
     * @throws Exception
     */
    private TacticsInfoJx convertData(UserSimpleInfo user, Map<String, Object> notifyParam, ActivityVO activityVO) throws Exception {
        // 子活动
        List<CampaignVO> campaignList = activityVO.getCampaignList();
        // 默认只有一个子活动
        CampaignVO campaignVO = campaignList.get(0);
        // 客户群
        SegmentVO segmentBO1 = campaignVO.getSegmentBO();
        // 产品
        List<OfferVO> offerList = campaignVO.getOfferList();
        // 默认产品只有一个
        OfferVO offerVO = offerList.get(0);
        // 渠道信息
        ChannelVO channelBO = campaignVO.getChannelBO();
        // 运营位信息
        List<PositionVO> positionList = channelBO.getPositionList();
        // 默认只有一个运营位
        PositionVO positionVO = positionList.get(0);

        TacticsInfoJx req = new TacticsInfoJx();
        req.setType(1);
        req.setIsSubmit(0);
        CampBaseInfoJxVO baseInfo = new CampBaseInfoJxVO();
        // 活动名称
        baseInfo.setCampsegName("[精准营销]" + campaignVO.getCampaignName());
        // 活动描述
        baseInfo.setCampsegDesc(campaignVO.getCampaignName());
        // 活动模板ID
        baseInfo.setActivityTemplateId(activityVO.getActivityTemplateId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // 活动开始时间
        baseInfo.setStartDate(sdf.parse(campaignVO.getCampaignStartTime()));
        // 活动结束时间
        baseInfo.setEndDate(sdf.parse(campaignVO.getCampaignEndTime()));
        // rootId
        baseInfo.setCampsegRootId("0");
        baseInfo.setCampsegId(campaignVO.getActivityId());
        baseInfo.setCampDefType(1);
        baseInfo.setActivityType("1");
        baseInfo.setActivityObjective("1");
        baseInfo.setCreateUserId(user.getUserId());
        baseInfo.setCreateTime(new Date());
        baseInfo.setTacticsMap("[{\"group\":[\"1\",\"2\",\"3\"],\"children\":[]}]");

        // 默认不预演
        baseInfo.setPreviewCamp("0");
        req.setBaseCampInfo(baseInfo);

        List<CampScheme> campSchemes = new ArrayList<>();
        req.setCampSchemes(campSchemes);

        CampScheme campScheme = new CampScheme();
        campSchemes.add(campScheme);
        // 子活动基本信息
        CampBaseInfoVO baseCampInfo = new CampBaseInfoVO();
        // 子活动ID
        baseCampInfo.setCampsegId(IdUtils.generateId());
        baseCampInfo.setCampsegName(baseInfo.getCampsegName());
        baseCampInfo.setStartDate(baseInfo.getStartDate());
        baseCampInfo.setEndDate(baseInfo.getEndDate());
        baseCampInfo.setCampsegRootId(baseInfo.getCampsegId());
        baseCampInfo.setCampsegDesc(baseInfo.getCampsegDesc());
        baseCampInfo.setCreateTime(baseInfo.getCreateTime());
        baseCampInfo.setActivityObjective(baseInfo.getActivityObjective());
        baseCampInfo.setActivityType(baseInfo.getActivityType());
        baseCampInfo.setCreateUserId(baseInfo.getCreateUserId());
        baseCampInfo.setActivityTemplateId(baseInfo.getActivityTemplateId());
        baseCampInfo.setTacticsMap(baseInfo.getTacticsMap());
        campScheme.setBaseCampInfo(baseCampInfo);

        // 产品信息
        List<PlanDefVO> product = new ArrayList<>();
        PlanDefVO planDefVO = new PlanDefVO();
        LambdaQueryWrapper<IopProductMapping> query = new LambdaQueryWrapper<>();
        query.eq(IopProductMapping::getGoodsId, offerVO.getOfferCode());
        List<IopProductMapping> list = productMappingService.list(query);
        if (CollectionUtil.isEmpty(list)) {
            throw new Exception(StrUtil.format("集团产品:{}在省端找不到映射的产品", offerVO.getOfferCode()));
        }
        planDefVO.setPlanId(list.get(0).getProductItemId());
        product.add(planDefVO);
        campScheme.setProduct(product);

        // 客户群信息
        List<CustgroupDetailVO> customer = new ArrayList<>();
        CustgroupDetailVO custgroupDetailVO = new CustgroupDetailVO();
        // 一级精准营销专用虚拟客户群,需要提前创建好，眀客户群不能为空
        String custGroupId = RedisUtils.getDicValue("MCD_959_CUSTGROUP_ID");
        custgroupDetailVO.setCustomGroupId(custGroupId);
        // 默认为一次性
        custgroupDetailVO.setUpdateCycle(1);
        customer.add(custgroupDetailVO);
        campScheme.setCustomer(customer);

        // 渠道信息
        List<ChannelInfo> channels = new ArrayList<>();
        ChannelInfo channelInfo = new ChannelInfo();
        // 一级精准营销默认创建 959渠道活动
        channelInfo.setChannelId("959");
        CampChannelConfQuery channelConf = new CampChannelConfQuery();
        channelInfo.setChannelConf(channelConf);
        CampChannelExtQuery channelExtConf = new CampChannelExtQuery();
        channelConf.setChannelExtConf(channelExtConf);
        // 默认全量推送
        channelExtConf.setColumnExt1("1");


        String positionImgAddr = null;
        String positionLinkAddr = null;
        String materielFilePath = null;
        // 定义两个key,positionImgAddr：物料图片URL,positionLinkAddr：物料链接地址URL
        //List<CamExtAttrMap> camExtAttrMap = campaignVO.getCamExtAttrMap();
        List<KeyValue> actAttrExtMap = campaignVO.getCamExtAttrMap();
        if (CollectionUtil.isNotEmpty(actAttrExtMap)) {
            for (KeyValue attrMap : actAttrExtMap) {
                if ("positionImgID".equals(attrMap.getKey())) {
                    positionImgAddr = attrMap.getValue();
                }
                if ("positionLinkAddr".equals(attrMap.getKey())) {
                    positionLinkAddr = attrMap.getValue();
                }
            }
        }
        String basePath = RedisUtils.getDicValue("MCD_959_91999_BASE_PATH");
        materielFilePath = basePath + File.separator + campaignVO.getCampaignId() + File.separator + positionImgAddr;

        // 运营位图片url
        channelExtConf.setColumnExt2(positionImgAddr);
        // 跳转连接
        channelExtConf.setColumnExt3(positionLinkAddr);
        // 素材路径
        channelExtConf.setColumnExt4(materielFilePath);
        // 集团产品ID
        channelExtConf.setColumnExt5(offerVO.getOfferCode());
        channels.add(channelInfo);
        campScheme.setChannels(channels);

        // 运营位信息
        McdDimAdivInfo mcdDimAdivInfo = new McdDimAdivInfo();
        channelInfo.setAdivInfo(mcdDimAdivInfo);
        // mcdDimAdivInfo.setAdivId(positionVO.getPositionId());
        String adivId = RedisUtils.getDicValue("MCD_959_ADIV_ID");
        adivId = adivId == null ? "9591" : adivId;
        mcdDimAdivInfo.setAdivId(adivId);

        List<CampChildrenScheme> childrenSchemes = new ArrayList<>();
        req.setChildrenSchemes(childrenSchemes);
        CampChildrenScheme campChildrenScheme = new CampChildrenScheme();
        childrenSchemes.add(campChildrenScheme);
        // 子活动ID
        campChildrenScheme.setCampsegId(campaignVO.getCampaignId());
        // 产品id，客群id，渠道id，运营位id]
        List<String> data = new ArrayList<>();
        campChildrenScheme.setData(data);
        // 产品ID
        data.add(planDefVO.getPlanId());
        // 客群id
        data.add(custGroupId);
        // 渠道id
        data.add(channelInfo.getChannelId());
        // 运营位id
        data.add(mcdDimAdivInfo.getAdivId());

        sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        notifyParam.put("campName", campaignVO.getCampaignName());
        notifyParam.put("startDate", sdf.format(baseInfo.getStartDate()));
        notifyParam.put("endDate", sdf.format(baseInfo.getEndDate()));
        notifyParam.put("campId", campaignVO.getCampaignId());
        notifyParam.put("positionImgID", materielFilePath);
        notifyParam.put("positionLinkAddr", positionLinkAddr);

        return req;
    }

    /**
     * 更改集团模板活动状态
     *
     * @param request HttpServletRequest
     * @return
     * @throws Exception
     */
    @Override
    public String changeActivityStatus(HttpServletRequest request) throws Exception {
        return null;
    }

    /**
     * 物料上下线通知接口
     *
     * @param campInfo   活动信息
     * @param notifyType 通知类型，1：上线 2：下线
     * @throws Exception
     */
    @Override
    public void materialNotify(Map<String, Object> campInfo, String notifyType) throws Exception {
        // 和我信发现(一级)指定的渠道ID
        String huiJtChannel = RedisUtils.getDicValue("MCD_959_HUIJTCHANNEL");
        log.info("huiJtChannel = {} ", huiJtChannel);

        //　和我信发现(一级)通知接口的url
        String notifyUrl = RedisUtils.getDicValue("MCD_959_NOTIFY_URL");
        log.info("通知接口的url = {} ", notifyUrl);

        // 获取token
        String token = getToken(huiJtChannel);
        String startDate = campInfo.get("startDate") == null ? "" : String.valueOf(campInfo.get("startDate"));
        String endDate = campInfo.get("endDate") == null ? "" : String.valueOf(campInfo.get("endDate"));

        // 获取签名的参数
        Map<String, Object> signParam = new HashMap<>();

        signParam.put("notifyType", notifyType);
        // 子活动ID
        signParam.put("contactorId", String.valueOf(campInfo.get("campId")));
        // 活动名称
        signParam.put("contactorName", String.valueOf(campInfo.get("campName")));
        // 活动名称
        signParam.put("contactorDesc", String.valueOf(campInfo.get("campName")));
        // 图片路径
        signParam.put("imageName", String.valueOf(campInfo.get("positionImgID")));
        // 开始时间
        signParam.put("startTime", startDate);
        // 结束时间
        signParam.put("endTime", endDate);
        // 跳转连接
        signParam.put("url", String.valueOf(campInfo.get("positionLinkAddr")));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        String huiJtTimeStamp = dateFormat.format(new Date());
        signParam.put("huiJtTimeStamp", huiJtTimeStamp);
        signParam.put("huiJtChannel", huiJtChannel);
        log.info("signParam:{}", signParam);
        String sign = QuickTacticsUtil.createSign(signParam, token, false);
        log.info("获取sign:{}", sign);


        List<BasicNameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("notifyType", notifyType));
        param.add(new BasicNameValuePair("contactorId", String.valueOf(campInfo.get("campId"))));
        param.add(new BasicNameValuePair("contactorName", String.valueOf(campInfo.get("campName"))));
        param.add(new BasicNameValuePair("contactorDesc", String.valueOf(campInfo.get("campName"))));
        param.add(new BasicNameValuePair("imageName", String.valueOf(campInfo.get("positionImgID"))));
        param.add(new BasicNameValuePair("startTime", startDate));
        param.add(new BasicNameValuePair("endTime", endDate));
        param.add(new BasicNameValuePair("url", String.valueOf(campInfo.get("positionLinkAddr"))));
        param.add(new BasicNameValuePair("huiJtChannel", huiJtChannel));
        param.add(new BasicNameValuePair("huiJtTimeStamp", huiJtTimeStamp));
        param.add(new BasicNameValuePair("huiJtSign", sign));

        log.info("开始调用通知接口: param = {} ", param);
        String respNotify = QuickTacticsUtil.httpCall(notifyUrl, param);
        log.info("通知接口返回结果:{}", respNotify);
    }

    /**
     * 日期格式转换
     *
     * @param srcFormat  原日期格式
     * @param destFormat 目标日期格式
     * @param srcDateStr 原日期字符串
     * @return
     * @throws ParseException
     */
    String convertDateFormat(String srcFormat, String destFormat, String srcDateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormat);
        Date d = sdf.parse(srcDateStr);
        sdf = new SimpleDateFormat(destFormat);
        return sdf.format(d);

    }

    /**
     * 获取token
     *
     * @param huiJtChannel 渠道编码
     * @return
     */
    private String getToken(String huiJtChannel) {
        // 获取token的url
        String tokenUrl = null;
        String token = "";
        try {
            tokenUrl = RedisUtils.getDicValue("MCD_959_TOKEN_URL");
            tokenUrl += "?huiJtChannel=" + huiJtChannel;
            log.info("开始获取token, tokenUrl = {} ", tokenUrl);
            JSONObject tokenRes = restTemplate.getForObject(tokenUrl, JSONObject.class);
            log.info("获取token结果 = {} ", tokenRes);
            String respCode = tokenRes.getString("code");
            if ("1".equals(respCode)) {
                JSONObject data = tokenRes.getJSONObject("data");
                token = data.getString("huiToken");
            }
        } catch (Exception e) {
            log.error("获取token失败:", e);
        }
        return token;
    }


    @Override
    public void updateActivityStatus() {
        List<Map<String, Object>> list = mcdCampsegJxDao.queryActivityList();
        log.info("查询到{}条需要处理的活动。", list.size());
        String templateStatus;
        String campStat;
        String activityId = null;
        for (Map<String, Object> map : list) {
            try {
                templateStatus = String.valueOf(map.get("ACTIVITY_STATUS"));
                campStat = String.valueOf(map.get("CAMPSEG_STAT_ID"));
                activityId = String.valueOf(map.get("ACTIVITY_ID"));
                List<McdCampChannelList> campChannelList = campChannelListService.listMcdCampChannelListByCampsegRootId(activityId);
                final List<McdCampDef> campDefs = mcdCampDefService.listByCampsegRootId(activityId);
                Assert.notEmpty(campDefs, "当前策略不存在");
                // 模板状态是暂停（1） 且 活动状态是执行中（54）时，需要将活动状态变更为暂停（59）
                if ("1".equals(templateStatus) && CampStatus.EXECUTED.getCode().equals(campStat)) {
                    log.warn("变更一级精准推荐渠道活动:{}状态为暂停(59)", activityId);

                    updateCampStat(campDefs, CampStatus.PAUSE.getId());
                    updateCampChnListStat(campChannelList, CampStatus.PAUSE.getId());
                    continue;
                }
                // 模板状态是终止（2）且当前活动状态不是终止（91）和已完成（90）时，需要变更为终止
                if ("2".equals(templateStatus) && !CampStatus.SUSPEND.getCode().equals(campStat) && !CampStatus.DONE.getCode().equals(campStat)) {
                    log.warn("变更一级精准推荐渠道活动:{}状态为终止(91)", activityId);
                    updateCampStat(campDefs, CampStatus.SUSPEND.getId());
                    updateCampChnListStat(campChannelList, CampStatus.SUSPEND.getId());
                    continue;
                }
                // 模板状态是执行中（3）且当前活动状态是审批中（40）时，将活动变更为执行中（54），并生成相关任务
                if ("3".equals(templateStatus) && CampStatus.APPROVING.getCode().equals(campStat)) {
                    qucickApprove959Camp(activityId);
                    continue;
                }
                // 模板状态是执行中（3）且当前活动状态是暂停（59）时，将活动变更为执行中（54）
                if ("3".equals(templateStatus) && CampStatus.PAUSE.getCode().equals(campStat)) {
                    log.warn("变更一级精准推荐渠道活动:{}状态为执行中(54)", activityId);
                    updateCampStat(campDefs, CampStatus.EXECUTED.getId());
                    updateCampChnListStat(campChannelList, CampStatus.EXECUTED.getId());
                    continue;
                }

            } catch (Exception e) {
                log.error("更新活动状态异常,activityId={}", activityId, e);
            }
        }
    }


    private void updateCampStat(List<McdCampDef> campDefList, int status) {

        for (McdCampDef mcdCampDef : campDefList) {
            mcdCampDef.setCampsegStatId(status);
        }

        mcdCampDefService.updateBatchById(campDefList);
    }

    private void updateCampChnListStat(List<McdCampChannelList> campChnList, int status) {

        for (McdCampChannelList campChannelList : campChnList) {
            campChannelList.setStatus(status);
            LambdaUpdateWrapper<McdCampTask> taskWrapper = new LambdaUpdateWrapper<>();
            taskWrapper.eq(McdCampTask::getCampsegId,campChannelList.getCampsegId())
                    .eq(McdCampTask::getChannelId,campChannelList.getChannelId())
                    .set(McdCampTask::getExecStatus,status);
            mcdCampTaskService.update(taskWrapper);
        }

        campChannelListService.saveBatch(campChnList);

    }


    @Override
    public void qucickApprove959Camp(String campsegRootId) {
        CampStatusQuery req = new CampStatusQuery();
        req.setCampStat(CampStatus.PUBLISHING.getId());
        req.setFlowId(campsegRootId);
        req.setCampsegRootId(campsegRootId);
        mcdCampsegService.modifyCampStatusFromAppr(req);

        //策略发布
        mcdCampsegService.doCampRelease(campsegRootId);
    }
}
