package com.asiainfo.biapp.pec.element.jx.material.service.impl;

import com.asiainfo.biapp.pec.element.dao.McdDimAdivInfoDao;
import com.asiainfo.biapp.pec.element.dobo.AdivInfoBO;
import com.asiainfo.biapp.pec.element.dobo.ChannelNameAndIdBO;
import com.asiainfo.biapp.pec.element.dobo.ContactInfoBO;
import com.asiainfo.biapp.pec.element.dto.request.AdivCampInfoListRequest;
import com.asiainfo.biapp.pec.element.dto.request.DimAdivInfoRequest;
import com.asiainfo.biapp.pec.element.dto.request.DimAdivPageListRequest;
import com.asiainfo.biapp.pec.element.dto.response.*;
import com.asiainfo.biapp.pec.element.jx.material.request.DimAdivInfoJxQuery;
import com.asiainfo.biapp.pec.element.jx.material.service.IMcdDimAdivInfoJxService;
import com.asiainfo.biapp.pec.element.model.McdDimAdivInfo;
import com.asiainfo.biapp.pec.element.model.McdDimContact;
import com.asiainfo.biapp.pec.element.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  运营位管理 服务实现类
 * </p>
 *
 * @author wuhq6
 * @since 2021-11-23
 */
@Service
@Slf4j
public class McdDimAdivInfoJxServiceImpl extends ServiceImpl<McdDimAdivInfoDao, McdDimAdivInfo> implements IMcdDimAdivInfoJxService {
    
    @Resource
    private McdDimAdivInfoDao mcdDimAdivInfoDao;
    
    @Resource
    private IMcdDimChannelService mcdDimChannelService;
    
    @Resource
    private IMcdDimContactService mcdDimContactService;
    
    @Resource
    private IMcdCampDefService mcdCampDefService;
    
    @Resource
    private IMcdCampChannelListService mcdCampChannelListService;
    
    /**
     * 根据运营位id查询详情
     *
     * @param adivId 运营位id
     * @return 运营位详情
     */
    @Override
    public McdDimAdivInfo getAdivInfo(String adivId) {
        return baseMapper.selectById(adivId);
    }
    /**
     * 根据运营位id及渠道id查询运营位表详情
     * @param adivId  渠道id
     * @param adivId 运营位id
     * @return 运营位详情
     */
    @Override
    public McdDimAdivInfo getAdivInfoByAdivIdAndChannelId(String adivId,String channelId) {
        LambdaQueryWrapper<McdDimAdivInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(McdDimAdivInfo::getAdivId, adivId).eq(McdDimAdivInfo::getChannelId,channelId);
        return baseMapper.selectOne(queryWrapper);
    }
    
    /**
     * 新增或更新运营位
     *
     * @param mcdDimAdivInfo 入参对象
     * @return true/false
     */
    @Override
    public boolean saveOrUpdateAdivInfo(McdDimAdivInfo mcdDimAdivInfo) {
        int re = 0;
        if (StringUtils.isBlank(mcdDimAdivInfo.getAdivCode())){
            //新建运营位
            re = mcdDimAdivInfoDao.saveAdivInfo(mcdDimAdivInfo);
        } else {
            //修改运营位
            re = mcdDimAdivInfoDao.updateAdivInfo(mcdDimAdivInfo,mcdDimAdivInfo.getAdivId());
        }
        return re == 1;
    }
    
    
    /**
     * 校验运营位名称(新建)
     *
     * @param adivName
     * @return
     */
    @Override
    public boolean getValidateAdivInfoName(String adivName,String adivId) {
        //LambdaQueryWrapper<McdDimAdivInfo> wrapper = new LambdaQueryWrapper<>();
        //wrapper.eq(McdDimAdivInfo::getAdivName,adivName);
        //Integer count = getBaseMapper().selectCount(wrapper);
        //return count == 0;
        LambdaQueryWrapper<McdDimAdivInfo>  wrapperById = new LambdaQueryWrapper<>();
        wrapperById.eq(McdDimAdivInfo::getAdivId,adivId);
        //Integer countById = baseMapper.selectCount(wrapperById);
        McdDimAdivInfo mcdDimAdivInfo = baseMapper.selectOne(wrapperById);
        if (null != mcdDimAdivInfo) {
            LambdaQueryWrapper<McdDimAdivInfo> wrapperByName = new LambdaQueryWrapper<>();
            wrapperByName.eq(McdDimAdivInfo::getAdivName,adivName);
            //Integer countByName = getBaseMapper().selectCount(wrapperByName);
            McdDimAdivInfo dimAdivInfo = baseMapper.selectOne(wrapperByName);
            return dimAdivInfo == null;
        }
        return true;
    }

