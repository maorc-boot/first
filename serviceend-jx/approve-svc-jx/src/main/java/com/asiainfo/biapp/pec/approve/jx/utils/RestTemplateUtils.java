package com.asiainfo.biapp.pec.approve.jx.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring RestTemplate工具类
 */
@Slf4j
public class RestTemplateUtils {
    private static class SingletonRestTemplate {
        static final RestTemplate INSTANCE = new RestTemplate();
    }

    private RestTemplateUtils() {

    }

    /**
     * 单例实例
     */
    public static RestTemplate getInstance() {
        return SingletonRestTemplate.INSTANCE;
    }

    /**
     * get根据url获取对象
     */
    public static String post(String url, Map<String, String> params) {
        ((SimpleClientHttpRequestFactory) RestTemplateUtils.getInstance().getRequestFactory()).setConnectTimeout(3000);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("appId", "imcd");//业务系统简称，英文缩写，如：OA系统，oa
        requestHeaders.set("processId", "yxdbprocess");//为业务系统的流程模板id，如：公司发文类型， gsfw
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Map<String, String> param = desMessage(params);//对参数加密
        HttpEntity<Object> request = new HttpEntity<Object>(param, requestHeaders);
        log.info("请求emis接口url:{},入参:{},加密前的参数：{}", url, request, params);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        try {
            responseEntity = RestTemplateUtils.getInstance().postForEntity(url, request, String.class);
            log.info("emis接口返回结果:{}", responseEntity);
        } catch (Exception e) {
            log.error("调用emis异常,massge=" + e.getMessage());
            log.error("error: ", e);
        }
        return responseEntity.getBody();
    }

    /**
     * 阅知待办调用emis参数header参数组装和接口调用
     */
    public static String postEmisRead(String url, Map<String, String> params) {
        ((SimpleClientHttpRequestFactory) RestTemplateUtils.getInstance().getRequestFactory()).setConnectTimeout(3000);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("appId", "imcd");//业务系统简称，英文缩写，如：OA系统，oa
        requestHeaders.set("processId", "yxdbprocess2");//流程定义ID // 营销阅知流程
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Map<String, String> param = encryptReadMessage(params);//对参数加密
        HttpEntity<Object> request = new HttpEntity<Object>(param, requestHeaders);
        log.info("请求emis接口url:{},入参:{},阅知待办加密前的参数：{}", url, request, params);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        try {
            responseEntity = RestTemplateUtils.getInstance().postForEntity(url, request, String.class);
            log.info("emis阅知待办接口返回结果:{}", responseEntity);
        } catch (Exception e) {
            log.error("调用emis阅知待办异常,massge=" + e.getMessage());
            log.error("error: ", e);
        }
        return responseEntity.getBody();
    }


    /**
     * 加密
     *
     * @param params
     * @return Map<String, String>
     */
    private static Map<String, String> desMessage(Map<String, String> params) {
        JSONObject jsonObj = JSONObject.fromObject(params);
        // 对参数加密
        String bodyDes = DesUtils.encode3Des("imcdpassword2018", jsonObj.toString());

        Map<String, String> desMessage = new HashMap<String, String>();
        desMessage.put("header", JSONObject.fromObject(EmisUtils.header).toString());

        desMessage.put("body", bodyDes);
        return desMessage;
    }

    /**
     * 阅知消息加密
     *
     * @param params
     * @return Map<String, String>
     */
    private static Map<String, String> encryptReadMessage(Map<String, String> params) {
        JSONObject jsonObj = JSONObject.fromObject(params);
        // 对参数加密
        String bodyDes = DesUtils.encode3Des("imcdpassword2018", jsonObj.toString());

        Map<String, String> desMessage = new HashMap<String, String>();
        desMessage.put("header", JSONObject.fromObject(EmisUtils.readHeader).toString());
        desMessage.put("body", bodyDes);
        return desMessage;
    }



    /**
     * 预警待办调用emis参数header参数组装和接口调用
     */
    public static String postEmisCampWarn(String url, Map<String, String> params) {
        ((SimpleClientHttpRequestFactory)RestTemplateUtils.getInstance().getRequestFactory()).setConnectTimeout(3000);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("appId", "imcd");//业务系统简称，英文缩写，如：OA系统，oa
        requestHeaders.set("processId", "yxdbprocess3");//流程定义ID // 营销预警流程
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Map<String, String> param = encryptCampWarnMessage(params);//对参数加密
        HttpEntity<Object> request=new HttpEntity<Object>(param, requestHeaders);
        log.info("请求emis接口url:{},入参:{},预警待办加密前的参数：{}",url,request,params);
        ResponseEntity<String> responseEntity= new ResponseEntity<>(HttpStatus.OK);
        try {
            responseEntity = RestTemplateUtils.getInstance().postForEntity(url, request, String.class);
            log.info("emis预警待办接口返回结果:{}",responseEntity);
        } catch (Exception e) {
            log.error("调用emis预警待办异常,massge=" + e.getMessage());
            log.error("error: ", e);
        }
        return responseEntity.getBody();
    }


    /**
     * 预警消息加密
     *
     * @param  params
     * @return Map<String, String>
     */
    private static Map<String, String> encryptCampWarnMessage(Map<String, String> params) {
        JSONObject jsonObj = JSONObject.fromObject(params);
        // 对参数加密
        String bodyDes = DesUtils.encode3Des("imcdpassword2018", jsonObj.toString());

        Map<String, String> desMessage = new HashMap<String, String>();
        desMessage.put("header", JSONObject.fromObject(EmisUtils.warnHeader).toString());
        desMessage.put("body", bodyDes);
        return desMessage;
    }

}