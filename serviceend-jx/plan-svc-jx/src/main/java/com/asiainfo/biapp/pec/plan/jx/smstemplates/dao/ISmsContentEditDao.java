package com.asiainfo.biapp.pec.plan.jx.smstemplates.dao;

import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdSmsContentEditInfo;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsContentQuery;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsContentTemplateQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ISmsContentEditDao extends BaseMapper<McdSmsContentEditInfo> {

    IPage<McdSmsContentEditInfo>   getSmsContentEditList(Page page, @Param("param") McdSmsContentQuery contentQuery);
    IPage<McdSmsContentEditInfo>   getSmsContentTemplateList(Page page, @Param("param") McdSmsContentTemplateQuery contentQuery);
}
