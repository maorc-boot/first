package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.RestFullUtil;
import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.camp.model.ChannelMaterialModel;
import com.asiainfo.biapp.pec.plan.jx.camp.req.*;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IChannelMaterialQueryService;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCommunicationUsers;
import com.asiainfo.biapp.pec.plan.util.MD5Util;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 活动策划渠道素材查询
 * </p>
 *
 * @author ranpf
 * @since 2023-1-3
 */
@RestController
@RequestMapping("/api/channelMaterial/jx")
@Api(tags = "江西:活动策划渠道素材查询服务")
@Slf4j
public class McdDimMaterialQueryController {


    @Resource
    private IChannelMaterialQueryService channelMaterialQueryService;

    @PostMapping(path = "/queryStandardizationMaterialList")
    @ApiOperation(value = "江西标准化素材查询接口", notes = "标准化素材查询接口")
    public ActionResponse<IPage<ChannelMaterialModel>> queryChannelMaterialList(@RequestBody ChannelMaterialListQuery req) {
        log.info("start queryChannelMaterialList para:{}", new JSONObject(req));
        return ActionResponse.getSuccessResp(channelMaterialQueryService.queryChannelMaterialList(req));
    }

    @PostMapping(path = "/querySelectMaterialList")
    @ApiOperation(value = "江西渠道选择素材查询接口", notes = "渠道选择素材查询接口")
    public ActionResponse<IPage<ChannelMaterialModel>> getSelectMaterialList(@RequestBody ChannelSelectMaterialListQuery req) {
        log.info("start getSelectMaterialList para:{}", new JSONObject(req));
        return ActionResponse.getSuccessResp(channelMaterialQueryService.getSelectMaterialList(req));
    }


    /**
     * 图片 视频预览功能
     *
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "江西:素材图片视频预览 ", notes = "素材图片视频预览")
    @PostMapping(path = "/loadImage")
    public ActionResponse<MaterialLoadImageJxResponse> loadImage(@RequestBody DimMaterialPreviewRequest dimMaterialRequest, HttpServletResponse response) throws Exception {
        log.info("start loadImageJx param {}", new JSONObject(dimMaterialRequest));
        if (StringUtils.isBlank(dimMaterialRequest.getMaterialId())) {
            throw new RuntimeException("预览失败,素材ID为空!");
        }
        String resourceUrl = channelMaterialQueryService.queryMaterialById(dimMaterialRequest.getMaterialId()) .get(0).getResourceUrl();
        channelMaterialQueryService.loadImage(resourceUrl, dimMaterialRequest, response);
        return null;
    }



    @PostMapping(path = "/queryCommunicationUsers")
    @ApiOperation(value = "江西转至沟通人查询接口", notes = "转至沟通人查询接口")
    public ActionResponse<IPage<McdCommunicationUsers>> queryCommunicationUsers(@RequestBody McdCommunicationQuery req) {
        log.info("start queryCommunicationUsers para:{}", new JSONObject(req));
        return ActionResponse.getSuccessResp(channelMaterialQueryService.queryCommunicationUsers(req));
    }


    @PostMapping(path = "/queryH5CommunicationUsers")
    @ApiOperation(value = "江西H5转至沟通人查询接口", notes = "H5转至沟通人查询接口")
    public ActionResponse  queryH5CommunicationUsers(@RequestBody McdCommunicationQuery req) {
        log.info("start queryH5CommunicationUsers para:{}", new JSONObject(req));
        List<Map<String,Object>> dataList = new ArrayList<>();
        Pager pager = new Pager();
        IPage<McdCommunicationUsers> usersPage = channelMaterialQueryService.queryCommunicationUsers(req);

        usersPage.getRecords().forEach(mcUser->{
            Map<String,Object>  userMap = new HashMap<>();
            userMap.put("USERID",mcUser.getUserId());
            userMap.put("USERNAME",mcUser.getUserName());
            userMap.put("DEPARTMENTID",mcUser.getDepartmentId());
            userMap.put("MOBILEPHONE",mcUser.getMobilePhone());
            userMap.put("CITYID",mcUser.getCityId());
            userMap.put("CITYNAME",mcUser.getCityName());
            userMap.put("TITLE",mcUser.getDepartmentName());
            dataList.add(userMap);
        });
        pager.setPageNum(req.getCurrent());
        pager.setPageSize(req.getSize());
        pager.setResult(dataList);
        pager.setTotalPage(Integer.parseInt(usersPage.getPages()+""));
        pager.setTotalSize( usersPage.getTotal());

        return ActionResponse.getSuccessResp(pager);

    }


    /**
     *  获取星火素材类型
     */
    @PostMapping("/queryMaterialType")
    @ApiOperation(value = "获取星火素材类型",notes = "获取星火素材类型")
    public Object queryMaterialType() {
        Map<String,String> typeMap = new HashMap<>();
        typeMap.put("provinceCode", RedisUtils.getDicValue("MCD_QYWX_REQUEST_PROVINCE_CODE"));

        try {
            log.info("开始调用获取星火素材类型接口，url={} ,入参: {}", RedisUtils.getDicValue("MCD_QYWX_REQUEST_MATERIAL_TYPE_URL"), JSONUtil.toJsonStr(typeMap));
           String jsonRes ="";
            if ("1".equalsIgnoreCase(RedisUtils.getDicValue("MCD_IS_PRODUCTION_ENVIRONMENT"))){
                jsonRes = getMatialType();
            } else {
                 jsonRes = RestFullUtil.getInstance().sendPost2(RedisUtils.getDicValue("MCD_QYWX_REQUEST_MATERIAL_TYPE_URL")+getReqHeaderMap(), JSONUtil.toJsonStr(typeMap));

            }

            return jsonRes;
        } catch (Exception e) {
            log.error("调用获取星火素材类型接口失败", e);
            return null;
        }
    }


