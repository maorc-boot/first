package com.asiainfo.biapp.pec.plan.jx.smstemplates.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdDimSmsBossTemplateType;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdSmsContentEditInfo;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.McdSmsTemplatesInfo;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.model.MtlChannelBossSmsTemplate;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.service.ISmsContentEditService;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.service.ISmsTemplatesService;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.util.CommonIdUtil;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsContentQuery;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsContentTemplateQuery;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsTemplatesQuery;
import com.asiainfo.biapp.pec.plan.jx.smstemplates.vo.McdSmsTemplatesRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "江西:短信夹带模板")
@RequestMapping("/api/action/smsTemplates")
@RestController
public class McdSmsTemplatesController {

    private static final String CHANNELID ="910";

    @Autowired
    ISmsTemplatesService smsTemplatesService;

    @Autowired
    ISmsContentEditService smsContentEditService;

    @Autowired
    HttpServletRequest  request;

    @ApiOperation(value = "江西:推荐用语编辑查询" ,notes = "推荐用语编辑查询")
    @PostMapping("/smsContentEdit")
    @ResponseBody
    public IPage<McdSmsContentEditInfo> getSmsContentEditList(@RequestBody McdSmsContentQuery contentQuery){

        return smsContentEditService.getSmsContentEditList(contentQuery);
    }

    @ApiOperation(value = "江西:营销用语模板查询" ,notes = "营销用语模板查询")
    @PostMapping("/getSmsContentTemplateList")
    @ResponseBody
    public IPage<McdSmsContentEditInfo> getSmsContentTemplateList(@RequestBody McdSmsContentTemplateQuery contentQuery){

        return smsContentEditService.getSmsContentTemplateList(contentQuery);
    }

    @ApiOperation(value = "江西:推荐用语编辑新增或编辑",notes = "推荐用语编辑新增或编辑")
    @PostMapping("/smsContentSaveOrUpdate")
    @ResponseBody
    public ActionResponse smsContentSaveOrUpdate(@RequestBody McdSmsContentEditInfo info){

         UserSimpleInfo user = UserUtil.getUser(request);
        if (StringUtils.isEmpty(info.getId())){
            info.setId(CommonIdUtil.generateId());
            info.setChannelId(CHANNELID);
            info.setCreateUser(user.getUserId());
        }
        smsContentEditService.saveOrUpdate(info);

        return ActionResponse.getSuccessResp();
    }


    @ApiOperation(value = "江西:推荐用语编辑删除",tags = "推荐用语编辑删除")
    @PostMapping("/smsContentDel")
    @ResponseBody
    public ActionResponse delSmsContentInfo(@RequestBody McdSmsContentQuery req){

        smsContentEditService.removeById(req.getId());

        return ActionResponse.getSuccessResp();
    }


    @ApiOperation(value = "江西:推荐用语编辑批量删除",tags = "推荐用语编辑批量删除")
    @PostMapping("/smsContentBatchDel")
    @ResponseBody
    public ActionResponse batchDelSmsContentInfo(@RequestBody McdSmsContentQuery req){

        smsContentEditService.removeByIds(req.getIds());

        return ActionResponse.getSuccessResp();
    }




    @ApiOperation(value = "江西:短信夹带管理查询" ,notes = "短信夹带管理查询")
    @PostMapping("/smsTemplates")
    @ResponseBody
    public IPage<McdSmsTemplatesInfo> getSmsTemplateList(@RequestBody McdSmsTemplatesQuery templatesQuery){

        return  smsTemplatesService.getSmsTemplateList(templatesQuery);
    }


    @ApiOperation(value = "江西:默认短信夹带编辑",notes = "默认短信夹带编辑")
    @PostMapping("/smsTemplateSaveOrUpdate")
    @ResponseBody
    public ActionResponse saveOrUpdate (@RequestBody MtlChannelBossSmsTemplate info){

        smsTemplatesService.updateById(info);

        return ActionResponse.getSuccessResp();
    }

    @ApiOperation(value = "江西:默认短信夹带下线及批量下线")
    @ResponseBody
    @PostMapping("/smsTemplateOffline")
    public ActionResponse smsTemplateOffline(@RequestBody McdSmsTemplatesQuery req){

        boolean flag = smsTemplatesService.smsTemplateOffline(req);
        if (flag){
            ActionResponse.getFaildResp("该模板正在被其他营销活动使用，不能下线");
        }
        return ActionResponse.getSuccessResp();
    }

    @ApiOperation(value = "江西:短信夹带渠道策划查询" ,notes = "短信夹带渠道策划查询")
    @PostMapping("/smsTemplateList")
    @ResponseBody
    public ActionResponse<List<McdDimSmsBossTemplateType>> querySmsTemplateList(@RequestBody McdSmsTemplatesRequest req){

        return  ActionResponse.getSuccessResp( smsTemplatesService.querySmsTemplateList(req) );
    }

}
