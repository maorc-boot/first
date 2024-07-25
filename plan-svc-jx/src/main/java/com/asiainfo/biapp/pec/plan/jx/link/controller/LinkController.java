package com.asiainfo.biapp.pec.plan.jx.link.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.iopws.util.JsonUtil;
import com.asiainfo.biapp.pec.plan.jx.link.service.LinkService;
import com.asiainfo.biapp.pec.plan.jx.link.util.AESEncryptUtil;
import com.asiainfo.biapp.pec.plan.jx.link.util.SignatureUtil;
import com.asiainfo.biapp.pec.plan.jx.link.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RequestMapping("/action/link/linkManage")
@RestController
@Api(tags = "江西:链接库")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private HttpServletRequest request;

    @Value("${convertShortUrl.secretId}")
    private String toShortUrlSecretId;
    @Value("${convertShortUrl.secretKey}")
    private String toShortUrlSecretKey;
    @Value("${convertShortUrl.postUrl}")
    private String toShortUrlUrl;



    /**
     * 根据条件查询链接
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据条件查询链接")
    @RequestMapping(value = "/searchAllLink",method = {RequestMethod.POST})
    public Map<String, Object> searchAllLink(@RequestBody McdAllLink allLink) throws Exception {
        Map<String, Object> map = new HashMap();
        Map<String, Object> paramMap = new HashMap();
        try {
            final UserSimpleInfo user = UserUtil.getUser(request);
            String Id = allLink.getLinkId();
        String Name = allLink.getLinkName();
        String createDateS = allLink.getCreateDateS();
        String createDateE = allLink.getCreateDateE();
        String creator = allLink.getLinkCreator();
        String url = allLink.getLinkUrl();
        String planId = allLink.getPlanId();
        String planName = allLink.getPlanName();
        String pageNum = allLink.getPageNum();
        String pageSize = allLink.getPageSize();
        String aSwitch = allLink.getAswitch();
        paramMap.put("Id",Id);
        paramMap.put("Name",Name);
        paramMap.put("createDateS",createDateS);
        paramMap.put("createDateE",createDateE);
        paramMap.put("creator",creator);
        paramMap.put("url",url);
        paramMap.put("planId",planId);
        paramMap.put("planName",planName);
        paramMap.put("pageNum",pageNum);
        paramMap.put("pageSize",pageSize);
        paramMap.put("switch",aSwitch);
        paramMap.put("userId",user.getUserId());
        Map<String, Object> result = linkService.searchAllLink(paramMap);
            map.put("data",result);
            map.put("status","200");
            map.put("massage","流程处理成功");
        }catch (Exception e){
            log.error(e.toString());
            map.put("status","0");
            map.put("massage","流程出现错误");
        }
        return map;
    }

    /**
     * 保存或修改链接
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "保存或修改链接")
    @RequestMapping(value = "/saveOrUpdateLink", method = {RequestMethod.POST})
    public Map<String, Object> saveOrUpdateLink(@RequestBody McdAllLink allLink) throws Exception {
        Map<String, Object> map = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date();
        try {
            UserSimpleInfo user = UserUtil.getUser(request);
        String Id = allLink.getLinkId();
        String linkName = allLink.getLinkName();
        String linkUrl = allLink.getLinkUrl();
        String planId = allLink.getPlanId();
        String planName = allLink.getPlanName();
        String description = allLink.getLinkDescription();
        String creator = user.getUserId();
        String status = allLink.getStatus();
        if ("".equals(status)||status==null){
            status = "1";
        }
        Link link = new Link();
        if ("".equals(Id)||Id==null){
            Id = sdf.format(date);
            status = "0";
        }
        link.setId(Id);
        link.setName(linkName);
        link.setUrl(linkUrl);
        link.setPlanId(planId);
        link.setPlanName(planName);
        link.setDescription(description);
        link.setCreator(creator);
        link.setStatus(status);
        link.setCreateDate(String.valueOf(date.getTime()));
            status = linkService.saveOrUpdateLink(link);
            //status=1 新增或修改状态成功，status=2 均失败，status=3 修改链接成功
            if ("2".equals(status)){
                map.put("status",status);
                map.put("massage","新增或修改失败!");
            }else {
                map.put("status",status);
                map.put("massage","新增或修改成功!");
            }

        }catch (Exception e){
            map.put("status","0");
            map.put("massage","新增或修改异常!");
            log.error("新增或修改异常"+e.toString());
        }
        return map;
    }

    /**
     * 转短链接
     * @param originUrl
     * @param templateId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "转短链接")
    @RequestMapping(value = "/getShortUrl",method = {RequestMethod.POST})
    public Map<String, Object> getShortUrl(@RequestParam String originUrl,@RequestParam String templateId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String timeStamp = SignatureUtil.getTimeStamp();
        String randomStr = SignatureUtil.getRandomStr();
        String signature = SignatureUtil.createSignature(timeStamp,randomStr, toShortUrlSecretId);
        StringBuilder content = new StringBuilder();
        content.append("{\"srcUrl\":\"")
                .append(originUrl)
                .append("\",\"linkId\":")
                .append(templateId)
                .append("}");
        String aesEncrypt = AESEncryptUtil.aesEncrypt(content.toString(), toShortUrlSecretKey);
        map.put("timeStamp",timeStamp);
        map.put("nonce",randomStr);
        map.put("signature",signature);
        map.put("encrypt",aesEncrypt);
        map.put("secretId",toShortUrlSecretId);
        String jason = JsonUtil.objectToJsonStr(map);
        log.info("jason:"+jason);
        String url = toShortUrlUrl+"redirectUrl/convertShortUrl";
        String shortUrl = linkService.proxyPost(url, jason);
        log.info("entity:"+shortUrl);
        Map<String, Object> result = JSON.parseObject(shortUrl, Map.class);
        return result;
    }

    /**
     * 获取短链模板
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取短链接模板")
    @RequestMapping(value = "/getLinkId",method = {RequestMethod.POST})
    public ActionResponse getLinkId(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String toShortUrlLinkId = linkService.getToShortUrlLinkId();
        log.info("toShortUrlLinkId:"+toShortUrlLinkId);
        JSONObject object = JSON.parseObject(toShortUrlLinkId);

        return ActionResponse.getSuccessResp(object);
    }

    /**
     * 根据链接Id查询活动
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据链接URL查询活动")
    @RequestMapping(value = "/searchCampsByUrl",method = {RequestMethod.POST})
    public ActionResponse searchCampsByUrl(@RequestBody McdAllLink allLink) throws Exception {
        Map<String, Object> result = new HashMap<>();
        String linkUrl = allLink.getLinkUrl();
        String pageSize = allLink.getPageSize();
        String pageNum = allLink.getPageNum();
        try {
            result = linkService.searchCampsByUrl(linkUrl,pageSize,pageNum);
           return ActionResponse.getSuccessResp(result);
        }catch (Exception e){
            log.error("error:"+e);
           return ActionResponse.getFaildResp("根据链接URL查询活动失败");
        }

    }

    /**
     * 下载批量上传模板
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "下载批量上传模板")
    @RequestMapping(value = "/downloadLinkTemplate",method = {RequestMethod.GET})
    public Map<String, Object> downloadLinkTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("status","200");
            result.put("message","流程处理成功");
            linkService.downloadLinkTemplate(response);
        }catch (Exception e){
            log.error("error:"+e);
            result.put("status","0");
            result.put("message","流程处理失败");
        }
        return result;
    }

    /**
     * 批量导入素材列表，excel文件导入
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "批量导入素材列表")
    @ResponseBody
    @RequestMapping(value = "/uploadBatchLinkFiles",method = {RequestMethod.POST})
    public Respond uploadBatchLinkFiles(@RequestParam(value = "file", required = true) MultipartFile uploadBatchLinkFile,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        Respond respond = new Respond();
        if (uploadBatchLinkFile == null) {
            respond.setStatus(RespondStatus.FAILD.getValue());
            respond.setMessage("文件获取为空");
            return respond;
        }
        Map<String, String> map = new HashMap<>();
        try {
            UserSimpleInfo user = UserUtil.getUser(request);
            map = linkService.uploadBatchLinkFile(uploadBatchLinkFile,user);
            respond.setStatus(RespondStatus.SUCESS.getValue());
            respond.setMessage("调用成功");
            List<Map<String, String>> list = new ArrayList<>();
            list.add(map);
            respond.setData(list);
        } catch (Exception e) {
            log.error("导入内容异常：", e);
            respond.setStatus(RespondStatus.FAILD.getValue());
            respond.setMessage("调用失败:"+e.getMessage());
        }
        return respond;
    }


    /**
     * 删除链接
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "江西:删除短链接")
    @PostMapping(value = "/deleteLink" )
    public ActionResponse deleteLink(@RequestBody McdDeleteLinkQuery req) throws Exception {
        boolean flag = linkService.deleteLink(req);
        return ActionResponse.getSuccessResp(flag);
    }

}
