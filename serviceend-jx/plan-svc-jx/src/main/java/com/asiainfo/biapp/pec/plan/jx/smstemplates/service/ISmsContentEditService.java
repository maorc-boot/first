package com.asiainfo.biapp.pec.plan.jx.smstemplates.service;

import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdSmsContentEditInfo;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsContentQuery;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsContentTemplateQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISmsContentEditService extends IService<McdSmsContentEditInfo> {

    IPage<McdSmsContentEditInfo> getSmsContentEditList(McdSmsContentQuery contentQuery);
    IPage<McdSmsContentEditInfo> getSmsContentTemplateList(McdSmsContentTemplateQuery contentQuery);



}
