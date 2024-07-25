package com.asiainfo.biapp.pec.plan.jx.mmstemplates.dao;

import com.asiainfo.biapp.pec.plan.jx.mmstemplates.vo.McdMmsTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMcdMmsTemplatesServiceDao {
    int queryMmsTemplateCount(@Param("keyWords") String keyWords);

    List<McdMmsTemplate> queryMmsTemplate(@Param("pageSize") String pageSize, @Param("pageNum") String pageNum,
                                               @Param("keyWords") String keyWords);

    boolean updateMmsTemplate(@Param("id") String id, @Param("mmstaskNum") String mmstaskNum,
                              @Param("mmsTemplateSubject") String mmsTemplateSubject,@Param("mmsAddress") String mmsAddress);

    boolean saveMmsTemplate(@Param("template") McdMmsTemplate template);

    int getMmsTemplateStatus(@Param("templateId") String templateId);

    boolean removeMmsTemplate(@Param("id") String id);
}
