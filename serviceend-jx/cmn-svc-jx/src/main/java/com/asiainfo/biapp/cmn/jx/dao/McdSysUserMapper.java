package com.asiainfo.biapp.cmn.jx.dao;

import com.asiainfo.biapp.cmn.jx.model.McdSysUser;
import com.asiainfo.biapp.cmn.model.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2022-12-12
 */
public interface McdSysUserMapper extends BaseMapper<McdSysUser> {

    /**
     * 查询排除掉二级的菜单id
     *
     * @param menuId 角色下的所有菜单id
     * @return
     */
    Menu queryMenuIdsExcluedeThreeMenu(@Param("menuId") String menuId);

}
