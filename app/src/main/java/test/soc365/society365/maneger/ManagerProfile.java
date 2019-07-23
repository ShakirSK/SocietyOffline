package test.soc365.society365.maneger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.R;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.maneger.VolleyMultipartRequest.VolleyMultipartRequest;
import test.soc365.society365.staticurl.StaticUrl;
import test.soc365.society365.user.userprofile.edit_profile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ManagerProfile extends AppCompatActivity {

    TextView firstname,lastname,email,mobile,buildingno,flatstatus,dateofjoining,password,emid,flatno,soname,soaddress;
    ImageView profile,edit,editprofile;
    String userid,emailid,pass,building,flat,firstname_str,lastname_str,mobile_str,dateofjoining_str,emid_str,societyname,societyaddress;
   String ufname,ulname,uemail,mno;
    FloatingActionButton uploadimg;
    SpotsDialog dialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog=new SpotsDialog(getApplicationContext(),R.style.Custom);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("USERID","0" );
        societyname=sharedPreferences.getString("sname", "0");
        societyaddress=sharedPreferences.getString("saddress", "0");

        ufname = sharedPreferences.getString("firstname","0" );
        ulname = sharedPreferences.getString("lastname","0" );
        uemail= sharedPreferences.getString("email","0");
        mno= sharedPreferences.getString("mobile","0");

        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lstname);
        mobile=findViewById(R.id.mobile);
        edit=findViewById(R.id.edit);
       //buildingno=findViewById(R.id.buildingno);
       // flatstatus=findViewById(R.id.flatstatus);
        dateofjoining=findViewById(R.id.dateofjoining);
        emid=findViewById(R.id.email);
        profile=findViewById(R.id.profile);
        uploadimg=findViewById(R.id.uploadimg);
        editprofile=findViewById(R.id.editprof);
        soname=findViewById(R.id.zone);
        soaddress=findViewById(R.id.address);


        emid.setText(uemail);
        soname.setText(societyname);
        soaddress.setText(societyaddress);
        firstname.setText(ufname);
        lastname.setText(ulname);
        mobile.setText(mno);

        //flatno=findViewById(R.id.flatno);

        //final Intent arg=getIntent();

       // buildingno.setText("Building No: "+arg.getStringExtra("building"));
        //flatno.setText("Flat No: "+arg.getStringExtra("flat"));
        //flatstatus.setText(arg.getStringExtra("flatstatus"));

        //buildingno.setText(arg.getStringExtra("building" +
             //   "."));
        //flatno.setText(arg.getStringExtra("flat"));
        //userid=arg.getStringExtra("userid");

         editprofile.setOnClickListener(new View.OnClickListener()
         {
            @Override
            public void onClick(View view)
            {
                Intent  intent=new Intent(getApplicationContext(), edit_profile.class);
                intent.putExtra("editstatus", "2");
                intent.putExtra("firstname", firstname_str);
                intent.putExtra("lastname", lastname_str);
                intent.putExtra("mobileno", mobile_str);
                intent.putExtra("emailid", emailid);
                intent.putExtra("memberstartdate", dateofjoining_str);
                intent.putExtra("buildingno",building);
                intent.putExtra("flatno", flat);
                intent.putExtra("userid", userid);
                intent.putExtra("usertype", "1");
                //intent.putExtra("profilepic", profilepic"));
                startActivity(intent);
            }
        });

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        getprofile();
    }

    @Override
    protected void onResume() {
        getprofile();
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null)
        {
            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                //displaying selected image to imageview
                profile.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     }

    private void uploadBitmap(final Bitmap bitmap)
    {
        String targeturl = StaticUrl.profileimg;

        Log.d("updateprofimg",targeturl);

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,targeturl,
                new Response.Listener<NetworkResponse>() {

                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(response.data));
                            //JSONObject obj = new JSONObject(new String(response.data));
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if(jsonObject.getString("status").equals("1"))
                            {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                getprofile();
                            }else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                //params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                params.put("image",new DataPart(getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);
    }

    //This is for image conversion FOR pdf
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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



    private void getprofile()
    {
        String profileurl=StaticUrl.getprofile+userid;

        if (Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest stringRequest=new StringRequest(Request.Method.GET,
                    profileurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("profileurl", response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (jsonObject.getString("status").equals("1"))
                                {
                                    Log.d("audit_status", jsonObject.getString("status"));

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("profile");
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                                    building=jsonObject1.getString("building_no");
                                    flat=jsonObject1.getString("flat_no");
                                    firstname_str=jsonObject1.getString("first_name");
                                    lastname_str=jsonObject1.getString("last_name");
                                    mobile_str =jsonObject1.getString("mobile_no");
                                    dateofjoining_str=jsonObject1.getString("join_date");
                                    emid_str=jsonObject1.getString("email");

                                    /*if (jsonObject1.getString("flat_status").equals("1"))
                                        flatstatus.setText("Active");
                                    else
                                        flatstatus.setText("Inactive");*/

                                    //userid=jsonObject1.getString("user_id");

                                    //societyname.setText(arg.getString("societyname"));
                                    String profurl = jsonObject1.getString("profile_pic_url");

                                    Picasso.with(getApplicationContext()).load(profurl).placeholder(R.drawable.ic_person_black_24dp).error(R.drawable.ic_person_black_24dp).into(profile);
                                    firstname.setText(firstname_str);
                                    lastname.setText(lastname_str);
                                    mobile.setText(mobile_str);
                                    dateofjoining.setText(dateofjoining_str);
                                    emid.setText(emid_str);
                                } else {
                                   // Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();

                                }

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
    }
}
