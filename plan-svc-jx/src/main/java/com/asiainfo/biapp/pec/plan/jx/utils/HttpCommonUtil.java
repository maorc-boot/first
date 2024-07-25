package com.asiainfo.biapp.pec.plan.jx.utils;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;


/**
 * http工具类
 * 在org.apache.commons.httpclient上封装的
 * ranpf   2023 1 9
 */
public final class HttpCommonUtil {

    private static final Log log = LogFactory.getLog(HttpCommonUtil.class);
    private HttpCommonUtil() {
    }

    /**
     * post请求
     *
     * @param url    服务端地址
     * @param params 请求参数
     *               如下
     *               params.put("templateId", "1");
     *               params.put("isCepNodeAtFirst", "1");
     * @return 服务端返回内容  默认为""字符串
     */
    public static Map<String,Object> postMethod(String url, Map<String, String> params) {
        String response = "";
        Map<String,Object> resultMap = new HashMap<>();
        HttpClient client = new HttpClient();
        // Create a method instance.
        PostMethod method = new PostMethod(url);
        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            // Execute the method.
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator<Entry<String, String>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
                list.add(new NameValuePair(entry.getKey(), entry.getValue()));
            }
            method.setRequestBody(list.toArray(new NameValuePair[list.size()]));

            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                log.error("Method failed: " + method.getStatusLine());
            } else {
                response = method.getResponseBodyAsString();
            }
            resultMap.put("statusCode",statusCode);
            resultMap.put("response",response);

        } catch (HttpException e) {
            log.error("Fatal protocol violation: " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("Fatal transport error:" + e.getMessage(), e);
        } finally {
            method.releaseConnection();
        }
        return resultMap;
    }

    /**
     * get请求
     *
     * @param url 服务端地址
     * @return 服务端返回内容  默认为""字符串
     */
    public static String getMethod(String url) {
        String responseBody = "";
        // Create an instance of HttpClient.
        HttpClient client = new HttpClient();
        // Create a method instance.
        GetMethod method = new GetMethod(url);
        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                log.error("Method failed: " + method.getStatusLine());
            } else {
                // Read the response body.
                responseBody = method.getResponseBodyAsString();
                log.debug("getMethod response:" + responseBody);
            }
        } catch (HttpException e) {
            log.error("Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            log.error("Fatal transport error: " + e.getMessage());
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return responseBody;
    }
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数
     *
     * @return 所代表远程资源的响应结果
     */
    public static String sendHttpJsonPost(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");    // POST方法


            // 设置通用的请求属性

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");

            conn.connect();

            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error("发送 POST 请求出现异常！"+e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                log.error("发送 POST 请求关闭流异常"+ex);
            }
        }
        return result;
    }
    
}
