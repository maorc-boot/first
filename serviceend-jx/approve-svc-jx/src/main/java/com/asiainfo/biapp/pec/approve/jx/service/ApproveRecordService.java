package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.dto.ApproverProcessDTO;
import com.asiainfo.biapp.pec.approve.jx.dto.*;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批流程记录表 服务类
 * </p>
 *
 * @author wanghao
 * @since 2021-11-02
 */
public interface ApproveRecordService extends IService<CmpApproveProcessRecord> {

    public List<ApproverProcessDTO> getApproverProcess(ApproverProcessDTO approverProcessDTO);

    public List<ApproverProcessNewDTO> getApproverProcessNew(ApproverProcessDTO approverProcessDTO);

    /**
     * @param userId 当前用户id
     * @param dealStatus record状态
     * @return List<CmpApproveProcessRecord>
     */
    IPage<CmpApproveProcessRecordJx> getRecordByUserNew(Page<CmpApproveProcessRecordJx> page, String userId, Integer dealStatus, List<String> list);
    IPage<McdCampCmpApproveProcessRecordJx> queryCampAppRecord( McdCampApproveJxNewQuery param);

    /**
     * 根据用户ID查询所有待审批节点
     * @param userId
     * @return
     */
    List<ApproveNodeDTO> queryApproveNodesByUserId(String userId,String channelId);


}
