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


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {
    public EditText serverIP, username, password, fileSize, dest;
    public static boolean hcomecc = true;
    public static boolean ftps = false;
    public static boolean ranked = false;
    public static final int times = 1;
    public static String sadResults = null;
    public static String sadResultLocation  = "/home/mazharul-islam/Lab/summary.mobie.txt";
    public  static  BufferedOutputStream sadFos = null;
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
        fileSize.setText("4096");

        hcomecc = false;
        ftps = true;
        ranked = false;
        // ask for file read permissions.
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        try {
            sadResults = getApplicationContext().getFilesDir()+"/sadResults.csv";
            sadFos = new BufferedOutputStream(new FileOutputStream(sadResults));
            String header = "method , size , time\n";
            sadFos.write(header.getBytes());
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void up(View v) {
        String address = serverIP.getText().toString();
        String u = username.getText().toString();
        String p = password.getText().toString();
        String size = fileSize.getText().toString();


        String source = getApplicationContext().getFilesDir()+"/"+size+".sh";
        String destination = "/upload/"+size+".sh";

        if(hcomecc == true) {
            try {
                Huffman huffman = new Huffman();
                for (int i = 0; i < times; i++) {
                    String compressedFile = source+".zip";
                    String enCompressedFile = source+".zip.encrypted";
                    String decryptedCompressedFile = compressedFile+".again";
                    String outFile = source+".again.txt";
                    double t = huffman.compress(source,compressedFile,enCompressedFile);
                    t += huffman.deCompress(enCompressedFile, decryptedCompressedFile,outFile);
                    //Toast.makeText(getApplicationContext(), ("Compressed and decompressed in = " + t + " s"), Toast.LENGTH_LONG).show();
                    new PCUP(true, size, t, getApplicationContext()).execute(address, u, p, enCompressedFile, outFile);
                }
            }catch(Exception ex){
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
        }

        else if(ranked == true){
            try {
                RankOrderHuffman helios = new RankOrderHuffman();
                for (int i = 0; i < times; i++) {
                    String compressedFile = source+".zip";
                    String outFile = source+".again.txt";
                    double t = helios.compress(source, compressedFile);
                    t += helios.deCompress(compressedFile, outFile);
                    //Toast.makeText(getApplicationContext(), ("Compressed and decompressed in = " + t + " s"), Toast.LENGTH_LONG).show();
                    new PCUP(true, source, t, getApplicationContext()).execute(address, u, p, source+".zip", outFile);
                }
            }catch(Exception ex){
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else if(ftps == true){
            // full encryption
            try{
                String encryptedFile = source+".en";
                FullEncryption fullEncryption = new FullEncryption(source,encryptedFile);
                for(int i=0;i<times;i++) {
                   //double t = 0;
                     double t = fullEncryption.encrypt();
                    t += fullEncryption.decrypt();
                    new PCUP(true, source, t, getApplicationContext()).execute(address, u, p, encryptedFile, destination);
                   // Toast.makeText(getApplicationContext(), ("Full Encryption in = " + t + " s"), Toast.LENGTH_LONG).show();
                }
            }catch(Exception ex){
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "choose a method", Toast.LENGTH_LONG).show();
        }
    }
    public  void getResults(View view){

        try{
            sadFos.close();
            new PCUP(false,"0",0,getApplicationContext()).execute("192.168.0.102", "mazharul-islam", "r", sadResults, sadResultLocation);
        }catch (Exception ex){
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void showToast(String str){
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }

    public void down(View v) {
        String address = serverIP.getText().toString();
        String u = username.getText().toString();
        String p = password.getText().toString();
        String size = fileSize.getText().toString();
        String destination = getApplicationContext().getFilesDir()+"/"+size+".sh";
        String source = "/upload/"+size+".sh";

        new PCDN(true,size,0,getApplicationContext()).execute(address,u,p,source,destination);
        //Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
    }
    public  void onRadioButtonClicked(View view){
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
        boolean flag = true;
        public  double ctime = 0;
        public String size = "0";
        Context context = null;
        public long s = 0;
        public PCUP(boolean f, String _size, double _ctime, Context _context){
            flag = f;
            size = _size;
            ctime = _ctime;
            context = _context;
        }

        long time = 0 ;
        protected Void doInBackground(String ... str){

                Client client = new Client(str[0],str[1],str[2]);
                try {


                    if(ftps == true) {
                        time = client.FTPSUP(str[3],str[4]);
                    }
                    else if(hcomecc == true){
                        time = client.FTPSUP(str[3],str[4]); // should be FTP
                    }
                    else{
                        time = client.FTPUP(str[3],str[4]);
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

            if (flag == false) return;
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

                String line = method + " , " + size+" , "+ ctime + " , " +time+"\n";
                sadFos.write(line.getBytes());
                Toast.makeText(context, line, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private  class PCDN extends AsyncTask<String, Void, Void> {
        boolean flag = true;
        public  double ctime = 0;
        public String size = "0";
        Context context = null;
        public long s = 0;
        public PCDN(boolean f, String _size, double _ctime, Context _context){
            flag = f;
            size = _size;
            ctime = _ctime;
            context = _context;
        }

        long time = 0 ;
        protected Void doInBackground(String ... str){

            Client client = new Client(str[0],str[1],str[2]);
            try {


                if(ftps == true) {
                    time = client.FTPSDownload(str[4],str[3]);
                }
                else if(hcomecc == true){
                    time = client.FTPSDownload(str[4],str[3]); // should be FTP
                }
                else {
                    time = client.FTPSDownload(str[4], str[3]);
                }
            } catch (Exception ex) {
                //showToast(ex.toString());
                //Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
            return null;
        }

        protected  void onPostExecute(Void param) {

            if (flag == false) return;
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

                String line = method + " , " + size+" , "+ ctime + " , " +time+"\n";
                sadFos.write(line.getBytes());
                Toast.makeText(context, line, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}



