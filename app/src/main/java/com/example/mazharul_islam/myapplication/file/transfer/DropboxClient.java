package com.example.mazharul_islam.myapplication.file.transfer;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DropboxClient {
    private static final String ACCESS_TOKEN = "MsW4mTV8XWAAAAAAAAAPYIDVhCoMOfwjnZdoWMJfRb02eD76hHiczOQjXsfY7HX3";
    DbxClientV2 client = null;
    public DropboxClient() {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        client = new DbxClientV2(config, ACCESS_TOKEN);
    }
    public long HTTPUP(String source, String des) {
        try {
            InputStream in = new FileInputStream(source);
            FileMetadata metadata = client.files().uploadBuilder(des)
                    .uploadAndFinish(in);
            // Now delete the file
            in.close();
            client.files().deleteV2(des);
        } catch ( Exception ex){
           ex.printStackTrace();
        }
        return 0;
    }
    public long HTTPDN(String source, String des) {
        try {
            // Upload the file
            InputStream in = new FileInputStream(source);
            FileMetadata metadata = client.files().uploadBuilder(des)
                    .uploadAndFinish(in);
            in.close();
            //  downaload the file
            OutputStream os = new FileOutputStream(source);
            metadata = client.files().downloadBuilder(des).download(os);
            os.close();
             // delete the file
            client.files().deleteV2(des);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
   // public static void main(String args[]) {

   // }
}
