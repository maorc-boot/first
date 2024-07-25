package com.asiainfo.biapp.pec.preview.jx.mapper;

import com.asiainfo.biapp.pec.preview.jx.entity.McdChnPreUserNum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户群渠道偏好用户数统计表 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2022-09-30
 */
public interface McdChnPreUserNumMapper extends BaseMapper<McdChnPreUserNum> {

    List<McdChnPreUserNum> queryPreData(@Param("custgroupId") String custgroupId, @Param("preLevel") Integer preLevel, @Param("dataDate") String dataDate);
}
