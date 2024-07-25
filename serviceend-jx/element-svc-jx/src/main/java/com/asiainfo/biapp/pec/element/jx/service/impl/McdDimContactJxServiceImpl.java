package com.asiainfo.biapp.pec.element.jx.service.impl;

import com.asiainfo.biapp.pec.element.dao.McdDimAdivInfoDao;
import com.asiainfo.biapp.pec.element.dao.McdDimContactDao;
import com.asiainfo.biapp.pec.element.dobo.ContactInfoBO;
import com.asiainfo.biapp.pec.element.dto.request.DimContactPageListRequest;
import com.asiainfo.biapp.pec.element.dto.response.ConctactListResponse;
import com.asiainfo.biapp.pec.element.dto.response.DimAdivInfoListResponse;
import com.asiainfo.biapp.pec.element.dto.response.DimContactDetailResponse;
import com.asiainfo.biapp.pec.element.jx.entity.McdDimContactJx;
import com.asiainfo.biapp.pec.element.jx.mapper.McdDimContactJxDao;
import com.asiainfo.biapp.pec.element.jx.service.IMcdDimContactJxService;
import com.asiainfo.biapp.pec.element.jx.vo.DimContactDetailResponseJx;
import com.asiainfo.biapp.pec.element.jx.vo.DimContactPageListRequestJx;
import com.asiainfo.biapp.pec.element.model.McdDimChannel;
import com.asiainfo.biapp.pec.element.model.McdDimContact;
import com.asiainfo.biapp.pec.element.service.IMcdDimAdivInfoService;
import com.asiainfo.biapp.pec.element.service.IMcdDimChannelService;
import com.asiainfo.biapp.pec.element.util.MpmUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 触点信息表 服务实现类
 * </p>
 *
 * @author wuhq6
 * @since 2021-11-20
 */
@Service
public class McdDimContactJxServiceImpl extends ServiceImpl<McdDimContactJxDao, McdDimContactJx> implements IMcdDimContactJxService {
    
    @Resource
    private IMcdDimChannelService channelService;
    
    @Resource
    private McdDimContactDao mcdDimContactDao;

    @Resource
    private McdDimContactJxDao mcdDimContactJxDao;


    
    @Resource
    private McdDimAdivInfoDao mcdDimAdivInfoDao;
    /**
     * 触点信息列表分页查询或根据条件查询
     *
     * @param dimContactPageListRequest 条件入参
     * @return 触点列表信息
     */
    @Override
    public IPage<DimContactDetailResponseJx> pagelistMcdDimContact(DimContactPageListRequestJx dimContactPageListRequest) {
        Page<McdDimContactJx> page = new Page<>(dimContactPageListRequest.getCurrent(), dimContactPageListRequest.getSize());
        //时间范围处理
        String star = "";
        String end = "";
        if (StringUtils.isNotBlank(dimContactPageListRequest.getStartDate())&&StringUtils.isNotBlank(dimContactPageListRequest.getEndDate())){
            star = dimContactPageListRequest.getStartDate() + " 00:00:00";
            end = dimContactPageListRequest.getEndDate() + " 23:59:59";
        }

        IPage<McdDimContactJx>  mcdDimContactPage = page(page, Wrappers.<McdDimContactJx>query().lambda()
                .like(StringUtils.isNotBlank(dimContactPageListRequest.getKeywords()), McdDimContactJx::getContactName, dimContactPageListRequest.getKeywords())
                .eq(StringUtils.isNotBlank(dimContactPageListRequest.getChannelId()), McdDimContactJx::getChannelId, dimContactPageListRequest.getChannelId())
                .eq(dimContactPageListRequest.getContactSource() != null, McdDimContactJx::getContactSource, dimContactPageListRequest.getContactSource())
                .between(StringUtils.isNotBlank(dimContactPageListRequest.getStartDate()) && StringUtils.isNotBlank(dimContactPageListRequest.getEndDate()), McdDimContactJx::getCreateTime, star,end)
                .orderByDesc(McdDimContactJx::getCreateTime)
        );
    
        List<McdDimContactJx> records = mcdDimContactPage.getRecords();
        List<DimContactDetailResponseJx> contactList = new ArrayList<>();
        for (McdDimContactJx item : records) {
            DimContactDetailResponseJx response = new DimContactDetailResponseJx();
            BeanUtils.copyProperties(item,response);
            if (StringUtils.isNotBlank(item.getChannelId())){
                McdDimChannel dimChannel = channelService.getDimChannel(item.getChannelId());
                String channelName = dimChannel != null ? StringUtils.isNotBlank(dimChannel.getChannelName()) ? dimChannel.getChannelName() : "" : "";
                response.setChannelName(channelName);
            }else {
                response.setContactName("");
            }
            contactList.add(response);
        }
    
        IPage<DimContactDetailResponseJx> pages = new Page<>();
        
        pages.setPages(mcdDimContactPage.getPages());
        pages.setCurrent(mcdDimContactPage.getCurrent());
        pages.setRecords(contactList);
        pages.setTotal(mcdDimContactPage.getTotal());
        pages.setSize(mcdDimContactPage.getSize());
        return pages;
    }
    
