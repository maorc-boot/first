package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.client.pec.approve.model.CmpApprovalProcess;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.api.PecApproveFeignClient;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.DynamicDataSourceProperties;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.mapper.McdAppAlarmInfoMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdAlarmThresholdLog;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdAppAlarmInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdAutoClearAlarmPlan;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model.McdDimCity;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.McdAppAlarmInfoService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.AlarmCityThrasholdResult;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmDetailResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo.SelectAlarmTypeResult;
import com.asiainfo.biapp.pec.plan.jx.utils.ExcelUtils;
import com.asiainfo.biapp.pec.plan.service.IMcdCampOperateLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 预警信息表 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-28
 */
@Service
@Slf4j
public class McdAppAlarmInfoServiceImpl extends ServiceImpl<McdAppAlarmInfoMapper, McdAppAlarmInfo> implements McdAppAlarmInfoService {

    // 2023-07-18 14:59:25 因业务层互相调用出现缓存失效的情况，暂时用装配自身的方法来解决
    @Autowired
    private McdAppAlarmInfoMapper mcdAppAlarmInfoMapper;

    @Resource
    private IMcdCampOperateLogService logService;

    @Autowired
    private DynamicDataSourceProperties dataSourceProperties;

    @Value("${khtusemultidatasource.phonefieldnames}")
    private List<String> phoneFieldNames;

    @Autowired
    @Qualifier("com.asiainfo.biapp.pec.plan.jx.api.PecApproveFeignClient")
    private PecApproveFeignClient approveFeignClient;

    private final Map<String, String> checkColumnMap = new HashMap() {{
        put("stat_date", "账日期");
        put("phone_no", "手机号");
        put("is_clear_warning", "是否解警");
//        put("rea_clear_warning", "解警原因");
//        put("clear_warning_date", "解警时间");
        put("warning_date", "预警时间");
    }};

    /**
     * 自定义预警页面列表查询
     *
     * @param alarmParam 查询入参
     * @return {@link Page}<{@link SelectAlarmResultInfo}>
     */
    @Override
    public Page<SelectAlarmResultInfo> selectAlarm(SelectAlarmParam alarmParam) {
        // return baseMapper.selectAlarm(
        //         new Page<>(alarmParam.getPageNum(), alarmParam.getPageSize()),
        //         alarmParam.getKeywords()
        // );
        Page<SelectAlarmResultInfo> page = new Page<>(alarmParam.getPageNum(), alarmParam.getPageSize());
        // 分页查询数据
        List<SelectAlarmResultInfo> alarmResultInfoList = baseMapper.selectAlarmByPage(page, alarmParam.getKeywords());
        page.setRecords(alarmResultInfoList);
        // List<SelectAlarmResultInfo> alarmResultInfoList = baseMapper.selectAlarmByPage(alarmParam.getPageNum(), alarmParam.getPageSize(), alarmParam.getKeywords());
        // 获取总数
        // int totalSize = baseMapper.countAlarm(alarmParam.getKeywords());
        // int totalPage = totalSize % alarmParam.getPageSize() == 0 ? totalSize / alarmParam.getPageSize() : totalSize / alarmParam.getPageSize() + 1;
        // page.setTotal(totalSize);
        // page.setPages(totalPage);
        return page;
    }

    /**
     * 查询审批中的预警信息(预警待审批列表接口使用)
     *
     * @param query 查询入参
     * @return {@link Page}<{@link SelectAlarmResultInfo}>
     */
    @Override
    public Page<SelectAlarmResultInfo> queryApprovingAlarm(McdCampApproveJxNewQuery query) {
        Page<SelectAlarmResultInfo> page = new Page<>(query.getCurrent(), query.getSize());
        // 分页查询数据
        List<SelectAlarmResultInfo> alarmResultInfoList = baseMapper.queryApprovingAlarm(page, query);
        page.setRecords(alarmResultInfoList);
        return page;
    }

