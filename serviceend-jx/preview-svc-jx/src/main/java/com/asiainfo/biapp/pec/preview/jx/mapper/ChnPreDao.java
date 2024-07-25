package com.asiainfo.biapp.pec.preview.jx.mapper;

import com.asiainfo.biapp.pec.preview.jx.entity.McdChnPreUserNum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mamp
 * @date 2022/6/9
 */
public interface ChnPreDao extends BaseMapper<McdChnPreUserNum> {

    /**
     * 查询所有渠道偏好模型中的渠道编码
     *
     * @return
     */
    List<String> queryChannelIds();

    /**
     * 根据渠道ID查询，偏好数据
     *
     * @param channelId 渠道ID
     * @return
     */

    List<String> queryUserListByChnId(@Param("channelId") String channelId);

}

