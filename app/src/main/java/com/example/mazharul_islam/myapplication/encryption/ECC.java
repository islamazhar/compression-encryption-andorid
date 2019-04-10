/**
 * @author mazharul.islam
 *
 */
package com.example.mazharul_islam.myapplication.encryption;



import com.example.mazharul_islam.myapplication.compression.BinaryStdIn;
import com.example.mazharul_islam.myapplication.compression.BinaryStdOut;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
	//static KeyGenerator keygen = null;
	//static SecretKey key = null;
	 Cipher  cipher = null;
	 byte[] key = null;
	public static int BLOCK_SIZE = 1024;
	 SecretKeySpec skeySpec = null;
	//public List encryptedtSymbolsList = null;
	public ECC() {
		// constructor
		try {
			byte[] keyStart = "this is a key".getBytes();
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(keyStart);
			kgen.init(192, sr); // 192 and 256 bits may not be available
			SecretKey skey = kgen.generateKey();
			key = skey.getEncoded();
			skeySpec = new SecretKeySpec(key, "AES");
			cipher = Cipher.getInstance("AES");
		}catch (Exception ex){
			System.err.println("While calling ECC constructor");
			ex.printStackTrace();
		}
	}

	public  long encryption (String plainTextFile, String encryptedTextFile) {

        long starTime = System.currentTimeMillis();
	    BinaryStdIn.takeInputFile(plainTextFile);
		BinaryStdOut.takeInputFile(encryptedTextFile);

		//System.out.println(plainText.length);
		try {
			//System.out.println(plainText.length);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte [] plainText = BinaryStdIn.readAllBytes();
			for(int i=0;i<plainText.length;i+=BLOCK_SIZE) {
				int s = i;
				int e = Math.min(s + BLOCK_SIZE, plainText.length);
				//System.out.println(s+" - "+ e+" "+(e-s+1));
				byte[] tempPlainText = Arrays.copyOfRange(plainText, s, e);
				//System.out.println(new String(tempPlainText));
				ECC ecc = new ECC();
				byte[] tempCipherText = ecc.doEncryption(cipher,tempPlainText);
				BinaryStdOut.write(tempCipherText.length);
				//System.out.println(tempCipherText.length);
				//System.out.println("Writing");
				for(int j=0;j<tempCipherText.length;j++){
					BinaryStdOut.write(tempCipherText[j]);
				}
			}
			BinaryStdOut.write(-1);
		}catch(Exception ex) {
			System.out.println("Encrypting huffman tree!");
			System.out.println(ex.toString());
		}

		BinaryStdOut.close();
		BinaryStdIn.close();
        long endTime = System.currentTimeMillis();
		return endTime - starTime;
	}
    public  byte[] doDecryption(Cipher cipher, byte[] plainText ){
        try {
            //cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] tempCipherText = cipher.doFinal(plainText);
            return tempCipherText;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public  byte[] doEncryption(Cipher cipher, byte[] plainText ){
        try {
            //cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] tempCipherText = cipher.doFinal(plainText);
            return tempCipherText;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public long decryption(String encryptedFileName, String decryptedFileName) {
        long starTime = System.currentTimeMillis();
	    BinaryStdIn.takeInputFile(encryptedFileName);
        BinaryStdOut.takeInputFile(decryptedFileName);
        int length = 0;
        try {
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            while((length = BinaryStdIn.readInt())!=-1){
                byte [] cipherText = new byte[length];
                for(int i=0;i<cipherText.length;i++) {
                    cipherText[i]  = BinaryStdIn.readByte();
                }
                ECC ecc = new ECC();
                byte[] tempPlainText = ecc.doDecryption(cipher,cipherText);
                for(int i=0;i<tempPlainText.length;i++){
                    BinaryStdOut.write(tempPlainText[i]);
                }
            }

        }catch(Exception ex) {
            System.out.println("Decryption huffman tree!");
            System.out.println(ex.toString());
        }
        BinaryStdOut.close();
        BinaryStdIn.close();
        long endTime = System.currentTimeMillis();
        return endTime - starTime;
    }



	public static void main(String[] args) throws Exception {
	}

}