    /**
     * 根据alarmId查询预警详情
     *
     * @param alarmId 预警id
     * @return {@link SelectAlarmDetailResultInfo}
     */
    @Override
    public SelectAlarmDetailResultInfo selectAlarmDetailById(Integer alarmId) {
        SelectAlarmDetailResultInfo selectAlarmDetailResultInfo = baseMapper.selectAlarmDetailById(alarmId);
        if ("1".equals(selectAlarmDetailResultInfo.getSourceDb())) {
            selectAlarmDetailResultInfo.setSourceDb("yitijimarket");
        } else if ("2".equals(selectAlarmDetailResultInfo.getSourceDb())) {
            selectAlarmDetailResultInfo.setSourceDb("newmarket");
        } else if ("3".equals(selectAlarmDetailResultInfo.getSourceDb())) {
            selectAlarmDetailResultInfo.setSourceDb("coc");
        }
        Map<String, String> cityThrash = mcdAppAlarmInfoMapper.selectAlarmCityThrasResult(alarmId);
        List<McdDimCity> citys = mcdAppAlarmInfoMapper.queryAllCityCode();
        List<AlarmCityThrasholdResult> cityThrasholdResults = citys.stream().map(city -> new AlarmCityThrasholdResult(city.getCityId(), city.getCityName(), MapUtil.getStr(cityThrash, "CITY_" + city.getCityId()))).collect(Collectors.toList());
        selectAlarmDetailResultInfo.setAlarmThreshold(cityThrasholdResults);
        return selectAlarmDetailResultInfo;
    }

    /**
     * 查询客户通自定义预警类别
     *
     * @return {@link List}<{@link SelectAlarmTypeResult}>
     */
    @Override
    public List<SelectAlarmTypeResult> selectAlarmType() {
        return baseMapper.selectAlarmType();
    }

