package com.asiainfo.biapp.pec.plan.jx.coordination.dao;

import com.asiainfo.biapp.pec.plan.jx.coordination.req.CampCoordinationApproveQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.McdStrategicCoordinationReq;
import com.asiainfo.biapp.pec.plan.jx.coordination.response.ApprTaskRecord;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.StrategicCoordinationBaseInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface McdStrategicCoordinationDao {

    //策略池分页查询
    Page<StrategicCoordinationBaseInfo>  queryStrategicCoordinationBaseInfos(Page page, @Param("req")McdStrategicCoordinationReq req);

    //融合计算全部选择添加
    List<StrategicCoordinationBaseInfo> selectAllStrategicCoordinationList(@Param("req")McdStrategicCoordinationReq req);


    List<ApprTaskRecord> qryApprTaskRecord(@Param("taskIds") Set<String> taskIds, @Param("req") CampCoordinationApproveQuery req);

    /**
     * 统筹子任务待审批列表查询
     *
     * @param taskIds     父任务id
     * @param qryApproved true--审批列表通过主任务查询子任务 false--计算结果通过主任务查询子任务
     * @param status      状态
     * @return {@link List}<{@link ApprTaskRecord}>
     */
    List<ApprTaskRecord> approveChildTaskRecord(@Param("taskIds") List<String> taskIds, @Param("qryApproved") boolean qryApproved, @Param("status") String status);



}
