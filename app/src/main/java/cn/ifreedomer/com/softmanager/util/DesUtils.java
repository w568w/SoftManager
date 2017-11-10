package cn.ifreedomer.com.softmanager.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class DesUtils {
	private static String key = "S1Dc9BDS";
	private static String[] key1 = { "UkE", "Wmc" };
	private static String[] key2 = { "WXc", "VUE" };
	private static String[] key3 = { "Wmc", "ZGc" };
	private static String[] key4 = { "TUE", "TkE" };

	// 加密方法
	public static String encryptDES(String encryptString, String encryptKey) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(encryptKey.getBytes());
		DESKeySpec desKey = new DESKeySpec(encryptKey.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, securekey, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
		return Base64.encodeToString(encryptedData, Base64.DEFAULT);
	}

	// 解密方法
	public static String decryptDES(String decryptString, String decryptKey) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(decryptKey.getBytes());
		byte[] byteMi = Base64.decode(decryptString, Base64.DEFAULT);
		DESKeySpec desKey = new DESKeySpec(decryptKey.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, securekey, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);
		return new String(decryptedData);
	}
	

	public static String getKey1() {
		return key;
	}

	public static String getKey2() {
		for (byte b : key.getBytes()) {
			byte[] bytes = new byte[1];
			bytes[0] = b;
			String aa = Base64.encodeToString(bytes, Base64.DEFAULT);
			String bb = aa.substring(0, 3);
			String cc = Base64.encodeToString(bb.getBytes(), Base64.DEFAULT);
			String dd = cc.substring(0, 3);
			dd = dd.toString();
		}
		return key;
	}

	public static String getKey3() {
		for (byte b : key.getBytes()) {
			byte[] bytes = new byte[1];
			bytes[0] = b;
			String aa = Base64.encodeToString(bytes, Base64.DEFAULT);
			String bb = aa.substring(0, 3);
			String cc = Base64.encodeToString(bb.getBytes(), Base64.DEFAULT);
			String dd = cc.substring(0, 3);
			dd = dd.toString();
		}
		String k1 = new String(Base64.decode(new String(Base64.decode(key1[0] + "=", Base64.DEFAULT)) + "==", Base64.DEFAULT));
		String k2 = new String(Base64.decode(new String(Base64.decode(key1[1] + "=", Base64.DEFAULT)) + "==", Base64.DEFAULT));
		String k3 = new String(Base64.decode(new String(Base64.decode(key2[0] + "=", Base64.DEFAULT)) + "==", Base64.DEFAULT));
		String k4 = new String(Base64.decode(new String(Base64.decode(key2[1] + "=", Base64.DEFAULT)) + "==", Base64.DEFAULT));
		String k5 = new String(Base64.decode(new String(Base64.decode(key3[0] + "=", Base64.DEFAULT)) + "==", Base64.DEFAULT));
		String k6 = new String(Base64.decode(new String(Base64.decode(key3[1] + "=", Base64.DEFAULT)) + "==", Base64.DEFAULT));
		String k7 = new String(Base64.decode(new String(Base64.decode(key4[0] + "=", Base64.DEFAULT)) + "==", Base64.DEFAULT));
		String k8 = new String(Base64.decode(new String(Base64.decode(key4[1] + "=", Base64.DEFAULT)) + "==", Base64.DEFAULT));
		return k1 + k2 + k3 + k4 + k5 + k6 + k7 + k8;
	}

}
