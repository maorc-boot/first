package com.asiainfo.biapp.pec.preview.jx.mapper;

import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustConf;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 渠道敏感客户群配置表 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2022-10-12
 */
public interface McdChannelSensitiveCustConfMapper extends BaseMapper<McdChannelSensitiveCustConf> {

    /**
     * 查询渠道敏感客户群信息
     *
     * @param channelId 渠道ID
     * @return
     */
    IPage<McdChannelSensitiveCustInfo> querySensitiveCustInfo(Page<?> page, @Param("channelId") String channelId ,@Param("keywords")String keywords ,@Param("custgroupType")String custgroupType);
}