    /**
     * 新增客户通自定义预警
     *
     * @param user       用户信息
     * @param alarmParam 保存入参对象
     * @return {@link Integer}
     */
    @Override
    public Integer addAlarm(UserSimpleInfo user, AddAlarmParam alarmParam) {
        //检查是否存在相同名称的预警
        McdAppAlarmInfo mcdAppAlarmInfo = baseMapper.selectOne(new LambdaQueryWrapper<McdAppAlarmInfo>()
                .eq(McdAppAlarmInfo::getAlarmName, alarmParam.getAlarmName())
        );
        if (mcdAppAlarmInfo != null) throw new BaseException("预警名称：{" + alarmParam.getAlarmName() + "}已存在！");

        //检查预警类型是否有效
        // List<SelectAlarmTypeResult> alarmTypeResults = baseMapper.selectAlarmType(alarmParam.getDicKey());
        // if (alarmTypeResults.size() == 0) throw new BaseException("无效的字典KEY值！");

        //先查询表是否存在
        // Map<String, String> tableList = mcdAppAlarmInfoService.queryDataTableList(alarmParam.getSourceDb());
        // if (tableList.size() == 0 || !tableList.keySet().contains(alarmParam.getSourceTable()))
        //     throw new BaseException("无效的表名称：{" + alarmParam.getSourceTable() + "}！");

        //保存新的自定义预警
        mcdAppAlarmInfo = BeanUtil.copyProperties(alarmParam, McdAppAlarmInfo.class);
        // 查询自增序列值
        int seqValue = mcdAppAlarmInfoMapper.querySeqValue();

        // mcdAppAlarmInfo.setAlarmType(alarmTypeResults.get(0).getDicValue());
        mcdAppAlarmInfo.setAlarmId(seqValue);
        mcdAppAlarmInfo.setCreateUser(user.getUserId());
        mcdAppAlarmInfo.setUserCityId(user.getCityId());
        mcdAppAlarmInfo.setExecuteCondition(alarmParam.getExecuteCondition());
        mcdAppAlarmInfo.setExecuteConditionVal(alarmParam.getExecuteConditionVal());
        mcdAppAlarmInfo.setAlarmExecDealtime(alarmParam.getAlarmExecDealtime());
        mcdAppAlarmInfo.setAlarmExecDealtimeUnit(alarmParam.getAlarmExecDealtimeUnit());
        mcdAppAlarmInfo.setIsDefineTime(alarmParam.isDefineTime() ? "true" : "false");
        // mcdAppAlarmInfo.setCreateTime(new Date());
        mcdAppAlarmInfo.setCreateTime(DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
        if ("yitijimarket".equals(alarmParam.getSourceDb())) {
            mcdAppAlarmInfo.setSourceDb("1");
        } else if ("newmarket".equals(alarmParam.getSourceDb())) {
            mcdAppAlarmInfo.setSourceDb("2");
        } else if ("coc".equals(alarmParam.getSourceDb())) {
            mcdAppAlarmInfo.setSourceDb("3");
        }
        mcdAppAlarmInfo.setStartTime(DateUtil.format(alarmParam.getStartTime(), "yyyy-MM-dd"));
        mcdAppAlarmInfo.setEndTime(DateUtil.format(alarmParam.getEndTime(), "yyyy-MM-dd"));
        mcdAppAlarmInfo.setAutoClearAlarmStatus(StrUtil.isEmpty(alarmParam.getAutoClearAlarmStatus()), "0", alarmParam.getAutoClearAlarmStatus());
        baseMapper.insert(mcdAppAlarmInfo);
        Map<String, String> mapList = alarmParam.getAlarmThreshold().stream().map(v -> {
            String code = v.getAlarmCityCode();
            v.setAlarmCityCode("city" + code);
            v.setAlarmThrashValue(v.getAlarmThrashValue());
            return v;
        }).collect(Collectors.toMap(AlarmCityThrasholdResult::getAlarmCityCode, AlarmCityThrasholdResult::getAlarmThrashValue));
        mcdAppAlarmInfoMapper.insertAlarmThraCity(String.valueOf(seqValue), mapList);
        return seqValue;
    }

    @Override
    public ActionResponse<SubmitProcessJxDTO> getNodeApprover() {
        //获取自定义预警的审批模板
        ActionResponse<CmpApprovalProcess> customAlarm = approveFeignClient.getApproveConfig(PecApproveFeignClient.CUSTOM_ALARM);
        //feign调用失败的情况
        if (ResponseStatus.SUCCESS.getCode() != customAlarm.getStatus().getCode())
            throw new BaseException(customAlarm.getMessage());

        //首次获取审批流程节点的必要参数
        JSONObject triggerParm = new JSONObject();
        triggerParm.put("channelId", "");  //不设置此值无法查询到结果

        SubmitProcessJxDTO submitProcessJxDTO = new SubmitProcessJxDTO();
        submitProcessJxDTO.setProcessId(customAlarm.getData().getProcessId());
        submitProcessJxDTO.setBerv(customAlarm.getData().getBerv());
        submitProcessJxDTO.setTriggerParm(triggerParm);

        ActionResponse<SubmitProcessJxDTO> nodeApprover = approveFeignClient.getNodeApprover(submitProcessJxDTO);
        //feign调用失败的情况
        if (ResponseStatus.SUCCESS.getCode() != nodeApprover.getStatus().getCode())
            throw new BaseException(customAlarm.getMessage());

        return nodeApprover;
    }

    @SneakyThrows
    @Override
    public void commitProcess(AlarmCommitProcessParam processParam, UserSimpleInfo user) {

        //检查当前登录用户是否是标签的创建者
        McdAppAlarmInfo mcdAppAlarmInfo = baseMapper.selectById(processParam.getAlarmId());
        if (!user.getUserId().equals(mcdAppAlarmInfo.getCreateUser()))
            throw new BaseException("此预警不是你创建的，无法提交审批！");

        //将数据复制到SubmitProcessQuery中，因feign调用使用的此实体类
        SubmitProcessQuery submitProcessQuery = BeanUtil.copyProperties(processParam, SubmitProcessQuery.class);
        submitProcessQuery.setBusinessId(String.valueOf(processParam.getAlarmId()));
        submitProcessQuery.setApprovalType(PecApproveFeignClient.CUSTOM_ALARM);

        //此方法是提交审批时的接口，远程调用方法为：commitProcess（易产生歧义）
        ActionResponse<Object> commitProcess = approveFeignClient.submit(submitProcessQuery);
        if (ResponseStatus.SUCCESS.getCode() != commitProcess.getStatus().getCode())
            throw new BaseException(commitProcess.getMessage());

        //修改预警状态
        mcdAppAlarmInfo.setApproveStatus(2);    //将预警的状态改为审批中
        mcdAppAlarmInfo.setApproveFlowId(commitProcess.getData().toString()); //添加流程编号
        baseMapper.updateById(mcdAppAlarmInfo);

        //是否保存日志，暂定
/*        CampLogType campAppr = CampLogType.CAMP_APPR;
        System.out.println(campAppr.getDesc());
        Class<CampLogType> campLogTypeClass = CampLogType.class;
        Field desc = campLogTypeClass.getDeclaredField("desc");
        desc.setAccessible(true);
        desc.set(campAppr, "提交自定义预警审批");
        System.out.println(campAppr.getDesc());

        logService.markSuccLog(processParam.getAlarmId(), campAppr, null, user);*/
    }

    /**
     * 修改客户通自定义预警
     *
     * @param user       用户信息
     * @param alarmParam 保存入参对象
     */
    @Override
    public void modifyAlarm(UserSimpleInfo user, ModifyAlarmParam alarmParam) {

        //在alarmId值不相同的情况下，检查是否存在相同名称的预警
        McdAppAlarmInfo mcdAppAlarmInfo = baseMapper.selectOne(new LambdaQueryWrapper<McdAppAlarmInfo>()
                .eq(McdAppAlarmInfo::getAlarmName, alarmParam.getAlarmName())
        );
        if (mcdAppAlarmInfo != null && !mcdAppAlarmInfo.getAlarmId().equals(alarmParam.getAlarmId()))
            throw new BaseException("预警名称：{" + alarmParam.getAlarmName() + "}已存在！");

        //检查预警类型是否有效
        // List<SelectAlarmTypeResult> alarmTypeResults = baseMapper.selectAlarmType(alarmParam.getDicKey());
        // if (alarmTypeResults.size() == 0) throw new BaseException("无效的字典KEY值！");

        //如果根据预警名称没有查找到相应的预警，就根据alarmId重新查一次
        if (mcdAppAlarmInfo == null) {
            if ((mcdAppAlarmInfo = baseMapper.selectById(alarmParam.getAlarmId())) == null)
                throw new BaseException("根据alarmId值：{" + alarmParam.getAlarmId() + "}没有查找到相应的预警，修改失败！");
        }

        //应当是只有在草稿：0，或者审批驳回时：4 才能修改预警信息
        if (!Arrays.asList(0, 4).contains(mcdAppAlarmInfo.getApproveStatus()))
            throw new BaseException("自定义预警只有在草稿或者审批驳回时才可修改！");

        //先查询表是否存在
        // Map<String, String> tableList = this.queryDataTableList(alarmParam.getSourceDb());
        // if (tableList.size() == 0 || !tableList.keySet().contains(alarmParam.getSourceTable()))
        //     throw new BaseException("无效的表名称：{" + alarmParam.getSourceTable() + "}！");

        //保存新的自定义预警
        BeanUtil.copyProperties(alarmParam, mcdAppAlarmInfo);

        // mcdAppAlarmInfo.setAlarmType(alarmTypeResults.get(0).getDicValue());
        mcdAppAlarmInfo.setApproveStatus(0); //如果修改了预警信息，应当变为草稿的状态
        mcdAppAlarmInfo.setApproveFlowId(null); //将审批流程编号也清除
        mcdAppAlarmInfo.setUpdateTime(DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
        if ("yitijimarket".equals(alarmParam.getSourceDb())) {
            mcdAppAlarmInfo.setSourceDb("1");
        } else if ("newmarket".equals(alarmParam.getSourceDb())) {
            mcdAppAlarmInfo.setSourceDb("2");
        } else if ("coc".equals(alarmParam.getSourceDb())) {
            mcdAppAlarmInfo.setSourceDb("3");
        }
        mcdAppAlarmInfo.setStartTime(DateUtil.format(alarmParam.getStartTime(), "yyyy-MM-dd"));
        mcdAppAlarmInfo.setEndTime(DateUtil.format(alarmParam.getEndTime(), "yyyy-MM-dd"));
        mcdAppAlarmInfo.setAlarmId(alarmParam.getAlarmId());
        mcdAppAlarmInfo.setExecuteCondition(alarmParam.getExecuteCondition());
        mcdAppAlarmInfo.setExecuteConditionVal(alarmParam.getExecuteConditionVal());
        mcdAppAlarmInfo.setAlarmExecDealtime(alarmParam.getAlarmExecDealtime());
        mcdAppAlarmInfo.setAlarmExecDealtimeUnit(alarmParam.getAlarmExecDealtimeUnit());
        mcdAppAlarmInfo.setIsDefineTime(alarmParam.isDefineTime() ? "true" : "false");
        mcdAppAlarmInfo.setAutoClearAlarmStatus(StrUtil.isEmpty(alarmParam.getAutoClearAlarmStatus()), "0", alarmParam.getAutoClearAlarmStatus());
        baseMapper.updateById(mcdAppAlarmInfo);
        Map<String, String> mapList = alarmParam.getAlarmThreshold().stream().map(v -> {
            String code = v.getAlarmCityCode();
            v.setAlarmCityCode("city" + code);
            v.setAlarmThrashValue(v.getAlarmThrashValue());
            return v;
        }).collect(Collectors.toMap(AlarmCityThrasholdResult::getAlarmCityCode, AlarmCityThrasholdResult::getAlarmThrashValue));
        mcdAppAlarmInfoMapper.updateAlarmThraCity(String.valueOf(mcdAppAlarmInfo.getAlarmId()), mapList);
    }

    /**
     * 根据alarmId值删除自定义预警
     *
     * @param user    用户信息
     * @param alarmId 预警id
     */
    @Override
    public void deleteAlarm(UserSimpleInfo user, Integer alarmId) {
        //根据alarmId检查是否存在相应的预警信息
        McdAppAlarmInfo mcdAppAlarmInfo = baseMapper.selectById(alarmId);
        if (mcdAppAlarmInfo == null) throw new BaseException("无效的alarmId值！");

        //应当是只有在草稿：0，或者终止时：3，或者审批驳回时：4 才能删除预警信息
        if (!Arrays.asList(0, 3, 4).contains(mcdAppAlarmInfo.getApproveStatus()))
            throw new BaseException("自定义预警只有在草稿或者审批驳回时才可修改！");

        if (!user.getUserId().equals(mcdAppAlarmInfo.getCreateUser()))
            throw new BaseException("此预警信息不是你创建的，删除失败！");

        //将预警信息逻辑删除
        mcdAppAlarmInfo.setDataStatus(0);
        baseMapper.updateById(mcdAppAlarmInfo);

    }

    /**
     * 根据alarmId值终止自定义预警
     *
     * @param user    用户信息
     * @param alarmId 预警id
     */
    @Override
    public void terminateAlarm(UserSimpleInfo user, Integer alarmId) {
        //根据alarmId检查是否存在相应的预警信息
        McdAppAlarmInfo mcdAppAlarmInfo = baseMapper.selectById(alarmId);
        if (mcdAppAlarmInfo == null) throw new BaseException("无效的alarmId值！");

        //应当是只有在审批完成时：1，才能终止预警信息
        if (mcdAppAlarmInfo.getApproveStatus() != 1)
            throw new BaseException("自定义预警只有在审批完成状态时才能终止预警！");

        if (!user.getUserId().equals(mcdAppAlarmInfo.getCreateUser()))
            throw new BaseException("此预警信息不是你创建的，无法终止！");

        //将预警信息状态改为终止状态
        mcdAppAlarmInfo.setApproveStatus(3);
        baseMapper.updateById(mcdAppAlarmInfo);
    }

    @Override
    public void modifyAlarmStatus(ModifyAlarmStatusParam alarmStatusParam) {
        //根据alarmId检查是否存在相应的预警信息
        McdAppAlarmInfo mcdAppAlarmInfo = baseMapper.selectById(alarmStatusParam.getAlarmId());
        if (mcdAppAlarmInfo == null) throw new BaseException("无效的alarmId值！");

        //修改状态的接口只给远程调用，所以只有在审批中：2 的预警才能修改
        if (mcdAppAlarmInfo.getApproveStatus() != 2)
            throw new BaseException("当前预警不是审批中的状态！");

        //更改预警信息审批状态和设置流程实例id
        mcdAppAlarmInfo.setApproveStatus(alarmStatusParam.getApproveStatus());
        mcdAppAlarmInfo.setApproveFlowId(alarmStatusParam.getApproveFlowId());
        baseMapper.updateById(mcdAppAlarmInfo);


    }

    @Override
    public String selectAlarmApproveFlowId(Integer alarmId) {
        McdAppAlarmInfo mcdAppAlarmInfo = baseMapper.selectById(alarmId);
        if (mcdAppAlarmInfo == null) throw new BaseException("无效的alarmId值！");
        return mcdAppAlarmInfo.getApproveFlowId();
    }

    @Override
    public Map<String, Object> queryStructure(@DataSource String dataSourceName, String tableName) {

        //先查询表是否存在
        Map<String, String> tableList = this.queryDataTableList(dataSourceName);
        if (tableList.size() == 0 || !tableList.keySet().contains(tableName))
            throw new BaseException("无效的表名称：{" + tableName + "}！");

        return baseMapper.queryStructure(tableName);
    }

    @Override
    public Map<String, String> queryDataSourceList() {

        Map<String, DataSourceProperties> datasource = dataSourceProperties.getDatasource();
        LinkedHashMap<String, String> datasourceMap = new LinkedHashMap<>();

        Iterator<Map.Entry<String, DataSourceProperties>> datasourceIterator = datasource.entrySet().iterator();
        while (datasourceIterator.hasNext()) {
            Map.Entry<String, DataSourceProperties> nextDatasource = datasourceIterator.next();
            String alias = nextDatasource.getValue().getName();
            //如果为数据源取了别名，就将value设定为别名，否则value取key值
            if (!nextDatasource.getKey().equals("khtmanageusedb")) { // 客户通管理界面使用的数据源，不需要展示在预警数据源的下拉
                if (StringUtils.isNotBlank(alias)) {
                    datasourceMap.put(nextDatasource.getKey(), alias);
                } else {
                    datasourceMap.put(nextDatasource.getKey(), nextDatasource.getKey());
                }
            }
        }

        return datasourceMap;
    }

    //查询时间太长，增加缓存
    @Override
    @Cacheable(value = "selfDefinedAlarmCache@30*60", key = "'DATA_SOURCE_'+#dataSourceName")
    public Map<String, String> queryDataTableList(@DataSource String dataSourceName) {
        //查询符合手机号码字段的表
        List<Map<String, String>> dataTableListMaps = baseMapper.queryDataTableList(phoneFieldNames);
        //用于存储表名称和批注
        Map<String, String> dataTableMaps = new LinkedHashMap<>();

        ArrayList<String> tableComment;
        for (int i = 0; i < dataTableListMaps.size(); i++) {
            tableComment = new ArrayList<>(dataTableListMaps.get(i).values());
            dataTableMaps.put(tableComment.get(0), tableComment.get(1));
        }
        return dataTableMaps;
    }

    /**
     * 根据数据源名称和表名称验证表名以及获取表的变量信息
     *
     * @param dataSourceName 数据源名称
     * @param tableName      表名
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public ActionResponse<List<Map<String, Object>>> querySourceTable(String dataSourceName, String tableName) {
        List<Map<String, Object>> tabAttr = null;
        List<Map<String, Object>> tabData = null;
        ActionResponse response = ActionResponse.getSuccessResp();
        StringJoiner sj = new StringJoiner(StrUtil.COMMA, "\"", "\"");
        try {
            if ("coc".equals(dataSourceName)) {// COC客户群查询mcd_core本库,sourceTabName传入的是客户群ID
                tabAttr = baseMapper.queryCocSourceTableAttr(tableName);
                if (CollectionUtils.isEmpty(tabAttr)) {
                    return ActionResponse.getSuccessResp(new ArrayList<>());
                }
                String userTable = tabAttr.get(0).get("USER_TABLE").toString();
                tabData = baseMapper.querySourceTable(userTable);
            } else {// 其它从集市库(一体机集市库、新集市库),sourceTabName传入的是用户名.表名
                String owner = null;
                String tarTableName = null;
                if (tableName.contains(".")) {
                    String[] temp = tableName.split("\\.");
                    owner = temp[0];
                    tarTableName = temp[1];
                } else {
                    tarTableName = tableName;
                }
                tabAttr = baseMapper.querySourceTableAttr(dataSourceName, tarTableName, owner);
                if (CollectionUtils.isEmpty(tabAttr)) {
                    return ActionResponse.getSuccessResp(new ArrayList<>());
                }
                tabData = baseMapper.querySourceTable(tableName);
            }
            Map<String, Object> firstData = null;
            if (CollectionUtils.isNotEmpty(tabData)) {
                firstData = tabData.get(0);
            }
            for (Map<String, Object> tempAttr : tabAttr) {
                if (firstData != null) {
                    String attrName = tempAttr.get("ATTR_NAME").toString();
                    tempAttr.put("DATA_VALUE", firstData.get(attrName));
                } else {
                    tempAttr.put("DATA_VALUE", "");
                }
            }

            //字段是否存在、是否有null值校验
            int num = 0;
            for (Map.Entry<String, String> entry : checkColumnMap.entrySet()) {
                try {
                    int count = baseMapper.columnCheck(tableName, entry.getKey());
                    if (count > 0) {
                        sj.add(entry.getValue());
                        num++;
                    }
                } catch (Exception e) {
                    sj.add(entry.getValue());
                    num++;
                }
            }
            String msg = null;
            if (num != 0) {
                msg = StrUtil.format("必填字段{}未填写，请补充。", sj.toString());
                response = ActionResponse.getFaildResp(msg);
            } else {
                response.setData(tabAttr);
            }

        } catch (Exception e) {
            log.error("querySourceTable-->根据数据源名称和表名称验证表名以及获取表的变量信息异常：", e);
        }
        return response;
    }

    /**
     * 根据用户id查询角色id
     *
     * @param userId 当前登录用户id
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getUserRoleList(String userId) {
        return mcdAppAlarmInfoMapper.getUserRoleList(userId);
    }

    /**
     * 解警产品批量导入(全量数据覆盖)
     *
     * @param file 需要导入的文件数据
     * @param user 当前登录用户信息
     */
    @Override
    public void batchImportAutoClearAlarm(MultipartFile file, UserSimpleInfo user) throws Exception {
        // 1.导入之前先清空原表数据
        mcdAppAlarmInfoMapper.truncateData();
        // 2.解析excel
        List<Object[]> objects = ExcelUtils.readeExcelData(file.getInputStream(), 0, 1);
        Set<McdAutoClearAlarmPlan> mcdAutoClearAlarmPlanSet = new HashSet<>();
        for (Object[] res : objects) {
            McdAutoClearAlarmPlan plan = new McdAutoClearAlarmPlan();
            if (res == null || res.length < 3) {
                throw new Exception("解警产品批量导入数据不完整");
            }
            plan.setPlanId((String) res[0]);
            plan.setPlanName((String) res[1]);
            plan.setPlanTypeName((String) res[2]);
            plan.setCreateUser(user.getUserId());
            plan.setCreateCity(user.getCityId());
            mcdAutoClearAlarmPlanSet.add(plan);
        }
        // 3.解析完成后入库
        if (CollectionUtil.isNotEmpty(mcdAutoClearAlarmPlanSet)) {
            mcdAppAlarmInfoMapper.batchInsertAlarmPlanTab(mcdAutoClearAlarmPlanSet);
            log.info("解警产品批量导入成功，已导入={}个产品", mcdAutoClearAlarmPlanSet.size());
        }
    }

    /**
     * 查询预警名称以及创建人信息
     *
     * @param alarmId 预警id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> queryAlarmNameAndCreator(String alarmId) {
        return mcdAppAlarmInfoMapper.queryAlarmNameAndCreator(alarmId);
    }

    /**
     * 更新预警阈值信息
     *
     * @param param 预警阈值地市信息
     */
    @Override
    public boolean updateThreshold(McdCityThreshParam param) {
        List<McdAlarmThresholdLog> thresholdLogList = new ArrayList<>();

        Map<String, String> mapList = param.getNewAlarmThreshold().stream().map(v -> {
            String code = v.getAlarmCityCode();
            v.setAlarmCityCode("city" + code);
            v.setAlarmThrashValue(v.getAlarmThrashValue());
            return v;
        }).collect(Collectors.toMap(AlarmCityThrasholdResult::getAlarmCityCode, AlarmCityThrasholdResult::getAlarmThrashValue));
        mcdAppAlarmInfoMapper.updateAlarmThraCity(String.valueOf(param.getAlarmId()), mapList);
        log.info("预警阈值更新入库成功! ");
        param.getOldAlarmThreshold().forEach(old -> {
            McdAlarmThresholdLog thresholdLog = new McdAlarmThresholdLog();
            thresholdLog.setAlarmId(param.getAlarmId());
            thresholdLog.setUserId(param.getUserId());
            thresholdLog.setCityId(param.getCityId());
            thresholdLog.setAlarmCity("city" + old.getAlarmCityCode());
            thresholdLog.setOldAlarmThreshold(old.getAlarmThrashValue());
            param.getNewAlarmThreshold().forEach(news -> {
                if (thresholdLog.getAlarmCity().equals(news.getAlarmCityCode())) {
                    thresholdLog.setAlarmCity("city" + news.getAlarmCityCode());
                    thresholdLog.setNewAlarmThreshold(StringUtils.isEmpty(news.getAlarmThrashValue()) ? null : news.getAlarmThrashValue());
                }
            });
            String oldAlarmThre = StringUtils.isEmpty(thresholdLog.getOldAlarmThreshold()) ? "-1" : thresholdLog.getOldAlarmThreshold();
            String newAlarmThre =StringUtils.isEmpty(thresholdLog.getNewAlarmThreshold()) ? "-1" : thresholdLog.getNewAlarmThreshold();
            if(oldAlarmThre.equals(newAlarmThre)) return;
            thresholdLogList.add(thresholdLog);
        });

        return mcdAppAlarmInfoMapper.insertAlarmThreLog(thresholdLogList);
    }

    /**
     * 获取地市信息
     *
     * @return
     */
    @Override
    public List<McdDimCity> queryAllCitys() {
        return mcdAppAlarmInfoMapper.queryAllCityCode();
    }


}
