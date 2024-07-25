package com.asiainfo.biapp.pec.plan.jx.link.util;

/**
 * @author mamp
 * @date 2023/2/10
 */
public class Test {
    public static void main(String[] args) throws Exception {
        // 私钥,加密和解密要使用相同的私钥,双方提前约定好
        String secretKey = "2C898E5B4B187D96664C1D36C975C62D";

        // 原始内容
        String content = "IOP测试AES加密";
        System.out.println("原始内容: " + content);

        // AES加密后的内容
        String encryptContent = SecurityUtil.encryptAES(content, secretKey);
        System.out.println("加密后的内容: " + encryptContent);

        // 解密后的内容
        String decryptContent = SecurityUtil.decryptAES(encryptContent, secretKey);
        System.out.println("解密后的内容: " + decryptContent);

    }
}
