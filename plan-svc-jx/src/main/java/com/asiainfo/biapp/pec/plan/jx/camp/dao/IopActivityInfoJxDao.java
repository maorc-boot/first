package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.plan.jx.camp.req.QueryActivityJxQuery;
import com.asiainfo.biapp.pec.plan.model.IopActivityInfo;
import com.asiainfo.biapp.pec.plan.vo.ActivityVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 活动模板信息 Mapper 接口
 * </p>
 *
 * @author imcd
 * @since 2022-12-16
 */
public interface IopActivityInfoJxDao extends BaseMapper<IopActivityInfo> {

    IPage<ActivityVO> queryActivityInfo(Page<?> page, @Param("param") QueryActivityJxQuery form);

    /**
     * 活动状态修改
     *
     * @param activityTemplateId
     * @param activityId
     */
    void updateActivityStatus(@Param("activityTemplateId") String activityTemplateId, @Param("activityId") String activityId, @Param("status") String status) throws Exception;

}
