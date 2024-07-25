package com.asiainfo.biapp.pec.plan.jx.mmstemplates.service;

import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.mmstemplates.vo.McdMmsTemplate;

import java.util.List;

public interface IMcdMmsTemplatesService {
    int queryMmsTemplateCount(String keyWords) throws Exception;

    List<McdMmsTemplate> queryMmsTemplate(Pager pager, String keyWords) throws Exception;

    boolean saveMmsTemplate(McdMmsTemplate template);

    boolean updateMmsTemplates(String id, String mmstaskNum, String mmsTemplateSubject, String mmsAddress);

    int getMmsTemplateStatus(String id);

    boolean deleteMmsTemplates(String id);
}
