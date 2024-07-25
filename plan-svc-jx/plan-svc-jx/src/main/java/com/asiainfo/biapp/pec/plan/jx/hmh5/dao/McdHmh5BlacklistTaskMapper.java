package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdHmh5BlacklistTask;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.BlacklistApprRecord;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.BlacklistApproveJxQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 客户通黑名单批量导入任务 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2024-05-28
 */
public interface McdHmh5BlacklistTaskMapper extends BaseMapper<McdHmh5BlacklistTask> {

    /**
     * 客户通黑名单审批列表查询
     *
     * @param taskIds 任务编码集合
     * @param req req
     * @return List<BlacklistApprRecord>
     */
    List<BlacklistApprRecord> qryApprRecord(@Param("taskIds") Set<String> taskIds, @Param("query") BlacklistApproveJxQuery req);

}
