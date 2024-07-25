package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.OutInterface;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.common.RedisDicKey;
import com.asiainfo.biapp.pec.plan.jx.camp.req.GroupHallsAllDataJxQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.grouphalls.GroupHallsJxMaterialVo;
import com.asiainfo.biapp.pec.plan.jx.utils.HttpCommonUtil;
import com.asiainfo.biapp.pec.plan.model.echannel.ChangeStatusReq;
import com.asiainfo.biapp.pec.plan.model.echannel.HallVerVO;
import com.asiainfo.biapp.pec.plan.service.*;
import com.asiainfo.biapp.pec.plan.util.MD5Util;
import com.asiainfo.biapp.pec.plan.vo.DownloadMatUrlVo;
import com.asiainfo.biapp.pec.plan.vo.grouphalls.AdivBO;
import com.asiainfo.biapp.pec.plan.vo.req.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags = "企业微信渠道")
@Slf4j
@RestController
@RequestMapping("/api/grouphalls/jx")
public class GroupHallsJxController {

    @Value("#{'${grouphalls.security.fileType:zip}'.split(',')}")
    private Set<String> fileTypes;

    @Autowired
    private IMcdCampChannelExtService mcdCampChannelExtService;

    @Autowired
    private IGroupHallsService groupHallsService;
    @Resource
    private IGroupHallsJxService groupHallsJxService;

    @Autowired
    private IMcdCampsegService campsegService;

    @Autowired
    private IMcdPlanDefService planDefService;

