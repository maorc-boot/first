package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.model.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author feify
 * @since 2022-06-06
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 当前用户
     * @return
     */
    SysUser getUser();
    /**
     * 根据角色ID查找用户信息
     * @param roleId
     * @return
     */
    List<SysUser> listByRoleId(String roleId);

    /**
     * 根据部门ID查找用户信息
     * @param departmentId
     * @return
     */
    List<SysUser> listByDepartmentId(String departmentId);

    /**
     * 根据区县ID查找用户信息
     * @param countyId
     * @return
     */
    List<SysUser> listByCountyId(String countyId);

    /**
     * 根据网格ID查找用户信息
     * @param gridId
     * @return
     */
    List<SysUser> listByGridId(String gridId);

}
