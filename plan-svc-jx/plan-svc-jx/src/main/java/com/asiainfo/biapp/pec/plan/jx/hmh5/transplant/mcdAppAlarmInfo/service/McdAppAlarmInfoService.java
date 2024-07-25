package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdAppAlarmInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdDimCity;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmDetailResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmTypeResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

/**
 * <p>
 * 预警信息表 服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-28
 */
public interface McdAppAlarmInfoService extends IService<McdAppAlarmInfo> {

    /**
     * 自定义预警页面列表查询
     *
     * @param alarmParam 查询入参
     * @return {@link Page}<{@link SelectAlarmResultInfo}>
     */
    Page<SelectAlarmResultInfo> selectAlarm(SelectAlarmParam alarmParam);

    /**
     * 查询审批中的预警信息(预警待审批列表接口使用)
     *
     * @param query 查询入参
     * @return {@link Page}<{@link SelectAlarmResultInfo}>
     */
    Page<SelectAlarmResultInfo> queryApprovingAlarm(McdCampApproveJxNewQuery query);


    /**
     * 根据alarmId查询预警详情
     *
     * @param alarmId 预警id
     * @return {@link SelectAlarmDetailResultInfo}
     */
    SelectAlarmDetailResultInfo selectAlarmDetailById(Integer alarmId);

    /**
     * 查询客户通自定义预警类别
     *
     * @return {@link List}<{@link SelectAlarmTypeResult}>
     */
    List<SelectAlarmTypeResult> selectAlarmType();

    /**
     * 新增客户通自定义预警
     *
     * @param user 用户信息
     * @param alarmParam 保存入参对象
     * @return {@link Integer}
     */
    Integer addAlarm(UserSimpleInfo user, AddAlarmParam alarmParam);

    ActionResponse<SubmitProcessJxDTO> getNodeApprover();

    void commitProcess(AlarmCommitProcessParam processParam, UserSimpleInfo user);

    /**
     * 修改客户通自定义预警
     *
     * @param user 用户信息
     * @param alarmParam 保存入参对象
     */
    void modifyAlarm(UserSimpleInfo user, ModifyAlarmParam alarmParam);

    /**
     * 根据alarmId值删除自定义预警
     *
     * @param user 用户信息
     * @param alarmId 预警id
     */
    void deleteAlarm(UserSimpleInfo user, Integer alarmId);

    /**
     * 根据alarmId值终止自定义预警
     *
     * @param user 用户信息
     * @param alarmId 预警id
     */
    void terminateAlarm(UserSimpleInfo user, Integer alarmId);

    void modifyAlarmStatus(ModifyAlarmStatusParam alarmStatusParam);

    String selectAlarmApproveFlowId(Integer alarmId);

    Map<String, Object> queryStructure(String dataSourceName,String tableName);

    Map<String, String> queryDataSourceList();

    Map<String, String> queryDataTableList(String dataSourceName);

    /**
     * 根据数据源名称和表名称验证表名以及获取表的变量信息
     *
     * @param dataSourceName 数据源名称
     * @param tableName 表名
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    ActionResponse<List<Map<String, Object>>> querySourceTable(String dataSourceName, String tableName);

    /**
     * 根据用户id查询角色id
     *
     * @param userId 当前登录用户id
     * @return {@link List}<{@link String}>
     */
    List<String> getUserRoleList(String userId);

    /**
     * 解警产品批量导入(全量数据覆盖)
     *
     * @param file 需要导入的文件数据
     * @param user 当前登录用户信息
     */
    void batchImportAutoClearAlarm(MultipartFile file, UserSimpleInfo user) throws Exception;

    /**
     * 查询预警名称以及创建人信息
     *
     * @param alarmId 预警id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> queryAlarmNameAndCreator(String alarmId);

    /**
     * 更新预警阈值信息
     *
     * @param param 预警阈值地市信息
     */
    boolean updateThreshold(McdCityThreshParam param);

    /**
     * 获取地市信息
     *
     * @return
     */
    List<McdDimCity> queryAllCitys();
}
