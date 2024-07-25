package com.asiainfo.biapp.pec.plan.jx.smstemplates.service;

import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdDimSmsBossTemplateType;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdSmsTemplatesInfo;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.MtlChannelBossSmsTemplate;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsTemplatesQuery;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsTemplatesRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISmsTemplatesService extends IService<MtlChannelBossSmsTemplate> {

    IPage<McdSmsTemplatesInfo> getSmsTemplateList(McdSmsTemplatesQuery templatesQuery);

    List<McdDimSmsBossTemplateType> querySmsTemplateList(McdSmsTemplatesRequest templatesQuery);

    boolean smsTemplateOffline(McdSmsTemplatesQuery req);

}
