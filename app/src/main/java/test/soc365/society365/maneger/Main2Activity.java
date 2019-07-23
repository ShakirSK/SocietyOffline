package test.soc365.society365.maneger;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import test.soc365.society365.R;
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.maneger.partyledger.partymodel;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Main2Activity extends AppCompatActivity {
    private long enqueue;
    private DownloadManager dm;
    // Database Helper
    DatabaseHelper db;
    BroadcastReceiver receiver;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        db = new DatabaseHelper(getApplicationContext());

      /*   receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            Uri a = Uri.parse(uriString);
                            File d = new File(a.getPath());
                            // copy file from external to internal will esaily avalible on net use google.
                       //     view.setImageURI(a);


*//*
                            Intent share = new Intent(getApplicationContext(),showpdftest_file.class);
                            // set flag to give temporary permission to external app to use your FileProvider
                            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri apkURI = FileProvider.getUriForFile(
                                    context,
                                    BuildConfig.APPLICATION_ID  + ".myfileprovider", d);
                            share.setDataAndType(apkURI, "application/pdf");
                            share.putExtra(Intent.EXTRA_STREAM, apkURI);
                            share.setAction(Intent.ACTION_SEND);

                            startActivity(share);*//*
                            Intent share = new Intent(context,showpdftest_file.class);
                            // set flag to give temporary permission to external app to use your FileProvider
                            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri apkURI = FileProvider.getUriForFile(
                                    context,
                                    BuildConfig.APPLICATION_ID  + ".myfileprovider", d);
                            Log.d("pdfmain2", apkURI.toString());
                           share.putExtra("URL", apkURI.toString());

                            startActivity(share);

                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));*/
    }

    public void onClick(View view) {
        int SHARE_PERMISSION_CODE = 223;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MarshMallowPermission.checkMashMallowPermissions(this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    SHARE_PERMISSION_CODE)) {

           /*     String backupDBPath = Environment.getExternalStorageDirectory().getPath() + "/unzipped";
                File sd = Environment.getExternalStorageDirectory();
                File backupDBFoldera = new File(backupDBPath);
                if (sd.canWrite()) {
                    final File backupDBFolder = new File(backupDBFoldera, "/query.zip");
                    try {
                        unzip(backupDBPath, backupDBFolder.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
                downloadAndUnzipContent();

            }

        }
           /* if (MarshMallowPermission.checkMashMallowPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, SHARE_PERMISSION_CODE);
        }*/

    }

    public void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }

    public void insertDataExample(View view)
    {
        //Find the directory for the SD Card using the API
//*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStorageDirectory();

//Get the text file
        File file = new File(sdcard,"User_10076");

        File list[] = file.listFiles();
/*
        for( int i=0; i< list.length; i++)
        {
            Log.d("fileexampel", "file:"+ list[i].getName());
             try {
            File file1 =  new File(sdcard+ "/User_10076/" +list[i].getName());
          db.insertFromFile(file1 );
            } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
          //  myList.add( list[i].getName() );
        }*/




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
    }

    public void getDataExample(View view)
    {
        // Fetching user details from sqlite
       /* HashMap<String, String> user = db.getUserDetails();*/
       db.getdata(getApplicationContext());
        // Getting all Todos
        Log.d("Get Todos", "Getting All ToDos");

        List<partymodel> allToDos = db.getdata(getApplicationContext());
        for (partymodel todo : allToDos) {
            Log.d("ToDo", todo.getTitle());
        }
    }

    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }

    }
    private void downloadAndUnzipContent(){
     //   File dir = new File(Environment.getExternalStorageDirectory() +"/User_10076");
        File sdcard = Environment.getExternalStorageDirectory();
        File f=new File(sdcard+"/User_10076");
        f.mkdir();
        try{
            if(f.mkdir()) {
                Log.i("zipexample", "file  completed");
            } else {
                Log.i("zipexample", "file not  completed");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        String url = "http://150.242.14.196:8012/society/service/company_id_2019-05-03_16-30-19.zip";
        DownloadFileAsync download = new DownloadFileAsync("/sdcard/company_id_2019-05-03_16-30-19.zip", this, new DownloadFileAsync.PostDownload(){
            @Override
            public void downloadDone(File file) {
                Log.i("zipexample", "file download completed");

                // check unzip file now
                Decompress unzip = new Decompress(Main2Activity.this, file);
                unzip.unzip();

                Log.i("zipexample2", "file unzip completed");
            }
        });
        download.execute(url);
    }
}