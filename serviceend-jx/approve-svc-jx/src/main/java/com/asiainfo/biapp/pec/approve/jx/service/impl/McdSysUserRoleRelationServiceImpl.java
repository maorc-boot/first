package com.asiainfo.biapp.pec.approve.jx.service.impl;

import com.asiainfo.biapp.pec.approve.jx.dao.McdSysUserRoleRelationDao;
import com.asiainfo.biapp.pec.approve.jx.model.McdSysUserRoleRelationModel;
import com.asiainfo.biapp.pec.approve.jx.service.IMcdSysUserRoleRelationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户角色映射表 服务实现类
 * </p>
 *
 * @author liuHT
 * @since 2021-11-04
 */
@Slf4j
@Service
public class McdSysUserRoleRelationServiceImpl extends ServiceImpl<McdSysUserRoleRelationDao, McdSysUserRoleRelationModel> implements IMcdSysUserRoleRelationService {


    @Override
    public List<Long> findRoleIDByUserID(String userid) {
        List<Long> roleIdList = new ArrayList<>();
        LambdaQueryWrapper<McdSysUserRoleRelationModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdSysUserRoleRelationModel::getUserId, userid);
        List<McdSysUserRoleRelationModel> userRoleRelations = baseMapper.selectList(queryWrapper);
        //判断是否有效
        if(userRoleRelations==null || userRoleRelations.size() == 0){
            return null;
        }
        for (McdSysUserRoleRelationModel userRoleRelation : userRoleRelations) {
            roleIdList.add(userRoleRelation.getRoleId());
        }
        return roleIdList;
    }

}
