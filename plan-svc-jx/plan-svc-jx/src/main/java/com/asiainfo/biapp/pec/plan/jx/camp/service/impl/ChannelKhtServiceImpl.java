package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampListDao;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ChannelKhtService;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.TaskTypeDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取客户通和网格渠道创建活动时的任务类型
 *
 * @author admin
 * @version 1.0
 * @date 2022/6/15 15:18
 */
@Service
@Slf4j
public class ChannelKhtServiceImpl implements ChannelKhtService {

    @Autowired
    private McdCampListDao mcdCampListDao;

    @Override
    public List<TaskTypeDetail> selectChannelTaskTypeDetail(String pcode) {
        return mcdCampListDao.selectChannelTaskTypeDetail(pcode);
    }
}
