package com.asiainfo.biapp.cmn.jx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.asiainfo.biapp.cmn.jx.dao.UserJxDao;
import com.asiainfo.biapp.cmn.jx.query.UserPageQueryJx;
import com.asiainfo.biapp.cmn.jx.service.UserJxService;
import com.asiainfo.biapp.cmn.jx.vo.UserJxVO;
import com.asiainfo.biapp.cmn.model.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mamp
 * @date 2022/12/12
 */
@Service
public class UserJxServiceImpl implements UserJxService {

    @Resource
    private UserJxDao userJxDao;

    /**
     * 分页查询所有用户信息
     *
     * @param dto
     * @return
     */
    @Override
    public IPage<UserJxVO> pageList(UserPageQueryJx dto) {
        IPage<User> page = new Page(dto.getCurrent(), dto.getSize());
        IPage<UserJxVO> userVOIPage = userJxDao.pageListUserVo(page, dto);
        return userVOIPage;
    }

    @Override
    public List<List<String>> exportUser(UserPageQueryJx dto) {
        IPage<User> page = new Page(dto.getCurrent(), dto.getSize());
        IPage<UserJxVO> userVOIPage = userJxDao.pageListUserVo(page, dto);
        List<String> list1;
        List<List<String>> list2 = new ArrayList<>();
        for (UserJxVO record : userVOIPage.getRecords()) {
            list1 = new ArrayList<>();
            list1.add(record.getCityName());
            list1.add(record.getUserId());
            list1.add(record.getUserName());
            list1.add(record.getTitle());
            list1.add(record.getRoleNames());
            list1.add(DateUtil.formatDate(record.getCreateTime()));
            list2.add(list1);
        }
        return list2;
    }

    @Override
    public UserJxVO getUserById(String id) {
        List<UserJxVO> userJxVOS = userJxDao.getUserById(id);
        if (CollectionUtil.isNotEmpty(userJxVOS)) {
            return userJxVOS.get(0);
        }
        return null;
    }

    @Override
    public int countMonthSurveyLog(String userId) {
        return userJxDao.countMonthSurveyLog(userId);
    }

    @Override
    public int insertSurveyLog(String userId) {
        return userJxDao.insertSurveyLog(userId);
    }
}
