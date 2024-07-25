package com.asiainfo.biapp.pec.element.jx.service;


import com.asiainfo.biapp.pec.element.dto.request.DimChannelPageListRequest;
import com.asiainfo.biapp.pec.element.jx.entity.McdDimChannelJx;
import com.asiainfo.biapp.pec.element.jx.query.DimChannelPageListRequestJx;
import com.asiainfo.biapp.pec.element.jx.vo.DimChannelListRequestJx;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 * 渠道表 服务类
 * </p>
 *
 * @author wuhq6
 * @since 2021-11-23
 */
public interface IMcdDimChannelJxService extends IService<McdDimChannelJx> {


    /**
     * 新增或更新渠道信息
     *
     * @param mcdDimChannel 入参对象
     * @return true/false
     */
    boolean saveOrUpdateDimChannel(McdDimChannelJx mcdDimChannel);

    /**
     * 渠道(或根据条件)分页查询
     *
     * @param dimChannelPageListRequest 入参对象
     * @return 渠道分页结果集
     */
    IPage<McdDimChannelJx> pagelistDimChannel(DimChannelPageListRequestJx dimChannelPageListRequest);

    /**
     * 根据用户ID查询渠道权限
     *
     * @param userId
     * @return
     */
    List<String> listChannelByUserId(String userId);

    /**
     * 根据上线状态获取渠道列表
     *
     * @param dimChannelListRequest
     * @return
     */
    List<McdDimChannelJx> getDimChannelList(DimChannelListRequestJx dimChannelListRequest);


}
