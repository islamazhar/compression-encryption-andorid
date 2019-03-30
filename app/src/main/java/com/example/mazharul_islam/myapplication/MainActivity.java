package com.example.mazharul_islam.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.mazharul_islam.myapplication.compression.FullEncryption;
import com.example.mazharul_islam.myapplication.compression.Huffman;
import com.example.mazharul_islam.myapplication.compression.RankOrderHuffman;
import com.example.mazharul_islam.myapplication.encryption.ECC;
import com.example.mazharul_islam.myapplication.file.transfer.Client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {
    public EditText ip, username, password, spath, dest;
    public static boolean hcomecc = true;
    public static boolean ftps = false;
    public static boolean ranked = false;
    public static final int times = 1;
    public static int universalCounter = 0;
   // public static  long acvtime = 0;
    public static String sadResults = "/storage/emulated/0/DCIM/Screenshots/results.txt";
    public  static  BufferedOutputStream fos = null;
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
        ip = (EditText) findViewById(R.id.address);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        spath = (EditText) findViewById(R.id.server_pth);
        dest = (EditText) findViewById(R.id.dest);
        ip.setText("199.71.215.197");
        username.setText("demo-user");
        password.setText("demo-user");
       // spath.setText("/storage/emulated/0/DCIM/Screenshots/sizes/32768.txt");
        spath.setText("4096");
        dest.setText("/upload/mazhar.sh");
        hcomecc = false;
        ftps = false;
        // ask for permissions.
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        cur = getApplicationContext();

        try {
            fos = new BufferedOutputStream(new FileOutputStream(sadResults));
            String header = "method , size , time\n";
            fos.write(header.getBytes());
        }catch (Exception ex){
             Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void up(View v) {
        String address = ip.getText().toString();
        String u = username.getText().toString();
        String p = password.getText().toString();
        String des = dest.getText().toString();
        String spathh = spath.getText().toString();


        String inputFileName = "/storage/emulated/0/DCIM/Screenshots/sizes/"+spathh+".sh";
        if(spathh.equals("-1")){ // send summary bad design will work on latter improve this.
            try {
                fos.close();
                des = "/home/mazharul-islam/Lab/summary.mobie.txt";
                new PCUP(false,spathh,0,getApplicationContext()).execute("192.168.0.104", "mazharul-islam", "r", sadResults, des);
                return ;
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                return ;
            }
        }

        //acvtime = 0;
        //long s = System.currentTimeMillis();
        if(hcomecc == true) {
            try {
                Huffman huffman = new Huffman();
                for (int i = 0; i < times; i++) {
                    double t = huffman.compress(inputFileName, inputFileName + ".zip");
                    t += huffman.deCompress(inputFileName + ".zip", inputFileName + ".decompress");
                    //Toast.makeText(getApplicationContext(), ("Compressed and decompressed in = " + t + " s"), Toast.LENGTH_LONG).show();
                    new PCUP(true, spathh, t, getApplicationContext()).execute(address, u, p, inputFileName+".zip", des);
                }
            }catch(Exception ex){
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
        }

        else if(ranked == true){
            try {
                RankOrderHuffman huffman = new RankOrderHuffman();
                for (int i = 0; i < times; i++) {
                    double t = huffman.compress(inputFileName, inputFileName + ".zip");
                    t += huffman.deCompress(inputFileName + ".zip", inputFileName + ".decompress");
                    //Toast.makeText(getApplicationContext(), ("Compressed and decompressed in = " + t + " s"), Toast.LENGTH_LONG).show();
                    new PCUP(true, spathh, t, getApplicationContext()).execute(address, u, p, inputFileName+".zip", des);
                }
            }catch(Exception ex){
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else{
            // full encryption
            try{
                FullEncryption fullEncryption = new FullEncryption(inputFileName);
                for(int i=0;i<times;i++) {
                    double t = fullEncryption.encrypt();
                    t += fullEncryption.decrypt();
                    new PCUP(true, spathh, t, getApplicationContext()).execute(address, u, p, inputFileName+".encrypted", des);
                   // Toast.makeText(getApplicationContext(), ("Full Encryption in = " + t + " s"), Toast.LENGTH_LONG).show();
                }
            }catch(Exception ex){
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
        }




    }
    public void showToast(String str){
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }

    public void down(View v) {
        String address = ip.getText().toString(), u = username.getText().toString(), p = password.getText().toString(), des = dest.getText().toString();
        Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
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


              //      if(hcomecc == true) {
                        time = client.FTPUP(str[3],str[4]);
                //    }
                //    else if(ranked == true){
                 //       time = client.FTPUP(str[3],str[4]);
                //    }
                 //   else{
                 //       time = client.FTPSUP(str[3],str[4]);
                 //   }
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
                String line = "size = "+size+" time = "+(time+ctime)+"\n";
                // String line = "FTPS" + ","+ size + ","+ (time+ctime)+"\n";
                //  String line = "classicalHuffman+HuffmanTreeEncryption" + ","+ size + ","+ (time+ctime)+"\n";
                //String line = "Ranked+HuffmanTreeEncryption" + "," + size + "," + (time + ctime) + "\n";
                //   String line = "classicalHuffman+CompressedFileEncryption" + ","+ size + ","+ (time+ctime)+"\n";
                fos.write(line.getBytes());
                Toast.makeText(context, line, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}



