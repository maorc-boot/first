package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.asiainfo.biapp.pec.approve.dto.ApproverProcessDTO;
import com.asiainfo.biapp.pec.approve.jx.dao.ApproveRecordDao;
import com.asiainfo.biapp.pec.approve.jx.dto.*;
import com.asiainfo.biapp.pec.approve.jx.service.ApproveRecordService;
import com.asiainfo.biapp.pec.approve.jx.service.feign.CustomAlarmFeignClient;
import com.asiainfo.biapp.pec.approve.jx.service.feign.CustomLabelFeignClient;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.SelectAlarmResultInfo;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <p>
 * 审批流程记录表 服务实现类
 * </p>
 *
 * @author feify
 * @since 2022-06-07
 */
@Service
public class ApproveRecordServiceImpl extends ServiceImpl<ApproveRecordDao, CmpApproveProcessRecord> implements ApproveRecordService {

    @Autowired
    private CustomAlarmFeignClient customAlarmFeignClient;

    @Override
    public List<ApproverProcessDTO> getApproverProcess(ApproverProcessDTO approverProcessDTO) {
        return baseMapper.getApproverProcess(approverProcessDTO.getBusinessId());
    }

    @Override
    public List<ApproverProcessNewDTO> getApproverProcessNew(ApproverProcessDTO approverProcessDTO) {
        return baseMapper.getApproverProcessNew(approverProcessDTO.getBusinessId(),approverProcessDTO.getInstanceId());
    }


    @Override
    public IPage<CmpApproveProcessRecordJx> getRecordByUserNew(Page<CmpApproveProcessRecordJx> page, String userId, Integer dealStatus, List<String> list) {
        return baseMapper.getRecordByUserNew(page, userId, dealStatus,list);
    }

    @Override
    @Transactional
    public IPage<McdCampCmpApproveProcessRecordJx> queryCampAppRecord( McdCampApproveJxNewQuery param) {
        Page<McdCampCmpApproveProcessRecordJx> page = new Page<>();
        page.setCurrent(param.getCurrent());
        page.setSize(param.getSize());

        // 2023-07-05 17:50:47 针对自定义标签和自定义预警做不同的查询
        if (CustomLabelFeignClient.CUSTOM_LABEL.equals(param.getEnumKey())){
            return baseMapper.queryCustomSelfDefinedLabel(page,param);
        }else if (CustomAlarmFeignClient.CUSTOM_ALARM.equals(param.getEnumKey())){
            return AppAlarmLogic(param, page);
        }
        return baseMapper.queryCampAppRecord(page,param);
    }

    /**
     * 自定义预警查询待审批列表信息逻辑
     *
     * @param param 入参
     * @param page 返回的分页数据
     * @return {@link IPage}<{@link McdCampCmpApproveProcessRecordJx}>
     */
    private IPage<McdCampCmpApproveProcessRecordJx> AppAlarmLogic(McdCampApproveJxNewQuery param, Page<McdCampCmpApproveProcessRecordJx> page) {
        Map<Integer, SelectAlarmResultInfo> collect = null;
        List<Integer> alarmIds = null;
        // 1.查询审批中的预警
        ActionResponse<Page<SelectAlarmResultInfo>> selectAlarmResultInfoActionResponse = customAlarmFeignClient.queryApprovingAlarm(param);
        if (selectAlarmResultInfoActionResponse.getStatus().getCode() == ResponseStatus.SUCCESS.getCode() && CollectionUtil.isNotEmpty(selectAlarmResultInfoActionResponse.getData().getRecords())) {
            // 1.1 预警id映射预警信息的map
            collect = selectAlarmResultInfoActionResponse.getData().getRecords().stream().collect(Collectors.toMap(SelectAlarmResultInfo::getAlarmId, Function.identity()));
            // 1.2 获取所有审批中预警id
            alarmIds = Lists.newArrayList(collect.keySet());
        }
        if (CollectionUtil.isEmpty(alarmIds) || ObjectUtil.isEmpty(collect)) {
            log.warn("queryCampAppRecord-->查询审批中的预警信息为空");
            return new Page<>();
        }
        // 2. 根据查询的预警id关联审批表获取审批信息
        IPage<McdCampCmpApproveProcessRecordJx> mcdCampCmpApproveProcessRecordJxIPage = baseMapper.queryCustomSelfDefinedAlarm(page, alarmIds, param);
        List<McdCampCmpApproveProcessRecordJx> records = mcdCampCmpApproveProcessRecordJxIPage.getRecords();
        List<McdCampCmpApproveProcessRecordJx> recordsNew = new ArrayList<>();
        // 3. 循环组装界面所属数据返回
        for (McdCampCmpApproveProcessRecordJx record : records) {
            McdCampCmpApproveProcessRecordJx mcdCampCmpApproveProcessRecordJxNew = new McdCampCmpApproveProcessRecordJx();
            BeanUtil.copyProperties(record, mcdCampCmpApproveProcessRecordJxNew);
            // 3.1 根据预警id获取预警信息并组装
            SelectAlarmResultInfo selectAlarmResultInfo = collect.get(Integer.valueOf(record.getBusinessId()));
            if (ObjectUtil.isNotEmpty(selectAlarmResultInfo)) {
                // mcdCampCmpApproveProcessRecordJxNew.setBusinessId(record.getBusinessId());
                mcdCampCmpApproveProcessRecordJxNew.setCampsegName(selectAlarmResultInfo.getAlarmName());
                // mcdCampCmpApproveProcessRecordJxNew.setNodeName(record.getNodeName());
                mcdCampCmpApproveProcessRecordJxNew.setType(selectAlarmResultInfo.getType());
                mcdCampCmpApproveProcessRecordJxNew.setCreateUserId(selectAlarmResultInfo.getCreateUserId());
                mcdCampCmpApproveProcessRecordJxNew.setCreateTime(DateUtil.parse(selectAlarmResultInfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                recordsNew.add(mcdCampCmpApproveProcessRecordJxNew);
            }
        }
        mcdCampCmpApproveProcessRecordJxIPage.setRecords(recordsNew);
        return mcdCampCmpApproveProcessRecordJxIPage;
    }

    /**
     * 根据用户ID查询所有待审批节点
     *
     * @param userId
     * @return
     */
    @Override
    public List<ApproveNodeDTO> queryApproveNodesByUserId(String userId,String channelId) {
        return baseMapper.queryApproveNodesByUserId(userId,channelId);
    }
}
