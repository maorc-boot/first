package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCallOutRecordingDetail;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdOutCallQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdOutDetailsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMcdCallDetailsMapper extends BaseMapper<McdCallOutRecordingDetail> {

    IPage<McdOutDetailsVo> queryCallDetailsList(@Param("page") IPage<McdOutDetailsVo> page, @Param("query") McdOutCallQuery query);

    String getUrl();
}
