package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.Enum.CampLogType;
import com.asiainfo.biapp.pec.approve.jx.common.CampOperateResult;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampOperateLog;
import com.asiainfo.biapp.pec.approve.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 策略操作记录 服务类
 * </p>
 *
 * @author imcd
 * @since 2021-11-06
 */
public interface IMcdCampOperateLogJxService extends IService<McdCampOperateLog> {

    /**
     * 记录成功日志
     *
     * @param campsegId
     * @param logType
     * @param userComment
     * @param user
     */
    void markSuccLog(String campsegId, CampLogType logType, String userComment, User user);

    /**
     * 记录失败日志
     *
     * @param campsegId
     * @param logType
     * @param userComment
     * @param user
     */
    void markErrLog(String campsegId, CampLogType logType, String userComment, User user);

    /**
     * 保存日志
     *
     * @param campsegId
     * @param logType
     * @param userComment
     * @param user
     * @param result
     * @return
     */
    Boolean saveLog(String campsegId, CampLogType logType, String userComment, User user, CampOperateResult result);

    /**
     * 获取活动操作日志
     *
     * @param campsegId
     * @return
     */
    List<McdCampOperateLog> queryCampOperateLogByCampsegId(String campsegId);
}