    /**
     *  根据素材类型获取星火素材列表
     */
    @PostMapping("/queryMaterialInfos")
    @ApiOperation(value = "获取星火素材列表",notes = "获取星火素材列表")
    public ActionResponse queryMaterialInfos(@RequestBody McdSparkMaterialPageQuery pageQuery ) {


        log.info("获取星火素材列表 入参: "+ com.alibaba.fastjson.JSONObject.toJSONString(pageQuery));
        McdSparkMaterialQuery req = new McdSparkMaterialQuery();

        req.setMaterialType(pageQuery.getMaterialType());
        req.setMobile(pageQuery.getMobile());
        req.setCategoryId(pageQuery.getCategoryId());
        req.setProvinceCode(RedisUtils.getDicValue("MCD_QYWX_REQUEST_PROVINCE_CODE"));
        req.setSearch(pageQuery.getSearch());
        Page page = new Page();
        page.setCurrent(pageQuery.getCurrent());
        page.setSize(pageQuery.getSize());
        int begin = (pageQuery.getCurrent() - 1) * pageQuery.getSize();
        int end = begin + pageQuery.getSize();
        List resultList = new ArrayList<>();

        try {
            log.info("开始调用获取星火素材列表接口，url={} ,入参: {}", RedisUtils.getDicValue("MCD_QYWX_REQUEST_MATERIAL_LIST_URL"),JSONUtil.toJsonStr(req));

            String jsonRes ="";
            if ("1".equalsIgnoreCase(RedisUtils.getDicValue("MCD_IS_PRODUCTION_ENVIRONMENT"))){
                jsonRes = getMatialInfo();
            } else {
                jsonRes = RestFullUtil.getInstance().sendPost2(RedisUtils.getDicValue("MCD_QYWX_REQUEST_MATERIAL_LIST_URL")+getReqHeaderMap(), JSONUtil.toJsonStr(req));
            }

            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(jsonRes);
            if ("200".equals(jsonObject.get("statusCode")+"")) {
                JSONArray data = (JSONArray) jsonObject.get("data");
                if(Objects.nonNull(data)){
                    if (end >= data.size() ){
                        end = data.size();
                    }
                    page.setTotal(data.size());
                    for (int i = begin; i < end; i++) {
                        resultList.add(data.get(i));
                    }
                    page.setRecords(resultList);

                }
                return ActionResponse.getSuccessResp(page);

            }
            return ActionResponse.getFaildResp("查询星火素材接口异常");

        } catch (Exception e) {
            log.error("调用获取星火素材列表接口失败", e);
            return ActionResponse.getFaildResp("查询星火素材接口异常");
        }
    }



