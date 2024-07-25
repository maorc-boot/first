package com.asiainfo.biapp.pec.element.jx.mapper;

import com.asiainfo.biapp.pec.element.jx.entity.McdDimChannelJx;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 渠道表 Mapper 接口
 * </p>
 *
 * @author ranpf
 * @since 2022-12-13
 */
@Mapper
public interface McdDimChannelJxDao extends BaseMapper<McdDimChannelJx> {

    /**
     * 保存渠道信息
     *
     * @param mcdDimChannel
     * @return
     */
    int saveChannelInfo(@Param("mcdDimChannel") McdDimChannelJx mcdDimChannel);

    /**
     * 更新渠道信息
     *
     * @param mcdDimChannel
     * @return
     */
    int updateChannel(@Param("mcdDimChannel") McdDimChannelJx mcdDimChannel);

    /**
     * 根据用户ID查询渠道权限
     *
     * @param userId
     * @return
     */
    List<String> listChannelByUserId(@Param("userId") String userId);
}
