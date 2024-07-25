package com.asiainfo.biapp.pec.plan.jx.smstemplates.service.impl;

import com.asiainfo.biapp.pec.plan.jx.smstemplates.dao.ISmsContentEditDao;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdSmsContentEditInfo;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.service.ISmsContentEditService;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsContentQuery;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsContentTemplateQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SmsContentEditServiceImpl extends ServiceImpl<ISmsContentEditDao, McdSmsContentEditInfo> implements ISmsContentEditService {

    @Resource
    ISmsContentEditDao smsContentEditDao;

    @Override
    public IPage<McdSmsContentEditInfo> getSmsContentEditList(McdSmsContentQuery contentQuery) {
        Page page = new Page();
        page.setCurrent(contentQuery.getCurrent());
        page.setSize(contentQuery.getSize());
        return smsContentEditDao.getSmsContentEditList(page,contentQuery);
    }

    @Override
    public IPage<McdSmsContentEditInfo> getSmsContentTemplateList(McdSmsContentTemplateQuery contentQuery) {
        Page page = new Page();
        page.setCurrent(contentQuery.getCurrent());
        page.setSize(contentQuery.getSize());
        return smsContentEditDao.getSmsContentTemplateList(page,contentQuery);
    }
}
