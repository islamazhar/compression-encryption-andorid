package com.example.mazharul_islam.myapplication.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TextFileGenerate {
	int  fileSize;
	File folder = null;
	public ArrayList<String> fileNames = null ;
	public String folderName = null;
	
	public static void main(String args[]) {
		String folderlocation = "test-data/";
		File in = new File(folderlocation+"LittleDorrit.txt");
		File out = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			System.out.println(in.length());
			for(Integer size = 256;size<=10240;size=size+1024) {
	//	for(Integer  size=1024;size<in.length() && size<=262144 ;size=size+1000) {
						fis = new FileInputStream(in);
						out = new File(folderlocation+"sizes/"+String.valueOf(size)+".txt");
						fos = new FileOutputStream(out);
					
						int i;
						int read = 0;
						byte [] b = new byte[64];
						while( (i = fis.read(b)) !=-1)  {
							//	while( (i = fis.read(b)) !=-1)  {
							fos.write(b, 0, i);
							read += i;
							if(read>=size) {
								break;
							}
						}
						fos.close();
						fis.close();
					}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
	}
		

}