    /**
     * 图片 视频预览功能
     *
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "江西:企业微信图片预览 ", notes = "企业微信图片预览")
    @PostMapping(path = "/loadImagePreview")
    public ActionResponse<MaterialLoadImageJxResponse> loadImagePreview(@RequestBody DimMaterialPreviewRequest dimMaterialRequest, HttpServletResponse response) throws Exception {
        log.info("start loadImagePreview param {}", new JSONObject(dimMaterialRequest));
        if (StringUtils.isBlank(dimMaterialRequest.getMaterialId())) {
            throw new RuntimeException("预览失败,素材ID为空!");
        }
        String resourceUrl = channelMaterialQueryService.queryMaterialById(dimMaterialRequest.getMaterialId()) .get(0).getResourceUrl();
        channelMaterialQueryService.loadImage(resourceUrl, dimMaterialRequest, response);
        return null;
    }

    /**
     * 星火新增请求头信息
     * @return
     */
    private String getReqHeaderMap(){

        Map<String,String> headerMap = new LinkedHashMap<>();

        headerMap.put("channel",RedisUtils.getDicValue("MCD_QYWX_REQUEST_CHANNEL"));
        headerMap.put("requestNo",System.currentTimeMillis()+"");
        headerMap.put("timeStamp",getTimeStamp());

        StringBuilder buffer = new StringBuilder();
        for (Map.Entry<String,String>  entry:headerMap.entrySet()){
            buffer.append(entry.getKey()).append(entry.getValue());
        }
        buffer.append(RedisUtils.getDicValue("MCD_QYWX_REQUEST_SECRET"));

        String sign = getSign(buffer.toString());
        headerMap.put("sign",sign);

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String,String> entry: headerMap.entrySet()){

            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");

        }
        String publicParams = builder.deleteCharAt(builder.length()-1).toString();
        log.info("星火请求公共参数信息: "+ publicParams);

