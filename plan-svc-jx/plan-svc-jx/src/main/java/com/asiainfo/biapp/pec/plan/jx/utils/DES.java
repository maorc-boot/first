package com.asiainfo.biapp.pec.plan.jx.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 与Coc页面集成,传用户ID DES加密工作类
 */
public class DES {
	
	protected final Log log = LogFactory.getLog(getClass());

	public static final int _DES = 1;
	public static final int _DESede = 2;
	public static final int _Blowfish = 3;

	private Cipher p_Cipher;
	private SecretKey p_Key;
	private String p_Algorithm;
	private static DES _instance;
	private static String hexKey = "B5584A5D9B61C23BE52CA1168C9110894C4FE9ABC8E9F251";// 密钥

	private void selectAlgorithm(int al) {
		switch (al) {
		default:
		case 1:
			this.p_Algorithm = "DES";
			break;
		case 2:
			this.p_Algorithm = "DESede";
			break;
		case 3:
			this.p_Algorithm = "Blowfish";
			break;
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param algorithm
	 *            加密算法类型，取值为1,2,3其中一个，对应为DES，DESede，Blowfish
	 * @throws Exception
	 */
	public DES(int algorithm) throws Exception {
		this.selectAlgorithm(algorithm);
		//Security.addProvider(new com.sun.crypto.provider.SunJCE());
		this.p_Cipher = Cipher.getInstance(this.p_Algorithm);
	}


	private SecretKey checkKey() {
		try {
			if (this.p_Key == null) {
				KeyGenerator keygen = KeyGenerator
						.getInstance(this.p_Algorithm);
				/*
				 * SecureRandom sr = new SecureRandom(key.getBytes());
				 * keygen.init(168, sr);
				 */
				this.p_Key = keygen.generateKey();
			}
		} catch (Exception nsae) {
		}
		return this.p_Key;
	}

	private void setKey(byte[] enckey) {
		this.p_Key = new SecretKeySpec(enckey, this.p_Algorithm);
	}

	private byte[] encode(byte[] data) throws Exception {
		this.p_Cipher.init(Cipher.ENCRYPT_MODE, this.checkKey());
		return this.p_Cipher.doFinal(data);
	}

	private byte[] decode(byte[] encdata, byte[] enckey) throws Exception {
		this.setKey(enckey);
		this.p_Cipher.init(Cipher.DECRYPT_MODE, this.p_Key);
		return this.p_Cipher.doFinal(encdata);
	}

	private String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int i = 0; i < b.length; i++) {
			stmp = Integer.toHexString(b[i] & 0xFF);
			if (stmp.length() == 1) {
				hs += "0" + stmp;
			} else {
				hs += stmp;
			}
		}
		return hs.toUpperCase();
	}

	private byte[] hex2byte(String hex) throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			log.error("hex:" + hex + "\nlength:" + hex.length());
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}

	/**
	 * 加密字符串
	 * 
	 * @param s
	 *            待加密的字符串
	 * @return String 加密后的字符串
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("static-access")
	public static String encrypt(String s) throws Exception {
		// byte[] key; //密钥文件(byte)
		if (null == _instance) {
			_instance = new DES(DES._DESede);
		}
		// key = _instance.getKey();
		_instance.setKey(_instance.hex2byte(_instance.hexKey));
		// String hexkey = _instance.byte2hex(key); //生成十六进制密钥
		byte[] enc = _instance.encode(s.getBytes()); // 生成加密文件(byte)
		String hexenc = _instance.byte2hex(enc);
		return hexenc;
	}

	/**
	 * 解密字符串
	 * 
	 * @param s
	 *            待解密的字符串
	 * @return String 解密后的字符串
	 * @throws Exception
	 * 
	 * 
	 */
	@SuppressWarnings("static-access")
	public static String decrypt(String s) throws Exception {
		if (null == _instance) {
			_instance = new DES(DES._DESede);
		}
		return new String(_instance.decode(_instance.hex2byte(s), _instance
				.hex2byte(_instance.hexKey)));
	}

}
