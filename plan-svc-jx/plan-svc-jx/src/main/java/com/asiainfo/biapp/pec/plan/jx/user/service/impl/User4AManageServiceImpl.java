package com.asiainfo.biapp.pec.plan.jx.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.asiainfo.biapp.pec.plan.dao.McdSysUserDao;
import com.asiainfo.biapp.pec.plan.jx.user.dao.McdSysUser4aMapper;
import com.asiainfo.biapp.pec.plan.jx.user.model.McdSysUser4a;
import com.asiainfo.biapp.pec.plan.jx.user.service.User4AManageService;
import com.asiainfo.biapp.pec.plan.jx.user.vo.UserInfo4A;
import com.asiainfo.biapp.pec.plan.model.McdSysUser;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2023/5/15
 */
@Service
public class User4AManageServiceImpl implements User4AManageService {
    @Resource
    private McdSysUser4aMapper user4aMapper;

    @Resource
    private McdSysUserDao sysUserDao;



    @Override
    public int deleteUserInfoByUserId(String userId) {

        LambdaUpdateWrapper<McdSysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(McdSysUser::getUserId, userId);
        return sysUserDao.delete(wrapper);
    }

    @Override
    public int updateStatus4A(String userId, String status) {
        LambdaUpdateWrapper<McdSysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(McdSysUser::getUserId, userId);
        McdSysUser user = new McdSysUser();
        user.setStatus(Integer.valueOf(status));
        return sysUserDao.update(user, wrapper);
    }

    @Override
    public int updateUserPwd4A(String userId, String password) {
        LambdaUpdateWrapper<McdSysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(McdSysUser::getUserId, userId);
        McdSysUser user = new McdSysUser();
        user.setPwd(password);

        LambdaUpdateWrapper<McdSysUser4a> wrapper4a = new LambdaUpdateWrapper<>();
        wrapper4a.eq(McdSysUser4a::getUserId, userId);
        McdSysUser4a user4a = new McdSysUser4a();
        user4a.setPassword(password);
        user4aMapper.update(user4a, wrapper4a);


        return sysUserDao.update(user, wrapper);
    }

    @Override
    public int deleteUserById4A(String userId) {
        LambdaUpdateWrapper<McdSysUser4a> wrapper4a = new LambdaUpdateWrapper<>();
        wrapper4a.eq(McdSysUser4a::getUserId, userId);
        return user4aMapper.delete(wrapper4a);
    }

    @Override
    public List<Map<String, Object>> getUserByUserId(String userId) {
        LambdaQueryWrapper<McdSysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdSysUser::getUserId, userId);
        return sysUserDao.selectMaps(queryWrapper);
    }

    @Override
    public Map<String, String> saveOrUpdateUserInfo4A(UserInfo4A user4A, String modifyType) {
        Map<String, String> result = new HashMap<>();
        LambdaQueryWrapper<McdSysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdSysUser::getUserId, user4A.getUserId());
        List<McdSysUser> list = sysUserDao.selectList(queryWrapper);
        if ("add".equals(modifyType)) {
            if (CollectionUtil.isNotEmpty(list)) {
                result.put("SUCCESS", "0");
                result.put("msg", "所填写的用户名已存在，请重新输入！");
                return result;
            }

            McdSysUser mcdSysUser = new McdSysUser();
            mcdSysUser.setId(IdUtils.generateId());
            mcdSysUser.setUserId(user4A.getUserId());
            mcdSysUser.setUserName(user4A.getUserName());
            mcdSysUser.setPwd(user4A.getPassword());
            mcdSysUser.setMobilePhone(user4A.getMobile());
            mcdSysUser.setEmail(user4A.getEmail());
            mcdSysUser.setCreateTime(new Date());
            mcdSysUser.setCityId("");
            mcdSysUser.setDepartmentId(user4A.getOrgId());
            sysUserDao.insert(mcdSysUser);

            McdSysUser4a mcdSysUser4a = new McdSysUser4a();
            mcdSysUser4a.setUserId(user4A.getUserId());
            mcdSysUser4a.setUsername(user4A.getUserName());
            mcdSysUser4a.setMobile(user4A.getMobile());
            mcdSysUser4a.setEmail(user4A.getEmail());
            mcdSysUser4a.setLoginNo(user4A.getLoginNo());
            mcdSysUser4a.setPassword(user4A.getPassword());
            mcdSysUser4a.setOrgId(user4A.getOrgId());
            mcdSysUser4a.setRemark(user4A.getRemark());
            user4aMapper.insert(mcdSysUser4a);
            result.put("SUCCESS", "1");
            result.put("msg", "用户信息已插入记录！");
        } else {
            if (CollectionUtil.isEmpty(list)) {
                result.put("SUCCESS", "0");
                result.put("msg", "所填写的用户ID不存在，请重新输入！");
                return result;
            }
            McdSysUser mcdSysUser = new McdSysUser();
            mcdSysUser.setUserId(user4A.getUserId());
            mcdSysUser.setUserName(user4A.getUserName());
            mcdSysUser.setPwd(user4A.getPassword());
            mcdSysUser.setMobilePhone(user4A.getMobile());
            mcdSysUser.setEmail(user4A.getEmail());
            mcdSysUser.setCreateTime(new Date());

            LambdaUpdateWrapper<McdSysUser> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(McdSysUser::getUserId, user4A.getUserId());
            sysUserDao.update(mcdSysUser, updateWrapper);

            LambdaQueryWrapper<McdSysUser4a> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(McdSysUser4a::getUserId, user4A.getUserId());
            List<McdSysUser4a> mcdSysUser4as = user4aMapper.selectList(lambdaQueryWrapper);

            if (CollectionUtil.isNotEmpty(mcdSysUser4as)) {
                McdSysUser4a mcdSysUser4a = new McdSysUser4a();
                mcdSysUser4a.setUserId(user4A.getUserId());
                mcdSysUser4a.setUsername(user4A.getUserName());
                mcdSysUser4a.setMobile(user4A.getMobile());
                mcdSysUser4a.setEmail(user4A.getEmail());
                mcdSysUser4a.setLoginNo(user4A.getLoginNo());
                mcdSysUser4a.setPassword(user4A.getPassword());
                mcdSysUser4a.setOrgId(user4A.getOrgId());
                mcdSysUser4a.setRemark(user4A.getRemark());

                LambdaUpdateWrapper<McdSysUser4a> updateWrapper4a = new LambdaUpdateWrapper();
                updateWrapper4a.eq(McdSysUser4a::getUserId, user4A.getUserId());

                user4aMapper.update(mcdSysUser4a, updateWrapper4a);
            } else {
                McdSysUser4a mcdSysUser4a = new McdSysUser4a();
                mcdSysUser4a.setUserId(user4A.getUserId());
                mcdSysUser4a.setUsername(user4A.getUserName());
                mcdSysUser4a.setMobile(user4A.getMobile());
                mcdSysUser4a.setEmail(user4A.getEmail());
                mcdSysUser4a.setLoginNo(user4A.getLoginNo());
                mcdSysUser4a.setPassword(user4A.getPassword());
                mcdSysUser4a.setOrgId(user4A.getOrgId());
                mcdSysUser4a.setRemark(user4A.getRemark());
                user4aMapper.insert(mcdSysUser4a);
            }
            result.put("SUCCESS", "1");
            result.put("msg", "用户信息更新成功！");
        }
        return result;
    }
}
