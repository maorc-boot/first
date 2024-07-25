package com.asiainfo.biapp.cmn.jx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.asiainfo.biapp.cmn.dao.MenuDao;
import com.asiainfo.biapp.cmn.jx.dao.McdSysUserMapper;
import com.asiainfo.biapp.cmn.jx.service.IMenuJxService;
import com.asiainfo.biapp.cmn.model.Menu;
import com.asiainfo.biapp.cmn.model.Permission;
import com.asiainfo.biapp.cmn.service.IMenuService;
import com.asiainfo.biapp.cmn.service.IPermissionService;
import com.asiainfo.biapp.cmn.service.IRoleMenuRelationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <p>
 * 江西菜单表 服务实现类
 * </p>
 *
 * @author ranpf
 * @since 2023-1-13
 */
@Slf4j
@Service
@Transactional
public class MenuJxServiceImpl extends ServiceImpl<MenuDao, Menu> implements IMenuJxService {


    @Autowired
    private IRoleMenuRelationService iRoleMenuRelationService;

    @Autowired
    private IPermissionService iPermissionService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private McdSysUserMapper sysUserMapper;

    @Autowired
    private MenuDao menuDao;

    @Override
    public Map<String,List<String>> listIDByRoleId(Long roleId) {

        Map<String,List<String>> permisAndMenuMap = new HashMap<>();
        List<String> fisrtMenu = new ArrayList<>();
        // 三级菜单对应的二级菜单集合
        List<String> secMenuByThds = new ArrayList<>();

        List<Long> menuIDByRoleID = iRoleMenuRelationService.findMenuIDByRoleID(roleId);

        if(CollectionUtil.isNotEmpty(menuIDByRoleID)){
            LambdaQueryWrapper<Menu>  menuWrapper = new LambdaQueryWrapper<>();
            menuWrapper.eq(Menu::getParentId,-1) ;
            //查询一级菜单
            List<Menu> menuList = this.list(menuWrapper);
            for (Menu  men:menuList){
                fisrtMenu.add(men.getMenuId().toString());
            }

            //菜单不返回一级菜单,返回二级菜单及菜单下的接口
            for (Long aLong : menuIDByRoleID) {
                if (!fisrtMenu.contains(aLong.toString())){
                    getMenuOrPermission( aLong, roleId, permisAndMenuMap);
                }else {
                    LambdaQueryWrapper<Menu> menuLamWrapper = new LambdaQueryWrapper<>();
                    menuLamWrapper.eq(Menu::getMenuId,aLong)
                               .eq(Menu::getParentId,-1)
                               .like(Menu::getMenuitemTitle,"首页");
                    List<Menu> menus = menuService.list(menuLamWrapper);
                    if (!CollectionUtils.isEmpty(menus)){
                        getMenuOrPermission( aLong, roleId, permisAndMenuMap);
                    }

                }
            }
            // 排除三级菜单对应的二级菜单
            try {
                for (Map.Entry<String, List<String>> stringListEntry : permisAndMenuMap.entrySet()) {
                    // a. 判断此菜单是否三级菜单 true-是
                    if (queryMenuIdsExcluedeThreeMenu(stringListEntry.getKey())) {
                        // b. 根据此菜单id查询出他的父id
                        Menu menu = menuDao.selectOne(Wrappers.<Menu>query().lambda().eq(Menu::getMenuId, stringListEntry.getKey()));
                        if (ObjectUtil.isNotEmpty(menu)) {
                            // 三级菜单对应的二级菜单id收集
                            secMenuByThds.add(menu.getParentId().toString());
                        }
                    }
                }
                // c. 将三级菜单对应的二级菜单id从permisAndMenuMap中删除
                for (String secMenuByThd : secMenuByThds) {
                    if (permisAndMenuMap.containsKey(secMenuByThd)) {
                        // 移除父id对应的这条数据
                        permisAndMenuMap.remove(secMenuByThd);
                    }
                }
            } catch (Exception e) {
                log.error("listIDByRoleId-->排除三级菜单对应的二级菜单异常：", e);
            }
        }

        return permisAndMenuMap;
    }

    /**
     * 查询排除掉二级的菜单id
     *
     * @param menuId 角色下的菜单id
     * @return {@link List}<{@link Long}>
     */
    public boolean queryMenuIdsExcluedeThreeMenu(String menuId) {
        Menu menu = sysUserMapper.queryMenuIdsExcluedeThreeMenu(menuId);
        if (ObjectUtil.isNotEmpty(menu)) {
            // =-1表示菜单对应的是二级菜单
            return -1 != menu.getParentId() || 0 != menu.getParentId();
        }
        return false;
    }


    private void getMenuOrPermission(Long aLong,Long roleId,Map<String,List<String>> permisAndMenuMap){

        List<String> permis = new ArrayList<>();

        List<Permission> permissionList = iPermissionService.listIDByRoleID(aLong, roleId.toString(), "");
        if (Objects.isNull(permissionList)){
            permisAndMenuMap.put(aLong.toString(),permis);
            return;
        }

        for (Permission per: permissionList){
            permis.add(per.getId().toString());
        }
        permisAndMenuMap.put(aLong.toString(),permis);
    }
}
