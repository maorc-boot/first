package com.asiainfo.biapp.pec.plan.jx.smstemplates.dao;

import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdDimSmsBossTemplateType;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdSmsTemplatesInfo;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.MtlChannelBossSmsTemplate;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsTemplatesQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ISmsTemplateDao extends BaseMapper<MtlChannelBossSmsTemplate> {

    IPage<McdSmsTemplatesInfo> getSmsTemplateList(Page  page,@Param("param") McdSmsTemplatesQuery templatesQuery);

     int smsTemplateOffline(@Param("templateId") String templateId);

     List<McdDimSmsBossTemplateType> getSmsBossTemplateTypes(@Param("channelId") String channelId, @Param("adivId") String adivId);

     List<McdSmsTemplatesInfo> getChannelBossSmsTemplates(@Param("channelId") String channelId, @Param("adivId") String adivId);
}