    /**
     * 根据渠道id查询所有触点信息
     *
     * @param dimContactPageListRequest - 条件入参
     * @return 触点信息列表
     */
    @Override
    public IPage<McdDimContactJx> pagelistContactByChannelId(DimContactPageListRequest dimContactPageListRequest) {
        Page<McdDimContactJx> page = new Page<>(dimContactPageListRequest.getCurrent(),dimContactPageListRequest.getSize());
        IPage<McdDimContactJx>  mcdDimContactPage = page(page, Wrappers.<McdDimContactJx>query().lambda()
                .eq(StringUtils.isNotBlank(dimContactPageListRequest.getChannelId()), McdDimContactJx::getChannelId,dimContactPageListRequest.getChannelId())
        );
        return mcdDimContactPage;
    }
    
    /**
     * 根据触点id查询触点详情
     *
     * @param contactId 触点id
     * @return 触点信息
     */
    @Override
    public DimContactDetailResponseJx getContactDetailById(String contactId) {
        DimContactDetailResponseJx response = new DimContactDetailResponseJx();
        McdDimContactJx mcdDimContact = baseMapper.selectById(contactId);
        BeanUtils.copyProperties(mcdDimContact,response);
        if (StringUtils.isNotBlank(mcdDimContact.getChannelId())){
            McdDimChannel dimChannel = channelService.getDimChannel(mcdDimContact.getChannelId());
            String channelName = dimChannel != null ? StringUtils.isNotBlank(dimChannel.getChannelName()) ? dimChannel.getChannelName() : "" : "";
            response.setChannelName(channelName);
        }else {
            response.setContactName("");
        }
        return response;
    }
    
    @Override
    public McdDimContactJx getContactBaseById(String contactId) {
        return baseMapper.selectById(contactId);
    }
    
    /**
     * 判断触点名称(新建)
     *
     * @param contactName 要保存触点名称
     * @return true/false
     */
    @Override
    public boolean getValidateContactName(String contactName) {
        LambdaQueryWrapper<McdDimContactJx> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdDimContactJx::getContactName,contactName);
        Integer count = getBaseMapper().selectCount(wrapper);
        return count == 0;
    }
    
    /**
     * 判断触点名称(修改)
     *
     * @param mcdDimContact
     * @return true/false
     */
    @Override
    public boolean getValidateContactNameAndId(McdDimContact mcdDimContact) {
        LambdaQueryWrapper<McdDimContactJx> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdDimContactJx::getContactId,mcdDimContact.getContactId());
        McdDimContactJx dimContact = baseMapper.selectOne(wrapper);
        if (StringUtils.equals(mcdDimContact.getContactName(),dimContact.getContactName())){
            return true;
        }else {
            LambdaQueryWrapper<McdDimContactJx> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(McdDimContactJx::getContactName,mcdDimContact.getContactName());
            //Integer count = getBaseMapper().selectCount(wrapper1);
            McdDimContactJx contact = baseMapper.selectOne(wrapper1);
            return contact == null;
        }
    }
    
    /**
     * 保存或更新触点
     *
     * @param mcdDimContact 入参对象
     * @return true/false
     */
    @Override
    public boolean saveOrUpdateContact(McdDimContactJx mcdDimContact) {
        
        String contactId = mcdDimContact.getContactId();
        if (StringUtils.isBlank(contactId)) {
            mcdDimContact.setContactId(MpmUtil.generateCampsegAndTaskNo());
            return mcdDimContactJxDao.saveContactInfo(mcdDimContact);
        } else {
            boolean re = mcdDimContactJxDao.updateContactInfo(mcdDimContact);
            McdDimContact vo = new McdDimContact();
            BeanUtils.copyProperties(mcdDimContact, vo);
            mcdDimAdivInfoDao.updateAdivByContactId(vo);
            return re;
        }
    }
    
    @Autowired
    private IMcdDimAdivInfoService mcdDimAdivInfoService;
    /**
     * 删除触点信息
     *
     * @param contactId 触点 id
     * @return true/false
     */
    @Override
    public boolean removeContact(String contactId) {
        List<DimAdivInfoListResponse> adivInfoList = mcdDimAdivInfoService.queryContactAdivByContactId(contactId);
        // 没有关联的运营位,就删除
        if (CollectionUtils.isEmpty(adivInfoList)) {
            return removeById(contactId);
        }
        return false;
    }
    
    /**
     * 触点 下拉框信息查询
     *
     * @param channelId
     * @return
     */
    @Override
    public ConctactListResponse listContactInfo(String channelId) {
        ConctactListResponse response = new ConctactListResponse();
        LambdaQueryWrapper<McdDimContactJx> lambdaWrapper = new LambdaQueryWrapper<>();
        lambdaWrapper.eq(StringUtils.isNotBlank(channelId), McdDimContactJx::getChannelId,channelId);
        
        List<ContactInfoBO> list = baseMapper.selectList(lambdaWrapper).stream().map(e -> new ContactInfoBO(
                e.getContactId(), e.getContactName(), e.getChannelId()
        )).collect(Collectors.toList());
        response.setContactInfoList(list);
        return response;
    }
    
    /**
     * 根据渠道id 获取 Contact列表
     *
     * @param channelId
     * @return
     */
    @Override
    public List<McdDimContactJx> getContactList(String channelId) {
        LambdaQueryWrapper<McdDimContactJx> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdDimContactJx::getChannelId,channelId);
        return baseMapper.selectList(wrapper);
    }
    
    /**
     * 查询渠道与触点关系,通过渠道Id
     *
     * @param channelId
     * @return
     */
    @Override
    public List<DimContactDetailResponse> queryChannelContactByChannelId(String channelId) {
        return mcdDimContactDao.queryChannelContactByChannelId(channelId);
    }
}
