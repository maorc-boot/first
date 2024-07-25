package com.asiainfo.biapp.pec.approve.jx.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DesUtils {
	

	 /**
     * 转换成十六进制字符串
     * @param key
     * @return
     *
     * daixin on 2018-08-08 10:51:44
     */
    public static byte[] hex(String key){  
        String f = DigestUtils.md5Hex(key);
        byte[] bkeys = new String(f).getBytes();  
        byte[] enk = new byte[24];  
        for (int i=0;i<24;i++){  
            enk[i] = bkeys[i];  
        }  
        return enk;  
    }
    
    /**
     * 3DES加密
     * @param key 密钥，24位
     * @param srcStr 将加密的字符串
     * @return
     *
     * daixin on 2018-08-08 10:51:44
     */
    public static String  encode3Des(String key,String srcStr){  
    	
        	byte[] keybyte = hex(key);
        	try {  
        	byte[] src = srcStr.getBytes("utf-8");
           //生成密钥  
           SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
           //加密  
           Cipher c1 = Cipher.getInstance("DESede");
           c1.init(Cipher.ENCRYPT_MODE, deskey);  
           
           String pwd = Base64.encodeBase64String(c1.doFinal(src));
//           return c1.doFinal(src);//在单一方面的加密或解密  
           return pwd;
       } catch (java.security.NoSuchAlgorithmException e1) {  
       }catch(javax.crypto.NoSuchPaddingException e2){  
       }catch(Exception e3){
       }  
       return null;  
   }
    
   /**
    * 3DES解密
    * @param key 加密密钥，长度为24字节  
    * @param desStr 解密后的字符串
    * @return
    *
    * daixin on 2018-08-08 10:51:44
    */ 
    public static String decode3Des(String key, String desStr){  
    	Base64 base64 = new Base64();
    	byte[] keybyte = hex(key);
    	byte[] src = base64.decode(desStr);
    			
        try {  
            //生成密钥  
            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");  
            //解密  
            Cipher c1 = Cipher.getInstance("DESede");  
            c1.init(Cipher.DECRYPT_MODE, deskey);  
            String pwd = new String(c1.doFinal(src),"utf-8");
//            return c1.doFinal(src);  
            return pwd;
        } catch (java.security.NoSuchAlgorithmException e1) {  
        }catch(javax.crypto.NoSuchPaddingException e2){  
        }catch(Exception e3){
        }  
        return null;          
    }
    
}

