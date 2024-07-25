package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCustTitleList;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdCustomTitleQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface McdCustTitleDao extends BaseMapper<McdCustTitleList> {

    List<McdCustTitleList> queryCustomizeTitleList(@Param("page") Page<McdCustTitleList> page, @Param("query") McdCustomTitleQuery query);

    List<String> queryExistCustTitleList(@Param("list") List<String> phoneList);

}
