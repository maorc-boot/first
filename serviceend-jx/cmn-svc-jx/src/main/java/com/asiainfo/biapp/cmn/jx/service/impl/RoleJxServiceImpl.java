package com.asiainfo.biapp.cmn.jx.service.impl;

import com.asiainfo.biapp.cmn.jx.dao.RoleJxDao;
import com.asiainfo.biapp.cmn.jx.query.RolePageQueryJx;
import com.asiainfo.biapp.cmn.jx.service.RoleJxService;
import com.asiainfo.biapp.cmn.vo.RoleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mamp
 * @date 2022/12/13
 */
@Service
@Slf4j
public class RoleJxServiceImpl implements RoleJxService {

    @Resource
    private RoleJxDao roleJxDao;

    /**
     * 分页查询所有角色信息
     *
     * @param dto
     * @return
     */
    @Override
    public IPage pageList(RolePageQueryJx dto) {
        IPage<RoleVO> page = new Page(dto.getCurrent(),dto.getSize());
        String keyWords = dto.getKeyWords();
        IPage<RoleVO> roleVOIPage = roleJxDao.pageListRoleVo(page, keyWords,dto.getDeptId());
        return roleVOIPage;

    }
}
