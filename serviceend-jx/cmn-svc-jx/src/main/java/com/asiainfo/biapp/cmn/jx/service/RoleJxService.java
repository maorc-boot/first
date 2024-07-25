package com.asiainfo.biapp.cmn.jx.service;

import com.asiainfo.biapp.cmn.jx.query.RolePageQueryJx;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author mamp
 * @date 2022/12/13
 */
public interface RoleJxService {
    /**
     * 分页查询所有角色信息
     *
     * @param dto
     * @return
     */
    IPage pageList(RolePageQueryJx dto);
}
