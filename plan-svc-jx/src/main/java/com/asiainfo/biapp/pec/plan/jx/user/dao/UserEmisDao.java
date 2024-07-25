package com.asiainfo.biapp.pec.plan.jx.user.dao;


import com.asiainfo.biapp.pec.plan.jx.user.model.McdEmisUserCrmOmModel;
import com.asiainfo.biapp.pec.plan.jx.user.vo.UserOrg;
import com.asiainfo.biapp.pec.plan.jx.user.vo.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * @author mamp
 */
@Mapper
public interface UserEmisDao extends BaseMapper<McdEmisUserCrmOmModel> {
    /**
     * 保存用户数据至USER_CRM_OM表
     *
     * @param infoMap
     * @return
     */
    int addInfo(Map<String, String> infoMap);

    /**
     * 根据userId更新USER_CRM_OM表中的数据
     *
     * @param infoMap
     * @return
     */
    int updateInfo(Map<String, String> infoMap);

    /**
     * 根据useId删除表USER_CRM_OM中的数
     *
     * @param infoMap
     * @return
     */
    int deleteInfo(Map<String, String> infoMap);

    /**
     * 查询USER_CRM_OM表是否存在
     *
     * @param id
     * @return
     */
    List<Map<String, Object>> queryById(String id);

    /**
     * 查询用户在USER_USER表中是否存在
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> queryUserByUserId(String userId);

    /**
     * 根据部门ID查询user_company表中数据
     *
     * @param id
     * @return
     */
    List<Map<String, Object>> queryByDeptId(String id);

    /**
     * 保存用户数据至USER_USER
     *
     * @param userMap
     * @return
     */
    int addUser(Map<String, String> userMap);

    /**
     * 根据userId更新USER_USER表中的数据状态、电话号码、部门ID
     *
     * @param userMap
     * @return
     */
    int updateUser(Map<String, String> userMap);

    /**
     * 根据userID删除USER_USER表中对应数据
     *
     * @param userMap
     * @return
     */
    int deleteUser(Map<String, String> userMap);

    //同步数据到user_user_role_relation表

    /**
     * 添加用户和角色的关系
     * 操作表：user_user_role_relation
     *
     * @param roleMap
     * @return
     */
    int addRole(Map<String, String> roleMap);

    /**
     * 删除用户和角色的关系
     * 操作表：user_user_role_relation
     *
     * @param roleMap
     * @return
     */
    int deleteRole(Map<String, String> roleMap);

    /**
     * 查询所有USER_ROLE关系
     *
     * @return
     */
    List<UserRole> queryRoleInfo();

    /**
     * 查询用户部分关系 USER_COMPANY
     *
     * @return
     */
    List<UserOrg> queryOrgInfo();

    /**
     * 查询用户所有的角色信息  user_user_role_relation
     *
     * @param userId
     * @return
     */
    List<String> queryRolesByUserId(String userId);

}
