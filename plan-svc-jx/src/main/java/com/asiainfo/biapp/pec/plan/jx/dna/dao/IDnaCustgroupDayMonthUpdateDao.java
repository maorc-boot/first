package com.asiainfo.biapp.pec.plan.jx.dna.dao;

import com.asiainfo.biapp.pec.plan.jx.dna.vo.DnaCustgroupUpdateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description: DNA客群更新调度(日周期/月周期)dao层
 *
 * @author: lvchaochao
 * @date: 2023/12/19
 */
public interface IDnaCustgroupDayMonthUpdateDao {

    /**
     * DNA客群更新调度(日周期/月周期)任务
     *
     * @return {@link List}<{@link DnaCustgroupUpdateVo}>
     */
    List<DnaCustgroupUpdateVo> queryNeedUpdateCustgroup(@Param("custGroupId") String custGroupId);
    // List<DnaCustgroupUpdateVo> queryNeedUpdateCustgroup(@Param("type") String type);
}