        return publicParams;
    }

    private String getSign(String key){

        log.info("星火需要签名认证的key: "+ key);
        try {
            return MD5Util.encode(key);

        }catch (Exception e){

            log.error("星火获取签名sign异常",e);
            return "";
        }
    }


    private String getTimeStamp(){
        FastDateFormat df = FastDateFormat.getInstance("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    private String getMatialType(){

        return  " {\"success\":true,\"data\":[{\"name\":\"集团商品\",\"materialType\":4,\"categoryId\":1},{\"name\":\"活动\",\"materialType\":1,\"categoryId\":2},{\"name\":\"便民\",\"materialType\":1,\"categoryId\":3},{\"name\":\"常用语\",\"materialType\":2,\"categoryId\":4},{\"name\":\"服务\",\"materialType\":1,\"categoryId\":5},{\"name\":\"专题\",\"materialType\":1,\"categoryId\":6},{\"name\":\"品牌\",\"materialType\":1,\"categoryId\":7},{\"name\":\"媒体\",\"materialType\":1,\"categoryId\":19},{\"name\":\"商品\",\"materialType\":5,\"categoryId\":34},{\"name\":\"图片类\",\"materialType\":7,\"categoryId\":65},{\"name\":\"IOP产品\",\"materialType\":5,\"categoryId\":311}],\"statusCode\":200,\"alertMsg\":\"操作成功\"}";
    }

    private String getMatialInfo(){
        return "{\"success\":true,\"data\":[ {\"id\":\"56072\",\"title\":\"ch白名单配件\",\"content\":\"\",\"picUrl\":\"https://img1.shop.10086.cn/goods/tq7ujamtfyjnedrqr_350x350.png\",\"price\":\"1\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"51215\",\"title\":\"华为 SuperCharge 车载快速充电器AP38\",\"content\":null,\"picUrl\":\"https://img1.shop.10086.cn/goods/ta942s2zyxzttgewa_350x350.png\",\"price\":\"10500\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"57289\",\"title\":\"iphone5s\",\"content\":\"\",\"picUrl\":\"https://img1.shop.10086.cn/goods/tq7whb4kzc7v5eb5b_350x350.png\",\"price\":\"350000\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"50031\",\"title\":\"【熠天海纳】荣耀Note10 6GB+128G公开版4G智能手机\",\"content\":null,\"picUrl\":\"https://img1.shop.10086.cn/goods/t7qmnrzjysztt5225_350x350.png\",\"price\":\"279900\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"48289\",\"title\":\"行车卫士电摩车定位器裸机版\",\"content\":\"机车安防 轨迹回放 快速报警\",\"picUrl\":\"https://img1.shop.10086.cn/goods/twanwgygcasstsw2_350x350.png\",\"price\":\"18800\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"54734\",\"title\":\"倍思 一次性酒精消毒棉片 加赠20片\",\"content\":null,\"picUrl\":\"https://img1.shop.10086.cn/goods/th9vuyscpekttndtu7_350x350.png\",\"price\":\"2990\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"53099\",\"title\":\"【享好礼】 iPhone11 Pro（A2217）双卡公开版\",\"content\":\"购机享好礼 \",\"picUrl\":\"https://img1.shop.10086.cn/goods/tapgkj6meztt3m3a_350x350.png\",\"price\":\"819900\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"57672\",\"title\":\"信用购机预售-luld-1229-2\",\"content\":\"测试专用，别人勿动\",\"picUrl\":\"https://img1.shop.10086.cn/goods/tq7kpbqfsr2v5ebpf_350x350.png\",\"price\":\"30000\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"56663\",\"title\":\"终端公司 团购 syj 0716\",\"content\":\"\",\"picUrl\":\"https://img1.shop.10086.cn/goods/tq7kpbqfsr2v5ebpf_350x350.png\",\"price\":\"10000\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"51687\",\"title\":\"中国移动和云镜智能行车记录仪CM52\",\"content\":\"和云镜行车记录仪CM52\",\"picUrl\":\"https://img1.shop.10086.cn/goods/taghwtq3x5btt8nae_350x350.png\",\"price\":\"32900\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"55905\",\"title\":\"ch测试全网套餐西藏真实\",\"content\":\"\",\"picUrl\":\"https://img1.shop.10086.cn/goods/thrypmxgpz7fkb3ef_350x350.png\",\"price\":\"11100\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"50991\",\"title\":\"狗尾草 Gowild AI智能音箱 家用 HE琥珀小姐姐 智能机\",\"content\":null,\"picUrl\":\"https://img1.shop.10086.cn/goods/taf9trxv5ghttybea_350x350.png\",\"price\":\"139900\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"49056\",\"title\":\"咪咕中信书店\",\"content\":null,\"picUrl\":\"https://img1.shop.10086.cn/goods/t6rsdk5nwkd7zz8a_350x350.png\",\"price\":\"2900\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null} ,{\"id\":\"52366\",\"title\":\"【优惠购机】华为Mate30 5G版 公开版手机\",\"content\":\"信用购机最高优惠3780元 *仅支持部分省份  或享最高699元大礼包\",\"picUrl\":\"https://img1.shop.10086.cn/goods/th57rhuhej3tt9qgu_350x350.png\",\"price\":\"449900\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"57256\",\"title\":\"PC终端详情页改版-优惠购机、信用购机\",\"content\":\"\",\"picUrl\":\"https://img1.shop.10086.cn/goods/tq7kpbqfsr2v5ebpf_350x350.png\",\"price\":\"9999900\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null},{\"id\":\"56251\",\"title\":\"终端+标签-luld-0610\",\"content\":\"测试专用，别人勿动\",\"picUrl\":\"https://img1.shop.10086.cn/goods/tq7kpvck6p4f7r7cf_350x350.png\",\"price\":\"66600\",\"referenceStatus\":null,\"referenceStatusName\":\"集团标识\",\"beginTime\":null,\"endTime\":null} ],\"statusCode\":200,\"alertMsg\":\"操作成功\"}";
    }

}

