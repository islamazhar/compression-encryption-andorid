package com.example.mazharul_islam.myapplication.file.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import com.example.mazharul_islam.myapplication.compression.Huffman;

//import classical.ClassicalHuffman;
//import ourmethod.Huffman;

public class Client {
	public static String USERNAME = "demo-user";
	public static String PASS = "demo-user";
	//final public static String IP = "103.109.52.10";
	public static String IP = "199.71.215.197";

	public Client(String _IP, String _username, String _pass){
		IP = _IP;
		USERNAME =_username;
		PASS = _pass;
	}
	/*
	 * FTPS FILE UP
	 */
	
	public long FTPSUP(String source, String des) {
		FTPSClient client = new FTPSClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		long tim1 = 0;
		long tim2 = 0;
		try {
			
			String srcFilename = source;
		//	long avg = 0;
			String filename = des;
			File file = new File(srcFilename);
			client.connect(IP,21);
			client.login(USERNAME, PASS);
			client.execPBSZ(0);
			client.execPROT("P");
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			FileInputStream srcFileStream = new FileInputStream(file);
			tim1 = System.currentTimeMillis();
			boolean status = client.storeFile(filename, srcFileStream);
			tim2 = System.currentTimeMillis();
			srcFileStream.close();
			client.disconnect();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return tim2-tim1;
		
	}
	/*
	 * FTPS FILE DOWNLOAD
	 */
	public long FTPSDownload(String source, String des) {
		FTPSClient client = new FTPSClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		long tim1 = 0;
		long tim2 = 0;
		try {
			
			String srcFilename = source;
			long avg = 0;
			String filename = des;
			File file = new File(srcFilename);
			client.connect(IP,21);
			client.login(USERNAME, PASS);
			client.execPBSZ(0);
			client.execPROT("P");
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			FileOutputStream srcFileStream = new FileOutputStream(file);
			tim1 = System.currentTimeMillis();
			boolean status = client.retrieveFile(filename, srcFileStream);
			tim2 = System.currentTimeMillis();
			srcFileStream.close();
			client.disconnect();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return tim2-tim1; 
	}
	
	/*
	 * FTP FILE UPLOAD
	 */
	public long FTPUP(String source, String des) {
		FTPClient client = new FTPClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		long tim1 = 0;
		long tim2 = 0;
		try {
			
			String srcFilename = source;
			long avg = 0;
			String filename = des;
			File file = new File(srcFilename);
			client.connect(IP,21);
			client.login(USERNAME, PASS);
			
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			FileInputStream srcFileStream = new FileInputStream(file);
			tim1 = System.currentTimeMillis();
			boolean status = client.storeFile(filename, srcFileStream);
			tim2 = System.currentTimeMillis();
			srcFileStream.close();
			client.disconnect();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return tim2-tim1;
	}
	
	/*
	 * FTP FILE DOWNLOAD
	 */
	
	public long FTPDownload (String source, String des) {
		FTPClient client = new FTPClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		long tim1 = 0;
		long tim2 = 0;
		try {
			
			String srcFilename = source;
			long avg = 0;
			String filename = des;
			File file = new File(srcFilename);
			client.connect(IP,21);
			client.login(USERNAME, PASS);
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			FileInputStream srcFileStream = new FileInputStream(file);
			tim1 = System.currentTimeMillis();
			boolean status = client.storeFile(filename, srcFileStream);
			tim2 = System.currentTimeMillis();
			srcFileStream.close();
			client.disconnect();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return tim2-tim1;
	}
	
	public static void main(String args[]) {

	//	Client client = new Client();
	//	String inputFileNamme = "test-data/1.jpg";
	//	Huffman h = new Huffman();
		
	//	System.out.println(h.compress(inputFileNamme, "test.zip"));
	//	System.out.println(client.FTPSUP(inputFileNamme,"/home/uiu/mazharul/Lab/a.txt"));
		
		//System.out.println(client.FTPSUP("test.zip","/home/mazharul-islam/512/1.jpg"));
	//	System.out.println(client.FTPUP("test.zip","/home/mazharul-islam/512/1.zip"));
		
	}
}

/***
 * 
 * sudo gedit /etc/vsftpd.conf
 * sudo systemctl restart vsftpd
 * 
 * ******/