    /**
     * 集团统一物料查询接口
     *
     * @param query 查询入参对象
     * @return 物料
     */
    @ApiOperation(value = "江西：集团统一物料查询接口", notes = "集团统一物料查询接口")
    @PostMapping(value = "/getAllDataView")
    public GroupHallsJxMaterialVo getAllDataView(@RequestBody GroupHallsAllDataJxQuery query) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //输入参数
        String digest = null;
        Map<String, Object> requestPara = new HashMap<>();
        Map<String, Object> header = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        long currentTimeStamp = System.currentTimeMillis();
        header.put("version", "1.0");
        header.put("timestamp", String.valueOf(currentTimeStamp));
        try {
            digest = Base64.encodeBase64String(MD5Util.encode(String.valueOf(currentTimeStamp) + "CM_201709").getBytes());
        } catch (Exception e) {
            log.error("集团统一物料查询加密失败",e);
        }
        String pageSize = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_PAGESIZE);
        String iopCode = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_IOPCODE);
        iopCode = StringUtils.isNotBlank(iopCode) ? iopCode : "2791";
        header.put("digest", digest);
        header.put("conversationId", formatter.format(currentTime) + "888888");
        data.put("iopCode", iopCode);
        data.put("searchKeys", query.getKeyWords());//搜索关键字  匹配营销活动物料名称；
        data.put("pageRecordNum", pageSize);//页面记录数
        data.put("currPageNo", query.getPageNum());//分页页码
        data.put("ifQueryTotalRecords", "0");//是否查询总记录数：0-查询；1-不查询
        requestPara.put("serviceName", "mat_unify_query");
        requestPara.put("header", header);
        requestPara.put("data", data);
        HttpEntity<Map<String, Object>> requestParam = new HttpEntity<>(requestPara, httpHeaders);

        GroupHallsJxMaterialVo resultDate = new GroupHallsJxMaterialVo();
        try {
            String httpUrl = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_MATERIEL_URL);
            log.info("调用地址：" + httpUrl + ",调用参数：" + requestParam);

            //现网真实数据
            String requestPost = "{\"result\":{\"totalRecords\":7,\"recordList\":[{\"matUrl\":\"http://172.17.70.222/pomp/service/previewImage.do?target=/uploadBaseDir/content/jpg/20180828/201808280934082693dM.jpg\",\"cardInfo\":{\"cardType\":\"\",\"cardC\":\"\",\"cardActId\":\"\",\"cardP\":\"\"},\"ifJb\":\"1\",\"matMarkDesc\":\"不限量10元乐享包\",\"jbPicSrc\":\"\",\"jbText\":\"\",\"generalInfo\":{\"generalTextName\":\"\",\"generalTextValue\":\"\",\"generalImageName\":\"\",\"generalImageSrc\":\"\",\"generalImageLink\":\"\",\"generalImageAdFlag\":\"\"},\"matType\":\"流量类\",\"matActionUrl\":\"http://service.bj.10086.cn/m/activity/manager/showIndex.action?activityName=bxllx10&channelName=jtApp\",\"schId\":null,\"searchInfo\":{\"searchType\":\"\",\"searchKey\":\"\",\"searchImgName\":\"\"},\"ugInfo\":{\"ugType\":\"\",\"targetUserGroup\":\"\",\"ugRule\":\"\"},\"matId\":\"YX0000000010131052018083000321-08\",\"matTime\":\"2018-08-30 09:46:55\",\"jbShow\":\"\",\"jbType\":\"\",\"matActionType\":\"1\",\"ext3\":\"\",\"ext2\":\"\",\"matName\":\"物料-手厅窗帘0830乐享10元\",\"ext1\":\"\"},{\"matUrl\":\"http://172.17.70.222/pomp/service/previewImage.do?target=/uploadBaseDir/content/jpg/20180828/20180828093550160qdz.jpg\",\"cardInfo\":{\"cardType\":\"\",\"cardC\":\"\",\"cardActId\":\"\",\"cardP\":\"\"},\"ifJb\":\"1\",\"matMarkDesc\":\"不限量20元乐享包\",\"jbPicSrc\":\"\",\"jbText\":\"\",\"generalInfo\":{\"generalTextName\":\"\",\"generalTextValue\":\"\",\"generalImageName\":\"\",\"generalImageSrc\":\"\",\"generalImageLink\":\"\",\"generalImageAdFlag\":\"\"},\"matType\":\"流量类\",\"matActionUrl\":\"http://service.bj.10086.cn/m/activity/manager/showIndex.action?activityName=bxllx20&channelName=jtApp\",\"schId\":null,\"searchInfo\":{\"searchType\":\"\",\"searchKey\":\"\",\"searchImgName\":\"\"},\"ugInfo\":{\"ugType\":\"\",\"targetUserGroup\":\"\",\"ugRule\":\"\"},\"matId\":\"YX0000000010131052018083000322-08\",\"matTime\":\"2018-08-30 09:47:32\",\"jbShow\":\"\",\"jbType\":\"\",\"matActionType\":\"1\",\"ext3\":\"\",\"ext2\":\"\",\"matName\":\"物料-手厅窗帘0830乐享20元\",\"ext1\":\"\"},{\"matUrl\":\"http://172.17.70.222/pomp/service/previewImage.do?target=/uploadBaseDir/content/jpg/20220829/group1@M00@00@30@CuaJA2MMcF6AGaVfAAKfyMRWAjY815.jpg\",\"cardInfo\":{\"cardType\":\"\",\"cardC\":\"\",\"cardActId\":\"\",\"cardP\":\"\"},\"ifJb\":\"1\",\"matMarkDesc\":\"\",\"jbPicSrc\":\"\",\"jbText\":\"\",\"generalInfo\":{\"generalTextName\":\"\",\"generalTextValue\":\"\",\"generalImageName\":\"\",\"generalImageSrc\":\"\",\"generalImageLink\":\"\",\"generalImageAdFlag\":\"\"},\"matType\":\"终端类\",\"matActionUrl\":\"https://service.bj.10086.cn/m/activity/manager/showIndex.action?activityName=d5qHrqW2&channelName=jtApp\",\"schId\":\"\",\"searchInfo\":{\"searchType\":\"\",\"searchKey\":\"\",\"searchImgName\":\"\"},\"ugInfo\":{\"ugType\":\"\",\"targetUserGroup\":\"\",\"ugRule\":\"\"},\"matId\":\"YX0000000010131052022082908916-08\",\"matTime\":\"2022-08-29 15:53:09\",\"jbShow\":\"\",\"jbType\":\"\",\"matActionType\":\"1\",\"ext3\":\"\",\"ext2\":\"\",\"matName\":\"弹窗-亲情网\",\"ext1\":\"\"},{\"matUrl\":\"http://172.17.70.222/pomp/service/previewImage.do?target=/uploadBaseDir/content/jpg/20220901/group1@M00@00@31@CuaJA2MQYpuATsjWAAPy3xqpoXE196.jpg\",\"cardInfo\":{\"cardType\":\"\",\"cardC\":\"\",\"cardActId\":\"\",\"cardP\":\"\"},\"ifJb\":\"1\",\"matMarkDesc\":\"\",\"jbPicSrc\":\"\",\"jbText\":\"\",\"generalInfo\":{\"generalTextName\":\"\",\"generalTextValue\":\"\",\"generalImageName\":\"\",\"generalImageSrc\":\"\",\"generalImageLink\":\"\",\"generalImageAdFlag\":\"\"},\"matType\":\"5G产品类\",\"matActionUrl\":\"https://service.bj.10086.cn/m/activity/manager/showIndex.action?activityName=bb1000sp3z&channelName=jtApp\",\"schId\":\"\",\"searchInfo\":{\"searchType\":\"\",\"searchKey\":\"\",\"searchImgName\":\"\"},\"ugInfo\":{\"ugType\":\"\",\"targetUserGroup\":\"\",\"ugRule\":\"\"},\"matId\":\"YX0000000010131052022090108938-08\",\"matTime\":\"2022-09-01 15:43:48\",\"jbShow\":\"\",\"jbType\":\"\",\"matActionType\":\"1\",\"ext3\":\"\",\"ext2\":\"\",\"matName\":\"弹窗-宽带提速包\",\"ext1\":\"\"} ,{\"matUrl\":\"http://172.17.70.222/pomp/service/previewImage.do?target=/uploadBaseDir/content/gif/20220208/group1@M00@00@0B@CuaJA2ICNeyAHWVPAABHlGVch-4583.gif\",\"cardInfo\":{\"cardType\":\"\",\"cardC\":\"\",\"cardActId\":\"\",\"cardP\":\"\"},\"ifJb\":\"1\",\"matMarkDesc\":\"\",\"jbPicSrc\":\"\",\"jbText\":\"\",\"generalInfo\":{\"generalTextName\":\"\",\"generalTextValue\":\"\",\"generalImageName\":\"\",\"generalImageSrc\":\"\",\"generalImageLink\":\"\",\"generalImageAdFlag\":\"\"},\"matType\":\"流量类\",\"matActionUrl\":\"https://service.bj.10086.cn/m/cnxfsll/showToFlowAndPrivilege.action?channelName=jtApp\",\"schId\":null,\"searchInfo\":{\"searchType\":\"\",\"searchKey\":\"\",\"searchImgName\":\"\"},\"ugInfo\":{\"ugType\":\"\",\"targetUserGroup\":\"\",\"ugRule\":\"\"},\"matId\":\"YX0000000010131052022090809030-08\",\"matTime\":\"2022-09-08 17:21:42\",\"jbShow\":\"\",\"jbType\":\"\",\"matActionType\":\"1\",\"ext3\":\"\",\"ext2\":\"\",\"matName\":\"悬浮窗-超惠享特权9.8\",\"ext1\":\"\"},{\"matUrl\":\"http://172.17.70.222/pomp/service/previewImage.do?target=/uploadBaseDir/content/jpg/20221104/group1@M00@00@3E@CuaJA2Nktp2AUrFxAARGaFm9QtM141.jpg\",\"cardInfo\":{\"cardType\":\"0\",\"cardC\":\"\",\"cardActId\":\"\",\"cardP\":\"\"},\"ifJb\":\"1\",\"matMarkDesc\":\"\",\"jbPicSrc\":\"\",\"jbText\":\"\",\"generalInfo\":{\"generalTextName\":\"\",\"generalTextValue\":\"\",\"generalImageName\":\"\",\"generalImageSrc\":\"\",\"generalImageLink\":\"\",\"generalImageAdFlag\":\"\"},\"matType\":\"5G产品类\",\"matActionUrl\":\"https://service.bj.10086.cn/m/activity/manager/showIndex.action?activityName=winterWarmthHalfYearPack&channelName=jtApp\",\"schId\":null,\"searchInfo\":{\"searchType\":\"\",\"searchKey\":\"\",\"searchImgName\":\"\"},\"ugInfo\":{\"ugType\":\"\",\"targetUserGroup\":\"\",\"ugRule\":\"\"},\"matId\":\"YX0000000010131052022110409302-08\",\"matTime\":\"2022-11-04 14:52:38\",\"jbShow\":\"\",\"jbType\":\"\",\"matActionType\":\"1\",\"ext3\":\"\",\"ext2\":\"\",\"matName\":\"弹窗-暖心半年包11.4\",\"ext1\":\"\"},{\"matUrl\":\"http://172.17.70.222/pomp/service/previewImage.do?target=/uploadBaseDir/content/jpg/20221111/group1@M00@00@41@CuZjhGNuD8-ABNBoAAALXLngLeA664.jpg\",\"cardInfo\":{\"cardType\":\"0\",\"cardC\":\"\",\"cardActId\":\"\",\"cardP\":\"\"},\"ifJb\":\"0\",\"matMarkDesc\":\"北京搜索1\",\"jbPicSrc\":\"\",\"jbText\":\"1\",\"generalInfo\":{\"generalTextName\":\"文本1\",\"generalTextValue\":\"北京搜索1\",\"generalImageName\":\"图片1\",\"generalImageSrc\":\"/uploadBaseDir/content/jpg/20221111/group1@M00@00@41@CuZjhGNuD8-ABNBoAAALXLngLeA664.jpg\",\"generalImageLink\":\"http://www.10086.cn/2\",\"generalImageAdFlag\":\"1\"},\"matType\":\"5G产品类\",\"matActionUrl\":\"http://www.10086.cn/1\",\"schId\":\"\",\"searchInfo\":{\"searchType\":\"01\",\"searchKey\":\"北京搜索1\",\"searchImgName\":\"http://172.17.70.222/pomp/service/previewImage.do?target=/uploadBaseDir/ad/jpg/20221111/group1@M00@00@41@CuaJA2NuD6WAMhCDAAAJB0ftFDg756.jpg\"},\"ugInfo\":{\"ugType\":\"1\",\"targetUserGroup\":\"1\",\"ugRule\":\"1\"},\"matId\":\"YX0000000010131052022111109370-08\",\"matTime\":\"2022-11-11 17:03:17\",\"jbShow\":\"0\",\"jbType\":\"1\",\"matActionType\":\"12\",\"ext3\":\"1\",\"ext2\":\"\",\"matName\":\"北京搜索1\",\"ext1\":\"\"}],\"conversationId\":\"20221111183801888888\",\"responseCode\":\"0000\"}}" ;

            if ("0".equals(RedisUtils.getDicValue("MCD_IS_PRODUCTION_ENVIRONMENT"))){
                log.info("调用地址：" + httpUrl + ",调用参数：" + com.alibaba.fastjson.JSONObject.toJSONString(requestPara));
                requestPost  = HttpCommonUtil.sendHttpJsonPost(httpUrl, com.alibaba.fastjson.JSONObject.toJSONString(requestPara));
            };

            com.alibaba.fastjson.JSONObject result = com.alibaba.fastjson.JSONObject.parseObject(requestPost);
            com.alibaba.fastjson.JSONObject json = result.getJSONObject("result");
            String totalRecords=json.getString("totalRecords");

            log.info("**********返回result内容转化为json：" + json);
            if ("0000".equals(json.getString("responseCode"))) {
                resultDate = com.alibaba.fastjson.JSONObject.toJavaObject(json,GroupHallsJxMaterialVo.class);
                resultDate.setTotalRecords(totalRecords);
            } else {
                resultDate.setResponseCode(json.getString("responseCode"));
                resultDate.setTotalRecords(totalRecords);
            }
        } catch (Exception e) {
            log.error("mobileAppService.getAllDataView失败", e);
        }

        log.info("resultDate:" + resultDate);
        return resultDate;
    }

    /**
     * 集团手厅：校验策略弹窗id
     *
     * @param query 校验入参对象
     * @return 校验结果
     */
    @ApiOperation(value = "集团手厅：校验策略弹窗id", notes = "")
    @PostMapping(value = "validChannelAdvidIdAvailable")
    public ActionResponse validChannelAdvidIdAvailable(@RequestBody ValidMaterialQuery query) {
        try {
            if(StringUtils.isBlank(query.getChannelAdivdId())){
                return ActionResponse.getFaildResp("策略弹窗id不能为空");
            }
            Boolean aBoolean = mcdCampChannelExtService.validMaterial(query.getChannelId(), query.getChannelAdivdId(), query.getMatUrl(), query.getCampsegId());
            if(aBoolean){
                ActionResponse resp = ActionResponse.getSuccessResp("校验成功");
                resp.setData(true);
                return resp;
            }else{
                ActionResponse resp = ActionResponse.getSuccessResp("策略弹窗不可用");
                resp.setData(false);
                return resp;
            }
        } catch (Exception e) {
            log.error("【{}】校验策略弹窗是否可用异常",query.getChannelAdivdId(),e);
            return ActionResponse.getFaildResp("校验策略弹窗是否可用异常");
        }
    }

    @ApiOperation(value = "集团手厅：下载素材", notes = "集团手厅：下载素材")
    @PostMapping(value = "downLoadMatUrl")
    public DownloadMatUrlVo downLoadMatUrl(@RequestBody DownLoadMatUrlQuery query) {
        DownloadMatUrlVo result = new DownloadMatUrlVo();
        if(StringUtils.isBlank(query.getMatUrl())){
            log.error("素材链接url为空");
            result.setStatus("201");
            return result;
        }
        String url = query.getMatUrl();
        String picName = url.substring(url.lastIndexOf("/") + 1);
        try {
            String status = downloadFromRemote(picName, url);
            result.setStatus(status);
            result.setMatUrl(picName);
            return result;
        } catch (Exception e) {
            log.error("【{}】集团手厅下载素材失败",url,e);
            result.setStatus("201");
            return result;
        }
    }

    private String downloadFromRemote(String picName, String picUrl) {
        String downloadLoaclpath = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_MATERIEL_DOWNLOAD_LOCALPATH);
        File dir = new File(downloadLoaclpath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String downPath = downloadLoaclpath + picName;
        log.info("下载图片："+picUrl);
        return groupHallsService.downloadPicture(picUrl, downPath);

    }

    /**
     * 读取一个图片文件输入到前端
     * @throws Exception
     */
    @ApiOperation(value = "集团手厅：读取一个图片文件输入到前端", notes = "")
    @PostMapping("/loadImage")
    public void loadImage(HttpServletRequest request, HttpServletResponse response, @RequestBody LoadImageQuery query) throws Exception {
        BufferedOutputStream bout = null;
        String picName = query.getPicName();
        try {
            String filePath = null;
            File dest;
            if(picName.contains("/")) {//传递URL全路径的时候，如果本地没有图片还可以从远程下载
                String picName2 = picName.substring(picName.lastIndexOf("/") + 1);
                filePath = getFilePath(request,picName2);
                dest = new File(filePath);
                if(!dest.exists()) {
                    downloadFromRemote(picName2,picName);
                }
            }else {//传递文件名的时候
                filePath = getFilePath(request,picName);
            }
            dest = new File(filePath);
            log.info("start loadImage：{}", filePath);
            FileInputStream inputImage = new FileInputStream(dest);
            bout = new BufferedOutputStream(response.getOutputStream());
            IOUtils.copy(inputImage, bout);
            bout.flush();
        } catch (Exception e) {
            log.error("error on loadImage!", e);
        } finally {
            if(bout != null);
            bout.close();
        }
    }

    /**
     * 获取图片路径
     * @param request
     * @param lastFileName 文件名
     * @return 文件全路径
     */
    private String getFilePath(HttpServletRequest request,String lastFileName) {
        String downloadLoaclpath = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_MATERIEL_DOWNLOAD_LOCALPATH);
        return downloadLoaclpath + lastFileName;
    }

    /**
     * 集团手厅：活动信息同步接口
     *
     * @param request
     * @param response
     * @return 1:成功，0：失败
     */
    @ApiOperation(value = "集团手厅：活动信息同步接口", notes = "")
    @PostMapping(value = "sendCampsegInfoById")
    public int sendCampsegInfoById(HttpServletRequest request, HttpServletResponse response) {
        String campsegPid = request.getParameter("campsegPid");
        try {
            groupHallsJxService.sendCampsegInfoById(campsegPid);
            return 1;
        } catch (Exception e) {
            log.error("一级电渠活动信息campsegPid=" + campsegPid + "同步异常:", e);
            return 0;
        }
    }

    /**
     * 集团手厅：运营位位列表查询
     *
     * @param query
     * @return
     */
    @PostMapping("/queryChannelAdivInfo")
    @ApiOperation(value = "集团手厅：运营位位列表查询", notes = "")
    public ActionResponse<List<AdivBO>> queryChannelAdivInfoList(@RequestBody ChannelAdivInfoQuery query) {

        ActionResponse<List<AdivBO>> result = new ActionResponse<List<AdivBO>>();
        String channelId =query.getChannelId();
        log.debug("查询{}下的运营位信息 ......渠道：" + channelId);
        List<AdivBO> list = new ArrayList<>();
        try {
            list = this.groupHallsService.queryChannelAdivInfoList(channelId);
            result.setStatus(ResponseStatus.SUCCESS);
            result.setData(list);
        } catch (Exception ex) {
            log.error("查询{}下的运营位时出现异常，异常信息如下：\n{}", StringUtils.isEmpty(channelId) ? "所有渠道" : ("渠道：" + channelId), ex);
            result.setStatus(ResponseStatus.ERROR);
        }
        log.debug("查询到{}条运营位记录 ......", list.size());
        return result;

    }

    /**
     * 集团手厅-审核接口
     *
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping(value = "/getApproveFeedback")
    @ApiOperation(value = "集团手厅-审核接口", notes = "")
    @OutInterface
    public JSONObject getApproveFeedback(HttpServletRequest request, HttpServletResponse response) {
        log.info("**********一级手厅审批回调开始***********");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        JSONObject jsonObject;
        JSONObject jsonResult = null;
        JSONObject headerJson;
        JSONObject dataJson;
        String interfaceName = "";
        String conversationId = "";
        String iopCode = "";
        String activityId = "";
        String approveStatus = "";
        String description = "";
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            jsonObject = JSONUtil.parseObj(sb.toString());
            interfaceName = jsonObject.getStr("interfaceName");
            headerJson = (JSONObject) jsonObject.get("header");
            dataJson = (JSONObject) jsonObject.get("data");

            log.info("getApproveFeedback 审批回调入参: " + dataJson.toString());
            conversationId = headerJson.getStr("conversationId") == null ? ""
                    : headerJson.getStr("conversationId");
            iopCode = dataJson.getStr("iopCode") == null ? "" : dataJson.getStr("iopCode");// 2471营销系统编码渠道运营位编码
            activityId = dataJson.getStr("activityId") == null ? "" : dataJson.getStr("activityId");// 子活动ID
            if (activityId.length() > 4) {
                activityId = activityId.substring(4);
            }
            approveStatus = dataJson.getStr("approveStatus") == null ? "" : dataJson.getStr("approveStatus");// 处理状态：1：成功办理；0：其他（异常办理）
            description = dataJson.getStr("description") == null ? "" : dataJson.getStr("description");// 手机号码

            log.info("iopCode:" + iopCode + ",activityId:" + activityId + ",approveStatus:" + approveStatus
                    + ",description:" + description);
            if ("activityFeedbackService".equals(interfaceName)) {
                // activityId=2100 + 营销活动编码，需要截取为mcd的活动id
                // approveStatus 1：审批通过 0：驳回，需要转换为mcd的审批结果
                // description 审批意见
                //以上调用审批组件时候进行转换
                String JsonStr = "{\"activityId\":\"" + activityId + "\",\"iopCode\":\"" + iopCode
                        + "\",\"approveStatus\":\"" + approveStatus + "\",\"description\":\"" + description + "\"}";
                log.info("接口参数:" + JsonStr);
                Map<String, Object> mapForParam = new HashMap<String, Object>();
                mapForParam.put("param", JsonStr);
                Map<String, String> resultMap = null;
                CampStatusQuery req = new CampStatusQuery();
                req.setCampsegRootId(activityId);
                int campsegStatId = 0;
                if("0".equals(approveStatus)){
                    campsegStatId = 41;
                }else if("1".equals(approveStatus)){
                    campsegStatId = 52;
                }else {
                    map.put("message", "审批结果异常");
                    map.put("responseCode", "9997");
                    map.put("conversationId", conversationId);
                    map1.put("result", map);
                    jsonResult = new JSONObject(map1);
                    return jsonResult;
                }
                req.setCampStat(campsegStatId);
                req.setSource(1);
                campsegService.modifyCampStatusFromAppr(req);
                map.put("message", "操作成功");
                map.put("responseCode", "0000");
                map.put("conversationId", conversationId);
            }
        } catch (Exception e) {
            log.error("一级手厅审批回调省端接口异常", e);
            map.put("message", "操作失败，失败原因：" + e);
            map.put("responseCode", "9997");
        } finally {
            if (!"activityFeedbackService".equals(interfaceName)) {
                map.put("message", "操作失败，失败原因：接口名称错误，应为：activityFeedbackService，实际为：" + interfaceName);
                map.put("responseCode", "9999");
                map.put("conversationId", conversationId);
            }
            map1.put("result", map);
            jsonResult = new JSONObject(map1);
        }
        log.info("一级手厅审批回调结束返回结果集result：" + jsonResult);
        return jsonResult;
    }

    /**
     * R60001分页查询一级手厅版本信息
     */
    @PostMapping("pageAppVersion")
    @ApiOperation(value = "集团手厅-查询一级手厅版本信息接口", notes = "不分页")
    public List<HallVerVO> pageAppVersion(@RequestBody McdPageQuery query) {
        List<HallVerVO> page = groupHallsService.pageAppVersion(query);
        return page;
    }
    /**
     * 查询一级手厅版本信息接口
     */
    @PostMapping("queryAppVersion")
    @ApiOperation(value = "集团手厅-查询一级手厅版本信息接口", notes = "")
    public Object queryAppVersion() {
        Map<String, Object> requestParam = new HashMap<String, Object>();
        requestParam.put("serviceName", "chan1000003_ver_query");
        requestParam.put("header", getParamHeader());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("iopCode", "1000003");
        requestParam.put("data", data);
        log.info("请求app版本号接口参数：{}", requestParam);
        String url = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_QUERY_APP_VERSION_URL);
        log.debug("一级手厅版本信息接口地址:{}", url);
        ResponseEntity<String> result = sendPostReqestByJson(url, requestParam);
        String response = result.getBody();
        log.info(response);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        // responseCode=0000 成功
        if (!"0000".equals(jsonObject.getStr("responseCode"))) {
            Map<String, Object> resultMap = new HashMap<>();
            // 9999 默认为未知错误
            resultMap.put("responseCode", jsonObject.getStr("responseCode") == null ? "9999" : jsonObject.getStr("responseCode"));
            resultMap.put("message", jsonObject.getStr("response_desc") == null ? "" : jsonObject.getStr("response_desc"));
        }
        return jsonObject;
    }
    /**
     * R10002营销活动状态变更接口
     */
    @PostMapping("SynchronousChangeStatus")
    @ApiOperation(value = "集团手厅-营销活动状态变更接口", notes = "")
    @OutInterface
    public Object SynchronousChangeStatus(@RequestBody ChangeStatusReq query) {
        Map<String, Object> requestParam = new HashMap<String, Object>();
        requestParam.put("serviceName", "iop_activity_status_change");
        Map<String, Object> paramHeader = getParamHeader();
        requestParam.put("header", paramHeader);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("activityId", query.getActivityId());
        data.put("activityStatus", query.getActivityStatus());
        requestParam.put("data", data);
        JSONObject paramJson = new JSONObject(requestParam);
        log.info("R10002营销活动状态变更接口参数：{}", paramJson);
        String url = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_SYNC_CHANGE_STATUS_URL);
        log.debug("R10002营销活动状态变更接口地址:{}", url);
        ResponseEntity<String> result = sendPostReqestByJson(url, paramJson);
        String response = result.getBody();
        log.info(response);
        JSONObject responseObject = JSONUtil.parseObj(response);
        // responseCode=0000 成功
        String resultData = responseObject.getStr("result");
        JSONObject resultObject = JSONUtil.parseObj(resultData);
        String conversationId = resultObject.getStr("conversationId");
        if (!"0000".equals(resultObject.getStr("responseCode"))) {
            Map<String, Object> resultMap = new HashMap<>();
            // 9999 默认为未知错误
            resultMap.put("responseCode", resultObject.getStr("responseCode") == null ? "9999" : resultObject.getStr("responseCode"));
        }
        return resultObject;
    }
    /**
     * R20004渠道发布状态反馈接口
     */
    @PostMapping("activityStatusFeedback")
    @ApiOperation(value = "集团手厅-营销活动状态变更接口", notes = "")
    @OutInterface
    public JSONObject ActivityStatusFeedback(HttpServletRequest request, HttpServletResponse response) {
        log.info("**********营销活动状态变更开始***********");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        JSONObject jsonObject;
        JSONObject jsonResult = null;
        JSONObject headerJson;
        JSONObject dataJson;
        String serviceName = "";
        String conversationId = "";
        String iopCode = "";
        String activityId = "";
        String approveStatus = "";
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            jsonObject = JSONUtil.parseObj(sb.toString());
            serviceName = jsonObject.getStr("serviceName");
            headerJson = (JSONObject) jsonObject.get("header");
            dataJson = (JSONObject) jsonObject.get("data");

            conversationId = headerJson.getStr("conversationId") == null ? ""
                    : headerJson.getStr("conversationId");//会话ID
            iopCode = dataJson.getStr("iopCode") == null ? "" : dataJson.getStr("iopCode");// 2471营销系统编码渠道运营位编码
            activityId = dataJson.getStr("activityId") == null ? "" : dataJson.getStr("activityId");// 子活动ID
            if (activityId.length() > 4) {
                activityId = activityId.substring(4);
            }
            approveStatus = dataJson.getStr("approveStatus") == null ? "" : dataJson.getStr("approveStatus");// 处理状态：1：成功办理；0：其他（异常办理）

            log.info("iopCode:" + iopCode + ",activityId:" + activityId + ",approveStatus:" + approveStatus);
            if ("iop_activity_publish_status_feedback".equals(serviceName)) {
                // activityId=2100 + 营销活动编码，需要截取为mcd的活动id
                // approveStatus 1：审批通过 0：驳回，需要转换为mcd的审批结果
                // description 审批意见
                //以上调用审批组件时候进行转换
                String JsonStr = "{\"activityId\":\"" + activityId + "\",\"iopCode\":\"" + iopCode
                        + "\",\"approveStatus\":\"" + approveStatus + "\"}";
                log.info("接口参数:" + JsonStr);

                Map<String, Object> mapForParam = new HashMap<String, Object>();
                mapForParam.put("param", JsonStr);
                Map<String, String> resultMap = null;
                CampStatusQuery req = new CampStatusQuery();
                req.setCampsegRootId(activityId);
                int campsegStatId = 0;
                /*营销活动发布状态
                1：待上线；52
                2：已上线；54
                3：待下线；90
                4：已下线；91*/
                if("1".equals(approveStatus)){
                    campsegStatId = 52;
                }else if("2".equals(approveStatus)){
                    campsegStatId = 54;
                }else if("3".equals(approveStatus)){
                    campsegStatId = 90;
                }else if("4".equals(approveStatus)){
                    campsegStatId = 91;
                }else {
                    map.put("message", "入参营销活动发布状态异常");
                    map.put("responseCode", "9998");
                    map.put("conversationId", conversationId);
                    map1.put("result", map);
                    jsonResult = new JSONObject(map1);
                    return jsonResult;
                }
                req.setCampStat(campsegStatId);
                req.setSource(1);
                campsegService.modifyCampStatusFromAppr(req);
                map.put("message", "操作成功");
                map.put("responseCode", "0000");
                map.put("conversationId", conversationId);
            }
        } catch (Exception e) {
            log.error("一级手厅审批回调省端接口异常", e);
            map.put("message", "操作失败，失败原因：" + e);
            map.put("responseCode", "9997");
        } finally {
            if (!"iop_activity_publish_status_feedback".equals(serviceName)) {
                map.put("message", "操作失败，失败原因：接口名称错误，应为：iop_activity_publish_status_feedback，实际为：" + serviceName);
                map.put("responseCode", "9999");
                map.put("conversationId", conversationId);
            }
            map1.put("result", map);
            jsonResult = new JSONObject(map1);
        }
        log.info("一级手厅审批回调结束返回结果集result：" + jsonResult);
        return jsonResult;
    }
    /**
     * 消息单推接口
     */
    @PostMapping("iopPushMsg")
    public Object iopPushMsg(HttpServletRequest request) {
        String requestDataJson = request.getParameter("data");
        log.info("iopPushMsg 接收到请求参数：{}", requestDataJson);
        Map<String, Object> requestDataMap = getRequestParamData(requestDataJson);
        if (requestDataMap == null) {
            log.error("iopPushMsg接收到请求参数异常:{}", requestDataJson);
            return dataErrorMessage(requestDataJson);
        }
        Map<String, Object> requestParam = new HashMap<String, Object>();
        requestParam.put("serviceName", "iop_push_msg");
        requestParam.put("header", getParamHeader());
        requestParam.put("data", requestDataMap);
        log.info("消息单推接口请求参数:{}", requestParam);
        String url = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_MSG_PUSH_URL);
        log.debug("消息单推接口请求url:", url);
        ResponseEntity<String> result = sendPostReqestByJson(url, requestParam);
        String response = result.getBody();
        log.info("消息单推接口返回结果:{}", response);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
        if ("-1".equals(jsonObjectResult.getStr("responseCode"))) {
            return responseErrorMsg(jsonObjectResult);
        }
        return jsonObject;
    }


    /**
     * 消息群推接口
     *
     * @param request
     * @return
     */
    @PostMapping("iopPushFileMsg")
    public Object iopPushFileMsg(HttpServletRequest request) {
        String requestDataJson = request.getParameter("data");
        log.info("iopPushFileMsg 接收到请求参数：{}", requestDataJson);
        Map<String, Object> requestDataMap = getRequestParamData(requestDataJson);
        if (requestDataMap == null) {
            log.error("iopPushFileMsg-接收到请求参数异常:{}", requestDataJson);
            return dataErrorMessage(requestDataJson);
        }
        Map<String, Object> requestParam = new HashMap<String, Object>();
        requestParam.put("serviceName", "iop_push_file_msg");
        requestParam.put("header", getParamHeader());
        requestParam.put("data", requestDataMap);
        log.info("消息群推接口请求参数:{}", requestParam);
        String url = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_MSG_PUSH_FILE_URL);
        log.debug("消息群推接口地址:{}", url);
        ResponseEntity<String> result = sendPostReqestByJson(url, requestParam);
        String response = result.getBody();
        log.info("消息群推接口返回结果:{}", response);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
        if ("-1".equals(jsonObjectResult.getStr("responseCode"))) {
            return responseErrorMsg(jsonObjectResult);
        }
        return jsonObject;
    }

    /**
     * 推送取消接口
     *
     * @param request
     * @return 返回结果
     */
    @PostMapping("iopTaskCancel")
    public Object iopTaskCancel(HttpServletRequest request) {
        String requestDataJson = request.getParameter("data");
        log.info("iopTaskCancel 接收到请求参数：{}", requestDataJson);
        Map<String, Object> requestDataMap = getRequestParamData(requestDataJson);
        if (requestDataMap == null) {
            log.error("iopTaskCancel-接收到请求参数异常:{}", requestDataJson);
            return dataErrorMessage(requestDataJson);
        }
        Map<String, Object> requestParam = new HashMap<String, Object>();
        requestParam.put("serviceName", "iop_task_cancel");
        requestParam.put("header", getParamHeader());
        requestParam.put("data", requestDataMap);
        log.info("推送取消接口请求参数:{}", requestParam);
        String url = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_MSG_PUSH_CANCEL_URL);
        log.debug("推送取消接口地址:{}", url);
        ResponseEntity<String> result = sendPostReqestByJson(url, requestParam);
        String response = result.getBody();
        log.info("推送取消接口返回结果:{}", response);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
        if ("-1".equals(jsonObjectResult.getStr("responseCode"))) {
            return responseErrorMsg(jsonObjectResult);
        }
        return jsonObject;
    }


    /**
     * 短信单推接口
     *
     * @param request HttpServletRequest
     * @return 返回结果
     */
    @PostMapping("iopPushSmsMsg")
    public Object iopPushSmsMsg(HttpServletRequest request) {
        String requestDataJson = request.getParameter("data");
        log.info("iopPushSmsMsg 接收到请求参数：{}", requestDataJson);
        Map<String, Object> requestDataMap = getRequestParamData(requestDataJson);
        if (requestDataMap == null) {
            log.error("iopPushSmsMsg-接收到请求参数异常:{}", requestDataJson);
            return dataErrorMessage(requestDataJson);
        }
        Map<String, Object> requestParam = new HashMap<String, Object>();
        requestParam.put("serviceName", "iop_push_sms_msg");
        requestParam.put("header", getParamHeader());
        requestParam.put("data", requestDataMap);
        log.info("短信单推接口请求参数:{}", requestParam);
        String url = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_SMS_PUSH_URL);
        log.debug("短信单推接口地址:{}", url);
        ResponseEntity<String> result = sendPostReqestByJson(url, requestParam);
        String response = result.getBody();
        log.info("短信单推接口返回结果:{}", response);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
        if ("-1".equals(jsonObjectResult.getStr("responseCode"))) {
            return responseErrorMsg(jsonObjectResult);
        }
        return jsonObject;
    }


    /**
     * 短信群推接口
     *
     * @param request HttpServletRequest
     * @return 返回结果
     */
    @PostMapping("iopPushFileSmsMsg")
    public Object iopPushFileSmsMsg(HttpServletRequest request) {
        // 参数格式示例：{"msg_params":{"keyword1":"短信群发内容"},"send_type":0,"msg_type":1005,"start_time":"2019-05-06 16:00:00","file_sign":"39e769fc0c02dc9e2fb82808c940c613","push_level":1,"app_id":"2290","expire_time":"2019-05-06 18:00:00","file_name":"/home/common/2290/message/20190505/day/push0505.txt"}
        String requestDataJson = request.getParameter("data");
        log.info("iopPushFileSmsMsg 接收到请求参数：{}", requestDataJson);
        Map<String, Object> requestDataMap = getRequestParamData(requestDataJson);
        if (requestDataMap == null) {
            log.error("iopPushFileSmsMsg-接收到请求参数异常:{}", requestDataJson);
            return dataErrorMessage(requestDataJson);
        }
        Map<String, Object> requestParam = new HashMap<String, Object>();
        requestParam.put("serviceName", "iop_push_file_sms_msg");
        requestParam.put("header", getParamHeader());
        requestParam.put("data", requestDataMap);
        log.info("短信群推接口请求参数:{}", requestParam.toString());
        String url = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_SMS_PUSH_FILE_URL);
        log.debug("短信群推接口地址:{}", url);
        ResponseEntity<String> result = sendPostReqestByJson(url, requestParam);
        String response = result.getBody();
        log.info("短信群推接口返回结果:{}", response);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
        if ("-1".equals(jsonObjectResult.getStr("responseCode"))) {
            return responseErrorMsg(jsonObjectResult);
        }
        return jsonObject;
    }


    @ApiOperation(value = "上传安全附件", notes = "上传安全附件")
    @PostMapping("uploadFile")
    public ActionResponse uploadFile(@RequestParam MultipartFile file) {
        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            if (file.isEmpty()) {
                return ActionResponse.getFaildResp("文件为空");
            }
            if (fileTypes.stream().noneMatch(t -> Objects.requireNonNull(file.getOriginalFilename()).contains(t))) {
                return ActionResponse.getFaildResp("当前支持的安全附件格式不正确，请上传" + JSONUtil.toJsonStr(fileTypes) + "格式附件！");
            }
            String newFileName = planDefService.saveUploadFile(file);
            resp.setData(newFileName);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ActionResponse.getFaildResp("文件上传失败");
        }
        return resp;
    }

    /**
     * 获取请求参数数据
     *
     * @param requestDataJson 请求参数(json格式)
     * @return Map<String, Object>
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getRequestParamData(String requestDataJson) {
        Map<String, Object> requestDataMap = null;
        if (StringUtils.isEmpty(requestDataJson)) {
            return requestDataMap;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestDataMap = mapper.readValue(requestDataJson, Map.class);
        } catch (IOException e) {
            log.error("data数据格式不正确，data={}", requestDataJson, e);
        }
        return requestDataMap;
    }

    /**
     * 数据格式错误返回信息封装
     *
     * @param requestDataJson 原始请求参数
     * @return 错误信息
     */
    private Map<String, Object> dataErrorMessage(String requestDataJson) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("responseCode", "-1");
        resultMap.put("message", "data数据格式不正确:" + requestDataJson);
        Map<String, Object> result = new HashMap<>();
        result.put("result", resultMap);
        return result;
    }

    /**
     * 接口错误信息封装
     *
     * @param jsonObject jsonObject对象
     * @return 错误信息
     */
    private Map<String, Object> responseErrorMsg(JSONObject jsonObject) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("responseCode", jsonObject.get("responseCode"));
        resultMap.put("message", jsonObject.get("message"));
        Map<String, Object> result = new HashMap<>();
        result.put("result", resultMap);
        return result;
    }


    /**
     * 生成请求参数header,一级电渠的http请求参数的header是相同的。
     *
     * @return 返回参数header部分
     */
    private Map<String, Object> getParamHeader() {
        Map<String, Object> header = new HashMap<String, Object>();
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        long currentTimeStamp = System.currentTimeMillis();
        header.put("version", "1.0");
        header.put("timestamp", String.valueOf(currentTimeStamp));
        String digest = null;
        try {
            digest = Base64.encodeBase64String(MD5Util.encode(String.valueOf(currentTimeStamp) + "CM_201709").getBytes());
        } catch (Exception e) {
            log.error("加密失败",e);
        }
        header.put("digest", digest);
        header.put("conversationId", formatter.format(currentTime) + "888888");
        return header;
    }

    /**
     * post请求简单封装
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return 请求结果
     */
    private ResponseEntity<String> sendPostReqestByJson(String url, Map<String, Object> param) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestParam = new HttpEntity<Map<String, Object>>(param, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.postForEntity(url, requestParam, String.class);
        return result;
    }

}
