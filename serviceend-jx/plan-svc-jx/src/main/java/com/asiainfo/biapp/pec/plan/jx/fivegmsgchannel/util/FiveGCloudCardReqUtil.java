package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * description: 5G云卡请求工具类
 *
 * @author: lvchaochao
 * @date: 2023/9/12
 */
@Component
public class FiveGCloudCardReqUtil {

    /**
     * 参数嵌套自然排序工具方法--自然排序，得到排序后的参数
     *
     * @param json 参数串
     * @return {@link String}
     */
    public String sort(String json) {
        try {
            SortedMap<String, Object> resultTreeMap = new TreeMap<>();
            Map<String, Object> map = JSONObject.parseObject(json, Map.class);
            nestSortParameter(map);
            naturalOrdering(new TreeMap<>(), map);
            map.forEach(resultTreeMap::put);
            return JSONObject.toJSONString(resultTreeMap, SerializerFeature.WriteMapNullValue);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private static byte[] md5(byte[] str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str);
        return md.digest();
    }

    /**
     * 将待加密数据data，通过密钥key，使用hmac-md5算法进行加密，然后返回加密结果。 参照rfc2104 HMAC算法介绍实现。
     *
     * @param key  密钥
     * @param data 待加密数据
     * @return 加密结果
     */
    public byte[] getHmacMd5Bytes(byte[] key, byte[] data) throws NoSuchAlgorithmException {
        /*
         * HmacMd5 calculation formula: H(K XOR opad, H(K XOR ipad, text))
         * HmacMd5 计算公式：H(K XOR opad, H(K XOR ipad, text))
         * H代表hash算法，本类中使用MD5算法，K代表密钥，text代表要加密的数据 ipad为0x36，opad为0x5C。
         */
        int length = 64;
        byte[] ipad = new byte[length];
        byte[] opad = new byte[length];
        for (int i = 0; i < 64; i++) {
            ipad[i] = 0x36;
            opad[i] = 0x5C;
        }
        byte[] actualKey = key; // Actual key.
        byte[] keyArr = new byte[length]; // Key bytes of 64 bytes length
        /*
         * If key's length is longer than 64,then use hash to digest it and use
         * the result as actual key. 如果密钥长度，大于64字节，就使用哈希算法，计算其摘要，作为真正的密钥。
         */
        if (key.length > length) {
            actualKey = md5(key);
        }
        for (int i = 0; i < actualKey.length; i++) {
            keyArr[i] = actualKey[i];
        }

        /*
         * append zeros to K 如果密钥长度不足64字节，就使用0x00补齐到64字节。
         */
        if (actualKey.length < length) {
            for (int i = actualKey.length; i < keyArr.length; i++)
                keyArr[i] = 0x00;
        }

        /*
         * calc K XOR ipad 使用密钥和ipad进行异或运算。
         */
        byte[] kIpadXorResult = new byte[length];
        for (int i = 0; i < length; i++) {
            kIpadXorResult[i] = (byte) (keyArr[i] ^ ipad[i]);
        }

        /*
         * append "text" to the end of "K XOR ipad" 将待加密数据追加到K XOR ipad计算结果后面。
         */
        byte[] firstAppendResult = new byte[kIpadXorResult.length + data.length];
        for (int i = 0; i < kIpadXorResult.length; i++) {
            firstAppendResult[i] = kIpadXorResult[i];
        }
        for (int i = 0; i < data.length; i++) {
            firstAppendResult[i + keyArr.length] = data[i];
        }

        /*
         * calc H(K XOR ipad, text) 使用哈希算法计算上面结果的摘要。
         */
        byte[] firstHashResult = md5(firstAppendResult);

        /*
         * calc K XOR opad 使用密钥和opad进行异或运算。
         */
        byte[] kOpadXorResult = new byte[length];
        for (int i = 0; i < length; i++) {
            kOpadXorResult[i] = (byte) (keyArr[i] ^ opad[i]);
        }

        /*
         * append "H(K XOR ipad, text)" to the end of "K XOR opad" 将H(K XOR
         * ipad, text)结果追加到K XOR opad结果后面
         */
        byte[] secondAppendResult = new byte[kOpadXorResult.length + firstHashResult.length];
        for (int i = 0; i < kOpadXorResult.length; i++) {
            secondAppendResult[i] = kOpadXorResult[i];
        }
        for (int i = 0; i < firstHashResult.length; i++) {
            secondAppendResult[i + keyArr.length] = firstHashResult[i];
        }

        /*
         * H(K XOR opad, H(K XOR ipad, text)) 对上面的数据进行哈希运算。
         */
        byte[] hmacMd5Bytes = md5(secondAppendResult);

        return hmacMd5Bytes;

    }

    /**
     * 字节转16进制
     *
     * @param src
     * @return
     */
    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 嵌套排序参数
     *
     * @param map map
     */
    private static void nestSortParameter(Map<String, Object> map) {
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        String json;
        Map parameterMap;
        for (Map.Entry<String, Object> entry : entries) {
            if (entry.getValue() instanceof JSONObject) {
                json = JSONObject.toJSONString(entry.getValue());
                parameterMap = JSONObject.parseObject(json, Map.class);
                TreeMap<String, Object> innerLayerParamsTreeMap = new TreeMap<String, Object>(parameterMap);
                entry.setValue(innerLayerParamsTreeMap);
                nestSortParameter(innerLayerParamsTreeMap);
            }
        }
    }

    private static void naturalOrdering(TreeMap<String, Object> containerMap, Map<String, Object> outerLayerParamsMap) throws JsonProcessingException {
        Set<Map.Entry<String, Object>> outerLayerParamsMapEntries = outerLayerParamsMap.entrySet();
        for (Map.Entry<String, Object> outerLayerParamsMapEntry : outerLayerParamsMapEntries) {
            Object entryValue = outerLayerParamsMapEntry.getValue();
            if (entryValue instanceof HashMap) {
                Map<String, Object> innerLayerParamsMapEntry = new ObjectMapper()
                        .readValue(JSONObject.toJSONString(entryValue), new TypeReference<Map<String, Object>>() {
                        });
                TreeMap<String, Object> innerLayerParamsTreeMap = new TreeMap<>(innerLayerParamsMapEntry);
                outerLayerParamsMapEntry.setValue(innerLayerParamsTreeMap);
                naturalOrdering(new TreeMap<>(), innerLayerParamsTreeMap);
            }
        }
    }
}
