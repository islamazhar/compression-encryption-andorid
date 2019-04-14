package com.example.mazharul_islam.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.mazharul_islam.myapplication.compression.FullEncryption;
import com.example.mazharul_islam.myapplication.compression.Huffman;
import com.example.mazharul_islam.myapplication.compression.RankOrderHuffman;
import com.example.mazharul_islam.myapplication.file.transfer.Client;
import com.example.mazharul_islam.myapplication.file.transfer.DropboxClient;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class MainActivity extends AppCompatActivity {
    public EditText serverIP, username, password, fileSize, dest;
    public static boolean hcomecc = true;
    public static boolean ftps = false;
    public static boolean ranked = false;
    public static final int times = 1;
    public static String sadResults = null;
   // public static String sadResultLocation  = "/home/mazharul-islam/Lab/summary.mobie.txt";
    public  static PrintStream sadPs = null;
    Context cur= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = this.getCurrentFocus();
        if (view != null) { //Removing on screen keyboard if still active
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        serverIP = findViewById(R.id.IPaddress);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        fileSize = findViewById(R.id.size);

        serverIP.setText("199.71.215.197");
        username.setText("demo-user");
        password.setText("demo-user");
        fileSize.setText("32768");

        hcomecc = false;
        ftps = true;
        ranked = false;
        // ask for file read permissions...
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        writeLine("method,size,time,direction\n");
        // initially download the files you need...

        for(int size=1024;size <= 10240;size+=1024) {
            String source = getApplicationContext().getFilesDir()+"/"+size+".sh";
            String destination = "/upload/"+size+".sh";
            new PCDN("Download",0,getApplicationContext()).execute("199.71.215.197","demo-user","demo-user",source,destination);
        }
        //new PCDN(true,size,0,getApplicationContext()).execute(address,u,p,source,destination);
        //Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
    }
    public void hcomecc (String address, String u, String p, String size, boolean upload){
        String source = getApplicationContext().getFilesDir()+"/"+size+".sh";
        String destination = "/upload/"+size+".sh.server";
        try {
            Huffman huffman = new Huffman();
            for (int i = 0; i < times; i++) {
                String compressedFile = source+".huffman";
                String enCompressedFile = source+".huffman.encrypted";
                String decryptedCompressedFile = compressedFile+".again";
                String outFile = source+".again.txt";
                double t = 0;
                double compressedTime = huffman.compress(source,compressedFile,enCompressedFile);
                double decompressedTime = huffman.deCompress(enCompressedFile, decryptedCompressedFile,outFile);

                if (upload) {
                    t += compressedTime;
                    new PCUP (size, t, getApplicationContext()).execute(address, u, p, enCompressedFile, destination);
                } else {
                    t += decompressedTime;
                    new PCDN (size, t, getApplicationContext()).execute(address, u, p, enCompressedFile, destination);
                }

                //Toast.makeText(getApplicationContext(), ("Compressed and decompressed in = " + t + " s"), Toast.LENGTH_LONG).show();
            }
        } catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void helios (String address, String u, String p, String size, boolean upload) {
        String source = getApplicationContext().getFilesDir()+"/"+size+".sh";
        String destination = "/upload/"+size+".sh.server";
        try {
            RankOrderHuffman helios = new RankOrderHuffman();
            for (int i = 0; i < times; i++) {
                String compressedFile = source+".helios";
                String outFile = source+".again.txt";
                double t = 0 ;
                double compressedTime = helios.compress(source, compressedFile);
                double decompressedTime = helios.deCompress(compressedFile, outFile);
                if (upload) {
                    t += compressedTime;
                    new PCUP(size, t, getApplicationContext()).execute(address, u, p, compressedFile, destination);
                } else {
                    t += decompressedTime;
                    new PCDN(size, t, getApplicationContext()).execute(address, u, p, compressedFile, destination);
                }
                //Toast.makeText(getApplicationContext(), ("Compressed and decompressed in = " + t + " s"), Toast.LENGTH_LONG).show();
            }
        } catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void fullEncrytption  (String address, String u, String p, String size, boolean upload) {
        String source = getApplicationContext().getFilesDir()+"/"+size+".sh";
        String destination = "/upload/"+size+".sh.server";

        // full encryption
        try{
            String encryptedFile = source+".en";
            FullEncryption fullEncryption = new FullEncryption(source,encryptedFile);
            for(int i=0;i<times;i++) {
                double t = 0;
                double encryptionTime = fullEncryption.encrypt();
                double decryptionTime = fullEncryption.decrypt();
                if (upload) {
                    t += encryptionTime;
                    new PCUP (size, t, getApplicationContext()).execute(address, u, p, encryptedFile, destination);
                }
                else {
                    t += decryptionTime;
                    new PCDN (size, t, getApplicationContext()).execute(address, u, p, encryptedFile, destination);
                }
                //Toast.makeText(getApplicationContext(), ("Full Encryption in = " + t + " s"), Toast.LENGTH_LONG).show();
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void up(View v) {
        String address = serverIP.getText().toString();
        String u = username.getText().toString();
        String p = password.getText().toString();
        String size = fileSize.getText().toString();

        if(hcomecc == true) {
            hcomecc(address,u,p,size,true);
        }

        else if(ranked == true){
           helios(address,u,p,size,true);
        }
        else if(ftps == true){
          fullEncrytption(address,u,p,size,true);
        }
        else{
            Toast.makeText(getApplicationContext(), "choose a method", Toast.LENGTH_LONG).show();
        }
    }

    public void showToast(String str){
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }
    public void writeLine(String line){
        try {
            sadResults = getApplicationContext().getFilesDir()+"/sadResults.csv";
            FileOutputStream fos = new FileOutputStream(new File(sadResults), true);
            sadPs = new PrintStream(new BufferedOutputStream(fos));
            sadPs.append(line);
            sadPs.close();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void down(View v) {
        String address = serverIP.getText().toString();
        String u = username.getText().toString();
        String p = password.getText().toString();
        String size = fileSize.getText().toString();

        if (hcomecc == true) {
            hcomecc(address,u,p,size,false);
        }

        else if (ranked == true) {
            helios(address,u,p,size,false);
        }
        else if (ftps == true) {
            fullEncrytption(address,u,p,size,false);
        }
        else {
            Toast.makeText(getApplicationContext(), "choose a method", Toast.LENGTH_LONG).show();
        }
    }

    public  void onRadioButtonClicked (View view){
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_ftps:
                if (checked) {
                    hcomecc = ranked = false;
                    ftps = true;
                    break;
                }
            case R.id.radio_hcomecc:
                if (checked) {
                    hcomecc = true;
                    ftps = ranked = false;
                    break;
                }
            case R.id.radio_ourmethod:
                if (checked) {
                    ranked = true;
                    ftps = hcomecc = false;
                    break;
                }
        }
    }

    private  class PCUP extends AsyncTask<String, Void, Void> {
        public  double ctime = 0;
        public String size = "0";
        Context context = null;
        public long s = 0;
        public PCUP (String _size, double _ctime, Context _context){
            size = _size;
            ctime = _ctime;
            context = _context;
        }
        long time = 0;
        protected Void doInBackground (String ... str) {
                //Client  client = new Client(str[0],str[1],str[2]);
            DropboxClient client = new DropboxClient();
            try {
                    if (ftps == true) {
                        time = client.HTTPUP(str[3],str[4]);
                    }
                    else if (hcomecc == true) {
                        time = client.HTTPUP(str[3],str[4]); // should be FTP
                    }
                    else {
                        time = client.HTTPUP(str[3],str[4]);
                    }
                  //  acvtime+=time;
                } catch (Exception ex) {
                    showToast(ex.toString());
                    //Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
                return null;
        }

        protected  void onPostExecute(Void param) {

            try {
                String method = "None";
                if(ftps==true){
                    method = "FTPS";
                }
                else if(hcomecc == true){
                    method = "hcomecc";
                }
                else{
                    method = "HEliOS";
                }
                String line = method + "," + size+","+ ctime + "," +time+",up\n";
                writeLine(line);
                Toast.makeText(context, line, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private  class PCDN extends AsyncTask<String, Void, Void> {
        public  double ctime = 0;
        public String size = "0";
        Context context = null;
        public long s = 0;
        public PCDN (String _size, double _ctime, Context _context){
            size = _size;
            ctime = _ctime;
            context = _context;
        }

        long time = 0 ;
        protected Void doInBackground(String ... str){
            if(size.equals("Download")) {
                 Client client = new Client(str[0],str[1],str[2]);
                try {
                    if (ftps == true) {
                        time = client.FTPSDownload(str[3], str[4]);
                    } else if (hcomecc == true) {
                        time = client.FTPSDownload(str[3], str[4]); // should be FTP
                    } else {
                        time = client.FTPSDownload(str[3], str[4]);
                    }
                } catch (Exception ex) {
                    //showToast(ex.toString());
                    //Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
            else {
                // Client client = new Client(str[0],str[1],str[2]);
                DropboxClient client = new DropboxClient();
                try {
                    if (ftps == true) {
                        time = client.HTTPDN(str[3], str[4]);
                    } else if (hcomecc == true) {
                        time = client.HTTPDN(str[3], str[4]); // should be FTP
                    } else {
                        time = client.HTTPDN(str[3], str[4]);
                    }
                } catch (Exception ex) {
                    //showToast(ex.toString());
                    //Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
            return null;
        }

        protected  void onPostExecute(Void param) {
            try {
                String method = "None";
                if (ftps==true) {
                    method = "FTPS";
                }
                else if (hcomecc == true) {
                    method = "hcomecc";
                }
                else {
                    method = "HEliOS";
                }
                String line = method + "," + size+","+ ctime + "," +time+",down\n";
                writeLine(line);

                Toast.makeText(context, line, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}