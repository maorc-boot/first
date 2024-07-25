package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.approve.jx.dao.SysUserDao;
import com.asiainfo.biapp.pec.approve.jx.model.SysUser;
import com.asiainfo.biapp.pec.approve.jx.service.SysUserService;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wanghao
 * @since 2021-11-15
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    @Setter(onMethod_ = {@Autowired})
    private SysUserDao sysUserDao;

    @Override
    public SysUser getUser() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String userId = UserUtil.getUserId(request);
        SysUser user = sysUserDao.selectOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getUserId, userId)
                .last("LIMIT 1"));
        return user;
    }

    @Override
    public List<SysUser> listByRoleId(String roleId) {
        List<SysUser> users = sysUserDao.queryUserByRoleId(roleId);
        return users;
    }

    @Override
    public List<SysUser> listByDepartmentId(String departmentId) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.select(SysUser.class, i -> !i.getColumn().equals("PWD"));

        List<String> idList = new ArrayList<>();
        String[] ids = departmentId.split(",");
        for (String id : ids) {
            idList.add(id);
        }
        if (!StrUtil.isBlank(departmentId)) {
            wrapper.in("DEPARTMENT_ID", idList);
        }

        List<SysUser> users = sysUserDao.selectList(wrapper);
        return users;
    }

    @Override
    public List<SysUser> listByCountyId(String countyId) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.select(SysUser.class, i -> !i.getColumn().equals("PWD"));

        List<String> idList = new ArrayList<>();
        String[] ids = countyId.split(",");
        for (String id : ids) {
            idList.add(id);
        }
        if (!StrUtil.isBlank(countyId)) {
            wrapper.in("COUNTY_ID", idList);
        }

        List<SysUser> users = sysUserDao.selectList(wrapper);
        return users;
    }

    @Override
    public List<SysUser> listByGridId(String gridId) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.select(SysUser.class, i -> !i.getColumn().equals("PWD"));

        List<String> idList = new ArrayList<>();
        String[] ids = gridId.split(",");
        for (String id : ids) {
            idList.add(id);
        }
        if (!StrUtil.isBlank(gridId)) {
            wrapper.in("GRID_ID", idList);
        }

        List<SysUser> users = sysUserDao.selectList(wrapper);
        return users;
    }
}
