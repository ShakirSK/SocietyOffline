package test.soc365.society365.maneger.MomReport;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.R;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.maneger.VolleyMultipartRequest.FilePath;
import test.soc365.society365.staticurl.StaticUrl;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import dmax.dialog.SpotsDialog;


public class MomFragment extends AppCompatActivity
{

    ImageView delete,attach;
    Button upload;
    EditText title,pdfname;
    TextView messtxt;
    String usertype,filePath,societyid,userid;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MomModel> momModelArrayList;
    SpotsDialog spotsDialog;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    int progressStatusCounter = 0;
    Handler progressHandler = new Handler();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.momfragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        delete=findViewById(R.id.delete);
        upload=findViewById(R.id.btn);
        title=findViewById(R.id.title);
        attach=findViewById(R.id.attach);
        pdfname=findViewById(R.id.uplodimg);
        messtxt=findViewById(R.id.messtxt);
        progressBar=findViewById(R.id.progressbar);
        spotsDialog=new SpotsDialog(this,R.style.Custom);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        momModelArrayList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MomAdapter(getApplicationContext(),momModelArrayList);
        recyclerView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("USERID","0" );
        societyid = sharedPreferences.getString("SOCIETYID","0" );
        usertype = sharedPreferences.getString("USERTYPE","0" );

        /*usertype=getIntent().getStringExtra("usertype");
        societyid=getIntent().getStringExtra("societyid");
        userid=getIntent().getStringExtra("userid");*/

        momlist();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               validation();
            }
        });
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"), 200);
            }
        });

        requestStoragePermission();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 200)
            {
                Uri picUri = data.getData();

                filePath = FilePath.getPath(getApplicationContext(),picUri);//getPath(picUri);

                Log.d("picUri", picUri.toString());
                Log.d("filePath", filePath);
                String displayName = null;
                File myFile = new File(filePath);
                displayName = myFile.getName();
                pdfname.setText(displayName);

            }
        else {
            Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_SHORT).show();
        }
    }

    private void validation()
    {
        if (title.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Enter Title",Toast.LENGTH_SHORT).show();
        }else if(pdfname.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Attach the file",Toast.LENGTH_SHORT).show();

        } else
        {
            if (filePath != null) {
                uploadmom(filePath);
            } else {
                Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadmom(String filePath)
    {

        String targeturl = StaticUrl.momreport;
        Log.d("momurl",targeturl);

        if (Connectivity.isConnected(getApplicationContext()))
        {
           // progressDialog = ProgressDialog.show(MomFragment.this,"File is Uploading","Please Wait",false,false);
            /*progressDialog = new ProgressDialog(MomFragment.this);
            progressDialog.setMessage("File is Uploading");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
            */
            progressBar.setVisibility(View.VISIBLE);
            //Start progressing
            new Thread(new Runnable() {
                public void run() {
                    while (progressStatusCounter < 100) {
                        progressStatusCounter += 2;
                        progressHandler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progressStatusCounter);
                            }
                        });
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            try
                {
                    String uploadId = UUID.randomUUID().toString();

                    //Creating a multi part request
                    new MultipartUploadRequest(this, uploadId, targeturl)
                            .addFileToUpload(filePath, "attachment") //Adding file
                            .addParameter("title", title.getText().toString()).setUtf8Charset()
                            .addParameter("society_id", societyid)
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(Context context, UploadInfo uploadInfo) {

                                }

                                @Override
                                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                                }

                                @Override
                                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    popup();
                                }

                                @Override
                                public void onCancelled(Context context, UploadInfo uploadInfo) {

                                }
                            })
                            .startUpload(); //Starting the upload
                  // Toast.makeText(getApplicationContext(),"File Uploaded", Toast.LENGTH_SHORT).show();

                    //messtxt.setText("File Uploaded");
                } catch (Exception exc) {
                   // Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Exception", exc.getMessage());
                }
        }else {
            Toast.makeText(getApplicationContext(), "Check internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void momlist()
    {
        String momlist= StaticUrl.momlist+"?society_id="+societyid;

        Log.d("momlisturl",momlist);

        if (Connectivity.isConnected(getApplicationContext()))
        {

            spotsDialog.show();

            momModelArrayList.clear();

            StringRequest stringRequest=new StringRequest(Request.Method.GET,
                    momlist,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("momresponse", response);

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.getString("status").equals("1"))
                                {
                                    Log.d("amom_status", jsonObject.getString("status"));

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("momList");
                                    Log.d("mom_jsonArray1", String.valueOf(jsonObject.getJSONArray("momList")));

                                    for (int i = 0; i < jsonArray1.length(); i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                        MomModel mlist = new MomModel();
                                        mlist.setMom_id(jsonObject1.getString("mom_id"));
                                        mlist.setTitle(jsonObject1.getString("title"));
                                        mlist.setAttachment(jsonObject1.getString("attachment"));
                                        mlist.setCreated_on(jsonObject1.getString("created_on"));
                                        momModelArrayList.add(mlist);
                                    }
                                    Log.d("audit_arraylist", momModelArrayList.toString());

                                    adapter.notifyDataSetChanged();

                                } else {
                                //    Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();

                                }
                               // spotsDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }else {
            Toast.makeText(getApplicationContext(), "Check internet Connection!", Toast.LENGTH_SHORT).show();
        }
        spotsDialog.dismiss();
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//If the user has denied the permission previously your code will come to this block
//Here you can explain why you need this permission
//Explain here why you need this permission
        }
//And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

//Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

//If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
//Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void popup()
    {
        title.setText("");
        pdfname.setText("");

        final Dialog dialog = new Dialog(MomFragment.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_memberadd);

        //TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        // text.setText(msg);
        TextView mesgtxt = dialog.findViewById(R.id.skill);
        mesgtxt.setText("File Uploaded Successfully");
        Button dialogButton = dialog.findViewById(R.id.donebtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                momlist();
            }
        });
        dialog.show();

    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
