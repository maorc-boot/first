package com.asiainfo.biapp.pec.plan.jx.link.util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESEncryptUtil {

    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception {
        return StringUtils.isEmpty(base64Code) ? null : Base64.decodeBase64(base64Code);
    }

    public static byte[] aesEncryptToBytes(String content, byte[] encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgenInit(kgen, encryptKey);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, getAESKey(encryptKey)));
    }

    public static String aesDecryptByBytes(byte[] encryptBytes, byte[] decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgenInit(kgen, decryptKey);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes,"utf-8");
    }

    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), getAESKey(decryptKey));
    }

    public static byte[] getAESKey(String encodingAESKey){
        byte[] array = Base64.decodeBase64(encodingAESKey+"=");
        return array;
    }

    /**
     **防止在linux下随机生成key
     * @param kgen
     * @param bytes
     * @throws NoSuchAlgorithmException
     */
    public static void kgenInit(KeyGenerator kgen, byte[] bytes)
            throws NoSuchAlgorithmException {
        //1.防止linux下 随机生成key
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
        secureRandom.setSeed(bytes);
        //2.根据密钥初始化密钥生成器
        kgen.init(128, secureRandom);
    }

    public static void main(String[] args) throws Exception {

        /** 测试线secret_key */
        String encoding_aes_key="88assadsfsdfsffsf6dsfsdfd";

        /** 发送短信接口 */
        //String content = "{\"mobiles\":[\"19800000011\"],\"templateId\":45,\"placeHolderContent\":{\"{[placeholder:url]}\":\"https://www.baidu.com\"}}";

        /** 转短链接接口 */
        String content = "{\"srcUrl\":\"https://www.baidu.com\",\"templateId\":45}";
        System.out.println("原始请求参数:" + content);

        /** 参数加密 */
        String encrypt = aesEncrypt(content, encoding_aes_key);
        System.out.println("加密后:" + encrypt);

        /** 参数解密 */
        String decrypt = aesDecrypt(encrypt, encoding_aes_key);
        System.out.println("解密后:" + decrypt);
    }
}