package com.asiainfo.biapp.pec.plan.jx.mmstemplates.service.impl;

import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.mmstemplates.dao.IMcdMmsTemplatesServiceDao;
import com.asiainfo.biapp.pec.plan.jx.mmstemplates.service.IMcdMmsTemplatesService;
import com.asiainfo.biapp.pec.plan.jx.mmstemplates.vo.McdMmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class McdMmsTemplatesServiceImpl implements IMcdMmsTemplatesService {

    @Autowired
    IMcdMmsTemplatesServiceDao mmsTemplatesServiceDao;

    @Override
    public int queryMmsTemplateCount(String keyWords) throws Exception {
        return mmsTemplatesServiceDao.queryMmsTemplateCount(keyWords);
    }

    @Override
    public List<McdMmsTemplate> queryMmsTemplate(Pager pager, String keywords) throws Exception {
        String pageSize = String.valueOf(pager.getPageSize());
        String pageNum = String.valueOf(pager.getPageNum());
        int start = (Integer.valueOf(pageNum) -1) * Integer.valueOf(pageSize);
        return mmsTemplatesServiceDao.queryMmsTemplate(pageSize, String.valueOf(start), keywords);
    }

    @Override
    public boolean saveMmsTemplate(McdMmsTemplate template) {
        boolean isSuccess = mmsTemplatesServiceDao.saveMmsTemplate(template);
        return isSuccess;
    }

    @Override
    public boolean updateMmsTemplates(String id, String mmstaskNum, String mmsTemplateSubject, String mmsAddress) {
        log.info("更新模板编号，参数：mmsNum={},更新前模板编号：templateId = {}", mmstaskNum, id);
        boolean isSuccess = mmsTemplatesServiceDao.updateMmsTemplate(id, mmstaskNum, mmsTemplateSubject, mmsAddress);
        return isSuccess;
    }

    @Override
    public int getMmsTemplateStatus(String id) {
        int cnt = 0;
        if (id.contains(",")) {
            String[] arrs = id.split(",");
            for (String templateId : arrs) {
                cnt = mmsTemplatesServiceDao.getMmsTemplateStatus(templateId);
                if (cnt > 0) {
                    break;
                }
            }
        } else {
            cnt = mmsTemplatesServiceDao.getMmsTemplateStatus(id);
        }
        return cnt;
    }

    @Override
    public boolean deleteMmsTemplates(String ids) {
        log.info("删除模板，参数：ids={}", ids);
        boolean result = true;
        if (ids.contains(",")) {
            String[] arr = ids.split(",");
            for (String id : arr) {
                result = mmsTemplatesServiceDao.removeMmsTemplate(id);
            }
        } else {
            result = mmsTemplatesServiceDao.removeMmsTemplate(ids);
        }
        return result;
    }
}
