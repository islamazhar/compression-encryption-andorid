package com.example.mazharul_islam.myapplication.compression;

import com.example.mazharul_islam.myapplication.encryption.ECC;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

public class FullEncryption {
    public static ECC ecc = null;
    public static String filename = null;
    public  static BufferedOutputStream fos1 = null;
    byte[] cipherText = null;
    public FullEncryption(String _filename){
        filename = _filename;
        ecc = new ECC();
    }
    public double encrypt(){
            double t = 0;
            BinaryStdIn.takeInputFile(filename);
            String s = BinaryStdIn.readString();

            t = System.currentTimeMillis();
            cipherText = ecc.encryption(s.getBytes());
            System.err.println(new String(cipherText));
            t = System.currentTimeMillis() - t;
            // save the encrypted file
            BinaryStdOut.takeInputFile(filename+".encrypted");
            for(int i=0;i<cipherText.length;i++){
                BinaryStdOut.write(cipherText[i]);
            }
            //BinaryStdOut.write(new String(cipherText));
            BinaryStdOut.close();
            return t;

    }

    public double decrypt(){

        double t = 0;
       // BinaryStdIn.takeInputFile(filename+".encrypted");
       // String  s = BinaryStdIn.readString();
        t = System.currentTimeMillis();
        ecc.decryption(cipherText);
        t = System.currentTimeMillis()-t;
        return t;
    }
}
