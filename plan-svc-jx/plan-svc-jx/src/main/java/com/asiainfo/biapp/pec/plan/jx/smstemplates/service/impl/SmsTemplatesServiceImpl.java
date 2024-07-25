package com.asiainfo.biapp.pec.plan.jx.smstemplates.service.impl;

import com.asiainfo.biapp.pec.plan.jx.smstemplates.dao.ISmsTemplateDao;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdDimSmsBossTemplateType;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdSmsTemplatesInfo;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.MtlChannelBossSmsTemplate;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.service.ISmsTemplatesService;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsTemplatesQuery;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsTemplatesRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SmsTemplatesServiceImpl extends ServiceImpl<ISmsTemplateDao, MtlChannelBossSmsTemplate> implements ISmsTemplatesService {

    @Resource
    ISmsTemplateDao smsTemplateDao;
    @Override
    public IPage<McdSmsTemplatesInfo> getSmsTemplateList(McdSmsTemplatesQuery templatesQuery) {
        Page page = new Page();
        page.setCurrent(templatesQuery.getCurrent());
        page.setSize(templatesQuery.getSize());
        return smsTemplateDao.getSmsTemplateList(page ,templatesQuery);
    }


    @Override
    public boolean smsTemplateOffline(McdSmsTemplatesQuery req) {

        if (Objects.nonNull(req.getTemplateId())) {
           int num =  smsTemplateDao.smsTemplateOffline(req.getTemplateId());
           if (num > 0 ){
               return true;
           }

            updateSmsTemplateStatus(req.getTemplateId(),req.getStatus());
           return false;
        }

        if (Objects.nonNull(req.getTemplateIds())){
            List<String>  templateIds = req.getTemplateIds();
            for (String templateId : templateIds) {
                int num = smsTemplateDao.smsTemplateOffline(templateId);
                if (num > 0){
                    return true;
                }
            }

            for (String templateId : templateIds) {
                updateSmsTemplateStatus(templateId,req.getStatus());
            }

        }


        return false;
    }


    private void updateSmsTemplateStatus(String templateId,int status){

        MtlChannelBossSmsTemplate templatesInfo =  smsTemplateDao.selectById(templateId);
        templatesInfo.setStatus(status);

        //线下操作
        smsTemplateDao.updateById(templatesInfo);
    }


    @Override
    public List<McdDimSmsBossTemplateType> querySmsTemplateList(McdSmsTemplatesRequest templatesQuery) {
         List<McdSmsTemplatesInfo> templates = smsTemplateDao.getChannelBossSmsTemplates(templatesQuery.getChannelId(), templatesQuery.getAdviId());

         List<McdDimSmsBossTemplateType> types = smsTemplateDao.getSmsBossTemplateTypes(templatesQuery.getChannelId(), templatesQuery.getAdviId());

        for(int i=0;i<types.size();i++){
            McdDimSmsBossTemplateType type =  types.get(i);
            List<McdSmsTemplatesInfo> tmp = new ArrayList<>();
            for(int j=0;j<templates.size();j++){
                McdSmsTemplatesInfo template =templates.get(j);
                if(type.getTypeId().equals(template.getTemplateTypeId())){
                    tmp.add(template);
                }
            }
            type.setTemplateList(tmp);
        }
        return types;
    }
}
