package com.asiainfo.biapp.cmn.jx.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * hmacSHA算法
 *
 */
@Slf4j
public class HmacSHAEncoder {
    /**
     * 算法名称
     */
    public static final String DEFAULT_HMAC_SHA256 = "HmacSHA256";

    /**
     * hmacSHA运算
     *
     * @param message      明文输入
     * @param key          密钥
     * @param shaAlgorithm 算法 如HmacSHA256
     * @return 编码后的结果转16进制
     */
    public static String hmacSHA(String message, String key, String shaAlgorithm) {
        Mac mac = null;
        String algorithm = shaAlgorithm;
        try {
            mac = Mac.getInstance(shaAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            log.error("HmacSHAEncoder Exception: ", e);
            return null;
        }
        SecretKeySpec secretKey = null;
        try {
            secretKey = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), algorithm);
        } catch (DecoderException e) {
            log.error("DecoderException Exception: ", e);
            return null;
        }
        try {
            mac.init(secretKey);
        } catch (InvalidKeyException e) {
            log.error("InvalidKeyException Exception: ", e);
            return null;
        }

        byte[] bytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(bytes);
    }
}