    /**
     * 校验运营位名称(新建)
     * 校验条件为：运营位ID加渠道ID
     * @param adivName
     * @return
     */
    @Override
    public boolean getValidateAdivInfoNameAndChannelId(String adivName,String adivId,String channelId) {
        LambdaQueryWrapper<McdDimAdivInfo>  wrapperById = new LambdaQueryWrapper<>();
        wrapperById.eq(McdDimAdivInfo::getAdivId,adivId);
        wrapperById.eq(McdDimAdivInfo::getChannelId,channelId);
        McdDimAdivInfo mcdDimAdivInfo = baseMapper.selectOne(wrapperById);
        //运营位ID加渠道ID存在则直接返回false,否则再去校验该渠道下是否存在名称一样的
        if (null != mcdDimAdivInfo) {
            return false;
        }else{
            LambdaQueryWrapper<McdDimAdivInfo> wrapperByName = new LambdaQueryWrapper<>();
            wrapperByName.eq(McdDimAdivInfo::getAdivName,adivName);
            wrapperByName.eq(McdDimAdivInfo::getChannelId,channelId);
            McdDimAdivInfo dimAdivInfo = baseMapper.selectOne(wrapperByName);
            return dimAdivInfo == null;
        }
    }
    
    /**
     * 校验运营位名称(修改)
     * @param mcdDimAdivInfo
     * @return
     */
    @Override
    public boolean getValidateAdivInfoNameAndId(McdDimAdivInfo mcdDimAdivInfo) {
        LambdaQueryWrapper<McdDimAdivInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdDimAdivInfo::getAdivId,mcdDimAdivInfo.getAdivId());
        McdDimAdivInfo mcdDimAdiv = baseMapper.selectOne(wrapper);
        if (StringUtils.equals(mcdDimAdiv.getAdivName(),mcdDimAdivInfo.getAdivName())){
            return true;
        }else {
            LambdaQueryWrapper<McdDimAdivInfo> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(McdDimAdivInfo::getAdivName,mcdDimAdivInfo.getAdivName());
            Integer count = getBaseMapper().selectCount(wrapper2);
            return count == 0;
        }
    }

    /**
     * 校验运营位名称(修改) 校验条件为：运营位ID加渠道ID
     * @param mcdDimAdivInfo
     * @return
     */
    @Override
    public boolean getValidateAdivInfoNameAndChannelId(McdDimAdivInfo mcdDimAdivInfo) {
        LambdaQueryWrapper<McdDimAdivInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdDimAdivInfo::getAdivId,mcdDimAdivInfo.getAdivId());
        wrapper.eq(McdDimAdivInfo::getChannelId,mcdDimAdivInfo.getChannelId());
        McdDimAdivInfo mcdDimAdiv = baseMapper.selectOne(wrapper);
        if (StringUtils.equals(mcdDimAdiv.getAdivName(),mcdDimAdivInfo.getAdivName())){
            return true;
        }else {
            LambdaQueryWrapper<McdDimAdivInfo> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(McdDimAdivInfo::getAdivName,mcdDimAdivInfo.getAdivName());
            wrapper2.eq(McdDimAdivInfo::getChannelId,mcdDimAdivInfo.getChannelId());
            Integer count = getBaseMapper().selectCount(wrapper2);
            return count == 0;
        }
    }
    
    /**
     * 删除运营位
     *
     * @param adivId 运营位id
     * @return true/false
     */
    @Override
    public boolean removeAdivInfo(String adivId) {
        //查询 运营位id是否被关联活动中的
        Integer count = mcdCampChannelListService.queryChannelListByAdiv(adivId);
        if (count == 0) {
            return removeById(adivId);
        }
        return  false;
    }
    
    /**
     * 运营位(或根据条件)分页查询
     *
     * @param dimAdivPageListRequest 入参条件
     * @return 运营位列表分页对象
     */
    @Override
    public IPage<McdDimAdivInfo> pagelistAdivInfo1(DimAdivPageListRequest dimAdivPageListRequest) {
        Page<McdDimAdivInfo> page =new Page<>(dimAdivPageListRequest.getCurrent(),dimAdivPageListRequest.getSize());
        
        IPage<McdDimAdivInfo> mcdDimAdivInfoIPage = page(page, Wrappers.<McdDimAdivInfo>query().lambda()
                .like(StringUtils.isNotBlank(dimAdivPageListRequest.getKeywords()),
                        McdDimAdivInfo::getAdivName,dimAdivPageListRequest.getKeywords())
                .eq(StringUtils.isNotBlank(dimAdivPageListRequest.getChannelId()),
                        McdDimAdivInfo::getChannelId,dimAdivPageListRequest.getChannelId())
                .eq(StringUtils.isNotBlank(dimAdivPageListRequest.getContactId()),
                        McdDimAdivInfo::getContactId,dimAdivPageListRequest.getContactId())
        );
        
        return mcdDimAdivInfoIPage;
    }

    /**
     * 运营位 下拉框信息查询
     *
     * @param jxQuery
     * @return
     */
    @Override
    public AdivInfoListResponse listPositionInfo(DimAdivInfoJxQuery jxQuery) {
        AdivInfoListResponse response = new AdivInfoListResponse();
        LambdaQueryWrapper<McdDimAdivInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotBlank(jxQuery.getChannelId()), McdDimAdivInfo::getChannelId, jxQuery.getChannelId()) ;

        final List<AdivInfoBO> list = baseMapper.selectList(lambdaQueryWrapper).stream().map(e -> {
            List<String> adivSizes = new ArrayList<>();
            adivSizes.add(e.getAdivSize());
            adivSizes.add(e.getAdivSize2());
            return new AdivInfoBO(e.getAdivId(), e.getAdivName(), e.getAdivSize(), e.getChannelId(),
                e.getContactId(), adivSizes);
        }).collect(Collectors.toList());
        response.setAdivInfoList(list);
        return response;
    }
    
    /**
     * 运营位(或根据条件)分页查询
     *
     * @param dimAdivPageListRequest 入参对象
     * @return
     */
    @Override
    public IPage<DimAdivInfoListResponse> pagelistAdivInfo(DimAdivPageListRequest dimAdivPageListRequest) {
        Page<DimAdivInfoListResponse> page =
                new Page<>(dimAdivPageListRequest.getCurrent(),dimAdivPageListRequest.getSize());
        IPage<DimAdivInfoListResponse> list = mcdDimAdivInfoDao.selectByResquest(page,dimAdivPageListRequest);
        List<DimAdivInfoListResponse> records = list.getRecords();
        for (DimAdivInfoListResponse record : records) {
            String adivId = record.getAdivId();
            String channelId = record.getChannelId();
            log.info("运营位id【{}】，渠道id【{}】",adivId,channelId);
            Integer count = mcdDimAdivInfoDao.countCampByAdivIdAndChannelId(adivId,channelId);
            if (count!=null){
                record.setExcutingCampNo(count);
            } else {
                record.setExcutingCampNo(0);
            }
        }
        list.setRecords(records);
        return list;
    }
    
    /**
     * 查询该运营位下的执行活动列表
     *
     * @param adivCampInfoListRequest
     * @return
     */
    @Override
    public IPage<CampManage> listpageCampsegInfoWithThisAdivId(AdivCampInfoListRequest adivCampInfoListRequest) {
        return  mcdCampDefService.selectByThisAdivId(adivCampInfoListRequest);
    }
    
    /**
     * 根据运营位id查询运营位及相关详情
     *
     * @return
     */
    @Override
    public DimAdivInfoDetailResponse getAdivInfoById(DimAdivInfoRequest dimAdivInfoRequest) {
        return mcdDimAdivInfoDao.selectInfoByAdivId(dimAdivInfoRequest);
    }
    
    /**
     * 获取渠道信息及相关联的触点信息
     *
     * @return
     */
    @Override
    public List<AdivInfoChannelAndContactResponse> listChannelInfoAndContactInfo() {
        List<AdivInfoChannelAndContactResponse> resultList = new ArrayList<>();
        ChannelNameAndIdListResponse nameAndIdListResponse = mcdDimChannelService.listAllChannelNameAndId();
        List<ChannelNameAndIdBO> channelNameAndIdList = nameAndIdListResponse.getChannelNameAndIdList();
        for (ChannelNameAndIdBO channelNameAndIdBO : channelNameAndIdList) {
            AdivInfoChannelAndContactResponse result = new AdivInfoChannelAndContactResponse();
            result.setChannelId(channelNameAndIdBO.getChannelId());
            result.setChannelName(channelNameAndIdBO.getChannelName());
            List<McdDimContact> contactInfos =mcdDimContactService.getContactList(channelNameAndIdBO.getChannelId());
            List<ContactInfoBO> list =new ArrayList<>();
            if (contactInfos.size() > 0) {
                for (McdDimContact info : contactInfos) {
                    ContactInfoBO contactInfo = new ContactInfoBO();
                    contactInfo.setContactId(info.getContactId());
                    contactInfo.setContactName(info.getContactName());
                    list.add(contactInfo);
                }
                result.setContactList(list);
            } else {
                result.setContactList(list);
            }
            resultList.add(result);
        }
        return resultList;
    }
    
    
    /**
     * 根据触点id 查询运营位列表
     *
     * @param contactId
     * @return
     */
    @Override
    public List<DimAdivInfoListResponse> queryContactAdivByContactId(String contactId) {
        return mcdDimAdivInfoDao.queryContactAdivByContactId(contactId);
    }
    
    /**
     * 统计被运营位关联的配置
     *
     * @param confId
     * @return
     */
    @Override
    public Integer selectCountByConfId(Long confId) {
        LambdaQueryWrapper<McdDimAdivInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdDimAdivInfo::getConfigId,confId);
        return mcdDimAdivInfoDao.selectCount(wrapper);
    }
}
