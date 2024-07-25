package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.asiainfo.biapp.pec.client.jx.preview.model.McdCampPreview;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampDefJxDao;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ICampPreveiwJxService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.feign.PreviewJxFeignClient;
import com.asiainfo.biapp.pec.plan.model.McdCampChannelList;
import com.asiainfo.biapp.pec.plan.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.service.IMcdCustgroupDefService;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.asiainfo.biapp.pec.plan.vo.CustgroupDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mamp
 * @date 2022/10/20
 */
@Service
@Slf4j
public class CampPreveiwJxServiceImpl implements ICampPreveiwJxService {

    @Autowired
    private PreviewJxFeignClient previewJxFeignClient;

    @Autowired
    private IMcdCustgroupDefService custgroupDefService;

    @Resource
    private McdCampDefJxDao mcdCampDefJxDao;

    /**
     * 生成预演信息
     *
     * @param campDef
     * @param campChannelLists
     */
    @Override
    public void savePreview(McdCampDef campDef, List<McdCampChannelList> campChannelLists) {
        try {
            if (CollectionUtil.isEmpty(campChannelLists)) {
                return;
            }
            // 先删除历史数据
            previewJxFeignClient.deletePreviewByRootId(campChannelLists.get(0).getCampsegRootId());
            // 预演策略: 1-是,0-否
            if (!"1".equalsIgnoreCase(campDef.getPreviewCamp())) {
                log.info("活动[{}]不需要预演", campDef.getCampsegId());
                return;
            }
            // 支持预演的渠道Ids
            List<String> previewChannelIds = mcdCampDefJxDao.queryPreviewChannelIds();
            if(CollectionUtil.isEmpty(previewChannelIds)){
                log.warn("没有查询到支持预演的渠道,不进行预演");
                return ;
            }
            List<McdCampPreview> mcdCampPreviews = new ArrayList<>();
            campChannelLists.stream().forEach(campList -> {
                if(!previewChannelIds.contains(campList.getChannelId())){
                    log.info("渠道{}不支持预演，不生成预演表数据");
                    return ;
                }
                if(campList.getCampClass() != 1){
                    return ;
                }
                McdCampPreview mcdCampPreview = new McdCampPreview();
                mcdCampPreview.setId(IdUtils.generateId());
                mcdCampPreview.setCampsegId(campList.getCampsegId());
                mcdCampPreview.setCampsegRootId(campList.getCampsegRootId());
                mcdCampPreview.setChannelId(campList.getChannelId());
                mcdCampPreview.setCustgroupId(campList.getCustgroupId());
                CustgroupDetailVO custgroupDetailVO = custgroupDefService.detailCustgroup(campList.getCustgroupId());
                if (ObjectUtil.isNotEmpty(custgroupDetailVO)) {
                    // 客户群清单数量
                    mcdCampPreview.setTargetCustomNum(custgroupDetailVO.getCustomNum());
                } else {
                    // dna客群时，保存客群定义表是异步操作，这里会空指针，先赋值0
                    mcdCampPreview.setTargetCustomNum(0);
                }
                mcdCampPreview.setCreateUser(campDef.getCreateUserId());
                mcdCampPreview.setPreviewTime(new Date());
                mcdCampPreviews.add(mcdCampPreview);
            });
            previewJxFeignClient.createPreview(mcdCampPreviews);
        } catch (Exception e) {
            log.error("保存预演数据失败:", e);
        }
    }
}
