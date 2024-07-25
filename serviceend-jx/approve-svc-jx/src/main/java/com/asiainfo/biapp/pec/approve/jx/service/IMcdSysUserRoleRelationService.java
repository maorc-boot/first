package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.model.McdSysUserRoleRelationModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色映射表 服务类
 * </p>
 *
 * @author ranpf
 * @since 2023-5-24
 */
public interface IMcdSysUserRoleRelationService extends IService<McdSysUserRoleRelationModel> {


    /**
     * 根据用户ID查询所属角色
     * @param userid
     * @return
     */
    List<Long> findRoleIDByUserID(String userid);



}
