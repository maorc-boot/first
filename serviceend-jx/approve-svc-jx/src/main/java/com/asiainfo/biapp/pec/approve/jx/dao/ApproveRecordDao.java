package com.asiainfo.biapp.pec.approve.jx.dao;

import com.asiainfo.biapp.pec.approve.dto.ApproverProcessDTO;
import com.asiainfo.biapp.pec.approve.jx.dto.*;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 审批流程记录表 Mapper 接口
 * </p>
 *
 * @author feify
 * @since 2022-06-08
 */
@Mapper
public interface ApproveRecordDao extends BaseMapper<CmpApproveProcessRecord> {

    List<ApproverProcessDTO> getApproverProcess(@Param("businessId") String businessId);

    List<ApproverProcessNewDTO> getApproverProcessNew(@Param("businessId") String businessId, @Param("instanceId") Long instanceId);

    IPage<CmpApproveProcessRecordJx> getRecordByUserNew(Page<CmpApproveProcessRecordJx> page, @Param("userId") String userId, @Param("dealStatus") Integer dealStatus, @Param("list") List<String> list);

    IPage<McdCampCmpApproveProcessRecordJx> queryCampAppRecord(Page<McdCampCmpApproveProcessRecordJx> page,@Param("param") McdCampApproveJxNewQuery param);

    IPage<McdCampCmpApproveProcessRecordJx> queryCustomSelfDefinedLabel(Page page, @Param("param") McdCampApproveJxNewQuery param);

    // IPage<McdCampCmpApproveProcessRecordJx> queryCustomSelfDefinedAlarm(Page page, @Param("param") McdCampApproveJxNewQuery param);
    IPage<McdCampCmpApproveProcessRecordJx> queryCustomSelfDefinedAlarm(Page page, @Param("alarmIds") List<Integer> alarmIds, @Param("param") McdCampApproveJxNewQuery param);

    /**
     * 根据用户ID查询所有待审批节点
     * @param userId
     * @return
     */
    List<ApproveNodeDTO> queryApproveNodesByUserId(@Param("userId") String userId , @Param("channelId") String channelId);
}
