package com.asiainfo.biapp.pec.plan.jx.mmstemplates.controller;


import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.mmstemplates.service.IMcdMmsTemplatesService;
import com.asiainfo.biapp.pec.plan.jx.mmstemplates.vo.McdMmsTemplate;
import com.asiainfo.biapp.pec.plan.jx.mmstemplates.vo.McdMmsTemplateHttpVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/action/mmsTemplates")
@RestController
@Api(tags = "江西:彩信模板")
public class McdMmsTemplatesController {

    @Autowired
    IMcdMmsTemplatesService mmsTemplatesService;

    @ApiOperation(value = "查询彩信模板")
    @RequestMapping(value = "/queryMmsTemplates", method = {RequestMethod.POST})
    public Pager queryMmsTemplatesList(@RequestBody McdMmsTemplateHttpVo mmsTemplate) {
        List<McdMmsTemplate> templateList = new ArrayList<McdMmsTemplate>();
        Pager pager = new Pager();
        try {
            String pageSize = mmsTemplate.getPageSize() != null ? mmsTemplate.getPageSize() : "10";
            String pageNum = mmsTemplate.getPageNum() != null ? mmsTemplate.getPageNum() : "0";
            pager.setPageSize(pageSize);
            pager.setPageNum(pageNum); // 当前页
            String keywords = mmsTemplate.getKeywords();
            pager.setTotalSize(mmsTemplatesService.queryMmsTemplateCount(keywords));
            templateList = mmsTemplatesService.queryMmsTemplate(pager, keywords);
            pager.setResult(templateList);
        } catch (Exception e) {
            log.error("查询彩信模板失败", e);
        }
        return pager;
    }

    /**
     * 保存或者更新视频彩信模板
     *
     * @return
     */
    @ApiOperation(value = "保存或者更新视频彩信模板")
    @RequestMapping(value = "/updateMmsTemplate", method = {RequestMethod.POST})
    public Map<String, Object> updateMmsTemplateLists(@RequestBody McdMmsTemplateHttpVo mmsTemplate) {

        Map<String, Object> result = new HashMap<>();
        String id = mmsTemplate.getId() == null ? "" : mmsTemplate.getId();
        String mmstaskNum = mmsTemplate.getMmsTemplateNum() == null ? "" : mmsTemplate.getMmsTemplateNum();
        String mmsTemplateSubject = mmsTemplate.getMmsTemplateSubject() == null ? "" : mmsTemplate.getMmsTemplateSubject();
        String mmsAddress = mmsTemplate.getMmsAddress() == null ? "" : mmsTemplate.getMmsAddress();
        String addOrUpdate = mmsTemplate.getFlag();
        boolean flag = true;
        try {
            log.info("更新视频彩信模板：id={}，模板编号 mmstaskNum={},模板主题 mmsTemplateSubject={}，彩信地址 mmsAddress={}", id, mmstaskNum, mmsTemplateSubject, mmsAddress);
            if ("add".equals(addOrUpdate)) {
                McdMmsTemplate template = new McdMmsTemplate();
                template.setId(mmstaskNum);
                template.setTemplateName(mmsTemplateSubject);
                template.setTemplateSubject(mmsTemplateSubject);
                template.setMmsAddress(mmsAddress);
                flag = mmsTemplatesService.saveMmsTemplate(template);
            } else {
                if (StringUtils.isEmpty(id)){
                    result.put("status", 201);
                    result.put("message","修改时彩信编号是必传参数!");
                    return result;
                }
                flag = mmsTemplatesService.updateMmsTemplates(id, mmstaskNum, mmsTemplateSubject, mmsAddress);
            }
            if (flag) {
                result.put("status", 200);
                result.put("message","新增或修改成功!");
            } else {
                result.put("status", 201);
                result.put("message","新增或修改失败!");
            }
        } catch (Exception ex) {
            log.error("更新营销模板(id={}，mmsTemplateSubject={},mmsAddress={} )异常，异常信息：\n{}", id, mmsTemplateSubject, mmsAddress, ex);
            result.put("status", 201);
            result.put("message","新增或修改异常!");
        }
        return result;
    }

    /**
     * 删除视频彩信模板
     * @return
     */
    @ApiOperation(value = "删除视频彩信模板")
    @RequestMapping(value = "/createTactics/deleteMmsTemplateLists",method = RequestMethod.POST)
    public Map<String, Object> deleteMmsTemplateLists(@RequestBody McdIdQuery mcdIdQuery) {

        Map<String, Object> result = new HashMap<>();
        String id = mcdIdQuery.getId() == null ? "" : mcdIdQuery.getId();
        boolean flag = true;
        try {
            log.debug("删除视频彩信模板：ids={}", id);
            int cnt = mmsTemplatesService.getMmsTemplateStatus(id);
            if (cnt > 0) {
                result.put("status", 202);
                log.error("该模板正在被其他营销活动使用，不能删除");
            } else {
                flag = mmsTemplatesService.deleteMmsTemplates(id);
                if (flag) {
                    result.put("status", 200);
                } else {
                    result.put("status", 201);
                }
            }
        } catch (Exception e) {
            log.error("删除视频彩信模板(id={})异常，异常信息：\n{}", id, e);
            result.put("status", 201);
        }
        return result;
    }

}
