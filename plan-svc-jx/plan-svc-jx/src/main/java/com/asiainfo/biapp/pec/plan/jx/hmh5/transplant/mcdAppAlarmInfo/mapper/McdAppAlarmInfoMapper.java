package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.mapper;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam.McdCampApproveJxNewQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdAlarmThresholdLog;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdAppAlarmInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdAutoClearAlarmPlan;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdDimCity;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmDetailResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmTypeResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 预警信息表 Mapper 接口
 * </p>
 *
 * @author chenlin
 * @since 2023-06-28
 */
public interface McdAppAlarmInfoMapper extends BaseMapper<McdAppAlarmInfo> {

    /**
     * 分页查询自定义预警信息
     *
     * @param keywords 模糊搜索关键词参数
     * @param page     page
     * @return {@link List}<{@link SelectAlarmResultInfo}>
     */
    // Page<SelectAlarmResultInfo> selectAlarm( String keywords); Page<SelectAlarmResultInfo> page,
    List<SelectAlarmResultInfo> selectAlarmByPage(@Param("page") Page<SelectAlarmResultInfo> page, @Param("keywords") String keywords);
    // List<SelectAlarmResultInfo> selectAlarmByPage(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("keywords") String keywords);.

    /**
     * 查询审批中的预警信息(预警待审批列表接口使用)
     *
     * @param query 查询入参
     * @return {@link Page}<{@link SelectAlarmResultInfo}>
     */
    List<SelectAlarmResultInfo> queryApprovingAlarm(@Param("page") Page<SelectAlarmResultInfo> page, @Param("query") McdCampApproveJxNewQuery query);

    /**
     * 查询预警总数
     *
     * @param keywords 模糊查询参数
     * @return int 总数
     */
    int countAlarm(@Param("keywords") String keywords);

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

    Map<String, Object> queryStructure(String tableName);

    List<Map<String, String>> queryDataTableList(List<String> phoneFieldNames);

    /**
     * coc数据源，查询表的变量信息
     *
     * @param sourceTabName 客群id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryCocSourceTableAttr(@Param("sourceTabName") String sourceTabName);

    List<Map<String, Object>> querySourceTableAttr(@Param("sourceTabName") String sourceTabName, @Param("dataSourceName") String dataSourceName, @Param("owner") String owner);

    /**
     * 根据数据源名称和表名称验证表名以及获取表的变量信息
     *
     * @param sourceTabName
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> querySourceTable(@Param("sourceTabName") String sourceTabName);

    /**
     * 查询自增序列值
     *
     * @return int
     */
    int querySeqValue();

    /**
     * 根据用户id查询角色id
     *
     * @param userId 当前登录用户id
     * @return {@link List}<{@link String}>
     */
    List<String> getUserRoleList(@Param("userId") String userId);

    /**
     * 自动解警产品数据清空
     */
    void truncateData();

    /**
     * 批量保存自动解警产品
     *
     * @param mcdAutoClearAlarmPlanSet 批量保存的对象
     */
    void batchInsertAlarmPlanTab(@Param("set") Set<McdAutoClearAlarmPlan> mcdAutoClearAlarmPlanSet);

    /**
     * 查询预警名称以及创建人信息
     *
     * @param alarmId 预警id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> queryAlarmNameAndCreator(@Param("alarmId") String alarmId);

    /**
     * 查询自定义预警-预警阈值信息
     *
     * @param alarmId 预警id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, String> selectAlarmCityThrasResult(@Param("alarmId") Integer alarmId);

    /**
     * 获取地市信息
     *
     * @return
     */
    List<McdDimCity> queryAllCityCode();

    /**
     * 预警下发预警阈值
     *
     * @param item
     */
    void insertAlarmThraCity(@Param("alarmId") String alarmId, @Param("item") Map<String, String> item);

    /**
     * 预警下发预警阈值
     *
     * @param item
     */
    boolean updateAlarmThraCity(@Param("alarmId") String alarmId, @Param("item") Map<String, String> item);

    int columnCheck(@Param("tableName") String tableName, @Param("column") String column);

    boolean insertAlarmThreLog(@Param("thresholdLog") List<McdAlarmThresholdLog> thresholdLog);
}
