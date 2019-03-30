/**
 * @author mazharul.islam
 *
 */
package com.example.mazharul_islam.myapplication.encryption;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.*;
import java.security.spec.*;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ECC {
	
	/**
	 * @param args
	 */
	static KeyGenerator keygen = null;
	//static SecretKey key = null;
	static Cipher  cipher = null;
	static byte[] key = null;
	//public List encryptedtSymbolsList = null;
	public ECC() {
		// constructor
		try {
			byte[] keyStart = "this is a key".getBytes();
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(keyStart);
			kgen.init(128, sr); // 192 and 256 bits may not be available
			SecretKey skey = kgen.generateKey();
			key = skey.getEncoded();
		}catch (Exception ex){
				System.err.println("While calling ECC constructor");
		}
	}


	public static byte[] decryption(byte[] encrypted){
		byte[] decrypted = null;
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			decrypted = cipher.doFinal(encrypted);
		}catch (Exception ex){
			System.err.println("While calling decryption");
		}
		return decrypted;
	}
	public static byte[] encryption(byte[] clear){
		byte[] encrypted = null;
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encrypted = cipher.doFinal(clear);
		}catch (Exception  ex){
			System.err.println("While calling encryption");
		}
		return encrypted;
	}
	

	//public static void main(String[] args) throws Exception {		
//	}
	
}
