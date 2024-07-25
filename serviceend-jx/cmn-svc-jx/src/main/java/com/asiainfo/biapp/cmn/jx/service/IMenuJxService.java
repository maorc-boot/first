package com.asiainfo.biapp.cmn.jx.service;

import com.asiainfo.biapp.cmn.model.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author ranpf
 * @since 2023-1-13
 */
public interface IMenuJxService extends IService<Menu> {


    /**
     * 通过角色ID查询所有菜单and权限的ID以集合返回
     * @param roleId
     * @return
     */
    Map<String,List<String>> listIDByRoleId(Long roleId);
}
