package com.asiainfo.biapp.cmn.jx.service;

import com.asiainfo.biapp.cmn.jx.query.UserPageQueryJx;
import com.asiainfo.biapp.cmn.jx.vo.UserJxVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mamp
 * @date 2022/12/12
 */
public interface UserJxService {

    /**
     * 分页查询所有用户信息
     *
     * @param dto
     * @return
     */
    IPage<UserJxVO> pageList(UserPageQueryJx dto);

    /**
     * 导出用户清单数据
     *
     * @param dto
     * @return
     */
    List<List<String>> exportUser(UserPageQueryJx dto);

    /**
     * 根据用户ID查询
     *
     * @param id
     * @return
     */
    UserJxVO getUserById(String id);
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
