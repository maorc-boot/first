package com.asiainfo.biapp.cmn.jx.dao;

import com.asiainfo.biapp.cmn.jx.query.UserPageQueryJx;
import com.asiainfo.biapp.cmn.jx.vo.UserJxVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mamp
 * @date 2022/12/12
 */
@Mapper
public interface UserJxDao {

    /**
     * 查询用户列表
     *
     * @param page
     * @param query
     * @return
     */
    IPage<UserJxVO> pageListUserVo(IPage<?> page, @Param("query") UserPageQueryJx query);

    /**
     * 根据ID查询用户
     *
     * @param id
     * @return
     */
    List<UserJxVO> getUserById(@Param("id") String id);


    /**
     * 查询用户当月调查问卷访问数量
     * @param userId
     * @return
     */
    int countMonthSurveyLog(@Param("userId") String userId);

    /**
     * 记录用户调查问卷访问日志
     * @param userId
     * @return
     */
    int insertSurveyLog(@Param("userId") String userId);
}
