package com.example.mazharul_islam.myapplication.compression;

import com.example.mazharul_islam.myapplication.encryption.ECC;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

public class FullEncryption {
    public static String plainFile = null;
    public static String cipherFile = null;
    public  static  ECC ecc = null;
    public FullEncryption(String _plainFile, String _cipherFile){
        plainFile = _plainFile;
        cipherFile = _cipherFile;
        ecc = new ECC();
    }
    public double encrypt(){
        double t = 0;
        t = ecc.encryption(plainFile,cipherFile);
        return t;
    }

    public double decrypt(){
        double t = 0;
        t = ecc.decryption(cipherFile,plainFile);
        return t;
    }
}
