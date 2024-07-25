package com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampDefJxDao;
import com.asiainfo.biapp.pec.plan.jx.camp.enums.CampStatusJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampThemeQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdPrismCampThemeVO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.SceneCampVO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.ThemeCampStatDetail;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dao.IMcdPrismCampThemeDao;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity.McdPrismCampTheme;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.IMcdPrismCampThemeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.ONE_NUMBER;
import static com.asiainfo.biapp.pec.plan.common.Constant.SpecialNumber.ZERO_NUMBER;

/**
 * description:
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
@Service
@Slf4j
public class McdPrismCampThemeServiceImpl extends ServiceImpl<IMcdPrismCampThemeDao,McdPrismCampTheme> implements IMcdPrismCampThemeService {


    @Autowired
    private IMcdPrismCampThemeDao campThemeDao;

    @Resource
    private McdCampDefJxDao mcdCampDefJxDao;


    /**
     * 查询主题信息
     *
     * @param req 入参对象信息
     * @return 主题分页信息
     */
    @Override
    public IPage<McdPrismCampThemeVO> searchThemeCampList(CampThemeQuery req) {
        // 能否提交标识
        boolean submittable = true;
        // 能否修改标识
        boolean modifiable = true;
        // 能否删除标识
        boolean deletable = true;
        // 1. 构造时间过滤参数
        buildDateFilterParam(req);
        // 2. 分页查询主题信息
        IPage<McdPrismCampThemeVO> page = campThemeDao.searchThemeCampList(new Page<>(req.getCurrent(), req.getSize()), req);
        if (CollectionUtil.isEmpty(page.getRecords())) {
            log.info("查询主题信息为空");
            return new Page<>();
        }
        // 3. 获取所有的主题id
        List<String> themeIdList = page.getRecords().stream().map(McdPrismCampThemeVO::getThemeId).collect(Collectors.toList());
        // 4. 根据主题id批量查询主题下的活动
        List<SceneCampVO> campListByThemeIdList = campThemeDao.searchCampListByTheme(themeIdList);
        // 4.1 按主题id分组
        Map<String, List<SceneCampVO>> campMapByThemeId = campListByThemeIdList.stream().collect(Collectors.groupingBy(SceneCampVO::getThemeId));
        // 5. 根据主题批量查询渠道信息以及所有活动的状态及是否是预演策略
        List<ThemeCampStatDetail> channelIdMapByThemeId = campThemeDao.listChannelIdByThemeId(themeIdList);
        // 5.1 按主题id分组
        Map<String, List<ThemeCampStatDetail>> channelIdMap = channelIdMapByThemeId.stream().collect(Collectors.groupingBy(ThemeCampStatDetail::getThemeId));
        // 6. 批量查询主题下的所有活动的状态及是否是预演策略
        Date now = DateUtil.date();
        for (McdPrismCampThemeVO record : page.getRecords()) {
            List<SceneCampVO> sceneCampVOS = campMapByThemeId.get(record.getThemeId());
            // 7. 回填活动列表对象
            record.setCampList(sceneCampVOS);
            // 7.1 查询所有活动是否都过期
            boolean isAllExpired = sceneCampVOS.stream().allMatch(sceneCampVO -> sceneCampVO.getEndDate().before(now));
            log.info("主题={}下策略是否全部过期={}", record.getThemeId(), isAllExpired);
            List<ThemeCampStatDetail> themeCampStatDetails = channelIdMap.get(record.getThemeId());
            String channelIds = themeCampStatDetails.get(ZERO_NUMBER).getChannelId();
            // 8. 回填渠道信息
            record.setChannelIds(Arrays.asList(channelIds.split(StrUtil.COMMA)));
            // 判断主题下的所有活动是否可以批量提交、修改、删除
            // 可批量提交：状态为草稿的非预演活动，状态为预演完成的预演活动
            // 可批量修改删除：状态为草稿/审批退回的非预演活动，状态为草稿/预演完成/预演失败的预演活动
            for (ThemeCampStatDetail detail : themeCampStatDetails) {
                int previewCamp = detail.getPreviewCamp();
                int statId = detail.getCampsegStatId();
                if (previewCamp == ZERO_NUMBER) {
                    //非预演活动，状态不是草稿，不可进行批量提交、修改、删除
                    if (statId != CampStatusJx.DRAFT.getId()) {
                        submittable = false;
                        // 状态不是审批驳回，不可修改、删除
                        if (statId != CampStatusJx.APPROVE_BACK.getId()) {
                            modifiable = false;
                            deletable = false;
                        }
                        break;
                    }
                    // 草稿、审批驳回时 主题下策略是全部过期或个别过期的话  可以修改、删除 不能提交
                    if (((statId == CampStatusJx.DRAFT.getId() || statId == CampStatusJx.APPROVE_BACK.getId()) && isAllExpired)
                        || ((statId == CampStatusJx.DRAFT.getId() || statId == CampStatusJx.APPROVE_BACK.getId()) && !isAllExpired)) {
                        submittable = false;
                        break;
                    }
                    // 草稿、审批驳回时 主题下策略不是全部过期的话  不能提交、修改、删除
                    // if ((statId == CampStatusJx.DRAFT.getId() || statId == CampStatusJx.APPROVE_BACK.getId()) && !isAllExpired) {
                    //     submittable = false;
                    //     modifiable = false;
                    //     deletable = false;
                    //     break;
                    // }
                } else {
                    //预演活动，状态不是预演完成，不可进行批量提交
                    if (statId != CampStatusJx.PREVIEWED.getId()) {
                        submittable = false;
                    }
                    //预演活动，状态不是草稿/预演完成/预演失败/审批驳回、不可进行批量修改、删除
                    if (statId != CampStatusJx.PREVIEWED.getId() && statId != CampStatusJx.PREVIEW_ERROR.getId() && statId != CampStatusJx.DRAFT.getId() && statId != CampStatusJx.APPROVE_BACK.getId()) {
                        modifiable = false;
                        deletable = false;
                    }
                    // 草稿、审批驳回时 主题下策略是全部过期或个别过期的话  可以修改、删除 不能提交
                    if (((statId == CampStatusJx.DRAFT.getId() || statId == CampStatusJx.APPROVE_BACK.getId()) && isAllExpired)
                            || ((statId == CampStatusJx.DRAFT.getId() || statId == CampStatusJx.APPROVE_BACK.getId()) && !isAllExpired)) {
                        submittable = false;
                        break;
                    }
                }
            }
            // 回填按钮状态
            record.setSubmittable(submittable);
            record.setModifiable(modifiable);
            record.setDeletable(deletable);
        }
        return page;
    }

    /**
     * 根据主题ID查询策略
     *
     * @param req
     * @return
     */
    @Override
    public IPage<SceneCampVO> searchCampByTheme(CampThemeQuery req) {
		/*buildDateFilterParam(req);
		Page<SceneCampVO> page = new Page<>(req.getCurrent(), req.getSize());
		IPage<SceneCampVO> result = campThemeDao.searchCampListByTheme(req);

		//设置渠道和策略状态
		result.getRecords().forEach(sceneCampVO -> {
			sceneCampVO.setChannelIds(mcdCampDefJxDao.listChannelIdByCampsegId(sceneCampVO.getCampsegId()));
			sceneCampVO.setCampsegStatName(CampStatusJx.valueOfId(sceneCampVO.getCampsegStatId()));
		});*/
        return null;
    }

    /**
     * 构造时间过滤参数
     *
     * @param req 入参对象
     */
    private void buildDateFilterParam(CampThemeQuery req) {
        if (StrUtil.isNotBlank(req.getCampEndDay())) {
            req.setCampEndDay(req.getCampEndDay() + " 23:59:59");
        }
        String campStartDay = req.getCampStartDay();
        String campEndDay = req.getCampEndDay();
        if (StringUtils.isNotBlank(campStartDay) && campStartDay.contains(StrUtil.COMMA)) {
            req.setCampStartDayStart(campStartDay.split(StrUtil.COMMA)[ZERO_NUMBER]);
            req.setCampStartDayEnd(campStartDay.split(StrUtil.COMMA)[ONE_NUMBER]);
        }
        if (StringUtils.isNotBlank(campEndDay) && campEndDay.contains(StrUtil.COMMA)) {
            req.setCampEndDayStart(campEndDay.split(StrUtil.COMMA)[ZERO_NUMBER]);
            req.setCampEndDayEnd(campEndDay.split(StrUtil.COMMA)[ONE_NUMBER]);
        }
    }
}
