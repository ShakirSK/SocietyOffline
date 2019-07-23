package test.soc365.society365.user.userprofile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class u_MyProfile extends Fragment
{

    TextView fullname,email,mobile,buildingno,flatstatus,dateofjoining,password,emid,flatno,address,soname,soaddress;
    ImageView profile,edit;
    String emailid,pass,userid,flatid,building,societyname,societyaddress;
    FloatingActionButton uploadimg;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String flat,firstname_str,lastname_str,mobile_str,dateofjoining_str,emid_str,societynamestr;
    String ufname,ulname,uemail,mno,buildingn,flatn;
    ImageView emailicon;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view1=inflater.inflate(R.layout.u_myprofile,container,false);

        fullname=view1.findViewById(R.id.fullname);
        mobile=view1.findViewById(R.id.mobile);
        buildingno=view1.findViewById(R.id.buildingno);
        flatstatus=view1.findViewById(R.id.flatstatus);
        dateofjoining=view1.findViewById(R.id.dateofjoining);
        emid=view1.findViewById(R.id.email);
        profile=view1.findViewById(R.id.profile);
        uploadimg=view1.findViewById(R.id.uploadimg);
        flatno=view1.findViewById(R.id.flatno);
        address=view1.findViewById(R.id.location);
        soname=view1.findViewById(R.id.societyname);
        soaddress=view1.findViewById(R.id.location);
        edit=view1.findViewById(R.id.editprof);
        emailicon = view1.findViewById(R.id.emailicon);


        sharedPreferences = getActivity().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("USERID","0" );
        societynamestr= sharedPreferences.getString("sname","0" );
        societyname=sharedPreferences.getString("sname", "0");
        societyaddress=sharedPreferences.getString("saddress", "0");


        ufname = sharedPreferences.getString("firstname","0" );
        ulname = sharedPreferences.getString("lastname","0" );
        uemail= sharedPreferences.getString("email","0");
        mno= sharedPreferences.getString("mobile","0");


        buildingn= sharedPreferences.getString("buildingno","0");
        flatn= sharedPreferences.getString("flatno","0");



        fullname.setText(ufname+" "+ulname);
        mobile.setText(mno);
        buildingno.setText("Building No: "+buildingn+"  ");
        flatno.setText("Flat No:  "+flatn);
        emid.setText(uemail);

        if(uemail.equals(""))
        {
            emid.setVisibility(View.GONE);
            emailicon.setVisibility(View.GONE);
        }
        soname.setText(societyname);
        soaddress.setText(societyaddress);


        //flatid=arg.getString("flat");
        //building=arg.getString("building");
        //if (arg.getString("flatstatus").equals("1"))
          //  flatstatus.setText("Active");
        //else
          //  flatstatus.setText("Inactive");

       //Picasso.with(getContext()).load(arg.getString("profilepic")).placeholder(R.drawable.ic_person_black_24dp).error(R.drawable.ic_person_black_24dp).into(profile);

       //Log.d("UseridProfile", arg.getString("userid"));

       //comapare();

        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent  intent=new Intent(getActivity(), edit_profile.class);
                intent.putExtra("editstatus", "2");
                intent.putExtra("usertype", "2");
                intent.putExtra("firstname", firstname_str);
                intent.putExtra("lastname", lastname_str);
                intent.putExtra("mobileno", mobile_str);
                intent.putExtra("emailid", emailid);
                intent.putExtra("memberstartdate", dateofjoining_str);
                intent.putExtra("buildingno",building);
                intent.putExtra("flatno", flat);
                intent.putExtra("userid", userid);
                startActivity(intent);
                getprofile();
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

        return view1;
    }
    @Override
    public void onResume() {
        getprofile();
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

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
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                targeturl,
                new Response.Listener<NetworkResponse>()
                {

                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        Log.d("response_imgupl", new String(response.data)+userid);
                        try {
                            JSONArray jsonArray = new JSONArray(new String(response.data));
                            //JSONObject obj = new JSONObject(new String(response.data));
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if(jsonObject.getString("status").equals("1"))
                            {
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                getprofile();
                            }else {
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("VolleyError", error.getMessage());
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
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    //This is for image conversion FOR pdf
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
    }


    private void getprofile()
    {
        String profileurl=StaticUrl.getprofile+userid;
        Log.d("profileurlprint", profileurl);

        if (Connectivity.isConnected(getActivity()))
        {

            StringRequest stringRequest=new StringRequest(Request.Method.GET,
                    profileurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("profileurl", response);

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                Log.d("imageresonse", response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (jsonObject.getString("status").equals("1"))
                                {
                                    Log.d("audit_status", jsonObject.getString("status"));

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("profile");
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                                    String profurl = jsonObject1.getString("profile_pic_url");
                                    Log.d("imageurl", profurl);
                                    Picasso.with(getContext()).load(profurl).placeholder(R.drawable.ic_person_black_24dp).error(R.drawable.ic_person_black_24dp).fit().into(profile);

                                    building=jsonObject1.getString("building_no");
                                    flat=jsonObject1.getString("flat_no");
                                    firstname_str=jsonObject1.getString("first_name");
                                    lastname_str=jsonObject1.getString("last_name");
                                    mobile_str =jsonObject1.getString("mobile_no");
                                    dateofjoining_str=jsonObject1.getString("join_date");
                                    emid_str=jsonObject1.getString("email");

                                    fullname.setText(firstname_str+" "+lastname_str);
                                    mobile.setText(mobile_str);
                                    buildingno.setText("Building No: "+building);
                                    flatno.setText("Flat No:  "+flat);
                                    dateofjoining.setText(dateofjoining_str);
                                    emid.setText(emid_str);

                                } else {
                                   // Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


        }else {
            Toast.makeText(getActivity(), "Check internet Connection!", Toast.LENGTH_SHORT).show();
        }

    }
}
