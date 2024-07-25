package com.asiainfo.biapp.pec.approve.jx.dao;

import com.asiainfo.biapp.pec.approve.jx.model.SysUser;
import com.asiainfo.biapp.pec.approve.jx.vo.McdChannelRoleListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author feify
 * @since 2022-06-06
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUser> {

    /**
     * 根据角色id查询用户
     * @param roleId
     * @return
     */
    List<SysUser> queryUserByRoleId(String roleId);

    List<McdChannelRoleListVo> queryChannelRoleByCampsegId(@Param("campsegId") String campsegId);

    /**
     * 根据活动根id或预警id查询活动名称或预警名称以及创建人信息
     *
     * @param campsegRootId 活动根id或预警id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> getUserAndCampInfoCampsegId(@Param("campsegRootId") String campsegRootId);

}
