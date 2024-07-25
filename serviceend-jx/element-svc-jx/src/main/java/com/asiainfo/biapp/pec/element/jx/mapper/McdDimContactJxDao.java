package com.asiainfo.biapp.pec.element.jx.mapper;

import com.asiainfo.biapp.pec.element.dto.response.DimContactDetailResponse;
import com.asiainfo.biapp.pec.element.jx.entity.McdDimContactJx;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 触点信息表 Mapper 接口
 * </p>
 *
 * @author wuhq6
 * @since 2021-11-20
 */
@Mapper
public interface McdDimContactJxDao extends BaseMapper<McdDimContactJx> {
    
    /**
     * 保存触点信息
     *
     * @param mcdDimContact
     */
    boolean saveContactInfo(@Param("mcdDimContact") McdDimContactJx mcdDimContact);
    
    /**
     * 更新触点信息
     *
     * @param mcdDimContact
     * @return
     */
    boolean updateContactInfo(@Param("mcdDimContact") McdDimContactJx mcdDimContact);
    
    /**
     * 根据渠道id 查询触点
     *
     * @param channelId
     * @return
     */
    List<DimContactDetailResponse> queryChannelContactByChannelId(String channelId);
}
