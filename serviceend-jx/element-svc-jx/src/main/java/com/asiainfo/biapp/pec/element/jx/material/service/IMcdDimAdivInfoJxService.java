package com.asiainfo.biapp.pec.element.jx.material.service;

import com.asiainfo.biapp.pec.element.dto.request.AdivCampInfoListRequest;
import com.asiainfo.biapp.pec.element.dto.request.DimAdivInfoRequest;
import com.asiainfo.biapp.pec.element.dto.request.DimAdivPageListRequest;
import com.asiainfo.biapp.pec.element.dto.response.*;
import com.asiainfo.biapp.pec.element.jx.material.request.DimAdivInfoJxQuery;
import com.asiainfo.biapp.pec.element.model.McdDimAdivInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  江西运营位管理 服务类
 * </p>
 *
 * @author ranpf
 * @since 2023-1-3
 */
public interface IMcdDimAdivInfoJxService extends IService<McdDimAdivInfo> {

    /**
     * 根据运营位id查询运营位表详情
     *
     * @param adivId 运营位id
     * @return 运营位详情
     */
    McdDimAdivInfo getAdivInfo(String adivId);
    /**
     * 根据运营位id及渠道id查询运营位表详情
     *
     * @param adivId 运营位id
     * @return 运营位详情
     */
    McdDimAdivInfo getAdivInfoByAdivIdAndChannelId(String adivId, String channelId);

    /**
     * 新增或更新运营位
     *
     * @param mcdDimAdivInfo 入参对象
     * @return true/false
     */
    boolean saveOrUpdateAdivInfo(McdDimAdivInfo mcdDimAdivInfo);

    /**
     * 删除运营位
     *
     * @param adivId 运营位id
     * @return true/false
     */
    boolean removeAdivInfo(String adivId);

    /**
     * 运营位(或根据条件)分页查询
     *
     * @param dimAdivPageListRequest 入参条件
     * @return 运营位列表分页对象
     */
    IPage<McdDimAdivInfo> pagelistAdivInfo1(DimAdivPageListRequest dimAdivPageListRequest);

    /**
     * 运营位 下拉框信息查询
     *
     * @param dimAdivInfoRequest
     * @return
     */
    AdivInfoListResponse listPositionInfo(DimAdivInfoJxQuery dimAdivInfoRequest);

    /**
     * 运营位(或根据条件)分页查询
     *
     * @param dimAdivPageListRequest 入参对象
     * @return
     */
    IPage<DimAdivInfoListResponse> pagelistAdivInfo(DimAdivPageListRequest dimAdivPageListRequest);

    /**
     * 校验运营位名称(新建)
     *
     * @param adivName
     * @return
     */
    boolean getValidateAdivInfoName(String adivName, String adivId);

    /**
     * 校验运营位名称(新建)
     * 名称校验条件为：运营位ID加渠道ID
     * @param adivName
     * @return
     */
    boolean getValidateAdivInfoNameAndChannelId(String adivName, String adivId, String channelId);
    
    /**
     * 校验运营位名称(修改)
     *
     * @param mcdDimAdivInfo
     * @return
     */
    boolean getValidateAdivInfoNameAndId(McdDimAdivInfo mcdDimAdivInfo);

    /**
     * 校验运营位名称(修改)
     * 名称校验条件为：运营位ID加渠道ID
     * @param mcdDimAdivInfo
     * @return
     */
    boolean getValidateAdivInfoNameAndChannelId(McdDimAdivInfo mcdDimAdivInfo);
    
    /**
     * 查询该运营位下的执行活动列表
     *
     * @param adivCampInfoListRequest
     * @return
     */
    //IPage<AdivCampInfoListResponse> listpageCampsegInfoWithThisAdivId(AdivCampInfoListRequest adivCampInfoListRequest);
    IPage<CampManage> listpageCampsegInfoWithThisAdivId(AdivCampInfoListRequest adivCampInfoListRequest);
    /**
     * 根据运营位id查询运营位及相关详情
     *
     * @return
     */
    DimAdivInfoDetailResponse getAdivInfoById(DimAdivInfoRequest dimAdivInfoRequest);
    
    /**
     * 获取渠道信息及相关联的触点信息
     *
     * @return
     */
    List<AdivInfoChannelAndContactResponse> listChannelInfoAndContactInfo();
    
    /**
     * 根据触点id 查询运营位列表
     *
     * @param contactId
     * @return
     */
    List<DimAdivInfoListResponse> queryContactAdivByContactId(String contactId);
    
    /**
     * 统计被运营位关联的配置
     * @param confId
     * @return
     */
    Integer selectCountByConfId(Long confId);
}
