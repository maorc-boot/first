package com.asiainfo.biapp.pec.element.jx.service;

import com.asiainfo.biapp.pec.element.dto.request.DimContactPageListRequest;
import com.asiainfo.biapp.pec.element.dto.response.ConctactListResponse;
import com.asiainfo.biapp.pec.element.dto.response.DimContactDetailResponse;
import com.asiainfo.biapp.pec.element.jx.entity.McdDimContactJx;
import com.asiainfo.biapp.pec.element.jx.vo.DimContactDetailResponseJx;
import com.asiainfo.biapp.pec.element.jx.vo.DimContactPageListRequestJx;
import com.asiainfo.biapp.pec.element.model.McdDimContact;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 触点信息表 服务类
 * </p>
 *
 * @author wuhq6
 * @since 2021-11-20
 */
public interface IMcdDimContactJxService extends IService<McdDimContactJx> {
    
    /**
     * 触点信息列表分页查询或根据条件查询
     *
     * @param dimContactPageListRequest 条件入参
     * @return 触点列表信息
     */
    IPage<DimContactDetailResponseJx> pagelistMcdDimContact(DimContactPageListRequestJx dimContactPageListRequest);
    
    /**
     * 根据渠道id查询所有触点信息
     *
     * @param dimContactPageListRequest - 条件入参
     * @return 触点信息列表
     */
    IPage<McdDimContactJx> pagelistContactByChannelId(DimContactPageListRequest dimContactPageListRequest);
    
    /**
     * 根据触点id查询触点详情
     *
     * @param contactId 触点id
     * @return 触点信息
     */
    DimContactDetailResponseJx getContactDetailById(String contactId);
    
    /**
     * 根据id 触点获取基本信息
     *
     * @param contactId
     * @return
     */
    McdDimContactJx getContactBaseById(String contactId);
    
    /**
     * 判断触点名称(新建)
     *
     * @param contactName 要保存触点名称
     * @return true/false
     */
    boolean getValidateContactName(String contactName);
    
    /**
     * 判断触点名称(修改)
     *
     * @param mcdDimContact
     * @return true/false
     */
    boolean getValidateContactNameAndId(McdDimContact mcdDimContact);
    
    /**
     * 保存或更新触点
     *
     * @param mcdDimContact 入参对象
     * @return true/false
     */
    boolean saveOrUpdateContact(McdDimContactJx mcdDimContact);
    
    /**
     * 根据触点id 删除触点信息
     *
     * @param contactId 触点 id
     * @return true/false
     */
    boolean removeContact(String contactId);
    
    /**
     * 触点 下拉框信息查询
     *
     * @param channelId
     * @return
     */
    ConctactListResponse listContactInfo(String channelId);
    
    /**
     * 根据渠道id 获取 Contact列表
     *
     * @param channelId
     * @return
     */
    List<McdDimContactJx> getContactList(String channelId);
    
    
    /**
     * 查询渠道与触点关系,通过渠道Id
     *
     * @param channelId
     * @return
     */
    List<DimContactDetailResponse> queryChannelContactByChannelId(String channelId);
}
