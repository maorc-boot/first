package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.model.ReportYjDetailRecording;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdAlarmReportQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdTouchTraceDetailsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMcdTouchTraceMapper extends BaseMapper<ReportYjDetailRecording> {

    /**
     * 外呼轨迹查询
     *
     * @param page
     * @param query
     * @return
     */
    IPage<McdTouchTraceDetailsVo> queryTouchTraceList(@Param("page") IPage page, @Param("query") McdAlarmReportQuery query);
}
