package test.soc365.society365;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rollbar.android.Rollbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.login.Login;
import test.soc365.society365.maneger.Decompress;
import test.soc365.society365.maneger.DownloadFileAsync;
import test.soc365.society365.maneger.MainActivity;
import test.soc365.society365.staticurl.StaticUrl;
import test.soc365.society365.user.Home;

public class EmptyActivity extends AppCompatActivity {

    Button button,button2,next;
    String bothret="false";
    // Database Helper
    DatabaseHelper db;
    Rollbar rollbar;

    //progress diaolog

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    private long fileSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        db = new DatabaseHelper(getApplicationContext());
        Rollbar.init(this);
         rollbar  = Rollbar.instance();
        int currentVer = android.os.Build.VERSION.SDK_INT;
        String currentVer2 = Build.VERSION.RELEASE;

        Log.d("currentVer",String.valueOf(currentVer)+" "+ currentVer2+" "+ Build.MODEL );
        rollbar.debug(String.valueOf(currentVer)+" "+currentVer2+" "+ Build.MODEL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            //rollbar.debug("Permission is already available");
            Toast.makeText(getApplicationContext(), "Permission is already available", Toast.LENGTH_SHORT).show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            // Permission is missing and must be requested.
            //  requestCameraPermission();
        }
        button =(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);
        next = (Button)findViewById(R.id.next);
       // login();
    //    final Login login = new Login();

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              String rtn = downloadAndUnzipContent();
               if(rtn.equals("true"))
               {
                   bothret = "true";
                   button.setText("Successfully Downloaded");
                   button2.setVisibility(View.VISIBLE);
                   Toast.makeText(getApplicationContext()," Successfully Downloaded",Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(getApplicationContext(),"Downlo.. Cancel",Toast.LENGTH_SHORT).show();
               }
           }
       });
        button2.setVisibility(View.VISIBLE);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog(view);

            }
        });
        next.setVisibility(View.VISIBLE);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmptyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });




    }
   /* private void login()
    {
        String uemail = "gopalheights@society365.in";
        String pass = "Soc365@9908";
        String login = StaticUrl.login+
                "?email="+uemail+
                "&password="+pass+
                "&token=197dsfsdf"+
                "&user_type="+1;
        //String Loginurl="http://makonlinesolutions.com/aditi/societymgt/api/login.php?email=aditichavan1111@gmail.com
        // &password=L76HRehF&token=197dsfsdf&user_type=1";
        Log.d("loginurl", login);


        if (Connectivity.isConnected(getApplicationContext())){

            final StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responsenew30", response);

                            Toast.makeText(getApplicationContext(), "Successfully Downloaded ", Toast.LENGTH_SHORT).show();
                            try {

                                // datainsert_create();
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.getString("status").equals("1"))
                                {

                                    JSONArray jsonArray1=jsonObject.getJSONArray("user");
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(0);

                                    String ufname,ulname,societyid,uemail,usrid;
                                    ufname=jsonObject1.getString("first_name");
                                    ulname=jsonObject1.getString("last_name");
                                    societyid=jsonObject1.getString("society_id");
                                    usrid=jsonObject1.getString("user_id");
                                    uemail=jsonObject1.getString("email");

                                    textView.setText(ufname+" "+ulname+" "+societyid+" "+uemail+" "+usrid);


                                } else
                                {
                                    Log.d("msg", jsonObject.getString("message"));
                                    String msg = jsonObject.getString("message");
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getApplicationContext(),
                                "error_network_timeout",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        //TODO
                        Toast.makeText(getApplicationContext(),
                        "AuthFailureError",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        //TODO
                        Toast.makeText(getApplicationContext(),
                                "ServerError",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        //TODO

                        Toast.makeText(getApplicationContext(),
                                "NetworkError",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        //TODO
                        Toast.makeText(getApplicationContext(),
                                "ParseError",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
            );
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }*/

    public String downloadAndUnzipContent(){
        String ret = null;
        //   File dir = new File(Environment.getExternalStorageDirectory() +"/User_10076");
        File sdcard = Environment.getExternalStorageDirectory();
        File f=new File(sdcard+"/User_10076");
        f.mkdir();
        try{
           /*

            company_id_2019-05-07_02-15-37
            company_id_2019-05-12_22-06-34
            company_id_2019-06-07_15-53-52
            https://web.society365.in/service/company_id_2019-06-08_11-05-01.zip
            http://150.242.14.196:8012/society/service/company_id_2019-06-08_11-05-01.zip*/
            String url = "http://150.242.14.196:8012/society/service/company_id_2019-06-08_11-25-50.zip";
            DownloadFileAsync download = new DownloadFileAsync("/sdcard/company_id_2019-06-08_11-25-50.zip", this, new DownloadFileAsync.PostDownload(){
                @Override
                public void downloadDone(File file) {
                    Log.i("zipexample", "file download completed");


                    rollbar.debug("file download completed");
                    // check unzip file now
                    Decompress unzip = new Decompress(EmptyActivity.this, file);
                    unzip.unzip();
                    rollbar.debug("file unzip completed");
                    Log.i("zipexample2", "file unzip completed");


                }
            });
            download.execute(url);
            ret ="true";
        }catch(Exception e){
            ret ="false";
            e.printStackTrace();
        }
        return ret;
        //getDataExample();
    }
    public String insertDataExample()
    {
        String one="";
        String ret = " ";
        //Find the directory for the SD Card using the API
//*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStorageDirectory();

//Get the text file
        File file = new File(sdcard,"User_10076");

        File list[] = file.listFiles();
        File file1;

        try {
            rollbar.debug("filelocation:"+ sdcard+ "/User_10076/");
            rollbar.debug("filelength:"+ list.length);

            Cursor cursor = SQLiteDatabase.openOrCreateDatabase(":memory:", null).rawQuery("select sqlite_version() AS sqlite_version", null);
            String sqliteVersion = "";
            while(cursor.moveToNext()){
                sqliteVersion += cursor.getString(0);
            }
            rollbar.debug("sqliteVersion:"+sqliteVersion);


            for( int i=0; i< list.length; i++)
            {

                String insertStmt = null;
                     file1 =  new File(sdcard+ "/User_10076/" +list[i].getName());
                    rollbar.debug("1filename:"+ file1);
                BufferedReader insertReader = new BufferedReader(new FileReader(file1));

                while (insertReader.ready()) {
                     insertStmt = insertReader.readLine();
                }


                    String[] separated = insertStmt.split("values ");
                     one =  separated[0]; // this will contain "Insert statement"
                     String two =  separated[1]; // this will contain " all values"

                if (separated[1].contains("), ("))
                {
                   rollbar.debug("have brakets: "+android.os.Build.VERSION.SDK_INT+" "+Build.VERSION.RELEASE+" "+ Build.MODEL+one);

                    two = two.substring(0, two.length() - 1);

                    int count = 0;
                    Pattern p = Pattern.compile("\\), \\(");
                    Matcher m = p.matcher(two);
                    while (m.find()) {
                        count++;
                    }
                    rollbar.debug("3countlength:" + count+one);
                  /*  if (count > 300) {
                        ret = "true";
                        int insertCount = db.insertFromDirectFile(this, file1);
                    } else {*/
                        List<String> listresult = new ArrayList<String>(Arrays.asList(p.split(two)));
                   /* for(int w=1999;w<2002;w++)
                    {
                        rollbar.debug("volitr:  "+listresult.get(w));
                    }*/

                        int n = 300;
                        int t = 300;
                        int lastIndex = 0;

                        HashMap<Integer, List<String>> hashList = new HashMap<Integer, List<String>>();
                        List<String> listr = new ArrayList<String>();
                        for (int k = 0; k < listresult.size(); k++) {
                            if (k < t) {
                                if (hashList.containsKey(t)) {
                                    hashList.get(t).add(listresult.get(k));
                                } else {
                                    listr = new ArrayList<String>();
                                    listr.add(listresult.get(k));
                                    hashList.put(t, listr);
                                }
                            } else {
                                if (hashList.containsKey(t)) {

                                    hashList.get(t).add(listresult.get(k));
                                }

                                t = t + 300;
                                lastIndex = t;
                            }
                        }
                        Log.d("insertDataExample:test ", +hashList.size() + "::::" + listresult.size());
               /*String str2 = "";
                for (Map.Entry<Integer,List<String>> entry2 : hashList.entrySet()) {
                    str2 = "";
                    rollbar.debug("lastIndex = " + " "+ lastIndex);
                    if(entry2.getKey()==lastIndex) {
                        rollbar.debug("lastIndex 1= " + " "+ lastIndex);
                        for (int a = 0; a < entry2.getValue().size(); a++) {
                            if(entry2.getValue().get(a).contains("),"))
                            {
                                rollbar.debug("lastIndex conatains= " + " "+ lastIndex);
                                str2 = entry2.getValue().get(a).replace("),","");
                                entry2.getValue().set(a,str2);
                            }


                        }
                    }
                }*/

                        String str1 = "";
                        String lv_str = "";
                        HashMap<Integer, String> hashStr = new HashMap<Integer, String>();
                        for (Map.Entry<Integer, List<String>> entry : hashList.entrySet()) {
                            //System.out.println("Key1 = " + entry.getKey() + " :size:" + entry.getValue());
                            // rollbar.debug("hamza = " + " "+ entry.getKey() + " :size:" + entry.getValue());
                            str1 = "";
                            for (int a = 0; a < entry.getValue().size(); a++) {
                                if (entry.getKey().equals(300)) {
                                    if (a == 0) {
                                        str1 = str1.concat(("".concat(entry.getValue().get(a)).concat("), ")));
                                        hashStr.put(entry.getKey(), str1);

                                    } else if (a == entry.getValue().size() - 1) {
                                        str1 = str1.concat(("(".concat(entry.getValue().get(a)).concat(")")));
                                        hashStr.put(entry.getKey(), str1);

                                    } else {
                                        str1 = str1.concat(("(".concat(entry.getValue().get(a)).concat("), ")));
                                        hashStr.put(entry.getKey(), str1);
                                    }
                                } else if (a == entry.getValue().size() - 1) {
                                    str1 = str1.concat(("(".concat(entry.getValue().get(a)).concat(")")));
                                    hashStr.put(entry.getKey(), str1);

                                } else {
                                    str1 = str1.concat(("(".concat(entry.getValue().get(a)).concat("), ")));
                                    hashStr.put(entry.getKey(), str1);
                                }
                            }
                        }


                        for (Map.Entry<Integer, String> entry1 : hashStr.entrySet()) {
                            //System.out.println("Key = " + entry1.getKey() + " :size:" + entry1.getValue());
                            rollbar.debug("jkob = " + " " + entry1.getKey() + " :size:" + entry1.getValue());
                            String inst = one + "values " + entry1.getValue();
                            ret = "true";
                            db.insertFromFile(this, inst);
                        }


                   /* do{
                   //     Log.d("insertDataExample:a ",listresult.size() +" " +listresult);

                        int count500= 1;
                        StringBuilder valuesBuilder = new StringBuilder();

                        if(listresult.size()>500) {
                            for (int j = 0; j < n; j++) {
                                if(listresult.size()<940)
                                {
                                    Log.d("insertDataExample:d ","j::"+j+"   :size:"+listresult.size() );
                                }
                                if(j!=listresult.size()) {

                                    valuesBuilder.append(listresult.get(j));
                                    valuesBuilder.append("), (");
                                    listresult.remove(j);
                                }


                                if (j == 499) {
                                   // Toast.makeText(getApplicationContext(), "insertdataat500row", Toast.LENGTH_SHORT).show();
                                    rollbar.debug(" b " + " " + valuesBuilder);
                                  //  Log.d("insertDataExample:b ", String.valueOf(valuesBuilder));
                                }
                            }
                        }
                   // Log.d("insertDataExample:c ",listresult.size() +" " +listresult);
                        count500++;
                    }
                    while (listresult.size()>n);
*/
                   /* String insertdirectly =  result[0]; // this will contain "Fruit"
                    String two3 =  result[result.length-1]; // this will contain " they taste good"
                    rollbar.debug("in1c f:  "+insertdirectly);
                    rollbar.debug("in2c f:  "+two3);

                    rollbar.debug("Data in2 count2=:  "+count+" "+one);
*/
/*
                    }*/
                }
                else {
                    rollbar.debug("nonbracket:  "+android.os.Build.VERSION.SDK_INT+" "+Build.VERSION.RELEASE+" "+ Build.MODEL+one);
                ret = "true";
                int insertCount = db.insertFromDirectFile(this,file1);
                     }

                        insertReader.close();


                //  myList.add( list[i].getName() );
            }
            rollbar.debug("Data Inserted Successfully");

        }
        catch (Exception e)
        {
            ret ="false";
            Log.e("ExceptionInsert","exception",e);
            /*rollbar  = Rollbar.instance();
            rollbar.error(e);*/
        }





/*//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

//Find the view by its id
        TextView tv = (TextView)findViewById(R.id.text_ex);

//Set the text
        tv.setText(text.toString());*/
       /* try {
            db = new DatabaseHelper(getApplicationContext());
            int insertCount = db.insertFromFile(this, file);
            Toast.makeText(this, "Rows loaded from file= " + insertCount, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/
        return ret;
    }


    public void startProgressDialog(View V)
    {


        // prepare for a progress bar dialog
        progressBar = new ProgressDialog(V.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Downloading File...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        //reset progress bar status
        progressBarStatus = 0;

        //reset filesize
        fileSize = 0;

        new Thread(new Runnable() {
            public void run() {
                if(progressBarStatus==-100)
                {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"NOT INSERTED",Toast.LENGTH_SHORT).show();

                        }
                    });
                    // close the progress bar dialog
                    progressBar.dismiss();
                }
                while (progressBarStatus < 100) {
                    if(progressBarStatus==60) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), " INSERTED", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process some tasks
                    progressBarStatus = fileDownloadStatus();

                    //  sleep 1 second to show the progress
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }

                // when, file is downloaded 100%,
                if (progressBarStatus >= 100) {

                    // sleep 2 seconds, so that you can see the 100% of file download
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // close the progress bar dialog
                    progressBar.dismiss();
                }
            }
        }).start();

    }
    //method returns the % of file downloaded
    public int fileDownloadStatus()
    {

        while (fileSize <= 1000000) {

            fileSize++;

            if (fileSize == 100000) {
                return 10;
            } else if (fileSize == 200000) {
                return 20;
            } else if (fileSize == 300000) {
                return 30;
            } else if (fileSize == 400000) {
                return 40;
            } else if (fileSize == 500000) {
                return 50;
            } else if (fileSize == 600000) {
                String rtn = insertDataExample();
                if(rtn.equals("true"))
                {

                 //   button2.setText("INSERTED");
                   // next.setVisibility(View.VISIBLE);
                     return 60;
                }
                else{
                  //  Toast.makeText(getApplicationContext(),"NOT INSERTED",Toast.LENGTH_SHORT).show();
                    return -100;
                }

            }

            // write your code here

        }

        return 100;

    }




}
