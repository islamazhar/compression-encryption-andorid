/**
 * @author mazharul.islam
 *
 */
package com.example.mazharul_islam.myapplication.encryption;



import com.example.mazharul_islam.myapplication.compression.BinaryStdIn;
import com.example.mazharul_islam.myapplication.compression.BinaryStdOut;

import org.spongycastle.jce.ECNamedCurveTable;
import  org.spongycastle.jce.spec.ECParameterSpec;


import java.util.Arrays;
import java.security.*;


import javax.crypto.Cipher;


public class ECC {

	/**
	 * @param args
	 */
	 Cipher  cipher = null;
	 public static final int BLOCK_SIZE = 256;
	 PrivateKey privKey = null;
	 PublicKey pubKey = null;
	public ECC() {
		// constructor
		try {
			Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECDSA","SC");
			kpg.initialize(ecSpec, new SecureRandom());
			KeyPair kpU = kpg.generateKeyPair();
			this.privKey = kpU.getPrivate();
			this.pubKey = kpU.getPublic();
			cipher = Cipher.getInstance("ECIES","SC");
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
			cipher.init(Cipher.ENCRYPT_MODE, this.pubKey, new SecureRandom());
            byte [] plainText = BinaryStdIn.readAllBytes();
			for(int i=0;i<plainText.length;i+=BLOCK_SIZE) {
				int s = i;
				int e = Math.min(s + BLOCK_SIZE, plainText.length);
				//System.out.println(s+" - "+ e+" "+(e-s+1));
				byte[] tempPlainText = Arrays.copyOfRange(plainText, s, e);
				//System.out.println(new String(tempPlainText));
				ECC ecc = new ECC();
				byte[] tempCipherText = doEncryption(cipher,tempPlainText);
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
            cipher.init(Cipher.DECRYPT_MODE, privKey, new SecureRandom());
            while((length = BinaryStdIn.readInt())!=-1){
                byte [] cipherText = new byte[length];
                for(int i=0;i<cipherText.length;i++) {
                    cipherText[i]  = BinaryStdIn.readByte();
                }
                ECC ecc = new ECC();
                byte[] tempPlainText = doDecryption(cipher,cipherText);
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



//	public static void main(String[] args) throws Exception {
//		ECC ecc = new ECC();
//	}

}