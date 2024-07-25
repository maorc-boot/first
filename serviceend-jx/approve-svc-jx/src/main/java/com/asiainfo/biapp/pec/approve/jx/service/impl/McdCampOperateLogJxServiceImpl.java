package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.lang.Assert;
import com.asiainfo.biapp.pec.approve.common.MetaHandler;
import com.asiainfo.biapp.pec.approve.jx.Enum.CampLogType;
import com.asiainfo.biapp.pec.approve.jx.common.CampOperateResult;
import com.asiainfo.biapp.pec.approve.jx.dao.McdCampOperateLogJxDao;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampOperateLog;
import com.asiainfo.biapp.pec.approve.jx.service.IMcdCampOperateLogJxService;
import com.asiainfo.biapp.pec.approve.jx.utils.IdUtils;
import com.asiainfo.biapp.pec.approve.model.User;
import com.asiainfo.biapp.pec.approve.util.NetUtils;
import com.asiainfo.biapp.pec.approve.util.RequestUtils;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 策略操作记录 服务实现类
 * </p>
 *
 * @author imcd
 * @since 2021-11-06
 */
@Service
@Slf4j
public class McdCampOperateLogJxServiceImpl extends ServiceImpl<McdCampOperateLogJxDao, McdCampOperateLog> implements IMcdCampOperateLogJxService {


    @Override
    public void markSuccLog(String campsegId, CampLogType logType, String userComment, User user) {
        final Boolean result = saveLog(campsegId, logType, userComment, user, CampOperateResult.CAMP_OPERATE_SUCC);
        Assert.isTrue(result, "活动{}记录操作成功日志错误", campsegId);
    }

    @Override
    public void markErrLog(String campsegId, CampLogType logType, String userComment, User user) {
        final Boolean result = saveLog(campsegId, logType, userComment, user, CampOperateResult.CAMP_OPERATE_ERR);
        Assert.isTrue(result, "活动{}记录操作失败日志错误", campsegId);
    }

    /**
     * 创建操作日志
     *
     * @param campsegId
     * @param logType
     * @param userComment
     * @param user
     * @param opResult
     * @return
     */
    @Override
    public Boolean saveLog(String campsegId, CampLogType logType, String userComment, User user, CampOperateResult opResult) {
        McdCampOperateLog log = new McdCampOperateLog();
        final HttpServletRequest request = RequestUtils.getRequest();
        if (null != request) {
            log.setUserIpAddr(NetUtils.getClientIp(request));
            log.setUserBrower(NetUtils.getClientBrowser(request));
        }

        if (null == user && null != request) {
            user = MetaHandler.getUser();
        }
        if(null != user) {
            log.setUserId(user.getUserId());
            log.setUserName(user.getUserName());
        }

        log.setId(IdUtils.generateId());
        log.setLogType(logType.getType());
        log.setLogDesc(logType.getDesc());
        log.setLogTime(new Date());
        log.setCampsegId(campsegId);
        log.setOpResult(opResult.getResult());
        log.setUserComment(userComment);
        return save(log);
    }

    @Override
    public List<McdCampOperateLog> queryCampOperateLogByCampsegId(String campsegId) {
        final LambdaQueryWrapper<McdCampOperateLog> qry = Wrappers.lambdaQuery();
        qry.eq(McdCampOperateLog::getCampsegId, campsegId).orderByAsc(McdCampOperateLog::getLogTime,McdCampOperateLog::getLogType);
        return list(qry);
    }
}
