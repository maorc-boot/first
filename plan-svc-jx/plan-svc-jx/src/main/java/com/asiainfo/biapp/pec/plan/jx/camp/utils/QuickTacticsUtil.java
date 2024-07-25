package com.asiainfo.biapp.pec.plan.jx.camp.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mamp
 * @date 2021/5/26
 */
@Slf4j
public class QuickTacticsUtil {

    private static final Logger logger = LoggerFactory.getLogger(QuickTacticsUtil.class);

    public static void main(String[] args) {
        QuickTacticsUtil test = new QuickTacticsUtil();
        try {
            String result = test.processRequest();
            logger.info("result:{}", result);
        } catch (Exception e) {
            logger.error("error: ", e);
        }
    }

    private String processRequest() throws Exception {
        String url = "http://10.180.211.23:27156/huiI/discover/iopNotify";
        List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
        param.add(new BasicNameValuePair("notifyType", "1"));
        param.add(new BasicNameValuePair("contactorId", "9999"));
        param.add(new BasicNameValuePair("contactorName", "测试"));
        param.add(new BasicNameValuePair("contactorDesc", "测试"));
        param.add(new BasicNameValuePair("imageName", "/home/ecps/T1A8ETBXYT1RXrhCrK.jpg"));
        param.add(new BasicNameValuePair("startTime", "20210521 00:00:00"));
        param.add(new BasicNameValuePair("endTime", "20210528 00:00:00"));
        param.add(new BasicNameValuePair("url", "https://wap.jx.10086.cn/hui/release/home/index.html"));
        String huiJtChannel = "huiIJCrhNy2YvK624iKEkf";
        param.add(new BasicNameValuePair("huiJtChannel", huiJtChannel));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        String huiJtTimeStamp = dateFormat.format(new Date());
        param.add(new BasicNameValuePair("huiJtTimeStamp", huiJtTimeStamp));
        param.add(new BasicNameValuePair("huiJtSign", sign(huiJtTimeStamp, huiJtChannel)));
        return httpCall(url, param);
    }

    /**
     * 获取sign
     *
     * @param huiJtTimeStamp
     * @param huiJtChannel
     * @return
     * @throws Exception
     */
    private String sign(String huiJtTimeStamp, String huiJtChannel)
            throws Exception {
        Map<String, Object> params = new HashMap<>(16);
        params.put("notifyType", "1");
        params.put("contactorId", "9999");
        params.put("contactorName", "测试");
        params.put("contactorDesc", "测试");
        params.put("imageName", "/home/ecps/T1A8ETBXYT1RXrhCrK.jpg");
        params.put("startTime", "20210521 00:00:00");
        params.put("endTime", "20210528 00:00:00");
        params.put("url", "https://wap.jx.10086.cn/hui/release/home/index.html");
        params.put("huiJtTimeStamp", huiJtTimeStamp);
        params.put("huiJtChannel", huiJtChannel);
        String signResult = createSign(params, getToken(huiJtChannel), false);
        logger.info("++" + signResult);
        return signResult;
    }

    private String getToken(String huiJtChannel) throws Exception {
        return "2021052616331631637414";
    }

    /**
     * 发送http请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String httpCall(String url, List<BasicNameValuePair> param) {
        StringBuffer result = new StringBuffer();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig config = RequestConfig.custom().setSocketTimeout(6000).build();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(config);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(param,
                    "utf-8");
            httpPost.setEntity(entity);
            CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                result.append(EntityUtils.toString(httpResponse.getEntity(),
                        "utf-8"));
            }
            logger.info("httpCall result:" + result);
        } catch (Exception e) {
            logger.error("error: ", e);
            throw new RuntimeException(e);
        } finally {
            if (null != httpclient) {
                try {
                    httpclient.close();
                } catch (Exception e) {
                }
            }
        }

        return result.toString();

    }

    /**
     * createSign
     * @param params
     * @param secret
     * @param encode
     * @return
     */
    public static String createSign(Map<String, Object> params,
                                    String secret, boolean encode) {
        Set<String> keysSet = params.keySet();
        // 对参数名进行字典排序.
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        // 拼接有序的参数名-值串
        StringBuffer temp = new StringBuffer();
        for (Object key : keys) {
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = String.valueOf(value);
            }
            if (encode) {
                try {
                    temp.append(URLEncoder.encode(valueString, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error("error: ", e);
                }
            } else {
                temp.append(valueString);
            }
        }
        temp.append(secret);
        return md5Encode(temp.toString()).toUpperCase();
    }

    @SuppressWarnings("all")
    public static String md5Encode(String originStr) {
        String md5String = "";
        StringBuffer buffer = new StringBuffer();
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(originStr.getBytes("UTF-8"));
            for (byte b : bytes) {
                buffer.append(Integer.toHexString((b & 0xf0) >>> 4));
                buffer.append(Integer.toHexString(b & 0x0f));
            }
            md5String = buffer.toString();
        } catch (java.security.NoSuchAlgorithmException
                | UnsupportedEncodingException nsae) {
            logger.info("MD5 does not appear to be supported" + nsae);
        }
        return md5String;
    }
}
