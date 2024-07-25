package com.asiainfo.biapp.pec.plan.jx.link.util;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class SignatureUtil {

    public static final String TOKEN = "9adkcieiq907a2pmli09";//用于生成数字签名

    private static final Log log = LogFactory.getLog(SignatureUtil.class);
    /**
     * 创建SHA1签名
     * @param params
     * @return SHA1签名
     */
    public static String createSignature(SortedMap<String, String> params) {
        String s = sha1Encrypt(sortParams(params));
        return s;
//		return sha1Encrypt(sortParams(params));
    }

    /**
     * 创建SHA1签名
     * @param timeStamp
     * @param nonce
     * @param
     * @return
     */
    public static String createSignature(String timeStamp, String nonce) {
        SortedMap<String, String> signParams = new TreeMap<String, String>();
        signParams.put("token", TOKEN);
        signParams.put("timeStamp", timeStamp);
        signParams.put("nonce", nonce);
        return createSignature(signParams);
    }

    public static String createSignature(String timeStamp, String nonce, String secretId) {
        SortedMap<String, String> signParams = new TreeMap<String, String>();
        signParams.put("token", TOKEN);
        signParams.put("timeStamp", timeStamp);
        signParams.put("nonce", nonce);
        signParams.put("secretId", secretId);
        return createSignature(signParams);
    }



    /**
     * 使用SHA1算法对字符串进行加密
     * @param str
     * @return
     */
    public static String sha1Encrypt(String str) {

        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };

        try {

            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }

            return new String(buf);

        } catch (Exception e) {
            log.error("error:"+e);
            return null;
        }
    }

    /**
     * 生成时间戳
     * @return
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * 生成随机字符串
     * @return
     */
    public static String getRandomStr() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    /**
     * 根据参数名称对参数进行字典排序
     * @param params
     * @return
     */
    private static String sortParams(SortedMap<String, String> params) {
        StringBuffer sb = new StringBuffer();
        Set<Entry<String, String>> es = params.entrySet();
        Iterator<Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            sb.append(k + "=" + v + "&");
        }
        return sb.substring(0, sb.lastIndexOf("&"));
    }

    public static String getMD5(String str, String charset) {
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
//            log.error("context:", e);
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static void main(String[] args) throws Exception {
        //c4db7f5867a6290378cf24429f2803da0516a788
        String timeStamp = getTimeStamp();
        String nonce = getRandomStr();
        String secretId = "zT06CfkZ9q9ua";

        String sign = createSignature(timeStamp, nonce, secretId);
        System.out.println("timeStamp:"+timeStamp);
        System.out.println("nonce:"+nonce);
        System.out.println("secretId:"+secretId);
        System.out.println("signature:" + sign);
    }
}