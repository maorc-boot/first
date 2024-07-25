package com.asiainfo.biapp.pec.plan.jx.user.service;


import com.asiainfo.biapp.pec.plan.jx.user.vo.UserInfo4A;

import java.util.List;
import java.util.Map;

public interface User4AManageService {

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    int deleteUserInfoByUserId(String userId);

    /**
     * 更新用户状态
     *
     * @param userId
     * @param status
     * @return
     */
    int updateStatus4A(String userId, String status);

    /**
     * 更新用户密码
     */
    int updateUserPwd4A(String userId, String password);

    /**
     * 删除用户
     */
    int deleteUserById4A(String userId);

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> getUserByUserId(String userId);

    /**
     * 保存或者更新用户
     *
     * @param user4A
     * @param modifyType
     * @return
     */
    Map<String, String> saveOrUpdateUserInfo4A(UserInfo4A user4A, String modifyType);

}
