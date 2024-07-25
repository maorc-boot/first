package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.dao.IopActivityInfoJxDao;
import com.asiainfo.biapp.pec.plan.jx.camp.req.QueryActivityJxQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ITemplateJxService;
import com.asiainfo.biapp.pec.plan.vo.ActivityVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TemplateServiceImpl.java
 *
 * @author [Li.wang]
 * @CreateDate 2022/12/18 0018
 * @see com.asiainfo.biapp.pec.plan.service.impl
 */
@Slf4j
@Service
public class TemplateJxServiceImpl implements ITemplateJxService {

    @Resource
    private IopActivityInfoJxDao iopActivityInfoJxDao;

    @Override
    public IPage<ActivityVO> queryActivityInfo(QueryActivityJxQuery form) {
        Page pager = new Page<>(form.getCurrent(), form.getSize());
        return iopActivityInfoJxDao.queryActivityInfo(pager,form);
    }

    /**
     * 活动状态修改
     *
     * @param activityTemplateId
     * @param activityId
     * @param status
     */
    @Override
    public void updateActivityStatus(String activityTemplateId, String activityId, String status) throws Exception {
        iopActivityInfoJxDao.updateActivityStatus(activityTemplateId,activityId, status);
    }

}
