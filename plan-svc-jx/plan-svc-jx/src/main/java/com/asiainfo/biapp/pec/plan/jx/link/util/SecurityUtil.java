package com.asiainfo.biapp.pec.plan.jx.link.util;

/**
 * @author mamp
 * @date 2023/2/10
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;


/**
 * AES加密解密
 */
public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
    /**
     * 秘钥
     */
    private static final String SECRET_KEY = "2C898E5B4B187D96664C1D36C975C62D";

    /**
     * AES加密
     *
     * @param content   明文
     * @param secretKey 秘钥
     * @return 密文
     */
    public static String encryptAES(String content, String secretKey) throws Exception {
        if (content == null || content.length() == 0) {
            throw new Exception("明文不能为空！");
        }
        if (secretKey == null || secretKey.length() == 0) {
            throw new Exception("密钥不能为空！");
        }
        byte[] encryptResult = encrypt(content, secretKey);
        String encryptResultStr = parseByte2HexStr(encryptResult);
        return encryptResultStr;
    }

    /**
     * AES加密 使用默认秘钥
     *
     * @param content 明文
     * @return
     * @throws Exception
     */
    public static String encryptAES(String content) throws Exception {
        return encryptAES(content, SECRET_KEY);
    }

    /**
     * AES解密
     *
     * @param encryptStr 密文
     * @param secretKey  秘钥
     * @return 明文
     */
    public static String decryptAES(String encryptStr, String secretKey) throws Exception {
        if (encryptStr == null || encryptStr.length() == 0) {
            throw new Exception("密文不能为空");
        }
        if (secretKey == null || secretKey.length() == 0) {
            throw new Exception("密钥不能为空！");
        }
        try {
            byte[] decryptFrom = parseHexStr2Byte(encryptStr);
            byte[] decryptResult = decrypt(decryptFrom, secretKey);
            return new String(decryptResult);
        } catch (Exception e) {
            logger.error("AES解密异常", e);
        }
        return null;
    }

    /**
     * AES解密 使用默认秘钥
     *
     * @param encryptStr 密文
     * @return
     * @throws Exception
     */
    public static String decryptAES(String encryptStr) throws Exception {
        return decryptAES(encryptStr, SECRET_KEY);
    }

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    private static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // 防止linux下 随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            logger.error("AES加密异常", e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    private static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // 防止linux下 随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kgen.init(128, secureRandom);
            // kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            logger.error("解密异常", e);
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String content = "IOP2023";
        System.out.println("原内容为：" + content);

        // 加密
        String encryContent = encryptAES(content);
        System.out.println("加密后的内容为：" + encryContent);

        // 解密
        String decryContent = decryptAES(encryContent);
        System.out.println("解密后的内容为：" + decryContent);
    }


}